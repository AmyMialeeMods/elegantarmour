package xyz.amymialee.elegantarmour.util;

import net.minecraft.entity.EquipmentSlot;
import xyz.amymialee.elegantarmour.config.ElegantPart;

public interface IEleganttable {
    boolean isElegantPartEnabled(ElegantPart part);

    boolean isElegantPartEnabled(EquipmentSlot armorSlot);

    void setElegantPart(ElegantPart part, boolean enabled);
}