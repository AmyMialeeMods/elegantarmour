package xyz.amymialee.elegantarmour.compat;

import com.unascribed.ears.api.EarsStateType;
import com.unascribed.ears.api.OverrideResult;
import com.unascribed.ears.api.registry.EarsStateOverriderRegistry;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import xyz.amymialee.elegantarmour.ElegantArmour;
import xyz.amymialee.elegantarmour.ElegantArmourConfig;
import xyz.amymialee.elegantarmour.util.ElegantState;

public class EarsCompat {
    public static void init() {
        EarsStateOverriderRegistry.register("elegantarmour", (state, peer) -> {
            if (!(peer instanceof PlayerEntity player)) return OverrideResult.DEFAULT;
            var component = ElegantArmour.ARMOUR.get(player);
            if (state == EarsStateType.WEARING_HELMET && (component.data.getHeadState() == ElegantState.HIDE || (component.data.getHeadState() == ElegantState.DEFAULT && ElegantArmourConfig.getState(EquipmentSlot.HEAD) == ElegantState.HIDE))) return OverrideResult.FALSE;
            if (state == EarsStateType.WEARING_CHESTPLATE && (component.data.getChestState() == ElegantState.HIDE || (component.data.getChestState() == ElegantState.DEFAULT && ElegantArmourConfig.getState(EquipmentSlot.CHEST) == ElegantState.HIDE))) return OverrideResult.FALSE;
            if (state == EarsStateType.WEARING_LEGGINGS && (component.data.getLegsState() == ElegantState.HIDE || (component.data.getLegsState() == ElegantState.DEFAULT && ElegantArmourConfig.getState(EquipmentSlot.LEGS) == ElegantState.HIDE))) return OverrideResult.FALSE;
            if (state == EarsStateType.WEARING_BOOTS && (component.data.getFeetState() == ElegantState.HIDE || (component.data.getFeetState() == ElegantState.DEFAULT && ElegantArmourConfig.getState(EquipmentSlot.FEET) == ElegantState.HIDE))) return OverrideResult.FALSE;
            if (state == EarsStateType.WEARING_ELYTRA && (component.data.getElytraState() == ElegantState.HIDE || (component.data.getElytraState() == ElegantState.DEFAULT && ElegantArmourConfig.getDefaultElytra() == ElegantState.HIDE))) return OverrideResult.FALSE;
            return OverrideResult.DEFAULT;
        });
    }
}