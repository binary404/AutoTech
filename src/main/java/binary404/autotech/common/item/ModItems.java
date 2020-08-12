package binary404.autotech.common.item;

import binary404.autotech.AutoTech;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import static binary404.autotech.common.core.util.RegistryUtil.register;

@Mod.EventBusSubscriber(modid = AutoTech.modid, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {

    @ObjectHolder("autotech:copper_ingot")
    public static Item copper_ingot;

    @ObjectHolder("autotech:tin_ingot")
    public static Item tin_ingot;

    @ObjectHolder("autotech:lead_ingot")
    public static Item lead_ingot;

    public static Item.Properties properties = new Item.Properties().group(AutoTech.group);

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> r = event.getRegistry();

        register(r, new Item(properties), "copper_ingot");
        register(r, new Item(properties), "tin_ingot");
        register(r, new Item(properties), "lead_ingot");
    }

}
