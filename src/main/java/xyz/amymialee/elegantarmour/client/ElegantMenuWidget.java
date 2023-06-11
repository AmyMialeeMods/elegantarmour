package xyz.amymialee.elegantarmour.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import xyz.amymialee.elegantarmour.ElegantArmour;

import java.awt.Color;
import java.util.UUID;

public class ElegantMenuWidget extends ButtonWidget {
    private static final Identifier ELEGANT_TEXTURE = ElegantArmour.id("textures/gui/widgets.png");
    private static float cycle = 0f;
    private final UUID uuid;

    public ElegantMenuWidget(int x, int y, Text message, PressAction action, UUID uuid) {
        super(x, y, 20, 20, message, action, DEFAULT_NARRATION_SUPPLIER);
        this.uuid = uuid;
    }

    @Override
    public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, ELEGANT_TEXTURE);
        context.drawTexture(ELEGANT_TEXTURE, this.getX(), this.getY(), 0, !this.isHovered() ? 0 : 20, 20, 20, 64, 64);
        if (this.uuid != null) {
            cycle = cycle + delta % 160;
            Color colour = Color.getHSBColor(cycle / 160f, 0.65f, 1.0f);
            RenderSystem.setShaderColor(colour.getRed() / 255f, colour.getGreen() / 255f, colour.getBlue() / 255f, 1.0F);
        }
        context.drawTexture(ELEGANT_TEXTURE, this.getX(), this.getY(), 0, 40, 20, 20, 64, 64);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}