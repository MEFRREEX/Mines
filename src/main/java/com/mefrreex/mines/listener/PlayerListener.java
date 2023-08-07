package com.mefrreex.mines.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.player.PlayerMoveEvent;

import com.mefrreex.mines.manager.MineManager;
import com.mefrreex.mines.mine.Mine;
import com.mefrreex.mines.utils.Point;

public class PlayerListener implements Listener {
    
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (MineManager.get(player.getLevel()) == null) {
            return;
        }
        for (Mine mine : MineManager.get(player.getLevel())) {
            if (mine.isInMine(new Point(player)) && mine.isUpdating()) {
                if (mine.getTeleportPoint() != null) {
                    player.teleport(mine.getTeleportPoint().toLocation());
                }
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (MineManager.get(player.getLevel()) == null) {
            return;
        }
        for (Mine mine : MineManager.get(player.getLevel())) {
            if (mine.isInMine(new Point(event.getBlock()))) {
                if (mine.isUpdating()) {
                    event.setCancelled();
                    return;
                }
                if (mine.getCurrentSize().get() > 0) {
                    mine.getCurrentSize().decrementAndGet();
                }
                if (mine.getOccupancyPercent() <= mine.getUpdatePercent()) {
                    if (mine.isUpdateBelowPercent()) {
                        mine.update();
                    }
                }
            }
        }
    }
}
