package com.dafttech.minechanics.client.renderer.model;

import java.util.ArrayList;
import java.util.List;

import com.dafttech.minechanics.client.renderer.model.gl.GLAction;
import com.dafttech.minechanics.client.renderer.model.gl.GLNormal;
import com.dafttech.minechanics.client.renderer.model.gl.GLVertex;
import com.dafttech.minechanics.util.Vector3f;

public class Box extends Model {
    public Box(Model... childs) {
        super(childs);
    }

    public Box(Vector3f position, Model... childs) {
        super(position, childs);
    }

    public Box(float x, float y, float z, Model... childs) {
        super(x, y, z, childs);
    }

    public Box(Vector3f position, Vector3f size, Model... childs) {
        super(position, size, childs);
    }

    public Box(float x, float y, float z, float sX, float sY, float sZ, Model... childs) {
        super(x, y, z, sX, sY, sZ, childs);
    }

    @Override
    protected List<GLAction> renderThis(Texture[] texture) {
        List<GLAction> actions = new ArrayList<GLAction>();
        // RenderHelper.preRender(position, rotation, color, getLightVal());

        Texture tex = texture[0];

        // TOP
        actions.add(new GLNormal(0, 1, 0));
        actions.add(new GLVertex(0, 1, 0, tex));
        actions.add(new GLVertex(0, 1, 1, tex));
        actions.add(new GLVertex(1, 1, 1, tex));
        actions.add(new GLVertex(1, 1, 0, tex));
        // FRONT
        actions.add(new GLNormal(0, 0, -1));
        actions.add(new GLVertex(0, 0, 0, tex));
        actions.add(new GLVertex(0, 1, 0, tex));
        actions.add(new GLVertex(1, 1, 0, tex));
        actions.add(new GLVertex(1, 0, 0, tex));
        // BACK
        actions.add(new GLNormal(0, 0, 1));
        actions.add(new GLVertex(1, 0, 1, tex));
        actions.add(new GLVertex(1, 1, 1, tex));
        actions.add(new GLVertex(0, 1, 1, tex));
        actions.add(new GLVertex(0, 0, 1, tex));
        // RIGHT
        actions.add(new GLNormal(1, 0, 0));
        actions.add(new GLVertex(1, 0, 0, tex));
        actions.add(new GLVertex(1, 1, 0, tex));
        actions.add(new GLVertex(1, 1, 1, tex));
        actions.add(new GLVertex(1, 0, 1, tex));
        // LEFT
        actions.add(new GLNormal(-1, 0, 0));
        actions.add(new GLVertex(0, 0, 1, tex));
        actions.add(new GLVertex(0, 1, 1, tex));
        actions.add(new GLVertex(0, 1, 0, tex));
        actions.add(new GLVertex(0, 0, 0, tex));
        // BOTTOM
        actions.add(new GLNormal(0, -1, 0));
        actions.add(new GLVertex(0, 0, 0, tex));
        actions.add(new GLVertex(1, 0, 0, tex));
        actions.add(new GLVertex(1, 0, 1, tex));
        actions.add(new GLVertex(0, 0, 1, tex));

        // RenderHelper.postRender(position, rotation);
        return actions;
    }

    @Override
    protected Model getNewInstance() {
        return new Box();
    }
}
