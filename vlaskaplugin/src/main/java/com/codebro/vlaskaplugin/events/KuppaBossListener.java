package com.codebro.vlaskaplugin.events;

import com.codebro.vlaskaplugin.bosses.KuppaBoss;
import com.codebro.vlaskaplugin.bossitems.KuppaTrident;
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
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class KuppaBossListener implements Listener {
    private int counterSpawnZombie = 0;
    private int counterShield = 0;

    private Location fromWhere;

    private BossBar kuppaBossBar = Bukkit.createBossBar(KuppaBoss.getKuppaName(), KuppaBoss.getKuppaBossBarColor(), BarStyle.SOLID);


    private Plugin plugin;
    private Economy econ;

    public KuppaBossListener(Plugin pl, Economy ec) {
        plugin = pl;
        econ = ec;
    }

    public void spawnZombie(Location location, Player player) {
        PotionEffect fireResistance = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 2147483647, 1);

        Drowned zombie1 = (Drowned) location.getWorld().spawnEntity(location, EntityType.DROWNED);

        zombie1.setTarget(player);
        zombie1.setShouldBurnInDay(false);
        zombie1.addPotionEffect(fireResistance);

        AttributeInstance attribute1 = zombie1.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        attribute1.setBaseValue(zombie1.getHealth() * 1.5);
        zombie1.setHealth(zombie1.getHealth() * 1.5);
    }

    private void bossParticles(Particle.DustOptions dustOptionsMain, Particle.DustOptions dustOptionsSmall, Location location) {
        World world = location.getWorld();

        world.spawnParticle(Particle.REDSTONE, location.clone().add(0.0D, 1.7D, 0.0D), 0, 0.1D, 2.0D, 0.1D, dustOptionsMain);
        world.spawnParticle(Particle.REDSTONE, location.clone().add(0.8D, 1.7D, 0.9D), 0, 0.3D, 0.5D, 0.4D, dustOptionsSmall);
        world.spawnParticle(Particle.REDSTONE, location.clone().add(0.6D, 1.7D, 0.7D), 0, 0.1D, 0.5D, 0.2D, dustOptionsSmall);
        world.spawnParticle(Particle.REDSTONE, location.clone().add(-0.8D, 1.7D, -0.6D), 0, -0.4D, 0.3D, -0.1D, dustOptionsSmall);
        world.spawnParticle(Particle.REDSTONE, location.clone().add(-0.9D, 1.7D, 0.6D), 0, -0.4D, 0.8D, 0.1D, dustOptionsSmall);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager().toString() == "CraftTrident" || event.getDamager().toString().equals("CraftTrident")) {
            Trident trident = (Trident) event.getDamager();
            if (trident.getShooter() instanceof Zombie) {
                Zombie zombie = (Zombie) trident.getShooter();
                if (KuppaBoss.checkKuppaBoss(zombie)) {
                    if (event.getEntity() instanceof Player) {
                        Player player = (Player) event.getEntity();
                        if (player.getEquipment().getItemInOffHand().getType() == Material.SHIELD) {
                            counterShield++;
                            if (counterShield == KuppaBoss.getKuppaBreakShieldCooldown()) {
                                player.getEquipment().setItemInOffHand(new ItemStack(Material.AIR));
                                player.playSound(player.getLocation(), Sound.ITEM_SHIELD_BREAK, 1.0F, 1.0F);
                                counterShield = 0;
                            }
                        } else if (player.getEquipment().getItemInMainHand().getType() == Material.SHIELD) {
                            counterShield++;
                            if (counterShield == KuppaBoss.getKuppaBreakShieldCooldown()) {
                                player.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
                                player.playSound(player.getLocation(), Sound.ITEM_SHIELD_BREAK, 1.0F, 1.0F);
                                counterShield = 0;
                            }

                        }
                    }
                }
            }
        }
        if (KuppaBoss.checkKuppaBoss(event.getEntity())) {
            Zombie zombie = (Zombie) event.getEntity();
            kuppaBossBar.setVisible(true);

            for (Entity entity : zombie.getNearbyEntities(50, 50, 50)) {
                if (entity instanceof Player) {
                    Player player = (Player) entity;
                    kuppaBossBar.addPlayer(player);
                    kuppaBossBar.setProgress(zombie.getHealth() / zombie.getMaxHealth());
                }
            }
            counterSpawnZombie++;
            if (counterSpawnZombie >= KuppaBoss.getKuppaSpawnZombieCooldown()) {
                int count = (int) (Math.random() * 100.0D);
                Location location = event.getEntity().getLocation();
                if (count % 2 == 0) {
                    for (Entity entity : zombie.getNearbyEntities(20, 20, 20)) {
                        if (entity instanceof Player) {
                            Player player = (Player) entity;
                            spawnZombie(location.clone(), player);
                            spawnZombie(location.clone().add(1.0D, 1.0D, 0.0D), player);
                            spawnZombie(location.clone().add(-2.0D, 0.0D, 0.0D), player);
                            spawnZombie(location.clone().add(1.0D, 0.0D, 1.0D), player);
                            break;
                        }
                    }
                } else {
                    for (Entity entity : zombie.getNearbyEntities(20, 20, 20)) {
                        if (entity instanceof Player) {
                            Player player = (Player) entity;
                            spawnZombie(location.clone(), player);
                            spawnZombie(location.clone().add(0.0D, 1.0D, 1.0D), player);
                            spawnZombie(location.clone().add(-1.0D, 0.0D, -1.0D), player);
                            spawnZombie(location.clone().add(1.0D, 0.0D, -2.0D), player);
                            break;
                        }
                    }
                }
                counterSpawnZombie = 0;
            }

            Particle.DustOptions dustOptions2 = new Particle.DustOptions(Color.fromRGB(6, 156, 71), 5.0F);
            Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(6, 156, 71), 1.0F);

            bossParticles(dustOptions2, dustOptions, zombie.getLocation());


        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (KuppaBoss.checkKuppaBoss(event.getEntity())) {
            Zombie zombie = (Zombie) event.getEntity();

            event.setDeathSound(Sound.ENTITY_ENDER_DRAGON_DEATH);

            event.getEntity().getServer().broadcast(Component.text(""));
            event.getEntity().getServer().broadcast(Component.text(KuppaBoss.getKuppaDeathMessage()));
            event.getEntity().getServer().broadcast(Component.text(""));

            if (zombie.getKiller() instanceof Player) {
                Player player = zombie.getKiller();
                player.sendMessage(Component.text(KuppaBoss.getKuppaEconomyRewardMessage()));
                econ.depositPlayer(player, KuppaBoss.getKuppaEconomyReward());
            }

            event.getDrops().clear();

            if (Math.random() < KuppaBoss.getKuppaTridentDropChance()) {
//                ItemStack ureyBow = new UreyBow().getBow();
//                event.getDrops().add(ureyBow);
            }
            event.setDroppedExp(400);

            kuppaBossBar.setVisible(false);
            kuppaBossBar.removeAll();
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (event.getEntity().getType() == EntityType.TRIDENT) {
            if (event.getEntity().getShooter() instanceof LivingEntity) {
                LivingEntity le = (LivingEntity) event.getEntity().getShooter();
                if (KuppaBoss.checkKuppaBoss(le)) {
                    Zombie zombie = (Zombie) event.getEntity().getShooter();
                    fromWhere = zombie.getLocation();
                }
            }

        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity().getShooter() instanceof LivingEntity) {
            if (event.getEntity().getShooter() instanceof LivingEntity) {
                LivingEntity le = (LivingEntity) event.getEntity().getShooter();
                if (KuppaBoss.checkKuppaBoss(le)) {
                    Zombie zombie = (Zombie) le;
                    if (event.getHitBlock() == null) {
                        if (event.getHitEntity() instanceof Player) {
                            Player player = (Player) event.getHitEntity();
                            if ((player.getInventory().getItemInMainHand().getType() != Material.SHIELD) || (player.getInventory().getItemInOffHand().getType() != Material.SHIELD)) {
                                KuppaTrident.addKuppaTridentEffects(player);
                            }
                        } else if (event.getHitEntity() instanceof LivingEntity) {
                            LivingEntity livingEntity = (LivingEntity) event.getHitEntity();
                            KuppaTrident.addKuppaTridentEffects(livingEntity);

                            Location location = livingEntity.getLocation();
                            KuppaTrident.pullEntity(zombie, fromWhere, location.clone().add(0.0, 0.7, 0.0));

                        } else if (event.getHitEntity() == null) {
                            Location location = event.getHitBlock().getLocation();
                            if (fromWhere != null) {
                                KuppaTrident.pullEntity(zombie, fromWhere, location.clone().add(0.0, 0.7, 0.0));
                            }
                        }
                    }
                }

            }
        }
    }
}
