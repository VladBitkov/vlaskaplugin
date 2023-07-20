package com.codebro.vlaskaplugin;

import com.codebro.vlaskaplugin.bosses.UreyBoss;
import com.codebro.vlaskaplugin.commands.GiveItem;
import com.codebro.vlaskaplugin.commands.SpawnBoss;
import com.codebro.vlaskaplugin.events.GeneralEvents;
import com.codebro.vlaskaplugin.events.KuppaBossListener;
import com.codebro.vlaskaplugin.events.OnyBossListener;
import com.codebro.vlaskaplugin.events.UreyBossListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class Vlaskaplugin extends JavaPlugin {
    private static Economy econ = null;

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    @Override
    public void onEnable() {
        if (!setupEconomy() ) {
            System.out.println(format("Disabled due to no Vault dependency found!"));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getCommand("spawnCommand").setExecutor(new SpawnBoss(this));
        getCommand("giveItem").setExecutor(new GiveItem());

        getServer().getPluginManager().registerEvents(new GeneralEvents(), this);
        getServer().getPluginManager().registerEvents(new UreyBossListener(this, econ), this);
        getServer().getPluginManager().registerEvents(new KuppaBossListener(this, econ), this);
        getServer().getPluginManager().registerEvents(new OnyBossListener(this, econ), this);
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private static String format(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
