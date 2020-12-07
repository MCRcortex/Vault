package iskallia.vault.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.world.data.StreamData;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;

import static net.minecraft.command.Commands.literal;

public class DebugCommand extends Command {

    @Override
    public String getName() {
        return "debug";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("20_subs")
                .executes(this::random20Subs));
    }

    private int random20Subs(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        MinecraftServer server = player.getServer();
        StreamData streamData = StreamData.get(player.getServerWorld());
        for (int i = 0; i < 20; i++) {
            streamData.onSub(server, player.getUniqueID(), "Test" + (i + 1), i * 5);
        }
        return 0;
    }

    @Override
    public boolean isDedicatedServerOnly() {
        return false;
    }

}
