package constant;

import java.awt.Color;
import java.awt.Dimension;

public interface GameConstant {
	static final int GAME_SIZE = 8; // number of cells per row or column
    static final Dimension BOARD_PANEL_DIMENSION = new Dimension(600, 600);
    static final Dimension TILE_PANEL_DIMENSION = new Dimension(BOARD_PANEL_DIMENSION.width / GAME_SIZE,
            													BOARD_PANEL_DIMENSION.height / GAME_SIZE);
    static final Color LIGHT_COLOR = Color.decode("#fdce9d"); // Color of white cells
    static final Color DARK_COLOR = Color.decode("#d18b46"); // Color of black cells
    static final Color SELECTED_COLOR = Color.decode("#2596be"); // Cell's color of selected piece
    static final Color MOVE_COLOR = Color.decode("#28a745"); // Possible move of selected piece
    static final String RESOURCE_PATH = "resources/";
    static final Color WARNING_COLOR = Color.decode("#be4d25"); // Cell's color of checked king
}
