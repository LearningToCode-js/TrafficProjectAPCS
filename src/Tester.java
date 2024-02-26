// Connor Leyden and Andrew Liu
// due 02272024 TUE
// Purpose: Project 2B class to execute program

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Tester {
    static boolean[][] grid;
    static int DESIRED_OBSTACLE_RANGE = 15;
    static int NUM_SIMULATIONS_PER_OBSTACLE_COUNT = 250_000;
    static int TARGET_ROW = 6;
    static int TARGET_COL = 5;
    static int NUM_HORIZONTAL_ROADS = 6;
    static int NUM_VERTICAL_ROADS = 5;
    static int MAX_STEPS_PER_SIMULATION = 100;

    public static void createNewGrid(int numRowIntersections, int numColIntersections) {
        grid = new boolean[2 * numRowIntersections-1][2 * numColIntersections-1];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = false;
            }
        }
    }

    public static void fillObstacles(int numObstacles) {
        int totalPositions = grid.length * grid[0].length;
        for (int i = 0; i < numObstacles; i++) {
            boolean foundEmptySpace = false;
            while (!foundEmptySpace) {
                int position = (int) (Math.random()*(totalPositions));
                int randomRow = (position) / grid[0].length;
                int randomCol = (position) % grid[0].length;
                int tempRowColSum = randomRow+randomCol;
                if (tempRowColSum % 2 == 1 && !grid[randomRow][randomCol]) {
                    grid[randomRow][randomCol] = true;
                    foundEmptySpace = true;
                }
            }
        }
    }

    public static int[] pickNextDirection(int currentRow, int currentCol, int targetRow, int targetCol) {
        ArrayList<String> possibleMovesList = new ArrayList<>();
        possibleMovesList.add("Up");
        possibleMovesList.add("Down");
        possibleMovesList.add("Right");
        possibleMovesList.add("Left");
//        Checking Boundaries and Obstacles
        if(currentRow == 0  || grid[currentRow-1][currentCol]) {
            possibleMovesList.remove("Up");
//            System.out.println("Removed up");
        }
        if (currentRow == grid.length-1 || grid[currentRow+1][currentCol]) {
            possibleMovesList.remove("Down");
//            System.out.println("Removed Down");
        }

        if(currentCol == 0 || grid[currentRow][currentCol-1]) {
            possibleMovesList.remove("Left");
//            System.out.println("Removed Left");
        }
        if (currentCol == grid[0].length -1 || grid[currentRow][currentCol+1]) {
            possibleMovesList.remove("Right");
//            System.out.println("Removed Right");
        }

        int minDistance = Integer.MAX_VALUE;
        ArrayList<String> shortestMoves = new ArrayList<>();

        for (String direction : possibleMovesList) {
            int distance;
            if (direction.equals("Up")) {
                distance = Math.abs(targetCol-currentCol) + Math.abs(targetRow - (currentRow-2));
            } else if(direction.equals("Down")) {
                distance = Math.abs(targetCol-currentCol) + Math.abs(targetRow - (currentRow+2));
            } else if(direction.equals("Right")) {
                distance = Math.abs(targetCol-(currentCol+2)) + Math.abs(targetRow - (currentRow));
            } else if (direction.equals("Left")) {
                distance = Math.abs(targetCol-(currentCol-2)) + Math.abs(targetRow - (currentRow));
            } else {
                continue;
            }

            if (distance < minDistance) {
                minDistance = distance;
                shortestMoves.clear();
                shortestMoves.add(direction);
            } else if (distance == minDistance) {
                shortestMoves.add(direction);
            }
        }

        if (shortestMoves.isEmpty()) {
            return new int[]{currentRow, currentCol};
        }
        int randomIndex = (int) (Math.random() * shortestMoves.size());
        String chosenDirection = shortestMoves.get(randomIndex);
//        System.out.println("Selected: " + chosenDirection);

        if (chosenDirection.equals("Up")) {
            currentRow -= 2;
        } else if(chosenDirection.equals("Down")) {
            currentRow += 2;
        } else if(chosenDirection.equals("Right")) {
            currentCol+=2;
        } else if (chosenDirection.equals("Left")) {
            currentCol-=2;
        }

//        System.out.println("Checking in Here with Current row: " + currentRow + ". Current Col: " + currentCol);
        return new int[]{currentRow, currentCol};
    }

    public static int runOneSimulation(int targetRow, int targetCol, int numObstacles, int numRowIntersections, int numColIntersections)  {
        createNewGrid(numRowIntersections, numColIntersections);
        fillObstacles(numObstacles);
        targetRow = targetRow * 2 - 2;
        targetCol = targetCol * 2 - 2;
        int currentRow = 0;
        int currentCol = 0;
        int stepsTaken = 0;
        while (!(currentRow == targetRow) || !(currentCol == targetCol)) {
            int[] tempArr = pickNextDirection(currentRow, currentCol, targetRow, targetCol);
            currentRow = tempArr[0];
            currentCol = tempArr[1];
            stepsTaken++;
            if(stepsTaken >= MAX_STEPS_PER_SIMULATION) {
                return MAX_STEPS_PER_SIMULATION;
            }

        }
        return stepsTaken;
    }
    public static ArrayList<Integer> runSimulation(int numSimulations, int targetRow, int targetCol, int numObstacles, int numRowIntersections, int numColIntersections) {
        ArrayList<Integer> stepsArray = new ArrayList<>();
        for (int i = 0; i < numSimulations; i++) {
            stepsArray.add(runOneSimulation(targetRow, targetCol, numObstacles, numRowIntersections, numColIntersections));
        }
        System.out.println(stepsArray);
        return stepsArray;
    }

    public static void printGrid() {
        System.out.println();
        for (boolean[] i : grid) {
            for (boolean j : i) {
                System.out.print(j + " ");
            }
            System.out.println();
        }
        System.out.println();

//        for (int i = 1; i < grid.length; i+=2) {
//            for (int j = 1; j < grid[0].length; j+=2) {
//                grid[i][j] = '☐';
//            }
//        }
//        for (int i = 1; i < grid.length; i+=2) {
//            for (int j = 0; j < grid[0].length; j+=2) {
//                grid[i][j] = '|';
//            }
//        }
//        for (int i = 0; i < grid.length; i+=2) {
//            for (int j = 1; j < grid[0].length; j+=2) {
//                grid[i][j] = '—';
//            }
//        }
//        for (int i = 0; i < grid.length; i+=2) {
//            for (int j = 0; j < grid[0].length; j+=2) {
//                grid[i][j] = '*';
//            }
//        }
    }

