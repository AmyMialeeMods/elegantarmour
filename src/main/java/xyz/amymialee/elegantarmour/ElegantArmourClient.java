package xyz.amymialee.elegantarmour;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import xyz.amymialee.elegantarmour.config.ElegantClientConfig;

@Environment(EnvType.CLIENT)
public class ElegantArmourClient implements ClientModInitializer {
    public static final EntityModelLayer THIN_ARMOR_INNER_LAYER = new EntityModelLayer(ElegantArmour.id("thin_armor_inner_layer"), "main");
    public static final EntityModelLayer THIN_ARMOR_OUTER_LAYER = new EntityModelLayer(ElegantArmour.id("thin_armor_outer_layer"), "main");
    public static final EntityModelLayer SLIM_THIN_ARMOR_OUTER_LAYER = new EntityModelLayer(ElegantArmour.id("slim_thin_armor_outer_layer"), "main");

    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(THIN_ARMOR_INNER_LAYER, () -> TexturedModelData.of(getThinModelData(new Dilation(0.27f), false), 64, 32));
        EntityModelLayerRegistry.registerModelLayer(THIN_ARMOR_OUTER_LAYER, () -> TexturedModelData.of(getThinModelData(new Dilation(0.28f), false), 64, 32));
        EntityModelLayerRegistry.registerModelLayer(SLIM_THIN_ARMOR_OUTER_LAYER, () -> TexturedModelData.of(getThinModelData(new Dilation(0.28f), true), 64, 32));
        ElegantClientConfig.loadConfig();
    }

    public static ModelData getThinModelData(Dilation dilation, boolean slim) {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
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