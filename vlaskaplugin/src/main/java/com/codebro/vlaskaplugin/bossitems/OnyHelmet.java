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

public class OnyHelmet {
    ItemStack onyHelmet;

    private static final String ONY_HELMET_NAME = format("&6\ud83d\udd25 &f&lШлем &c&lÓни &6\ud83d\udd25");

    public OnyHelmet() {
        onyHelmet = ExecutableItemsAPI.getExecutableItemsManager().getExecutableItem("OnyHelmet").get().buildItem(1, Optional.empty());

        ItemMeta onyHelmetMeta = this.onyHelmet.getItemMeta();
        onyHelmetMeta.getPersistentDataContainer().set(NamespacedKey.fromString("name"), PersistentDataType.STRING, "OnyHelmet");
        onyHelmetMeta.displayName(Component.text(ONY_HELMET_NAME));

        List<String> loreOnyHelmet = new ArrayList();
        loreOnyHelmet.add(format(""));
        loreOnyHelmet.add(format("&6◎ &f&lНеразрушимость: &6ВЕЧНОСТЬ"));
        loreOnyHelmet.add(format(""));
        loreOnyHelmet.add(format("&6◎ &fСила &f&lI&f: &6ВЕЧНОСТЬ"));
        loreOnyHelmet.add(format("&6◎ &fОгнестойкость &f&lI&f: &6ВЕЧНОСТЬ"));
        loreOnyHelmet.add(format("&4◎ &cМедлительность &c&lI&c: &4ВЕЧНОСТЬ"));
        loreOnyHelmet.add(format(""));
        loreOnyHelmet.add(format("&4◎ &c&lПроклятье утраты"));
        loreOnyHelmet.add(format("&4◎ &c&lПроклятье несъёмности"));
        loreOnyHelmet.add(format(""));
        loreOnyHelmet.add(format("&6☯ &f&lРедкость: &6✰✰✰✰&e✰"));

        onyHelmet.setItemMeta(onyHelmetMeta);

        onyHelmet.addEnchantment(Enchantment.VANISHING_CURSE, 1);
        onyHelmet.addEnchantment(Enchantment.BINDING_CURSE, 1);
        onyHelmet.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        onyHelmet.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
        onyHelmet.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_POTION_EFFECTS});
        onyHelmet.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_UNBREAKABLE});
    }

    public ItemStack getOnyHelmet() {
        return onyHelmet;
    }

    public static String getOnyHelmetName() {
        return ONY_HELMET_NAME;
    }

    private static String format(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}
