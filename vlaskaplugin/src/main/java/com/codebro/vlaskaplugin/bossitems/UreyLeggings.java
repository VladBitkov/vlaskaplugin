package com.codebro.vlaskaplugin.bossitems;

import org.bukkit.Color;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class UreyLeggings {
    private ItemStack leggings;

    public UreyLeggings(ItemStack itemStack){
        leggings = itemStack;

        LeatherArmorMeta armorMeta;
        if (leggings.getItemMeta() instanceof LeatherArmorMeta) {
            armorMeta = (LeatherArmorMeta)leggings.getItemMeta();
            armorMeta.setColor(Color.fromRGB(102, 102, 102));
            armorMeta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);

            leggings.setItemMeta(armorMeta);

        }
    }

    public ItemStack getLeggings() {
        return leggings;
    }
}
