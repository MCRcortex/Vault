package iskallia.vault.research;

import iskallia.vault.Vault;
import iskallia.vault.world.data.PlayerResearchesData;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.*;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = Vault.MOD_ID)
public class StageManager {

    private static ResearchTree getResearchTree(PlayerEntity player) {
        if (player.world.isRemote) {
            return new ResearchTree(player.getUniqueID()); // TODO

        } else {
            return PlayerResearchesData.get((ServerWorld) player.world)
                    .getResearches(player);
        }
    }

    private static void warnResearchRequirement(String researchName, String i18nKey) {
        TextComponent name = new StringTextComponent(researchName);
        Style style = Style.EMPTY.setColor(Color.fromInt(0xFF_fce336));
        name.setStyle(style);

        TextComponent text = new TranslationTextComponent("overlay.requires_research." + i18nKey, name);

        Minecraft.getInstance().ingameGUI.setOverlayMessage(text, false);
    }

    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        PlayerEntity player = event.getPlayer();
        ResearchTree researchTree = getResearchTree(player);

        ItemStack craftedItemStack = event.getCrafting();
        IInventory craftingMatrix = event.getInventory();

        String restrictedBy = researchTree.restrictedBy(craftedItemStack.getItem(), Restrictions.Type.CRAFTABILITY);

        if (restrictedBy == null)
            return; // Doesn't restrict craftability of this item, so stop here.

        if (!event.getPlayer().world.isRemote) {
            warnResearchRequirement(restrictedBy, "craft");
        }

        for (int i = 0; i < craftingMatrix.getSizeInventory(); i++) {
            ItemStack itemStack = craftingMatrix.getStackInSlot(i);
            if (itemStack != ItemStack.EMPTY) {
                ItemStack itemStackToDrop = itemStack.copy();
                itemStackToDrop.setCount(1);
                player.dropItem(itemStackToDrop, false, false);
            }
        }

        int slot = player.inventory.getSlotFor(craftedItemStack);

