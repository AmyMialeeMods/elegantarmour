package xyz.amymialee.elegantarmour;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.amymialee.mialeemisc.MialeeMisc;

public class ElegantArmour implements ModInitializer {
    public static final String MOD_ID = "elegantarmour";
    public final static Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {}

    public static Identifier id(String... path) {
        return MialeeMisc.namedId(MOD_ID, path);
    }
}