package com.mefrreex.mines.service;

import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.mefrreex.mines.Mines;
import com.mefrreex.mines.event.MineInitEvent;
import com.mefrreex.mines.event.MineLoadEvent;
import com.mefrreex.mines.event.MineRemoveEvent;
import com.mefrreex.mines.event.MineSaveEvent;
import com.mefrreex.mines.mine.Mine;
import com.mefrreex.mines.utils.Point;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class MineServiceImpl implements MineService {

    private final Map<Level, Set<Mine>> mines = new HashMap<>();
    private final Map<String, Mine> mineByName = new HashMap<>();
    private final Gson gson = new Gson();

    @Override
    public File getMinesFolder() {
        return new File(Mines.getInstance().getDataFolder(), "mines");
    }

    @Override
    public Map<Level, Set<Mine>> getMines() {
        return mines;
    }

    @Override
    public Optional<Mine> getMineByName(String name) {
        return Optional.ofNullable(mineByName.get(name));
    }

    @Override
    public List<Mine> getLevelMines(Level level) {
        return new ArrayList<>(mines.getOrDefault(level, Collections.emptySet()));
    }

    @Override
    public List<Mine> getMinesAtPosition(Position position) {
        return mines.getOrDefault(position.getLevel(), Collections.emptySet())
                .stream()
                .filter(mine -> mine.isInMine(new Point(position)))
                .toList();
    }

    @Override
    public void addMine(Mine mine) {
        MineInitEvent event = new MineInitEvent(mine);
        Server.getInstance().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }

        mine.setCurrentUpdateInterval(new AtomicLong(mine.getUpdateInterval()));
        mine.setCurrentSize(new AtomicLong(0));

        mines.computeIfAbsent(mine.getLevel(), mines -> new HashSet<>()).add(mine);
        mineByName.put(mine.getName(), mine);
    }

    @Override
    public void removeMine(Mine mine) {
        Level mineLevel = mine.getLevel();
        if (!mines.containsKey(mineLevel)) {
            return;
        }

        File file = new File(getMinesFolder(), mine.getName() + ".json");

        MineRemoveEvent event = new MineRemoveEvent(mine, file);
        Server.getInstance().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }

        mines.get(mineLevel).remove(mine);
        mineByName.remove(mine.getName());
        if (file.exists()) {
            file.delete();
        }
    }

    @Override
    public void loadMine(File file) {
        try (JsonReader reader = new JsonReader(new FileReader(file))) {
            Mine mine = gson.fromJson(reader, Mine.class);

            MineLoadEvent event = new MineLoadEvent(mine, file);
            Server.getInstance().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return;
            }

            this.addMine(mine);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load mine from file: " + file.getName(), e);
        }
    }

    @Override
    public void loadMines() {
        File dir = getMinesFolder();
        if (dir.exists() && dir.isDirectory()) {
            for (File file : Objects.requireNonNull(dir.listFiles(), "Mines folder is empty")) {
                if (file.isFile() && file.getName().endsWith(".json")) {
                    loadMine(file);
                }
            }
        }
    }

    @Override
    public void saveMine(Mine mine) {
        File file = new File(getMinesFolder(), mine.getName() + ".json");

        MineSaveEvent event = new MineSaveEvent(mine, file);
        Server.getInstance().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(gson.toJson(mine));
        } catch (Exception e) {
            throw new RuntimeException("Failed to save mine: " + mine.getName(), e);
        }
    }

    @Override
    public void saveMines() {
        mines.values().stream()
                .flatMap(Collection::stream)
                .forEach(this::saveMine);
    }
}
