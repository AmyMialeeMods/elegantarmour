package xyz.amymialee.elegantarmour.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameOptions.class)
public class GameOptionsMixin {
    @Shadow protected MinecraftClient client;

    @Inject(method = "sendClientSettings", at = @At("TAIL"))
    private void elegantArmour$sendSettings(CallbackInfo ci) {
        if (this.client.player != null) {
            int i = 0;
//            for (ElegantPart playerModelPart : ElegantPart.ENABLED_ELEGANT_PARTS) {
//                i |= playerModelPart.getBitFlag();
//            }
//            this.client.player.networkHandler.sendPacket(
//                    new ClientSettingsC2SPacket(
//                            this.language,
//                            this.viewDistance.getValue(),
//                            this.chatVisibility.getValue(),
//                            this.chatColors.getValue(),
//                            i,
//                            this.mainArm.getValue(),
//                            this.client.shouldFilterText(),
//                            this.allowServerListing.getValue()
//                    )
//            );
        }
    }
}