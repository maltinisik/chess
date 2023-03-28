package com.chess;

import java.util.Collection;
import java.util.Scanner;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MoveFactory;
import com.chess.engine.board.Move.QueenSideCastleMove;
import com.chess.gui.Table;

public class JChess {

	public static void main(String[] args) {
		method0();
		
       //method1();
       
		//method_o_o();
		
		//method_o_o_o();
	}

	private static void method0() {
		Board board = Board.createStandardBoard();
		System.out.println(board.toString());
		
		Table.get().show();
		
	}

	private static void method_o_o_o() {
		Board board = Board.createStandardBoard();
		System.out.println(board.toString());
		   
		Collection<Move> legalMoves = board.getCurrentPlayer().getLegalMoves();
		for (Move move : legalMoves) {
			if (move instanceof QueenSideCastleMove) {
				   board = move.execute();
				   System.out.println(board.toString());
			}
		}
		
		legalMoves = board.getCurrentPlayer().getLegalMoves();
		for (Move move : legalMoves) {
			if (move instanceof QueenSideCastleMove) {
				   board = move.execute();
				   System.out.println(board.toString());
			}
		}
	}

	private static void method1() {
		Board board = Board.createStandardBoard();
		   System.out.println(board.toString());
		   
		   //Move Factory
		   Move move = MoveFactory.createMove(board, 52, 36);
		   board = move.execute();
		   System.out.println(board.toString());

		   move = MoveFactory.createMove(board, 12, 28);
		   board = move.execute();
		   System.out.println(board.toString());       

		   move = MoveFactory.createMove(board, 62, 45);
		   board = move.execute();
		   System.out.println(board.toString());       

		   move = MoveFactory.createMove(board, 11, 27);
		   board = move.execute();
		   System.out.println(board.toString());
		   
		   move = MoveFactory.createMove(board, 36, 27);
		   board = move.execute();
		   System.out.println(board.toString());       

		   move = MoveFactory.createMove(board, 9, 25);
		   board = move.execute();
		   System.out.println(board.toString());       

		   
		   move = MoveFactory.createMove(board, 48, 32);
		   board = move.execute();
		   System.out.println(board.toString());
		   
		   
		   Scanner input = new Scanner(System.in);
		   int a,b;
		   
		   while (true) {
		       System.out.println("current : ");
		       a = input.nextInt();
		       
		       System.out.println("destination : ");
		       b = input.nextInt();

			   move = MoveFactory.createMove(board, a, b);
		       board = move.execute();
		       System.out.println(board.toString());
		   }
		   
		   /*
		   Piece e2 = null;
		   Piece e7 = null;
		   
		   Collection<Piece> whitePieces = board.getWhitePieces();
		   for (Piece piece : whitePieces) {
			   if (piece.getPiecePosition()==52) {
				   e2 = piece;
			   }
		   }
		  
		   Move move = new Move.MajorMove(board, e2, 36);
		   Board board2 = move.execute();
		   
		   System.out.println(board2.toString());
		   
		   Collection<Piece> blackPieces = board.getBlackPieces();
		   for (Piece piece : blackPieces) {
			   if (piece.getPiecePosition()==12) {
				   e7 = piece;
			   }
		   }       
		   
		   
		   Move move2 = new Move.MajorMove(board2, e7, 28);
		   Board board3 = move2.execute();
		   
		   System.out.println(board3.toString());
		   
		   //Move Factory
		   Move move4 = MoveFactory.createMove(board3, 11, 19);
		   Board board4 = move4.execute();
		   System.out.println(board4.toString());
		   */
	}

}
