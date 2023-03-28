package com.chess.engine.board;

import com.chess.engine.board.Board.Builder;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

public abstract class Move {
  final Board board;
  final Piece movedPiece;
  final int destinationCoordinate;
  
  private static final Move NULL_MOVE = new NullMove();

  private Move(Board board, Piece movedPiece, int destinationCoordinate) {
	super();
	this.board = board;
	this.movedPiece = movedPiece;
	this.destinationCoordinate = destinationCoordinate;
  }
  
  public static final class MajorMove extends Move {
	  public MajorMove(Board board, Piece movedPiece, int destinationCoordinate) {
         super(board, movedPiece, destinationCoordinate);
	  }
	  
	  @Override
	  public boolean equals(Object otherObject) {
		return this==otherObject || (otherObject instanceof MajorMove && super.equals(otherObject));
	  }
	  
	  @Override
	  public String toString() {
		 return this.movedPiece.getPieceType().toString() + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
	  }
	  
  }
  
  public static final class PawnPromotion extends Move {
	  final Move decoratedMove;
	  final Pawn promotedPawn;
	  public PawnPromotion(final Move decoratedMove) {
         super(decoratedMove.getBoard(), decoratedMove.getMovedPiece(), decoratedMove.getDestinationCoordinate());
         
         this.decoratedMove=decoratedMove;
         this.promotedPawn=(Pawn)decoratedMove.getMovedPiece();
	  }
	  
	  @Override
	  public Board execute() {
		    
		    final Board pawnMovedBoard = this.decoratedMove.execute();
		    final Builder builder = new Builder();
		    
		    for (Piece piece : pawnMovedBoard.getCurrentPlayer().getActivePieces()) {
				if (!this.movedPiece.equals(piece)) {
					builder.setPiece(piece);
				}
			}
		    
		    for (Piece piece : pawnMovedBoard.getCurrentPlayer().getOpponent().getActivePieces()) {
					builder.setPiece(piece);	
			}
		    
		    //promote the piece
		    
		    builder.setPiece(this.promotedPawn.getPromotionPiece().movePiece(this));
		    builder.setMoveMaker(pawnMovedBoard.getCurrentPlayer().getAlliance());
		    
			return builder.build();
    }
  }  

  public static final class PawnMove extends Move {
	  public PawnMove(Board board, Piece movedPiece, int destinationCoordinate) {
         super(board, movedPiece, destinationCoordinate);
	  }
  }
  
  public static class AttackMove extends Move {
	  final Piece attackedPiece;
	  public AttackMove(Board board, Piece movedPiece, int destinationCoordinate,Piece attackedPiece) {
         super(board, movedPiece, destinationCoordinate);
	     this.attackedPiece=attackedPiece;
	  }
	  
	  @Override
	  public int hashCode() {
		  return this.attackedPiece.hashCode() + super.hashCode();
	  }
	  
	  @Override
	  public boolean equals(final Object object) {
		  if(this==object) return true;
		  
		  if (!(object instanceof AttackMove)) {return false;}
		  
		  AttackMove otherAttackMove = (AttackMove) object;
		  
		  return super.equals(otherAttackMove) && otherAttackMove.getAttackedPiece().equals(this.getAttackedPiece());
	  }
	  
	  @Override
	  public boolean isAttack() {
		  return true;
	  }
	  
	  @Override
	  public Piece getAttackedPiece() {
		  return this.getAttackedPiece();
	  }
  }
  
  public static class PawnAttackMove extends AttackMove {
	  public PawnAttackMove(Board board, Piece movedPiece, int destinationCoordinate,Piece attackedPiece) {
         super(board, movedPiece, destinationCoordinate,attackedPiece);
	  }
  }
  
  public static final class PawnEsPassantAttackMove extends PawnAttackMove {
	  public PawnEsPassantAttackMove(Board board, Piece movedPiece, int destinationCoordinate,Piece attackedPiece) {
         super(board, movedPiece, destinationCoordinate,attackedPiece);
	  }
	  
	  public Board execute() {
		    final Builder builder = new Builder();
		    
		    for (Piece piece : board.getCurrentPlayer().getActivePieces()) {
				if (!this.movedPiece.equals(piece)) {
					builder.setPiece(piece);
				}
			}
		    
		    for (Piece piece : board.getCurrentPlayer().getOpponent().getActivePieces()) {
		    	if (!this.attackedPiece.equals(piece) ) {
					builder.setPiece(piece);	
				}
			}
		    
		    //move the moved piece
		    builder.setPiece(movedPiece.movePiece(this));
		    builder.setMoveMaker(board.getCurrentPlayer().getOpponent().getAlliance());
		    
			return builder.build();
      }
  }
  
  public static final class PawnJump extends Move {
	  public PawnJump(Board board, Piece movedPiece, int destinationCoordinate) {
         super(board, movedPiece, destinationCoordinate);
	  }
	  
