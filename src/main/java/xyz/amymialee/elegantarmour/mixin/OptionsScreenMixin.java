package xyz.amymialee.elegantarmour.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.yggdrasil.ProfileActionType;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.session.Bans;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.amymialee.elegantarmour.ElegantArmourConfig;
import xyz.amymialee.elegantarmour.client.ElegantMenuWidget;
import xyz.amymialee.elegantarmour.client.ElegantOptionsScreen;
import xyz.amymialee.elegantarmour.util.ElegantPlayerData;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

@Mixin(OptionsScreen.class)
public class OptionsScreenMixin extends Screen {
    protected OptionsScreenMixin(Text title) {
        super(title);
    }

    @WrapOperation(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/option/OptionsScreen;createButton(Lnet/minecraft/text/Text;Ljava/util/function/Supplier;)Lnet/minecraft/client/gui/widget/ButtonWidget;", ordinal = 0))
    private ButtonWidget elegantarmour$makespace(OptionsScreen optionsScreen, Text message, Supplier<Screen> screenSupplier, @NotNull Operation<ButtonWidget> original, @Share("button") @NotNull LocalRef<ButtonWidget> skinButton) {
        var result = original.call(optionsScreen, message, screenSupplier);
        skinButton.set(result);
        return result;
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void elegantarmour$init(CallbackInfo ci, @Share("button") LocalRef<ButtonWidget> skinButton) {
        if (this.client == null) return;
        var profileResult = this.client.gameProfileFuture.join();
        if (profileResult == null) return;
        skinButton.get().setWidth(skinButton.get().getWidth() - 20);
        var gameProfile = profileResult.profile();
        var data = ElegantArmourConfig.playerOverrides.computeIfAbsent(gameProfile.getId(), (u) -> new ElegantPlayerData(gameProfile.getName()));
        this.addDrawableChild(new ElegantMenuWidget(skinButton.get().getX() + skinButton.get().getWidth(), skinButton.get().getY(), Text.translatable("elegantarmour.options"), button -> this.client.setScreen(new ElegantOptionsScreen(this, data))));
    }
}