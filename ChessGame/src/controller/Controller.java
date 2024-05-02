package controller;

import static javax.swing.SwingUtilities.isLeftMouseButton;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import constant.GameStatus;
import model.Ai;
import model.Board;
import model.Data;
import model.Move;
import model.Piece;
import view.MenuBar;
import view.View;
import view.BoardPanel.TilePanel;

public class Controller {
    @SuppressWarnings("unused")
	private GameStatus status;
    private Board gameBoard;
    private View chessView;
    private MenuBar chessMenuBar;
    private Data data;
    
    private Piece selectedPiece = null;
    private Piece invalidSelectedPiece = null;
    private Piece lastmovedPiece = null;
    private List<Move> validMoves;

    // inititalizes game
    public Controller() {
        this.init();
    }

    public void start() {
        this.chessView.getBoardPanel().drawBoard();
        this.chessView.setVisible(true);
    }


    public Board getGameBoard() {
        return this.gameBoard;
    }

    public Piece getSelectedPiece() {
        return this.selectedPiece;
    }

    public Piece getInvalidSelectedPiece() {
        return invalidSelectedPiece;
    }

    public void setInvalidSelectedPiece(Piece invalidSelectedPiece) {
        this.invalidSelectedPiece = invalidSelectedPiece;
    }

    public Piece getLastmovedPiece() {
        return lastmovedPiece;
    }

    public void setLastmovedPiece(Piece lastmovedPiece) {
        this.lastmovedPiece = lastmovedPiece;
    }

    private void init() {
    	this.status = GameStatus.STARTED;
        this.gameBoard = new Board(true);
        this.chessView = new View(this);
        this.chessMenuBar = new MenuBar();
        this.chessView.setJMenuBar(this.chessMenuBar);
        this.data = Data.getInstance();
        this.selectedPiece = null;
        this.addTilesMouseListener(this.chessView.getBoardPanel().getBoardTiles());
        this.addMenuActionListener();
    }
    
