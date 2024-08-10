package com.uow.dsa;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is responsible for solving a puzzle by finding the shortest path in a maze.
 * It uses a com.uow.dsa.Parser to read the maze from a file, and a com.uow.dsa.ChosenAlgorithm to find the shortest path.
 */
public class Puzzle {
    private final static Scanner inputReader = new Scanner(System.in);
    private static Parser parsedInputFile;
    private static boolean shouldSkipLoad;

    private static final Logger LOGGER = Logger.getLogger(Puzzle.class.getName());

    /**
     * The main method that drives the program.
     * It displays the main menu to the user and handles their choices.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        System.out.println("Welcome to Puzzle solving");

        while (true) {
            shouldSkipLoad = false;
            displayMainMenu();
            String loopChoice = inputReader.nextLine();

            switch (loopChoice) {
                case "0":
                    System.out.println("\nThank you for using Puzzle solving. Goodbye!");
                    System.exit(0);
                    break;
                case "1":
                    loadNewInput();
                    break;
                default:
                    System.out.println("Invalid choice\n");
                    break;
            }
        }
    }

    /**
     * Displays the main menu to the user.
     * The user can choose to load a new input or quit the application.
     */
    private static void displayMainMenu() {
        System.out.print("""
                Main Menu:
                1. Load new input
                0. Quit the application
                Please enter your choice:\s""");
    }

    /**
     * Displays the load input menu to the user.
     * The user can choose to enter a file name or go back to the main menu.
     */
    private static void displayLoadInputMenu() {
        System.out.print("""

                Load Input Menu:
                1. Enter file name
                2. Go back to main menu
                Please enter your choice:\s""");
    }

    /**
     * Calculates the shortest distance from the start point to the end point in the maze.
     * It uses the com.uow.dsa.ChosenAlgorithm to find the shortest path and measures the time it takes to do so.
     */
    private static void calculateDistance() {
        Instant startTime = null;
        Instant endTime = null;
        Duration timeElapsed = null;
        while (true) {
            System.out.print("""

                    Calculation Menu:
                    1. Print the path
                    2. Restart the application
                    Please enter your choice:\s""");

            String loopChoice = inputReader.nextLine();

            int[][] n = parsedInputFile.getPuzzle();
            int[] s = parsedInputFile.getStartPoint();
            int[] t = parsedInputFile.getEndPoint();

            ChosenAlgorithm solver = new ChosenAlgorithm();

            switch (loopChoice) {
                case "1":
                    if (startTime == null) {
                        startTime = Instant.now();
                    }
                    System.out.println("\nFinding the shortest distance");

                    System.out.println(solver.shortestDistance(n, s, t));

                    if (endTime == null) {
                        endTime = Instant.now();
                        timeElapsed = Duration.between(startTime, endTime);
                    }
                    System.out.print("Time elapsed: ");

                    if (timeElapsed.toMillis() > 1000) {
                        System.out.print(timeElapsed.toSeconds() + " seconds\n");
                        return;
                    }

                    System.out.print(timeElapsed.toMillis() + " milliseconds\n");
                    break;
                case "2":
                    System.out.println();
                    parsedInputFile = null;
                    shouldSkipLoad = true;
                    return;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }

    }

    /**
     * Loads a new input from the user.
     * It displays the load input menu to the user and handles their choices.
     */
    private static void loadNewInput() {
        while (true) {
            if (shouldSkipLoad) return;
            displayLoadInputMenu();
            String loopChoice = inputReader.nextLine();
            switch (loopChoice) {
                case "1":
                    loadGraphFromFile();
                    return;
                case "2":
                    return;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }
    }


    /**
     * Loads a graph from a file specified by the user.
     * It asks the user for the file name, reads the file using a com.uow.dsa.Parser, and then calculates the shortest distance.
     */
    private static void loadGraphFromFile() {
        System.out.println("\nPlease place the text file in the 'inputs' folder and enter the text file in the console");
        try {
            String userInputFileName = getUserInputFileName();
            if (userInputFileName.equals("2")) return; // Check if user wants to exit

            Parser fileParser = new Parser();
            fileParser.readFile("src/inputs/" + userInputFileName);
            fileParser.loadLines();
            fileParser.loadValues();
            if (!fileParser.isFileRead()) {
                LOGGER.log(Level.SEVERE, "File not found in the path");
                throw new FileNotFoundException("File not loaded");
            }
            parsedInputFile = fileParser;
            shouldSkipLoad = true;
            calculateDistance();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error reading input file\ncause: {0}", Arrays.toString(e.getStackTrace()));
        }
    }


    /**
     * Gets the name of the input file from the user.
     * It asks the user for the file name and checks if it is valid.
     *
     * @return the name of the input file
     */
    private static String getUserInputFileName() {
        while (true) {
            System.out.println("Note: It should be a text file with the '.txt' file extension");
            System.out.print("Input file name:  ");
            String userInputFileName = inputReader.nextLine();
            if (isValidFileName(userInputFileName)) return userInputFileName;
            System.out.println("\nInvalid file name format or file not found in the path");
        }
    }

    /**
     * Checks if the given file name is valid.
     * A file name is valid if it ends with ".txt" and the file exists in the "src/inputs/" directory.
     *
     * @param fileName the name of the file
     * @return true if the file name is valid, false otherwise
     */
    private static boolean isValidFileName(String fileName) {
        return fileName.endsWith(".txt") && new File("src/inputs/" + fileName).exists();
    }
}