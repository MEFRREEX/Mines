package com.mefrreex.mines.form;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import ru.contentforge.formconstructor.form.CustomForm;
import ru.contentforge.formconstructor.form.SimpleForm;
import ru.contentforge.formconstructor.form.element.Input;
import com.mefrreex.mines.Mines;
import com.mefrreex.mines.mine.Mine;
import com.mefrreex.mines.mine.MineBlock;
import com.mefrreex.mines.utils.Language;

public class MineBlocksForm {

    public static void sendTo(Player player, Mine mine) {
        SimpleForm form = new SimpleForm(Language.get("form-edit-blocks-title"));
        form.addContent(Mines.PREFIX_YELLOW + Language.get("form-edit-blocks-content"));
        form.addButton(Language.get("form-edit-blocks-button-add"), (pl, b) -> {
            MineBlocksForm.sendToNew(player, mine);
        });
        for (MineBlock block : mine.getBlocks()) {
            String name = block.getBlock().getName();
            form.addButton(Language.get("form-edit-blocks-button-block", name), (pl, b) -> {
                if (mine.getBlocks().contains(block)) mine.getBlocks().remove(block);
                player.sendMessage(Mines.PREFIX_GREEN + Language.get("form-edit-blocks-message-removed"));
            });
        }
        form.send(player);
    }

    public static void sendToNew(Player player, Mine mine) {
        CustomForm form = new CustomForm(Language.get("form-blocks-new-block-title"));
        
        form.addElement("id", Input.builder()
                .setName(Mines.PREFIX_YELLOW + Language.get("form-blocks-new-block-input-id-name"))
                .setPlaceholder(Language.get("form-blocks-new-block-input-id-placeholder"))
                .build())
            .addElement("damage", Input.builder()
                .setName(Mines.PREFIX_YELLOW + Language.get("form-blocks-new-block-input-damage-name"))
                .setPlaceholder(Language.get("form-blocks-new-block-input-damage-placeholder"))
                .build())
            .addElement("chance", Input.builder()
                .setName(Mines.PREFIX_YELLOW + Language.get("form-blocks-new-block-input-chance-name"))
                .setPlaceholder(Language.get("form-blocks-new-block-input-chance-placeholder"))
                .build());

        form.setHandler((pl, response) -> {
            int id;
            try {
                id = Integer.valueOf(response.getInput("id").getValue());
            } catch(NumberFormatException e) {
                player.sendMessage(Mines.PREFIX_RED + Language.get("form-blocks-new-block-message-id-nan"));
                return;
            }

            String damageString = response.getInput("damage").getValue();
            int damage;
            if (!damageString.isBlank()) {
                try {
                    damage = Integer.valueOf(damageString);
                } catch(NumberFormatException e) {
                    player.sendMessage(Mines.PREFIX_RED + Language.get("form-blocks-new-block-message-damage-nan"));
                    return;
                }
            } else {
                damage = 0;
            }

            double chance;
            try {
                chance = Double.valueOf(response.getInput("chance").getValue());
                if (chance < 0 || chance > 100) {
                    player.sendMessage(Mines.PREFIX_RED + Language.get("form-blocks-new-block-message-chance-invalid"));
                    return;    
                }
            } catch (Exception e) {
                player.sendMessage(Mines.PREFIX_RED + Language.get("form-blocks-new-block-message-chance-invalid"));
                return;
            }

            Block block = Block.get(id, damage);
            MineBlock mineBlock = new MineBlock(block, chance);
            mine.getBlocks().add(mineBlock);
            mine.update();
            player.sendMessage(Mines.PREFIX_GREEN + Language.get("form-blocks-new-block-message-added", block.getName(), chance));
            MineBlocksForm.sendTo(player, mine);
        });
        form.send(player);
    }
}
