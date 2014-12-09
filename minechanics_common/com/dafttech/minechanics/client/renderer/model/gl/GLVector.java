package com.dafttech.minechanics.client.renderer.model.gl;

import com.dafttech.minechanics.util.Vector3f;

public abstract class GLVector extends GLAction {
    public Vector3f pos;
    public boolean translate = true, resize = true, rotate = true;

    public GLVector(Vector3f pos) {
        this.pos = pos.clone();
    }

    public GLVector(float x, float y, float z) {
        pos = new Vector3f(x, y, z);
    }

    @Override
    public void translate(Vector3f position) {
        if (translate) pos.add(position);
    }

    @Override
    public void resize(Vector3f size) {
        if (resize) pos.mul(size);
    }

    @Override
    public void rotate(Vector3f rotation, Vector3f origin) {
        if (rotate) pos.rotate(rotation, origin);
    }
}
