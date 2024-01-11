package com.mefrreex.mines.form;

import cn.nukkit.Player;
import com.mefrreex.mines.Mines;
import com.mefrreex.mines.mine.Mine;
import com.mefrreex.mines.mine.MineManager;
import com.mefrreex.mines.utils.Language;
import ru.contentforge.formconstructor.form.SimpleForm;

import java.util.List;
import java.util.function.BiConsumer;

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
