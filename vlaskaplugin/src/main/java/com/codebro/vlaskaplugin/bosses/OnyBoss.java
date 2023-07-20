package com.codebro.vlaskaplugin.bosses;

import com.codebro.vlaskaplugin.bossitems.OnyHelmet;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Golem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class OnyBoss {
    private static final int ONY_SPAWN_MOBS_COOLDOWN = 8;

    private static final double ONY_HEALTH = 90.0;
    private static final double ONY_ECONOMY_REWARD = 350.0;

    private static final double ONY_DROP_CHANCE_HELMET = 0.06;

    private static final String ONY_NAME = format("&cÓни");
    private static final String ONY_BOSS_BAR_TITLE = format("&c&lÓни");
    private static final String ONY_DEATH_MESSAGE = format("&7&l[&6&lVl-ASKA&7&l] &6&lВнимание&f, &cнечисть &fпод именем: " + ONY_NAME + "&f, &cубили&f. Поздравьте героев!");
    private static final String ONY_ECONOMY_REWARD_MESSAGE = format("&7&l[&6&lVl-ASKA&7&l] &6&lРеспект! &fЗа &cубийство &fэтой &4нечисти &eты &fполучаешь: &6" + ONY_ECONOMY_REWARD + "Ɏ&f.");

    private static final BarColor ONY_BOSS_BAR_COLOR = BarColor.RED;

    private Golem golem;
    private Plugin plugin;

    public OnyBoss(Golem gl, Plugin pl) {
        golem = gl;
        plugin = pl;

        Attributable golemAt = golem;
        AttributeInstance attribute1 = golemAt.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        attribute1.setBaseValue(ONY_HEALTH);
        golem.setHealth(ONY_HEALTH);
        golem.setCustomName(ONY_NAME);
        golem.setCustomNameVisible(true);

        golem.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 2));

        OnyHelmet onyHelmet = new OnyHelmet();

        golem.getEquipment().setHelmet(onyHelmet.getOnyHelmet());
    }

    public static double getOnyDropChanceHelmet() {
        return ONY_DROP_CHANCE_HELMET;
    }

    public static BarColor getOnyBossBarColor() {
        return ONY_BOSS_BAR_COLOR;
    }

    public static double getOnyEconomyReward() {
        return ONY_ECONOMY_REWARD;
    }

    public static int getOnySpawnMobsCooldown() {
        return ONY_SPAWN_MOBS_COOLDOWN;
    }

    public static String getOnyBossBarTitle() {
        return ONY_BOSS_BAR_TITLE;
    }

    public static String getOnyDeathMessage() {
        return ONY_DEATH_MESSAGE;
    }

    public static String getOnyEconomyRewardMessage() {
        return ONY_ECONOMY_REWARD_MESSAGE;
    }

    public static double getOnyHealth() {
        return ONY_HEALTH;
    }

    public static String getOnyName() {
        return ONY_NAME;
    }

    public static boolean checkOnyBoss(LivingEntity entity) {
        if (entity.getType() == EntityType.IRON_GOLEM && (entity.getCustomName().equals(ONY_NAME))) {
            return true;
        } else {
            return false;
        }
    }

    private static String format(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
