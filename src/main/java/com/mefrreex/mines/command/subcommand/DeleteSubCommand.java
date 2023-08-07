package com.mefrreex.mines.command.subcommand;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import com.mefrreex.mines.Mines;
import com.mefrreex.mines.command.BaseSubCommand;
import com.mefrreex.mines.form.SelectMineForm;
import com.mefrreex.mines.manager.MineManager;
import com.mefrreex.mines.mine.Mine;
import com.mefrreex.mines.utils.Language;

public class DeleteSubCommand extends BaseSubCommand {

    public DeleteSubCommand() {
        super("delete", "Delete mine");
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
            Mine mine = MineManager.get(args[0]);
            if (mine == null) {
                player.sendMessage(Mines.PREFIX_RED + Language.get("command-mine-not-found"));
                return false;
            }
            player.sendMessage(mine.remove() ?
                Mines.PREFIX_GREEN + Language.get("subcommand-delete-deleted") : 
                Mines.PREFIX_RED + Language.get("command-mine-not-found"));
            return true;
        }
        SelectMineForm.sendTo(player, (pl, mine) -> {
            player.sendMessage(mine.remove() ? 
                Language.get("subcommand-delete-deleted") : 
                Language.get("command-mine-not-found"));
        });
        return true;
    } 
}
