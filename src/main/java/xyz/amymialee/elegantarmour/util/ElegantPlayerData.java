package xyz.amymialee.elegantarmour.util;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.nbt.NbtCompound;

public class ElegantPlayerData {
    private final String playerName;
    private ElegantState headState = ElegantState.DEFAULT;
    private ElegantState chestState = ElegantState.DEFAULT;
    private ElegantState legsState = ElegantState.DEFAULT;
    private ElegantState feetState = ElegantState.DEFAULT;
    private ElegantState elytraState = ElegantState.DEFAULT;
    private ElegantState smallArmourState = ElegantState.DEFAULT;
    private static final String KEY_HEAD_STATE = "headState"; 
    private static final String KEY_CHEST_STATE = "chestState"; 
    private static final String KEY_LEGS_STATE = "legsState"; 
    private static final String KEY_FEET_STATE = "feetState"; 
    private static final String KEY_ELYTRA_STATE = "elytraState"; 
    private static final String KEY_SMALL_ARMOUR_STATE = "smallArmour";

    public ElegantPlayerData(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public ElegantState getState(int index) {
        return switch (index) {
            case 0 -> this.headState;
            case 1 -> this.chestState;
            case 2 -> this.legsState;
            case 3 -> this.feetState;
            case 4 -> this.elytraState;
            case 5 -> this.smallArmourState;
            default -> ElegantState.DEFAULT;
        };
    }

    public void setState(int index, ElegantState state) {
        switch (index) {
            case 0 -> this.headState = state;
            case 1 -> this.chestState = state;
            case 2 -> this.legsState = state;
            case 3 -> this.feetState = state;
            case 4 -> this.elytraState = state;
            case 5 -> this.smallArmourState = state;
        }
    }

    public ElegantState getState(EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> this.headState;
            case CHEST -> this.chestState;
            case LEGS -> this.legsState;
            case FEET -> this.feetState;
            default -> ElegantState.DEFAULT;
        };
    }
    
    public void readFromNbt(NbtCompound tag) {
        setHeadState(ElegantState.values()[tag.getInt(KEY_HEAD_STATE)]);
        setChestState(ElegantState.values()[tag.getInt(KEY_CHEST_STATE)]);
        setLegsState(ElegantState.values()[tag.getInt(KEY_LEGS_STATE)]);
        setFeetState(ElegantState.values()[tag.getInt(KEY_FEET_STATE)]);
        setElytraState(ElegantState.values()[tag.getInt(KEY_ELYTRA_STATE)]);
        setSmallArmourState(ElegantState.values()[tag.getInt(KEY_SMALL_ARMOUR_STATE)]);
    }

    public void writeToNbt(NbtCompound tag) {
        tag.putInt(KEY_HEAD_STATE, getHeadState().ordinal());
        tag.putInt(KEY_CHEST_STATE, getChestState().ordinal());
        tag.putInt(KEY_LEGS_STATE, getLegsState().ordinal());
        tag.putInt(KEY_FEET_STATE, getFeetState().ordinal());
        tag.putInt(KEY_ELYTRA_STATE, getElytraState().ordinal());
        tag.putInt(KEY_SMALL_ARMOUR_STATE, getSmallArmourState().ordinal());
    }

    public ElegantState getHeadState() {
        return this.headState;
    }

    public void setHeadState(ElegantState headState) {
        this.headState = headState;
    }

    public ElegantState getChestState() {
        return this.chestState;
    }

    public void setChestState(ElegantState chestState) {
        this.chestState = chestState;
    }

    public ElegantState getLegsState() {
        return this.legsState;
    }

    public void setLegsState(ElegantState legsState) {
        this.legsState = legsState;
    }

    public ElegantState getFeetState() {
        return this.feetState;
    }

    public void setFeetState(ElegantState feetState) {
        this.feetState = feetState;
    }

    public ElegantState getElytraState() {
        return this.elytraState;
    }

    public void setElytraState(ElegantState elytraState) {
        this.elytraState = elytraState;
    }

    public ElegantState getSmallArmourState() {
        return this.smallArmourState;
    }

    public void setSmallArmourState(ElegantState smallArmourState) {
        this.smallArmourState = smallArmourState;
    }
}