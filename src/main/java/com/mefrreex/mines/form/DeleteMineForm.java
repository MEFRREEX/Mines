package com.mefrreex.mines.form;

import cn.nukkit.Player;
import com.mefrreex.mines.Mines;
import com.mefrreex.mines.mine.Mine;
import com.mefrreex.mines.utils.Language;
import com.formconstructor.form.ModalForm;

public class DeleteMineForm {
    
    public static void sendTo(Player player, Mine mine) {
        new ModalForm(Language.get("form-delete-title"))
            .setContent(Language.get("form-delete-content"))
            .setPositiveButton(Language.get("form-delete-button-confirm"))
            .setNegativeButton(Language.get("form-delete-button-back"))
            .setHandler((pl, response) -> {
                if (response) {
                    mine.remove();
                    pl.sendMessage(Mines.PREFIX_RED + Language.get("form-delete-message-deleted"));
                }
            })
            .send(player);
    }
}
