package binary404.autotech.data;

import binary404.autotech.AutoTech;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.SurvivesExplosion;
import net.minecraft.loot.functions.CopyNbt;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class BlockLootProvider implements IDataProvider {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final DataGenerator generator;
    private final Map<Block, Function<Block, LootTable.Builder>> functionTable = new HashMap<>();

    public BlockLootProvider(DataGenerator generator) {
        this.generator = generator;

    }

    @Override
    public void act(DirectoryCache cache) throws IOException {
        Map<ResourceLocation, LootTable.Builder> tables = new HashMap<>();

        for (Block b : Registry.BLOCK) {
            ResourceLocation id = Registry.BLOCK.getKey(b);
            if (!AutoTech.modid.equals(id.getNamespace())) {
                continue;
            }
            Function<Block, LootTable.Builder> func = functionTable.getOrDefault(b, BlockLootProvider::genRegular);
            tables.put(id, func.apply(b));
        }

        for (Map.Entry<ResourceLocation, LootTable.Builder> e : tables.entrySet()) {
            Path path = getPath(generator.getOutputFolder(), e.getKey());
            IDataProvider.save(GSON, cache, LootTableManager.toJson(e.getValue().setParameterSet(LootParameterSets.BLOCK).build()), path);
        }
    }

    private static Path getPath(Path root, ResourceLocation id) {
        return root.resolve("data/" + id.getNamespace() + "/loot_tables/blocks/" + id.getPath() + ".json");
    }

    private static LootTable.Builder empty(Block b) {
        return LootTable.builder();
    }

    private static LootTable.Builder genCopyNbt(Block b, String... tags) {
        LootEntry.Builder<?> entry = ItemLootEntry.builder(b);
        CopyNbt.Builder func = CopyNbt.builder(CopyNbt.Source.BLOCK_ENTITY);
        for (String tag : tags) {
            func = func.replaceOperation(tag, "BlockEntityTag." + tag);
        }
        LootPool.Builder pool = LootPool.builder().name("main").rolls(ConstantRange.of(1)).addEntry(entry)
                .acceptCondition(SurvivesExplosion.builder())
                .acceptFunction(func);
        return LootTable.builder().addLootPool(pool);
    }

    private static LootTable.Builder genRegular(Block b) {
        LootEntry.Builder<?> entry = ItemLootEntry.builder(b);
        LootPool.Builder pool = LootPool.builder().name("main").rolls(ConstantRange.of(1)).addEntry(entry)
                .acceptCondition(SurvivesExplosion.builder());
        return LootTable.builder().addLootPool(pool);
    }

    private static LootTable.Builder genMultiBlockSpecial(Block a, Block b) {
        LootEntry.Builder entryA = ItemLootEntry.builder(a);
        LootEntry.Builder entryB = ItemLootEntry.builder(b);
        LootPool.Builder poolA = LootPool.builder().name("a").rolls(ConstantRange.of(1)).addEntry(entryA).acceptCondition(SurvivesExplosion.builder());
        LootPool.Builder poolB = LootPool.builder().name("b").rolls(ConstantRange.of(1)).addEntry(entryB).acceptCondition(SurvivesExplosion.builder());
        return LootTable.builder().addLootPool(poolA).addLootPool(poolB);
    }

    @Nonnull
    @Override
    public String getName() {
        return "Runic block loot tables";
    }

}
