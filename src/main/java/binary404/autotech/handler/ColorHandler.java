package binary404.autotech.handler;

import binary404.autotech.common.block.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;

public class ColorHandler {

    public static void init() {
        BlockColors blocks = Minecraft.getInstance().getBlockColors();

        IBlockColor lv_cable = (state, world, pos, tint) -> tint == 1 ? 0xbf8808 : 0xFFFFFF;
        blocks.register(lv_cable, ModBlocks.lv_cable);
    }

}
