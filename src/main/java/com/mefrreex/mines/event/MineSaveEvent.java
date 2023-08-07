package com.mefrreex.mines.event;

import com.mefrreex.mines.mine.Mine;
import cn.nukkit.event.HandlerList;
import lombok.Getter;

import java.io.File;

@Getter
public class MineSaveEvent extends MineEvent {

    private final File file;

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    public MineSaveEvent(Mine mine, File file) {
        super(mine);
        this.file = file;
    }
}
