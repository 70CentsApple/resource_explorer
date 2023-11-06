package traben.resource_explorer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

import java.util.LinkedList;

public class REDirectoryScreen extends Screen {

    private REResourceListWidget fileList;
    private ButtonWidget doneButton;

    public Screen parent;


    final String cumulativePath;

    public final LinkedList<REResourceEntry> entries;
    public REDirectoryScreen(Screen parent, Text title, LinkedList<REResourceEntry> entries, String cumulativePath) {
        super(title);
        this.cumulativePath = cumulativePath;
        this.entries = entries;
        this.parent = parent;
    }

    protected void init() {
        this.fileList = new REResourceListWidget(this.client, this, 200, this.height);
        this.fileList.setLeftPos(this.width / 2 - 4 - 200);
        this.addSelectableChild(this.fileList);
//        this.selectedPackList = new PackListWidget(this.client, this, 200, this.height, Text.translatable("pack.selected.title"));
//        this.selectedPackList.setLeftPos(this.width / 2 + 4);
//        this.addSelectableChild(this.selectedPackList);
        this.addDrawableChild( new REDrawableFile(this.width / 2 + 4,0,200, this.height));
//        this.addDrawableChild(ButtonWidget.builder(Text.translatable("pack.openFolder"), (button) -> {
//            Util.getOperatingSystem().open(this.file.toUri());
//        }).dimensions(this.width / 2 - 154, this.height - 48, 150, 20).tooltip(Tooltip.of(FOLDER_INFO)).build());
        this.doneButton = (ButtonWidget)this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, (button) -> {
            this.close();
        }).dimensions(this.width / 2 + 4, this.height - 48, 150, 20).build());
//        this.refresh();

    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.fileList.render(context, mouseX, mouseY, delta);
//        this.selectedPackList.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 8, 16777215);
        context.drawCenteredTextWithShadow(this.textRenderer, Text.of(cumulativePath), this.width / 2, 20, Colors.GRAY);
    }

    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackgroundTexture(context);
    }

    @Override
    public void close() {
        this.fileList.close();
        super.close();
        REDrawableFile.setSelectedFile(null);
        //reading resources this way has some... damage to the resource system
        // thus a resource reload is required
        MinecraftClient.getInstance().reloadResources();
    }

    public static class REResourceListWidget extends AlwaysSelectedEntryListWidget<REResourceEntry>{

        final REDirectoryScreen screen;
        public REResourceListWidget(MinecraftClient minecraftClient, REDirectoryScreen screen, int width, int height) {
            super(minecraftClient, width, height, 32, height - 55 + 4, 36);
            this.centerListVertically = false;
            this.screen = screen;

            screen.entries.forEach(entry->{
                entry.setWidget(this);
                addEntry(entry);
            });
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return super.mouseClicked(mouseX, mouseY, button);
        }

        public void close() {
            clearEntries();
        }
        public int getRowWidth() {
            return this.width;
        }
        protected int getScrollbarPositionX() {
            return this.right - 6;
        }



    }



}
