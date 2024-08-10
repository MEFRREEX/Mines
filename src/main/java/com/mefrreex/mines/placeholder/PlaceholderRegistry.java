package com.mefrreex.mines.placeholder;

import cn.nukkit.Player;
import com.creeperface.nukkit.placeholderapi.api.PlaceholderAPI;
import com.creeperface.nukkit.placeholderapi.api.PlaceholderParameters.Parameter;
import com.mefrreex.mines.placeholder.placeholders.PlaceholderUpdateCurrentInterval;
import com.mefrreex.mines.placeholder.placeholders.PlaceholderUpdateInterval;

import java.util.List;

public class PlaceholderRegistry {

    private static final PlaceholderAPI placeholderApi = PlaceholderAPI.getInstance();

    /**
     * Register all placeholders
     */
    public void init() {
        register(new PlaceholderUpdateInterval());
        register(new PlaceholderUpdateCurrentInterval());
    }

    /**
     * Register placeholder
     * @param placeholder Placeholder
     */
    public void register(Placeholder placeholder) {
        placeholderApi.builder(placeholder.getName(), String.class)
                .processParameters(true)
                .visitorLoader(entry -> {

                    Player player = entry.getPlayer();
                    List<Parameter> parameters = entry.getParameters().getUnnamed();

                    try {
                        return placeholder.onUpdate(player, parameters);
                    } catch(Exception e) {
                        throw new RuntimeException("An error occurred when updating the placeholder", e);
                    }
                })
                .build();
    }
}