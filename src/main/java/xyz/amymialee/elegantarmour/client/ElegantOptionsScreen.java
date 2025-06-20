package xyz.amymialee.elegantarmour.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.NotNull;
import xyz.amymialee.elegantarmour.ElegantArmour;
import xyz.amymialee.elegantarmour.ElegantArmourConfig;
import xyz.amymialee.elegantarmour.util.ElegantPlayerData;
import xyz.amymialee.elegantarmour.util.SkinHelper;

public class ElegantOptionsScreen extends Screen {
    private final Screen parent;
    private final ElegantPlayerData data;
    private OtherClientPlayerEntity player;
    private int x;
    private int y;
    private static final Identifier BACKGROUND = ElegantArmour.id("background");
    private static final ButtonTextures BUTTON = new ButtonTextures(ElegantArmour.id("button"), ElegantArmour.id("button_disabled"), ElegantArmour.id("button_highlighted"));
    private static final ButtonTextures TILE = new ButtonTextures(ElegantArmour.id("tile"), ElegantArmour.id("tile_disabled"), ElegantArmour.id("tile_highlighted"));
    private static final Identifier HEAD = ElegantArmour.id("head");
    private static final Identifier CHEST = ElegantArmour.id("chest");
    private static final Identifier LEGS = ElegantArmour.id("legs");
    private static final Identifier FEET = ElegantArmour.id("feet");
    private static final Identifier ELYTRA = ElegantArmour.id("elytra");

    private static final Identifier BETA = ElegantArmour.id("beta");

    public ElegantOptionsScreen(Screen parent, @NotNull ElegantPlayerData data) {
        super(Text.literal(data.playerName));
        this.parent = parent;
        this.data = data;
    }

    @Override
    protected void init() {
        this.x = (this.width / 2) - (248 / 2);
        this.y = (this.height / 2) - (135 / 2);
//        this.addButtons(isClientPlayer, this.y + 19, ElegantIcons.HELMET, "options.elegantarmour.head");
//        this.addButtons(isClientPlayer, this.y + 19 + 18, ElegantIcons.CHESTPLATE, "options.elegantarmour.chest");
//        this.addButtons(isClientPlayer, this.y + 19 + 2 * 18, ElegantIcons.LEGGINGS, "options.elegantarmour.legs");
//        this.addButtons(isClientPlayer, this.y + 19 + 3 * 18, ElegantIcons.BOOTS, "options.elegantarmour.feet");
//        this.addButtons(isClientPlayer, this.y + 19 + 4 * 18, ElegantIcons.ELYTRA, "options.elegantarmour.elytra");
//        this.addButtons(this.y + 19 + 5 * 18, ElegantIcons.SMALL, "options.elegantarmour.small", true);
        this.addDrawableChild(ButtonWidget.builder(Text.literal("toggle mod"), button -> ElegantArmour.modEnabled = !ElegantArmour.modEnabled)
                .dimensions(this.x + 120, this.y + 55, 90, 40).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawGuiTexture(BACKGROUND, this.x, this.y, 248, 135);
        Text name = this.data == ElegantArmourConfig.universalOverride ? Text.translatable("%s.universal".formatted(ElegantArmour.MOD_ID)) : Text.literal(this.data.playerName).append(" ").append(Text.translatable("%s.player".formatted(ElegantArmour.MOD_ID)));
        context.drawText(this.textRenderer, name, this.width / 2 - this.textRenderer.getWidth(name) / 2, this.y + 7, 4210752, false);
        for (var drawable : this.drawables) drawable.render(context, mouseX, mouseY, delta);
        if (this.player == null && this.client != null && this.client.world != null) {
            this.player = new ElegantPlayerEntity(this.client.world, SkinHelper.getGameProfile(this.data.playerName));
            this.player.equipStack(EquipmentSlot.HEAD, Items.IRON_HELMET.getDefaultStack());
            this.player.equipStack(EquipmentSlot.CHEST, Items.IRON_CHESTPLATE.getDefaultStack());
            this.player.equipStack(EquipmentSlot.LEGS, Items.IRON_LEGGINGS.getDefaultStack());
            this.player.equipStack(EquipmentSlot.FEET, Items.IRON_BOOTS.getDefaultStack());
        }
        if (this.player != null) InventoryScreen.drawEntity(context, this.x + 9, this.y + 10, this.x + 69, this.y + 136, 46, 0.075F, mouseX, mouseY + 24f, this.player);
        context.getMatrices().push();
        context.getMatrices().translate(MathHelper.sin(Util.getMeasuringTimeMs() / 320f) * 8, -MathHelper.cos(Util.getMeasuringTimeMs() / 320f) * 8, 0);
        context.getMatrices().translate(133.0F, -9.0F, 0.0F);
        var f = 1.8F - MathHelper.abs(MathHelper.sin((float)(Util.getMeasuringTimeMs() % 1000L) / 1000.0F * (float) (Math.PI * 2)) * 0.1F);
        f *= 0.4f;
        context.getMatrices().translate(this.x, this.y, 0.0F);
        context.getMatrices().translate(198 / 2f, -44 / 2f, 0.0F);
        context.getMatrices().scale(f, f, f);
        context.getMatrices().translate(-198 / 2f, 44 / 2f, 0.0F);
        context.getMatrices().multiply(RotationAxis.POSITIVE_Z.rotationDegrees(20.0F));
        context.drawGuiTexture(BETA, 0, 0, 198, 44);
        context.getMatrices().pop();
    }

    @Override
    public void close() {
        if (this.client != null && this.parent != null) this.client.setScreen(this.parent);
    }
}