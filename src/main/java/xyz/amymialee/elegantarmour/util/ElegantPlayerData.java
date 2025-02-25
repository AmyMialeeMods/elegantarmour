package xyz.amymialee.elegantarmour.util;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.network.PacketByteBuf;

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

    public ElegantState getState(EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> this.headState;
            case CHEST -> this.chestState;
            case LEGS -> this.legsState;
            case FEET -> this.feetState;
            default -> ElegantState.DEFAULT;
        };
    }

    public void setFromBuf(PacketByteBuf buf) {
        this.setHeadState(ElegantState.values()[buf.readByte()]);
        this.setChestState(ElegantState.values()[buf.readByte()]);
        this.setLegsState(ElegantState.values()[buf.readByte()]);
        this.setFeetState(ElegantState.values()[buf.readByte()]);
        this.setElytraState(ElegantState.values()[buf.readByte()]);
        this.setSmallArmourState(ElegantState.values()[buf.readByte()]);
    }

    public void writeToBuf(PacketByteBuf buf) {
        buf.writeByte(this.getHeadState().ordinal());
        buf.writeByte(this.getChestState().ordinal());
        buf.writeByte(this.getLegsState().ordinal());
        buf.writeByte(this.getFeetState().ordinal());
        buf.writeByte(this.getElytraState().ordinal());
        buf.writeByte(this.getSmallArmourState().ordinal());
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