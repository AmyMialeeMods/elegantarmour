package xyz.amymialee.elegantarmour.util;

import net.minecraft.entity.EquipmentSlot;
import xyz.amymialee.elegantarmour.config.ElegantPart;

import java.util.Set;

public interface IEleganttable {
    boolean isElegantPartEnabled(ElegantPart part);

    boolean isElegantPartEnabled(EquipmentSlot armorSlot);

    void setElegantPart(ElegantPart part, boolean enabled);

    Set<ElegantPart> getEnabledParts();

    boolean isElegantEnabled();
}