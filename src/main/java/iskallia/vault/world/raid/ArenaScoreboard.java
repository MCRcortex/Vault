package iskallia.vault.world.raid;

import com.google.common.collect.ImmutableMap;
import iskallia.vault.entity.ArenaFighterEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.FloatNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;

public class ArenaScoreboard implements INBTSerializable<CompoundNBT> {

	private final ArenaRaid raid;
	private Map<String, Float> damageMap = new HashMap<>();

	public ArenaScoreboard(ArenaRaid raid) {
		this.raid = raid;
	}

	public Map<String, Float> get() {
		return ImmutableMap.copyOf(this.damageMap);
	}

	public void onDamage(ArenaFighterEntity fighter, float amount) {
		String name = fighter.getDisplayName().getString();
		this.damageMap.put(name, this.damageMap.getOrDefault(name, 0.0F) + amount);
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT nbt = new CompoundNBT();

		ListNBT nameList = new ListNBT();
		ListNBT amountList = new ListNBT();

		this.damageMap.forEach((name, amount) -> {
			nameList.add(StringNBT.valueOf(name));
			amountList.add(FloatNBT.valueOf(amount));
		});

		nbt.put("NameList", nameList);
		nbt.put("AmountList", amountList);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		this.damageMap.clear();
		ListNBT nameList = nbt.getList("NameList", Constants.NBT.TAG_STRING);
		ListNBT amountList = nbt.getList("AmountList", Constants.NBT.TAG_FLOAT);

		if(nameList.size() != amountList.size()) {
			throw new IllegalStateException("Map doesn't have the same amount of keys as values");
		}

		for(int i = 0; i < nameList.size(); i++) {
			this.damageMap.put(nameList.getString(i), amountList.getFloat(i));
		}
	}

}
