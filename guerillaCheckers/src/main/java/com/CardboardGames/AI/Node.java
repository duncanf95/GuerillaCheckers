package com.CardboardGames.AI;

import android.graphics.Point;
import android.util.Log;
import com.CardboardGames.Models.BoardModel;

import java.util.ArrayList;
import java.util.Random;

public class Node {

    private BoardModel model = null;
    private ArrayList<BoardModel.Piece> g_pieces = null;
    private ArrayList<BoardModel.Piece> c_pieces = null;
    private Node parent = null;
    private ArrayList<Node> children = new ArrayList<Node>();
    private boolean expanded = false;
    private Random rand = new Random();
    private float reward = 0;

    public Node(BoardModel in_model, Point p, Node in_parent){
        model = new BoardModel(null);
        parent = in_parent;
        reward = rand.nextInt(100);
        try {
            model = (BoardModel) in_model.clone();
        }catch(Exception e){
            Log.d("", "Node: " + e);
        }
        if(parent != null) {
            model.placeGuerillaPiece(p);
        }
        if(model.getGPieces() != null) g_pieces = model.getGPieces();
        Point pos = null;

        //debug prints


        for(BoardModel.Piece piece: g_pieces) {
            Log.d("node:", "created ");
            pos = piece.getPosition();
            Log.d("node C_Piece Pos X", Integer.toString(pos.x));
            Log.d("Node C_Piece Pos Y", Integer.toString(pos.y));
        }

    }

    public ArrayList<Node> expandCoin(BoardModel.Piece p){
        ArrayList<Point> potmoves = model.getCoinPotentialMoves(p);

        for(Point point: potmoves){
            Node child = new Node(model, point, this);
            children.add(child);
        }
        expanded = true;
        return children;
    }

    public ArrayList<Node> expandGuerilla(){
        ArrayList<Point> potmoves = model.getPotentialGuerillaMoves();

        for(Point point: potmoves){
            Node child = new Node(model, point, this);
            children.add(child);
        }
        expanded = true;
        return children;
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

    public float getReward(){return reward;}
}