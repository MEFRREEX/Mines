package com.mefrreex.mines.mine;

import cn.nukkit.block.Block;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class MineBlock {
    
    @SerializedName("id") private int id;
    @SerializedName("damage") private int damage;
    @SerializedName("chance") @Setter private double chance;

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