//    runTestVisualization is for debugging purposes, it shows one simulation
    public static void runTestVisualization(int targetRow, int targetCol, int numObstacles, int numRowIntersections, int numColIntersections) {
        System.out.println("This is the beginning of a new trial!");
        createNewGrid(numRowIntersections, numColIntersections);
        fillObstacles(numObstacles);
        targetRow = targetRow * 2 - 2;
        targetCol = targetCol * 2 - 2;
        printGrid();
        int currentRow = 0;
        int currentCol = 0;
        int stepsTaken = 0;
        while (!(currentRow == targetRow) || !(currentCol == targetCol)) {
            System.out.println("Next Iteration");
            System.out.println("Current row: " + currentRow + ". Current Col: " + currentCol);
            grid[currentRow][currentCol] = true;
            printGrid();
            grid[currentRow][currentCol] = false;
            int[] tempArr = pickNextDirection(currentRow, currentCol, targetRow, targetCol);
            currentRow = tempArr[0];
            currentCol = tempArr[1];
            System.out.println("New Current row: " + currentRow + ". New Current Col: " + currentCol);
            System.out.println();
            stepsTaken++;
            if(stepsTaken >= MAX_STEPS_PER_SIMULATION) {
                break;
            }
        }
        System.out.println("We made it with " + stepsTaken + "!");
    }

    public static void getStatistics(ArrayList<Integer> arrayList) {
        double mean;
        double standardDeviation;
        int minimum;
        double q1;
        double median;
        double q3;
        int maximum;

        double sum = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            sum += arrayList.get(i);
        }
        mean = sum / arrayList.size();
        double residualSquared = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            residualSquared += Math.pow(arrayList.get(i) - mean, 2);
        }
        standardDeviation = Math.sqrt(residualSquared / (arrayList.size() -1 ));
        Collections.sort(arrayList);
        minimum = arrayList.get(0);
        maximum = arrayList.get(arrayList.size()-1);
        int middleIndex = arrayList.size() / 2;
        if (arrayList.size() % 2 == 0) {
            median = (arrayList.get(middleIndex) + arrayList.get(middleIndex - 1)) / 2.0;
            if (arrayList.size() % 4 == 0) {
                q1 = ((arrayList.get(middleIndex/2) + arrayList.get(middleIndex/2 - 1))) / 2.0;
                q3 = ((arrayList.get((middleIndex+arrayList.size())/2) + arrayList.get((middleIndex+arrayList.size())/2)) / 2.0);
            } else {
                q1 = (arrayList.get(middleIndex/2));
                q3 = (arrayList.get((middleIndex+arrayList.size())/2));
            }

        } else {
            median = (arrayList.get(middleIndex));
            if (arrayList.size() % 4 == 1) {
                q1 = ((arrayList.get(middleIndex/2) + arrayList.get(middleIndex/2 - 1))) / 2.0;
                q3 = ((arrayList.get((middleIndex+arrayList.size())/2) + arrayList.get((middleIndex+arrayList.size())/2)) / 2.0);
            } else {
                q1 = (arrayList.get(middleIndex/2));
                q3 = (arrayList.get((middleIndex+arrayList.size())/2));
            }
        }

        System.out.println("The mean is " + mean);
        System.out.println("The standard deviation is " + standardDeviation);
        System.out.println("The minimum is " + minimum);
        System.out.println("The Q1 is " + q1);
        System.out.println("The median is " + median);
        System.out.println("The Q3 is " + q3);
        System.out.println("The maximum is " + maximum);
    }

    public static void main(String[] args) {
        ArrayList<Integer>[] arrayOfArrayLists = new ArrayList[DESIRED_OBSTACLE_RANGE];
//        runTestVisualization(TARGET_ROW, TARGET_COL, 15, numRowIntersections, numColIntersections);
        for (int numObstacles = 1; numObstacles <= DESIRED_OBSTACLE_RANGE; numObstacles++) {
            arrayOfArrayLists[numObstacles-1] = runSimulation(NUM_SIMULATIONS_PER_OBSTACLE_COUNT, TARGET_ROW, TARGET_COL, numObstacles, NUM_HORIZONTAL_ROADS, NUM_VERTICAL_ROADS);
            System.out.println("=================================");
            System.out.println();
            System.out.println("The statistics for " + numObstacles + " are:");
            System.out.println("----------------------------------");
            getStatistics(arrayOfArrayLists[numObstacles]);
        }
    }
}