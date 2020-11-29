package iskallia.vault.client.gui.helper;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

public class ArenaScoreboardContainer {

    public List<ScoreboardEntry> scoreboard;

    public ArenaScoreboardContainer() {
        this.scoreboard = new LinkedList<>();
    }

    public String getMVP() {
        if (scoreboard.size() == 0) return null;
        return scoreboard.get(0).nickname;
    }

    public List<ScoreboardEntry> getTop(int n) {
        if (scoreboard.size() == 0) return new LinkedList<>();
        return scoreboard.subList(0, Math.min(n, scoreboard.size()));
    }

    public int getSize() {
        return scoreboard.size();
    }

    public void onDamageDealt(String nickname, float damageDealt) {
        int index = IntStream.range(0, scoreboard.size())
                .filter(i -> scoreboard.get(i).nickname.equals(nickname))
                .findFirst().orElse(-1);

        if (index == -1) {
            scoreboard.add(new ScoreboardEntry(nickname, damageDealt));
        } else {
            ScoreboardEntry entry = scoreboard.get(index);
            entry.totalDamage += damageDealt;
        }

        scoreboard.sort(Comparator.comparingDouble(o -> -o.totalDamage));
    }

    public void reset() {
        scoreboard.clear();
    }

    public static class ScoreboardEntry {
        public final String nickname;
        public float totalDamage;

        public ScoreboardEntry(String nickname, float totalDamage) {
            this.nickname = nickname;
            this.totalDamage = totalDamage;
        }

        @Override
        public String toString() {
            return String.format("%s=%f", nickname, totalDamage);
        }
    }

}
