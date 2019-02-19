package com.CardboardGames.AI;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class BoardAnalyser {
    public BoardAnalyser(){

    }

    public float gAnalyseGuerilla(ArrayList<Point> in_gPoints, Point choice,
                          boolean takeC, boolean takeG, int pieceRatio){
        int x = choice.x;
        int y = choice.y;
        int average = 1;
        int differenceX = 0, differenceY = 0;
        Point  changeX, changeY;
        int counterPlus = 1, counterMinus = 1;
        boolean found = false;
        int result = 2000;
        if(takeC){
            result += 2000;
            Log.d("gAnalyse", "take available");
        }else if(takeG){
            result -= 532;
        }
        if(in_gPoints.size() < 2){

            result += 187;

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
            differenceY = differenceY * 35;

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

            found = false;
            while(!found){
                changeX = new Point(x + counterPlus, y);
                changeY = new Point(x,y+counterPlus);

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
                    result -= 57 * counterPlus;
                    found = true;
                }
                Log.d("gAnalyseGuerilla", "centre loop");
                counterPlus ++;
            }
        }

        if(choice.x < 3){
            differenceX = 3 - choice.x;
        }else if(choice.x > 3){
            differenceX = 3 + choice.x;


        if(choice.y < 3){
            differenceX = 3 - choice.y;
        }else if(choice.y > 3) {
            differenceX = 3 + choice.y;

        }

        if(differenceX != 0 && differenceY != 0){
            average = (differenceX + differenceY)/2;
        }
        }
        return (result * pieceRatio) * average;
    }

    public float gAnalyseCoin(Point choice, boolean cTake, boolean gTake, int ratio){
       float result = 4000;
       Random rand = new Random();

       int average = 1;
       int differenceX = 1, differenceY = 1;


       int distance = 0;
       if(choice .y < 3){
           result += (3 - choice.y) * 50;
           differenceY = 3 - choice.y;
       }else if(choice.x < 3){
           result += (3 - choice.x) * 50;
           differenceX = 3 - choice.x;
       }

       if (choice.y > 3){
           result += (choice.y - 3) * 50;
           differenceY = choice.y - 3;
       }else if(choice.x > 3){
           result += (choice.x - 3) * 50;
           differenceX = choice.x - 3;
       }



       if(gTake){
           result -=600;
       }

       average = (differenceX + differenceY)/2;


       //result = rand.nextInt(1000);

       return (result - ((average * 219))) * ratio;
    }

    public float cAnalyseGuerilla(ArrayList<Point> in_gPoints, Point choice,
                                  boolean takeC, boolean takeG, int pieceRatio){
        int x = choice.x;
        int y = choice.y;
        int average = 1;
        int differenceX = 0, differenceY = 0;
        Point  changeX, changeY;
        int counterPlus = 1, counterMinus = 1;
        boolean found = false;
        int result = 2000;
        if(takeC){
            result -= 1000;
            Log.d("gAnalyse", "take available");
        }else if(takeG){
            result += 532;
        }
        if(in_gPoints.size() < 2){

            result += 187;

            if(choice.x < 3){
                differenceX = 3 - choice.x;
            }else if(choice.x > 3){
                differenceX = 3 + choice.x;
            }else{
                result -= 279;
            }

            if(choice.y < 3){
                differenceX = 3 - choice.y;
            }else if(choice.y > 3){
                differenceX = 3 + choice.y;
            }else{
                result -= 279;
            }

            differenceX = differenceX * 35;
            differenceY = differenceY * 35;

            result += differenceX + differenceY;

        }

        else{
            while(!found){
                changeX = new Point(x - counterMinus, y);
                changeY = new Point(x,y-counterMinus);

                if(changeX.x > 0){
                    if(in_gPoints.contains(changeX)){
                        result += 179;
                    }
                }

                if(changeY.y > 0){
                    if(in_gPoints.contains(changeY)){
                        result += 179;
                    }
                }

                if(!(in_gPoints.contains(changeX))&&!(in_gPoints.contains(changeY))) {
                    result += 57 * counterMinus;
                    found = true;
                }

                counterMinus ++;
            }

            found = false;
            while(!found){
                changeX = new Point(x + counterPlus, y);
                changeY = new Point(x,y+counterPlus);

                if(changeX.x < 7){
                    if(in_gPoints.contains(changeX)){
                        result += 179;
                    }
                }

                if(changeY.y < 7){
                    if(in_gPoints.contains(changeY)){
                        result += 179;
                    }
                }

                if(!(in_gPoints.contains(changeX))&&!(in_gPoints.contains(changeY))) {
                    result += 57 * counterPlus;
                    found = true;
                }
                Log.d("gAnalyseGuerilla", "centre loop");
                counterPlus ++;
            }
        }

        if(choice.x < 3){
            differenceX = 3 - choice.x;
        }else if(choice.x > 3){
            differenceX = 3 + choice.x;


            if(choice.y < 3){
                differenceX = 3 - choice.y;
            }else if(choice.y > 3) {
                differenceX = 3 + choice.y;

            }

            if(differenceX != 0 && differenceY != 0){
                average = (differenceX + differenceY)/2;
            }
        }
        Log.d("cAnalyseG: ", Float.toString((result * pieceRatio) * average));
        return (result * pieceRatio) * average;
    }

    public float cAnalyseCoin(Point choice, boolean cTake, boolean gTake, int ratio){
        float result = 7000;
        Random rand = new Random();

        int average = 1;
        int differenceX = 1, differenceY = 1;


        int distance = 0;
        if(choice .y < 3){
            result -= (3 - choice.y) * 190;
            differenceY = 3 - choice.y;
        }else if(choice.x < 3){
            result -= (3 - choice.x) * 190;
            differenceX = 3 - choice.x;
        }

        if (choice.y > 3){
            result -= (choice.y - 3) * 190;
            differenceY = choice.y - 3;
        }else if(choice.x > 3){
            result -= (choice.x - 3) * 190;
            differenceX = choice.x - 3;
        }



        if(gTake){
            result +=2000;
        }

        average = (differenceX + differenceY)/2;


        //result = rand.nextInt(1000);
        Log.d("cAnalyseG: ", Float.toString(((result + ((average * 219)))/ratio)));
        return ((result)/ratio) * (average * 219);
    }

}
