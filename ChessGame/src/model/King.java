package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class King extends Piece {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5582056274795996088L;

	public King(Point piecePosition, boolean white) {
        this.piecePosition = piecePosition;
        this.white = white;
        this.firstMove = true;
    }

    // Constructor for clone method
    private King(Point piecePosition, boolean white, boolean firstMove) {
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

        int[] dx = { -1, 0, 1, 1, 1, 0, -1, -1 };
        int[] dy = { -1, -1, -1, 0, 1, 1, 1, 0 };

        // add moves around the king if they are valid
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

        // "CastleMove"
        if (this.firstMove) {
            // if " " and this king is not the king in checked
            if (checkKing && this != board.getInCheck()) {
                List<Piece> pieces = board.getAllPieces();
                List<Piece> candidateRooks = new ArrayList<Piece>();

                // finds all "Rook" available for castling
                for (int i = 0; i < pieces.size(); i++) {
                    //(condition to be satisfied: same color and this is the first move)
                    if (pieces.get(i) instanceof Rook && 
                		pieces.get(i).white == this.white && 
                		pieces.get(i).firstMove) {
                    	candidateRooks.add(pieces.get(i));
                    }
                }

                // for each eligible rook
                for (Piece p : candidateRooks) {
                	boolean canCastle = true;

                    // Check for pieces between the king and the rook
                    int direction = (p.getPiecePosition().x > this.piecePosition.x) ? 1 : -1;
                    int startCol = this.piecePosition.x + direction;
                    int endCol = p.getPiecePosition().x;

                    for (int ix = startCol; ix != endCol; ix += direction) {
                        if (board.getPieceAt(new Point(ix, y)) != null) {
                            // Castling is illegal
                            canCastle = false;
                            break;
                        }
                    }

                    if (canCastle) {
                        int newKingX = (direction == 1) ? x + 2 : x - 2;
                        int newRookX = (direction == 1) ? x + 1 : x - 1; // so với vị trí của King trước khi di chuyển
                        moves.add(new CastleMove(this, new Point(newKingX, y), p, new Point(newRookX, y)));
                    }
                }
            }
        }

        // Remove moves that making own king in check
        if (checkKing) {
            this.removeMovesPutsKingInCheck(board, moves);
        }
        return moves;
    }

    @Override
    public String toString() {
        return "K";
    }

    @Override
    public King clone() {
        return new King(this.piecePosition, this.white, this.firstMove);
    }

    @Override
    public String getImageName() {
        return "King";
    }

}
