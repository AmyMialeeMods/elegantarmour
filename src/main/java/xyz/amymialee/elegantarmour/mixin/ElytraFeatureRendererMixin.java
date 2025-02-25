package xyz.amymialee.elegantarmour.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.amymialee.elegantarmour.ElegantArmour;
import xyz.amymialee.elegantarmour.ElegantArmourClient;
import xyz.amymialee.elegantarmour.ElegantArmourConfig;
import xyz.amymialee.elegantarmour.util.ElegantState;

@Mixin(ElytraFeatureRenderer.class)
public class ElytraFeatureRendererMixin<T extends LivingEntity> {
    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V", at = @At("HEAD"), cancellable = true)
    private void elegantArmour$dontRenderElytra(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T entity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
        if (entity instanceof PlayerEntity) {
            var state = ElegantArmourClient.getMainState(ElegantArmourConfig.getOrCreate(entity.getUuid(), entity.getEntityName()), ElegantArmour.ARMOUR.get(entity).data, 4);
            if (state == ElegantState.HIDE) ci.cancel();
        }
    }
}