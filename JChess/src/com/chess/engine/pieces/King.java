package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.List;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.AttackMove;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

public class King extends Piece {

	private final static int[] CANDIDATE_MOVE_COORDINATES = { -9,-8,-7,-1,1,7,8,9 };	
	
	private final boolean isCastled;
	
	public King(final int piecePostion, final Alliance pieceAllience) {
		super(PieceType.KING,piecePostion, pieceAllience);
		this.isCastled=false;
	}
	
	public King(final int piecePostion, final Alliance pieceAllience,boolean isCastled) {
		super(PieceType.KING,piecePostion, pieceAllience);
		this.isCastled=isCastled;
	}
	
	public boolean isCastled() {
		return isCastled;
	}

	@Override
	public List<Move> calculateLegalMoves(Board board) {
	
		final List<Move> legalMoves = new ArrayList<>();

		for (final int candidateCoordinateOffset : CANDIDATE_MOVE_COORDINATES) {
			    final int candidateDestinationCoordinate = this.piecePostion + candidateCoordinateOffset;
				if (isFirstColumnExclusion(this.piecePostion, candidateCoordinateOffset) ||
						isEightColumnExclusion(this.piecePostion, candidateCoordinateOffset)	) {
					continue;
				}
			
				if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
					  final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
					  
					  if (!candidateDestinationTile.isTileOccupied()) {
						  legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
					  }
					  else {
						  final Piece pieceAtDestination =  candidateDestinationTile.getPiece();
						  final Alliance pieceDestinateAlliance = pieceAtDestination.getPieceAlliance();
						  
						  if (this.pieceAllience != pieceDestinateAlliance) {
							  legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
						  }
					  }
					}
		}		
		
		return ImmutableList.copyOf(legalMoves);	
	}

	
	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == 7 || candidateOffset == -9 ||candidateOffset == -1);
	}

	private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -7 || candidateOffset == 1 ||candidateOffset == 9);
	}
	
	@Override
	public String toString() {
		return PieceType.KING.toString();
	}
	
	@Override
	public King movePiece(Move move) {
		King king = new King(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance(), move.isCatlingMove());
		king.isFirstMove=false;
		return king;
	}
	
}
