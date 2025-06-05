package xyz.amymialee.elegantarmour.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.HeadFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.amymialee.elegantarmour.cca.ElegantComponent;
import xyz.amymialee.elegantarmour.util.ElegantMode;
import xyz.amymialee.elegantarmour.util.ElegantSlot;

@Mixin(HeadFeatureRenderer.class)
public class HeadFeatureRendererMixin<T extends LivingEntity> {
    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V", at = @At("HEAD"), cancellable = true)
    private void elegantarmour$hide(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
        if (livingEntity instanceof PlayerEntity player && ElegantComponent.KEY.get(player).getMode(ElegantSlot.HAT) == ElegantMode.HIDE) ci.cancel();
    }
}