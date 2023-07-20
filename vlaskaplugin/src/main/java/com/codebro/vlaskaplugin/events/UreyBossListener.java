package com.codebro.vlaskaplugin.events;

import com.codebro.vlaskaplugin.bosses.UreyBoss;
import com.codebro.vlaskaplugin.bossitems.UreyBow;
import com.ssomar.score.api.executableitems.ExecutableItemsAPI;
import net.kyori.adventure.text.Component;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Optional;

public class UreyBossListener implements Listener {
    private final double UREY_DROP_CHANCE_BOW = UreyBoss.getUreyDropChanceBow();
    private final double UREY_DROP_CHANCE_ARROW = UreyBoss.getUreyDropChanceArrow();
    private final int UREY_DROP_CHANCE_ARROW_AMOUNT = UreyBoss.getUreyDropChanceArrowAmount();

    private int counterSpawnSn = 0;
    private int counterShield = 0;

    private BossBar ureyBossBar = Bukkit.createBossBar(UreyBoss.getUreyBossBarTitle(), UreyBoss.getUreyBossBarColor(), BarStyle.SOLID);

    private Plugin plugin;
    private Economy econ;


    public UreyBossListener(Plugin pl, Economy ec) {
        plugin = pl;
        econ = ec;
    }

    private static String format(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity().getShooter().toString().equals("CraftSkeleton")) {
            Skeleton skeleton = (Skeleton) event.getEntity().getShooter();
//            int wasUreyArrowCooldown = skeleton.getPersistentDataContainer().get(NamespacedKey.fromString("explosiveArrowCooldown"), PersistentDataType.INTEGER);
//            skeleton.getPersistentDataContainer().set(NamespacedKey.fromString("explosiveArrowCooldown"), PersistentDataType.INTEGER, wasUreyArrowCooldown + 1);
//            int currentUreyArrowCooldown = skeleton.getPersistentDataContainer().get(NamespacedKey.fromString("explosiveArrowCooldown"), PersistentDataType.INTEGER);
//            if(currentUreyArrowCooldown >= UreyBoss.getUreyArrowCooldown()){
//                event.getEntity().getWorld().createExplosion(event.getEntity().getLocation(), 3.0F, false, false);
//                skeleton.getPersistentDataContainer().set(NamespacedKey.fromString("explosiveArrowCooldown"), PersistentDataType.INTEGER, 0);
//            }
            if (skeleton.getMetadata("shootedArrow").size() > 0 && skeleton.getMetadata("shootedArrow").get(0) != null) {
                MetadataValue mdV = (MetadataValue) skeleton.getMetadata("shootedArrow").get(0);
                if (mdV.value() instanceof Integer) {
                    int count = (Integer) mdV.value();
                    if (count >= UreyBoss.getUreyArrowCooldown() + UreyBoss.getUreyArrowCooldown()) {
                        event.getEntity().getWorld().createExplosion(event.getEntity().getLocation(), 3.0F, true, false);
                        mdV.asInt();
                    }
                }
            }
        }
    }

    private void bossParticles(Particle.DustOptions dustOptionsMain, Particle.DustOptions dustOptionsSmall, Location location) {
        World world = location.getWorld();
        world.spawnParticle(Particle.REDSTONE, location.clone().add(0.0D, 1.7D, 0.0D), 0, 0.1D, 2.0D, 0.1D, dustOptionsMain);
        world.spawnParticle(Particle.REDSTONE, location.clone().add(0.8D, 1.7D, 0.9D), 0, 0.3D, 0.5D, 0.4D, dustOptionsSmall);
        world.spawnParticle(Particle.REDSTONE, location.clone().add(0.6D, 1.7D, 0.7D), 0, 0.1D, 0.5D, 0.2D, dustOptionsSmall);
        world.spawnParticle(Particle.REDSTONE, location.clone().add(-0.8D, 1.7D, -0.6D), 0, -0.4D, 0.3D, -0.1D, dustOptionsSmall);
        world.spawnParticle(Particle.REDSTONE, location.clone().add(-0.9D, 1.7D, 0.6D), 0, -0.4D, 0.8D, 0.1D, dustOptionsSmall);
    }

    private void spawnSilverfish(Location location, Player player) {
        PotionEffect fireResistance = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 2147483647, 2);
        Silverfish silverfish1 = (Silverfish) location.getWorld().spawnEntity(location.clone(), EntityType.SILVERFISH);

        silverfish1.setTarget(player);
        silverfish1.addPotionEffect(fireResistance);

        AttributeInstance attribute1 = silverfish1.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        attribute1.setBaseValue(silverfish1.getHealth() * 1.5D);
        silverfish1.setHealth(silverfish1.getHealth() * 1.5D);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager().toString().equals("CraftTippedArrow")) {
            Arrow arrow = (Arrow) event.getDamager();
            if (arrow.getShooter() instanceof Skeleton) {
                Skeleton skeleton = (Skeleton) arrow.getShooter();
                if (UreyBoss.checkUreyBoss(skeleton)) {
                    if (event.getEntity() instanceof Player) {
                        Player player = (Player) event.getEntity();
                        if (player.getEquipment().getItemInOffHand().getType() == Material.SHIELD) {
                            counterShield++;
                            if (counterShield == UreyBoss.getUreyBreakShieldCooldown()) {
                                player.getEquipment().setItemInOffHand(new ItemStack(Material.AIR));
                                player.playSound(player.getLocation(), Sound.ITEM_SHIELD_BREAK, 1.0F, 1.0F);
                                counterShield = 0;
                            }
                        } else if (player.getEquipment().getItemInMainHand().getType() == Material.SHIELD) {
                            counterShield++;
                            if (counterShield == UreyBoss.getUreyBreakShieldCooldown()) {
                                player.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
                                player.playSound(player.getLocation(), Sound.ITEM_SHIELD_BREAK, 1.0F, 1.0F);
                                counterShield = 0;
                            }

                        }
                    }
                }
            }
        }
        if (UreyBoss.checkUreyBoss(event.getEntity())) {
            Skeleton skeleton = (Skeleton) event.getEntity();

            ureyBossBar.setProgress(skeleton.getHealth() / skeleton.getMaxHealth());
            ureyBossBar.setVisible(true);

            for (Entity entity : skeleton.getNearbyEntities(50, 50, 50)) {
                if (entity instanceof Player) {
                    Player player = (Player) entity;
                    ureyBossBar.addPlayer(player);
                    ureyBossBar.setProgress(skeleton.getHealth() / skeleton.getMaxHealth());
                }
            }

            World world = skeleton.getWorld();

            Particle.DustOptions dustOptions2 = new Particle.DustOptions(Color.fromRGB(43, 0, 43), 5.0F);
            Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(43, 0, 43), 1.0F);

            bossParticles(dustOptions2, dustOptions, skeleton.getLocation());

            counterSpawnSn++;
            if (counterSpawnSn >= UreyBoss.getUreySpawnSilverishCooldown()) {
                int count = (int) (Math.random() * 100.0D);
                Location location = event.getEntity().getLocation();
                if (count % 2 == 0) {
                    for (Entity entity : skeleton.getNearbyEntities(20, 20, 20)) {
                        if (entity instanceof Player) {
                            Player player = (Player) entity;

                            spawnSilverfish(location.clone(), player);
                            spawnSilverfish(location.clone().add(1.0D, 1.0D, 0.0D), player);
                            spawnSilverfish(location.clone().add(-2.0D, 0.0D, 0.0D), player);
                            spawnSilverfish(location.clone().add(1.0D, 0.0D, 1.0D), player);
                            break;
                        }
                    }
                } else {
                    for (Entity entity : skeleton.getNearbyEntities(20, 20, 20)) {
                        if (entity instanceof Player) {
                            Player player = (Player) entity;

                            spawnSilverfish(location.clone().add(0.0D, 1.0D, 1.0D), player);
                            spawnSilverfish(location.clone().add(-1.0D, 0.0D, -1.0D), player);
                            spawnSilverfish(location.clone().add(1.0D, 0.0D, -2.0D), player);
                            break;
                        }
                    }
                }
            }

            counterSpawnSn = 0;
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (UreyBoss.checkUreyBoss(event.getEntity())) {
            Skeleton skeleton = (Skeleton) event.getEntity();
            if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) || event.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) || event.getCause().equals(EntityDamageEvent.DamageCause.FALL) || event.getCause().equals(EntityDamageEvent.DamageCause.FALLING_BLOCK) || event.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) || event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
                AttributeInstance attribute = skeleton.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                attribute.setBaseValue(skeleton.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() + 20.0D);
                skeleton.setHealth(skeleton.getHealth() + 15.0);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (UreyBoss.checkUreyBoss(event.getEntity())) {
            Skeleton skeleton = (Skeleton) event.getEntity();

            event.setDeathSound(Sound.ENTITY_ENDER_DRAGON_DEATH);

            event.getEntity().getServer().broadcast(Component.text(format("")));
            event.getEntity().getServer().broadcast(Component.text(UreyBoss.getUreyDeathMessage()));
            event.getEntity().getServer().broadcast(Component.text(format("")));

            if (skeleton.getKiller() instanceof Player) {
                Player player = skeleton.getKiller();
                player.sendMessage(Component.text(UreyBoss.getUreyEconomyRewardMessage()));
                econ.depositPlayer(player, UreyBoss.getUreyEconomyReward());
            }

            event.getDrops().clear();
            if (Math.random() < this.UREY_DROP_CHANCE_BOW) {
                ItemStack ureyBow = new UreyBow().getBow();
                event.getDrops().add(ureyBow);
            }

            if (Math.random() < this.UREY_DROP_CHANCE_ARROW) {
                for (int i = 0; i < UREY_DROP_CHANCE_ARROW_AMOUNT; i++) {
                    event.getDrops().add(ExecutableItemsAPI.getExecutableItemsManager().getExecutableItem("UreyArrow").get().buildItem(1, Optional.empty()));
                }
            }
            event.setDroppedExp(350);

            ureyBossBar.setVisible(false);
            ureyBossBar.removeAll();
        }
    }

    @EventHandler
    public void onEntityShootBowEvent(EntityShootBowEvent event) {
        if (UreyBoss.checkUreyBoss(event.getEntity())) {
            Arrow arrow = (Arrow) event.getProjectile();
            World world = arrow.getWorld();
            if (plugin != null) {
                (new BukkitRunnable() {
                    public void run() {
                        if (!arrow.isOnGround() && !arrow.isDead() && !arrow.isInBlock()) {
                            Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(102, 0, 102), 1.0F);
                            world.spawnParticle(Particle.REDSTONE, arrow.getLocation(), 0, 0.1D, 0.1D, 0.1D, dustOptions);
                        }
                    }
                }).runTaskTimerAsynchronously(plugin, 2L, 2L);
            }
        }
    }
}
