package net.coco.chess;

import net.coco.pieces.Piece;
import net.coco.pieces.Piece.Color;
import net.coco.pieces.PieceType;
import net.coco.pieces.Point;

import java.util.ArrayList;
import java.util.Collections;
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

    public void initializeEmpty() {
        for (int i = 0; i < BOARD_CELLS; i++) {
            ranks.add(Rank.initBlankLine());
        }
    }

    public int getPieceCount(Color color, PieceType pieceType) {
        int count = 0;
        for (Rank rank : ranks) {
            count += rank.findPieceCount(pieceType);
        }
        return count;
    }

    public Piece getPieceFromPoint(String pointStr) {
        Point point = new Point(pointStr);
        return ranks.get(point.getColumn()).findPieceFromPoint(point.getRow());
    }

    public String getRankResult(int findRank) {
        return getPiecesToString(ranks.get(findRank).getPieces());
    }


    private String getPiecesToString(List<Piece> pieces) {
        return pieces.stream()
                .map(piece -> String.valueOf(piece.getPieceType().getRepresentation(piece.getColor())))
                .collect(Collectors.joining());
    }

    public void move(String pointStr, Piece piece) {
        Point point = new Point(pointStr);

        ranks.get(point.getColumn()).movePieceToPoint(piece, point.getRow());
        //todo : piece가 있던 위치도 blank로 변경해야함
    }

    public double calculateScore(Color color) {
        double sum = 0.0;
        for (int column = 0; column < ranks.size(); column++) {
            List<Piece> pieces = getPieces(color, column);
            sum += calculateColumnScore(pieces);
        }
        return sum;
    }

    private double calculateColumnScore(List<Piece> pieces) {
        int pawnCount = 0;
        double sum = 0.0;
        for (Piece piece : pieces) {
            pawnCount += piece.getPieceType() == PieceType.PAWN ? 1 : 0;
            sum += piece.getScore();
        }
        sum -= pawnCount > 1 ? pawnCount * 0.5 : 0;
        return sum;
    }

    private List<Piece> getPieces(Color color, int column) {
        ArrayList<Piece> getPieces = new ArrayList<>();
        for (int row = 0; row < ranks.size(); row++) {
            Rank getRank = ranks.get(row);
            getPieces.add(getRank.findPieceByColumn(color, column));
        }
        return getPieces;
    }

    public List<Piece> getWhitePiecesSortByScore() {
        List<Piece> getWhitePieces = new ArrayList<>();
        for (Rank rank : ranks) {
            getWhitePieces.addAll(rank.getWhitePieces());
        }
        Collections.sort(getWhitePieces, (piece1, piece2) -> (int) (piece2.getScore() - piece1.getScore()));
        return getWhitePieces;
    }

    public List<Piece> getBlackPiecesSortByScore() {
        List<Piece> getBlackPieces = new ArrayList<>();
        for (Rank rank : ranks) {
            getBlackPieces.addAll(rank.getBlackPieces());
        }
        Collections.sort(getBlackPieces, (piece1, piece2) -> (int) (piece2.getScore() - piece1.getScore()));
        return getBlackPieces;
    }

}