    public void addTilesMouseListener(ArrayList<TilePanel> tilePanels) {
        // For dynamic GUI and basic piece movements
        for (TilePanel tilePanel : tilePanels) {
            tilePanel.addMouseListener(new MouseListener() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (isLeftMouseButton(e)) {
                        responseToPlayerAction(tilePanel);
                    }
                }

                @Override
                public void mouseEntered(MouseEvent arg0) {
                }

                @Override
                public void mouseExited(MouseEvent arg0) {
                }

                @Override
                public void mouseClicked(MouseEvent arg0) {
                }

                @Override
                public void mouseReleased(MouseEvent arg0) {
                }
            });
        }
    }

    private void responseToPlayerAction(TilePanel tilePanel) {
        if (this.gameBoard.getAi() == null || this.gameBoard.getAi().isWhite() != this.gameBoard.isWhiteTurn()) {
            this.lastmovedPiece = null;
            if (this.selectedPiece == null) {
                // select the piece that was clicked
                this.selectedPiece = this.gameBoard.getPieceAt(tilePanel.getPosition());
                if (selectedPiece != null) {
                    // get the available moves for the piece
                    this.validMoves = this.selectedPiece.calculateLegalMoves(this.gameBoard, true);

                    // if the piece is of the wrong color, mark as invalid
                    if (this.selectedPiece.isWhite() != this.gameBoard.isWhiteTurn()) {
                        this.invalidSelectedPiece = this.selectedPiece;
                        this.validMoves = null;
                        this.selectedPiece = null;
                    } else {
                        this.invalidSelectedPiece = null;
                    }
                }
            } else {
                Move playerMove = this.moveWithDestination(tilePanel.getPosition());
                if (playerMove != null) {
                    // Move successfully, then cancel selection and switch
                    // player
                    this.gameBoard.makeMove(playerMove, true);
                    this.selectedPiece = null;
                    this.lastmovedPiece = playerMove.getMovedPiece();
                    this.chessView.getBoardPanel().drawBoard();
                } else {
                    // Move unsuccessfully, then use new selection if new
                    // selection is same player,
                    // otherwise keep the current selection
                    Piece pieceOnTile = this.gameBoard.getPieceAt(tilePanel.getPosition());
                    if (pieceOnTile != null) {
                        if (pieceOnTile.isWhite() == this.gameBoard.isWhiteTurn()) {
                            this.selectedPiece = pieceOnTile;
                            this.validMoves = this.selectedPiece.calculateLegalMoves(this.gameBoard, true);
                        } else {
                            this.selectedPiece = null;
                            this.validMoves = null;
                            this.invalidSelectedPiece = pieceOnTile;
                        }
                    } else {
                        this.selectedPiece = null;
                        this.validMoves = null;
                    }
                }
            }
        }

        if (this.gameBoard.getAi() != null && this.gameBoard.getAi().isWhite() == this.gameBoard.isWhiteTurn()) {
            this.lastmovedPiece = null;
            Move computerMove = this.gameBoard.getAi().getMove(gameBoard);
            if (computerMove != null) {
                this.gameBoard.makeMove(computerMove, false);
                this.lastmovedPiece = computerMove.getMovedPiece();
            }
        }
        if (this.gameBoard.EndGame()) {
            // repaint board immediately, before JOptionPane is shown.
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    chessView.getBoardPanel().drawBoard();
                }
            });

            // if a king not currently in check, stalemate
            if (gameBoard.getInCheck() == null) {
                this.status = GameStatus.STALEMATE;
                JOptionPane.showMessageDialog(this.chessView.getBoardPanel(), "STALEMATE!", "",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                // if a king is in check, checkmate
                this.status = GameStatus.CHECKMATE;
                JOptionPane.showMessageDialog(this.chessView.getBoardPanel(), "CHECKMATE!", "",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                chessView.getBoardPanel().drawBoard();
            }
        });
    }

    private Move moveWithDestination(Point point) {
        for (Move move : this.validMoves) {
            if (move.getMoveTo().equals(point)) {
                return move;
            }
        }
        return null;
    }
    
    private void addMenuActionListener() {
    	this.chessMenuBar.getjMenuItem_Save().addActionListener(new ActionListener() {
    		@Override
    		public void actionPerformed(ActionEvent e) {
    			save();
    		}
    		
    	});
    	this.chessMenuBar.getjMenuItem_Load().addActionListener(new ActionListener() {
    		@Override
    		public void actionPerformed(ActionEvent e) {
    			load();
    		}
    	});
    	this.chessMenuBar.getjMenuItem_New1P().addActionListener(new ActionListener() {
    		@Override
    		public void actionPerformed(ActionEvent e) {
    			newAIGame();
    		}
    	});
        this.chessMenuBar.getjMenuItem_New2P().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newGame();
            }
        });
        this.chessMenuBar.getjMenuItem_Undo().addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		undo();
        	}
        });
        this.chessMenuBar.getjMenuItem_Close().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chessView.dispose();
            }
        });
    }

    private void save() {
    	// asks the user for a save name
    	String name = JOptionPane.showInputDialog(this.chessView.getBoardPanel(), "Save file name: ");
    	
    	// if no name given or prompt exited, leave method
    	if (name == null) {
    		return;
    	}
    	try {
    		data.saveBoard(name, this.gameBoard);
    	} catch (Exception e) {
    		String message = "Could not save game,";
    		// inform the user, give exception details
    		JOptionPane.showMessageDialog(this.chessView.getBoardPanel(), message, "Error!", JOptionPane.ERROR_MESSAGE);
    	}
    }
    
    private void load() {
    	File[] allSavedFile = data.getAllSavedFile();
    	if (allSavedFile == null || allSavedFile.length == 0) {
            JOptionPane.showMessageDialog(
                this.chessView.getBoardPanel(),
                "No saved games found",
                "Load game",
                JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            String[] fileNames = new String[allSavedFile.length];
            for (int i = 0; i < allSavedFile.length; i++) {
                fileNames[i] = allSavedFile[i].getName();
            }

            Object selectedFileName = JOptionPane.showInputDialog(
                this.chessView.getBoardPanel(),
                "Select save file:",
                "Load Game",
                JOptionPane.QUESTION_MESSAGE,
                null,
                fileNames,
                fileNames[0]
            );

            if (selectedFileName != null) {
                File selectedFile = new File("D:/Code/CNTT_VA_2/Nam2/Ki2/Java/saveGame/" + selectedFileName);
                if (data.loadBoard(selectedFile) != null) {
                    gameBoard = data.loadBoard(selectedFile);
                }
            }
        }

        // Check if the game has ended, adjust the status
        if (gameBoard.EndGame()) {
            status = (gameBoard.getInCheck() == null) ? GameStatus.STALEMATE : GameStatus.CHECKMATE;
        }

   	
    	// repaint to show changes
    	SwingUtilities.invokeLater(new Runnable() {
    		@Override
    		public void run() {
    			chessView.getBoardPanel().drawBoard();
    		}
    	});
    }
    
    private void newGame() {
        this.gameBoard = new Board(true);
        this.status = GameStatus.STARTED;
        this.selectedPiece = null;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                chessView.getBoardPanel().drawBoard();
            }
        });
    }

    private void newAIGame() {
        int aiDepth;
        boolean whiteAI;

        // creates a JOptionPane to ask the user for the difficulty of the ai
        Object level = JOptionPane.showInputDialog(this.chessView.getBoardPanel(), "Select AI level:", "New AI game",
                JOptionPane.QUESTION_MESSAGE, null, new Object[] { "Easy", "Normal", "Hard" }, "Easy");

        // interprets JOptionPane result
        if (level == null) {
            return;
        } else {
            if (level.toString().equals("Easy"))
                aiDepth = 1;
            else if (level.toString().equals("Normal"))
                aiDepth = 2;
            else
                aiDepth = 3;
        }

        // creates a JOptionPane to ask the user for the ai color
        Object color = JOptionPane.showInputDialog(this.chessView.getBoardPanel(), "Select AI Color:", "New AI game",
                JOptionPane.QUESTION_MESSAGE, null, new Object[] { "Black", "White" }, "Black");

        // interpretes JOptionPane result
        if (color == null) {
            return;
        } else {
            if (color.toString().equals("White")) {
                whiteAI = true;
            } else {
                whiteAI = false;
            }
        }

        // creates a new game
        this.newGame();
        // then sets the ai for the board
        this.gameBoard.setAi(new Ai(whiteAI, aiDepth));
        // Make ai move a piece if in white side then redraw the board
        if(this.gameBoard.getAi().isWhite()) {
            Move computerMove = this.gameBoard.getAi().getMove(this.gameBoard);
            this.gameBoard.makeMove(computerMove, false);
            this.lastmovedPiece = computerMove.getMovedPiece();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    chessView.getBoardPanel().drawBoard();
                }
            });
        }
    }
    private void undo() {
        this.selectedPiece = null;
        this.validMoves = null;
        // if a two player game
        Board testBoard;
        if (this.gameBoard.getAi() == null) {
            // skip back one move
            testBoard = this.gameBoard.getPreviousState();
        } else {
            // skip back to the last move by the player
            if (this.gameBoard.isWhiteTurn() != this.gameBoard.getAi().isWhite()) {
                testBoard = this.gameBoard.getPreviousState().getPreviousState();
            } else {
                testBoard = this.gameBoard.getPreviousState();
            }
        }

        if (testBoard != null) {
            this.gameBoard = testBoard;
        } else {
            JOptionPane.showMessageDialog(this.chessView.getBoardPanel(), "Undo failed!", "",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        // set the game status to started
        status = GameStatus.STARTED;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                chessView.getBoardPanel().drawBoard();
            }
        });
    }
}