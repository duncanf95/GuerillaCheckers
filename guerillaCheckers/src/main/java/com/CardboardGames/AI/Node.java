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
    private float initialReward = 0;
    private char moveType;
    private char agentType;
    private Point choice = null;
    private boolean depthReached = false;

    private enum GameState {
        GUERILLA_SETUP_FIRST{
            @Override
            public String toString(){
                return "g";
            }
        },
        GUERILLA_SETUP_SECOND{
            @Override
            public String toString(){
                return "g";
            }
        },
        COIN_MOVE{
            @Override
            public String toString(){
                return "c";
            }
        },
        COIN_CAPTURE{
            @Override
            public String toString(){
                return "c";
            }
        },
        GUERILLA_MOVE_FIRST{
            @Override
            public String toString(){
                return "g";
            }
        },
        GUERILLA_MOVE_SECOND{
            @Override
            public String toString(){
                return "g";
            }
        },
        END_GAME{
            @Override
            public String toString(){
                return "END";
            }
        }
    }

    private GameState state;

    public Node(BoardModel in_model, Point p, Node in_parent,char in_moveType, char in_agentType){
        model = new BoardModel(null);
        parent = in_parent;
        reward = rand.nextInt(10000);
        agentType = in_agentType;
        initialReward += reward;
        choice = p;
        try {
            model = in_model.clone();
            Log.d("clone success", " node");
            Log.d("model in mem", in_model.toString());
            Log.d("model out mem", model.toString());

        }catch(Exception e){
            Log.d("failed clon", "Node: " + e);
        }
        if(parent != null) {

            moveType = in_moveType;
            checkScore();
        }
        if(model.getGPieces() != null) g_pieces = model.getGPieces();
        if(model.getCPieces() != null) c_pieces = model.getCPieces();
        Point pos = null;

        //debug prints


        for(BoardModel.Piece piece: g_pieces) {
            Log.d("node:", "created ");
            pos = piece.getPosition();
            Log.d("node C_Piece Pos X", Integer.toString(pos.x));
            Log.d("Node C_Piece Pos Y", Integer.toString(pos.y));
        }

    }

    public ArrayList<Node>  expand(){
        Log.d("expandState", state.toString());
        switch(state){
            case GUERILLA_SETUP_FIRST:
                //expandGuerilla();
            case GUERILLA_SETUP_SECOND:
               // expandGuerilla();
            case GUERILLA_MOVE_FIRST:
                //expandGuerilla();
            case GUERILLA_MOVE_SECOND:
                //expandGuerilla();
            case COIN_MOVE:
                //expandCoin();
            case COIN_CAPTURE:
                //expandCoin();
        }
        return children;
    }

    public ArrayList<Node> expandCoin(int maxLevel, int currentLevel){
        ArrayList<BoardModel.Piece> pieces = model.getCPieces();
        Log.d("Coin", "expanded");
        Log.d("Coin", "amount of pieces " + pieces.size());

        ArrayList<Point> OriginalPositions= new ArrayList<Point>();


        for (BoardModel.Piece p: model.getCPieces()){
            int x = 0;
            int y = 0;


            x += p.getPosition().x;
            y += p.getPosition().y;

            OriginalPositions.add(new Point(x,y));
        }
        for(BoardModel.Piece p: pieces) {
            ArrayList<Point> potmoves = model.getCoinPotentialMoves(p);
            Log.d("Coin", "found pieces");

            for (Point point : potmoves) {
                Log.d("Coin", "found moves");
                Log.d("Coin","current state " + state.toString());
                Node child = new Node(model, point, this, 'c', agentType);
                child.setStateExpand(state);
                child.makeCMove(p);
                child.moveToNextState();
                if(state.toString() != Character.toString(agentType)){
                    if (child.getReward() <= reward  || !depthReached){
                        Log.d("Coin","expanding child");
                        child.Expand(maxLevel, currentLevel + 1);
                        children.add(child);
                    }
                }else if(state.toString() == Character.toString(agentType) ){
                    if (child.getReward() >= reward || !depthReached){
                        Log.d("Coin","expanding child");
                        child.Expand(maxLevel, currentLevel + 1);
                        children.add(child);
                    }
                }
                if(model.getCPieces().size() < OriginalPositions.size()) {
                    while (model.getCPieces().size() < OriginalPositions.size()){
                        model.RestorePiece();
                    }
                }

                for(int i = 0; i < OriginalPositions.size(); i++){
                    c_pieces.get(i).setPosition(OriginalPositions.get(i));
                }
            }
        }
        Log.d("expandCoin currentlevel", Integer.toString(currentLevel));
        Log.d("expandCoin currentscore", Float.toString(reward));
        Log.d("Cpp amount of pieces", Integer.toString(model.getNumCoinPieces()));
        expanded = true;
        return children;
    }

    public ArrayList<Node> expandGuerilla(int maxLevel, int currentLevel){
        Log.d("guerilla", "expanded");
        ArrayList<Point> potmoves = model.getPotentialGuerillaMoves();
        ArrayList<Point> OriginalPositions= new ArrayList<Point>();


        for (BoardModel.Piece p: model.getCPieces()){
            int x = 0;
            int y = 0;


            x += p.getPosition().x;
            y += p.getPosition().y;

            OriginalPositions.add(new Point(x,y));
        }

        for(Point point: potmoves){
            checkDepthReached();
            Node child = new Node(model, point, this, 'g', agentType);
            child.setStateExpand(state);
            child.makeGMove();
            child.moveToNextState();
            Log.d("Guerilla","current state " + state.toString());
            if(state.toString() != Character.toString(agentType)){
                if (child.getReward() <= reward  || !depthReached){
                    Log.d("Guerilla","expanding child");
                    child.Expand(maxLevel, currentLevel + 1);
                    children.add(child);
                }
            }else if(state.toString() == Character.toString(agentType) ){
                if (child.getReward() >= reward || !depthReached){
                    Log.d("Guerilla","expanding child");
                    child.Expand(maxLevel, currentLevel + 1);
                    children.add(child);
                }
            }
            Log.d("Gp currentlevel", Integer.toString(currentLevel));
            Log.d("Gp currentscore", Float.toString(reward));
            Log.d("Gpp amount of pieces", Integer.toString(model.getNumGuerillaPieces()));

            if(model.getCPieces().size() < OriginalPositions.size()) {
                while (model.getCPieces().size() < OriginalPositions.size()){
                    model.RestorePiece();
                }
            }

            for(int i = 0; i < OriginalPositions.size(); i++){
                c_pieces.get(i).setPosition(OriginalPositions.get(i));
            }

        }
        expanded = true;
        return children;
    }

    public void Expand(int maxLevel, int currentLevel){
        if(currentLevel <= maxLevel) {
            Log.d("Expand cl", Integer.toString(currentLevel));
            if (state.toString() == "g") {
                Log.d("Expand", "found state g");
                expandGuerilla(maxLevel,currentLevel);
            } else if (state.toString() == "c"){
                Log.d("Expand", "found state c");
                Log.d("Expand", Integer.toString(maxLevel));
                expandCoin(maxLevel,currentLevel);
            } else {
                Log.d("Expand", "state not found");
            }
        }else if(currentLevel > maxLevel){
            setDepthReached(true);
            Log.d("Expand", "depth reached");
        }
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

    public void setReward(float new_reward){reward = new_reward;}

    public void checkScore(){
        Log.d("checkScore", "checked");
        if(parent != null){
            if(parent.getState() == Character.toString(agentType)) {

                if (reward > parent.getReward()) {
                    Log.d("checkScore", "changed");
                    parent.setReward(reward);
                    parent.checkScore();
                }
            }else{
                if (reward < parent.getReward()) {
                    Log.d("checkScore", "changed");
                    parent.setReward(reward);
                    parent.checkScore();
                }
            }
        }
    }

    public void moveToNextState() {
        Log.d("moveToNextState Node", "in function");
        if (state != GameState.END_GAME && model.isGameOver()) {
            state = GameState.END_GAME;
            return;
        }

        switch (state) {
            case GUERILLA_SETUP_FIRST:
                state = GameState.GUERILLA_SETUP_SECOND;
                return;
            case GUERILLA_SETUP_SECOND:
                model.setCurrentPlayer(BoardModel.Player.COIN_PLAYER);
                state = GameState.COIN_CAPTURE;
                return;
            case COIN_MOVE:
                state = GameState.COIN_CAPTURE;
            case COIN_CAPTURE:
                if (model.lastCoinMoveCaptured()) {
                    model.setCoinMustCapture(true);
                    if (model.selectedCoinPieceHasValidMoves()) {
                        state = GameState.COIN_CAPTURE;
                        return;
                    }
                }

                model.setCoinMustCapture(false);
                model.setLastCoinMoveCaptured(false);
                model.deselectCoinPiece();
                model.setCurrentPlayer(BoardModel.Player.GUERILLA_PLAYER);
                state = GameState.GUERILLA_MOVE_FIRST;
                return;
            case GUERILLA_MOVE_FIRST:
                if (model.hasValidGuerillaPlacements()) {
                    state = GameState.GUERILLA_MOVE_SECOND;
                    return;
                }
            case GUERILLA_MOVE_SECOND: // pass-through from first
                model.clearGuerillaPieceHistory();
                model.setCurrentPlayer(BoardModel.Player.COIN_PLAYER);
                state = GameState.COIN_MOVE;
                return;
            case END_GAME:
                model.reset();
                state = GameState.GUERILLA_SETUP_FIRST;
                break;
        }
    }

    public void setState(char choice){
        switch (choice){
            case 'g':
                Log.d("setState", "guerilla");
                state = GameState.GUERILLA_SETUP_FIRST;

            case 'c':
                Log.d("setState", "coin");
                state = GameState.COIN_MOVE;
        }
    }

    public void setStateExpand(GameState s){
        state = s;
    }

    public String getState(){
        return state.toString();
    }

    public Point getChoice(){
        return choice;
    }

    public boolean getDepthReached(){
        return depthReached;
    }

    public void checkDepthReached(){
        if (parent.getDepthReached()){
            depthReached = true;
        }
    }

    public void setDepthReached(boolean d){
        depthReached = d;
        if (parent!=null) {
            parent.setDepthReached(d);
        }
    }

    public void makeGMove(){
        if(choice != null){
            model.placeGuerillaPiece(choice);
        }
    }

    public void makeCMove(BoardModel.Piece p){
        model.selectCoinPieceAt(p.getPosition());

        if(choice != null){
            model.moveSelectedCoinPiece(choice);
        }
    }

}