package net.fabricmc.freecam;

import net.fabricmc.api.ModInitializer;
import net.minecraft.src.MouseHelper;
import net.minecraft.src.MovementInput;

public class FreeCam implements ModInitializer {
	public static boolean allowFreeCam = false;
	public static MouseHelper mouseHelper;
	public static MovementInput movementInput;
	public static boolean isFreeCam;
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

//		System.out.println("Hello Fabric world!");
	}
}
