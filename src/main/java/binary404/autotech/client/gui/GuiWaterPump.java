package binary404.autotech.client.gui;

import binary404.autotech.client.gui.core.GuiTile;
import binary404.autotech.client.gui.core.Texture;
import binary404.autotech.common.container.device.ContainerWaterPump;
import binary404.autotech.common.tile.device.TileWaterPump;
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

public class GuiWaterPump extends GuiTile<TileWaterPump, ContainerWaterPump> {

    public GuiWaterPump(ContainerWaterPump container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title, Texture.WATERPUMP);
    }

    @Override
    protected void drawBackground(MatrixStack matrix, float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(matrix, partialTicks, mouseX, mouseY);

        FluidTank tank = this.te.getTank();
        if (!tank.isEmpty()) {
            FluidStack stack = tank.getFluid();
            FluidAttributes fa = stack.getFluid().getAttributes();
            ResourceLocation still = fa.getStillTexture(stack);
            if (still != null) {
                int color = fa.getColor(stack);
                float red = (color >> 16 & 0xFF) / 255.0F;
                float green = (color >> 8 & 0xFF) / 255.0F;
                float blue = (color & 0xFF) / 255.0F;
                RenderSystem.color3f(red, green, blue);
                TextureAtlasSprite sprite = this.mc.getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(still);
                bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
                gaugeV(sprite, this.guiLeft + 81, this.guiTop + 5, 14, 62, tank.getCapacity(), tank.getFluidAmount());
                RenderSystem.color3f(1.0F, 1.0F, 1.0F);
            }
        }
    }

    @Override
    protected boolean hasRedstoneButton() {
        return false;
    }

    @Override
    protected boolean hasItemButton() {
        return false;
    }
}
