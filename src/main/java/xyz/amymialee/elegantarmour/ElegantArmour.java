package xyz.amymialee.elegantarmour;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.amymialee.elegantarmour.cca.ArmourComponent;
import xyz.amymialee.elegantarmour.util.ElegantPlayerData;
import xyz.amymialee.elegantarmour.util.ElegantState;

public class ElegantArmour implements ModInitializer, EntityComponentInitializer {
    public static final String MOD_ID = "elegantarmour";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Identifier CLIENT_UPDATE = id("client_update");
    public static final ComponentKey<ArmourComponent> ARMOUR = ComponentRegistry.getOrCreate(ElegantArmour.id("armour"), ArmourComponent.class);

    @Override
    public void onInitialize() {
        ServerPlayNetworking.registerGlobalReceiver(CLIENT_UPDATE, (server, player, networkHandler, buf, sender) -> ArmourComponent.handleClientUpdate(player, buf));
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.beginRegistration(PlayerEntity.class, ARMOUR).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(ArmourComponent::new);
    }

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }
}