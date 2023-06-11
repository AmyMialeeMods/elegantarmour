package xyz.amymialee.elegantarmour.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.HeadFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.amymialee.elegantarmour.ElegantArmour;
import xyz.amymialee.elegantarmour.ElegantArmourConfig;
import xyz.amymialee.elegantarmour.cca.ArmourComponent;
import xyz.amymialee.elegantarmour.util.ElegantState;

@Mixin(HeadFeatureRenderer.class)
public class HeadFeatureRendererMixin<T extends LivingEntity> {
    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V", at = @At("HEAD"), cancellable = true)
    private void elegantArmour$dontRender(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T entity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
        if (entity instanceof PlayerEntity) {
            ArmourComponent armourComponent = ElegantArmour.ARMOUR.get(entity);
            ElegantState state = armourComponent.data.getHeadState();
            ElegantState configState = ElegantArmourConfig.getOrCreate(entity.getUuid(), entity.getEntityName()).getHeadState();
            if (configState == ElegantState.HIDE) {
                ci.cancel();
            } else if (configState == ElegantState.DEFAULT) {
                if (state == ElegantState.HIDE || (state == ElegantState.DEFAULT && ElegantArmourConfig.getState(EquipmentSlot.HEAD) == ElegantState.HIDE)) {
                    ci.cancel();
                }
            }
        }
    }
}