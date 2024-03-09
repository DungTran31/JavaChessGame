package model;

import java.awt.Point;
import java.util.List;
/*
- "abstract": it cannot be directly instantiated, and must be subclassed to be used
- "Cloneable": object can be cloned

By implementing these interfaces, the Piece class provides support for creating
new objects of the class through cloning, and also provides support for storing
or transmitting objects of the class in binary format.
 */


public abstract class Piece implements Cloneable {
    protected boolean white; //is this white piece ot not?
    protected Point piecePosition;
    protected boolean firstMove = true;

    public boolean isWhite() {
    	return this.white;
    }

    public Point getPiecePosition() {
    	return piecePosition;
	}

    public boolean isFirstMove() {
    	return firstMove;
    }

    public void setWhite(boolean white) {
    	this.white = white;
    }

    public void setPiecePosition(Point piecePosition) {
    	this.piecePosition = piecePosition;
	}

    public void setFirstMove(boolean firstMove) {
    	this.firstMove = firstMove;
    }

    // In the legal moves, eliminate all the moves that makes the king get checked
    protected void removeMovesPutsKingInCheck(Board board, List<Move> movesList) {
        for(int i = 0; i < movesList.size(); i++) {
            if(board.movePutsKingInCheck(movesList.get(i), this.white)) {
                movesList.remove(movesList.get(i));
                i--;
            }
        }
    }

    // Move the piece to "destination"
    public void moveTo(Point destination) {
        this.firstMove = false;
        this.piecePosition = destination;
    }
    // Calculate all legal moves, and return a list contains these moves
    public abstract List<Move> calculateLegalMoves(Board board, boolean checkKing);
    public abstract String toString();
    public abstract String getImageName();
    @Override
    protected abstract Piece clone();
}

