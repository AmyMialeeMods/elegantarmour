package xyz.amymialee.elegantarmour.util;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.NotNull;

public class ElegantPlayerData {
    private String playerName;
    private ElegantState headState = ElegantState.DEFAULT;
    private ElegantState chestState = ElegantState.DEFAULT;
    private ElegantState legsState = ElegantState.DEFAULT;
    private ElegantState feetState = ElegantState.DEFAULT;
    private ElegantState elytraState = ElegantState.DEFAULT;
    private ElegantState smallArmourState = ElegantState.DEFAULT;

    public ElegantPlayerData(String playerName) {
        this.playerName = playerName;
    }

    public void setPlayerName(String playerName) {
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

    public ElegantState getState(@NotNull EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> this.headState;
            case CHEST -> this.chestState;
            case LEGS -> this.legsState;
            case FEET -> this.feetState;
            default -> ElegantState.DEFAULT;
        };
    }

    public void setFromBytes(byte head, byte chest, byte legs, byte feet, byte elytra, byte smallArmour) {
        this.setHeadState(ElegantState.values()[head]);
        this.setChestState(ElegantState.values()[chest]);
        this.setLegsState(ElegantState.values()[legs]);
        this.setFeetState(ElegantState.values()[feet]);
        this.setElytraState(ElegantState.values()[elytra]);
        this.setSmallArmourState(ElegantState.values()[smallArmour]);
    }

    public byte[] writeToBytes() {
        var bytes = new byte[6];
        bytes[0] = (byte) this.getHeadState().ordinal();
        bytes[1] = (byte) this.getChestState().ordinal();
        bytes[2] = (byte) this.getLegsState().ordinal();
        bytes[3] = (byte) this.getFeetState().ordinal();
        bytes[4] = (byte) this.getElytraState().ordinal();
        bytes[5] = (byte) this.getSmallArmourState().ordinal();
        return bytes;
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

    public void setFrom(ElegantPlayerData elegantPlayerData) {
        this.playerName = elegantPlayerData.playerName;
        this.headState = elegantPlayerData.headState;
        this.chestState = elegantPlayerData.chestState;
        this.legsState = elegantPlayerData.legsState;
        this.feetState = elegantPlayerData.feetState;
        this.elytraState = elegantPlayerData.elytraState;
        this.smallArmourState = elegantPlayerData.smallArmourState;
    }
}