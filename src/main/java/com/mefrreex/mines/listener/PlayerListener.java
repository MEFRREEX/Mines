package com.mefrreex.mines.listener;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.player.PlayerMoveEvent;

import java.util.List;

import com.mefrreex.mines.Mines;
import com.mefrreex.mines.event.MineBlockBreakEvent;
import com.mefrreex.mines.event.NoMinePermissionEvent;
import com.mefrreex.mines.manager.MineManager;
import com.mefrreex.mines.mine.Mine;
import com.mefrreex.mines.utils.Language;

public class PlayerListener implements Listener {
    
    private final Mines main;

    public PlayerListener(Mines main) {
        this.main = main;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        
        List<Mine> mines = MineManager.getMinesAt(player);
        for (Mine mine : mines) {
            if (mine.getTeleportPoint() != null) {

                if (mine.isUpdating()) {
                    player.teleport(mine.getTeleportPoint().toLocation());
                }
                
                if (mine.isLocked() && !player.hasPermission(mine.getPermission())) {

                    NoMinePermissionEvent permissionEvent = new NoMinePermissionEvent(player, mine);
                    permissionEvent.setPermissionMessage(Mines.PREFIX_RED + Language.get("mine-no-permission", mine.getName()));
                    main.getServer().getPluginManager().callEvent(permissionEvent);
                    
                    if (!permissionEvent.isCancelled()) {
                        String message = permissionEvent.getPermissionMessage();
                        if (message != null && !message.isBlank()) {
                            player.sendMessage(message);
                        }
                        player.teleport(mine.getTeleportPoint().toLocation());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        List<Mine> mines = MineManager.getMinesAt(block);
        for (Mine mine : mines) {

            if (mine.isUpdating()) {
                event.setCancelled();
                return;
            }

            if (mine.isLocked() && !player.hasPermission(mine.getPermission())) {
                NoMinePermissionEvent permissionEvent = new NoMinePermissionEvent(player, mine);
                permissionEvent.setPermissionMessage(Mines.PREFIX_RED + Language.get("mine-no-permission", mine.getName()));
                main.getServer().getPluginManager().callEvent(permissionEvent);
            
                if (!permissionEvent.isCancelled()) {
                    String message = permissionEvent.getPermissionMessage();
                    if (message != null && !message.isBlank()) {
                        player.sendMessage(message);
                    }
                    event.setCancelled();
                    return;
                }
            }

            MineBlockBreakEvent mineEvent = new MineBlockBreakEvent(player, mine, block);
            main.getServer().getPluginManager().callEvent(mineEvent);

            if (mineEvent.isCancelled()) {
                event.setCancelled();
                return;
            }

            if (mine.getCurrentSize().get() > 0) {
                mine.getCurrentSize().decrementAndGet();
            }
            if (mine.getOccupancyPercent() <= mine.getUpdatePercent() && mine.isUpdateBelowPercent()) {
                mine.update();
            }
        }
    }
}
