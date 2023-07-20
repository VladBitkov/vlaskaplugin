package com.codebro.vlaskaplugin.bossitems;

import com.ssomar.score.api.executableitems.ExecutableItemsAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UreyBow {

    private static final String UREY_BOW_NAME = format("&6\ud83c\udff9 &f&lЛук &5&lЮрэй &6\ud83c\udff9");

    private ItemStack bow;

    public static boolean checkUreyBow(ItemStack bow){
        if(bow.getItemMeta().getPersistentDataContainer().get(NamespacedKey.fromString("name"), PersistentDataType.STRING).equals("UreyBow")){
            return true;
        }
        return false;
    }

    public UreyBow() {
//        bow = itemStack;

        bow = ExecutableItemsAPI.getExecutableItemsManager().getExecutableItem("UreyBow").get().buildItem(1, Optional.empty());

        ItemMeta bowMeta = bow.getItemMeta();

        bowMeta.addEnchant(Enchantment.ARROW_FIRE, 1, true);
        bowMeta.addEnchant(Enchantment.ARROW_DAMAGE, 3, true);
        bowMeta.addEnchant(Enchantment.ARROW_KNOCKBACK, 1, true);
        bowMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);

        bowMeta.displayName(Component.text(UREY_BOW_NAME));

        bowMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        bowMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
        bowMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_POTION_EFFECTS});
        bowMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_UNBREAKABLE});
        List<String> loreBow = new ArrayList();
        loreBow.add(format(""));
        loreBow.add(format("&6◎ &fСила: &eIII"));
        loreBow.add(format("&6◎ &fВоспламенения: &eI"));
        loreBow.add(format("&6◎ &fОткидывание: &eI"));
        loreBow.add(format(""));
        loreBow.add(format("&6◎ &f&lНеразрушимость: &6ВЕЧНОСТЬ"));
        loreBow.add(format("&6◎ &f&lБесконечность &fна &6➶ &f&lСтрелы &5&lЮрэй &6➶"));
        loreBow.add(format(""));
        loreBow.add(format("&6☯ &f&lРедкость: &6✰✰✰✰&e✰"));
        bowMeta.setLore(loreBow);

        bowMeta.setUnbreakable(true);

        bowMeta.getPersistentDataContainer().set(NamespacedKey.fromString("name"), PersistentDataType.STRING, "UreyBow");

        bow.setItemMeta(bowMeta);
    }

    public static String getUreyBowName() {
        return UREY_BOW_NAME;
    }

    private static String format(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public ItemStack getBow() {
        return bow;
    }
}
