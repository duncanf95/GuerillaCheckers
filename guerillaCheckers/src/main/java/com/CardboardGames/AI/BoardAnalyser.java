package com.CardboardGames.AI;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;

public class BoardAnalyser {
    public BoardAnalyser(){

    }

    public float gAnalyse(ArrayList<Point> in_gPoints, Point choice, boolean take){
        int x = choice.x;
        int y = choice.y;
        int differenceX = 0, differenceY = 0;
        Point  changeX, changeY;
        int counterPlus = 1, counterMinus = 1;
        boolean found = false;
        int result = 500;
        if(take){
            result += 10000;
            Log.d("gAnalyse", "take available");
        }
        if(in_gPoints.size() < 2){

            result += 150;

            if(choice.x < 3){
                differenceX = 3 - choice.x;
            }else if(choice.x > 3){
                differenceX = 3 + choice.x;
            }else{
                result += 279;
            }

            if(choice.y < 3){
                differenceX = 3 - choice.y;
            }else if(choice.y > 3){
                differenceX = 3 + choice.y;
            }else{
                result += 279;
            }

            differenceX = differenceX * 35;
            differenceY = differenceY * 34;

            result -= differenceX + differenceY;

        }

        else{
            while(!found){
                changeX = new Point(x - counterMinus, y);
                changeY = new Point(x,y-counterMinus);

                if(changeX.x > 0){
                    if(in_gPoints.contains(changeX)){
                        result -= 179;
                    }
                }

                if(changeY.y > 0){
                    if(in_gPoints.contains(changeY)){
                        result -= 179;
                    }
                }

                if(!(in_gPoints.contains(changeX))&&!(in_gPoints.contains(changeY))) {
                    result += 57 * counterMinus;
                    found = true;
                }

                counterMinus ++;
            }

            while(!found){
                changeX = new Point(x + counterMinus, y);
                changeY = new Point(x,y+counterMinus);

                if(changeX.x < 7){
                    if(in_gPoints.contains(changeX)){
                        result -= 179;
                    }
                }

                if(changeY.y < 7){
                    if(in_gPoints.contains(changeY)){
                        result -= 179;
                    }
                }

                if(!(in_gPoints.contains(changeX))&&!(in_gPoints.contains(changeY))) {
                    result += 57 * counterPlus;
                    found = true;
                }

                counterPlus ++;
            }
        }

        return result;
    }
}
