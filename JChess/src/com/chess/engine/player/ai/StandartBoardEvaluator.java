package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.player.Player;
import com.google.common.annotations.VisibleForTesting;

public final class StandartBoardEvaluator
        implements BoardEvaluator {

    private final static int CHECK_MATE_BONUS = 10000;
    private final static int CHECK_BONUS = 45;
    private final static int CASTLE_BONUS = 25;
    private final static int MOBILITY_MULTIPLIER = 5;
    private final static int ATTACK_MULTIPLIER = 1;
    private final static int TWO_BISHOPS_BONUS = 25;
    private static final StandartBoardEvaluator INSTANCE = new StandartBoardEvaluator();

    private StandartBoardEvaluator() {
    }

    public static StandartBoardEvaluator get() {
        return INSTANCE;
    }

    @Override
    public int evaluate(final Board board,
                        final int depth) {
        return score(board.getWhitePlayer(), depth) - score(board.getBlackPlayer(), depth);
    }

    public String evaluationDetails(final Board board, final int depth) {
        return
               ("White Mobility : " + mobility(board.getWhitePlayer()) + "\n") +
                "White kingThreats : " + kingThreats(board.getWhitePlayer(), depth) + "\n" +
                "White castle : " + castle(board.getWhitePlayer()) + "\n" +
                "---------------------\n" +
                "Black Mobility : " + mobility(board.getBlackPlayer()) + "\n" +
                "Black kingThreats : " + kingThreats(board.getBlackPlayer(), depth) + "\n" +
                "Black castle : " + castle(board.getBlackPlayer()) + "\n" +
                "Final Score = " + evaluate(board, depth);
    }

    @VisibleForTesting
    private static int score(final Player player,
                             final int depth) {
        return mobility(player) +
               kingThreats(player, depth) +
               castle(player);
    }

    private static int mobility(final Player player) {
        return MOBILITY_MULTIPLIER * mobilityRatio(player);
    }

    private static int mobilityRatio(final Player player) {
        return (int)((player.getLegalMoves().size() * 10.0f) / player.getOpponent().getLegalMoves().size());
    }

    private static int kingThreats(final Player player,
                                   final int depth) {
        return player.getOpponent().isInCheckMate() ? CHECK_MATE_BONUS  * depthBonus(depth) : check(player);
    }

    private static int check(final Player player) {
        return player.getOpponent().isInCheck() ? CHECK_BONUS : 0;
    }

    private static int depthBonus(final int depth) {
        return depth == 0 ? 1 : 100 * depth;
    }

    private static int castle(final Player player) {
        return player.isCastled() ? CASTLE_BONUS : 0;
    }

}