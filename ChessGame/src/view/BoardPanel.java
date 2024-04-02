package view;

import java.awt.GridLayout;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JPanel;

import constant.GameConstant;
import controller.Controller;

public class BoardPanel extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4365241468362875755L;
	private ArrayList<TilePanel> boardTiles;
    private Controller chessGame;
    public BoardPanel(final Controller chessGame) {
        super(new GridLayout(GameConstant.GAME_SIZE, GameConstant.GAME_SIZE));
        this.chessGame = chessGame;
        boardTiles = new ArrayList<>();
        TilePanel tilePanel;
        for (int row = 0; row < GameConstant.GAME_SIZE; ++row) {
            for (int col = 0; col < GameConstant.GAME_SIZE; ++col) {
                tilePanel = new TilePanel(new Point(col, row), chessGame.getGameBoard());
                this.boardTiles.add(tilePanel);
                this.add(tilePanel);
            }
        }
        this.setPreferredSize(GameConstant.BOARD_PANEL_DIMENSION);
    }

    public ArrayList<TilePanel> getBoardTiles() {
        return boardTiles;
    }

    public void drawBoard() {
        removeAll();
        for (TilePanel tilePanel : boardTiles) {
            tilePanel.drawTile(this.chessGame);
            add(tilePanel);
        }
        validate();
        repaint();
    }
}
