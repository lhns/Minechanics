package org.lolhens.minechanics.core.event;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import com.dafttech.eventmanager.Event;
import com.dafttech.eventmanager.EventManager;
import com.dafttech.eventmanager.EventType;
import com.dafttech.eventmanager.ListenerContainer;
import com.dafttech.storage.tuple.Tuple;
import com.dafttech.storage.tuple.TupleFacade;

public class Events {
    public static final EventManager eventManager = new EventManager();
    // INIT
    public static final EventType initPre = new EventType("init.pre", eventManager);
    public static final EventType init = new EventType("init", eventManager);
    public static final EventType initPost = new EventType("init.post", eventManager);
    public static final EventType initServer = new EventType("init.server", eventManager);
    public static final EventType initTexturesPre = new EventType("init.textures.pre", eventManager);
    public static final EventType initTexturesPost = new EventType("init.textures.post", eventManager);
    // CONFIG
    public static final EventType configChanged = new EventType("config.changed", eventManager);

    // TRANSFORM
    public static final EventType transform = new EventType("transform", eventManager) {
        @Override
        protected boolean isFiltered(Event event, Tuple filter, ListenerContainer eventListener) {
            return filter.get(0, String.class).equals(event.in.getType(String.class, 0));
        };

        @Override
        protected void onEvent(Event event) {
            if (!event.getListenerContainers().isEmpty()) {
                ClassReader classReader = new ClassReader(event.in.getType(byte[].class, 0));
                ClassNode classNode = new ClassNode();
                classReader.accept(classNode, 0);
                event.in = new TupleFacade(new ArrayList<Object>(event.in));
                event.in.add(classNode);
            }
        }

        @Override
        protected void onEventPost(Event event) {
            if (event.in.contains(ClassNode.class)) {
                ClassNode classNode = event.in.getType(ClassNode.class, 0);
                ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
                classNode.accept(writer);
                event.out.add(writer.toByteArray());
            } else {
                event.cancel();
            }
        };
    };

    public static final EventType water = new EventType("water", eventManager);
    public static final EventType waterCountForNewSource = new EventType("waterCountForNewSource", eventManager);
    public static final EventType waterBlocksMovement = new EventType("waterBlocksMovement", eventManager);
    // TICK
    public static final EventType tick = new EventType("tick", eventManager);
    // MESSAGE
    public static final EventType msgIMC = new EventType("msgIMC", eventManager);
    // ITEM
    public static final EventType itemToss = new EventType("item.toss", eventManager);
    public static final EventType itemPickup = new EventType("item.pickup", eventManager);
    public static final EventType itemExpire = new EventType("item.expire", eventManager);
    // DRAW
    public static final EventType drawBreakingAnim = new EventType("draw.breakingAnim", eventManager) {
        @Override
        public boolean isFiltered(Event event, Tuple filter, ListenerContainer eventlistener) {
            DrawBlockHighlightEvent highlightEvent = event.in.get(0, DrawBlockHighlightEvent.class);
            return highlightEvent.player.worldObj.getBlock(highlightEvent.target.blockX, highlightEvent.target.blockY,
                    highlightEvent.target.blockZ) == filter.get(0, Block.class);
        }
    };
    public static final EventType drawBoundingBox = new EventType("draw.boundingBox", eventManager) {
        @Override
        public boolean isFiltered(Event event, Tuple filter, ListenerContainer eventlistener) {
            DrawBlockHighlightEvent highlightEvent = event.in.get(0, DrawBlockHighlightEvent.class);
            return highlightEvent.player.worldObj.getBlock(highlightEvent.target.blockX, highlightEvent.target.blockY,
                    highlightEvent.target.blockZ) == filter.get(0, Block.class);
        }
    };
}
