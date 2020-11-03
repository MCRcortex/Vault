package iskallia.vault.world.gen.structure;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import iskallia.vault.Vault;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPatternRegistry;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.template.ProcessorLists;

public class VaultPools {

	public static final JigsawPattern START = JigsawPatternRegistry.func_244094_a(
			new JigsawPattern(Vault.id("vault/starts"), new ResourceLocation("empty"), ImmutableList.of(
					Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/rooms/start1"), ProcessorLists.field_244101_a), 1),
					Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/rooms/start2"), ProcessorLists.field_244101_a), 1),
					Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/rooms/start3"), ProcessorLists.field_244101_a), 1),
					Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/rooms/start4"), ProcessorLists.field_244101_a), 1),
					Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/rooms/start5"), ProcessorLists.field_244101_a), 1),
					Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/rooms/start6"), ProcessorLists.field_244101_a), 1),
					Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/rooms/start7"), ProcessorLists.field_244101_a), 1),
					Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/rooms/start8"), ProcessorLists.field_244101_a), 1),
					Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/rooms/start9"), ProcessorLists.field_244101_a), 1),
					Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/rooms/start10"), ProcessorLists.field_244101_a), 1),
					Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/rooms/start11"), ProcessorLists.field_244101_a), 1),
					Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/rooms/start12"), ProcessorLists.field_244101_a), 1),
					Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/rooms/start13"), ProcessorLists.field_244101_a), 1),
					Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/rooms/start14"), ProcessorLists.field_244101_a), 1),
					Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/rooms/start15"), ProcessorLists.field_244101_a), 1),
					Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/rooms/start16"), ProcessorLists.field_244101_a), 1),
					Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/rooms/start17"), ProcessorLists.field_244101_a), 1),
					Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/rooms/start18"), ProcessorLists.field_244101_a), 1)
			), JigsawPattern.PlacementBehaviour.RIGID));

	public static void init() {

	}

	/*
	static {
		JigsawPatternRegistry.func_244094_a(
				new JigsawPattern(Vault.id("vault/starts"), Vault.id("vault/rooms"), ImmutableList.of(
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/rooms/big1"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/rooms/big2"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/rooms/big3"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/rooms/big4"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/rooms/big5"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/rooms/big6"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/rooms/big7"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/rooms/big8"), ProcessorLists.field_244101_a), 1)
				), JigsawPattern.PlacementBehaviour.RIGID));

		JigsawPatternRegistry.func_244094_a(
				new JigsawPattern(Vault.id("vault/tunnels"), new ResourceLocation("empty"), ImmutableList.of(
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/h1"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/h2"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/h3"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/h4"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/h5"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/h6"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/v1"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/v2"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/v3"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b("minecraft:empty", ProcessorLists.field_244101_a), 9)
				), JigsawPattern.PlacementBehaviour.RIGID));

		JigsawPatternRegistry.func_244094_a(
				new JigsawPattern(Vault.id("vault/tunnels"), new ResourceLocation("empty"), ImmutableList.of(
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/h1"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/h2"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/h3"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/h4"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/h5"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/h6"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/v1"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/v2"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/v3"), ProcessorLists.field_244101_a), 1)
				), JigsawPattern.PlacementBehaviour.RIGID));
	}*/

}
