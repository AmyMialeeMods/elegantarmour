package xyz.amymialee.elegantarmour.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.feature.CapeFeatureRenderer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.amymialee.elegantarmour.ElegantArmour;
import xyz.amymialee.elegantarmour.ElegantArmourConfig;
import xyz.amymialee.elegantarmour.util.ElegantState;

@Mixin(CapeFeatureRenderer.class)
public class CapeFeatureRendererMixin {
    @WrapOperation(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/network/AbstractClientPlayerEntity;FFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;getEquippedStack(Lnet/minecraft/entity/EquipmentSlot;)Lnet/minecraft/item/ItemStack;"))
    private ItemStack elegantArmour$showCape(AbstractClientPlayerEntity entity, EquipmentSlot slot, Operation<ItemStack> original) {
        ElegantState state = ElegantArmour.getMainState(ElegantArmourConfig.getOrCreate(entity.getUuid(), entity.getEntityName()), ElegantArmour.ARMOUR.get(entity).data, 4);
        if (state == ElegantState.HIDE) return new ItemStack(Items.AIR);
        return original.call(entity, slot);
    }
}