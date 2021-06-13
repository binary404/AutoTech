package binary404.autotech.common.block.multiblock;

import binary404.autotech.common.block.BlockTile;
import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.util.FluidHelper;
import binary404.autotech.common.tile.multiblock.*;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fluids.FluidUtil;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockFluidHatch extends BlockTile {

    public static List<BlockFluidHatch> fluidHatches = new ArrayList<>();

    boolean isExportHatch;
    Tier tier;

    public BlockFluidHatch(boolean isExportHatch, Tier tier) {
        super();
        this.isExportHatch = isExportHatch;
        this.tier = tier;
        fluidHatches.add(this);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileFluidHatch(isExportHatch, tier);
    }

    public Tier getTier() {
        return tier;
    }
}
