package xyz.amymialee.elegantarmour.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.client.MinecraftClient;
import xyz.amymialee.elegantarmour.ElegantArmour;
import xyz.amymialee.elegantarmour.client.ElegantOptionsScreen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

public class ElegantClientConfig {
    private static final File optionsFile = new File(MinecraftClient.getInstance().runDirectory, "config/elegantarmour.json");

    public static void saveConfig() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonObject json = new JsonObject();
            for (ElegantPart part : ElegantPart.values()) {
                json.addProperty(part.name(), ElegantOptionsScreen.isElegantPartEnabled(part));
            }
            for (ElegantClientSettings setting : ElegantClientSettings.values()) {
                json.addProperty(setting.name(), ElegantClientSettings.isElegantPartEnabled(setting));
            }
            String jsonData = gson.toJson(json);
            FileWriter writer = new FileWriter(optionsFile);
            writer.write(jsonData);
            writer.close();
        } catch (Exception e) {
            ElegantArmour.LOGGER.info(e.toString());
        }
    }

    public static void loadConfig() {
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(optionsFile);
            JsonObject data = gson.fromJson(reader, JsonObject.class);
            for (ElegantPart part : ElegantPart.values()) {
                if (data.has(part.name())) {
                    ElegantOptionsScreen.setElegantPart(part, data.get(part.name()).getAsBoolean());
                }
            }
            for (ElegantClientSettings setting : ElegantClientSettings.values()) {
                if (data.has(setting.name())) {
                    ElegantClientSettings.setElegantPart(setting, data.get(setting.name()).getAsBoolean());
                }
            }
        } catch (FileNotFoundException e) {
            ElegantArmour.LOGGER.info("Config data not found.");
        } catch (Exception e) {
            ElegantArmour.LOGGER.info("Error loading config data.");
            ElegantArmour.LOGGER.info(e.toString());
        }
    }
}