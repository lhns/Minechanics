package com.dafttech.minechanics.client.renderer.model;

import java.util.ArrayList;
import java.util.List;

import com.dafttech.minechanics.client.renderer.model.gl.GLAction;
import com.dafttech.minechanics.util.Vector3f;

public class Rect extends Model {
    // TODO: fix (look at Box) DON'T USE!!! BROKEN!!!
    public Rect(Model... childs) {
        super(childs);
    }

    public Rect(Vector3f position, Model... childs) {
        super(position, childs);
    }

    @Override
    protected List<GLAction> renderThis(Texture[] texture) {
        List<GLAction> vertices = new ArrayList<GLAction>();
        return vertices;
    }
}
