package com.chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.Alliance;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public abstract class Player {
   protected final Board board;
   protected final King playerKing;
   protected final Collection<Move> legalMoves;
   protected final Collection<Move> opponentMoves;
   private final boolean isInCheck;
   
   public Player(Board board, Collection<Move> legalMoves, Collection<Move> opponentMoves) {
	super();
	this.board = board;
	this.playerKing = establishKing();
	this.isInCheck = !Player.calculateAttackOnTile(this.playerKing.getPiecePosition(),opponentMoves).isEmpty();
	this.legalMoves =  ImmutableList.copyOf(Iterables.concat(legalMoves,calculateKingCastles(legalMoves, opponentMoves),calculateEsPassantPawnMoves())) ;
	this.opponentMoves = opponentMoves;
   }
 
   protected abstract Collection<Move> calculateEsPassantPawnMoves();

   protected static Collection<Move> calculateAttackOnTile(int piecePosition, Collection<Move> moves) {
	   final List<Move> attackMoves = new ArrayList<>();
	   for (final Move move : moves) {
		   if (piecePosition==move.getDestinationCoordinate()) {
				 attackMoves.add(move);
		   }

	   }
   	   return ImmutableList.copyOf(attackMoves);
   }

   private King establishKing() {
	 for (Piece piece : getActivePieces()) {
		if (piece.getPieceType().isKing()) {
			return (King)piece;
		}
	 }
     throw new RuntimeException("not a valid board!");
   }
   
   public boolean isMoveLegal(Move move) {
	return this.legalMoves.contains(move);
   }
   
   public boolean isInCheck() {
	   return isInCheck;
   }

   public boolean isInCheckMate() {
	   return this.isInCheck && !hasEscapeMoves();
   }

   protected boolean hasEscapeMoves() {
	   for(final Move move : this.legalMoves) {
		   final MoveTransition transition = makeMove(move);
		   if (transition.getMoveStatus().isDone()) {
			return true;
		   }
	   }
	return false;
   }

   public boolean isInStaleMate() {
	   return !this.isInCheck && !hasEscapeMoves();
   }   

   public boolean isCastled() {
	   return this.playerKing.isCastled();
   }
   
   public MoveTransition makeMove(final Move move) {
	   if (!isMoveLegal(move)) {
		 return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
	   }
	   
	   final Board transitionBoard = move.execute();
	   
	   final Collection<Move> kingsAttack = Player.calculateAttackOnTile(transitionBoard.getCurrentPlayer().getOpponent().getPlayerKing().getPiecePosition(), 
			   transitionBoard.getCurrentPlayer().getLegalMoves());
	   
	   if (!kingsAttack.isEmpty()) {
			 return new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
	   }
	   
	   return new MoveTransition(transitionBoard,move,MoveStatus.DONE);
   }
   
   public abstract Collection<Piece> getActivePieces();
   
   public abstract Alliance getAlliance();
   
   public abstract Player getOpponent();

   public Collection<Move> getLegalMoves() {
		return legalMoves;
   }

   public King getPlayerKing() {
	return playerKing;
   }
   
   protected abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegalMoves, Collection<Move> opponentLegalMoves);
}