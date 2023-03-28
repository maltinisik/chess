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

public class Queen extends Piece {

	private final static int[] CANDIDATE_MOVE_COORDINATES = { -9,-8,-7,-1,1,7,8,9 };	
	
	public Queen(int piecePostion, Alliance pieceAllience) {
		super(PieceType.QUEEN,piecePostion, pieceAllience);
	}	
	
	@Override
	public List<Move> calculateLegalMoves(Board board) {
	
		final List<Move> legalMoves = new ArrayList<>();

		for (final int candidateCoordinateOffset : CANDIDATE_MOVE_COORDINATES) {
			int candidateDestinationCoordinate = this.piecePostion;
			while(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
				if (isFirstColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset) ||
						isEightColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)) {
					break;
				}
				candidateDestinationCoordinate += candidateCoordinateOffset;
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
						  break;
					  }
					}
			}
		}		
		
		return ImmutableList.copyOf(legalMoves);	
	}
	
	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 || candidateOffset == 7 || candidateOffset == -1);
	}

	private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -7 || candidateOffset == 9 || candidateOffset == 1);
	}	
	
	@Override
	public String toString() {
		return PieceType.QUEEN.toString();
	}
	
	@Override
	public Queen movePiece(Move move) {
		Queen queen = new Queen(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
		queen.isFirstMove=false;
		return queen;
	}
	
}
