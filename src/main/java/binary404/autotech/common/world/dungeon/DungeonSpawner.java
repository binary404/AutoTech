package binary404.autotech.common.world.dungeon;

import binary404.autotech.common.world.dungeon.data.Dungeon;
import net.minecraft.entity.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;

public class DungeonSpawner {

    private static EntityType[] entityTypes = {EntityType.ZOMBIE, EntityType.SKELETON, EntityType.CREEPER, EntityType.CAVE_SPIDER, EntityType.PILLAGER};

    private final Dungeon dungeon;
    private List<LivingEntity> mobs = new ArrayList<>();

    public DungeonSpawner(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    public int getMaxMobs() {
        return 9;
    }

    public void tick(ServerPlayerEntity player) {
        if (player.world.getDimensionKey() != ModDimensions.DUNGEON_KEY)
            return;
        if (this.dungeon.ticksLeft + 300 > this.dungeon.sTickLeft)
            return;

        this.mobs.removeIf(entity -> {
            if (entity.getDistanceSq(player) > 18 * 18) {
                entity.remove();
                return true;
            }
            return false;
        });

        if (this.mobs.size() >= this.getMaxMobs())
            return;

        List<BlockPos> spaces = this.getSpawningSpaces(player);

        while (this.mobs.size() < this.getMaxMobs() && spaces.size() > 0) {
            BlockPos pos = spaces.remove(player.getServerWorld().getRandom().nextInt(spaces.size()));
            this.spawn(player.getServerWorld(), pos);
        }
    }

    private List<BlockPos> getSpawningSpaces(ServerPlayerEntity player) {
        List<BlockPos> spaces = new ArrayList<>();

        for (int x = -18; x <= 18; x++) {
            for (int z = -18; z <= 18; z++) {
                for (int y = -5; y <= 5; y++) {
                    ServerWorld world = player.getServerWorld();
                    BlockPos pos = player.getPosition().add(x, y, z);

                    if (player.getDistanceSq(pos.getX(), pos.getY(), pos.getZ()) > 18 * 18) {
                        continue;
                    }

                    if (!world.getBlockState(pos).canEntitySpawn(world, pos, EntityType.ZOMBIE))
                        continue;
                    boolean isAir = true;

                    for (int o = 1; o <= 2; o++) {
                        if (world.getBlockState(pos.up(o)).isSuffocating(world, pos)) {
                            isAir = false;
                            break;
                        }
                    }

                    if (isAir) {
                        spaces.add(pos.up());
                    }
                }
            }
        }
        return spaces;
    }

    public void spawn(ServerWorld world, BlockPos pos) {
        EntityType type = entityTypes[world.rand.nextInt(entityTypes.length)];

        Entity e = type.create(world);

        if (e instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) e;
            entity.setLocationAndAngles(pos.getX() + 0.5F, pos.getY() + 0.2F, pos.getZ() + 0.5F, 0.0F, 0.0F);
            world.summonEntity(entity);

            if (entity instanceof MobEntity) {
                ((MobEntity) entity).spawnExplosionParticle();
                ((MobEntity) entity).onInitialSpawn(world, new DifficultyInstance(Difficulty.HARD, 13000L, 0L, 0L), SpawnReason.STRUCTURE, null, null);
            }

            this.mobs.add(entity);
        }
    }

}
