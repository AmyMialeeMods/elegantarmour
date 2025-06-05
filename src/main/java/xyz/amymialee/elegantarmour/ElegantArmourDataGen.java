package xyz.amymialee.elegantarmour;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.NotNull;
import xyz.amymialee.mialib.templates.MDataGen;

public class ElegantArmourDataGen extends MDataGen {
    @Override
    protected void generateTranslations(MLanguageProvider provider, RegistryWrapper.WrapperLookup registryLookup, FabricLanguageProvider.@NotNull TranslationBuilder builder) {
        builder.add("%s".formatted(ElegantArmour.MOD_ID), "Elegant Armour");
        builder.add("%s.universal".formatted(ElegantArmour.MOD_ID), "Universal Overrides");
        builder.add("%s.player".formatted(ElegantArmour.MOD_ID), "Player Overrides");

        builder.add("%s.default".formatted(ElegantArmour.MOD_ID), "Default");
        builder.add("%s.show".formatted(ElegantArmour.MOD_ID), "Show");
        builder.add("%s.slim".formatted(ElegantArmour.MOD_ID), "Slim");
        builder.add("%s.hide".formatted(ElegantArmour.MOD_ID), "Hide");

        builder.add("%s.head".formatted(ElegantArmour.MOD_ID), "Head");
        builder.add("%s.chest".formatted(ElegantArmour.MOD_ID), "Chest");
        builder.add("%s.legs".formatted(ElegantArmour.MOD_ID), "Legs");
        builder.add("%s.feet".formatted(ElegantArmour.MOD_ID), "Feet");
        builder.add("%s.elytra".formatted(ElegantArmour.MOD_ID), "Elytra");
        builder.add("%s.hat".formatted(ElegantArmour.MOD_ID), "Hat");
    }
}