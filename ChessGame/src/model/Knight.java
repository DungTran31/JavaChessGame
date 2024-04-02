package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2691461770869694876L;

	public Knight(Point piecePosition, boolean white) {
        this.piecePosition = piecePosition;
        this.white = white;
        this.firstMove = true;
    }

    private Knight(Point piecePosition, boolean white, boolean firstMove) {
        this.piecePosition = piecePosition;
        this.white = white;
        this.firstMove = firstMove;
    }

    @Override
    public List<Move> calculateLegalMoves(Board board, boolean checkKing) {
        int x = piecePosition.x;
        int y = piecePosition.y;

        List<Move> moves = new ArrayList<Move>();

        // if no board given, return empty list
        if (board == null) {
        	return moves;        	
        }

        int[] dx = { 1, -1, 1, -1, 2, 2, -2, -2 };
        int[] dy = { 2, 2, -2, -2, -1, 1, -1, 1 };
        // check L-shapes
        for (int i = 0; i < 8; i++) {
        	Point pos = new Point(x + dx[i], y + dy[i]);
        	// if the location is valid
            if (board.ValidPos(pos)) {
                // and the location does not contain same color piece
                Piece oppositePiece = board.getPieceAt(pos);
                if (oppositePiece == null || oppositePiece.white != this.white) {
                    // add the move to the list
                    moves.add(new Move(this, pos, oppositePiece));
                }
            }
        }

        // Remove moves that making own king in check
        if(checkKing) {
            this.removeMovesPutsKingInCheck(board, moves);
        }

        return moves;
    }
    
    @Override
    public String toString() {
    	return "N";
	}

    @Override
    public Knight clone() {
        return new Knight(this.piecePosition, this.white, this.firstMove);
    }

    @Override
    public String getImageName() {
        return "Knight";
    }
}