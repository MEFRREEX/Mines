package com.mefrreex.mines.event;

import com.mefrreex.mines.mine.Mine;

import cn.nukkit.Player;
import cn.nukkit.event.HandlerList;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NoMinePermissionEvent extends MineEvent {

    private final Player player;
    private String permissionMessage;

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    public NoMinePermissionEvent(Player player, Mine mine) {
        super(mine);
        this.player = player;
    }
    
}
