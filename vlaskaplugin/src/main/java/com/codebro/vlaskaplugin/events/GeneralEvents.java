package com.codebro.vlaskaplugin.events;

import com.codebro.vlaskaplugin.bosses.KuppaBoss;
import com.codebro.vlaskaplugin.bossitems.KuppaTrident;
import com.codebro.vlaskaplugin.bossitems.UreyBow;
import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.ssomar.score.api.executableitems.ExecutableItemsAPI;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.PluginLoadOrder;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class GeneralEvents implements Listener {

    private final EntityType[] bossList = new EntityType[]{EntityType.SKELETON, EntityType.DROWNED, EntityType.IRON_GOLEM};

    private Location fromWhere;
    private HashMap<String, Long> cooldowns = new HashMap();

    //    private final String[] bossItemsNames = new String[]{};

    @EventHandler
    public void playerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        if (message.contains("/k kingdom create") && !player.hasPermission("beKing")) {
            player.sendMessage(format("&cИзвините, у вас нет прав для этого, купите привелегию &4/donate"));
            event.setCancelled(true);
        }

        if (message.contains("/k settle") && !player.hasPermission("beLord")) {
            player.sendMessage(format("&cИзвините, у вас нет прав для этого, купите привелегию &4/donate"));
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onPlayerArmorChange(PlayerArmorChangeEvent event) {
        if (event.getNewItem().getType() == Material.DIAMOND_HELMET) {
            ItemStack diamondHelmet = event.getNewItem();
            if (diamondHelmet.getItemMeta().getPersistentDataContainer().get(NamespacedKey.fromString("name"), PersistentDataType.STRING).equals("OnyHelmet")){
                Player player = event.getPlayer();

                PotionEffect strength = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false);
                PotionEffect fireResistance = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false);
                PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, 2147483647, 0, false, false);

                player.addPotionEffect(strength);
                player.addPotionEffect(fireResistance);
                player.addPotionEffect(slow);
            }
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (event.getEntity().getType() == EntityType.TRIDENT) {

            if (event.getEntity().getShooter() instanceof Player) {
                Player player = (Player) event.getEntity().getShooter();
                fromWhere = player.getLocation();
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity().getShooter() instanceof Player) {
            Player player = (Player) event.getEntity().getShooter();
            if(event.getEntity().getType() == EntityType.TRIDENT) {
                Trident trident = (Trident) event.getEntity();
                if(KuppaTrident.checkKuppaTrident(trident.getItemStack())){

                    if(event.getHitEntity() == null) {
                        if (cooldowns.containsKey(player.getName())) {
                            long cooldownTime = cooldowns.get(player.getName());
                            long currentTime = System.currentTimeMillis();

                            if (currentTime < cooldownTime) {
                                long remainingTime = (cooldownTime - currentTime) / 1000;
                                player.sendMessage(KuppaTrident.getKuppaCooldownMessageFirst() + remainingTime + KuppaTrident.getKuppaCooldownMessageLast());
                                event.setCancelled(true);
                                return;
                            }
                        }

                            int cooldownSeconds = KuppaTrident.getKuppaCooldown();
                            long cooldownMillis = (long) (cooldownSeconds * 1000);
                            long newCooldownTime = System.currentTimeMillis() + cooldownMillis;
                            cooldowns.put(player.getName(), newCooldownTime);
                            if(fromWhere != null) {
                                Location location = event.getHitBlock().getLocation();
                                KuppaTrident.pullEntity(player, fromWhere, location.add(0.0D, 2.0D, 0.0D));
                            }

                    }
                    else if(event.getHitBlock() == null){
                        if(event.getHitEntity() instanceof LivingEntity) {
                            LivingEntity injured = (LivingEntity) event.getHitEntity();
                            KuppaTrident.addKuppaTridentEffects(injured);
                        }
                    }
                }
            }
        }
    }


    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
//        if((event.getDamager() instanceof Player) || (KuppaBoss.checkKuppaBoss(event.getDamager()))){
//            Player player = (Player) event.getDamager();
//            System.out.println("before check contains in EntityDamageByEntityEvent");
//            if(player.getInventory().contains(Material.TRIDENT)){
//                System.out.println("after check contains in EntityDamageByEntityEvent");
//
////                if(KuppaTrident.checkKuppaTrident(player.getInventory().))
//                ItemStack[] contents = player.getInventory().getContents();
//                for (ItemStack item : contents) {
//                    if (item != null && item.getType() == Material.TRIDENT) {
//                        if(KuppaTrident.checkKuppaTrident(item)){
//                            if(event.getEntity() instanceof LivingEntity) {
//
//                                break;
//                            }
//                        }
//                    }
//                }
//            }
//
//        }
    }

    @EventHandler
    public void onEntityTargetLivingEntity(EntityTargetLivingEntityEvent event) {
        if (event.getTarget() != null && event.getEntity() != null) {
            for(EntityType et: bossList){
                if((event.getTarget().getType() == et)){
                    event.setCancelled(true);
                }
                else if(event.getEntity().getType() == et){
                    if(!(event.getTarget() instanceof Player)){
                        event.setCancelled(true);
                    }
                }
            }

        }
    }


    @EventHandler
    public void onEntityShootBowEvent(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (UreyBow.checkUreyBow(event.getBow())) {
                if (event.getProjectile() instanceof Arrow) {
//                    System.out.println("event.getProjectile() instanceof  Arrow");
                    Arrow arrow = (Arrow) event.getProjectile();
                    arrow.addCustomEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 0), true);
                    arrow.addCustomEffect(new PotionEffect(PotionEffectType.POISON, 40, 1), true);
                }

            }
        }
    }

    @EventHandler
    public void onPlayerPickupArrowEvent(PlayerPickupArrowEvent event) {
        if(event.getItem() instanceof Arrow) {
            event.setCancelled(true);
        }
        if(event.getArrow() instanceof Arrow){
            event.setCancelled(true);
        }

    }
    private String format(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}
