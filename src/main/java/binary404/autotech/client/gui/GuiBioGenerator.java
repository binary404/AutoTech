package binary404.autotech.client.gui;

import binary404.autotech.client.gui.core.GuiEnergy;
import binary404.autotech.client.gui.core.Texture;
import binary404.autotech.common.container.generator.BioGeneratorContainer;
import binary404.autotech.common.tile.generator.TileSteamGenerator;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class GuiBioGenerator extends GuiEnergy<TileSteamGenerator, BioGeneratorContainer> {

    public GuiBioGenerator(BioGeneratorContainer container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title, Texture.BIO_GENERATOR);
    }

    @Override
    protected void drawBackground(MatrixStack matrix, float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(matrix, partialTicks, mouseX, mouseY);
        Texture.ENERGY_GAUGE.drawScalableH(matrix, this.te.getEnergy().subSized(), this.guiLeft + 5, this.guiTop + 5);
        if(this.te.isGenerating())
            Texture.PROGRESS.drawScalableH(matrix, this.te.getGenerator().subSized(), this.guiLeft + 113, this.guiTop + 24);

        FluidTank tank = this.te.getTank();
        if(!tank.isEmpty()) {
            FluidStack stack = tank.getFluid();
            FluidAttributes fa = stack.getFluid().getAttributes();
            ResourceLocation still = fa.getStillTexture(stack);
            if(still != null) {
                int color = fa.getColor(stack);
                float red = (color >> 16 & 0xFF) / 255.0F;
                float green = (color >> 8 & 0xFF) / 255.0F;
                float blue = (color & 0xFF) / 255.0F;
                RenderSystem.color3f(red, green, blue);
                TextureAtlasSprite sprite = this.mc.getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(still);
                bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
                gaugeV(sprite, this.guiLeft + 157, this.guiTop + 5, 14, 62, tank.getCapacity(), tank.getFluidAmount());
                RenderSystem.color3f(1.0F, 1.0F, 1.0F);
            }
        }
    }
}
