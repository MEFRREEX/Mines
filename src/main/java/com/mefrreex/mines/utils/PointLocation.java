package com.mefrreex.mines.utils;

import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class PointLocation extends Point {

    @SerializedName("yaw") protected double yaw;
    @SerializedName("pitch") protected double pitch;
    @SerializedName("level") protected String levelName;

    public PointLocation(int x, int y, int z) {
        this(x, y, z, 0, 0);
    }

    public PointLocation(int x, int y, int z, double yaw, double pitch) {
        this(x, y, z, yaw, pitch, null);
    }

    public PointLocation(int x, int y, int z, double yaw, double pitch, Level level) {
        super(x, y, z);
        this.yaw = yaw;
        this.pitch = pitch;
        this.levelName = level != null ? level.getName() : null;
    }

    public PointLocation(Location loc){
        this(loc.getFloorX(), loc.getFloorY(), loc.getFloorZ(), loc.getYaw(), loc.getPitch(), loc.getLevel());
    }

    public Level getLevel() {
        return Server.getInstance().getLevelByName(levelName);
    }

    public Location toLocation() {
        return new Location(x, y, z, yaw, pitch, yaw, getLevel());
    }

    public static PointLocation min(PointLocation p1, PointLocation p2){
        return (PointLocation) Point.min(p1, p2);
    }

    public static PointLocation max(PointLocation p1, PointLocation p2){
        return (PointLocation) Point.max(p1, p2);
    }

}
