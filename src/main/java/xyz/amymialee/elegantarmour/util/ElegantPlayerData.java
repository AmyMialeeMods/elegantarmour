package xyz.amymialee.elegantarmour.util;

import net.minecraft.entity.EquipmentSlot;

public class ElegantPlayerData {
    private final String playerName;
    private ElegantState headState = ElegantState.DEFAULT;
    private ElegantState chestState = ElegantState.DEFAULT;
    private ElegantState legsState = ElegantState.DEFAULT;
    private ElegantState feetState = ElegantState.DEFAULT;
    private ElegantState elytraState = ElegantState.DEFAULT;
    private ElegantState smallArmourState = ElegantState.DEFAULT;

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