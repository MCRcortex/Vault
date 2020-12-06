package iskallia.vault.init;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import iskallia.vault.Vault;
import iskallia.vault.command.*;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import org.omg.CORBA.INTERNAL;

import java.util.function.Supplier;

import static net.minecraft.command.Commands.literal;

public class ModCommands {

    public static ReloadConfigsCommand RELOAD_CONFIGS;
    public static RaidCommand RAID;
    public static VaultLevelCommand VAULT_LEVEL;
    public static InternalCommand INTERNAL;
    public static DebugCommand DEBUG;

    public static void registerCommands(CommandDispatcher<CommandSource> dispatcher, Commands.EnvironmentType env) {
        RELOAD_CONFIGS = registerCommand(ReloadConfigsCommand::new, dispatcher, env);
        RAID = registerCommand(RaidCommand::new, dispatcher, env);
        VAULT_LEVEL = registerCommand(VaultLevelCommand::new, dispatcher, env);
        INTERNAL = registerCommand(InternalCommand::new, dispatcher, env);
        DEBUG = registerCommand(DebugCommand::new, dispatcher, env);
    }

    public static <T extends Command> T registerCommand(Supplier<T> supplier, CommandDispatcher<CommandSource> dispatcher, Commands.EnvironmentType env) {
        T command = supplier.get();

        if (!command.isDedicatedServerOnly() || env == Commands.EnvironmentType.DEDICATED || env == Commands.EnvironmentType.ALL) {
            LiteralArgumentBuilder<CommandSource> builder = literal(command.getName());
            builder.requires((sender) -> sender.hasPermissionLevel(command.getRequiredPermissionLevel()));
            command.build(builder);
            dispatcher.register(literal(Vault.MOD_ID).then(builder));
        }

        return command;
    }

}
