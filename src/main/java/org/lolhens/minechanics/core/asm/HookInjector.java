package org.lolhens.minechanics.core.asm;

import static org.objectweb.asm.Opcodes.AASTORE;
import static org.objectweb.asm.Opcodes.ACONST_NULL;
import static org.objectweb.asm.Opcodes.ANEWARRAY;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.BIPUSH;
import static org.objectweb.asm.Opcodes.DCONST_0;
import static org.objectweb.asm.Opcodes.DLOAD;
import static org.objectweb.asm.Opcodes.DRETURN;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.FCONST_0;
import static org.objectweb.asm.Opcodes.FLOAD;
import static org.objectweb.asm.Opcodes.FRETURN;
import static org.objectweb.asm.Opcodes.F_APPEND;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INTEGER;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.LCONST_0;
import static org.objectweb.asm.Opcodes.LLOAD;
import static org.objectweb.asm.Opcodes.LRETURN;
import static org.objectweb.asm.Opcodes.RETURN;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.dafttech.eventmanager.EventType;

public class HookInjector {
    private MethodNode methodNode;
    private LineNumberNode lineNode;
    private boolean returnTrue = false;

    public HookInjector(MethodNode methodNode, LineNumberNode lineNode) {
        this.methodNode = methodNode;
        this.lineNode = lineNode;
    }

    public HookInjector setReturnTrue() {
        returnTrue = true;
        return this;
    }

    public void inject(String eventManagerField, EventType eventType, Object... varInsns) {
        eventManagerField = eventManagerField.replace(".", "/");
        int fieldIndex = eventManagerField.lastIndexOf("/");
        if (fieldIndex == -1) return;
        InsnList toInject = new InsnList();
        toInject.add(new FieldInsnNode(GETSTATIC, eventManagerField.substring(0, fieldIndex), eventManagerField
                .substring(fieldIndex + 1), "Lcom/dafttech/eventmanager/EventManager;"));
        toInject.add(new LdcInsnNode(eventType.getName()));
        toInject.add(new MethodInsnNode(INVOKESTATIC, "com/dafttech/eventmanager/EventType", "getByName",
                "(Ljava/lang/String;)Lcom/dafttech/eventmanager/EventType;"));
        toInject.add(new IntInsnNode(BIPUSH, varInsns.length));
        toInject.add(new TypeInsnNode(ANEWARRAY, "java/lang/Object"));
        for (int i = 0; i < varInsns.length; i++) {
            toInject.add(new InsnNode(DUP));
            toInject.add(new IntInsnNode(BIPUSH, i));
            if (varInsns[i] instanceof InsnList) {
                toInject.insert((InsnList) varInsns[i]);
            } else if (varInsns[i] instanceof AbstractInsnNode) {
                toInject.add((AbstractInsnNode) varInsns[i]);
                if (varInsns[i] instanceof VarInsnNode) injectObjConv(toInject, ((AbstractInsnNode) varInsns[i]).getOpcode());
            }
            toInject.add(new InsnNode(AASTORE));
        }
        toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "com/dafttech/eventmanager/EventManager", "callSync",
                "(Lcom/dafttech/eventmanager/EventType;[Ljava/lang/Object;)Lcom/dafttech/eventmanager/Event;"));
        toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "com/dafttech/eventmanager/Event", "isCancelled", "()Z"));
        LabelNode jmpLabel = new LabelNode();
        toInject.add(new JumpInsnNode(IFEQ, jmpLabel));
        injectReturn(toInject, Type.getReturnType(methodNode.desc).getSort());
        toInject.add(jmpLabel);
        toInject.add(new LineNumberNode(lineNode.line + 1, jmpLabel));
        toInject.add(new FrameNode(F_APPEND, 2, new Object[] { INTEGER, INTEGER }, 0, null));
        methodNode.instructions.insert(lineNode, toInject);
    }

    private void injectObjConv(InsnList toInject, int opcode) {
        Type varType = null;
        if (opcode == ILOAD) {
            varType = Type.INT_TYPE;
        } else if (opcode == FLOAD) {
            varType = Type.FLOAT_TYPE;
        } else if (opcode == LLOAD) {
            varType = Type.LONG_TYPE;
        } else if (opcode == DLOAD) {
            varType = Type.DOUBLE_TYPE;
        }
        if (varType != null) {
            String typeClassName = varType.getClassName().replace("int", "integer");
            typeClassName = typeClassName.substring(0, 1).toUpperCase() + typeClassName.substring(1);
            toInject.add(new MethodInsnNode(INVOKESTATIC, "java/lang/" + typeClassName, "valueOf", "(" + varType.getDescriptor()
                    + ")Ljava/lang/" + typeClassName + ";"));
        }
    }

    private void injectReturn(InsnList toInject, int type) {
        if (type == Type.VOID) {
            toInject.add(new InsnNode(RETURN));
        } else if (type >= Type.BOOLEAN && type <= Type.INT) {
            toInject.add(new InsnNode(returnTrue ? ICONST_1 : ICONST_0));
            toInject.add(new InsnNode(IRETURN));
        } else if (type == Type.FLOAT) {
            toInject.add(new InsnNode(FCONST_0));
            toInject.add(new InsnNode(FRETURN));
        } else if (type == Type.LONG) {
            toInject.add(new InsnNode(LCONST_0));
            toInject.add(new InsnNode(LRETURN));
        } else if (type == Type.DOUBLE) {
            toInject.add(new InsnNode(DCONST_0));
            toInject.add(new InsnNode(DRETURN));
        } else if (type >= Type.ARRAY) {
            toInject.add(new InsnNode(ACONST_NULL));
            toInject.add(new InsnNode(ARETURN));
        }
    }

    public Object test() {
        return null;
    }
}
