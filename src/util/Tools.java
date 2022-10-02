package util;

import piece.ChessPiece;

import java.util.ArrayList;

public class Tools {
    public static ArrayList<ChessPiece> copy(ArrayList<ChessPiece> chessPieces) {
        ArrayList<ChessPiece> result = new ArrayList<>();
        for (ChessPiece chessPiece : chessPieces) {
            if (chessPiece != null) {
                result.add(chessPiece.clone());
            }
            else {
                result.add(null);
            }
        }
        return result;
    }
}
