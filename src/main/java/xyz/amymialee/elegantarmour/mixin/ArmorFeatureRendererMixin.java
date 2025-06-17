package xyz.amymialee.elegantarmour.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.trim.ArmorTrimPattern;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.amymialee.elegantarmour.ElegantArmour;
import xyz.amymialee.elegantarmour.cca.ElegantComponent;
import xyz.amymialee.elegantarmour.util.ElegantMode;
import xyz.amymialee.elegantarmour.util.ElegantSlot;

import java.util.HashMap;
import java.util.Map;

@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorFeatureRendererMixin<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> extends FeatureRenderer<T, M> {
    @Unique private static final Map<Identifier, Identifier> SLIM_ARMOR_TEXTURE_CACHE = new HashMap<>();
    @Unique private static final Map<Identifier, Identifier> SLIM_TRIMS_TEXTURE_CACHE = new HashMap<>();
    @Unique private static final Map<Identifier, Identifier> SLIM_LEGGINGS_TRIMS_TEXTURE_CACHE = new HashMap<>();
    @Unique private T currentEntity;
    @Unique private EquipmentSlot currentSlot;

    public ArmorFeatureRendererMixin(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @Inject(method = "renderArmor", at = @At("HEAD"), cancellable = true)
    private void elegantarmour$hide(MatrixStack matrices, VertexConsumerProvider vertexConsumers, T entity, EquipmentSlot armorSlot, int light, A model, CallbackInfo ci) {
        if (entity instanceof PlayerEntity player && ElegantComponent.KEY.get(player).getMode(ElegantSlot.get(armorSlot)) == ElegantMode.HIDE) ci.cancel();
        this.currentEntity = entity;
        this.currentSlot = armorSlot;
    }

    @WrapOperation(method = "renderArmor", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ArmorMaterial$Layer;getTexture(Z)Lnet/minecraft/util/Identifier;"))
    private @Nullable Identifier elegantarmour$slim(ArmorMaterial.Layer instance, boolean secondLayer, @NotNull Operation<Identifier> original, MatrixStack matrices, VertexConsumerProvider vertexConsumers, T entity, EquipmentSlot armorSlot) {
        var result = original.call(instance, secondLayer);
        if (entity instanceof PlayerEntity player && ElegantComponent.KEY.get(player).getMode(ElegantSlot.get(armorSlot)) == ElegantMode.SLIM) {
            if (SLIM_ARMOR_TEXTURE_CACHE.containsKey(result)) {
                var id = SLIM_ARMOR_TEXTURE_CACHE.get(result);
                if (id != null) return id;
                return result;
            }
            var id = ElegantArmour.id("textures/slimmodels/" + result.getNamespace() + "/" + result.getPath());
            if (MinecraftClient.getInstance().getResourceManager().getResource(id).isPresent()) {
                SLIM_ARMOR_TEXTURE_CACHE.put(result, id);
                return id;
            } else {
                SLIM_ARMOR_TEXTURE_CACHE.put(result, null);
            }
        }
        return result;
    }

//    @WrapOperation(method = "renderTrim", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/trim/ArmorTrim;getGenericModelId(Lnet/minecraft/registry/entry/RegistryEntry;)Lnet/minecraft/util/Identifier;"))
//    private @Nullable Identifier elegantarmour$slimTrim(ArmorTrim instance, RegistryEntry<ArmorMaterial> armorMaterial, @NotNull Operation<Identifier> original) {
//        var result = original.call(instance, armorMaterial);
//        if (this.currentEntity instanceof PlayerEntity player && ElegantComponent.KEY.get(player).getMode(ElegantSlot.get(this.currentSlot)) == ElegantMode.SLIM) {
//            if (SLIM_TRIMS_TEXTURE_CACHE.containsKey(result)) {
//                var id = SLIM_TRIMS_TEXTURE_CACHE.get(result);
//                if (id != null) return id;
//                return result;
//            }
//            var id = ElegantArmour.id("textures/slimmodels/" + result.getNamespace() + "/" + result.getPath());
//            if (MinecraftClient.getInstance().getResourceManager().getResource(id).isPresent()) {
//                SLIM_TRIMS_TEXTURE_CACHE.put(result, id);
//                return id;
//            } else {
//                SLIM_TRIMS_TEXTURE_CACHE.put(result, null);
//            }
//        }
//        return result;
//    }
//
//    @WrapOperation(method = "renderTrim", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/trim/ArmorTrim;getLeggingsModelId(Lnet/minecraft/registry/entry/RegistryEntry;)Lnet/minecraft/util/Identifier;"))
//    private @Nullable Identifier elegantarmour$slimTrimLeggings(ArmorTrim instance, RegistryEntry<ArmorMaterial> armorMaterial, @NotNull Operation<Identifier> original) {
//        var result = original.call(instance, armorMaterial);
//        if (this.currentEntity instanceof PlayerEntity player && ElegantComponent.KEY.get(player).getMode(ElegantSlot.get(this.currentSlot)) == ElegantMode.SLIM) {
//            if (SLIM_LEGGINGS_TRIMS_TEXTURE_CACHE.containsKey(result)) {
//                var id = SLIM_LEGGINGS_TRIMS_TEXTURE_CACHE.get(result);
//                if (id != null) return id;
//                return result;
//            }
//            var id = ElegantArmour.id("textures/slimmodels/" + result.getNamespace() + "/" + result.getPath());
//            if (MinecraftClient.getInstance().getResourceManager().getResource(id).isPresent()) {
//                SLIM_LEGGINGS_TRIMS_TEXTURE_CACHE.put(result, id);
//                return id;
//            } else {
//                SLIM_LEGGINGS_TRIMS_TEXTURE_CACHE.put(result, null);
//            }
//        }
//        return result;
//    }

    @WrapOperation(method = "renderTrim", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/trim/ArmorTrimPattern;decal()Z"))
    private boolean elegantarmour$slimTrimLeggings(ArmorTrimPattern instance, @NotNull Operation<Boolean> original) {
        var result = original.call(instance);
        if (this.currentEntity instanceof PlayerEntity player && ElegantComponent.KEY.get(player).getMode(ElegantSlot.get(this.currentSlot)) == ElegantMode.SLIM) return true;
        return result;
    }
}