package com.codebro.vlaskaplugin.bossitems;

import com.codebro.vlaskaplugin.bosses.KuppaBoss;
import com.ssomar.score.api.executableitems.ExecutableItemsAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Trident;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class KuppaTrident {
    private final static String KUPPA_TRIDENT_NAME = format("&6\ud83d\udd31 &f&lТрезубец &2&lКаппы &6\ud83d\udd31");
    private final static String KUPPA_COOLDOWN_MESSAGE_FIRST = format("&fПодождите &6");
    private final static String KUPPA_COOLDOWN_MESSAGE_LAST = format(" секунд(-ы) &fперед использованием &aэтого свойства предмета: " + KUPPA_TRIDENT_NAME);

    private final static int KUPPA_COOLDOWN = 30;

    private ItemStack trident;

    public ItemStack getTrident() {
        return trident;
    }

    public static String getKuppaCooldownMessageFirst() {
        return KUPPA_COOLDOWN_MESSAGE_FIRST;
    }

    public static String getKuppaTridentName() {
        return KUPPA_TRIDENT_NAME;
    }

    public static String getKuppaCooldownMessageLast() {
        return KUPPA_COOLDOWN_MESSAGE_LAST;
    }

    public static int getKuppaCooldown() {
        return KUPPA_COOLDOWN;
    }

    public static LivingEntity addKuppaTridentEffects(LivingEntity injured){
        PotionEffect poison = new PotionEffect(PotionEffectType.POISON, 40, 1);
        injured.addPotionEffect(poison);
        PotionEffect glowing = new PotionEffect(PotionEffectType.GLOWING, 400, 0);
        injured.addPotionEffect(glowing, true);
        PotionEffect weakness = new PotionEffect(PotionEffectType.WEAKNESS, 100, 0);
        injured.addPotionEffect(weakness);

        return injured;
    }

    public static boolean checkKuppaTrident(ItemStack t){
        if(t.getItemMeta().getPersistentDataContainer().get(NamespacedKey.fromString("name"), PersistentDataType.STRING) != null) {
            if (t.getItemMeta().getPersistentDataContainer().get(NamespacedKey.fromString("name"), PersistentDataType.STRING) == "KuppaTrident") {

                return true;
            }
        }
        return false;
    }
    public static void pullEntity(LivingEntity entity, Location fromWhere, Location targetLocation) {
        Vector direction = targetLocation.toVector().subtract(fromWhere.toVector()).normalize();
        double substractX = Math.abs(Math.abs(fromWhere.getX()) - Math.abs(targetLocation.getX()));
        double substractY = Math.abs(Math.abs(fromWhere.getY()) - Math.abs(targetLocation.getY()));
        double substractZ = Math.abs(Math.abs(fromWhere.getZ()) - Math.abs(targetLocation.getZ()));
        double speed = 10.0D;
        if (substractX > 5.0D || substractZ > 5.0D || substractY > 5.0D) {
            if (entity instanceof Zombie && entity.getCustomName() != null) {
                if(KuppaBoss.checkKuppaBoss(entity)) {
                    speed = 2.5D;
                }
            }
            entity.setVelocity(direction.multiply(speed));
        }

    }

    public KuppaTrident(){
        trident = ExecutableItemsAPI.getExecutableItemsManager().getExecutableItem("KuppaTrident").get().buildItem(1, Optional.empty());

        ItemMeta tridentMeta = trident.getItemMeta();
        tridentMeta.setUnbreakable(true);
        tridentMeta.addEnchant(Enchantment.CHANNELING, 1, true);
        tridentMeta.addEnchant(Enchantment.IMPALING, 5, true);
        tridentMeta.addEnchant(Enchantment.LOYALTY, 3, true);

        tridentMeta.displayName(Component.text(KUPPA_TRIDENT_NAME));
        tridentMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        tridentMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
        tridentMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_POTION_EFFECTS});
        tridentMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_UNBREAKABLE});
        List<String> loreTrident = new ArrayList();
        loreTrident.add(format(""));
        loreTrident.add(format("&6◎ &fПронзатель: &eV"));
        loreTrident.add(format("&6◎ &fВерность: &eIII"));
        loreTrident.add(format("&6◎ &fГромовержец: &eI"));
        loreTrident.add(format(""));
        loreTrident.add(format("&6◎ &fСлабость I: &e(0:05)"));
        loreTrident.add(format("&6◎ &fУтомление III: &e(0:20)"));
        loreTrident.add(format("&6◎ &fОтравление II: &e(0:03)"));
        loreTrident.add(format(""));
        loreTrident.add(format("&6◎ &f&lТягучка: &6(0:30)"));
        loreTrident.add(format("&6◎ &f&lНеразрушимость: &6ВЕЧНОСТЬ"));
        loreTrident.add(format(""));
        loreTrident.add(format("&6☯ &f&lРедкость: &6✰✰✰✰&e✰"));
        tridentMeta.setLore(loreTrident);

        PersistentDataContainer tPDC = tridentMeta.getPersistentDataContainer();
        tPDC.set(NamespacedKey.fromString("name"), PersistentDataType.STRING, "KuppaTrident");

        trident.setItemMeta(tridentMeta);

    }
    private static String format(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
