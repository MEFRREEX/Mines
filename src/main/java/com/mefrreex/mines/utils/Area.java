package com.mefrreex.mines.utils;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class Area {

    @SerializedName("minPos") private Point minPoint;
    @SerializedName("maxPos") private Point maxPoint;

    public Area(Point point1, Point point2){
        this.minPoint = Point.min(point1, point2);
        this.maxPoint = Point.max(point1, point2);
    }

    public int deltaX() {
        return maxPoint.x - minPoint.x;
    }

    public int deltaY() {
        return maxPoint.y - minPoint.y;
    }

    public int deltaZ() {
        return maxPoint.z - minPoint.z;
    }

    public long getSize() {
        int width = Math.abs(deltaX()) + 1;
        int height = Math.abs(deltaY()) + 1;
        int length = Math.abs(deltaZ()) + 1;
        return width * height * length;
    }

    public boolean isInside(Point point){
        return minPoint.x <= point.x && point.x <= maxPoint.x &&
                minPoint.y <= point.y && point.y <= maxPoint.y &&
                minPoint.z <= point.z && point.z <= maxPoint.z;
    }

    public boolean isNotCollided(Area area){
        if(area.minPoint.x > maxPoint.x || area.maxPoint.x < minPoint.x) return true;
        if(area.minPoint.y > maxPoint.y || area.maxPoint.y < minPoint.y) return true;
        return area.minPoint.z > maxPoint.z || area.maxPoint.z < minPoint.z;
    }

    public boolean isCollided(Area area){
        return !isNotCollided(area);
    }
}