package toni.immersivearmorhud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import toni.immersivearmorhud.foundation.config.AllConfigs;
import toni.lib.utils.ColorUtils;
import toni.lib.utils.VersionUtils;

import java.util.Map;

#if mc >= 215
import net.minecraft.client.renderer.RenderType;
#else
import net.minecraft.world.item.ArmorItem;
#endif

#if mc >= 211
import net.minecraft.core.component.DataComponents;
#endif

public class ArmorBarRenderer {
    public static final EquipmentSlot[] ARMOR_SLOTS = new EquipmentSlot[] { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET };

    public static Map<String, String> configuredPrefixes = Map.ofEntries(
        Map.entry("minecraft:diamond", "diamond"),
        Map.entry("betterend:thallasium", "diamond"),
        Map.entry("betterend:terminite", "diamond"),
        Map.entry("deeperdarker:resonarium", "diamond"),
        Map.entry("minecraft:netherite", "netherite"),
        Map.entry("minecraft:iron", "iron"),
        Map.entry("minecraft:chainmail", "iron"),
        Map.entry("minecraft:gold", "gold"),
        Map.entry("minecraft:leather", "leather"),
        Map.entry("minecraft:elytra", "elytra"),
        Map.entry("aether:zanite", "zanite"),
        Map.entry("aether:gravitite", "gravitite"),
        Map.entry("aether:phoenix", "phoenix"),
        Map.entry("aether:neptune", "neptune"),
        Map.entry("aether:valkyrie", "valkyrie"),
        Map.entry("aether:obsidian", "obsidian"),
        Map.entry("create:copper", "copper_diving"),
        Map.entry("create:netherite", "netherite_diving"),
        Map.entry("create:cardboard", "cardboard")
    );

    private static ResourceLocation getTextureForItem(Item item) {
        var itemID = BuiltInRegistries.ITEM.getKey(item).toString();
        var path = "iron";

        for (var prefix : configuredPrefixes.entrySet()) {
            if (itemID.contains(prefix.getKey()))
            {
                path = prefix.getValue();
                break;
            }
        }

        #if mc >= 211
        return VersionUtils.resource("immersivearmorhud", path);
        #else
        return VersionUtils.resource("immersivearmorhud", "textures/gui/sprites/" + path + ".png");
        #endif
    }

    private static float getDurabilityPercentage(ItemStack stack) {
        int maxDurability = getMaxDamage(stack.getItem(), stack);
        int currentDurability = stack.getDamageValue();

        return 1f - (currentDurability / (float)maxDurability);
    }

    private static int getMaxDamage(Item item, ItemStack stack) {
        #if mc >= 211
        return (Integer) stack.getOrDefault(DataComponents.MAX_DAMAGE, 0);
        #else
        return item.getMaxDamage();
        #endif
    }

    private static int getDurabilityColor(float durability) {
        if (durability > 0.51) return ColorUtils.color(0, 255, 0);
        if (durability > 0.25) return ColorUtils.color(255, 165, 0);
        return ColorUtils.color(255, 0, 0);
    }

    public static boolean render(GuiGraphics graphics, int vanillaHeight) {
        var client = Minecraft.getInstance();
        var player = client.player;
        var level = client.level;
        if (player == null || level == null)
            return false;

        boolean rightAligned = AllConfigs.client().rightAligned.get();
        int yOffset = AllConfigs.client().yOffset.get();
        int xOffset = AllConfigs.client().xOffset.get();

        int screenWidth = graphics.guiWidth();

        int slotOffset = 0;
        boolean flag = false;

        for (int i = 0; i < ARMOR_SLOTS.length; i++) {
            var slot = ARMOR_SLOTS[i];
            ItemStack stack = player.getItemBySlot(slot);
            if (!stack.isEmpty()) {
                flag = true;
                int u = i * 8;
                int x = (rightAligned ? 149 : 0) + xOffset + (screenWidth / 2) - 91 + (slotOffset * 8);
                int y = yOffset + vanillaHeight;

                #if mc >= 215
                graphics.blitSprite(RenderType::guiTextured, getTextureForItem(stack.getItem()), 33, 9, u, 0, x, y, 8, 8);
                #elif mc >= 211
                graphics.blitSprite(getTextureForItem(stack.getItem()), 33, 9, u, 0, x, y, 8, 8);
                #else
                graphics.blit(getTextureForItem(stack.getItem()), x, y, 8, 8, u, 0, 8, 8, 33, 9);
                #endif

                float durability = getDurabilityPercentage(stack);
                if (durability < 0.5f) {
                    int barWidth = (int) (6 * durability);
                    int color = getDurabilityColor(durability);

                    graphics.fill(x + 1, y + 7, x + 8, y + 8, ColorUtils.color(0, 0, 0));
                    graphics.fill(x + 1, y + 7, x + 2 + barWidth, y + 8, color);
                }

                slotOffset++;
            }
        }

        return flag;
    }
}
