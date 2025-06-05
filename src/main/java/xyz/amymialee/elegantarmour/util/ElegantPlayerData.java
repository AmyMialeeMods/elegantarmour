package xyz.amymialee.elegantarmour.util;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

public class ElegantPlayerData {
    public String playerName;
    public ElegantMode head = ElegantMode.DEFAULT;
    public ElegantMode chest = ElegantMode.DEFAULT;
    public ElegantMode legs = ElegantMode.DEFAULT;
    public ElegantMode feet = ElegantMode.DEFAULT;
    public ElegantMode elytra = ElegantMode.DEFAULT;
    public ElegantMode hat = ElegantMode.DEFAULT;

    public ElegantPlayerData(String playerName) {
        this.playerName = playerName;
    }

    public ElegantMode get(ElegantSlot slot) {
        return switch (slot) {
            case HEAD -> this.head;
            case CHEST -> this.chest;
            case LEGS -> this.legs;
            case FEET -> this.feet;
            case ELYTRA -> this.elytra;
            case HAT -> this.hat;
        };
    }

    public ElegantPlayerData(@NotNull JsonObject json) {
        this.playerName = json.get("name").getAsString();
        this.head = ElegantMode.valueOf(json.get("head").getAsString());
        this.chest = ElegantMode.valueOf(json.get("chest").getAsString());
        this.legs = ElegantMode.valueOf(json.get("legs").getAsString());
        this.feet = ElegantMode.valueOf(json.get("feet").getAsString());
        this.elytra = ElegantMode.valueOf(json.get("elytraState").getAsString());
        this.hat = ElegantMode.valueOf(json.get("hideHat").getAsString());
    }

    public void writeToJson(@NotNull JsonObject json) {
        json.addProperty("name", this.playerName);
        json.addProperty("head", this.head.name());
        json.addProperty("chest", this.chest.name());
        json.addProperty("legs", this.legs.name());
        json.addProperty("feet", this.feet.name());
        json.addProperty("elytraState", this.elytra.name());
        json.addProperty("hideHat", this.hat.name());
    }
}