package com.mefrreex.mines.task;

import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import cn.nukkit.scheduler.Task;
import com.mefrreex.mines.mine.Mine;
import com.mefrreex.mines.utils.Point;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UpdateMineTask extends Task {

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
    public void onRun(int currentTick) {
        CompletableFuture.runAsync(() -> {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
    
                int minX = Math.min(point1.getX(), point2.getX());
                int maxX = Math.max(point1.getX(), point2.getX());
                int minY = Math.min(point1.getY(), point2.getY());
                int maxY = Math.max(point1.getY(), point2.getY());
                int minZ = Math.min(point1.getZ(), point2.getZ());
                int maxZ = Math.max(point1.getZ(), point2.getZ());
    
                ExecutorService subExecutor = Executors.newCachedThreadPool();
                for (int x = minX; x <= maxX; x++) {
                    for (int y = minY; y <= maxY; y++) {
                        for (int z = minZ; z <= maxZ; z++) {
                            final int fx = x, fy = y, fz = z;
                            subExecutor.execute(() -> {
                                level.setBlock(new Vector3(fx, fy, fz), mine.getBlockWithChance().getBlock());
                                mine.getCurrentSize().incrementAndGet();
                            });
                        }
                    }
                }
                subExecutor.shutdown();
            });
            executor.shutdown();
            mine.setUpdating(false);
        });
    }
    
}
