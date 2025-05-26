package xyz.amymialee.elegantarmour;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.amymialee.elegantarmour.cca.ArmourComponent;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ElegantArmour implements ModInitializer, EntityComponentInitializer {
    public static final String MOD_ID = "elegantarmour";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Map<UUID, Optional<PacketByteBuf>> PENDING_INITIALISATIONS = new ConcurrentHashMap<>();

    @Override
    public void onInitialize() {
        ServerLoginConnectionEvents.QUERY_START.register((handler, server, sender, synchronizer) -> {
            sender.sendPacket(CLIENT_INIT_QUERY, PacketByteBufs.create());
        });
        ServerLoginNetworking.registerGlobalReceiver(CLIENT_INIT_QUERY, (server, handler, understood, buf, synchronizer, responseSender) -> {
            if (handler.profile != null) {
                // waitFor just to guard against race conditions - we want to make sure we're done here before the player logs in and the
                // pending initialisation is retrieved
                synchronizer.waitFor(server.submit(() -> {
                    // player does not exist yet, so we defer the actual initialisation
                    var uuid = handler.profile.getId();
                    if (understood) {
                        PENDING_INITIALISATIONS.put(uuid, Optional.of(PacketByteBufs.copy(buf)));
                    } else {
                        PENDING_INITIALISATIONS.put(uuid, Optional.empty());
                    }
                }));
            }
        });
        // sync data pack contents happens after the player is fully initialised
        // why can't we do init in the constructor of ArmourComponent? because components
        // are constructed before the player has their profile / uuid set, so we don't
        // know who they are
        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register((player, joined) -> {
            ArmourComponent.handleInit(player);
        });
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.beginRegistration(PlayerEntity.class, ArmourComponent.KEY).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(ArmourComponent::new);
    }

    public static @NotNull Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }
}