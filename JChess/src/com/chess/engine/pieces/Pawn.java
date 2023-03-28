package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.List;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.PawnJump;
import com.chess.engine.board.Move.PawnMove;
import com.chess.engine.board.Move.PawnPromotion;
import com.google.common.collect.ImmutableList;

public class Pawn extends Piece {

	private final static int[] CANDIDATE_MOVE_COORDINATES = {8,16,7,9};
	
	public Pawn(int piecePostion, Alliance pieceAllience) {
		super(PieceType.PAWN,piecePostion, pieceAllience);
	}

	@Override
	public List<Move> calculateLegalMoves(Board board) {
		
		final List<Move> legalMoves = new ArrayList<>();

		for (final int candidateCoordinateOffset : CANDIDATE_MOVE_COORDINATES) {
			int candidateDestinationCoordinate = this.piecePostion + (candidateCoordinateOffset* this.pieceAllience.getDirection());

			if (!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
				continue;
			}
			
			if (candidateCoordinateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied() ) {
				if (this.getPieceAlliance().isPawnPromotionSquare(candidateDestinationCoordinate)) {
					legalMoves.add(new PawnPromotion(new PawnMove(board, this, candidateDestinationCoordinate)));
				} else {
					legalMoves.add(new PawnMove(board, this, candidateDestinationCoordinate));
				}
			}
			
			else if (candidateCoordinateOffset == 16 && this.isFirstMove() &&  
					( (this.pieceAllience.isBlack() && BoardUtils.SECOND_ROW[this.piecePostion]) || 
					  (this.pieceAllience.isWhite() && BoardUtils.SEVENTH_ROW[this.piecePostion]))
					) 
			{
				final int behindCandidateDestinationCoordinate = this.piecePostion + (this.pieceAllience.getDirection()*8);
				
				if (!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() && 
					!board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
					legalMoves.add(new PawnJump(board, this, candidateDestinationCoordinate));
				}
			}
			else if(candidateCoordinateOffset == 7) {
				if ((this.pieceAllience.isBlack() && BoardUtils.FIRST_COLUMN[this.piecePostion]) || 
				     (this.pieceAllience.isWhite() && BoardUtils.EIGHTH_COLUMN[this.piecePostion])
					) {continue;} 
				if (board.getTile(candidateDestinationCoordinate).isTileOccupied() && board.getTile(candidateDestinationCoordinate).getPiece().getPieceAlliance()!= this.pieceAllience) {
					if (this.getPieceAlliance().isPawnPromotionSquare(candidateDestinationCoordinate)) {
						legalMoves.add(new PawnPromotion(new PawnMove(board, this, candidateDestinationCoordinate)));
					} else {
					    legalMoves.add(new Move.PawnAttackMove(board, this, candidateDestinationCoordinate, board.getTile(candidateDestinationCoordinate).getPiece()));					
					}
				}
			}
			else if(candidateCoordinateOffset == 9) {
				if ((this.pieceAllience.isBlack() && BoardUtils.EIGHTH_COLUMN[this.piecePostion]) || 
					     (this.pieceAllience.isWhite() && BoardUtils.FIRST_COLUMN[this.piecePostion])
						) {continue;} 
				if (board.getTile(candidateDestinationCoordinate).isTileOccupied() && board.getTile(candidateDestinationCoordinate).getPiece().getPieceAlliance()!= this.pieceAllience) {
					if (this.getPieceAlliance().isPawnPromotionSquare(candidateDestinationCoordinate)) {
						legalMoves.add(new PawnPromotion(new PawnMove(board, this, candidateDestinationCoordinate)));
					} else {
					    legalMoves.add(new Move.PawnAttackMove(board, this, candidateDestinationCoordinate, board.getTile(candidateDestinationCoordinate).getPiece()));					
					}
				}
			}
		}
		
		return ImmutableList.copyOf(legalMoves);	
	}

	@Override
	public String toString() {
		return PieceType.PAWN.toString();
	}
	
	@Override
	public Pawn movePiece(Move move) {
		Pawn pawn = new Pawn(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
		pawn.isFirstMove=false;
		return pawn;
	}

	public Queen getPromotionPiece() {
		 return new Queen(this.piecePostion, this.pieceAllience);
	}
	
}