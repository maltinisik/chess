package com.tests.chess.engine.board;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.chess.engine.board.Board;

public class TestBoard {

	@Test
	public void intialBoard() {
		Board board = Board.createStandardBoard();
		assertEquals(board.getCurrentPlayer().getLegalMoves().size(), 20);
		assertEquals(board.getCurrentPlayer().getOpponent().getLegalMoves().size(), 20);
		assertFalse(board.getCurrentPlayer().isInCheck());
		assertFalse(board.getCurrentPlayer().isInCheckMate());
		assertEquals(board.getCurrentPlayer(), board.getWhitePlayer());
		assertEquals(board.getCurrentPlayer().getOpponent(), board.getBlackPlayer());
	}
	
	@Test
	public void checkPlayer() {
		Board board = Board.createStandardBoard();
		assertEquals(board.getCurrentPlayer(), board.getWhitePlayer());
		assertEquals(board.getCurrentPlayer().getOpponent(), board.getBlackPlayer());
	}
	
}
