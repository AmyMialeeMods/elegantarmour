package xyz.amymialee.elegantarmour.compat;

import com.unascribed.ears.api.EarsStateType;
import com.unascribed.ears.api.OverrideResult;
import com.unascribed.ears.api.registry.EarsStateOverriderRegistry;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import xyz.amymialee.elegantarmour.ElegantArmourConfig;
import xyz.amymialee.elegantarmour.cca.ElegantComponent;
import xyz.amymialee.elegantarmour.util.ElegantMode;
import xyz.amymialee.elegantarmour.util.ElegantSlot;

public class EarsCompat {
    public static void init() {
        EarsStateOverriderRegistry.register("elegantarmour", (state, peer) -> {
            if (!(peer instanceof PlayerEntity player)) return OverrideResult.DEFAULT;
            var component = ElegantComponent.KEY.get(player);
            if (state == EarsStateType.WEARING_HELMET && component.getMode(EquipmentSlot.HEAD) == ElegantMode.HIDE) return OverrideResult.FALSE;
            if (state == EarsStateType.WEARING_CHESTPLATE && component.getMode(EquipmentSlot.CHEST) == ElegantMode.HIDE) return OverrideResult.FALSE;
            if (state == EarsStateType.WEARING_LEGGINGS && component.getMode(EquipmentSlot.LEGS) == ElegantMode.HIDE) return OverrideResult.FALSE;
            if (state == EarsStateType.WEARING_BOOTS && component.getMode(EquipmentSlot.FEET) == ElegantMode.HIDE) return OverrideResult.FALSE;
            if (state == EarsStateType.WEARING_ELYTRA && component.getMode(ElegantSlot.ELYTRA) == ElegantMode.HIDE) return OverrideResult.FALSE;
            return OverrideResult.DEFAULT;
        });
    }
}