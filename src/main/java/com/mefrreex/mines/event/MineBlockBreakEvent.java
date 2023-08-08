package com.mefrreex.mines.event;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.HandlerList;
import com.mefrreex.mines.mine.Mine;
import com.mefrreex.mines.mine.MineBlock;
import lombok.Getter;

@Getter
public class MineBlockBreakEvent extends MineEvent {

    private final Player player;
    private final Block block;

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    public MineBlockBreakEvent(Player player, Mine mine, Block block) {
        super(mine);
        this.player = player;
        this.block = block;
    }

    public MineBlock getMineBlock() {
        return mine.getMineBlock(block);
    }

    public boolean isMineBlock() {
        return this.getMineBlock() != null;
    }
}
