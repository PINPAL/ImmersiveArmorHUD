package toni.immersivearmorhud;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import toni.immersivearmorhud.foundation.GuiOverlayHandler;
import toni.immersivearmorhud.foundation.config.AllConfigs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


#if FABRIC
    import net.fabricmc.api.ClientModInitializer;
    import net.fabricmc.api.ModInitializer;
    #if mc >= 215
    import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
    import fuzs.forgeconfigapiport.fabric.api.v5.client.ConfigScreenFactoryRegistry;
    import net.neoforged.neoforge.client.gui.ConfigurationScreen;
    #elif after_21_1
    import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
    import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.client.ConfigScreenFactoryRegistry;
    import net.neoforged.neoforge.client.gui.ConfigurationScreen;
    #endif

    #if current_20_1
    import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
    #endif
#endif

#if FORGE
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;
#endif


#if NEO
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import toni.lib.utils.VersionUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
#endif


#if FORGELIKE
@Mod("immersivearmorhud")
#endif
#if FORGE
@EventBusSubscriber(modid = ImmersiveArmorHUD.ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
#endif
public class ImmersiveArmorHUD #if FABRIC implements ModInitializer, ClientModInitializer #endif
{
    public static final String MODNAME = "Immersive Armor HUD";
    public static final String ID = "immersivearmorhud";
    public static final Logger LOGGER = LogManager.getLogger(MODNAME);

    public ImmersiveArmorHUD(#if NEO IEventBus modEventBus, ModContainer modContainer #endif) {
        #if FORGE
        var context = FMLJavaModLoadingContext.get();
        var modEventBus = context.getModEventBus();
        #endif

        #if FORGELIKE
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);

        AllConfigs.register((type, spec) -> {
            #if FORGE
            ModLoadingContext.get().registerConfig(type, spec);
            #elif NEO
            modContainer.registerConfig(type, spec);
            #endif
        });
        #endif

        #if NEO
        modEventBus.addListener((FMLClientSetupEvent event) -> event.enqueueWork(() -> {
            modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        }));

        modEventBus.addListener((final RegisterGuiLayersEvent evt) -> {
            evt.registerAbove(VanillaGuiLayers.VEHICLE_HEALTH, VersionUtils.resource(ID, "armor_hud"), (gui, delta) -> GuiOverlayHandler.ARMOR_HUD.accept(gui));
        });
        #endif
    }

    #if FORGE
    @SubscribeEvent
    public static void onRegisterGuiOverlays(final RegisterGuiOverlaysEvent evt) {
        evt.registerAbove(VanillaGuiOverlay.MOUNT_HEALTH.id(), "armor_hud", GuiOverlayHandler.ARMOR_HUD);
    }
    #endif

    #if FABRIC @Override #endif
    public void onInitialize() {
        #if FABRIC
        AllConfigs.register((type, spec) -> {
            #if mc >= 215
            ConfigRegistry.INSTANCE.register(ImmersiveArmorHUD.ID, type, spec);
            #elif AFTER_21_1
            NeoForgeConfigRegistry.INSTANCE.register(ImmersiveArmorHUD.ID, type, spec);
            #else
            ForgeConfigRegistry.INSTANCE.register(ImmersiveArmorHUD.ID, type, spec);
            #endif
        });
        #endif
    }

    #if FABRIC @Override #endif
    public void onInitializeClient() {
        #if AFTER_21_1
            #if FABRIC
            ConfigScreenFactoryRegistry.INSTANCE.register(ImmersiveArmorHUD.ID, ConfigurationScreen::new);
            #endif
        #endif
    }

    // Forg event stubs to call the Fabric initialize methods, and set up cloth config screen
    #if FORGELIKE
    public void commonSetup(FMLCommonSetupEvent event) { onInitialize(); }
    public void clientSetup(FMLClientSetupEvent event) { onInitializeClient(); }
    #endif
}
