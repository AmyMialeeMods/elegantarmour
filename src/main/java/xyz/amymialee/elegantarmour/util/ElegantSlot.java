package xyz.amymialee.elegantarmour.util;

import net.minecraft.entity.EquipmentSlot;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import xyz.amymialee.elegantarmour.ElegantArmour;

public enum ElegantSlot {
    HEAD,
    CHEST,
    LEGS,
    FEET,
    ELYTRA,
    HAT;

    public @NotNull String getTranslationKey() {
        return "slot.%s.%s".formatted(ElegantArmour.MOD_ID, this.name().toLowerCase());
    }

    @Contract(pure = true)
    public static ElegantSlot get(@NotNull EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> HEAD;
            case CHEST -> CHEST;
            case LEGS -> LEGS;
            case FEET -> FEET;
            default -> CHEST;
        };
    }
}