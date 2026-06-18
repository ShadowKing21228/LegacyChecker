package com.shadowking21.legacychecker.gui;

import com.shadowking21.legacychecker.config.ConfigRegistry;
import com.shadowking21.legacychecker.config.ModRequirement;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import org.apache.commons.lang3.tuple.Triple;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.util.List;

public class GuiMissingMods extends GuiScreen {

    private final List<ModRequirement> missingMods;

    private int scrollOffset = 0;

    private static final int START_Y = 65;

    private static final int MOD_LIST_HEIGHT = 140;

    private static final int END_Y = START_Y + MOD_LIST_HEIGHT;

    private static final int ROW_HEIGHT = 24;

    private boolean isScrolling = false;

    public GuiMissingMods(List<ModRequirement> missingMods) {
        this.missingMods = missingMods;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();

        boolean canIgnore = ConfigRegistry.config.getCurrentConfig().canPlayerLoadGame;
        String btnContinue = I18n.format("gui.legacychecker.btn.continue");
        String btnExit = I18n.format("gui.legacychecker.btn.exit");

        if (canIgnore) {
            this.buttonList.add(new GuiButton(0, this.width / 2 - 105, this.height - 30, 100, 20, btnContinue));
            this.buttonList.add(new GuiButton(1, this.width / 2 + 5, this.height - 30, 100, 20, btnExit));
        } else {
            this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height - 30, 200, 20, btnExit));
        }

        refreshModButtons();
    }

    private void refreshModButtons() {
        this.buttonList.removeIf(button -> button.id >= 10);

        String btnDownload = I18n.format("gui.legacychecker.btn.download");

        for (int i = 0; i < this.missingMods.size(); i++) {
            int y = START_Y + 10 + (i * ROW_HEIGHT) - scrollOffset;

            if (y > START_Y + 4 && y + 20 < END_Y - 4) {
                this.buttonList.add(new GuiButton(10 + i, this.width - 110, y, 75, 20, btnDownload));
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        GlStateManager.disableLighting();

        String title = "§c§l" + I18n.format("gui.legacychecker.title");
        String subtitle = "§7" + I18n.format("gui.legacychecker.subtitle");

        this.drawCenteredString(this.fontRenderer, title, this.width / 2, 20, 0xFFFFFF);
        this.drawCenteredString(this.fontRenderer, subtitle, this.width / 2, 40, 0xAAAAAA);

        drawRect(15, START_Y, this.width - 15, END_Y, 0x55000000);
        drawHorizontalLine(15, this.width - 15, START_Y, 0x33FFFFFF);
        drawHorizontalLine(15, this.width - 15, END_Y, 0x33FFFFFF);

        int totalContentHeight = this.missingMods.size() * ROW_HEIGHT + 16;
        int maxScroll = Math.max(0, totalContentHeight - MOD_LIST_HEIGHT);

        if (totalContentHeight > MOD_LIST_HEIGHT) {
            int scrollbarX = this.width - 25;
            int scrollbarWidth = 6;

            int scrollbarHeight = Math.max(15, (MOD_LIST_HEIGHT * MOD_LIST_HEIGHT) / totalContentHeight);
            int availableScrollSpace = MOD_LIST_HEIGHT - scrollbarHeight;

            if (Mouse.isButtonDown(0)) {
                if (!this.isScrolling) {
                    if (mouseX >= scrollbarX - 2 && mouseX <= scrollbarX + scrollbarWidth + 2 && mouseY >= START_Y && mouseY <= END_Y) {
                        this.isScrolling = true;
                    }
                }

                if (this.isScrolling) {
                    float relativeY = (float)(mouseY - START_Y - (scrollbarHeight / 2)) / (float)availableScrollSpace;
                    scrollOffset = (int)(relativeY * maxScroll);
                    scrollOffset = Math.clamp(scrollOffset, 0, maxScroll);
                    refreshModButtons();
                }
            } else {
                this.isScrolling = false;
            }

            int scrollbarY = START_Y + (scrollOffset * availableScrollSpace) / maxScroll;

            drawRect(scrollbarX, START_Y + 2, scrollbarX + scrollbarWidth, END_Y - 2, 0x33FFFFFF);
            drawRect(scrollbarX, scrollbarY, scrollbarX + scrollbarWidth, scrollbarY + scrollbarHeight, 0x88FFFFFF);
        } else {
            this.isScrolling = false;
        }

        for (int i = 0; i < this.missingMods.size(); i++) {
            ModRequirement mod = this.missingMods.get(i);
            int y = START_Y + 16 + (i * ROW_HEIGHT) - scrollOffset;

            if (y > START_Y + 4 && y < END_Y - 6) {
                String rawText = String.format("§c• §e%s §7(%s)", mod.modName(), mod.modId());
                String trimmedText = this.fontRenderer.trimStringToWidth(rawText, this.width - 150);

                this.drawString(this.fontRenderer, trimmedText, 25, y, 0xFFFFFF);
            }
        }

        String footerKey = ConfigRegistry.config.getCurrentConfig().canPlayerLoadGame
                ? "gui.legacychecker.footer.can_load"
                : "gui.legacychecker.footer.cant_load";
        String footer = "§7" + I18n.format(footerKey);

        this.drawCenteredString(this.fontRenderer, footer, this.width / 2, this.height - 48, 0xAAAAAA);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        int scroll = Mouse.getDWheel();
        if (scroll != 0) {
            scrollOffset += (scroll > 0 ? -12 : 12);

            // ИСПРАВЛЕНО: Идентичный правильный расчет лимита для колесика
            int totalContentHeight = this.missingMods.size() * ROW_HEIGHT + 16;
            int maxScroll = Math.max(0, totalContentHeight - MOD_LIST_HEIGHT);
            scrollOffset = Math.clamp(scrollOffset, 0, maxScroll);

            refreshModButtons();
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            this.mc.displayGuiScreen(new GuiMainMenu());
        } else if (button.id == 1) {
            this.mc.shutdown();
        } else if (button.id >= 10) {
            int modIndex = button.id - 10;
            if (modIndex < this.missingMods.size()) {
                String url = this.missingMods.get(modIndex).url();
                openWebLink(url);
            }
        }
    }

    private void openWebLink(String url) {
        if (url == null || url.isEmpty() || url.equalsIgnoreCase("none")) return;
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(url));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void onGuiClosed() {}
}