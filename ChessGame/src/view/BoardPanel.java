package view;


import java.util.ArrayList;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.GridLayout;
import java.awt.Point;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import constant.GameConstant;
import controller.Controller;
import model.Board;
import model.Move;
import model.Piece;

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
    


    public class TilePanel extends JPanel {
        /**
    	 * 
    	 */
    	private static final long serialVersionUID = -2514285753432436084L;
    	private Point position;

        public TilePanel(Point position, final Board board) {
            super(new GridLayout());
            this.position = position;
            this.assignTileColor();
            this.assignPieceImage(board);
            setPreferredSize(GameConstant.TILE_PANEL_DIMENSION);
        }

        public Point getPosition() {
            return this.position;
        }

        public void drawTile(final Controller chessGame) {
            this.assignTileColor();

            Piece selectedPiece = chessGame.getSelectedPiece();
            Piece invalidPiece = chessGame.getInvalidSelectedPiece();
            Piece incheckKing = chessGame.getGameBoard().getInCheck();
            Piece lastMovedPiece = chessGame.getLastmovedPiece();

            if((incheckKing != null && incheckKing.getPiecePosition().equals(this.position)) ||
               (invalidPiece != null && invalidPiece.getPiecePosition().equals(this.position))) {
                this.setBackground(GameConstant.WARNING_COLOR);
            }

            if ((lastMovedPiece != null && lastMovedPiece.getPiecePosition().equals(this.position)) ||
                (selectedPiece != null && selectedPiece.getPiecePosition().equals(this.position))) {
                this.setBackground(GameConstant.SELECTED_COLOR);
            }

            if (chessGame.getSelectedPiece() != null) {
                for (Move pos : chessGame.getSelectedPiece().calculateLegalMoves(chessGame.getGameBoard(), true)) {
                    if (pos.getMoveTo().equals(this.position)) {
                        this.setBackground(GameConstant.MOVE_COLOR);
                    }
                }
            }

            this.assignPieceImage(chessGame.getGameBoard());
            validate();
            repaint();
        }

        private void assignPieceImage(final Board board) {
            // Clear previous information
            this.removeAll();

            Piece piece = board.getPieceAt(this.position);
            if (piece != null)  {
                String name = piece.getImageName();
                String color = piece.isWhite() ? "w" : "b";
                try {
                    BufferedImage image = ImageIO.read(new File(GameConstant.RESOURCE_PATH + color + name + ".png"));

                    // Sets the scale of image icon
                    Image img = image.getScaledInstance(65, 65, Image.SCALE_SMOOTH);
                    JLabel label = new JLabel(new ImageIcon(img));
                    this.add(label);
                } catch (IOException e) {
                    System.out.println(e.toString());
                    System.out.println("Cannot load images!");
                }
            }
        }

        private void assignTileColor() {
            this.setBackground((position.x + position.y) % 2 == 0 ? GameConstant.LIGHT_COLOR : GameConstant.DARK_COLOR);
        }
    }
}
