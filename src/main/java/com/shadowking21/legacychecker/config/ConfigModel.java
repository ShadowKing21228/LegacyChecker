package com.shadowking21.legacychecker.config;

import net.shadowking21.shadowconfig.annotation.ConfigComment;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Collections;
import java.util.List;

public class ConfigModel {

    @ConfigComment("The value responsible for the ability to continue the game correctly even without mods from requiredMods (the \"ignore\" button appears).")
    public boolean canPlayerLoadGame = false;

    @ConfigComment(
            "A list of required mods that must be installed. If any of these mods are missing,\n" +
                    "the game will intercept the main menu and display a custom download screen.\n" +
                    "\n" +
                    "Each entry must contain:\n" +
                    "  modId   - The exact internal ID of the mod (used for layout checking).\n" +
                    "  modName - The friendly name displayed to the player in the missing list.\n" +
                    "  url     - Direct link to the mod page (CurseForge/Modrinth) for the download button."
    )
    public List<ModRequirement> requiredMods = List.of(new ModRequirement[]{
            new ModRequirement("forge", "Example Mod Name", "https://curseforge.com/..."),
            new ModRequirement("minecraft", "Example Mod Name", "https://curseforge.com/...")
    });

}

