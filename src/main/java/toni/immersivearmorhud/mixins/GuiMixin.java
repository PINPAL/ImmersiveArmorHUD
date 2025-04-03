package toni.immersivearmorhud.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import toni.immersivearmorhud.ArmorBarRenderer;

@Mixin(Gui.class)
public class GuiMixin {

    @Inject(method = "renderVehicleHealth", at = @At("HEAD"))
    private void onRender(GuiGraphics guiGraphics, CallbackInfo ci) {
        var minecraft = Minecraft.getInstance();
        if (!minecraft.options.hideGui && minecraft.getCameraEntity() instanceof Player && minecraft.gameMode != null && minecraft.gameMode.canHurtPlayer()) {
            ArmorBarRenderer.render(guiGraphics, guiGraphics.guiHeight() - 59);
        }
    }
}
