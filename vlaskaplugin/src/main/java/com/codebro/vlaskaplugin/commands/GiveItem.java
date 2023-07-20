package com.codebro.vlaskaplugin.commands;

import com.codebro.vlaskaplugin.bossitems.KuppaTrident;
import com.codebro.vlaskaplugin.bossitems.OnyHelmet;
import com.codebro.vlaskaplugin.bossitems.UreyBow;
import com.ssomar.score.api.executableitems.ExecutableItemsAPI;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class GiveItem implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;

            PlayerInventory inventory = player.getInventory();
            ExecutableItemsAPI.getExecutableItemsManager().getExecutableItem("UreyArrow").get().buildItem(1, Optional.empty());

            inventory.addItem(new UreyBow().getBow());
            inventory.addItem(new KuppaTrident().getTrident());
            inventory.addItem(new OnyHelmet().getOnyHelmet());
        }

        return true;
    }
}
