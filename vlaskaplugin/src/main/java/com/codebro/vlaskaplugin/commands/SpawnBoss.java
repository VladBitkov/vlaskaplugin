package com.codebro.vlaskaplugin.commands;

import com.codebro.vlaskaplugin.bosses.KuppaBoss;
import com.codebro.vlaskaplugin.bosses.OnyBoss;
import com.codebro.vlaskaplugin.bosses.UreyBoss;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class SpawnBoss implements CommandExecutor {
    private Plugin plugin;

    public SpawnBoss(Plugin pl){
        plugin = pl;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if (args.length == 1 && args[0] != null) {
                if (args[0].equalsIgnoreCase("kuppa")) {
                    Drowned drowned = (Drowned) player.getWorld().spawnEntity(player.getLocation(), EntityType.DROWNED);
                    new KuppaBoss(drowned);
                }

                if (args[0].equalsIgnoreCase("urey")) {
                    Skeleton skeleton = (Skeleton)player.getWorld().spawnEntity(player.getLocation(), EntityType.SKELETON);
                    new UreyBoss(skeleton, plugin);
                }

                if (args[0].equalsIgnoreCase("ony")) {
                    Golem golem = (Golem) player.getWorld().spawnEntity(player.getLocation(), EntityType.IRON_GOLEM);
                    new OnyBoss(golem, plugin);
                }
            }
        }
        return true;
    }
}