        if (slot != -1) {
            player.inventory.getStackInSlot(slot).shrink(1);

        } else {
            craftedItemStack.shrink(craftedItemStack.getCount());
        }
    }

    @SubscribeEvent
    public static void onItemUse(PlayerInteractEvent.RightClickItem event) {
        if (!event.isCancelable()) return;

        PlayerEntity player = event.getPlayer();
        ResearchTree researchTree = getResearchTree(player);

        Item usedItem = event.getItemStack().getItem();

        String restrictedBy = researchTree.restrictedBy(usedItem, Restrictions.Type.USABILITY);

        if (restrictedBy == null)
            return; // Doesn't restrict usability of this item, so stop here.

        if (event.getSide() == LogicalSide.CLIENT) {
            warnResearchRequirement(restrictedBy, "usage");
        }

        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {
        if (!event.isCancelable()) return;

        PlayerEntity player = event.getPlayer();
        ResearchTree researchTree = getResearchTree(player);

        Item usedItem = event.getItemStack().getItem();

        String restrictedBy = researchTree.restrictedBy(usedItem, Restrictions.Type.USABILITY);

        if (restrictedBy == null)
            return; // Doesn't restrict usability of this item, so stop here.

        if (event.getSide() == LogicalSide.CLIENT) {
            warnResearchRequirement(restrictedBy, "usage");
        }

        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onBlockInteraction(PlayerInteractEvent.RightClickBlock event) {
        if (!event.isCancelable()) return;

        PlayerEntity player = event.getPlayer();
        ResearchTree researchTree = getResearchTree(player);

        String restrictedBy;

        BlockState blockState = player.world.getBlockState(event.getPos());
        restrictedBy = researchTree.restrictedBy(blockState.getBlock(), Restrictions.Type.BLOCK_INTERACTABILITY);
        if (restrictedBy != null) {
            if (event.getSide() == LogicalSide.CLIENT) {
                warnResearchRequirement(restrictedBy, "interact_block");
            }
            event.setCanceled(true);
            return;
        }

        ItemStack itemStack = event.getItemStack();
        if (itemStack == ItemStack.EMPTY) return;

        Item item = itemStack.getItem();
        restrictedBy = researchTree.restrictedBy(item, Restrictions.Type.USABILITY);
        if (restrictedBy != null) {
            if (event.getSide() == LogicalSide.CLIENT) {
                warnResearchRequirement(restrictedBy, "usage");
            }
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onBlockHit(PlayerInteractEvent.LeftClickBlock event) {
        if (!event.isCancelable()) return;

        PlayerEntity player = event.getPlayer();
        ResearchTree researchTree = getResearchTree(player);

        BlockState blockState = player.world.getBlockState(event.getPos());

        String restrictedBy;

        restrictedBy = researchTree.restrictedBy(blockState.getBlock(), Restrictions.Type.HITTABILITY);
        if (restrictedBy != null) {
            if (event.getSide() == LogicalSide.CLIENT) {
                warnResearchRequirement(restrictedBy, "hit");
            }
            event.setCanceled(true);
            return;
        }

        ItemStack itemStack = event.getItemStack();
        if (itemStack == ItemStack.EMPTY) return;

        Item item = itemStack.getItem();
        restrictedBy = researchTree.restrictedBy(item, Restrictions.Type.USABILITY);
        if (restrictedBy != null) {
            if (event.getSide() == LogicalSide.CLIENT) {
                warnResearchRequirement(restrictedBy, "usage");
            }
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onEntityInteraction(PlayerInteractEvent.EntityInteract event) {
        if (!event.isCancelable()) return;

        PlayerEntity player = event.getPlayer();
        ResearchTree researchTree = getResearchTree(player);
        Entity entity = event.getEntity();

        String restrictedBy;

        restrictedBy = researchTree.restrictedBy(entity.getType(), Restrictions.Type.ENTITY_INTERACTABILITY);
        if (restrictedBy != null) {
            if (event.getSide() == LogicalSide.CLIENT) {
                warnResearchRequirement(restrictedBy, "interact_entity");
            }
            event.setCanceled(true);
            return;
        }

        ItemStack itemStack = event.getItemStack();
        if (itemStack == ItemStack.EMPTY) return;

        Item item = itemStack.getItem();
        restrictedBy = researchTree.restrictedBy(item, Restrictions.Type.USABILITY);
        if (restrictedBy != null) {
            if (event.getSide() == LogicalSide.CLIENT) {
                warnResearchRequirement(restrictedBy, "usage");
            }
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPlayerAttack(AttackEntityEvent event) {
        if (!event.isCancelable()) return;

        PlayerEntity player = event.getPlayer();
        ResearchTree researchTree = getResearchTree(player);
        Entity entity = event.getEntity();

        String restrictedBy;

        restrictedBy = researchTree.restrictedBy(entity.getType(), Restrictions.Type.ENTITY_INTERACTABILITY);
        if (restrictedBy != null) {
            if (player.world.isRemote) {
                warnResearchRequirement(restrictedBy, "interact_entity");
            }
            event.setCanceled(true);
            return;
        }

        ItemStack itemStack = player.getHeldItemMainhand();
        if (itemStack == ItemStack.EMPTY) return;

        Item item = itemStack.getItem();
        restrictedBy = researchTree.restrictedBy(item, Restrictions.Type.USABILITY);
        if (restrictedBy != null) {
            if (player.world.isRemote) {
                warnResearchRequirement(restrictedBy, "usage");
            }
            event.setCanceled(true);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onItemTooltip(ItemTooltipEvent event) {
        PlayerEntity player = event.getPlayer();

        if (player == null) return;

        ResearchTree researchTree = getResearchTree(player);
        Item item = event.getItemStack().getItem();

        String restrictionCausedBy = Arrays.stream(Restrictions.Type.values())
                .map(type -> researchTree.restrictedBy(item, type))
                .filter(Objects::nonNull)
                .findFirst().orElseGet(() -> null);

        if (restrictionCausedBy == null) return;

        List<ITextComponent> toolTip = event.getToolTip();

        Style textStyle = Style.EMPTY.setColor(Color.fromInt(0xFF_a8a8a8));
        Style style = Style.EMPTY.setColor(Color.fromInt(0xFF_fce336));
        TextComponent text = new TranslationTextComponent("tooltip.requires_research");
        TextComponent name = new StringTextComponent(" " + restrictionCausedBy);
        text.setStyle(textStyle);
        name.setStyle(style);
        toolTip.add(new StringTextComponent(""));
        toolTip.add(text);
        toolTip.add(name);
    }

}
