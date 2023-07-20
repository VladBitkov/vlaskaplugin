package com.codebro.vlaskaplugin.bosses;

import com.codebro.vlaskaplugin.bossitems.UreyBow;
import com.codebro.vlaskaplugin.bossitems.UreyChestplate;
import com.codebro.vlaskaplugin.bossitems.UreyLeggings;
import com.ssomar.score.api.executableitems.ExecutableItemsAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Optional;

public class UreyBoss {
    private static final int UREY_ARROW_COOLDOWN = 8;
    private static final int UREY_SPAWN_SILVERISH_COOLDOWN = 6;
    private static final int UREY_BREAK_SHIELD_COOLDOWN = 3;

    private static final int UREY_DROP_CHANCE_ARROW_AMOUNT = 16;
    private static final double UREY_DROP_CHANCE_BOW = 0.08;
    private static final double UREY_DROP_CHANCE_ARROW = 0.2;

    private static final double UREY_HEALTH = 100.0;
    private static final double UREY_ECONOMY_REWARD = 300.0;

    private static final String UREY_NAME = format("&5&lЮрэй");
    private static final String UREY_BOSS_BAR_TITLE = format("&5&lЮрэй");
    private static final String UREY_DEATH_MESSAGE = format("&7&l[&6&lVl-ASKA&7&l] &6&lВнимание&f, &cнечисть &fпод именем: " + UreyBoss.getUreyName() + "&f, &cубили&f. Поздравьте героев!");
    private static final String UREY_ECONOMY_REWARD_MESSAGE = format("&7&l[&6&lVl-ASKA&7&l] &6&lРеспект! &fЗа &cубийство &fэтой &4нечисти &eты &fполучаешь: &6" + UreyBoss.getUreyEconomyReward() + "Ɏ&f.");


    private static final BarColor UREY_BOSS_BAR_COLOR = BarColor.PURPLE;

    private Plugin plugin;

//    private static BossBar ureyBossBar = null;


    public UreyBoss(Skeleton skeleton, Plugin pl) {
        plugin = pl;

        skeleton.setCustomName(UREY_NAME);
        skeleton.setCustomNameVisible(true);

        skeleton.setRemoveWhenFarAway(false);
        skeleton.setShouldBurnInDay(false);

        skeleton.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 3));
        skeleton.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 2));

        EntityEquipment skeletonEquipment = skeleton.getEquipment();

        skeletonEquipment.setChestplate(new UreyChestplate(new ItemStack(Material.LEATHER_CHESTPLATE)).getChestplate());
        skeletonEquipment.setLeggings(new UreyLeggings(new ItemStack(Material.LEATHER_LEGGINGS)).getLeggings());

        skeletonEquipment.setItemInOffHand(ExecutableItemsAPI.getExecutableItemsManager().getExecutableItem("UreyArrow").get().buildItem(1, Optional.empty()));
        skeletonEquipment.setItemInMainHand(new UreyBow().getBow());

        skeleton.setArrowCooldown((int) ((double) skeleton.getArrowCooldown() / 1.5D));

//        skeleton.getPersistentDataContainer().set(NamespacedKey.fromString("explosiveArrowCooldown"), PersistentDataType.INTEGER, 0);

        metaDataArrowValue metaDataValue = new metaDataArrowValue(plugin, 0);
        skeleton.setMetadata("shootedArrow", metaDataValue);

        AttributeInstance attribute = skeleton.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        attribute.setBaseValue(UREY_HEALTH);
        skeleton.setMaxHealth(UREY_HEALTH);
        skeleton.setHealth(UREY_HEALTH);


    }

    public static int getUreyBreakShieldCooldown() {
        return UREY_BREAK_SHIELD_COOLDOWN;
    }

    public static int getUreySpawnSilverishCooldown() {
        return UREY_SPAWN_SILVERISH_COOLDOWN;
    }

    public static double getUreyDropChanceArrow() {
        return UREY_DROP_CHANCE_ARROW;
    }

    public static double getUreyDropChanceBow() {
        return UREY_DROP_CHANCE_BOW;
    }

    public static int getUreyDropChanceArrowAmount() {
        return UREY_DROP_CHANCE_ARROW_AMOUNT;
    }

    public static int getUreyArrowCooldown() {
        return UREY_ARROW_COOLDOWN;
    }

    public static String getUreyEconomyRewardMessage() {
        return UREY_ECONOMY_REWARD_MESSAGE;
    }

    public static String getUreyDeathMessage() {
        return UREY_DEATH_MESSAGE;
    }

    public static double getUreyEconomyReward() {
        return UREY_ECONOMY_REWARD;
    }

    public static String getUreyBossBarTitle() {
        return UREY_BOSS_BAR_TITLE;
    }

    public static BarColor getUreyBossBarColor() {
        return UREY_BOSS_BAR_COLOR;
    }

    public static String getUreyName() {
        return UREY_NAME;
    }

    public static double getUreyHealth() {
        return UREY_HEALTH;
    }

    public static boolean checkUreyBoss(Entity entity) {
        if (entity.getType() == EntityType.SKELETON) {
            Skeleton skeleton = (Skeleton) entity;
            if (skeleton.getCustomName() != null && skeleton.getCustomName().equals(UREY_NAME)) {
                return true;
            }
        }
        return false;
    }

    private static String format(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
