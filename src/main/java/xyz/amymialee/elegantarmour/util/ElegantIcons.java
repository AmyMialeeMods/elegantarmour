package xyz.amymialee.elegantarmour.util;

import net.minecraft.util.Identifier;
import xyz.amymialee.elegantarmour.ElegantArmour;

public enum ElegantIcons {
    BACKGROUND(0, 0, 248, 135),
    BOX_DEFAULT(18, 135, 18, 18),
    BOX_HOVER(18, 153, 18, 18),
    OPTION_DEFAULT(18, 171, 49 ,18),
    OPTION_SELECTED(18, 189, 49 ,18),
    OPTION_HOVER(18, 207, 49 ,18),
    HELMET(0, 135, 18, 18),
    CHESTPLATE(0, 153, 18, 18),
    LEGGINGS(0, 171, 18, 18),
    BOOTS(0, 189, 18, 18),
    ELYTRA(0, 207, 18, 18),
    SMALL(0, 225, 18, 18);

    public static final Identifier ELEGANT_TEXTURE = ElegantArmour.id("textures/gui/elegantscreen.png");
    private final int u;
    private final int v;
    private final int width;
    private final int height;

    ElegantIcons(int u, int v, int width, int height) {
        this.u = u;
        this.v = v;
        this.width = width;
        this.height = height;
    }

    public int getU() {
        return this.u;
    }

    public int getV() {
        return this.v;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}