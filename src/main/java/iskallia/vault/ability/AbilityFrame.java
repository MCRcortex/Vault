package iskallia.vault.ability;

import iskallia.vault.Vault;
import iskallia.vault.util.ResourceBoundary;

public enum AbilityFrame {

	STAR(new ResourceBoundary(Vault.id("textures/gui/skill-widget.png"), 0, 31, 30, 30)),
	RECTANGULAR(new ResourceBoundary(Vault.id("textures/gui/skill-widget.png"), 30, 31, 30, 30));

	ResourceBoundary resourceBoundary;

	AbilityFrame(ResourceBoundary resourceBoundary) {
		this.resourceBoundary = resourceBoundary;
	}

	public ResourceBoundary getResourceBoundary() {
		return resourceBoundary;
	}

}
