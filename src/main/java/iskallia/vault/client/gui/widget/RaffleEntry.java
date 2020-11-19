package iskallia.vault.client.gui.widget;

import iskallia.vault.client.gui.helper.Rectangle;

public class RaffleEntry {

    protected Rectangle bounds;
    protected String occupantName;

    public RaffleEntry(String occupantName) {
        this.occupantName = occupantName;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public String getOccupantName() {
        return occupantName;
    }

}
