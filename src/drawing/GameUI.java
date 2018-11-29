package drawing;

import playercontrols.Player;
import processing.core.PConstants;

import java.util.List;

public class GameUI {
    private final Sketch sketch;
    float healthBarWidth;
    float xhealthBarMargin;
    float yhealthBarMargin;
    float yhealthBarHeight;

    public GameUI(Sketch sketch) {
        this.sketch = sketch;


    }

    public void draw(List<Player> players) {
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            drawPlayerInfo(player, i, players.size());
        }
    }

    private void drawPlayerInfo(Player player, int i, int totalDivisions) {
        drawHealthBar(player.getPercentageRemainingHealth(), i, totalDivisions);
    }

    private void drawHealthBar(double remainingHealth, int i, int totalDivisions) {
        yhealthBarHeight = sketch.height / 30f;
        System.out.println(yhealthBarHeight);
        yhealthBarMargin = sketch.height / 30f;
        xhealthBarMargin = sketch.width / 10f;
        sketch.rectMode(PConstants.CORNERS);

        float leftBoundary = ((float) i / totalDivisions) * sketch.width;
        float rightBoundary = ((float) (i + 1) / totalDivisions) * sketch.width;
        float healthBarWidth = ((float) (1) / totalDivisions) * sketch.width - 2 * xhealthBarMargin;
        float leftX = leftBoundary + xhealthBarMargin;
        float rightX = rightBoundary - xhealthBarMargin;
        float rightX2 = (float) ((rightX) - healthBarWidth*(1- remainingHealth));

        sketch.fill(255, 0, 0, 100);
        sketch.rect(leftX, yhealthBarMargin, rightX, yhealthBarMargin + yhealthBarHeight);
        sketch.fill(0, 255, 0, 100);
        sketch.rect(leftX, yhealthBarMargin, rightX2, yhealthBarMargin + yhealthBarHeight);
    }
}
