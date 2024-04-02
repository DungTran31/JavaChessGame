package view;

import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import constant.GameConstant;
import model.Board;
import model.Move;
import model.Piece;
import controller.Controller;

public class TilePanel extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2514285753432436084L;
	private Point position;

    public TilePanel(Point position, final Board board) {
        super(new GridBagLayout()); // để quản lý vị trí và cỡ của các thành phần (hình ảnh quân cờ) một cách linh hoạt.
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
