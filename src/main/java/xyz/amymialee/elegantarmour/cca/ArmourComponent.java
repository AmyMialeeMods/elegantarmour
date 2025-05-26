package xyz.amymialee.elegantarmour.cca;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import xyz.amymialee.elegantarmour.ElegantArmour;
import xyz.amymialee.elegantarmour.ElegantArmourConfig;
import xyz.amymialee.elegantarmour.util.ElegantPlayerData;
import xyz.amymialee.elegantarmour.util.ElegantState;

public class ArmourComponent implements AutoSyncedComponent {
	public static final ComponentKey<ArmourComponent> KEY = ComponentRegistry.getOrCreate(ElegantArmour.id("armour"), ArmourComponent.class);
	public boolean hasMod;
	public final ElegantPlayerData data;
	public final PlayerEntity player;

	public ArmourComponent(@NotNull PlayerEntity player) {
		this.player = player;
		this.data = new ElegantPlayerData(player.getNameForScoreboard());
	}

	public void sync() {
		KEY.sync(this.player);
	}

	@Override
	public void readFromNbt(@NotNull NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		this.data.setHeadState(ElegantState.values()[tag.getInt("headState")]);
		this.data.setChestState(ElegantState.values()[tag.getInt("chestState")]);
		this.data.setLegsState(ElegantState.values()[tag.getInt("legsState")]);
		this.data.setFeetState(ElegantState.values()[tag.getInt("feetState")]);
		this.data.setElytraState(ElegantState.values()[tag.getInt("elytraState")]);
		this.data.setSmallArmourState(ElegantState.values()[tag.getInt("smallArmour")]);
		// hasMod is not persisted, as it may be different every time the player connects
	}

	@Override
	public void writeToNbt(@NotNull NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		tag.putInt("headState", this.data.getHeadState().ordinal());
		tag.putInt("chestState", this.data.getChestState().ordinal());
		tag.putInt("legsState", this.data.getLegsState().ordinal());
		tag.putInt("feetState", this.data.getFeetState().ordinal());
		tag.putInt("elytraState", this.data.getElytraState().ordinal());
		tag.putInt("smallArmour", this.data.getSmallArmourState().ordinal());
	}

	@Override
	public void applySyncPacket(RegistryByteBuf buf) {
		this.data.setFromBuf(buf);
		this.hasMod = buf.readBoolean();
	}

	@Override
	public void writeSyncPacket(RegistryByteBuf buf, ServerPlayerEntity player) {
		this.data.writeToBuf(buf);
		buf.writeBoolean(this.hasMod);
	}

	public static void handleInit(PlayerEntity player) {
		var component = KEY.get(player);
		if (player.getWorld().isClient()) {
			if (ElegantArmourConfig.playerData.containsKey(player.getUuid())) {
				component.data.setFrom(ElegantArmourConfig.playerData.get(player.getUuid()));
			} else {
				component.data.setFrom(new ElegantPlayerData(player.getNameForScoreboard()));
				ElegantArmourConfig.playerData.put(player.getUuid(), component.data);
			}
		} else {
			if (ElegantArmour.PENDING_INITIALISATIONS.containsKey(player.getGameProfile().getId())) {
				ElegantArmour.PENDING_INITIALISATIONS.get(player.getUuid()).ifPresentOrElse(buf -> {
					component.data.setPlayerName(player.getNameForScoreboard());
					component.data.setFromBuf(buf);
					buf.release();
					component.hasMod = true;
				}, () -> component.hasMod = false);
				ElegantArmour.PENDING_INITIALISATIONS.remove(player.getUuid());
			}
		}
	}
}