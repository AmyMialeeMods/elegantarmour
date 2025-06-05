package xyz.amymialee.elegantarmour.util;

import org.jetbrains.annotations.NotNull;
import xyz.amymialee.elegantarmour.ElegantArmour;

public enum ElegantMode {
    DEFAULT,
    SHOW,
    SLIM,
    HIDE;

    public @NotNull String getTranslationKey() {
        return "options.%s.%s".formatted(ElegantArmour.MOD_ID, this.name().toLowerCase());
    }
}