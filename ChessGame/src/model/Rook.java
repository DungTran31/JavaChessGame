package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {

    //Contructor
    public Rook(Point piecePosition, boolean white) {
        this.piecePosition = piecePosition;
        this.white = white;
        this.firstMove = true;
    }

    //Constructor for clone method
    private Rook(Point piecePosition, boolean white, boolean firstMove) {
        this.piecePosition = piecePosition;
        this.white = white;
        this.firstMove = firstMove;
    }

    public boolean isFirstMove() {
        return firstMove;
    }

    public void setFirstMove(boolean firstMove) {
        this.firstMove = firstMove;
    }

    @Override
    public List<Move> calculateLegalMoves(Board board, boolean checkKing) {
        List<Move> moves = new ArrayList<Move>();
        int x = piecePosition.x;
        int y = piecePosition.y;
        // if no board given, return empty list
        if (board == null) {
        	return moves;        	
        }

        int[] dx = { 1, 0, -1, 0 };
        int[] dy = { 0, 1, 0, -1 };

        for (int i = 0; i < 4; i++) {
            Point pos = new Point(x + dx[i], y + dy[i]);
            Piece oppositePiece;

            while(board.ValidPos(pos)) {
            	oppositePiece = board.getPieceAt(pos);
                if(oppositePiece == null) {
                	moves.add(new Move(this, pos, oppositePiece));                	
                } else if(oppositePiece.white != this.white) {
                    moves.add(new Move(this, pos, oppositePiece));
                    break;
                } else {
                	break;                	
                }

                pos = new Point(pos.x + dx[i], pos.y + dy[i]);
            }
        }


        // check that move doesn't put own king in check
        if(checkKing) {
            this.removeMovesPutsKingInCheck(board , moves);
        }

        return moves;
    }
    
    @Override
    public String toString() {
        return "R";
    }

    @Override
    public Rook clone() {
        return new Rook(this.piecePosition, this.white, this.firstMove);
    }

    @Override
    public String getImageName() {
        return "Rook";
    }
}
