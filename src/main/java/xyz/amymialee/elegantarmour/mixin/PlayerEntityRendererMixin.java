package xyz.amymialee.elegantarmour.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.ArmorEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.amymialee.elegantarmour.ElegantArmourClient;
import xyz.amymialee.elegantarmour.util.ArmourDataHolder;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/PlayerEntityRenderer;addFeature(Lnet/minecraft/client/render/entity/feature/FeatureRenderer;)Z", ordinal = 0))
    private boolean elegantArmour$armourData(PlayerEntityRenderer renderer, FeatureRenderer<T, M> feature, Operation<Boolean> operation, @Local(ordinal = 0) EntityRendererFactory.Context ctx, @Local(ordinal = 0) boolean slim) {
        if (feature instanceof ArmourDataHolder holder) {
            holder.elegantarmour$setArmourData(new ArmorEntityModel<>(ctx.getPart(ElegantArmourClient.SLIM_BODY_LAYER)), new ArmorEntityModel<>(ctx.getPart(ElegantArmourClient.SMALL_BODY_LAYER)), new ArmorEntityModel<>(ctx.getPart(ElegantArmourClient.SMALL_SLIM_BODY_LAYER)), new ArmorEntityModel<>(ctx.getPart(ElegantArmourClient.SMALL_LEGS_LAYER)), slim);
        }
        return operation.call(renderer, feature);
    }
}