package binary404.autotech.data;

import binary404.autotech.AutoTech;
import binary404.autotech.common.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class AutoTechBlockStateProvider extends BlockStateProvider {

    public AutoTechBlockStateProvider(DataGenerator gen, ExistingFileHelper existingFileHelper) {
        super(gen, AutoTech.modid, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.simpleBlockState(ModBlocks.iron_plating);
        this.simpleBlockState(ModBlocks.heat_proof_casing);
        this.simpleBlockState(ModBlocks.stainless_steel_casing);
        this.simpleBlockState(ModBlocks.basic_coil);

        this.simpleBlockState(ModBlocks.reinforced_glass);

        this.simpleBlockState(ModBlocks.distilled_water);
        this.simpleBlockState(ModBlocks.crude_oil);
        this.simpleBlockState(ModBlocks.biomass);

        this.simpleBlockState(ModBlocks.rust);
        this.simpleBlockState(ModBlocks.rust2);

        this.simpleBlockState(ModBlocks.rubber_sapling);
        this.simpleBlockState(ModBlocks.rubber_leaves);
    }

    private void simpleBlockState(Block b) {
        this.simpleBlockState(b, model(b.getRegistryName()));
    }

    private void simpleBlockState(Block b, ModelFile targetModel) {
        getVariantBuilder(b).partialState().addModels(new ConfiguredModel(targetModel));
    }

    private ModelFile model(IForgeRegistryEntry<?> entry) {
        return model(entry.getRegistryName());
    }

    private ModelFile model(ResourceLocation name) {
        return new ModelFile.UncheckedModelFile(prefixPath(name, "block/"));
    }

    public static ResourceLocation prefixPath(ResourceLocation key, String prefix) {
        return new ResourceLocation(key.getNamespace(), prefix + key.getPath());
    }

}
