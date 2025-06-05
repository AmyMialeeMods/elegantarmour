package xyz.amymialee.elegantarmour.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.SkinTextures;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class SkinHelper {
    public static final Map<String, Optional<GameProfile>> failCache = new HashMap<>();

    public static GameProfile getGameProfile(String disguise) {
        var optional = SkullBlockEntity.fetchProfileByName(disguise).getNow(failCache(disguise));
        return optional.orElse(failCache(disguise).get());
    }

    public static Optional<GameProfile> failCache(String name) {
        return failCache.computeIfAbsent(name, (d) -> Optional.of(new GameProfile(UUID.randomUUID(), name)));
    }
}
