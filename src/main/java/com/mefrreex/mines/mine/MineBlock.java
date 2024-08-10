package com.mefrreex.mines.mine;

import cn.nukkit.block.Block;
import lombok.Getter;
import lombok.Setter;

@Getter
public class MineBlock {
    
    private int id;
    private int damage;

    @Setter
    private double chance;

    public MineBlock(Block block, double chance) {
        this.id = block.getId();
        this.damage = block.getDamage();
        this.chance = chance;
    }

    /**
     * Get Nukkit block
     * @return Block
     */
    public Block getBlock() {
        return Block.get(id, damage);
    }

    /**
     * Set block
     * @param block Block
     */
    public void setBlock(Block block) {
        this.id = block.getId();
        this.damage = block.getDamage();
    }
}