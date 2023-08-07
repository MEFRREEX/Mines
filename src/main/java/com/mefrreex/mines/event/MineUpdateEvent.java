package com.mefrreex.mines.event;

import com.mefrreex.mines.mine.Mine;
import cn.nukkit.event.HandlerList;

public class MineUpdateEvent extends MineEvent {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    public MineUpdateEvent(Mine mine) {
        super(mine);
    }
    
}
