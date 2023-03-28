package com.chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.KingSideCastleMove;
import com.chess.engine.board.Move.QueenSideCastleMove;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Alliance;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;
import com.google.common.collect.ImmutableList;

public class WhitePlayer extends Player {

	public WhitePlayer(final Board board, 
			           final Collection<Move> whiteStandartLegalMoves,
			           final Collection<Move> blackStandartLegalMoves) {
		super(board, whiteStandartLegalMoves, blackStandartLegalMoves);
	}

	@Override
	public Collection<Piece> getActivePieces() {
		return this.board.getWhitePieces();
	}

	@Override
	public Alliance getAlliance() {
		return Alliance.WHITE;
	}

	@Override
	public Player getOpponent() {
		return board.getBlackPlayer();
	}

	@Override
	protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegalMoves,
			                                        final Collection<Move> opponentLegalMoves) {

		List<Move> kingCastles = new ArrayList<Move>();
		
		if (!this.isInCheck() && this.playerKing.isFirstMove()) {
			//white king side castle
			if (!this.board.getTile(BoardUtils.TILE_61).isTileOccupied() && !this.board.getTile(BoardUtils.TILE_62).isTileOccupied()) {
				final Tile rookTile = this.board.getTile(BoardUtils.TILE_63);
				if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove() && rookTile.getPiece().getPieceType().isRook()) {
					if (Player.calculateAttackOnTile(BoardUtils.TILE_61, opponentLegalMoves).isEmpty() && Player.calculateAttackOnTile(BoardUtils.TILE_62, opponentLegalMoves).isEmpty()) {
						KingSideCastleMove kingSideCastleMove = new KingSideCastleMove(this.board, this.playerKing, BoardUtils.TILE_62, (Rook)rookTile.getPiece(), BoardUtils.TILE_63, BoardUtils.TILE_61);
						kingCastles.add(kingSideCastleMove);
					}
				}
			}

			//white queen side castle
			if (!this.board.getTile(BoardUtils.TILE_57).isTileOccupied() && !this.board.getTile(BoardUtils.TILE_58).isTileOccupied() && !this.board.getTile(BoardUtils.TILE_59).isTileOccupied()) {
				final Tile rookTile = this.board.getTile(BoardUtils.TILE_56);
				if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove() && rookTile.getPiece().getPieceType().isRook()) {
					if (Player.calculateAttackOnTile(BoardUtils.TILE_57, opponentLegalMoves).isEmpty() && Player.calculateAttackOnTile(BoardUtils.TILE_58, opponentLegalMoves).isEmpty() && Player.calculateAttackOnTile(BoardUtils.TILE_59, opponentLegalMoves).isEmpty()) {
						QueenSideCastleMove queenSideCastleMove = new QueenSideCastleMove(this.board, this.playerKing, BoardUtils.TILE_58, (Rook)rookTile.getPiece(), BoardUtils.TILE_56, BoardUtils.TILE_59);
						kingCastles.add(queenSideCastleMove);					}
				    }
			}
		}
		
		return ImmutableList.copyOf(kingCastles);
	}

	@Override
	protected Collection<Move> calculateEsPassantPawnMoves() {
		List<Move> esPassantPawnMoves = new ArrayList<Move>();
		Pawn esPassantPawn = this.board.getEsPassantPawn();
        if (esPassantPawn==null || esPassantPawn.getPieceAlliance().isWhite()) return esPassantPawnMoves;
        
        for (Piece piece : this.getActivePieces()) {
			if (piece.getPieceType().isPawn()) {
				if (esPassantPawn.getPiecePosition() == piece.getPiecePosition()-1) {
					esPassantPawnMoves.add(new Move.PawnEsPassantAttackMove(board, piece, (piece.getPiecePosition()-9), esPassantPawn));
				}
				if (esPassantPawn.getPiecePosition() == piece.getPiecePosition()+1) {
					esPassantPawnMoves.add(new Move.PawnEsPassantAttackMove(board, piece, (piece.getPiecePosition()-7), esPassantPawn));
				}
			}
		}
        
		return esPassantPawnMoves;
	}

}
