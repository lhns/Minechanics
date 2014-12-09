package org.lolhens.minechanics.core.asm;

import net.minecraft.launchwrapper.IClassTransformer;

import org.lolhens.minechanics.core.event.Events;

import com.dafttech.eventmanager.Event;
import com.dafttech.reflect.ReflectionUtil.SingletonInstance;

public class ClassTransformer implements IClassTransformer {
    @SingletonInstance
    public ClassTransformer instance;

    public ClassTransformer() {
        instance = this;
    }

    @Override
    public byte[] transform(String name, String deobfName, byte[] basicClass) {
        Event transformEvent = Events.eventManager.callSync(Events.transform, deobfName, new DeobfTranslator(name, deobfName),
                basicClass);
        if (!transformEvent.isCancelled())
            if (transformEvent.out.size() > 0) return (byte[]) transformEvent.out.get(transformEvent.out.size() - 1);
        return basicClass;
    }

    public static void registerASMListeners() {
        Events.eventManager.tryRegisterEventListener("org.lolhens.minechanics.liquids.LiquidTransformer");
    }
}