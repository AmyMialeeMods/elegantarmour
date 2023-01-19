package xyz.amymialee.elegantarmour.compat;

import com.unascribed.ears.api.EarsStateType;
import com.unascribed.ears.api.OverrideResult;
import com.unascribed.ears.api.registry.EarsStateOverriderRegistry;
import net.minecraft.entity.EquipmentSlot;
import xyz.amymialee.elegantarmour.config.ElegantClientSettings;
import xyz.amymialee.elegantarmour.util.IEleganttable;

public class EarsCompat {
    public static void init() {
        EarsStateOverriderRegistry.register("elegantarmour", (state, peer) -> {
            if (peer instanceof IEleganttable eleganttable) {
                if (state == EarsStateType.WEARING_HELMET) {
                    if (eleganttable.isElegantPartEnabled(EquipmentSlot.HEAD)) {
                        return OverrideResult.FALSE;
                    }
                    if (ElegantClientSettings.isElegantPartEnabled(eleganttable, EquipmentSlot.HEAD)) {
                        return OverrideResult.FALSE;
                    }
                }
                if (state == EarsStateType.WEARING_CHESTPLATE) {
                    if (eleganttable.isElegantPartEnabled(EquipmentSlot.CHEST)) {
                        return OverrideResult.FALSE;
                    }
                    if (ElegantClientSettings.isElegantPartEnabled(eleganttable, EquipmentSlot.CHEST)) {
                        return OverrideResult.FALSE;
                    }
                }
                if (state == EarsStateType.WEARING_LEGGINGS) {
                    if (eleganttable.isElegantPartEnabled(EquipmentSlot.LEGS)) {
                        return OverrideResult.FALSE;
                    }
                    if (ElegantClientSettings.isElegantPartEnabled(eleganttable, EquipmentSlot.LEGS)) {
                        return OverrideResult.FALSE;
                    }
                }
                if (state == EarsStateType.WEARING_BOOTS) {
                    if (eleganttable.isElegantPartEnabled(EquipmentSlot.FEET)) {
                        return OverrideResult.FALSE;
                    }
                    if (ElegantClientSettings.isElegantPartEnabled(eleganttable, EquipmentSlot.FEET)) {
                        return OverrideResult.FALSE;
                    }
                }
            }
            return OverrideResult.DEFAULT;
        });
    }
}