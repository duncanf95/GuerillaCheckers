package com.CardboardGames.AI;

import android.app.Application;

import android.graphics.Point;
import android.util.Log;
import com.CardboardGames.Controllers.GameController;
import com.CardboardGames.Models.BoardModel;
import com.CardboardGames.Models.BoardModel.Piece;
import com.CardboardGames.Views.BoardView;
import com.CardboardGames.DataStructures.BinarySort;

import java.lang.reflect.Array;
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
    private int maxDepth = 10;




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
        if(decision != null) {
            Log.d("placePieceGuerilla", "decision not null");
            model.placeGuerillaPiece(decision);
        }
    }

    private Point guerillaDecision(){
        Point decision = null;
        ArrayList<Point> potMoves = model.getPotentialGuerillaMoves();
        Random rand = new Random();


        ArrayList<Node> currentLevel = new ArrayList<Node>();

        decision = treeSearch();



        return decision;
    }

    private void placePieceCoin(){
        Point decision = null;
        if(model.lastCoinMoveCaptured()){
            decision=coinCaptureDecision();
        }else {
            decision=coinDecision();
        }
        model.moveSelectedCoinPiece(decision);
    }

    private Point coinDecision(){
        Point decision = null;

        ArrayList<Point> potMoves = null;model.getPotentialGuerillaMoves();
        ArrayList<Point> OriginalPositions= new ArrayList<Point>();
        ArrayList<Node >firstLevel = new ArrayList<Node>();
        ArrayList<Point> Selectedpieces = new ArrayList<Point>();

        for (BoardModel.Piece p: model.getCPieces()){
            int x = 0;
            int y = 0;


            x += p.getPosition().x;
            y += p.getPosition().y;

            OriginalPositions.add(new Point(x,y));
        }

        ArrayList<Array> moves = new ArrayList<Array>();

        int counter = 0;

        for(Piece piece: model.getCPieces()){
            for(Point point: model.getCoinPotentialMoves(piece)){
                Node newNode = new Node(model, point, null, ' ', agentPlayer);
                Selectedpieces.add(OriginalPositions.get(counter));
                newNode.setState('c');
                newNode.makeCMove(piece);
                newNode.Expand(maxDepth, 0);

                firstLevel.add(newNode);


                while(model.getCPieces().size() < OriginalPositions.size()){
                    model.RestorePiece();
                }

                for (int i = 0; i < OriginalPositions.size(); i++){
                    model.getCPieces().get(i).setPosition(OriginalPositions.get(i));
                }
            }

            counter ++;
        }
        Node max = firstLevel.get(0);
        int maxIterator = 0;
        counter = 0;
        for(Node n: firstLevel){
            if (n.getReward() > max.getReward()){
                max = n;
                maxIterator = 0;
                maxIterator += counter;
            }
            counter ++;
        }

        model.selectCoinPieceAt(Selectedpieces.get(maxIterator));

        decision = max.getChoice();

        return decision;
    }

    public Point coinCaptureDecision(){
        Point decision = null;
        ArrayList<Point> potMoves = model.getCoinPotentialMoves(model.getSelectedCoinPiece());
        ArrayList<Point> OriginalPositions= new ArrayList<Point>();
        ArrayList<Node>firstLevel = new ArrayList<Node>();

        for (BoardModel.Piece p: model.getCPieces()){
            int x = 0;
            int y = 0;


            x += p.getPosition().x;
            y += p.getPosition().y;

            OriginalPositions.add(new Point(x,y));
        }

        for (Point p: potMoves){
            Node newNode = new Node(model, p, null, ' ', agentPlayer);
            newNode.setState('c');
            newNode.makeCMove(model.getSelectedCoinPiece());
            newNode.Expand(maxDepth,0);

            firstLevel.add(newNode);

            while(model.getCPieces().size() < OriginalPositions.size()){
                model.RestorePiece();
            }

            for (int i = 0; i < OriginalPositions.size(); i++){
                model.getCPieces().get(i).setPosition(OriginalPositions.get(i));
            }
        }

        Node maxNode = firstLevel.get(0);

        for(Node n: firstLevel){
            if(n.getReward() > maxNode.getReward()){
                maxNode = n;
            }
        }

        decision = maxNode.getChoice();
        return decision;
    }

    private Point treeSearch()
    {
        ArrayList<Point> potMoves = model.getPotentialGuerillaMoves();
        ArrayList<Point> OriginalPositions= new ArrayList<Point>();

        for (BoardModel.Piece p: model.getCPieces()){
            int x = 0;
            int y = 0;


            x += p.getPosition().x;
            y += p.getPosition().y;

            OriginalPositions.add(new Point(x,y));
        }

        Log.d("treeSearch", "init");
        BinarySort firstLevel = new BinarySort();
        int nodesChecked = 0;
        int nodesExpanded = 0;

        Log.d("treeSearch", "entering potential moves");
        int counter = 0;
        for (Point p: potMoves){
            Log.d("treeSearch", "potential move");
            Node newNode = new Node(model, p, null, ' ', agentPlayer);
            newNode.setState('g');
            newNode.makeGMove();
            Log.d("MainModel", Integer.toString(model.getNumGuerillaPieces()));
            Log.d("agent", "model mem " + model.toString());
            Log.d("agent", "view model mem " + view.getModelString());

            firstLevel.push(newNode);
            counter += 1;

        }
        Log.d("treeSearch", "selecting node");

        if(firstLevel.GetSort().size() != 0){
        //Node currentNode = firstLevel.GetSort().get(firstLevel.GetSort().size() - 1);
        for (Node n: firstLevel.GetSort()){
            n.Expand(maxDepth,0);
        }


        }



        float max = 0;
        Node maxNode = null;
        Log.d("treeSearch, choice size", Integer.toString(firstLevel.GetSort().size()));
        for(Node n: firstLevel.GetSort()){
            Log.d("treeSearch", "selecting choice");
            if (n.getReward() > max){
                if(n.getChoice() != null) {
                    maxNode = n;
                }
            }

        }
        if(maxNode != null) {
            Log.d("treeSearch, choice", maxNode.getChoice().toString());
        }
        Log.d("treeSearch", "Nodes expanded: " + nodesExpanded);
        Log.d("treeSearch", "Nodes checked " + nodesChecked);


        for(int i = 0; i < OriginalPositions.size(); i++){
            c_pieces.get(i).setPosition(OriginalPositions.get(i));
        }
        if(maxNode != null) {
            return maxNode.getChoice();
        }else{
            return null;
        }
    }




}
