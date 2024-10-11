package xyz.amymialee.elegantarmour.cca;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import xyz.amymialee.elegantarmour.ElegantArmour;
import xyz.amymialee.elegantarmour.ElegantArmourConfig;
import xyz.amymialee.elegantarmour.util.ElegantPlayerData;
import xyz.amymialee.elegantarmour.util.ElegantState;

public class ArmourComponent implements AutoSyncedComponent {
	public boolean hasMod;
	public final ElegantPlayerData data;

	public ArmourComponent(PlayerEntity player) {
		if (player.getWorld().isClient()) {
			if (ElegantArmourConfig.playerData.containsKey(player.getUuid())) {
				this.data = ElegantArmourConfig.playerData.get(player.getUuid());
			} else {
				this.data = new ElegantPlayerData(player.getGameProfile() == null ? "" : player.getEntityName());
				ElegantArmourConfig.playerData.put(player.getUuid(), this.data);
			}
		} else {
			this.data = new ElegantPlayerData(player.getGameProfile() == null ? "" : player.getEntityName());
		}
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		this.data.setHeadState(ElegantState.values()[tag.getInt("headState")]);
		this.data.setChestState(ElegantState.values()[tag.getInt("chestState")]);
		this.data.setLegsState(ElegantState.values()[tag.getInt("legsState")]);
		this.data.setFeetState(ElegantState.values()[tag.getInt("feetState")]);
		this.data.setElytraState(ElegantState.values()[tag.getInt("elytraState")]);
		this.data.setSmallArmourState(ElegantState.values()[tag.getInt("smallArmour")]);
		// hasMod is not persisted, as it may be different every time the player connects
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putInt("headState", this.data.getHeadState().ordinal());
		tag.putInt("chestState", this.data.getChestState().ordinal());
		tag.putInt("legsState", this.data.getLegsState().ordinal());
		tag.putInt("feetState", this.data.getFeetState().ordinal());
		tag.putInt("elytraState", this.data.getElytraState().ordinal());
		tag.putInt("smallArmour", this.data.getSmallArmourState().ordinal());
	}

	@Override
	public void applySyncPacket(PacketByteBuf buf) {
		this.data.setFromBuf(buf);

		// if server is older version, this may be the end of the packet, so we check for that here
		// FIXME this can be changed after a breaking update, e.g. an MC update
		if (buf.readableBytes() > 0) {
			this.hasMod = buf.readBoolean();
		}
	}

	@Override
	public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity player) {
		this.data.writeToBuf(buf);
		buf.writeBoolean(this.hasMod);
	}

	public static void handleClientUpdate(ServerPlayerEntity serverPlayerEntity, PacketByteBuf buf) {
        var component = ElegantArmour.ARMOUR.get(serverPlayerEntity);
		component.data.setFromBuf(buf);
		component.hasMod = true; // if we've received a client update packet, we know the player has the mod
		ElegantArmour.ARMOUR.sync(serverPlayerEntity);
	}
}