package com.codebro.vlaskaplugin.bosses;

import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class metaDataArrowValue implements MetadataValue {
    Object value;
    Plugin plugin;

    public metaDataArrowValue(Plugin pl, Object o) {
        plugin = pl;
        value = o;
    }

    public boolean equals(Object obj) {
        return value.equals(obj);
    }

    public void set(Object o) {
        value = o;
    }

    public boolean asBoolean() {
        return false;
    }

    @NotNull
    public String asString() {
        return "";
    }

    @Nullable
    public Plugin getOwningPlugin() {
        return plugin;
    }

    public void invalidate() {
    }

    public byte asByte() {
        return 0;
    }

    public double asDouble() {
        return 0.0;
    }

    public long asLong() {
        return 0L;
    }

    public short asShort() {
        return 0;
    }

    public float asFloat() {
        return 0.0F;
    }

    @Nullable
    public Object value() {
        if (value instanceof Integer) {
            int o = (Integer)this.value + 1;
            value = o;
        }
        return value;
    }

    public int asInt() {
        value = 0;
        return 0;
    }
}
