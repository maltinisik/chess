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

public class BlackPlayer extends Player {

	public BlackPlayer(final Board board, 
			           final Collection<Move> whiteStandartLegalMoves,
			           final Collection<Move> blackStandartLegalMoves) {
		super(board, blackStandartLegalMoves, whiteStandartLegalMoves);
	}

	@Override
	public Collection<Piece> getActivePieces() {
		return this.board.getBlackPieces();
	}

	@Override
	public Alliance getAlliance() {
		return Alliance.BLACK;
	}

	@Override
	public Player getOpponent() {
		return board.getWhitePlayer();
	}

	@Override
	protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegalMoves,
			                                        final Collection<Move> opponentLegalMoves) {

		List<Move> kingCastles = new ArrayList<Move>();
		
		if (!this.isInCheck() && this.playerKing.isFirstMove()) {
			//black king side castle
			if (!this.board.getTile(BoardUtils.TILE_5).isTileOccupied() && !this.board.getTile(BoardUtils.TILE_6).isTileOccupied()) {
				final Tile rookTile = this.board.getTile(BoardUtils.TILE_7);
				if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove() && rookTile.getPiece().getPieceType().isRook()) {
					if (Player.calculateAttackOnTile(BoardUtils.TILE_5, opponentLegalMoves).isEmpty() && Player.calculateAttackOnTile(BoardUtils.TILE_6, opponentLegalMoves).isEmpty()) {
						KingSideCastleMove kingSideCastleMove = new KingSideCastleMove(this.board, this.playerKing, BoardUtils.TILE_6, (Rook)rookTile.getPiece(), BoardUtils.TILE_7, BoardUtils.TILE_5);
						kingCastles.add(kingSideCastleMove);										
					}
				}
			}

			//black queen side castle
			if (!this.board.getTile(BoardUtils.TILE_1).isTileOccupied() && !this.board.getTile(BoardUtils.TILE_2).isTileOccupied() && !this.board.getTile(BoardUtils.TILE_3).isTileOccupied()) {
				final Tile rookTile = this.board.getTile(BoardUtils.TILE_0);
				if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove() && rookTile.getPiece().getPieceType().isRook()) {
					if (Player.calculateAttackOnTile(BoardUtils.TILE_1, opponentLegalMoves).isEmpty() && Player.calculateAttackOnTile(BoardUtils.TILE_2, opponentLegalMoves).isEmpty() && Player.calculateAttackOnTile(BoardUtils.TILE_3, opponentLegalMoves).isEmpty()) {
						QueenSideCastleMove queenSideCastleMove = new QueenSideCastleMove(this.board, this.playerKing, BoardUtils.TILE_2, (Rook)rookTile.getPiece(), BoardUtils.TILE_0, BoardUtils.TILE_3);
						kingCastles.add(queenSideCastleMove);
					}
				}
			}
		}
		
		return ImmutableList.copyOf(kingCastles);
	}

	@Override
	protected Collection<Move> calculateEsPassantPawnMoves() {
		List<Move> esPassantPawnMoves = new ArrayList<Move>();
		Pawn esPassantPawn = this.board.getEsPassantPawn();
        if (esPassantPawn==null || esPassantPawn.getPieceAlliance().isBlack()) return esPassantPawnMoves;
        
        for (Piece piece : this.getActivePieces()) {
			if (piece.getPieceType().isPawn()) {
				if (esPassantPawn.getPiecePosition() == piece.getPiecePosition()-1) {
					esPassantPawnMoves.add(new Move.PawnEsPassantAttackMove(board, piece, (piece.getPiecePosition()+7), esPassantPawn));
				}
				if (esPassantPawn.getPiecePosition() == piece.getPiecePosition()+1) {
					esPassantPawnMoves.add(new Move.PawnEsPassantAttackMove(board, piece, (piece.getPiecePosition()+9), esPassantPawn));
				}
			}
		}
        
		return esPassantPawnMoves;
	}
}
