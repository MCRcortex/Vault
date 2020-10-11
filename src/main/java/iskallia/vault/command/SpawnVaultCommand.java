package iskallia.vault.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.init.ModFeatures;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.SectionPos;
import net.minecraft.world.biome.BiomeRegistry;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class SpawnVaultCommand extends Command {

	@Override
	public String getName() {
		return "spawn_vault";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public void build(LiteralArgumentBuilder<CommandSource> builder) {
		builder.executes(this::generateAtPlayer)
				.then(Commands.argument("location", BlockPosArgument.blockPos())
						.executes(this::generateAt));
	}

	private int generateAtPlayer(CommandContext<CommandSource> context) {
		Entity entity = context.getSource().getEntity();
		if(entity != null)this.generate(context, entity.getPosition());
		return 0;
	}

	private int generateAt(CommandContext<CommandSource> context) throws CommandSyntaxException {
		this.generate(context, BlockPosArgument.getBlockPos(context, "location"));
		return 0;
	}

	private void generate(CommandContext<CommandSource> context, BlockPos position) {
		ServerWorld world = context.getSource().getWorld();

		ChunkPos chunkPos = new ChunkPos(position.getX() >> 4, position.getZ() >> 4);
		IChunk chunk = world.getChunk(chunkPos.x, chunkPos.z);
		StructureStart<?> structurestart = world.func_241112_a_().func_235013_a_(
				SectionPos.from(chunk.getPos(), 0), ModFeatures.VAULT_FEATURE.field_236268_b_, chunk);


		int i = structurestart != null ? structurestart.getRefCount() : 0;
		StructureSeparationSettings structureseparationsettings = new StructureSeparationSettings(1, 0, -1);
		StructureStart<?> structurestart1 = ModFeatures.VAULT_FEATURE.func_242771_a(world.func_241828_r(),
				world.getChunkProvider().generator, world.getChunkProvider().generator.getBiomeProvider(),
				world.getStructureTemplateManager(), world.getSeed(), chunkPos,
				BiomeRegistry.PLAINS, i, structureseparationsettings);

		structurestart1.func_230366_a_(world, world.func_241112_a_(), world.getChunkProvider().generator, new Random(),
				MutableBoundingBox.func_236990_b_(), chunkPos);
	}

	@Override
	public boolean isDedicatedServerOnly() {
		return false;
	}

}
