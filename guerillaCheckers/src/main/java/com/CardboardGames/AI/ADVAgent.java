package com.CardboardGames.AI;

import android.app.Application;

import android.graphics.Point;
import android.util.Log;
import com.CardboardGames.Controllers.GameController;
import com.CardboardGames.GuerillaCheckers.Core.Piece;
import com.CardboardGames.Models.BoardModel;
import com.CardboardGames.Views.BoardView;
import com.CardboardGames.DataStructures.BinarySort;

import java.util.BitSet;
import java.util.Random;

import java.util.ArrayList;

public class ADVAgent {
    private BoardModel model;
    private BoardView view;
    private GameController controller = null;
    private ArrayList<BoardModel.Piece> g_pieces = null;
    private ArrayList<BoardModel.Piece> c_pieces = null;
    private char agentPlayer;
    private ArrayList<BinarySort> nodes = new ArrayList<BinarySort>();

    public ADVAgent(){
        //model = in_model;
        //view = in_view;
        //debugInfo();
    }


    public void setView(BoardView in_view){
        view = in_view;
    }

    public void setModel(BoardModel in_model){
        model = in_model;
        Log.d("Class load Agent", "successful");
    }

    public void setController(GameController in_controller){controller = in_controller;}

    public void setAgentPlayer(char p){ agentPlayer = p; }

    public void debugInfo(){
        Log.d("Agent", "in debug method");
        if(model.getGPieces() != null) g_pieces = model.getGPieces();
        if(model.getCPieces() != null) c_pieces = model.getCPieces();

        //Log.d("in", "debug ");

        Point pos = null;
        int counter = 1;



        // piece positions
        /*
        Log.d("count Gpieces", Integer.toString(g_pieces.size()));

        for(BoardModel.Piece piece: g_pieces){
            pos = piece.getPosition();
            Log.d("G_Piece Pos X" + Integer.toString(counter), Integer.toString(pos.x));
            Log.d("G_Piece Pos Y" + Integer.toString(counter), Integer.toString(pos.y));
            counter++;
        }
*/
        //Log.d("count Cpieces", Integer.toString(c_pieces.size()));

        for(BoardModel.Piece piece: c_pieces){
            pos = piece.getPosition();
            Log.d("C_Piece Pos X" + Integer.toString(counter), Integer.toString(pos.x));
            Log.d("C_Piece Pos Y" + Integer.toString(counter), Integer.toString(pos.y));
            counter++;
        }


        // potential moves
        /*
        ArrayList<Point> gMoves;

        if(view.getGuerillaPotentialMoves() != null) {
            gMoves = view.getGuerillaPotentialMoves();
            for (Point p :gMoves){
                Log.d("Pot G moves X " + counter, Integer.toString(p.x));
                Log.d("Pot G moves Y " + counter, Integer.toString(p.y));
                counter ++;
            }
        }

        */

    }

    public void makeMove(){
        Log.d("Agent", "makeMove");
        if(agentPlayer == 'g'){
            placePieceGuerilla();
        }else{
            Log.d("Agent", "choose coin");
            placePieceCoin();
        }
    }

    private void placePieceGuerilla(){
        Point decision = guerillaDecision();
        model.placeGuerillaPiece(decision);
    }

    private Point guerillaDecision(){
        Point decision = null;
        ArrayList<Point> potMoves = model.getPotentialGuerillaMoves();
        Random rand = new Random();

        Node tree = new Node(model, null, null);
        ArrayList<Node> currentLevel = tree.expandGuerilla();

        for(int i = 0; i < 4; i++){
            Log.d("guerillaDecision", " Level 1");
            if (nodes.size() < i+1) nodes.add(new BinarySort());
            if(i == 0) {
                for (Node n : currentLevel) {
                    ArrayList<Node> newNodes = n.expandGuerilla();
                    for (Node g : newNodes) {
                        nodes.get(i).push(n);
                    }
                }
            }else{
                BinarySort temp = nodes.get(i - 1);

                for (Node n : nodes.get(i-1).GetSort()) {
                    ArrayList<Node> newNodes = n.expandGuerilla();
                    for (Node g : newNodes) {
                        nodes.get(i).push(g);
                    }
                }
            }
        }
        int treesize = 0;
        for(BinarySort a: nodes){
            treesize += a.GetSort().size();
            for(Node e: a.GetSort()){
                Log.d("guerillaDecision", String.valueOf(e.getReward()));
            }
        }

        Log.d("guerillaDecision", "Tree size = " + treesize);

        int moveIndex = rand.nextInt(potMoves.size());

        decision = potMoves.get(moveIndex);

        return decision;
    }

    private void placePieceCoin(){
        Point decision = coinDecision();
        model.moveSelectedCoinPiece(decision);
    }

    private Point coinDecision(){
        Point decision = null;
        BoardModel.Piece selectedPiece = null;
        ArrayList<BoardModel.Piece> potPieces = model.getCPieces();
        ArrayList<Point> potMoves = null;

        Random rand = new Random();
        int pieceIndex = rand.nextInt(potPieces.size());

        selectedPiece = potPieces.get(pieceIndex);
        potMoves = view.getCoinPotentialMoves(selectedPiece);
        int moveIndex = rand.nextInt(potMoves.size());

        model.setSelectedCoinPiece(selectedPiece);

        potMoves = view.getCoinPotentialMoves(selectedPiece);

        decision = potMoves.get(moveIndex);
        Log.d("Agent", "coinDecision: " + decision);

        return decision;
    }



}
