package com.mefrreex.mines.placeholder.placeholders;

import cn.nukkit.Player;
import com.creeperface.nukkit.placeholderapi.api.PlaceholderParameters.Parameter;
import com.mefrreex.mines.mine.Mine;
import com.mefrreex.mines.placeholder.Placeholder;
import com.mefrreex.mines.service.MineService;
import com.mefrreex.mines.utils.TimeFormatter;

import java.util.List;
import java.util.Optional;

public class PlaceholderUpdateInterval extends Placeholder {

    public PlaceholderUpdateInterval() {
        super("mine_update_interval");
    }

    @Override
    public String onUpdate(Player player, List<Parameter> parameters) {
        if (parameters.isEmpty()) {
            throw new RuntimeException("Placeholder %mine_update_interval<>% must have 1 parameters");
        }

        String name = parameters.get(0).getValue();

        Optional<Mine> mine = MineService.getInstance().getMineByName(name);
        if (mine.isEmpty()) {
            throw new RuntimeException("Mine with name " + name + " not found");
        }

        return TimeFormatter.format(mine.get().getUpdateInterval() * 1000, player);
    }
}
