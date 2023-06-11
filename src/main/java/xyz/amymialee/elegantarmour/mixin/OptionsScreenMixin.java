package xyz.amymialee.elegantarmour.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.amymialee.elegantarmour.ElegantArmourConfig;
import xyz.amymialee.elegantarmour.client.ElegantMenuWidget;
import xyz.amymialee.elegantarmour.client.ElegantOptionsScreen;
import xyz.amymialee.elegantarmour.util.ElegantPlayerData;

import java.util.UUID;
import java.util.function.Supplier;

@Mixin(OptionsScreen.class)
public class OptionsScreenMixin extends Screen {
    protected OptionsScreenMixin(Text title) {
        super(title);
    }

    @WrapOperation(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/option/OptionsScreen;createButton(Lnet/minecraft/text/Text;Ljava/util/function/Supplier;)Lnet/minecraft/client/gui/widget/ButtonWidget;", ordinal = 0))
    private ButtonWidget elegantArmour$smallSkinOptions(OptionsScreen optionsScreen, Text message, Supplier<Screen> screenSupplier, Operation<ButtonWidget> original, @Share("button") LocalRef<ButtonWidget> button) {
        ButtonWidget buttonWidget = original.call(optionsScreen, message, screenSupplier);
        buttonWidget.setWidth(buttonWidget.getWidth() - 20);
        button.set(buttonWidget);
        return buttonWidget;
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void elegantArmour$init(CallbackInfo ci, @Share("button") LocalRef<ButtonWidget> skin) {
        ButtonWidget buttonWidget = skin.get();
        buttonWidget.setX(buttonWidget.getX() - 10);
        if (this.client == null || this.client.getSession() == null || this.client.player == null) {
            return;
        }
        ElegantPlayerData data = ElegantArmourConfig.getOrCreate(UUID.fromString(this.client.getSession().getUuid()), this.client.getSession().getUsername());
        this.addDrawableChild(new ElegantMenuWidget(this.width / 2 - 155 + 130, this.height / 6 + 48 - 6, Text.translatable("options.elegantCustomisation"), button -> this.client.setScreen(new ElegantOptionsScreen(this, this.client.player, data)), UUID.fromString(this.client.getSession().getUuid())));
    }
}