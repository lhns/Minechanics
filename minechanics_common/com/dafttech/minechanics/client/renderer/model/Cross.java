package com.dafttech.minechanics.client.renderer.model;

import com.dafttech.minechanics.util.Vector3f;

public class Cross extends Model {
    public Cross(Model... childs) {
        super(childs);
    }

    public Cross(Vector3f position, Model... childs) {
        super(position, childs);
    }

    @Override
    protected void init() {
        add(new Rect().setRotation(new Vector3f(90, 45, 0)));
        add(new Rect().setRotation(new Vector3f(90, 45 + 90, 0)));
        add(new Rect().setRotation(new Vector3f(90, 45 + 180, 0)));
        add(new Rect().setRotation(new Vector3f(90, 45 + 270, 0)));
    }
}
