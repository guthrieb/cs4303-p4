package drawing;

import playercontrols.Player;
import processing.core.PConstants;

import java.util.List;

class GameUI {
    private final Sketch sketch;

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
        float leftBoundary = ((float) i / totalDivisions) * sketch.width;
        float rightBoundary = ((float) (i + 1) / totalDivisions) * sketch.width;
        float bottomBoundary = sketch.height*2 / 30f + sketch.height/90f + sketch.height / 50f;

        sketch.rectMode(PConstants.CORNERS);
        sketch.fill(0, 0, 0, 0);
        sketch.strokeWeight(4);
        sketch.stroke(player.playerColour.r, player.playerColour.g, player.playerColour.b, 200);
        float boundaryMargin = sketch.width/20f;
        float yBoundaryMargin = sketch.height/40f;
        sketch.rect(leftBoundary + boundaryMargin, yBoundaryMargin, rightBoundary - boundaryMargin, bottomBoundary + yBoundaryMargin);
        sketch.strokeWeight(1);
        sketch.stroke(0, 0, 0);

        drawHealthBar(player.getPercentageRemainingHealth(), i, totalDivisions);
        drawFireBar(player.firingMode.percentageReady(), i, totalDivisions);
    }

    private void drawHealthBar(double remainingHealth, int i, int totalDivisions) {
        float yhealthBarHeight = sketch.height / 30f;
        float yhealthBarMargin = sketch.height / 30f;
        float xhealthBarMargin = sketch.width / 10f;
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

    private void drawFireBar(double timeCompleted, int i, int totalDivisions) {
        float yFireBarHeight = sketch.height / 50f;
        float yFireBarMargin = sketch.height*2 / 30f + sketch.height/90f;
        float xFireBarMargin = sketch.width / 6f;
        sketch.rectMode(PConstants.CORNERS);

        float leftBoundary = ((float) i / totalDivisions) * sketch.width;
        float rightBoundary = ((float) (i + 1) / totalDivisions) * sketch.width;
        float healthBarWidth = ((float) (1) / totalDivisions) * sketch.width - 2 * xFireBarMargin;
        float leftX = leftBoundary + xFireBarMargin;
        float rightX = rightBoundary - xFireBarMargin;
        float rightX2 = (float) ((rightX) - healthBarWidth*(timeCompleted));

        sketch.fill(40, 40, 40, 100);
        sketch.rect(leftX, yFireBarMargin, rightX, yFireBarMargin + yFireBarHeight);
        sketch.fill(255, 255, 255, 200);
        sketch.rect(leftX, yFireBarMargin, rightX2, yFireBarMargin + yFireBarHeight);
    }


}