	  @Override
	  public Board execute() {
		  final Builder builder = new Builder();
		  
		  for (Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
			if (piece!=this.movedPiece) {
				  builder.setPiece(piece);	
			}
		  }
		  
		  for (Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
			builder.setPiece(piece);
		  }
		  
		  final Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this);
		  builder.setPiece(movedPawn);
		  builder.setEsPassantPawn(movedPawn);
		  builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
		  return builder.build();
	  }
	  
  }  
  
  static class CastleMove extends Move {
	  protected final Rook castleRook;
	  protected final int castleRookStart;
	  protected final int castleRookDestination;
	  public CastleMove(final Board board, final Piece movedPiece, final int destinationCoordinate,final Rook castleRook,int castleRookStart,int castleRookDestination) {
         super(board, movedPiece, destinationCoordinate);
         this.castleRook=castleRook;
         this.castleRookStart=castleRookStart;
         this.castleRookDestination=castleRookDestination;
	  }
	
	  public Rook getCastleRook() {
		return castleRook;
	  }
	
	  public int getCastleRookStart() {
		return castleRookStart;
	  }
	
	  public int getCastleRookDestination() {
		return castleRookDestination;
	  }
	  
	  @Override
	  public boolean isCatlingMove() {
		  return true;
	  }
	  
	  @Override
	  public Board execute() {
		    final Builder builder = new Builder();
		    
		    for (Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
				if (!this.movedPiece.equals(piece) && !this.castleRook.equals(piece)) {
					builder.setPiece(piece);
				}
			}
		    
		    for (Piece piece : board.getCurrentPlayer().getOpponent().getActivePieces()) {
					builder.setPiece(piece);
			}
		    
		    //move the king
		    builder.setPiece(movedPiece.movePiece(this));
		    //move the rook
		    builder.setPiece(new Rook(castleRookDestination, this.board.getCurrentPlayer().getAlliance()));
		    //todo set isfirstmove
		    builder.setMoveMaker(board.getCurrentPlayer().getOpponent().getAlliance());
		    
			return builder.build();
	  }
  }
  
  public static class KingSideCastleMove extends CastleMove {
	  public KingSideCastleMove(Board board, Piece movedPiece, int destinationCoordinate,final Rook castleRook,int castleRookStart,int castleRookDestination) {
         super(board, movedPiece, destinationCoordinate,castleRook,castleRookStart,castleRookDestination);
	  }
	  
	  @Override
	  public String toString() {
		  return "o-o";
	  }
  }

  public static class QueenSideCastleMove extends CastleMove {
	  public QueenSideCastleMove(Board board, Piece movedPiece, int destinationCoordinate,final Rook castleRook,int castleRookStart,int castleRookDestination) {
         super(board, movedPiece, destinationCoordinate,castleRook,castleRookStart,castleRookDestination);
	  }
	  @Override
	  public String toString() {
		  return "o-o-o";
	  }
  }
  
  public static class NullMove extends Move {
	  public NullMove() {
         super(null, null, -1);
	  }
	  
	  @Override
	  public Board execute() {
		  throw new RuntimeException("null move!!!");
	  }
  }
  
  public int getDestinationCoordinate() {
	return destinationCoordinate;
  }

  public Board getBoard() {
	return this.board;
}

public int getCurrentCoordinate() {
	return this.movedPiece.getPiecePosition();
  }
  
  public Piece getMovedPiece() {
		return movedPiece;
  }
  
  public boolean isAttack() {
	  return false;
  }
  
  public boolean isCatlingMove() {
	  return false;
  }
  
  public Piece getAttackedPiece() {
	  return null;
  }

  public Board execute() {
    final Builder builder = new Builder();
    
    for (Piece piece : board.getCurrentPlayer().getActivePieces()) {
		if (!this.movedPiece.equals(piece)) {
			builder.setPiece(piece);
		}
	}
    
    for (Piece piece : board.getCurrentPlayer().getOpponent().getActivePieces()) {
			builder.setPiece(piece);
	}
    
    //move the moved piece
    builder.setPiece(movedPiece.movePiece(this));
    builder.setMoveMaker(board.getCurrentPlayer().getOpponent().getAlliance());
    
	return builder.build();
  }
  
  public static class MoveFactory {
	private  MoveFactory() {
		  throw new RuntimeException("Not instantible!");
	  }
	  
	  public static Move createMove(final Board board,
			                        final int currentCoordinate,
			                        final int destinationCoordinate) {
		  for(final Move move : board.getAllLegalMoves()) {
			  if (move.getCurrentCoordinate()==currentCoordinate && move.getDestinationCoordinate()==destinationCoordinate) {
				return move;
			}
		  }
		  
		  return NULL_MOVE;
	  }
  }
  
  @Override
  public int hashCode() {
	  final int prime = 31;
	  int result = 1;
	  
	  result = prime * result * this.destinationCoordinate;
	  result = prime * result * this.movedPiece.hashCode();
	  
	  return result;
  }
  
  @Override
  public boolean equals(final Object object) {
	  if(this==object) return true;
	  
	  if (!(object instanceof Move)) {return false;}
	  
	  Move otherMove = (Move) object;
	  
	  return otherMove.getDestinationCoordinate() == this.getDestinationCoordinate() &&
			 otherMove.getMovedPiece() == this.getMovedPiece();
  }
  
}
