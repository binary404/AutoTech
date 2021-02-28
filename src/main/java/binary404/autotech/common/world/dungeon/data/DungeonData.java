package binary404.autotech.common.world.dungeon.data;

import binary404.autotech.AutoTech;
import binary404.autotech.common.world.ModFeatures;
import binary404.autotech.common.world.ModStructures;
import binary404.autotech.common.world.dungeon.ModDimensions;
import it.unimi.dsi.fastutil.Hash;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.biome.BiomeRegistry;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DungeonData extends WorldSavedData {

    protected static final String DATA_NAME = AutoTech.modid + "_DungeonData";

    private Map<UUID, Dungeon> activeDungeons = new HashMap<>();
    private int xOffset = 0;

    public DungeonData() {
        this(DATA_NAME);
    }

    public DungeonData(String name) {
        super(name);
    }

    public Dungeon getAt(BlockPos pos) {
        return this.activeDungeons.values().stream().filter(dungeon -> dungeon.box.isVecInside(pos)).findFirst().orElse(null);
    }

    public void remove(ServerWorld server, UUID playerId) {
        Dungeon v = this.activeDungeons.remove(playerId);

        if (v != null) {
            v.ticksLeft = 0;
        }
    }

    public Dungeon getActiveFor(ServerPlayerEntity player) {
        return this.activeDungeons.get(player.getUniqueID());
    }

    public Dungeon startNew(ServerPlayerEntity player) {
        player.sendStatusMessage(new StringTextComponent("Generating Dungeon, please wait...").mergeStyle(TextFormatting.GREEN), true);

        Dungeon dungeon = new Dungeon(player.getUniqueID(), new MutableBoundingBox(
                this.xOffset, 0, 0, this.xOffset += Dungeon.REGION_SIZE, 256, Dungeon.REGION_SIZE
        ), 0);

        if (this.activeDungeons.containsKey(player.getUniqueID())) {
            this.activeDungeons.get(player.getUniqueID()).ticksLeft = 0;
        }

        this.activeDungeons.put(dungeon.getPlayerId(), dungeon);
        this.markDirty();

        ServerWorld world = player.getServer().getWorld(ModDimensions.DUNGEON_KEY);

        player.getServer().runAsync(() -> {
            try {
                ChunkPos pos = new ChunkPos((dungeon.box.minX + dungeon.box.getXSize() / 2) >> 4, (dungeon.box.minZ + dungeon.box.getZSize() / 2) >> 4);

                StructureSeparationSettings settings = new StructureSeparationSettings(1, 0, -1);

                StructureStart<?> start = ModFeatures.DUNGEON_FEATURE.func_242771_a(world.func_241828_r(), world.getChunkProvider().generator, world.getChunkProvider().generator.getBiomeProvider(), world.getStructureTemplateManager(), world.getSeed(), pos, BiomeRegistry.PLAINS, 0, settings);

                int chunkRadius = Dungeon.REGION_SIZE >> 5;

                for (int x = -chunkRadius; x <= chunkRadius; x += 17) {
                    for (int z = -chunkRadius; z <= chunkRadius; z += 17) {
                        world.getChunk(pos.x + x, pos.z + z, ChunkStatus.EMPTY, true).func_230344_a_(ModStructures.DUNGEON, start);
                    }
                }

                dungeon.start(world, player, pos);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return dungeon;
    }

    public void tick(ServerWorld world) {
        this.activeDungeons.values().forEach(dungeon -> dungeon.tick(world));

        boolean removed = false;

        List<Runnable> tasks = new ArrayList<>();

        for (Dungeon dungeon : this.activeDungeons.values()) {
            if (dungeon.isComplete()) {
                tasks.add(() -> this.remove(world, dungeon.playerId));
                removed = true;
            }
        }

        tasks.forEach(Runnable::run);

        if (removed || this.activeDungeons.size() > 0) {
            this.markDirty();
        }
    }

    @SubscribeEvent
    public static void onTick(TickEvent.WorldTickEvent event) {
        if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.START && event.world.getDimensionKey() == ModDimensions.DUNGEON_KEY) {
            get((ServerWorld) event.world).tick((ServerWorld) event.world);
        }
    }

    @Override
    public void read(CompoundNBT nbt) {
        this.activeDungeons.clear();

        nbt.getList("ActiveDungeons", Constants.NBT.TAG_COMPOUND).forEach(dungeonNbt -> {
            Dungeon dungeon = Dungeon.fromNBT((CompoundNBT) dungeonNbt);
            this.activeDungeons.put(dungeon.getPlayerId(), dungeon);
        });

        this.xOffset = nbt.getInt("XOffset");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        ListNBT dungeonList = new ListNBT();
        this.activeDungeons.values().forEach(dungeon -> dungeonList.add(dungeon.serializeNBT()));
        compound.put("ActiveDungeons", dungeonList);

        compound.putInt("XOffset", this.xOffset);
        return compound;
    }

    public static DungeonData get(ServerWorld world) {
        return world.getServer().func_241755_D_().getSavedData().getOrCreate(DungeonData::new, DATA_NAME);
    }
}
