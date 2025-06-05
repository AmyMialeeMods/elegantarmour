package xyz.amymialee.elegantarmour.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import xyz.amymialee.elegantarmour.ElegantArmour;

import java.awt.Color;

public class ElegantMenuWidget extends ButtonWidget {
    private static final ButtonTextures BUTTON = new ButtonTextures(ElegantArmour.id("button"), ElegantArmour.id("button_disabled"), ElegantArmour.id("button_highlighted"));
    private static final Identifier CHEST = ElegantArmour.id("chest");
    private static float cycle = 0f;

    public ElegantMenuWidget(int x, int y, Text message, PressAction action) {
        super(x, y, 20, 20, message, action, DEFAULT_NARRATION_SUPPLIER);
    }

    @Override
    protected void renderWidget(@NotNull DrawContext context, int mouseX, int mouseY, float delta) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        context.drawGuiTexture(BUTTON.get(this.active, this.hovered), this.getX(), this.getY(), this.getWidth(), this.getHeight());
        cycle = cycle + delta % 160;
        var colour = Color.getHSBColor(cycle / 160f, 0.65f, 1.0f);
        RenderSystem.setShaderColor(colour.getRed() / 255f, colour.getGreen() / 255f, colour.getBlue() / 255f, 1.0F);
        context.drawGuiTexture(CHEST, this.getX() + 1, this.getY() + 1, 18, 18);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}