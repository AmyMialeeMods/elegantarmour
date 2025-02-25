package xyz.amymialee.elegantarmour;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;
import xyz.amymialee.elegantarmour.cca.ArmourComponent;
import xyz.amymialee.elegantarmour.compat.EarsCompat;
import xyz.amymialee.elegantarmour.util.ElegantPlayerData;
import xyz.amymialee.elegantarmour.util.ElegantState;

import java.util.concurrent.CompletableFuture;

public class ElegantArmourClient implements ClientModInitializer {
    public static final EntityModelLayer SLIM_BODY_LAYER = new EntityModelLayer(ElegantArmour.id("slim_body_layer"), "main");
    public static final EntityModelLayer SMALL_BODY_LAYER = new EntityModelLayer(ElegantArmour.id("small_body_layer"), "main");
    public static final EntityModelLayer SMALL_SLIM_BODY_LAYER = new EntityModelLayer(ElegantArmour.id("small_slim_body_layer"), "main");
    public static final EntityModelLayer SMALL_LEGS_LAYER = new EntityModelLayer(ElegantArmour.id("small_legs_layer"), "main");

    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(SLIM_BODY_LAYER, () -> TexturedModelData.of(getSmallModelData(new Dilation(1.0f), true), 64, 32));
        EntityModelLayerRegistry.registerModelLayer(SMALL_BODY_LAYER, () -> TexturedModelData.of(getSmallModelData(new Dilation(0.6f), false), 64, 32));
        EntityModelLayerRegistry.registerModelLayer(SMALL_SLIM_BODY_LAYER, () -> TexturedModelData.of(getSmallModelData(new Dilation(0.6f), true), 64, 32));
        EntityModelLayerRegistry.registerModelLayer(SMALL_LEGS_LAYER, () -> TexturedModelData.of(getSmallModelData(new Dilation(0.3f), false), 64, 32));
        if (FabricLoader.getInstance().isModLoaded("ears")) {
            try {
                EarsCompat.init();
            } catch (Throwable ignored) {}
        }
        ElegantArmourConfig.loadConfig();
        ClientLoginNetworking.registerGlobalReceiver(ElegantArmour.CLIENT_INIT_QUERY, (client, handler, queryBuf, listenerAdder) -> {
            var responseBuf = PacketByteBufs.create();
            var uuid = client.getSession().getUuidOrNull(); // client.player is null at this point, we get uuid from elsewhere
            var name = client.getSession().getUsername();
            if (uuid != null) {
                ElegantArmourConfig.getOrCreate(uuid, name).writeToBuf(responseBuf);
                return CompletableFuture.completedFuture(responseBuf);
            }
            return CompletableFuture.completedFuture(null); // we have no uuid, so we have no data to send
        });

        ClientEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof PlayerEntity player) {
                ArmourComponent.handleInit(player);
            }
        });
    }

    public static ElegantState getMainState(ElegantPlayerData local, ElegantPlayerData server, @NotNull EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> getMainState(local, server, 0);
            case CHEST -> getMainState(local, server, 1);
            case LEGS -> getMainState(local, server, 2);
            case FEET -> getMainState(local, server, 3);
            default -> ElegantState.DEFAULT;
        };
    }

    public static ElegantState getMainState(@NotNull ElegantPlayerData local, @NotNull ElegantPlayerData server, int index) {
        var localState = local.getState(index);
        var defaultState = ElegantArmourConfig.getState(index);
        var serverState = server.getState(index);
        if (localState != ElegantState.DEFAULT) {
            return localState;
        }
        if (defaultState != ElegantState.DEFAULT) {
            return defaultState;
        }
        return serverState;
    }

    public static void syncC2S() {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        var buf = PacketByteBufs.create();
        ElegantArmourConfig.playerData.get(player.getUuid()).writeToBuf(buf);
        ClientPlayNetworking.send(ElegantArmour.CLIENT_UPDATE, buf);
    }

    public static @NotNull ModelData getSmallModelData(@NotNull Dilation dilation, boolean slim) {
        var modelData = new ModelData();
        var modelPartData = modelData.getRoot();
        modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, dilation.add(0.23F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        modelPartData.addChild(EntityModelPartNames.HAT, ModelPartBuilder.create().uv(32, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, dilation.add(0.24F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        modelPartData.addChild(EntityModelPartNames.BODY, ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, dilation), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        if (slim) {
            modelPartData.addChild(EntityModelPartNames.LEFT_ARM, ModelPartBuilder.create().uv(40, 16).mirrored().cuboid(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, dilation), ModelTransform.pivot(5.0F, 2.0F, 0.0F));
            modelPartData.addChild(EntityModelPartNames.RIGHT_ARM, ModelPartBuilder.create().uv(40, 16).cuboid(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, dilation), ModelTransform.pivot(-5.0F, 2.0F, 0.0F));
        } else {
            modelPartData.addChild(EntityModelPartNames.LEFT_ARM, ModelPartBuilder.create().uv(40, 16).mirrored().cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation), ModelTransform.pivot(5.0F, 2.0F, 0.0F));
            modelPartData.addChild(EntityModelPartNames.RIGHT_ARM, ModelPartBuilder.create().uv(40, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation), ModelTransform.pivot(-5.0F, 2.0F, 0.0F));
        }
        modelPartData.addChild(EntityModelPartNames.RIGHT_LEG, ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation), ModelTransform.pivot(-1.9F, 12.0F, 0.0F));
        modelPartData.addChild(EntityModelPartNames.LEFT_LEG, ModelPartBuilder.create().uv(0, 16).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation), ModelTransform.pivot(1.9F, 12.0F, 0.0F));
        return modelData;
    }
}