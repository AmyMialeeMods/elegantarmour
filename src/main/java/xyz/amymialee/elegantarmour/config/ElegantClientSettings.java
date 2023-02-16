package xyz.amymialee.elegantarmour.config;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.text.Text;
import xyz.amymialee.elegantarmour.util.IEleganttable;

import java.util.EnumSet;
import java.util.Set;

public enum ElegantClientSettings {
    HIDE_OTHERS_BOOTS("hideOthersBoots"),
    HIDE_OTHERS_LEGGINGS("hideOthersLeggings"),
    HIDE_OTHERS_CHESTPLATE("hideOthersChestplate"),
    HIDE_OTHERS_HELMET("hideOthersHelmet"),
    HIDE_OTHERS_ELYTRA("hideOthersElytra"),
    HIDE_OTHERS_HEAD_ITEM("hideOthersHeadItem"),
    EVERYONE_THIN_ARMOUR("everyoneThinArmour"),
    ENFORCE_SETTINGS("enforceSettings");

    public static final Set<ElegantClientSettings> ENABLED_ELEGANT_SETTINGS = EnumSet.noneOf(ElegantClientSettings.class);
    private final String name;
    private final Text optionName;

    ElegantClientSettings(String name) {
        this.name = name;
        this.optionName = Text.translatable("options.elegantPart." + name);
    }

    public String getName() {
        return this.name;
    }

    public Text getOptionName() {
        return this.optionName;
    }

    public static boolean isElegantPartEnabled(IEleganttable eleganttable, EquipmentSlot slot) {
        return switch (slot) {
            case FEET -> isElegantPartEnabled(eleganttable, ElegantClientSettings.HIDE_OTHERS_BOOTS);
            case LEGS -> isElegantPartEnabled(eleganttable, ElegantClientSettings.HIDE_OTHERS_LEGGINGS);
            case CHEST -> isElegantPartEnabled(eleganttable, ElegantClientSettings.HIDE_OTHERS_CHESTPLATE);
            case HEAD -> isElegantPartEnabled(eleganttable, ElegantClientSettings.HIDE_OTHERS_HELMET);
            case MAINHAND, OFFHAND -> true;
        };
    }

    public static void setElegantPart(ElegantClientSettings part, boolean enabled) {
        if (enabled) {
            ENABLED_ELEGANT_SETTINGS.add(part);
        } else {
            ENABLED_ELEGANT_SETTINGS.remove(part);
        }
        ElegantClientConfig.saveConfig();
    }

    public static boolean isElegantPartEnabled(IEleganttable eleganttable, ElegantClientSettings part) {
        if (eleganttable.isElegantPartEnabled(ElegantPart.CLIENT_ACTIVE)) {
            return false;
        }
        if (MinecraftClient.getInstance().player instanceof IEleganttable clientPlayer && clientPlayer == eleganttable) {
            return false;
        }
        return ENABLED_ELEGANT_SETTINGS.contains(part);
    }

    public static boolean isElegantPartEnabled(ElegantClientSettings part) {
        return ENABLED_ELEGANT_SETTINGS.contains(part);
    }

    public static boolean isEnforced() {
        return ENABLED_ELEGANT_SETTINGS.contains(ENFORCE_SETTINGS);
    }

    public static void toggleElegantPart(ElegantClientSettings part, boolean enabled) {
        setElegantPart(part, enabled);
        MinecraftClient.getInstance().options.sendClientSettings();
    }
}