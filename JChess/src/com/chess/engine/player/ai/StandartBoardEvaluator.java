package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.Player;

public final class StandartBoardEvaluator implements BoardEvaluator {

	private static final int CHECK_BONUS = 1;
	private static final int CHECK_MATE_BONUS = 10000;
	private static final int DEPTH_BONUS = 10;
	private static final int CASTLED_BONUS = 60;
	private static final int MAX_DEPTH = 10;

	@Override
	public int evaluate(final Board board, final int depth) {
		return scorePlayer(board, board.getWhitePlayer(),depth)-
			   scorePlayer(board, board.getBlackPlayer(),depth);
	}

	private int scorePlayer(final Board board, final Player player, final int depth) {
		return pieceValue(player) + 
			   mobility(player) + 
			   check(player) + 
			   checkMate(player,depth) + 
			   castled(player);
	}

	private static int castled(Player player) {
		return player.isCastled() ? CASTLED_BONUS : 0 ;
	}

	private int checkMate(Player player, final int depth) {
		return player.getOpponent().isInCheckMate() ? CHECK_MATE_BONUS * depthBonus(depth) : 0;
	}

	private static int depthBonus(int depth) {
		return (MAX_DEPTH-depth) * DEPTH_BONUS;
	}

	private int check(Player player) {
		return player.getOpponent().isInCheck() ? CHECK_BONUS : 0;
	}

	private int mobility(Player player) {
		return player.getLegalMoves().size();
	}

	private int pieceValue(Player player) {
		
		int pieceValueScore = 0;
		for (final Piece piece : player.getActivePieces()) {
			pieceValueScore += piece.getPieceValue();
		}
		
		return pieceValueScore;
	}

}
