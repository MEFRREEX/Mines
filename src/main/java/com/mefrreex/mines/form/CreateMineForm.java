package com.mefrreex.mines.form;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import com.mefrreex.mines.Mines;
import com.mefrreex.mines.mine.Mine;
import com.mefrreex.mines.mine.MineBlock;
import com.mefrreex.mines.mine.MineManager;
import com.mefrreex.mines.utils.Area;
import com.mefrreex.mines.utils.Language;
import com.mefrreex.mines.utils.Point;
import com.formconstructor.form.CustomForm;
import com.formconstructor.form.element.custom.Input;

public class CreateMineForm {
    
    public static void sendTo(Player player) {
        CustomForm form = new CustomForm(Language.get("form-create-title"));

        form.addElement("name", new Input()
            .setName(Mines.PREFIX_YELLOW + Language.get("form-create-input-name-name"))
            .setPlaceholder(Language.get("form-create-input-name-placeholder")));

        form.setHandler((pl, response) -> {
            String name = response.getInput("name").getValue();
            
            if (MineManager.get(name) != null) {
                player.sendMessage(Mines.PREFIX_RED + Language.get("command-mine-already-exists"));
                return;
            }

            Mine mine = new Mine(name);
            mine.getBlocks().add(new MineBlock(Block.get(Block.STONE), 100));

            Point point1 = Mines.getFirstPoints().get(pl);
            Point point2 = Mines.getSecondPoints().get(pl);
            mine.setArea(new Area(point1, point2));
            mine.setLevelName(pl.getLevel().getName());

            mine.init();
            mine.update();
            player.sendMessage(Mines.PREFIX_GREEN + Language.get("subcommand-create-created"));
            
            EditMineForm.sendTo(player, mine);
        });

        form.send(player);
    }
}
