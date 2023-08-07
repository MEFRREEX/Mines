package com.mefrreex.mines.form;

import java.util.List;
import java.util.function.BiConsumer;

import com.mefrreex.mines.Mines;
import com.mefrreex.mines.manager.MineManager;
import com.mefrreex.mines.mine.Mine;
import com.mefrreex.mines.utils.Language;

import cn.nukkit.Player;
import ru.contentforge.formconstructor.form.SimpleForm;

public class SelectMineForm {
    
    public static void sendTo(Player player, BiConsumer<Player, Mine> callback) {
        SimpleForm form = new SimpleForm(Language.get("form-select-title"));
        form.addContent(Mines.PREFIX_YELLOW + Language.get("form-select-content"));
        for (List<Mine> mines : MineManager.getMines().values()) {
            for (Mine mine : mines) {
                form.addButton(mine.getName(), (pl, b) -> {
                    callback.accept(player, mine);
                });
            }
        }
        form.send(player);
    }
}
