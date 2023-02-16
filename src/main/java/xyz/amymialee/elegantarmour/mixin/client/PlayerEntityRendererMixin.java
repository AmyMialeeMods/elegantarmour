package xyz.amymialee.elegantarmour.mixin.client;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.amymialee.elegantarmour.ElegantArmourClient;
import xyz.amymialee.elegantarmour.client.ThinArmorFeatureRenderer;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void elegantArmour$slimRenderer(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo ci) {
        this.addFeature(new ThinArmorFeatureRenderer<>(this, new BipedEntityModel<>(ctx.getPart(ElegantArmourClient.THIN_ARMOR_INNER_LAYER)), new BipedEntityModel<>(ctx.getPart(ElegantArmourClient.THIN_ARMOR_OUTER_LAYER)), new BipedEntityModel<>(ctx.getPart(ElegantArmourClient.SLIM_THIN_ARMOR_OUTER_LAYER)), slim));
    }
}