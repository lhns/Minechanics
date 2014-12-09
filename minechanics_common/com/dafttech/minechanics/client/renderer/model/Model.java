package com.dafttech.minechanics.client.renderer.model;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.util.Color;

import com.dafttech.minechanics.client.renderer.model.gl.GLAction;
import com.dafttech.minechanics.client.renderer.model.gl.GLBrightness;
import com.dafttech.minechanics.client.renderer.model.gl.GLColor;
import com.dafttech.minechanics.util.Reflected;
import com.dafttech.minechanics.util.Vector3f;

public class Model {
    private List<Model> childs = new ArrayList<Model>();
    public Vector3f position = new Vector3f(0, 0, 0);
    public Vector3f rotation = new Vector3f(0, 0, 0);
    public Vector3f size = new Vector3f(1, 1, 1);
    public Vector3f origin = new Vector3f(0.5f, 0.5f, 0.5f);

    public Texture[] texture = null;
    public Color color = null;
    public Integer brightness = null;

    public boolean visible = true;

    public boolean boundingBox = false;

    public Model(Model... childs) {
        add(childs);
        init();
    }

    public Model(Vector3f position, Model... childs) {
        this(childs);
        setPosition(position);
    }

    public Model(float x, float y, float z, Model... childs) {
        this(new Vector3f(x, y, z), childs);
    }

    public Model(Vector3f position, Vector3f size, Model... childs) {
        this(position, childs);
        this.setSize(size);
    }

    public Model(float x, float y, float z, float sX, float sY, float sZ, Model... childs) {
        this(x, y, z, childs);
        this.setSize(sX, sY, sZ);
    }

    public Model render() {
        return render(new Vector3f(0, 0, 0));
    }

    public Model render(Vector3f renderPosition) {
        if (color == null) color = new Color(255, 255, 255, 255);
        if (brightness == null) brightness = 15 << 20 | 15 << 4;

        boolean wasDrawing = preRender();

        List<GLAction> actions = renderModel(null);

        for (GLAction action : actions) {
            action.translate(renderPosition.add(getBoundingBox()));
        }

        actionsApply(actions);

        postRender(wasDrawing);
        return this;
    }

    private boolean preRender() {
        boolean wasDrawing = Tessellator.instance.isDrawing;
        if (wasDrawing) Tessellator.instance.draw();
        Tessellator.instance.startDrawingQuads();
        return wasDrawing;
    }

    private void postRender(boolean wasDrawing) {
        Tessellator.instance.draw();
        if (wasDrawing) Tessellator.instance.startDrawingQuads();
    }

    private List<GLAction> renderModel(Texture[] texture) {
        Texture[] newTexture = this.texture == null ? texture : this.texture;

        List<GLAction> actions = new ArrayList<GLAction>();

        if (!isVisible()) return actions;

        actions.add(new GLColor(color));
        actions.add(new GLBrightness(brightness));
        actions.addAll(renderThis(newTexture));

        for (Model model : getChilds()) {
            actions.addAll(model.renderModel(newTexture));
        }

        for (GLAction action : actions) {
            action.resize(getSize());
            action.translate(getPosition());
            action.rotate(getRotation(), getOrigin().clone().mul(size).add(getPosition()));
            action.colorize(color);
            action.illuminate(brightness);
        }
        return actions;
    }

    protected void init() {

    }

    protected List<GLAction> renderThis(Texture[] texture) {
        return new ArrayList<GLAction>();
    }

    private void actionsApply(List<GLAction> actions) {
        for (GLAction action : actions) {
            action.apply();
        }
    }

    // Getter/Setter

    public Model add(Model... childs) {
        for (Model child : childs) {
            if (child != null) {
                getChilds().add(child);
            }
        }
        return this;
    }

    public Model setPosition(Vector3f position) {
        this.position = position;
        return this;
    }

    public Model setPosition(float x, float y, float z) {
        return setPosition(new Vector3f(x, y, z));
    }

    public Model setRotation(Vector3f rotation) {
        this.rotation = rotation;
        return this;
    }

    public Model setRotation(float x, float y, float z) {
        return setRotation(new Vector3f(x, y, z));
    }

    public Model setSize(Vector3f size) {
        this.size = size;
        return this;
    }

    public Model setSize(float x, float y, float z) {
        return setSize(new Vector3f(x, y, z));
    }

    public Model setOrigin(Vector3f origin) {
        this.origin = origin;
        return this;
    }

    public Model setTexture(Texture... texture) {
        this.texture = texture;
        return this;
    }

    public Model setColor(Color color) {
        this.color = color;
        return this;
    }

    public Model setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public Model setBoundingBox() {
        boundingBox = true;
        return this;
    }

    public Model setBrightness(int brightness) {
        this.brightness = brightness;
        return this;
    }

    public Model setBrightness(IBlockAccess world, int x, int y, int z) {
        setBrightness(world.getLightBrightnessForSkyBlocks(MathHelper.floor_float(x), MathHelper.floor_float(y), MathHelper.floor_float(z), 0));
        return this;
    }

    public Model antifight() {
        final float factor = -0.001f;
        Vector3f oldSize = getSize().clone();
        resize(new Vector3f(1 + factor, 1 + factor, 1 + factor));
        move(oldSize.add(getSize().clone().neg()).mul(1f / 2f));
        return this;
    }

    public Model invert() {
        move(size);
        size.x *= -1;
        size.y *= -1;
        size.z *= -1;
        return this;
    }

    public Model move(Vector3f position) {
        this.position.x += position.x;
        this.position.y += position.y;
        this.position.z += position.z;
        return this;
    }

    public Model rotate(Vector3f rotation) {
        this.rotation.x += rotation.x;
        this.rotation.y += rotation.y;
        this.rotation.z += rotation.z;
        return this;
    }

    public Model resize(Vector3f size) {
        this.size.x *= size.x;
        this.size.y *= size.y;
        this.size.z *= size.z;
        return this;
    }

    public Model moveOrigin(Vector3f origin) {
        this.origin.x += origin.x;
        this.origin.y += origin.y;
        this.origin.z += origin.z;
        return this;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public Vector3f getSize() {
        return size;
    }

    public Vector3f getOrigin() {
        return origin;
    }

    public Texture[] getTexture() {
        return texture;
    }

    public Color getColor() {
        return color;
    }

    public boolean isVisible() {
        return visible;
    }

    public int getBrightness() {
        return brightness;
    }

    private Vector3f getBoundingBox() {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (boundingBox && player != null) {
            float parTicks = Reflected.instance.getParTicks();
            return new Vector3f((float) -(player.lastTickPosX + (player.posX - player.lastTickPosX) * parTicks),
                    (float) -(player.lastTickPosY + (player.posY - player.lastTickPosY) * parTicks),
                    (float) -(player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * parTicks));
        }
        return new Vector3f(0, 0, 0);
    }

    protected Model getNewInstance() {
        return new Model();
    }

    @Override
    public Model clone() {
        Model model;
        model = getNewInstance();
        if (position != null) model.position = position.clone();
        if (rotation != null) model.rotation = rotation.clone();
        if (size != null) model.size = size.clone();
        if (origin != null) model.origin = origin.clone();
        if (texture != null) model.texture = texture.clone();
        if (color != null) model.color = new Color(color);
        model.brightness = brightness;
        model.visible = visible;
        model.boundingBox = boundingBox;
        for (Model child : getChilds())
            model.add(child.clone());
        return model;
    }

    public List<Model> getChilds() {
        return childs;
    }
}
