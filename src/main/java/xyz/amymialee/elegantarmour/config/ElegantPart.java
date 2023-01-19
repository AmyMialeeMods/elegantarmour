package xyz.amymialee.elegantarmour.config;

import net.minecraft.text.Text;

public enum ElegantPart {
    HIDE_BOOTS(0, "hideBoots"),
    HIDE_LEGGINGS(1, "hideLeggings"),
    HIDE_CHESTPLATE(2, "hideChestplate"),
    HIDE_HELMET(3, "hideHelmet"),
    HIDE_ELYTRA(4, "hideElytra"),
    HIDE_HEAD_ITEM(5, "hideHeadItem"),
    THIN_ARMOUR(6, "thinArmour");

    private final int id;
    private final int bitFlag;
    private final String name;
    private final Text optionName;

    ElegantPart(int id, String name) {
        this.id = id;
        this.bitFlag = 1 << id;
        this.name = name;
        this.optionName = Text.translatable("options.elegantPart." + name);
    }

    public int getBitFlag() {
        return this.bitFlag;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Text getOptionName() {
        return this.optionName;
    }
}