package org.lolhens.minechanics.core.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

public class GuiRenderer {
    public GuiContainer gui;
    public Minecraft mc;
    public int guiLeft, guiTop;

    public GuiRenderer(GuiContainer gui, Minecraft mc, int guiLeft, int guiTop) {
        this.gui = gui;
        this.mc = mc;
        this.guiLeft = guiLeft;
        this.guiTop = guiTop;
    }

    public void drawRect(int minX, int minY, int maxX, int maxY) {
        drawTexturedModalRect(guiLeft + minX, guiTop + minY, 0, 0, maxX, maxY);
    }

    public void drawRect(String file, int minX, int minY, int maxX, int maxY) {
        setFile(file);
        drawRect(minX, minY, maxX, maxY);
    }

    public static void setFile(String file) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        // Minecraft.getMinecraft().renderEngine.bindTexture(Minecraft.getMinecraft().renderEngine.getTexture(file));
    }

    public void drawAutoGui() {
        int minX = 256, minY = 256, maxX = 0, maxY = 0;
        Slot slot;
        for (Object slotobj : gui.inventorySlots.inventorySlots) {
            slot = (Slot) slotobj;
            if (slot.xDisplayPosition < minX) minX = slot.xDisplayPosition;
            if (slot.yDisplayPosition < minY) minY = slot.yDisplayPosition;
            if (slot.xDisplayPosition > maxX) maxX = slot.xDisplayPosition;
            if (slot.yDisplayPosition > maxY) maxY = slot.yDisplayPosition;
        }
        minX -= 8;
        minY -= 18;
        maxX += 20;
        maxY += 20;
        drawRect("assets/mcncs/textures/gui/autogui.png", minX, minY, maxX, maxY); // bg
        drawRect("assets/mcncs/textures/gui/autogui.png", minX + maxX, minY, 3, maxY); // bdr_vr
        drawRect("assets/mcncs/textures/gui/autogui.png", minX, minY + maxY, maxX, 3); // bdr
                                                                                       // hr
        drawRect("assets/mcncs/textures/gui/autogui.png", minX + maxX - 1, minY + maxY - 1, 4, 4); // edge
        setFile("assets/mcncs/textures/gui/autogui.png"); // slot
        for (Object slotobj : gui.inventorySlots.inventorySlots) {
            slot = (Slot) slotobj;
            drawRect(slot.xDisplayPosition - 1, slot.yDisplayPosition - 1, 18, 18);
        }
    }

    private void drawTexturedModalRect(int minX, int minY, int u, int v, int maxX, int maxY) {
        float zLevel = 0.0f;
        float imgMultX = 0.00390625F;
        float imgMultY = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(minX + 0, minY + maxY, zLevel, (u + 0) * imgMultX, (v + maxY) * imgMultY);
        tessellator.addVertexWithUV(minX + maxX, minY + maxY, zLevel, (u + maxX) * imgMultX, (v + maxY) * imgMultY);
        tessellator.addVertexWithUV(minX + maxX, minY + 0, zLevel, (u + maxX) * imgMultX, (v + 0) * imgMultY);
        tessellator.addVertexWithUV(minX + 0, minY + 0, zLevel, (u + 0) * imgMultX, (v + 0) * imgMultY);
        tessellator.draw();
    }

    public static void drawSimpleRect(String file, double minX, double minY, double maxX, double maxY) {
        Tessellator tessellator = Tessellator.instance;
        double zLevel = -90D;
        maxX += minX;
        maxY += minY;
        setFile(file);
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(minX, maxY, zLevel, 0, 1);
        tessellator.addVertexWithUV(maxX, maxY, zLevel, 1, 1);
        tessellator.addVertexWithUV(maxX, minY, zLevel, 1, 0);
        tessellator.addVertexWithUV(minX, minY, zLevel, 0, 0);
        tessellator.draw();
    }

    public static void renderItem(Minecraft mc, ItemStack stack, int x, int y) {
        RenderItem itemRenderer = new RenderItem();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LIGHTING);
        itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, stack, x, y);
        itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, mc.renderEngine, stack, x, y);
    }

    public static void renderItemIntoGUI(FontRenderer fontRenderer, TextureManager renderEngine, ItemStack stack, int x, int y,
            float opacity, float scale) {
        Item item = stack.getItem();
        // int meta = stack.getItemDamage();
        // int iconIndex = stack.getIconIndex();
        GL11.glDisable(GL11.GL_LIGHTING);
        // renderEngine.bindTexture(renderEngine.getTexture(stack.getItem().getTextureFile()));
        int overlayColour = item.getColorFromItemStack(stack, 0);
        float var17 = (overlayColour >> 16 & 255) / 255.0F;
        float var16 = (overlayColour >> 8 & 255) / 255.0F;
        float var12 = (overlayColour & 255) / 255.0F;
        GL11.glColor4f(var17, var16, var12, opacity);
        // drawTexturedQuad(x, y, iconIndex % 16 * 16, iconIndex / 16 * 16, 16,
        // 16, -90, scale);
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    public static void drawTexturedQuad(int x, int y, int u, int v, int width, int height, double zLevel, float scale) {
        u = (int) (u * scale);
        v = (int) (v * scale);
        width = (int) (width * scale);
        height = (int) (height * scale);
        float var7 = 0.00390625F / scale;
        float var8 = 0.00390625F / scale;
        Tessellator var9 = Tessellator.instance;
        var9.startDrawingQuads();
        var9.addVertexWithUV(x + 0, y + height, zLevel, (u + 0) * var7, (v + height) * var8);
        var9.addVertexWithUV(x + width, y + height, zLevel, (u + width) * var7, (v + height) * var8);
        var9.addVertexWithUV(x + width, y + 0, zLevel, (u + width) * var7, (v + 0) * var8);
        var9.addVertexWithUV(x + 0, y + 0, zLevel, (u + 0) * var7, (v + 0) * var8);
        var9.draw();
    }
}
