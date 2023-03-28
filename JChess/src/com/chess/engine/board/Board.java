package com.chess.engine.board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chess.engine.board.Move.MoveFactory;
import com.chess.engine.pieces.Alliance;
import com.chess.engine.pieces.Bishop;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Knight;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Queen;
import com.chess.engine.pieces.Rook;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public class Board {

	private final List<Tile> gameBoard;
	private final Collection<Piece> whitePieces;
	private final Collection<Piece> blackPieces;
	
	private final WhitePlayer whitePlayer;
	private final BlackPlayer blackPlayer;
	private final Player currentPlayer;
	private final Pawn esPassantPawn;
	
	private Board(final Builder builder) {
		this.gameBoard=createGameBoard(builder);
		this.esPassantPawn=builder.esPassantPawn;
		
		this.whitePieces=calculateActivePieces(Alliance.WHITE);
		this.blackPieces=calculateActivePieces(Alliance.BLACK);
		
		final Collection<Move> whiteStandartLegalMoves = calculateLegalMoves(this.whitePieces);
		final Collection<Move> blackStandartLegalMoves = calculateLegalMoves(this.blackPieces);
		
		this.whitePlayer = new WhitePlayer(this,whiteStandartLegalMoves,blackStandartLegalMoves);
		this.blackPlayer = new BlackPlayer(this,whiteStandartLegalMoves,blackStandartLegalMoves);
			
		this.currentPlayer = calculateCurrentPlayer(builder);
	}
	
	private Player calculateCurrentPlayer(Builder builder) {
        
		return builder.nextMoveMaker == Alliance.WHITE ? whitePlayer : blackPlayer;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
			final String tileText = prettyPrint(this.gameBoard.get(i));
			sb.append(String.format("%3s", tileText));
			if ((i+1)%BoardUtils.NUM_TILES_PER_ROW == 0) {
				sb.append("\n");				
			}
		}
		
		/*
		sb.append("WHITE MOVES\n");
		for(Move move : this.whitePlayer.getLegalMoves()) {
            sb.append(move.getMovedPiece().toString() + "-" + move.getCurrentCoordinate() + "-" + move.getDestinationCoordinate()+"\n");
		}
		sb.append("BLACK MOVES\n");
		for(Move move : this.blackPlayer.getLegalMoves()) {
            sb.append(move.getMovedPiece().toString() + "-" + move.getCurrentCoordinate() + "-" + move.getDestinationCoordinate()+"\n");
		}
		*/

		
		return sb.toString();
		
	}

	private static String prettyPrint(Tile tile) {
		return tile.toString();
	}

	private Collection<Move> calculateLegalMoves(final Collection<Piece> pieces) {
		final List<Move> legalMoves= new ArrayList<Move>();
		for (final Piece piece : pieces) 
		{ 
			legalMoves.addAll(piece.calculateLegalMoves(this));
		}
		
		return ImmutableList.copyOf(legalMoves);
	}

	private Collection<Piece> calculateActivePieces(final Alliance allience) {
		final List<Piece> activePieces= new ArrayList<Piece>();
		for (Tile tile : gameBoard) 
		{ 
			if (tile.getPiece()!=null && tile.getPiece().getPieceAlliance()==allience) {
				activePieces.add(tile.getPiece());				
			}

		}
		return ImmutableList.copyOf(activePieces);
	}

	private static List<Tile> createGameBoard(final Builder builder) {
		final Tile[] tiles = new Tile[BoardUtils.NUM_TILES];

		for (int i = 0; i < tiles.length; i++) {
			tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
		}
		
		return ImmutableList.copyOf(tiles);
	}
	
	public Iterable<Move> getAllLegalMoves() {
		return Iterables.unmodifiableIterable(Iterables.concat(this.whitePlayer.getLegalMoves(),this.blackPlayer.getLegalMoves()));
	}
	
	public static Board createStandardBoard() {
		
		Builder builder = new Builder();
		//Black
		builder.setPiece(new Rook(0,Alliance.BLACK));
		builder.setPiece(new Knight(1,Alliance.BLACK));
		builder.setPiece(new Bishop(2,Alliance.BLACK));
		builder.setPiece(new Queen(3,Alliance.BLACK));
		builder.setPiece(new King(4,Alliance.BLACK));
		builder.setPiece(new Bishop(5,Alliance.BLACK));
		builder.setPiece(new Knight(6,Alliance.BLACK));
		builder.setPiece(new Rook(7,Alliance.BLACK));		
		builder.setPiece(new Pawn(8,Alliance.BLACK));
		builder.setPiece(new Pawn(9,Alliance.BLACK));
		builder.setPiece(new Pawn(10,Alliance.BLACK));
		builder.setPiece(new Pawn(11,Alliance.BLACK));
		builder.setPiece(new Pawn(12,Alliance.BLACK));
		builder.setPiece(new Pawn(13,Alliance.BLACK));
		builder.setPiece(new Pawn(14,Alliance.BLACK));
		builder.setPiece(new Pawn(15,Alliance.BLACK));
		
		//White
		builder.setPiece(new Pawn(48,Alliance.WHITE));
		builder.setPiece(new Pawn(49,Alliance.WHITE));
		builder.setPiece(new Pawn(50,Alliance.WHITE));
		builder.setPiece(new Pawn(51,Alliance.WHITE));
		builder.setPiece(new Pawn(52,Alliance.WHITE));
		builder.setPiece(new Pawn(53,Alliance.WHITE));
		builder.setPiece(new Pawn(54,Alliance.WHITE));
		builder.setPiece(new Pawn(55,Alliance.WHITE));		
		builder.setPiece(new Rook(56,Alliance.WHITE));
		builder.setPiece(new Knight(57,Alliance.WHITE));
		builder.setPiece(new Bishop(58,Alliance.WHITE));
		builder.setPiece(new Queen(59,Alliance.WHITE));
		builder.setPiece(new King(60,Alliance.WHITE));
		builder.setPiece(new Bishop(61,Alliance.WHITE));
		builder.setPiece(new Knight(62,Alliance.WHITE));
		builder.setPiece(new Rook(63,Alliance.WHITE));		
		builder.setMoveMaker(Alliance.WHITE);
		
		return builder.build();
	}

	public Tile getTile(int tileCoordinate) {
		return gameBoard.get(tileCoordinate);
	}

	public static class Builder {
		
		Map<Integer,Piece> boardConfig;
		Alliance nextMoveMaker;
		Pawn esPassantPawn;
		
		public Builder() {
			this.boardConfig = new HashMap<>();
		}
		
		public Builder setPiece(final Piece piece) {
			this.boardConfig.put(piece.getPiecePosition(),piece);
			return this;
		}
		
		public Builder setMoveMaker(final Alliance nextMoveMaker) {
			this.nextMoveMaker=nextMoveMaker;
			return this;
		}
		
		public Board build() {
			return new Board(this);
		}

		public void setEsPassantPawn(Pawn esPassantPawn) {
			this.esPassantPawn=esPassantPawn;
		}
	}

	public Collection<Piece> getBlackPieces() {
		return blackPieces;
	}

	public Collection<Piece> getWhitePieces() {
		return whitePieces;
	}

	public WhitePlayer getWhitePlayer() {
		return whitePlayer;
	}

	public BlackPlayer getBlackPlayer() {
		return blackPlayer;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	public Pawn getEsPassantPawn() {
		return this.esPassantPawn;
	}
}
