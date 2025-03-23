package com.mefrreex.mines.form;

import cn.nukkit.Player;
import cn.nukkit.Server;
import com.mefrreex.mines.Mines;
import com.mefrreex.mines.mine.Mine;
import com.mefrreex.mines.utils.Language;
import com.mefrreex.mines.utils.Point;
import com.mefrreex.mines.utils.PointLocation;
import com.formconstructor.form.CustomForm;
import com.formconstructor.form.SimpleForm;
import com.formconstructor.form.element.custom.Input;
import com.formconstructor.form.element.custom.Toggle;
import com.formconstructor.form.element.simple.Button;

public class EditMineForm {

    public static void sendTo(Player player, Mine mine) {
        SimpleForm form = new SimpleForm(Language.get("form-edit-title"));

        form.addButton(new Button()
                .setName(Language.get("form-edit-button-blocks"))
                .onClick((pl, b) -> MineBlocksForm.sendTo(player, mine)))

            .addButton(new Button()
                .setName(Language.get("form-edit-button-settings"))
                .onClick((pl, b) -> EditMineForm.sendToSettings(player, mine)))

            .addButton(new Button()
                .setName(Language.get("form-edit-button-teleport-point"))
                .onClick((pl, b) -> {
                    mine.setTeleportPoint(new PointLocation(player));
                    player.sendMessage(Mines.PREFIX_GREEN + Language.get("form-edit-message-teleport-point-set"));
                }))

            .addButton(new Button()
                .setName(Language.get("form-edit-button-first-point"))
                .onClick((pl, b) -> {
                    mine.setFirstPoint(new Point(player));
                    player.sendMessage(Mines.PREFIX_GREEN + Language.get("form-edit-message-first-point-set"));
                }))

            .addButton(new Button()
                .setName(Language.get("form-edit-button-second-point"))
                .onClick((pl, b) -> {
                    mine.setSecondPoint(new Point(player));
                    player.sendMessage(Mines.PREFIX_GREEN + Language.get("form-edit-message-second-point-set"));
                }));
            
        form.send(player);
    }

    public static void sendToSettings(Player player, Mine mine) {
        CustomForm form = new CustomForm(Language.get("form-edit-settings-title"));
        form.addElement("world", new Input()
                .setName(Mines.PREFIX_YELLOW + Language.get("form-edit-settings-input-world-name"))
                .setPlaceholder(Language.get("form-edit-settings-input-world-placeholder"))
                .setDefaultValue(mine.getLevelName()))

            .addElement("\n" + Mines.PREFIX_YELLOW + Language.get("form-edit-settings-label-permission"))
            .addElement("locked", new Toggle(Language.get("form-edit-settings-toggle-locked"), mine.isLocked()))
            
            .addElement("permission", new Input()
                .setName(Language.get("form-edit-settings-input-permission-name"))
                .setPlaceholder(Language.get("form-edit-settings-input-permission-placeholder"))
                .setDefaultValue(mine.getPermission() != null ? String.valueOf(mine.getPermission()) : "mine.permission." + mine.getName()))

            .addElement("\n" + Mines.PREFIX_YELLOW + Language.get("form-edit-settings-label-auto-update"))
            .addElement("autoUpdate", new Toggle(Language.get("form-edit-settings-toggle-auto-update"), mine.isAutoUpdate()))
            .addElement("updateOnLoad", new Toggle(Language.get("form-edit-settings-toggle-update-on-load"), mine.isUpdateOnLoad()))

            .addElement("updateInterval", new Input()
                .setName(Language.get("form-edit-settings-input-update-interval-name"))
                .setPlaceholder(Language.get("form-edit-settings-input-update-interval-placeholder"))
                .setDefaultValue(String.valueOf(mine.getUpdateInterval())))
            
            .addElement("\n" + Mines.PREFIX_YELLOW + Language.get("form-edit-settings-label-update-below-percent"))
            .addElement("updateBelowPercent", new Toggle(Language.get("form-edit-settings-toggle-update-below-percent"), mine.isUpdateBelowPercent()))
            
            .addElement("updatePercent", new Input()
                .setName(Language.get("form-edit-settings-input-update-percent-name"))
                .setPlaceholder(Language.get("form-edit-settings-input-update-percent-placeholder"))
                .setDefaultValue(String.valueOf(mine.getUpdatePercent())))

            .addElement("\n" + Mines.PREFIX_YELLOW + Language.get("form-edit-settings-label-parameters"))
            .addElement("safeUpdate", new Toggle(Language.get("form-edit-settings-toggle-safe-update"), mine.isSafeUpdate()));

        form.setHandler((pl, response) -> {
            // World
            String world = response.getInput("world").getValue();
            if (Server.getInstance().getLevelByName(world) == null) {
                player.sendMessage(Mines.PREFIX_RED + Language.get("form-edit-settings-message-world-not-found"));
                return;
            }

            // Permission
            boolean locked = response.getToggle("locked").getValue();
            String permission = response.getInput("permission").getValue();

            // Auto update
            boolean autoUpdate = response.getToggle("autoUpdate").getValue();
            int updateInterval;
            try {
                updateInterval = Integer.valueOf(response.getInput("updateInterval").getValue());
                if (updateInterval < 1) {
                    player.sendMessage(Mines.PREFIX_RED + Language.get("form-edit-settings-message-interval-nan"));
                    return;
                }
            } catch(NumberFormatException e) {
                player.sendMessage(Mines.PREFIX_RED + Language.get("form-edit-settings-message-interval-nan"));
                return;
            }
            boolean updateOnLoad = response.getToggle("updateOnLoad").getValue();

            // Update below percent
            boolean updateBelowPercent = response.getToggle("updateBelowPercent").getValue();
            double updatePercent;
            try {
                updatePercent = Double.valueOf(response.getInput("updatePercent").getValue());
                if (updatePercent < 0 || updatePercent > 100) {
                    player.sendMessage(Mines.PREFIX_RED + Language.get("form-edit-settings-message-update-percent-invalid"));
                    return;
                }
            } catch(NumberFormatException e) {
                player.sendMessage(Mines.PREFIX_RED + Language.get("form-edit-settings-message-update-percent-invalid"));
                return;
            }

            // Parameters
            boolean safeUpdate = response.getToggle("safeUpdate").getValue();
            
            mine.setLevelName(world);
            mine.setLocked(locked);
            mine.setPermission(permission);
            mine.setAutoUpdate(autoUpdate);
            mine.setUpdateOnLoad(updateOnLoad);
            mine.setUpdateInterval(updateInterval);
            mine.setUpdateBelowPercent(updateBelowPercent);
            mine.setUpdatePercent(updatePercent);
            mine.setSafeUpdate(safeUpdate);
            player.sendMessage(Mines.PREFIX_GREEN + Language.get("form-edit-settings-message-edited"));
        });
        form.send(player);
    }
}
