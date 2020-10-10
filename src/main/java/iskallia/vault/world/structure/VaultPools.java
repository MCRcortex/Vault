package iskallia.vault.world.structure;

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
					Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/starts/start_1"), ProcessorLists.field_244101_a), 1),
					Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/starts/start2"), ProcessorLists.field_244101_a), 1),
					Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/starts/start3"), ProcessorLists.field_244101_a), 1)
			), JigsawPattern.PlacementBehaviour.RIGID));

	public static void init() {

	}

	static {
		JigsawPatternRegistry.func_244094_a(
				new JigsawPattern(Vault.id("vault/tunnels"), new ResourceLocation("empty"), ImmutableList.of(
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/tunnel_1"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/tunnel_2"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/tunnel_3"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/tunnel_4"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/tunnel_5"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/tunnel_6"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/tunnel_7"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/tunnel_8"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/tunnel_9"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/tunnel_10"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/tunnel_11"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/tunnel_12"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/tunnel_13"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/tunnel_14"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/tunnel_15"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/tunnel_16"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/tunnel_17"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/tunnel_18"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/tunnel_19"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/tunnel_20"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/tunnel_21"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/tunnel_22"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/tunnel_23"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/tunnel_24"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/tunnel_25"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/tunnel_26"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/tunnel_27"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/tunnel_28"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/tunnel_29"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/tunnel_30"), ProcessorLists.field_244101_a), 1),
						Pair.of(JigsawPiece.func_242861_b(Vault.sId("vault/tunnels/tunnel_31"), ProcessorLists.field_244101_a), 1)
				), JigsawPattern.PlacementBehaviour.RIGID));
	}

}
