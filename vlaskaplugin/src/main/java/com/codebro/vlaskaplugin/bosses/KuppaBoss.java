package com.codebro.vlaskaplugin.bosses;

import com.codebro.vlaskaplugin.bossitems.KuppaTrident;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Drowned;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class KuppaBoss {
    private static final int KUPPA_SPAWN_ZOMBIE_COOLDOWN = 8;
    private static final int KUPPA_BREAK_SHIELD_COOLDOWN = 3;

    private static final double KUPPA_ECONOMY_REWARD = 450.0;
    private static final double KUPPA_HEALTH = 100.0;

    private static final double KUPPA_TRIDENT_DROP_CHANCE = 0.06;

    private static final String KUPPA_NAME = format("&2&lКаппа");
    private static final String KUPPA_DEATH_MESSAGE = format("&7&l[&6&lVl-ASKA&7&l] &6&lВнимание&f, &cнечисть &fпод именем: " + KUPPA_NAME + "&f, убили. Поздравьте героев!");
    private static final String KUPPA_ECONOMY_REWARD_MESSAGE = format("&7&l[&6&lVl-ASKA&7&l] &6&lРеспект! &fЗа &cубийство &fэтой &4нечисти &eты &fполучаешь: &6" + KUPPA_ECONOMY_REWARD + "Ɏ&f.");

    private static final BarColor KUPPA_BOSS_BAR_COLOR = BarColor.GREEN;

    Drowned drowned;

    public KuppaBoss(Drowned dr) {
        drowned = dr;

        drowned.setBaby();
        drowned.setShouldBurnInDay(false);
        drowned.setRemoveWhenFarAway(false);
        drowned.setCustomNameVisible(true);

        drowned.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
        drowned.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 2));

        AttributeInstance attribute1 = drowned.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        attribute1.setBaseValue(100.0);
        drowned.setHealth(100.0);

        drowned.getEquipment().setHelmet(new ItemStack(Material.TURTLE_HELMET));
        drowned.getEquipment().setItemInMainHand(new KuppaTrident().getTrident());

        drowned.setCustomName(KUPPA_NAME);
    }

    public static int getKuppaBreakShieldCooldown() {
        return KUPPA_BREAK_SHIELD_COOLDOWN;
    }

    public static String getKuppaDeathMessage() {
        return KUPPA_DEATH_MESSAGE;
    }

    public static String getKuppaEconomyRewardMessage() {
        return KUPPA_ECONOMY_REWARD_MESSAGE;
    }

    public static int getKuppaSpawnZombieCooldown() {
        return KUPPA_SPAWN_ZOMBIE_COOLDOWN;
    }

    public static BarColor getKuppaBossBarColor() {
        return KUPPA_BOSS_BAR_COLOR;
    }

    public static double getKuppaEconomyReward() {
        return KUPPA_ECONOMY_REWARD;
    }

    public static double getKuppaHealth() {
        return KUPPA_HEALTH;
    }

    public static double getKuppaTridentDropChance() {
        return KUPPA_TRIDENT_DROP_CHANCE;
    }

    public static String getKuppaName() {
        return KUPPA_NAME;
    }

    public static boolean checkKuppaBoss(Entity entity) {
        if (entity.getType() == EntityType.DROWNED || entity.getType() == EntityType.ZOMBIE) {
            Zombie zombie = (Zombie) entity;
            if (zombie.getCustomName() != null && zombie.getCustomName().equals(format(KUPPA_NAME))) {
                return true;
            }
        }
        return false;
    }

    private static String format(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
