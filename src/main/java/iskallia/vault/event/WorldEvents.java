package iskallia.vault.event;

import iskallia.vault.skill.ability.AbilityNode;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.skill.ability.type.VeinMinerAbility;
import iskallia.vault.world.data.PlayerAbilitiesData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WorldEvents {

    @SubscribeEvent
    public static void onBlockMined(BlockEvent.BreakEvent event) {
        if (!event.getWorld().isRemote()) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
            AbilityTree abilityTree = PlayerAbilitiesData.get((ServerWorld) event.getWorld()).getAbilities(player);

            if (!abilityTree.isActive()) return;

            AbilityNode<?> focusedAbility = abilityTree.getFocusedAbility();

            if (focusedAbility != null && focusedAbility.getAbility() instanceof VeinMinerAbility) {
                // TODO: Vein Mineeeeeeeeeeeeee!
                System.out.println("VEIN MINEEEEEEEE!");
                abilityTree.lockSwapping(true);
            }
        }
    }

}
