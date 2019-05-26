package com.zenika.academy.woodblocktoys.Brick;

import java.util.ArrayList;
import java.util.List;

public class BrickFactory {
    //returns the maximum value(number of pieces) that can be put in a knapsack of capacity W=50€)
    //can hold @ most maPrice€ worth of merchandise from a list of items.
    //function that maximizes the number of bricks picked for a given price value threshold
    public static Solution knapSack(List<Brick> inputBrickList, Solution currentSolution, int pos) {

        //****************END OF EXPLORATION CASES****************//
        //end of list & out of capacity => return  currentSolution, no need to go further
        if (currentSolution.getCapacity() == 0 || pos < 0) {
            return currentSolution;
        }

        Brick currentBrick = inputBrickList.get(pos);

        //if over-priced, just move to the pos to the left cause we can't keep this brick,
        if (currentBrick.getPrice() > currentSolution.getCapacity()) {
            return knapSack(inputBrickList, currentSolution, pos - 1);
        }

        //else switch on or off the brick at position pos => we create temporary solutions and compare them
        //switch ON current brick at position pos
        Solution tempRightSol = Solution.builder()
                .brickList(new ArrayList<>())
                .capacity(currentSolution.getCapacity() - currentBrick.getPrice())
                .value(currentSolution.getValue() + 1)
                .build();
        //copy currentSolution brickList content
        tempRightSol.getBrickList().addAll(currentSolution.getBrickList());
        //add current brick to tempRightSol brickList
        tempRightSol.getBrickList().add(currentBrick);

        //switch OFF current brick at position pos (exclude it from original bricklist => no added value, no decreased capacity (no added weight)
        Solution tempLeftSol = Solution.builder()
                .brickList(new ArrayList<>())
                .capacity(currentSolution.getCapacity())
                .value(currentSolution.getValue())
                .build();
        //copy currentSolution brickList content
        tempLeftSol.getBrickList().addAll(currentSolution.getBrickList());
        //do not add current brick to tempLeftSol brickList

        //recursive calls
        Solution tempRightResult = knapSack(inputBrickList, tempRightSol, pos - 1);
        Solution tempLeftResult = knapSack(inputBrickList, tempLeftSol, pos - 1);

        //return the brickList of the solution with max value
        return tempRightResult.getValue() > tempLeftResult.getValue() ? tempRightResult : tempLeftResult;

    }
}
