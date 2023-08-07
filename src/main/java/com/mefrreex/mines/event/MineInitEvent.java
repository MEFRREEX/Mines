package com.mefrreex.mines.event;

import com.mefrreex.mines.mine.Mine;
import cn.nukkit.event.HandlerList;

public class MineInitEvent extends MineEvent {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    public MineInitEvent(Mine mine) {
        super(mine);
    }
    
}
