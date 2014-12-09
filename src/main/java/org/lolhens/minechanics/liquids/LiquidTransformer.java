package org.lolhens.minechanics.liquids;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.IFLE;
import static org.objectweb.asm.Opcodes.ILOAD;

import java.util.Iterator;

import org.lolhens.minechanics.core.asm.DeobfTranslator;
import org.lolhens.minechanics.core.asm.HookInjector;
import org.lolhens.minechanics.core.event.Events;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.dafttech.eventmanager.Event;
import com.dafttech.eventmanager.EventListener;

public class LiquidTransformer {

    @EventListener.Filter("classname")
    private static String methodName = "net.minecraft.block.BlockDynamicLiquid";

    @EventListener(value = "transform", filter = "classname")
    private static void transform(Event event) {
        ClassNode classNode = event.in.getFirst(ClassNode.class);
        Iterator<MethodNode> methods = classNode.methods.iterator();
        DeobfTranslator translator = event.in.getFirst(DeobfTranslator.class);
        MethodNode method;
        String methodName;
        while (methods.hasNext()) {
            method = methods.next();
            methodName = translator.getDeobfName(method);
            if (methodName.equals(DeobfTranslator.updateTick) || methodName.equals(DeobfTranslator.getSmallestFlowDecay)
                    || methodName.equals(DeobfTranslator.flowIntoBlock)) {
                Iterator<AbstractInsnNode> instructions = method.instructions.iterator();
                AbstractInsnNode instruction;
                LineNumberNode lastLine = null;
                @SuppressWarnings("unused")
                int index = -1;
                while (instructions.hasNext()) {
                    index++;
                    instruction = instructions.next();
                    if (instruction instanceof LineNumberNode) lastLine = (LineNumberNode) instruction;
                    if (methodName.equals(DeobfTranslator.updateTick) && instruction.getOpcode() == IFLE && lastLine != null) {
                        new HookInjector(method, lastLine).inject(Events.class.getName() + ".eventManager", Events.water,
                                new VarInsnNode(ALOAD, 1), new VarInsnNode(ILOAD, 2), new VarInsnNode(ILOAD, 3), new VarInsnNode(
                                        ILOAD, 4), new VarInsnNode(ALOAD, 0));
                        break;
                    }
                    if (methodName.equals(DeobfTranslator.getSmallestFlowDecay) && instruction.getOpcode() == GETFIELD
                            && lastLine != null) {
                        new HookInjector(method, lastLine).inject(Events.class.getName() + ".eventManager",
                                Events.waterCountForNewSource, new VarInsnNode(ALOAD, 1), new VarInsnNode(ILOAD, 2),
                                new VarInsnNode(ILOAD, 3), new VarInsnNode(ILOAD, 4), new VarInsnNode(ALOAD, 0));
                        break;
                    }
                    if (methodName.equals(DeobfTranslator.flowIntoBlock) && instruction.getOpcode() == GETSTATIC) {
                        new HookInjector(method, lastLine).setReturnTrue().inject(Events.class.getName() + ".eventManager",
                                Events.waterBlocksMovement, new VarInsnNode(ALOAD, 1), new VarInsnNode(ILOAD, 2),
                                new VarInsnNode(ILOAD, 3), new VarInsnNode(ILOAD, 4), new VarInsnNode(ALOAD, 0),
                                new VarInsnNode(ALOAD, 5));
                        break;
                    }
                }
            }
        }
    }
}
