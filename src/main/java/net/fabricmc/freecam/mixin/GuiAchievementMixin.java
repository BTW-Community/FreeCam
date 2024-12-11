package net.fabricmc.freecam.mixin;

import net.fabricmc.freecam.FreeCam;
import net.fabricmc.freecam.imixin.GameSettingsAccessor;
import net.fabricmc.freecam.imixin.IGhost;
import net.minecraft.src.GuiAchievement;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.Minecraft;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiAchievement.class)
public class GuiAchievementMixin {
    @Unique
    private static final boolean[] wasDown = new boolean[256];

    @Inject(method = "updateAchievementWindow()V", at = @At("HEAD"))
    private void updateAchievementWindow(CallbackInfo info) {
        KeyBinding toggleGhost = ((GameSettingsAccessor) Minecraft.getMinecraft().gameSettings)
                .freeCam$getKeyBindingToggleGhost();
        if (Keyboard.isKeyDown(toggleGhost.keyCode) &&
                !wasDown[toggleGhost.keyCode]) {
            if (!FreeCam.allowFreeCam) {
                Minecraft.getMinecraft().thePlayer.addChatMessage("chat.allowFreeCam.false");
            } else {
                FreeCam.isFreeCam = !FreeCam.isFreeCam;
                ((IGhost) Minecraft.getMinecraft().entityRenderer)
                        .mITE_Neodymium$setPosition(0, 0, 0);
            }
        }
        for (int i = 0; i < 256; i++) {
            wasDown[i] = Keyboard.isKeyDown(i);
        }
    }
}