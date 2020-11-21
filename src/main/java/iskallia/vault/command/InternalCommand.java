package iskallia.vault.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.world.data.StreamData;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;

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
		builder.then(literal("add_sub")
				.then(argument("subscriber", StringArgumentType.word())
						.executes(context -> this.addSub(context, StringArgumentType.getString(context, "subscriber")))));

		builder.then(literal("add_dono")
				.then(argument("donator", StringArgumentType.word())
				.then(argument("amount", IntegerArgumentType.integer())
						.executes(context -> this.addDono(context, StringArgumentType.getString(context, "donator"), IntegerArgumentType.getInteger(context, "amount"))))));
	}

	private int addSub(CommandContext<CommandSource> context, String subscriber) throws CommandSyntaxException {
		ServerPlayerEntity player = context.getSource().asPlayer();
		StreamData.get(player.getServerWorld()).onSub(player.getUniqueID(), subscriber);
		return 0;
	}

	private int addDono(CommandContext<CommandSource> context, String donator, int amount) throws CommandSyntaxException {
		ServerPlayerEntity player = context.getSource().asPlayer();
		StreamData.get(player.getServerWorld()).onDono(player.getUniqueID(), donator, amount);
		return 0;
	}

	@Override
	public boolean isDedicatedServerOnly() {
		return false;
	}

}
