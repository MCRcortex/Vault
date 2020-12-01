package iskallia.vault.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.config.StreamerMultipliersConfig;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.item.ItemTraderCore;
import iskallia.vault.vending.TraderCore;
import iskallia.vault.world.data.StreamData;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;

import static net.minecraft.command.Commands.argument;
import static net.minecraft.command.Commands.literal;

public class InternalCommand extends Command {

    @Override
    public String getName() {
        return "internal";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("received_sub")
                .then(argument("subscriber", StringArgumentType.word())
                        .executes(context -> this.receivedSub(context, StringArgumentType.getString(context, "subscriber")))));

        builder.then(literal("received_sub_gift")
                .then(argument("gifter", StringArgumentType.word())
                        .then(argument("amount", IntegerArgumentType.integer())
                                .then(argument("tier", IntegerArgumentType.integer())
                                        .executes(context -> this.receivedSubGift(context, StringArgumentType.getString(context, "gifter"), IntegerArgumentType.getInteger(context, "amount"), IntegerArgumentType.getInteger(context, "tier")))))));

        builder.then(literal("received_donation")
                .then(argument("donator", StringArgumentType.word())
                        .then(argument("amount", IntegerArgumentType.integer())
                                .executes(context -> this.receivedDonation(context, StringArgumentType.getString(context, "donator"), IntegerArgumentType.getInteger(context, "amount"))))));

        builder.then(literal("received_bit_donation")
                .then(argument("donator", StringArgumentType.word())
                        .then(argument("amount", IntegerArgumentType.integer())
                                .executes(context -> this.receivedBitDonation(context, StringArgumentType.getString(context, "donator"), IntegerArgumentType.getInteger(context, "amount"))))));
    }

    private int receivedSub(CommandContext<CommandSource> context, String subscriber) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        StreamData.get(player.getServerWorld()).onSub(player.getServer(), player.getUniqueID(), subscriber);
        return 0;
    }

    private int receivedSubGift(CommandContext<CommandSource> context, String gifter, int amount, int tier) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        String mcNick = player.getDisplayName().getString();
        StreamerMultipliersConfig.StreamerMultipliers multipliers = ModConfigs.STREAMER_MULTIPLIERS.ofStreamer(mcNick);
        float multiplier = tier == 1 ? multipliers.weightPerGiftedSubT1
                : tier == 2 ? multipliers.weightPerGiftedSubT2
                : multipliers.weightPerGiftedSubT3;
        StreamData.get(player.getServerWorld()).onDono(player.getServer(), player.getUniqueID(), gifter, (int) (amount * multiplier));
        return 0;
    }

    private int receivedDonation(CommandContext<CommandSource> context, String donator, int amount) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        String mcNick = player.getDisplayName().getString();
        int multiplier = ModConfigs.STREAMER_MULTIPLIERS.ofStreamer(mcNick).weightPerDonationUnit;
        StreamData.get(player.getServerWorld()).onDono(player.getServer(), player.getUniqueID(), donator, amount * multiplier);
        if (amount >= 25) {
            ItemStack core = ItemTraderCore.generate(donator, amount >= 100);
            player.dropItem(core, false, false);
        }
        return 0;
    }

    private int receivedBitDonation(CommandContext<CommandSource> context, String donator, int amount) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        String mcNick = player.getDisplayName().getString();
        int multiplier = ModConfigs.STREAMER_MULTIPLIERS.ofStreamer(mcNick).weightPerHundredBits;
        StreamData.get(player.getServerWorld()).onDono(player.getServer(), player.getUniqueID(), donator, (amount / 100) * multiplier);
        if (amount >= 2500) {
            ItemStack core = ItemTraderCore.generate(donator, amount >= 10000);
            player.dropItem(core, false, false);
        }
        return 0;
    }

    @Override
    public boolean isDedicatedServerOnly() {
        return false;
    }

}
