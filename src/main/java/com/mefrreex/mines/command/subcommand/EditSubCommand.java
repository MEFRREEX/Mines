package com.mefrreex.mines.command.subcommand;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import com.mefrreex.mines.Mines;
import com.mefrreex.mines.command.BaseSubCommand;
import com.mefrreex.mines.form.EditMineForm;
import com.mefrreex.mines.form.SelectMineForm;
import com.mefrreex.mines.manager.MineManager;
import com.mefrreex.mines.utils.Language;

public class EditSubCommand extends BaseSubCommand {

    public EditSubCommand() {
        super("edit", "Edit mine");
        this.setPermission(Mines.PERMISSION_ADMIN);
        this.parameters.add(CommandParameter.newType("name", true, CommandParamType.TEXT));
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String label, String[] args) {
        if (!testPermission(sender)) {
            sender.sendMessage(Mines.PREFIX_RED + Language.get("command-no-permission"));
            return false;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(Mines.PREFIX_RED + Language.get("command-in-game"));
            return false;
        }
        Player player = (Player) sender;
        if (args.length > 0) {
            if (MineManager.get(args[0]) == null) {
                player.sendMessage(Mines.PREFIX_RED + Language.get("command-mine-not-found"));
                return false;
            }
            EditMineForm.sendTo(player, MineManager.get(args[0]));
            return true;
        }
        SelectMineForm.sendTo(player, (pl, mine) -> EditMineForm.sendTo(pl, mine));
        return true;
    } 
}
