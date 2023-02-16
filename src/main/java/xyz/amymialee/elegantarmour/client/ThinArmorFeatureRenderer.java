package xyz.amymialee.elegantarmour.client;

import com.google.common.collect.Maps;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import xyz.amymialee.elegantarmour.ElegantArmour;
import xyz.amymialee.elegantarmour.config.ElegantClientSettings;
import xyz.amymialee.elegantarmour.config.ElegantPart;
import xyz.amymialee.elegantarmour.util.IEleganttable;

import java.util.Map;

public class ThinArmorFeatureRenderer<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> extends FeatureRenderer<T, M> {
    private static final Map<String, Identifier> ARMOR_TEXTURE_CACHE = Maps.newHashMap();
    private static final Map<String, Identifier> SLIM_ARMOR_TEXTURE_CACHE = Maps.newHashMap();
    private final A leggingsModel;
    private final A bodyModel;
    private final A slimModel;
    private final boolean slim;

    public ThinArmorFeatureRenderer(FeatureRendererContext<T, M> context, A leggingsModel, A bodyModel, A slimModel, boolean slim) {
        super(context);
        this.leggingsModel = leggingsModel;
        this.bodyModel = bodyModel;
        this.slimModel = slimModel;
        this.slim = slim;
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
        if (livingEntity instanceof IEleganttable eleganttable && (eleganttable.isElegantPartEnabled(ElegantPart.THIN_ARMOUR) || ElegantClientSettings.isElegantPartEnabled(eleganttable, ElegantClientSettings.EVERYONE_THIN_ARMOUR))) {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                this.renderArmor(matrixStack, vertexConsumerProvider, livingEntity, slot, i);
            }
        }
    }

    private void renderArmor(MatrixStack matrices, VertexConsumerProvider vertexConsumers, T entity, EquipmentSlot armorSlot, int light) {
        if (entity instanceof IEleganttable eleganttable) {
            if (eleganttable.isElegantPartEnabled(armorSlot)) {
                return;
            }
        }
        ItemStack itemStack = entity.getEquippedStack(armorSlot);
        if (itemStack.getItem() instanceof ArmorItem armorItem) {
            if (armorItem.getSlotType() == armorSlot) {
                boolean legs = this.usesSecondLayer(armorSlot);
                Identifier identifier = this.getArmorTexture(armorItem, legs, null);
                A model;
                if (this.usesSecondLayer(armorSlot)) {
                    model = this.leggingsModel;
                } else {
                    model = this.canSlimTexture(armorItem) ? this.slimModel : this.bodyModel;
                }
                this.getContextModel().setAttributes(model);
                this.setVisible(model, armorSlot);
                boolean glint = itemStack.hasGlint();
                if (armorItem instanceof DyeableArmorItem dyeableArmorItem) {
                    int i = dyeableArmorItem.getColor(itemStack);
                    float f = (float)(i >> 16 & 0xFF) / 255.0F;
                    float g = (float)(i >> 8 & 0xFF) / 255.0F;
                    float h = (float)(i & 0xFF) / 255.0F;
                    this.renderArmorParts(identifier, matrices, vertexConsumers, light, glint, model, f, g, h);
                    Identifier overlay = this.getArmorTexture(armorItem, legs, "overlay");
                    this.renderArmorParts(overlay, matrices, vertexConsumers, light, glint, model, 1.0F, 1.0F, 1.0F);
                } else {
                    this.renderArmorParts(identifier, matrices, vertexConsumers, light, glint, model, 1.0F, 1.0F, 1.0F);
                }
            }
        }
    }

    protected void setVisible(A armorModel, EquipmentSlot slot) {
        armorModel.setVisible(false);
        switch (slot) {
            case HEAD -> {
                armorModel.head.visible = true;
                armorModel.hat.visible = true;
            }
            case CHEST -> {
                armorModel.body.visible = true;
                armorModel.rightArm.visible = true;
                armorModel.leftArm.visible = true;
            }
            case LEGS -> {
                armorModel.body.visible = true;
                armorModel.rightLeg.visible = true;
                armorModel.leftLeg.visible = true;
            }
            case FEET -> {
                armorModel.rightLeg.visible = true;
                armorModel.leftLeg.visible = true;
            }
        }
    }

    private void renderArmorParts(Identifier identifier, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, boolean usesSecondLayer, A model, float red, float green, float blue) {
        VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumers, RenderLayer.getArmorCutoutNoCull(identifier), false, usesSecondLayer);
        model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, red, green, blue, 1.0F);
    }

    private boolean usesSecondLayer(EquipmentSlot slot) {
        return slot == EquipmentSlot.LEGS;
    }

    private boolean canSlimTexture(ArmorItem armorItem) {
        String string = "textures/models/armor/" + armorItem.getMaterial().getName() + "_layer_1.png";
        return this.canSlimTexture(string);
    }

    private boolean canSlimTexture(String string) {
        return this.slim && MinecraftClient.getInstance().getResourceManager().getResource(ElegantArmour.id(string)).isPresent();
    }

    private Identifier getArmorTexture(ArmorItem item, boolean legs, @Nullable String overlay) {
        String string = "textures/models/armor/" + item.getMaterial().getName() + "_layer_" + (legs ? 2 : 1) + (overlay == null ? "" : "_" + overlay) + ".png";
        if (this.canSlimTexture(string)) {
            return SLIM_ARMOR_TEXTURE_CACHE.computeIfAbsent(string, ElegantArmour::id);
        }
        return ARMOR_TEXTURE_CACHE.computeIfAbsent(string, Identifier::new);
    }
}