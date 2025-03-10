package xyz.amymialee.elegantarmour.util;

import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;

public interface ArmourDataHolder<T extends LivingEntity, A extends BipedEntityModel<T>> {
    void elegantarmour$setArmourData(A slimBodyModel, A smallBodyModel, A smallSlimBodyModel, A smallLeggingsModel, boolean slim);
}