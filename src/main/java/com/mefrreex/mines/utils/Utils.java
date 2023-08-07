package com.mefrreex.mines.utils;

import com.mefrreex.mines.mine.MineBlock;

import cn.nukkit.Player;
import cn.nukkit.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {
    
    private static final Random random = new Random();

    public static MineBlock getBlockWithChance(List<MineBlock> blocks) {
        MineBlock result = null;
        MineBlock block = null;
        double blockRandom = 0;
        double blockChance = 0;
        do {
            block = blocks.get(random.nextInt(blocks.size()));
            blockRandom = random.nextDouble(99) + 1;
            blockChance = block.getChance();
            if (blockRandom <= blockChance) {
                result = block;
            }
        } while (blockRandom > blockChance);
        return result;
    }

    public static List<Player> getPlayersBetween(Point point1, Point point2, Level level) {
        List<Player> players = new ArrayList<>();
        for (Player player : level.getPlayers().values()) {
            Area area = new Area(point1, point2);
            if (area.isInside(new Point(player))) {
                players.add(player);
            }
        }
        return players;
    }

    public static double toPercentage(double value, double minValue, double maxValue) {
        return (value - minValue) / (maxValue - minValue) * 100;
    }
}
