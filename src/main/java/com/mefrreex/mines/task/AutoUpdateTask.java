package com.mefrreex.mines.task;

import cn.nukkit.scheduler.Task;

import com.mefrreex.mines.Mines;
import com.mefrreex.mines.manager.MineManager;
import com.mefrreex.mines.mine.Mine;

public class AutoUpdateTask extends Task {

    private final Mines main;

    public AutoUpdateTask(Mines main) {
        this.main = main;
    }

    @Override
    public void onRun(int currentTick) {
        boolean updateIfNoPlayers = main.getConfig().getBoolean("update-if-no-players", true);
        if (!updateIfNoPlayers && main.getServer().getOnlinePlayers().size() < 1) {
            return;
        }
        MineManager.getMines().forEach((level, mines) -> {
            if (level == null) {
                return;
            }
            if (level.getProvider() == null) {
                return;
            }
            for (Mine mine : mines) {
                if (!mine.isAutoUpdate()) {
                    continue;
                }
                mine.getCurrentUpdateInterval().decrementAndGet();
                if (mine.getCurrentUpdateInterval().get() < 1) {
                    mine.update();
                }
            }
        });
    }
    
}
