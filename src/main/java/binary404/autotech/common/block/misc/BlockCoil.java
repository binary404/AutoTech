package binary404.autotech.common.block.misc;

import binary404.autotech.common.block.CoilType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraftforge.common.ToolType;

public class BlockCoil extends Block {

    CoilType type;

    public BlockCoil(CoilType type) {
        super(Properties.create(Material.IRON).hardnessAndResistance(5.0F, 10.0F).sound(SoundType.METAL).harvestLevel(2).harvestTool(ToolType.PICKAXE));
        this.type = type;
    }

    public CoilType getType() {
        return type;
    }
}
