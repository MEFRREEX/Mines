package com.mefrreex.mines.event;

import com.mefrreex.mines.mine.Mine;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public abstract class MineEvent extends Event implements Cancellable {
    
    protected @Getter @Setter Mine mine;
}
