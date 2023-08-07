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
	public final ElegantPlayerData data;
	private final PlayerEntity player;

	public ArmourComponent(PlayerEntity player) {
		this.player = player;
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
		this.data.readFromNbt(tag);
		ElegantArmour.ARMOUR.sync(this.player);
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		this.data.writeToNbt(tag);
	}

	@Override
	public void applySyncPacket(PacketByteBuf buf) {
		NbtCompound tag = buf.readNbt();
		if (tag != null) {
			this.data.readFromNbt(tag);
		}
	}

	public static void handleClientUpdate(ServerPlayerEntity serverPlayerEntity, PacketByteBuf buf) {
		ArmourComponent component = ElegantArmour.ARMOUR.get(serverPlayerEntity);
		component.data.setHeadState(ElegantState.values()[buf.readInt()]);
		component.data.setChestState(ElegantState.values()[buf.readInt()]);
		component.data.setLegsState(ElegantState.values()[buf.readInt()]);
		component.data.setFeetState(ElegantState.values()[buf.readInt()]);
		component.data.setElytraState(ElegantState.values()[buf.readInt()]);
		component.data.setSmallArmourState(ElegantState.values()[buf.readInt()]);
		ElegantArmour.ARMOUR.sync(serverPlayerEntity);
	}
}