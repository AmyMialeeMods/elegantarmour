package xyz.amymialee.elegantarmour.client;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerModelPart;

public class ElegantPlayerEntity extends OtherClientPlayerEntity {
    public ElegantPlayerEntity(ClientWorld clientWorld, GameProfile gameProfile) {
        super(clientWorld, gameProfile);
    }

    @Override
    public boolean isPartVisible(PlayerModelPart modelPart) {
        return true;
    }

    @Override
    public boolean shouldRender(double distance) {
        return true;
    }

    @Override
    public void tickMovement() {}
}
