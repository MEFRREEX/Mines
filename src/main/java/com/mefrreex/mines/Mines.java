package com.mefrreex.mines;

import cn.nukkit.Player;
import cn.nukkit.plugin.PluginBase;
import com.mefrreex.mines.command.MineCommand;
import com.mefrreex.mines.listener.PlayerListener;
import com.mefrreex.mines.placeholder.PlaceholderRegistry;
import com.mefrreex.mines.service.MineService;
import com.mefrreex.mines.service.MineServiceImpl;
import com.mefrreex.mines.task.AutoUpdateTask;
import com.mefrreex.mines.utils.Language;
import com.mefrreex.mines.utils.Point;
import com.mefrreex.mines.utils.metrics.MetricsLoader;

import lombok.Getter;

import java.util.HashMap;

@Getter
public class Mines extends PluginBase {
    
    @Getter
    private static Mines instance;

    private static final @Getter HashMap<Player, Point> firstPoints = new HashMap<>();
    private static final @Getter HashMap<Player, Point> secondPoints = new HashMap<>();

    private MineService mineService;
    private PlaceholderRegistry placeholderRegistry;

    public static String PREFIX_RED;
    public static String PREFIX_YELLOW;
    public static String PREFIX_GREEN;

    public static final String PERMISSION_ADMIN = "mines.admin";

    @Override
    public void onLoad() {
        Mines.instance = this;
        this.saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        Language.init(this);

        this.mineService = new MineServiceImpl();
        this.mineService.getMinesFolder().mkdirs();
        this.mineService.loadMines();

        this.getServer().getCommandMap().register("Mines", new MineCommand("mine", this));
        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        this.getServer().getScheduler().scheduleRepeatingTask(this, new AutoUpdateTask(this), 20);

        this.loadPlaceholders();
        this.loadMetrics();
        this.initPrefixes();
    }

    @Override
    public void onDisable() {
        this.mineService.saveMines();
    }

    private void loadPlaceholders() {
        if (this.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            this.placeholderRegistry = new PlaceholderRegistry();
            this.placeholderRegistry.init();
        }
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
}
