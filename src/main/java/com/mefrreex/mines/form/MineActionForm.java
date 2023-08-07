package com.mefrreex.mines.form;

import cn.nukkit.Player;
import ru.contentforge.formconstructor.form.SimpleForm;
import com.mefrreex.mines.Mines;
import com.mefrreex.mines.mine.Mine;
import com.mefrreex.mines.utils.Language;

public class MineActionForm {
    
    public static void sendTo(Player player, Mine mine) {
        SimpleForm form = new SimpleForm(Language.get("form-action-title"));
        form.addContent(Mines.PREFIX_YELLOW + Language.get("form-action-content"));
        form.addButton(Language.get("form-action-button-teleport"), (pl, b) -> {
            if (mine.getTeleportPoint() != null) {
                pl.teleport(mine.getTeleportPoint().toLocation());
            } else {
                pl.teleport(mine.getSecondPoint().toVector3());
            }
            player.sendMessage(Mines.PREFIX_GREEN + Language.get("form-action-message-teleported"));
        });
        form.addButton(Language.get("form-action-button-update"), (pl, b) -> {
            mine.update();
            player.sendMessage(Mines.PREFIX_GREEN + Language.get("form-action-message-updated"));
        });
        form.addButton(Language.get("form-action-button-edit"), (pl, b) -> {
            EditMineForm.sendTo(player, mine);
        });
        form.addButton(Language.get("form-action-button-delete"), (pl, b) -> {
            mine.remove();
            player.sendMessage(Mines.PREFIX_GREEN + Language.get("subcommand-delete-deleted"));
        });
        form.send(player);
    }
}
