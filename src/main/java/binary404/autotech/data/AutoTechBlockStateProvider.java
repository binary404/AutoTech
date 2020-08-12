package binary404.autotech.data;

import binary404.autotech.AutoTech;
import binary404.autotech.common.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class AutoTechBlockStateProvider extends BlockStateProvider {

    public AutoTechBlockStateProvider(DataGenerator gen, ExistingFileHelper existingFileHelper) {
        super(gen, AutoTech.modid, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.simpleBlockState(ModBlocks.copper_ore);
        this.simpleBlockState(ModBlocks.tin_ore);
        this.simpleBlockState(ModBlocks.lead_ore);
        this.simpleBlockState(ModBlocks.silver_ore);
        this.simpleBlockState(ModBlocks.uranium_ore);
        this.simpleBlockState(ModBlocks.nickel_ore);
        this.simpleBlockState(ModBlocks.platinum_ore);
        this.simpleBlockState(ModBlocks.titanium_ore);
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
