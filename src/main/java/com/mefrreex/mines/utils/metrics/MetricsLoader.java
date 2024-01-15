package com.mefrreex.mines.utils.metrics;

import com.mefrreex.mines.Mines;
import com.mefrreex.mines.utils.metrics.Metrics.SimplePie;

public class MetricsLoader {
    
    private final Mines main;
    private final Metrics metrics;

    private final int pluginId = 20748;

    public MetricsLoader() {
        this.main = Mines.getInstance();
        this.metrics = new Metrics(main, pluginId);
    }

    public void addCustomMetrics() {
        metrics.addCustomChart(new SimplePie("nukkit_version", () -> main.getServer().getNukkitVersion()));
        metrics.addCustomChart(new SimplePie("xbox_auth", () -> main.getServer().getPropertyBoolean("xbox-auth") ? "Required" : "Not required"));
    }
}
