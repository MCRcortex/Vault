package iskallia.vault.config;

import iskallia.vault.skill.ability.AbilityGroup;

import java.util.Arrays;
import java.util.List;

public class AbilitiesConfig extends Config {

    @Override
    public String getName() {
        return "abilities";
    }

    public List<AbilityGroup<?>> getAll() {
        return Arrays.asList();
    }

    public AbilityGroup<?> getByName(String name) {
        return this.getAll().stream().filter(group -> group.getParentName().equals(name)).findFirst()
                .orElseThrow(() -> new IllegalStateException("Unknown ability with name " + name));
    }

    @Override
    protected void reset() { }

}
