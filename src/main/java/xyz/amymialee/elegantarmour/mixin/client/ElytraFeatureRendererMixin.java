package xyz.amymialee.elegantarmour.mixin.client;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.amymialee.elegantarmour.config.ElegantClientSettings;
import xyz.amymialee.elegantarmour.config.ElegantPart;
import xyz.amymialee.elegantarmour.util.IEleganttable;

@Mixin(ElytraFeatureRenderer.class)
public class ElytraFeatureRendererMixin<T extends LivingEntity> {
    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V", at = @At("HEAD"), cancellable = true)
    private void elegantArmour$dontRenderElytra(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
        if (livingEntity instanceof IEleganttable eleganttable) {
            if (eleganttable.isElegantPartEnabled(ElegantPart.HIDE_ELYTRA)) {
                ci.cancel();
            }
            if (ElegantClientSettings.isElegantPartEnabled(eleganttable, ElegantClientSettings.HIDE_OTHERS_ELYTRA)) {
                ci.cancel();
            }
        }
    }
}