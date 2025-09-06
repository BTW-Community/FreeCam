package net.fabricmc.freecam.mixin;

import net.fabricmc.freecam.imixin.GameSettingsAccessor;
import net.minecraft.src.GameSettings;
import net.minecraft.src.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(GameSettings.class)
public abstract class GameSettingsMixin implements GameSettingsAccessor {
    @Shadow public KeyBinding[] keyBindings;
    @Unique
    public KeyBinding keyBindingToggleGhost = new KeyBinding("key.toggleFreeCam", Keyboard.KEY_F6);

    @Inject(method = "<init>(Lnet/minecraft/src/Minecraft;Ljava/io/File;)V", at = @At(value = "FIELD",
            target = "Lnet/minecraft/src/GameSettings;mc:Lnet/minecraft/src/Minecraft;"))
    public void initKeybindings(CallbackInfo ci) {
        this.keyBindings = Arrays.copyOf(this.keyBindings, keyBindings.length + 1);
        keyBindings[keyBindings.length - 1] = this.keyBindingToggleGhost;
    }

    @Override
    public KeyBinding freeCam$getKeyBindingToggleGhost() {
        return keyBindingToggleGhost;
    }
}
