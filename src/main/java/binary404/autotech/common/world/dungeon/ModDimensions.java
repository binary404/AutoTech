package binary404.autotech.common.world.dungeon;

import binary404.autotech.AutoTech;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class ModDimensions {

    public static RegistryKey<World> DUNGEON_KEY = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, AutoTech.key("dungeon"));
}
