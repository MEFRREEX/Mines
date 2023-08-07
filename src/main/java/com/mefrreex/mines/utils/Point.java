package com.mefrreex.mines.utils;

import cn.nukkit.math.Vector3;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class Point {

    @SerializedName("x") protected int x;
    @SerializedName("y") protected int y;
    @SerializedName("z") protected int z;

    public Point(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point(Vector3 pos){
        this(pos.getFloorX(), pos.getFloorY(), pos.getFloorZ());
    }

    public Vector3 toVector3() {
        return new Vector3(x, y, z);
    }

    public static Point min(Point p1, Point p2){
        return new Point(
                Math.min(p1.x, p2.x),
                Math.min(p1.y, p2.y),
                Math.min(p1.z, p2.z)
        );
    }

    public static Point max(Point p1, Point p2){
        return new Point(
                Math.max(p1.x, p2.x),
                Math.max(p1.y, p2.y),
                Math.max(p1.z, p2.z)
        );
    }

}
