package xyz.amymialee.elegantarmour;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import xyz.amymialee.elegantarmour.compat.EarsCompat;

public class ElegantArmourClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        if (FabricLoader.getInstance().isModLoaded("ears")) EarsCompat.init();
        ElegantArmourConfig.loadConfig();
    }
}