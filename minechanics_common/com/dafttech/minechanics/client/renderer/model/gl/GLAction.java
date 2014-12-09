package com.dafttech.minechanics.client.renderer.model.gl;

import org.lwjgl.util.Color;

import com.dafttech.minechanics.util.Vector3f;

public abstract class GLAction {
    public abstract void apply();

    public void translate(Vector3f position) {
    }

    public void resize(Vector3f size) {
    }

    public void rotate(Vector3f rotation, Vector3f origin) {
    }

    public void colorize(Color color) {
    }

    public void illuminate(Integer brightness) {
    }
}
