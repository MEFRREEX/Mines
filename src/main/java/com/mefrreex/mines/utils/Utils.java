package com.mefrreex.mines.utils;

import cn.nukkit.Player;
import cn.nukkit.level.Level;
import com.mefrreex.mines.mine.MineBlock;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    
    private static final SecureRandom random = new SecureRandom();

    public static MineBlock getBlockWithChance(List<MineBlock> blocks) {
        MineBlock result = null;
        MineBlock block = null;
        double blockRandom = 0;
        double blockChance = 0;
        do {
            block = blocks.get(random.nextInt(blocks.size()));
            blockRandom = random.nextDouble(99.9) + 0.1;
            blockChance = block.getChance();
            if (blockRandom <= blockChance) {
                result = block;
            }
        } while (result == null);
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
