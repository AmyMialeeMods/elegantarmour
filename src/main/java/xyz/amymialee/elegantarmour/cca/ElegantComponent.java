package xyz.amymialee.elegantarmour.cca;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import xyz.amymialee.elegantarmour.ElegantArmour;
import xyz.amymialee.elegantarmour.ElegantArmourConfig;
import xyz.amymialee.elegantarmour.util.ElegantMode;
import xyz.amymialee.elegantarmour.util.ElegantPlayerData;
import xyz.amymialee.elegantarmour.util.ElegantSlot;

import java.util.UUID;

public class ElegantComponent implements AutoSyncedComponent {
	public static final ComponentKey<ElegantComponent> KEY = ComponentRegistry.getOrCreate(ElegantArmour.id("elegant"), ElegantComponent.class);
	public final PlayerEntity player;
	public final ElegantPlayerData playerPreferences;
	public final ElegantPlayerData playerOverrides;

	public ElegantComponent(@NotNull PlayerEntity player) {
		this.player = player;
		var gameProfile = player.getGameProfile();
		if (gameProfile == null) {
			gameProfile = new GameProfile(UUID.randomUUID(), "Dev");
		}
		this.playerPreferences = new ElegantPlayerData(gameProfile.getName());
		GameProfile finalGameProfile = gameProfile;
		this.playerOverrides = ElegantArmourConfig.playerOverrides.computeIfAbsent(player.getUuid(), uuid -> new ElegantPlayerData(finalGameProfile.getName()));
	}

	public void sync() {
		KEY.sync(this.player);
	}

	public ElegantMode getMode(EquipmentSlot slot) {
		return this.getMode(ElegantSlot.get(slot));
	}

	public ElegantMode getMode(ElegantSlot slot) {
		if (this.playerOverrides.get(slot) != ElegantMode.DEFAULT) return this.playerOverrides.get(slot);
		return this.playerPreferences.get(slot);
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {

	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {

	}
}