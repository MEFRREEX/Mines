package com.mefrreex.mines.command.subcommand;

import cn.nukkit.Player;
import cn.nukkit.block.BlockStone;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import com.mefrreex.mines.Mines;
import com.mefrreex.mines.command.BaseCommand;
import com.mefrreex.mines.command.BaseSubCommand;
import com.mefrreex.mines.form.CreateMineForm;
import com.mefrreex.mines.form.EditMineForm;
import com.mefrreex.mines.mine.Mine;
import com.mefrreex.mines.mine.MineBlock;
import com.mefrreex.mines.service.MineService;
import com.mefrreex.mines.utils.Area;
import com.mefrreex.mines.utils.Language;
import com.mefrreex.mines.utils.Point;

public class CreateSubCommand extends BaseSubCommand {

    public CreateSubCommand(BaseCommand command) {
        super("create", "Create a mine", command);
        this.setPermission(Mines.PERMISSION_ADMIN);
        this.getParameters().add(CommandParameter.newType("name", true, CommandParamType.TEXT));
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String label, String[] args) {
        if (!this.testPermission(sender)) {
            sender.sendMessage(Mines.PREFIX_RED + Language.get("command-no-permission"));
            return false;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Mines.PREFIX_RED + Language.get("command-in-game"));
            return false;
        }

        Point point1 = Mines.getFirstPoints().get(player);
        if (point1 == null) {
            player.sendMessage(Mines.PREFIX_RED + Language.get("subcommand-create-not-found-point1"));
            return false;
        }

        Point point2 = Mines.getSecondPoints().get(player);
        if (point2 == null) {
            player.sendMessage(Mines.PREFIX_RED + Language.get("subcommand-create-not-found-point2"));
            return false;
        }

        if (args.length > 0) {
            if (MineService.getInstance().getMineByName(args[0]).isPresent()) {
                player.sendMessage(Mines.PREFIX_RED + Language.get("command-mine-already-exists"));
                return false;
            }

            Mine mine = new Mine(args[0]);
            mine.getBlocks().add(new MineBlock(new BlockStone(), 100));
            mine.setArea(new Area(point1, point2));
            mine.setLevelName(player.getLevel().getName());
            MineService.getInstance().addMine(mine);
            mine.update();

            player.sendMessage(Mines.PREFIX_GREEN + Language.get("subcommand-create-created"));
            EditMineForm.sendTo(player, mine);
            return true;
        }
        
        CreateMineForm.sendTo(player);
        return true;
    } 
}
