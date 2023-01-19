package xyz.amymialee.elegantarmour.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.feature.CapeFeatureRenderer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.amymialee.elegantarmour.config.ElegantClientSettings;
import xyz.amymialee.elegantarmour.config.ElegantPart;
import xyz.amymialee.elegantarmour.util.IEleganttable;

@Mixin(CapeFeatureRenderer.class)
public class CapeFeatureRendererMixin {
    @WrapOperation(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/network/AbstractClientPlayerEntity;FFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;getEquippedStack(Lnet/minecraft/entity/EquipmentSlot;)Lnet/minecraft/item/ItemStack;"))
    private ItemStack elegantArmour$showCape(AbstractClientPlayerEntity abstractClientPlayerEntity, EquipmentSlot equipmentSlot, Operation<ItemStack> original) {
        if (abstractClientPlayerEntity instanceof IEleganttable eleganttable) {
            if (eleganttable.isElegantPartEnabled(ElegantPart.HIDE_ELYTRA)) {
                return new ItemStack(Items.AIR);
            }
            if (ElegantClientSettings.isElegantPartEnabled(eleganttable, ElegantClientSettings.HIDE_OTHERS_ELYTRA)) {
                return new ItemStack(Items.AIR);
            }
        }
        return original.call(abstractClientPlayerEntity, equipmentSlot);
    }
}