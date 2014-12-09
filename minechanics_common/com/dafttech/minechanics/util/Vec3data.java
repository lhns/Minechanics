package com.dafttech.minechanics.util;

public class Vec3data {
    public int x, y, z;
    public Object[] data;

    public Vec3data(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3data(int x, int y, int z, Object... data) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.data = data;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vec3data)) return false;
        Vec3data vec3data = (Vec3data) obj;
        return vec3data.x == x && vec3data.z == z && vec3data.y == y && (data.length == 0 || data.equals(vec3data.data));
    }
}
