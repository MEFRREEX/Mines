package com.mefrreex.mines.task;

import cn.nukkit.Player;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import cn.nukkit.scheduler.AsyncTask;
import com.mefrreex.mines.mine.Mine;
import com.mefrreex.mines.utils.Point;

import java.util.HashSet;
import java.util.Set;

public class UpdateMineTask extends AsyncTask {

    private final Mine mine;

    private final Point point1;
    private final Point point2;
    private final Level level;

    public UpdateMineTask(Mine mine) {
        this.mine = mine;
        this.point1 = mine.getFirstPoint();
        this.point2 = mine.getSecondPoint();
        this.level = mine.getLevel();
    }

    @Override
    public void onRun() {
        mine.setUpdating(true);

        int minX = Math.min(point1.getX(), point2.getX());
        int maxX = Math.max(point1.getX(), point2.getX());
        int minY = Math.min(point1.getY(), point2.getY());
        int maxY = Math.max(point1.getY(), point2.getY());
        int minZ = Math.min(point1.getZ(), point2.getZ());
        int maxZ = Math.max(point1.getZ(), point2.getZ());

        Set<Vector3> positions = new HashSet<>();

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Vector3 position = new Vector3(x, y, z);
                    positions.add(position);
                    level.setBlock(position, mine.getBlockWithChance().getBlock());
                    mine.getCurrentSize().incrementAndGet();
                }
            }
        }

        level.sendBlocks(level.getPlayers().values().toArray(new Player[0]), positions.toArray(new Vector3[0]));
        mine.setUpdating(false);
    }
}
