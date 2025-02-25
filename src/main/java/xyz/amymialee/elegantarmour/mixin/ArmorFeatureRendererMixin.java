package xyz.amymialee.elegantarmour.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.elegantarmour.ElegantArmour;
import xyz.amymialee.elegantarmour.ElegantArmourClient;
import xyz.amymialee.elegantarmour.ElegantArmourConfig;
import xyz.amymialee.elegantarmour.util.ArmourDataHolder;
import xyz.amymialee.elegantarmour.util.ElegantState;

import java.util.HashMap;
import java.util.Map;

@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorFeatureRendererMixin<T extends LivingEntity, A extends BipedEntityModel<T>> implements ArmourDataHolder<T, A> {
    @Unique private static final Map<String, Identifier> SLIM_ARMOR_TEXTURE_CACHE = new HashMap<>();
    @Unique private A slimBodyModel;
    @Unique private A smallBodyModel;
    @Unique private A smallSlimBodyModel;
    @Unique private A smallLeggingsModel;
    @Unique private boolean isPlayer;
    @Unique private boolean slim;
    @Unique private boolean small;

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V", at = @At(value = "HEAD"))
    private void elegantArmour$small(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T entity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
        if (this.isPlayer) {
            var state = ElegantArmourClient.getMainState(ElegantArmourConfig.getOrCreate(entity.getUuid(), entity.getNameForScoreboard()), ElegantArmour.ARMOUR.get(entity).data, 5);
            this.small = state == ElegantState.HIDE;
        }
    }

    @Inject(method = "renderArmor", at = @At("HEAD"), cancellable = true)
    private void elegantArmour$newRender(MatrixStack matrices, VertexConsumerProvider vertexConsumers, T entity, EquipmentSlot slot, int light, A model, CallbackInfo ci) {
        if (this.isPlayer) {
            var state = ElegantArmourClient.getMainState(ElegantArmourConfig.getOrCreate(entity.getUuid(), entity.getNameForScoreboard()), ElegantArmour.ARMOUR.get(entity).data, slot);
            if (state == ElegantState.HIDE) ci.cancel();
        }
    }

    @Inject(method = "getModel", at = @At("HEAD"), cancellable = true)
    private void elegantArmour$getModel(EquipmentSlot slot, CallbackInfoReturnable<A> cir) {
        if (this.isPlayer) {
            if (this.small) {
                if (slot != EquipmentSlot.LEGS) {
                    cir.setReturnValue(this.slim ? this.smallSlimBodyModel : this.smallBodyModel);
                } else {
                    cir.setReturnValue(this.smallLeggingsModel);
                }
            } else if (this.slim && slot != EquipmentSlot.LEGS) {
                cir.setReturnValue(this.slimBodyModel);
            }
        }
    }

    @Inject(method = "getArmorTexture", at = @At("HEAD"), cancellable = true)
    private void elegantArmour$getTexture(ArmorItem item, boolean secondLayer, String overlay, CallbackInfoReturnable<Identifier> cir) {
        if (this.slim && !secondLayer) {
            var string = "textures/models/armor/" + item.getMaterial().getName() + "_layer_" + 1 + (overlay == null ? "" : "_" + overlay) + ".png";
            if (SLIM_ARMOR_TEXTURE_CACHE.containsKey(string)) {
                var id = SLIM_ARMOR_TEXTURE_CACHE.get(string);
                if (id != null) {
                    cir.setReturnValue(id);
                    return;
                }
            }
            var id = ElegantArmour.id(string);
            if (MinecraftClient.getInstance().getResourceManager().getResource(id).isPresent()) {
                SLIM_ARMOR_TEXTURE_CACHE.put(string, id);
                cir.setReturnValue(id);
            } else {
                SLIM_ARMOR_TEXTURE_CACHE.put(string, null);
            }
        }
    }

    @Unique @Override
    public void elegantarmour$setArmourData(A slimBodyModel, A smallBodyModel, A smallSlimBodyModel, A smallLeggingsModel, boolean slim) {
        this.slimBodyModel = slimBodyModel;
        this.smallBodyModel = smallBodyModel;
        this.smallSlimBodyModel = smallSlimBodyModel;
        this.smallLeggingsModel = smallLeggingsModel;
        this.slim = slim;
        this.isPlayer = true;
    }
}