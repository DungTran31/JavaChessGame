package model;

import java.awt.Point;

//Class's purpose is only contain the info of the move
public class Move {
	private Piece movedPiece; // The piece makes the move
	private Point destination; // The destination of the move
    private Piece capturedPiece; // The piece be captured
    public Move(Piece movedPiece, Point destination, Piece capturedPiece) {
        this.movedPiece = movedPiece;
        this.destination = destination;
        this.capturedPiece = capturedPiece;
    }

    // Check if the "movedPiece" is null
    public boolean isMovedPieceNull() {
        return this.movedPiece == null;
    }

    public Piece getMovedPiece() {
        return movedPiece;
    }


    public Point getMoveTo() {
        return destination;
    }

    public Piece getCapturedPiece() {
    	return capturedPiece;
    }

    public void setCapturedPiece(Piece capturedPiece) {
        this.capturedPiece = capturedPiece;
    }

    public void setMovedPiece(Piece movedPiece) {
        this.movedPiece = movedPiece;
    }

    public void setMoveTo(Point destionation) {
        this.destination = destionation;
    }

    @Override
    public String toString() {
        return "[movedPiece=" + movedPiece + ", capturedPiece=" + capturedPiece + ", moveTo=" + destination + "]";
    }
}
