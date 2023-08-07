package com.mefrreex.mines.command.subcommand;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import com.mefrreex.mines.Mines;
import com.mefrreex.mines.command.BaseSubCommand;
import com.mefrreex.mines.form.MineActionForm;
import com.mefrreex.mines.form.SelectMineForm;
import com.mefrreex.mines.utils.Language;

public class ListSubCommand extends BaseSubCommand {

    public ListSubCommand() {
        super("list", "List of mines");
        this.setPermission(Mines.PERMISSION_ADMIN);
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
        SelectMineForm.sendTo((Player) sender, (pl, mine) -> MineActionForm.sendTo(pl, mine));
        return true;
    } 
}
