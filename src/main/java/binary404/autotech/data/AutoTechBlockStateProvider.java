package binary404.autotech.data;

import binary404.autotech.AutoTech;
import binary404.autotech.client.renders.core.Textures;
import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.block.generator.BlockGenerator;
import binary404.autotech.common.block.machine.BlockMachine;
import binary404.autotech.common.block.misc.BlockCasing;
import binary404.autotech.common.block.multiblock.BlockEnergyHatch;
import binary404.autotech.common.block.multiblock.BlockFluidHatch;
import binary404.autotech.common.block.multiblock.BlockItemHatch;
import binary404.autotech.common.block.multiblock.BlockMultiblock;
import binary404.autotech.common.block.transfer.CableBlock;
import binary404.autotech.common.block.world.BlockAutoTechOre;
import binary404.autotech.common.fluid.BlockBasicFlowingFluid;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Set;
import java.util.stream.Collectors;

public class AutoTechBlockStateProvider extends BlockStateProvider {

    public AutoTechBlockStateProvider(DataGenerator gen, ExistingFileHelper existingFileHelper) {
        super(gen, AutoTech.modid, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        Set<Block> remainingBlocks = Registry.BLOCK.stream()
                .filter(b -> AutoTech.modid.equals(Registry.BLOCK.getKey(b).getNamespace()))
                .collect(Collectors.toSet());

        remainingBlocks.removeAll(BlockAutoTechOre.ores);
        remainingBlocks.removeAll(CableBlock.cables);
        remainingBlocks.remove(ModBlocks.rubber_log);
        remainingBlocks.remove(ModBlocks.bronze_boiler_casing);
        remainingBlocks.removeAll(BlockBasicFlowingFluid.fluids);

        particleOnly(remainingBlocks, ModBlocks.lv_furnace, Textures.CASINGS[0].getParticleTexture());
        remainingBlocks.remove(ModBlocks.lv_furnace);

        for (BlockCasing casing : BlockCasing.casings) {
            String blockName = Registry.BLOCK.getKey(casing).getPath();
            ModelFile model = models().cubeAll(blockName, AutoTech.key("block/casings/solid/" + blockName));
            simpleBlock(casing, model);
        }
        remainingBlocks.removeAll(BlockCasing.casings);
        for (BlockMachine machine : BlockMachine.machines) {
            particleOnly(remainingBlocks, machine, Textures.CASINGS[machine.getTier().ordinal()].getParticleTexture());
        }
        remainingBlocks.removeAll(BlockMachine.machines);
        for (BlockGenerator generator : BlockGenerator.generators) {
            particleOnly(remainingBlocks, generator, Textures.CASINGS[generator.getTier().ordinal()].getParticleTexture());
        }
        remainingBlocks.removeAll(BlockGenerator.generators);
        for (BlockMultiblock multiblock : BlockMultiblock.multiblocks) {
            particleOnly(remainingBlocks, multiblock, multiblock.getParticleRenderer().getParticleTexture());
        }
        remainingBlocks.removeAll(BlockMultiblock.multiblocks);
        for (BlockFluidHatch hatch : BlockFluidHatch.fluidHatches) {
            particleOnly(remainingBlocks, hatch, Textures.CASINGS[hatch.getTier().ordinal()].getParticleTexture());
        }
        remainingBlocks.removeAll(BlockFluidHatch.fluidHatches);
        for (BlockItemHatch hatch : BlockItemHatch.itemHatches) {
            particleOnly(remainingBlocks, hatch, Textures.CASINGS[hatch.getTier().ordinal()].getParticleTexture());
        }
        remainingBlocks.removeAll(BlockItemHatch.itemHatches);
        for (BlockEnergyHatch hatch : BlockEnergyHatch.energyHatches) {
            particleOnly(remainingBlocks, hatch, Textures.CASINGS[hatch.getTier().ordinal()].getParticleTexture());
        }
        remainingBlocks.removeAll(BlockEnergyHatch.energyHatches);

        remainingBlocks.forEach(this::simpleBlock);
    }

    private void particleOnly(Set<Block> blocks, Block b, ResourceLocation particle) {
        String name = Registry.BLOCK.getKey(b).getPath();
        ModelFile f = models().getBuilder(name)
                .texture("particle", particle);
        simpleBlock(b, f);
        blocks.remove(b);
    }
}
