package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.List;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

public class Knight extends Piece {

	private final static int[] CANDIDATE_MOVE_COORDINATES = {-17,-15,-10,-6,6,10,15,17};
	
	public Knight(final int piecePostion, final Alliance pieceAllience) {
		super(PieceType.KNIGHT,piecePostion, pieceAllience);
	}

	@Override
	public List<Move> calculateLegalMoves(final Board board) {
	
		int candidateDestinationCoordinate;
		final List<Move> legalMoves = new ArrayList<>();
		
		for (final int currentCandidate : CANDIDATE_MOVE_COORDINATES) {
			candidateDestinationCoordinate = this.piecePostion + currentCandidate;
			
			if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
			  
				if (isFirstColumnExclusion(this.piecePostion,currentCandidate)) {
					continue;
				}

				if (isSecondColumnExclusion(this.piecePostion,currentCandidate)) {
					continue;
				}
				
				if (isSeventhColumnExclusion(this.piecePostion,currentCandidate)) {
					continue;
				}
				
				if (isEightColumnExclusion(this.piecePostion,currentCandidate)) {
					continue;
				}
				
			  final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
			  
			  if (!candidateDestinationTile.isTileOccupied()) {
				  legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
			  }
			  else {
				  final Piece pieceAtDestination =  candidateDestinationTile.getPiece();
				  final Alliance pieceDestinateAlliance = pieceAtDestination.getPieceAlliance();
				  
				  if (this.pieceAllience != pieceDestinateAlliance) {
					  legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
				  }
			  }
			}
		}
		
		return ImmutableList.copyOf(legalMoves);
	}

	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -17 || candidateOffset == -10 || candidateOffset == 6  || candidateOffset == 15);
	}
	
	private static boolean isSecondColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.SECOND_COLUMN[currentPosition] && (candidateOffset == -10 || candidateOffset == 6);
	}

	private static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.SEVENTH_COLUMN[currentPosition] && (candidateOffset == -6 || candidateOffset == 10);
	}
	
	private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -15 || candidateOffset == -6 || candidateOffset == 10  || candidateOffset == 17);
	}
	
	@Override
	public String toString() {
		return PieceType.KNIGHT.toString();
	}

	@Override
	public Knight movePiece(Move move) {
		Knight knight = new Knight(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
		knight.isFirstMove=false;
		return knight;
	}
	
}
