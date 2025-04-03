
package toni.immersivearmorhud.foundation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import toni.immersivearmorhud.ArmorBarRenderer;
import toni.immersivearmorhud.foundation.config.AllConfigs;

import java.util.function.Consumer;

#if NEO
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.LayeredDraw;
#endif

#if FORGE
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
#endif

public class GuiOverlayHandler {
    #if FORGE
    public static final IGuiOverlay ARMOR_HUD = (ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) -> {
        Minecraft minecraft = gui.getMinecraft();
        if (!minecraft.options.hideGui && gui.shouldDrawSurvivalElements()) {
            var didRender = ArmorBarRenderer.render(guiGraphics, guiGraphics.guiHeight() - (AllConfigs.client().rightAligned.get() ?  gui.rightHeight :  gui.leftHeight));
            if (didRender) {
                if (AllConfigs.client().rightAligned.get()) {
                    gui.rightHeight += 10;
                } else {
                    gui.leftHeight += 10;
                }
            }
        }
    };
    #endif

    #if NEO
    public static final Consumer<GuiGraphics> ARMOR_HUD = (GuiGraphics guiGraphics) -> {
        Minecraft minecraft = Minecraft.getInstance();
        if (!minecraft.options.hideGui && minecraft.getCameraEntity() instanceof Player && minecraft.gameMode != null && minecraft.gameMode.canHurtPlayer()) {
            var didRender = ArmorBarRenderer.render(guiGraphics, guiGraphics.guiHeight() - (AllConfigs.client().rightAligned.get() ?  minecraft.gui.rightHeight :  minecraft.gui.leftHeight));
            if (didRender) {
                if (AllConfigs.client().rightAligned.get()) {
                    minecraft.gui.rightHeight += 10;
                } else {
                    minecraft.gui.leftHeight += 10;
                }
            }
        }
    };
    #endif
}