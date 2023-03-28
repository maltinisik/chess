package com.chess.engine.pieces;

import java.util.List;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public abstract class Piece {
   protected final PieceType pieceType;
   protected final int piecePostion;
   protected final Alliance pieceAllience;
   protected boolean isFirstMove;
   private final int cachedHashCode;
   
   Piece(final PieceType pieceType, final int piecePosition, final Alliance pieceAllience) {
	   this.pieceType = pieceType;
	   this.piecePostion = piecePosition;
	   this.pieceAllience = pieceAllience;
	   this.isFirstMove=true;
	   this.cachedHashCode=computeHashCode();
   }
   
   private int computeHashCode() {
	   int result = pieceType.hashCode();
	   result = 31 * result * pieceAllience.hashCode();
	   result = 31 * result + piecePostion;
	   result = 31 * result + (isFirstMove == true ? 1 : 0);
	   
	   return result;
   }

  public abstract List<Move> calculateLegalMoves(final Board board);

   public Alliance getPieceAlliance() {
	   return this.pieceAllience;
   }
   
   public boolean isFirstMove() {
	   return isFirstMove;
   }

   public Integer getPiecePosition() {
		return piecePostion;
   }
   
   public PieceType getPieceType() {
	   return pieceType;
   }
   
   public abstract Piece movePiece(Move move);
   
   public int getPieceValue() {
	   return this.getPieceType().getPieceValue();
   }
   
   public enum PieceType {
	   PAWN("P",100) {
		public boolean isKing() {
			return false;
		}

		@Override
		public boolean isRook() {
			return false;
		}

		@Override
		public boolean isPawn() {
			return true;
		}
	},
	   BISHOP("B",300) {
		public boolean isKing() {
			return false;
		}

		@Override
		public boolean isRook() {
			return false;
		}

		@Override
		public boolean isPawn() {
			return false;
		}
	},
	   KNIGHT("N",300) {
		public boolean isKing() {
			return false;
		}

		@Override
		public boolean isRook() {
			return false;
		}

		@Override
		public boolean isPawn() {
			return false;
		}
	},
	   KING("K",10000) {
		public boolean isKing() {
			return true;
		}

		@Override
		public boolean isRook() {
			return false;
		}

		@Override
		public boolean isPawn() {
			return false;
		}
	},
	   QUEEN("Q",900) {
		public boolean isKing() {
			return false;
		}

		@Override
		public boolean isRook() {
			return false;
		}

		@Override
		public boolean isPawn() {
			return false;
		}
	},
	   ROOK("R",500) {
		public boolean isKing() {
			return false;
		}

		@Override
		public boolean isRook() {
			return true;
		}

		@Override
		public boolean isPawn() {
			return false;
		}
	};
	   
	   private String pieceName;
	   private int pieceValue;
	   
	   PieceType(final String pieceName, int pieceValue) {
		   this.pieceName = pieceName;
		   this.pieceValue= pieceValue;
	   }
	   
	   public String toString() {
		   return pieceName;
	   }
	   
	   public int getPieceValue() {
		   return pieceValue;
	   }

	   public abstract boolean isKing();
	
	   public abstract boolean isRook();

	   public abstract boolean isPawn();
   }
  
   @Override
   public boolean equals(final Object other) {
	   if (this==other) {return true;};
	   
	   if (!(other instanceof Piece)) {return false;}
	   
	   final Piece piece = (Piece) other;
	   
	   if (this.pieceAllience==piece.pieceAllience && this.getPiecePosition()==piece.getPiecePosition() && this.getPieceType()==piece.getPieceType() && this.isFirstMove==piece.isFirstMove()) {return true;}
	   
   	   return false;
   }
   
   @Override
   public int hashCode() {
	   return this.cachedHashCode;
   }
   
}