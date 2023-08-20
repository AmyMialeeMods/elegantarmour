package xyz.amymialee.elegantarmour;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.amymialee.elegantarmour.config.ElegantPart;
import xyz.amymialee.elegantarmour.util.IEleganttable;

public class ElegantArmour implements ModInitializer {
    public static final String MOD_ID = "elegantarmour";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Identifier elegantC2S = ElegantArmour.id("elegant_c2s");
    public static final Identifier elegantS2C = ElegantArmour.id("elegant_s2c");

    @Override
    public void onInitialize() {
        ServerPlayNetworking.registerGlobalReceiver(ElegantArmour.elegantC2S, (server, player, networkHandler, buf, sender) -> {
            byte flags = buf.readByte();
            server.execute(() -> {
                if (player instanceof IEleganttable eleganttable) {
                    for (ElegantPart part : ElegantPart.values()) {
                        eleganttable.setElegantPart(part, (flags & part.getBitFlag()) == part.getBitFlag());
                    }
                }
            });
        });
    }

    public static Identifier id(String... path) {
        return new Identifier(MOD_ID, String.join(".", path));
    }
}