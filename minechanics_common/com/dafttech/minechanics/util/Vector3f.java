package com.dafttech.minechanics.util;

public class Vector3f {
    public float x, y, z;

    public Vector3f() {
        this(0, 0, 0);
    }

    public Vector3f(float val) {
        x = val;
        y = val;
        z = val;
    }

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f(Vector3f vec) {
        x = vec.x;
        y = vec.y;
        z = vec.z;
    }

    public Vector3f set() {
        x = 0;
        y = 0;
        z = 0;
        return this;
    }

    public Vector3f set(float val) {
        x = val;
        y = val;
        z = val;
        return this;
    }

    public Vector3f set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vector3f set(Vector3f vec) {
        x = vec.x;
        y = vec.y;
        z = vec.z;
        return this;
    }

    public Vector3f add(float val) {
        x += val;
        y += val;
        z += val;
        return this;
    }

    public Vector3f add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Vector3f add(Vector3f vec) {
        x += vec.x;
        y += vec.y;
        z += vec.z;
        return this;
    }

    public Vector3f mul(float val) {
        x *= val;
        y *= val;
        z *= val;
        return this;
    }

    public Vector3f mul(float x, float y, float z) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
        return this;
    }

    public Vector3f mul(Vector3f vec) {
        x *= vec.x;
        y *= vec.y;
        z *= vec.z;
        return this;
    }

    public Vector3f neg() {
        x *= -1;
        y *= -1;
        z *= -1;
        return this;
    }

    @Override
    public Vector3f clone() {
        return new Vector3f(this);
    }

    public Vector3f rotate(float x, float y, float z, Vector3f origin) {
        float myX = this.x - origin.x, myY = this.y - origin.y, myZ = this.z - origin.z;
        set((float) (Math.cos(z) * (Math.cos(y) * myX + Math.sin(y) * (Math.sin(x) * myY + Math.cos(x) * myZ)) - Math.sin(z)
                * (Math.cos(x) * myY - Math.sin(x) * myZ)),
                (float) (Math.sin(z) * (Math.cos(y) * myX + Math.sin(y) * (Math.sin(x) * myY + Math.cos(x) * myZ)) + Math.cos(z)
                        * (Math.cos(x) * myY - Math.sin(x) * myZ)), (float) (-Math.sin(y) * myX + Math.cos(y)
                        * (Math.sin(x) * myY + Math.cos(x) * myZ)));
        add(origin);
        return this;
    }

    public Vector3f rotate(Vector3f rotation, Vector3f origin) {
        return rotate(rotation.x, rotation.y, rotation.z, origin);
    }
}
