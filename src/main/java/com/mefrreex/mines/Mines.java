package com.mefrreex.mines;

import cn.nukkit.Player;
import cn.nukkit.plugin.PluginBase;

import com.mefrreex.mines.command.BaseCommand;
import com.mefrreex.mines.listener.PlayerListener;
import com.mefrreex.mines.manager.MineManager;
import com.mefrreex.mines.task.AutoUpdateTask;
import com.mefrreex.mines.utils.Language;
import com.mefrreex.mines.utils.Point;

import lombok.Getter;

import java.util.HashMap;

public class Mines extends PluginBase {
    
    private static Mines instance;

    private static final @Getter HashMap<Player, Point> firstPoints = new HashMap<>();
    private static final @Getter HashMap<Player, Point> secondPoints = new HashMap<>();

    public static final String PREFIX_RED = "§l§c>§r§f ";
    public static final String PREFIX_YELLOW = "§l§e>§r§f ";
    public static final String PREFIX_GREEN = "§l§a>§r§f ";

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
        BaseCommand command = new BaseCommand();
        command.register();
        MineManager.loadAll();
    }

    @Override
    public void onDisable() {
        MineManager.saveAll();
    }

    public static Mines get() {
        return instance;
    }
}
