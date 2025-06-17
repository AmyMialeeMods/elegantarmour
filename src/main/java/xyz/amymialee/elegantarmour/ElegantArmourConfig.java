package xyz.amymialee.elegantarmour;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import xyz.amymialee.elegantarmour.util.ElegantPlayerData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ElegantArmourConfig {
    private static final File optionsFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), "elegantarmour.json");
    public static ElegantPlayerData universalOverride = new ElegantPlayerData("$Universal");
    public static Map<UUID, ElegantPlayerData> playerOverrides = new HashMap<>();

    public static void saveConfig() {
        try {
            var json = new JsonObject();
            var universal = new JsonObject();
            universalOverride.writeToJson(universal);
            json.add("universalOverride", universal);
            var playerArray = new JsonArray();
            for (var entry : playerOverrides.entrySet()) {
                var player = new JsonObject();
                player.addProperty("uuid", entry.getKey().toString());
                entry.getValue().writeToJson(player);
                playerArray.add(player);
            }
            json.add("playerData", playerArray);
            var writer = new FileWriter(optionsFile);
            writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(json));
            writer.close();
        } catch (Exception e) {
            ElegantArmour.LOGGER.info(e.toString());
        }
    }

    public static void loadConfig() {
        try {
            var reader = new FileReader(optionsFile);
            var data = new Gson().fromJson(reader, JsonObject.class);
            reader.close();
            universalOverride = new ElegantPlayerData(data.getAsJsonObject("universalOverride"));
            var playerArray = data.get("playerData").getAsJsonArray();
            for (var player : playerArray) {
                var json = player.getAsJsonObject();
                playerOverrides.put(UUID.fromString(json.get("uuid").getAsString()), new ElegantPlayerData(json));
            }
        } catch (FileNotFoundException ignored) {} catch (Exception e) {
            ElegantArmour.LOGGER.info("Error loading config data.");
            ElegantArmour.LOGGER.info(e.toString());
        }
    }
}