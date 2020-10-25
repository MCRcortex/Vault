package iskallia.vault.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.world.data.VaultRaidData;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import static net.minecraft.command.Commands.literal;

public class RaidCommand extends Command {

	@Override
	public String getName() {
		return "raid";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public void build(LiteralArgumentBuilder<CommandSource> builder) {
		builder.then(literal("start").executes(this::startRaid));
	}

	private int startRaid(CommandContext<CommandSource> context) throws CommandSyntaxException {
		VaultRaidData.get(context.getSource().getWorld()).startNew(context.getSource().asPlayer());
		context.getSource().sendFeedback(new StringTextComponent( "Generating vault, please wait...").mergeStyle(TextFormatting.GREEN), true);
		return 0;
	}

	@Override
	public boolean isDedicatedServerOnly() {
		return false;
	}

}
