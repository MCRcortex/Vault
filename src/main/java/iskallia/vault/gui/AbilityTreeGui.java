package iskallia.vault.gui;

import iskallia.vault.ability.AbilityTree;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;

public class AbilityTreeGui extends Screen {

	private AbilityTree tree;

	protected AbilityTreeGui() {
		super(new StringTextComponent("Ability Tree"));
	}

	public void syncTree(AbilityTree tree) {
		this.tree = tree;
	}

}
