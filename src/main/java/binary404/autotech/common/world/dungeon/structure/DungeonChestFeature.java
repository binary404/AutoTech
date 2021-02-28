package binary404.autotech.common.world.dungeon.structure;

import binary404.autotech.AutoTech;
import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.event.RegistryEvent;

import java.util.Random;

public class DungeonChestFeature extends Feature<NoFeatureConfig> {

    public static Feature<NoFeatureConfig> INSTANCE;

    public DungeonChestFeature(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean func_241855_a(ISeedReader world, ChunkGenerator gen, Random rand, BlockPos pos, NoFeatureConfig config) {
        for (int i = 0; i < 128; i++) {

            int x = rand.nextInt(16);
            int y = rand.nextInt(256);
            int z = rand.nextInt(16);
            BlockPos c = pos.add(x, y, z);

            if (world.getBlockState(c).getBlock() == Blocks.AIR && !world.getBlockState(c.down()).isAir()) {
                world.setBlockState(c, Blocks.CHEST.getDefaultState(), 2);
                TileEntity te = world.getTileEntity(c);

                if (te instanceof ChestTileEntity) {
                    ((ChestTileEntity) te).setLootTable(AutoTech.key("chest/dungeon_chest"), 0);
                }

                return true;
            }
        }
        return false;
    }

    public static void register(RegistryEvent.Register<Feature<?>> event) {
        INSTANCE = new DungeonChestFeature(NoFeatureConfig.field_236558_a_);
        INSTANCE.setRegistryName(AutoTech.key("dungeon_chest"));
        event.getRegistry().register(INSTANCE);
    }

}
