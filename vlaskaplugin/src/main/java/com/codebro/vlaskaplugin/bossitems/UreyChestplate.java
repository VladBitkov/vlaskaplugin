package com.codebro.vlaskaplugin.bossitems;

import org.bukkit.Color;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class UreyChestplate {
    private ItemStack chestplate;

    public UreyChestplate(ItemStack itemStack){
        chestplate = itemStack;
        LeatherArmorMeta armorMeta;
        if (chestplate.getItemMeta() instanceof LeatherArmorMeta) {
            armorMeta = (LeatherArmorMeta)chestplate.getItemMeta();
            armorMeta.setColor(Color.fromRGB(102, 102, 102));
            armorMeta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);

            chestplate.setItemMeta(armorMeta);

        }
    }

    public ItemStack getChestplate() {
        return chestplate;
    }
}
