package com.mefrreex.mines.service;

import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import com.mefrreex.mines.Mines;
import com.mefrreex.mines.mine.Mine;

import java.io.File;
import java.util.*;

public interface MineService {
    
    File getMinesFolder();

    Map<Level, Set<Mine>> getMines();

    Optional<Mine> getMineByName(String name);

    List<Mine> getLevelMines(Level level);

    List<Mine> getMinesAtPosition(Position position);

    void addMine(Mine mine);

    void removeMine(Mine mine);

    void loadMine(File file);

    void loadMines();

    void saveMine(Mine mine);

    void saveMines();

    static MineService getInstance() {
        return Mines.getInstance().getMineService();
    }
}
