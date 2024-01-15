package com.mefrreex.mines;

import cn.nukkit.Player;
import cn.nukkit.plugin.PluginBase;
import com.mefrreex.mines.command.MineCommand;
import com.mefrreex.mines.listener.PlayerListener;
import com.mefrreex.mines.mine.MineManager;
import com.mefrreex.mines.task.AutoUpdateTask;
import com.mefrreex.mines.utils.Language;
import com.mefrreex.mines.utils.Point;
import com.mefrreex.mines.utils.metrics.MetricsLoader;

import lombok.Getter;

import java.util.HashMap;

public class Mines extends PluginBase {
    
    private static Mines instance;

    private static final @Getter HashMap<Player, Point> firstPoints = new HashMap<>();
    private static final @Getter HashMap<Player, Point> secondPoints = new HashMap<>();

    public static String PREFIX_RED;
    public static String PREFIX_YELLOW;
    public static String PREFIX_GREEN;

    public static final String PERMISSION_ADMIN = "mines.admin";

    @Override
    public void onLoad() {
        Mines.instance = this;
        this.saveDefaultConfig();
        MineManager.getMinesFolder().mkdirs();
    }

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        this.getServer().getScheduler().scheduleRepeatingTask(this, new AutoUpdateTask(this), 20);
        Language.loadAll(this);
        MineCommand.register();
        MineManager.loadAll();
        this.loadMetrics();
        this.initPrefixes();
    }

    @Override
    public void onDisable() {
        MineManager.saveAll();
    }

    private void loadMetrics() {
        MetricsLoader metrics = new MetricsLoader();
        metrics.addCustomMetrics();
    }

    private void initPrefixes() {
        PREFIX_GREEN = Language.get("prefix-green");
        PREFIX_YELLOW = Language.get("prefix-yellow");
        PREFIX_RED = Language.get("prefix-red");
    }

    /* Instance */
    public static Mines getInstance() {
        return instance;
    }
}
