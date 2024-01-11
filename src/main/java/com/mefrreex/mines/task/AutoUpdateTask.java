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
        // Skip if there are no players on the server and updateIfNoPlayers = true
        boolean updateIfNoPlayers = main.getConfig().getBoolean("update-if-no-players", true);
        if (!updateIfNoPlayers && main.getServer().getOnlinePlayers().size() < 1) {
            return;
        }

        // Iteration of all mines
        MineManager.getMines().forEach((level, mines) -> {
            // Skip if the level null or closed
            if (level == null || level.getProvider() == null) {
                return;
            }

            // Iteration of all level mines
            for (Mine mine : mines) {
                if (!mine.isAutoUpdate()) {
                    continue;
                }

                // Checking the remaining time to update and updating the mine
                mine.getCurrentUpdateInterval().decrementAndGet();
                if (mine.getCurrentUpdateInterval().get() < 1) {
                    mine.update();
                }
            }
        });
    }
}
