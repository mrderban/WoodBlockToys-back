package com.zenika.academy.woodblocktoys.Brick;

public class BrickFactory {
    //returns the maximum value(number of pieces) that can be put in a knapsack of capacity W=50€)
    //can hold @ most maPrice€ worth of merchandise from a list of items.
    //function that maximizes the number of bricks picked for a given price value threshold
    public static Solution knapSack(Solution currentSolution, int pos) {

        //****************END OF EXPLORATION CASES****************//
        //end of list & out of capacity => return  currentSolution, no need to go further
        if (currentSolution.getCapacity() == 0 || pos == 0) {
            return currentSolution;
        }

        //if over-priced, just move to the pos to the left cause we can't keep this brick,
        else if (currentSolution.getBrickList().get(pos).getPrice() > currentSolution.getCapacity()) {
            return knapSack(currentSolution, pos - 1);
        }

        //else switch on or off the brick at position pos => we create temporary solutions and compare them

        //switch ON current brick at position pos
        Solution tempSol1 = Solution.builder()
                .brickList(currentSolution.getBrickList())
                .capacity(currentSolution.getCapacity() - currentSolution.getBrickList().get(pos).getPrice())
                .value(currentSolution.getValue() + 1)
                .build();

        //switch OFF current brick at position pos (exclude it from original bricklist => no added value, no decreased capacity (no added weight)
        currentSolution.getBrickList().remove(pos);
        Solution tempSol2 = Solution.builder()
                .brickList(currentSolution.getBrickList())
                .capacity(currentSolution.getCapacity())
                .value(currentSolution.getValue())
                .build();

        //recursive calls
        Solution tempResult1 = knapSack(tempSol1, pos - 1);
        Solution tempResult2 = knapSack(tempSol2, pos - 1);

        //return the brickList of the solution with max value
        return tempResult1.getValue() > tempResult2.getValue() ? tempResult1 : tempResult2;

    }
}
