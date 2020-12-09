package iskallia.vault.command;

import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.world.data.GlobalTimeData;
import net.minecraft.command.CommandSource;
import net.minecraft.world.server.ServerWorld;

import static net.minecraft.command.Commands.literal;
import static net.minecraft.command.Commands.argument;

public class GlobalTimerCommand extends Command {

    @Override
    public String getName() {
        return "global_timer";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("reset")
                .executes(context -> this.resetTimer(context, 7776000L))
                .then(argument("seconds", LongArgumentType.longArg())
                        .executes(context -> this.resetTimer(context, LongArgumentType.getLong(context, "seconds")))));
    }

    private int resetTimer(CommandContext<CommandSource> context, long seconds) throws CommandSyntaxException {
        CommandSource source = context.getSource();
        ServerWorld world = source.getWorld();
        GlobalTimeData globalTimeData = GlobalTimeData.get(world);
        globalTimeData.reset(seconds);
        return 0;
    }

    @Override
    public boolean isDedicatedServerOnly() {
        return false;
    }

}
