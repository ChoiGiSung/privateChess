package net.coco.chess;

import net.coco.pieces.Piece;
import net.coco.pieces.PieceType;
import net.coco.valid.PieceValid;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Board {

    private List<Rank> ranks = new ArrayList<>();
    public static final int BOARD_CELLS = 8;

    public void initializeWithPieces() {
        //rank1이 화이트쪽이라 먼저 쌓인다.
        ranks.clear();
        ranks.add(Rank.initWhitePieces());
        ranks.add(Rank.initWhitePawns());
        ranks.add(Rank.initBlankLine());
        ranks.add(Rank.initBlankLine());
        ranks.add(Rank.initBlankLine());
        ranks.add(Rank.initBlankLine());
        ranks.add(Rank.initBlackPawns());
        ranks.add(Rank.initBlackPieces());
    }

    public void initializeEmpty(){
        for (int i = 0; i <BOARD_CELLS; i++) {
            ranks.add(Rank.initBlankLine());
        }
    }

    public int getPieceCount(String color, PieceType pieceType) {
        PieceValid.checkColorType(color);
        int count = 0;
        for (Rank rank : ranks) {
            count += rank.findPieceCount(pieceType.getRepresentation(color));
        }
        return count;
    }

    public Piece getPieceFromPoint(String point) {
        int row = point.charAt(0) - 'a';
        int column = Character.getNumericValue(point.charAt(1)) - 1;
        PieceValid.piecePointValid(row, column);

        return ranks.get(column).findPieceFromPoint(row);
    }

    public String getRankResult(int findRank) {
        return getPiecesToString(ranks.get(findRank).getPieces());
    }


    private String getPiecesToString(List<Piece> pieces) {
        return pieces.stream()
                .map(piece -> String.valueOf(piece.getRepresentation()))
                .collect(Collectors.joining());
    }

    public void move(String point,Piece piece) {
        int row = point.charAt(0) - 'a';
        int column = Character.getNumericValue(point.charAt(1)) - 1;
        PieceValid.piecePointValid(row,column);

        ranks.get(column).movePieceToPoint(piece,row);
    }
}
