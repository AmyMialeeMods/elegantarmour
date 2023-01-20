package xyz.amymialee.elegantarmour.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.amymialee.elegantarmour.client.ElegantButtonWidget;
import xyz.amymialee.elegantarmour.client.ElegantOptionsScreen;

@Mixin(OptionsScreen.class)
public class OptionsScreenMixin extends Screen {
    @Shadow @Final private GameOptions settings;

    protected OptionsScreenMixin(Text title) {
        super(title);
    }

    @WrapOperation(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/option/OptionsScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;", ordinal = 4))
    private <T extends Element & Drawable & Selectable> T elegantArmour$thinSkinOptions(OptionsScreen screen, Element element, Operation<T> original) {
        if (element instanceof ButtonWidget buttonWidget) {
            buttonWidget.setWidth(buttonWidget.getWidth() - 20);
        }
        return original.call(screen, element);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void elegantArmour$init(CallbackInfo ci) {
        if (this.client == null) {
            return;
        }
        this.addDrawableChild(new ElegantButtonWidget(this.width / 2 - 155 + 130, this.height / 6 + 48 - 6, Text.translatable("options.elegantCustomisation"), button -> this.client.setScreen(new ElegantOptionsScreen(this, this.settings))));
    }
}