package com.CardboardGames.AI;

import android.graphics.Point;
import android.util.Log;
import com.CardboardGames.Models.BoardModel;

import java.util.ArrayList;

public class Node {

    private BoardModel model = null;
    private ArrayList<BoardModel.Piece> g_pieces = null;
    private ArrayList<BoardModel.Piece> c_pieces = null;
    private Node parent = null;
    private ArrayList<Node> children = null;

    public Node(BoardModel in_model, Point p, Node in_parent){
        model = new BoardModel(null);
        parent = in_parent;

        try {
            model = (BoardModel) in_model.clone();
        }catch(Exception e){
            Log.d("", "Node: " + e);
        }

        model.placeGuerillaPiece(p);
        if(model.getGPieces() != null) g_pieces = model.getGPieces();
        Point pos = null;
        for(BoardModel.Piece piece: g_pieces) {
            Log.d("node:", "created ");
            pos = piece.getPosition();
            Log.d("node C_Piece Pos X", Integer.toString(pos.x));
            Log.d("Node C_Piece Pos Y", Integer.toString(pos.y));
        }
    }

    public void expand(){

    }

    public void debug(){
        //Log.d("count Cpieces", Integer.toString(c_pieces.size()));
        if(model.getGPieces() != null) g_pieces = model.getGPieces();
        if(model.getCPieces() != null) c_pieces = model.getCPieces();
        int counter = 1;
        Point pos = null;
       // Log.d("count Gpieces", Integer.toString(g_pieces.size()));
/*
        for(BoardModel.Piece piece: g_pieces){
            pos = piece.getPosition();
            Log.d("node G_Piece Pos X" + Integer.toString(counter), Integer.toString(pos.x));
            Log.d("G_Piece Pos Y" + Integer.toString(counter), Integer.toString(pos.y));
            counter++;
        }
*/


        for(BoardModel.Piece piece: c_pieces){
            pos = piece.getPosition();
            Log.d("node C_Piece Pos X" + Integer.toString(counter), Integer.toString(pos.x));
            Log.d("C_Piece Pos Y" + Integer.toString(counter), Integer.toString(pos.y));
            counter++;
            //change
        }

    }
}

