package com.codebro.vlaskaplugin.events;

import com.codebro.vlaskaplugin.bosses.OnyBoss;

import com.codebro.vlaskaplugin.bosses.OnyBoss;
import com.codebro.vlaskaplugin.bossitems.OnyHelmet;
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
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Optional;

public class OnyBossListener implements Listener {
    private int counterSpawnPigZ = 0;

    private BossBar onyBossBar = Bukkit.createBossBar(OnyBoss.getOnyBossBarTitle(), OnyBoss.getOnyBossBarColor(), BarStyle.SOLID);

    private Plugin plugin;
    private Economy econ;

    public OnyBossListener(Plugin pl, Economy ec) {
        plugin = pl;
        econ = ec;
    }

    private static String format(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public void spawnPigZombie(Location location, Player player) {
        PotionEffect fireResistance = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 2147483647, 1);
        PigZombie zombie1 = (PigZombie) location.getWorld().spawnEntity(location, EntityType.ZOMBIFIED_PIGLIN);
        zombie1.setTarget(player);
        zombie1.setShouldBurnInDay(false);
        zombie1.addPotionEffect(fireResistance);
        zombie1.getEquipment().setChestplate(new ItemStack(Material.GOLDEN_CHESTPLATE));
        zombie1.getEquipment().setHelmet(new ItemStack(Material.GOLDEN_HELMET));
        AttributeInstance attribute1 = zombie1.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        attribute1.setBaseValue(zombie1.getHealth() * 1.2D);
        zombie1.setHealth(zombie1.getHealth() * 1.2D);
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
        if (event.getEntity() instanceof LivingEntity) {
            if (OnyBoss.checkOnyBoss((LivingEntity) event.getEntity())) {
                Golem golem = (Golem) event.getEntity();
                Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(214, 6, 6), 5.0F);
                Particle.DustOptions dustOptions2 = new Particle.DustOptions(Color.fromRGB(214, 6, 6), 1.0F);
                bossParticles(dustOptions, dustOptions2, golem.getLocation());

                for (Entity entity : golem.getNearbyEntities(50, 50, 50)) {
                    if (entity instanceof Player) {
                        Player player = (Player) entity;
                        onyBossBar.addPlayer(player);
                        onyBossBar.setProgress(golem.getHealth() / golem.getMaxHealth());
                    }
                }


                counterSpawnPigZ++;

                if (counterSpawnPigZ >= OnyBoss.getOnySpawnMobsCooldown()) {
                    golem.getWorld().createExplosion(golem.getLocation(), 3.0F, true, false);

                    int count = (int) (Math.random() * 100.0D);
                    Location location = golem.getLocation();
                    new BukkitRunnable(){

                        @Override
                        public void run() {
                            if (count % 2 == 0) {
                                for (Entity entity : golem.getNearbyEntities(20, 20, 20)) {
                                    if (entity instanceof Player) {
                                        spawnPigZombie(location, (Player) entity);
                                        spawnPigZombie(location, (Player) entity);
                                        spawnPigZombie(location, (Player) entity);
                                        spawnPigZombie(location, (Player) entity);
                                        break;
                                    }
                                }
                            } else {
                                for (Entity entity : golem.getNearbyEntities(20, 20, 20)) {
                                    if (entity instanceof Player) {
                                        spawnPigZombie(location, (Player) entity);
                                        spawnPigZombie(location, (Player) entity);
                                        spawnPigZombie(location, (Player) entity);
                                        spawnPigZombie(location, (Player) entity);
                                        spawnPigZombie(location, (Player) entity);
                                        spawnPigZombie(location, (Player) entity);
                                        break;
                                    }
                                }
                            }
                        }
                    }.runTaskLater(plugin, 15L);

                    counterSpawnPigZ = 0;



                }


            }
        }
    }
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (OnyBoss.checkOnyBoss(event.getEntity())) {
            Golem golem = (Golem) event.getEntity();

            event.setDeathSound(Sound.ENTITY_ENDER_DRAGON_DEATH);

            event.getEntity().getServer().broadcast(Component.text(format("")));
            event.getEntity().getServer().broadcast(Component.text(OnyBoss.getOnyDeathMessage()));
            event.getEntity().getServer().broadcast(Component.text(format("")));

            if (golem.getKiller() instanceof Player) {
                Player player = golem.getKiller();
                player.sendMessage(Component.text(OnyBoss.getOnyEconomyRewardMessage()));
                econ.depositPlayer(player, OnyBoss.getOnyEconomyReward());
            }

            event.getDrops().clear();
            if (Math.random() < OnyBoss.getOnyDropChanceHelmet()) {
                ItemStack onyHelmet = new OnyHelmet().getOnyHelmet();
                event.getDrops().add(onyHelmet);
            }
            event.setDroppedExp(390);

            onyBossBar.setVisible(false);
            onyBossBar.removeAll();
        }
    }
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof LivingEntity) {
            if (OnyBoss.checkOnyBoss((LivingEntity) event.getEntity())) {
                Golem golem = (Golem) event.getEntity();
                if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) || event.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) || event.getCause().equals(EntityDamageEvent.DamageCause.FALL) || event.getCause().equals(EntityDamageEvent.DamageCause.FALLING_BLOCK) || event.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) || event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
                    AttributeInstance attribute = golem.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                    attribute.setBaseValue(golem.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() + 20.0D);
                    golem.setHealth(golem.getHealth() + 15.0);
                    event.setCancelled(true);
                }
            }
        }
    }
}
