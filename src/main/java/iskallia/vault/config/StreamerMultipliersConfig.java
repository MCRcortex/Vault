package iskallia.vault.config;

import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.Map;

public class StreamerMultipliersConfig extends Config {

    @Expose private Map<String, StreamerMultipliers> MULTIPLIERS = new HashMap<>();

    @Override
    public String getName() {
        return "streamer_multipliers";
    }

    public StreamerMultipliers ofStreamer(String mcNickname) {
        StreamerMultipliers multipliers = MULTIPLIERS.get(mcNickname);
        if (multipliers == null) return new StreamerMultipliers();
        return multipliers;
    }

    @Override
    protected void reset() {
        MULTIPLIERS.put("iskall85", new StreamerMultipliers());
    }

    public static class StreamerMultipliers {
        @Expose public int weightPerGiftedSubT1 = 5;
        @Expose public int weightPerGiftedSubT2 = 10;
        @Expose public int weightPerGiftedSubT3 = 25;
        @Expose public int weightPerDonationUnit = 1;
        @Expose public int weightPerHundredBits = 1;
        @Expose public int subsNeededForArena = 100;
    }

}
