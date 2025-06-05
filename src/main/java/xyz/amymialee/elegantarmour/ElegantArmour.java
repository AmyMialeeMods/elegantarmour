package xyz.amymialee.elegantarmour;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.amymialee.elegantarmour.cca.ElegantComponent;

public class ElegantArmour implements ModInitializer, EntityComponentInitializer {
    public static final String MOD_ID = "elegantarmour";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        //TODO: Syncing
        //TODO: Menu
        //TODO: Do an ears PR to make it so boobs dont render unless the boob file is found
    }

    @Override
    public void registerEntityComponentFactories(@NotNull EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(ElegantComponent.KEY, ElegantComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
    }

    public static @NotNull Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }
}