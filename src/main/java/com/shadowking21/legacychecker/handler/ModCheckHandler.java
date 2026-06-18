package com.shadowking21.legacychecker.handler;

import com.shadowking21.legacychecker.config.ConfigRegistry;
import com.shadowking21.legacychecker.config.ModRequirement;
import com.shadowking21.legacychecker.gui.GuiMissingMods;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class ModCheckHandler {

    private static boolean checkedOnce = false;

    private static List<ModRequirement> missingMods = null;

    public static void checkRequiredMods() {
        if (checkedOnce) {
            return;
        }
        checkedOnce = true;

        missingMods = getMissingMods();
    }

    private static List<ModRequirement> getMissingMods() {
        List<ModRequirement> missing = new ArrayList<>();

        List<ModRequirement> required = ConfigRegistry.config.getCurrentConfig().requiredMods;
        if (required == null) {
            return missing;
        }

        for (ModRequirement mod : required) {
            String modId = mod.modId();

            if (modId != null && !Loader.isModLoaded(modId)) {
                missing.add(mod);
            }
        }

        return missing;
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiMainMenu) {
            checkRequiredMods();

            if (missingMods != null && !missingMods.isEmpty()) {
                event.setGui(new GuiMissingMods(missingMods));
                missingMods = null;
            }
        }
    }
}

