package binary404.autotech.common.block;

import binary404.autotech.common.world.dungeon.ModDimensions;
import binary404.autotech.common.world.dungeon.data.Dungeon;
import binary404.autotech.common.world.dungeon.data.DungeonData;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameType;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;

import java.util.Optional;
import java.util.Random;

public class BlockPortal extends NetherPortalBlock {

    public BlockPortal() {
        super(Properties.from(Blocks.NETHER_PORTAL));
        this.setDefaultState(this.getStateContainer().getBaseState().with(AXIS, Direction.Axis.X));
    }

    protected static BlockPos getSpawnPoint(ServerWorld p_241092_0_, int p_241092_1_, int p_241092_2_, boolean p_241092_3_) {
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(p_241092_1_, 0, p_241092_2_);
        Biome biome = p_241092_0_.getBiome(blockpos$mutable);
        boolean flag = p_241092_0_.getDimensionType().getHasCeiling();
        BlockState blockstate = biome.getGenerationSettings().getSurfaceBuilderConfig().getTop();
        if (p_241092_3_ && !blockstate.getBlock().isIn(BlockTags.VALID_SPAWN)) {
            return null;
        } else {
            Chunk chunk = p_241092_0_.getChunk(p_241092_1_ >> 4, p_241092_2_ >> 4);
            int i = flag ? p_241092_0_.getChunkProvider().getChunkGenerator().getGroundHeight() : chunk.getTopBlockY(Heightmap.Type.MOTION_BLOCKING, p_241092_1_ & 15, p_241092_2_ & 15);
            if (i < 0) {
                return null;
            } else {
                int j = chunk.getTopBlockY(Heightmap.Type.WORLD_SURFACE, p_241092_1_ & 15, p_241092_2_ & 15);
                if (j <= i && j > chunk.getTopBlockY(Heightmap.Type.OCEAN_FLOOR, p_241092_1_ & 15, p_241092_2_ & 15)) {
                    return null;
                } else {
                    for (int k = i + 1; k >= 0; --k) {
                        blockpos$mutable.setPos(p_241092_1_, k, p_241092_2_);
                        BlockState blockstate1 = p_241092_0_.getBlockState(blockpos$mutable);
                        if (!blockstate1.getFluidState().isEmpty()) {
                            break;
                        }

                        if (blockstate1.equals(blockstate)) {
                            return blockpos$mutable.up().toImmutable();
                        }
                    }

                    return null;
                }
            }
        }
    }

    @Override
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {

    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        World world = null;
        if (worldIn instanceof World) {
            world = (World) worldIn;
        }

        if (world != null) {
            if (world.getDimensionKey() == World.OVERWORLD) {
                Direction.Axis direction$axis = facing.getAxis();
                Direction.Axis direction$axis1 = stateIn.get(AXIS);
                boolean flag = direction$axis1 != direction$axis && direction$axis.isHorizontal();
                return !flag && !facingState.isIn(this) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
            }
        }

        return stateIn;
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (world.isRemote || !(entity instanceof PlayerEntity)) return;
        if (entity.isPassenger() || entity.isBeingRidden() || !entity.isNonBoss()) return;

        ServerPlayerEntity player = (ServerPlayerEntity) entity;
        VoxelShape playerVoxel = VoxelShapes.create(player.getBoundingBox().offset(-pos.getX(), -pos.getY(), -pos.getZ()));

        if (VoxelShapes.compare(playerVoxel, state.getShape(world, pos), IBooleanFunction.AND)) {
            RegistryKey<World> worldKey = world.getDimensionKey() == ModDimensions.DUNGEON_KEY ? World.OVERWORLD : ModDimensions.DUNGEON_KEY;
            ServerWorld destination = ((ServerWorld) world).getServer().getWorld(worldKey);

            if (destination == null)
                return;

            //Reset cooldown.
            if (player.func_242280_ah()) {
                player.func_242279_ag();
                return;
            }

            world.getServer().runAsync(() -> {
                if (worldKey == World.OVERWORLD) {
                    ServerPlayerEntity playerEntity = (ServerPlayerEntity) entity;
                    BlockPos blockPos = playerEntity.func_241140_K_();
                    Optional<Vector3d> spawn = blockPos == null ? Optional.empty() : PlayerEntity.func_242374_a(destination,
                            blockPos, playerEntity.func_242109_L(), playerEntity.func_241142_M_(), true);

                    if (spawn.isPresent()) {
                        BlockState blockstate = destination.getBlockState(blockPos);
                        Vector3d vector3d = spawn.get();

                        if (!blockstate.isIn(BlockTags.BEDS) && !blockstate.isIn(Blocks.RESPAWN_ANCHOR)) {
                            playerEntity.teleport(destination, vector3d.x, vector3d.y, vector3d.z, playerEntity.func_242109_L(), 0.0F);
                        } else {
                            Vector3d vector3d1 = Vector3d.copyCenteredHorizontally(blockPos).subtract(vector3d).normalize();
                            playerEntity.teleport(destination, vector3d.x, vector3d.y, vector3d.z,
                                    (float) MathHelper.wrapDegrees(MathHelper.atan2(vector3d1.z, vector3d1.x) * (double) (180F / (float) Math.PI) - 90.0D), 0.0F);
                        }
                    } else {
                        this.moveToSpawn(destination, player);
                    }
                } else if (worldKey == ModDimensions.DUNGEON_KEY) {
                    DungeonData.get(destination).startNew(player);
                }
            });

            if (worldKey == ModDimensions.DUNGEON_KEY) {
                world.setBlockState(pos, Blocks.AIR.getDefaultState());
                System.out.println("Setting To Air");
            }

            player.func_242279_ag();
        }
    }

    private void moveToSpawn(ServerWorld world, ServerPlayerEntity player) {
        BlockPos blockpos = world.getSpawnPoint();

        if (world.getDimensionType().hasSkyLight() && world.getServer().func_240793_aU_().getGameType() != GameType.ADVENTURE) {
            int i = Math.max(0, world.getServer().getSpawnRadius(world));
            int j = MathHelper.floor(world.getWorldBorder().getClosestDistance(blockpos.getX(), blockpos.getZ()));
            if (j < i) {
                i = j;
            }

            if (j <= 1) {
                i = 1;
            }

            long k = i * 2 + 1;
            long l = k * k;
            int i1 = l > 2147483647L ? Integer.MAX_VALUE : (int) l;
            int j1 = i1 <= 16 ? i1 - 1 : 17;
            int k1 = (new Random()).nextInt(i1);

            for (int l1 = 0; l1 < i1; ++l1) {
                int i2 = (k1 + j1 * l1) % i1;
                int j2 = i2 % (i * 2 + 1);
                int k2 = i2 / (i * 2 + 1);
                BlockPos blockpos1 = getSpawnPoint(world, blockpos.getX() + j2 - i, blockpos.getZ() + k2 - i, false);
                if (blockpos1 != null) {
                    player.teleport(world, blockpos1.getX(), blockpos1.getY(), blockpos1.getZ(), 0.0F, 0.0F);

                    if (world.hasNoCollisions(player)) {
                        break;
                    }
                }
            }
        } else {
            player.teleport(world, blockpos.getX(), blockpos.getY(), blockpos.getZ(), 0.0F, 0.0F);

            while (!world.hasNoCollisions(player) && player.getPosY() < 255.0D) {
                player.teleport(world, player.getPosX(), player.getPosY() + 1.0D, player.getPosZ(), 0.0F, 0.0F);
            }
        }
    }
}
