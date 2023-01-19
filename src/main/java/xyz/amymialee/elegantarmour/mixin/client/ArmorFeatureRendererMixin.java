package xyz.amymialee.elegantarmour.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.amymialee.elegantarmour.ElegantArmour;
import xyz.amymialee.elegantarmour.config.ElegantClientSettings;
import xyz.amymialee.elegantarmour.config.ElegantPart;
import xyz.amymialee.elegantarmour.util.IEleganttable;

@Mixin(ArmorFeatureRenderer.class)
public class ArmorFeatureRendererMixin<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> {
    @Inject(method = "renderArmor", at = @At("HEAD"), cancellable = true)
    private void elegantArmour$dontRender(MatrixStack matrices, VertexConsumerProvider vertexConsumers, T entity, EquipmentSlot armorSlot, int light, A model, CallbackInfo ci) {
        if (entity instanceof IEleganttable eleganttable) {
            if (eleganttable.isElegantPartEnabled(armorSlot)) {
                ci.cancel();
            }
            if (ElegantClientSettings.isElegantPartEnabled(eleganttable, armorSlot)) {
                ci.cancel();
            }
            if (eleganttable.isElegantPartEnabled(ElegantPart.THIN_ARMOUR) || (ElegantClientSettings.isElegantPartEnabled(eleganttable, ElegantClientSettings.EVERYONE_THIN_ARMOUR))) {
                ItemStack itemStack = entity.getEquippedStack(armorSlot);
                if (itemStack.getItem() instanceof ArmorItem armorItem) {
                    if (this.canTexture(armorItem)) {
                        ci.cancel();
                    }
                }
            }
        }
    }

    @Unique
    private boolean canTexture(ArmorItem armorItem) {
        ResourceManager resourceManager = MinecraftClient.getInstance().getResourceManager();
        String string = "textures/models/armor/" + armorItem.getMaterial().getName() + "_layer_1.png";
        return resourceManager.getResource(new Identifier(string)).isPresent() || resourceManager.getResource(ElegantArmour.id(string)).isPresent();
    }
}