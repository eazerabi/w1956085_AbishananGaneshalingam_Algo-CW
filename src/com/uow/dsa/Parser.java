package com.uow.dsa;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class is responsible for parsing a maze from a file.
 * It reads the file, loads the lines into an ArrayList, and then loads the values into the maze, start point, and end point.
 */
public class Parser {
    protected Scanner lineScanner = null;
    private final ArrayList<String> lines = new ArrayList<>();
    private boolean hasRead;
    protected int[] startPoint;
    protected int[] endPoint;
    protected int[][] maze;
    private boolean hasLoaded;
    private File inputFile;

    /**
     * Checks if the file has been read.
     * This is used to ensure that the file has been read before trying to load the lines or values.
     *
     * @return true if the file has been read, false otherwise
     */
    public Boolean isFileRead() {
        return this.hasRead;
    }

    /**
     * Gets the start point of the maze.
     * This is used after the values have been loaded to get the start point of the maze.
     *
     * @return the start point of the maze if the file has been loaded, null otherwise
     */
    public int[] getStartPoint() {
        if (hasLoaded()) {
            return this.startPoint;
        }
        return null;
    }

    /**
     * Gets the end point of the maze.
     * This is used after the values have been loaded to get the end point of the maze.
     *
     * @return the end point of the maze if the file has been loaded, null otherwise
     */
    public int[] getEndPoint() {
        if (hasLoaded()) {
            return this.endPoint;
        }
        return null;
    }

    /**
     * Gets the maze.
     * This is used after the values have been loaded to get the maze.
     *
     * @return the maze if the file has been loaded, null otherwise
     */
    public int[][] getPuzzle() {
        if (hasLoaded()) {
            return this.maze;
        }
        return null;
    }

    /**
     * Loads the lines from the file into the lines ArrayList.
     * This is done after the file has been read.
     *
     * @throws IOException if an I/O error occurs reading from the file or a malformed or unmappable byte sequence is read
     */
    public void loadLines() throws IOException {
        if (this.hasRead) {
            lines.addAll(Files.readAllLines(inputFile.toPath(), Charset.defaultCharset()));
            this.hasLoaded = true;
        }
    }

    /**
     * Reads the file from the given path.
     * This is the first step in parsing the maze from the file.
     *
     * @param path the path of the file
     * @throws FileNotFoundException if the file does not exist, is a directory rather than a regular file, or for some other reason cannot be opened for reading
     */
    public void readFile(String path) throws FileNotFoundException {
        File inputFile;
        inputFile = new File(path);

        if (inputFile.length() == 0) {
            throw new FileNotFoundException("File " + path + " does not exist");
        }

        this.inputFile = inputFile;
        this.hasRead = true;
    }

    /**
     * Gets the lines from the file.
     * This is used after the lines have been loaded to get the lines.
     *
     * @return the lines from the file if the file has been read, null otherwise
     */
    public ArrayList<String> getLines() {
        if (this.hasRead) {
            return this.lines;
        }
        return null;
    }

    /**
     * Checks if the file has been loaded.
     * This is used to ensure that the lines have been loaded before trying to load the values.
     *
     * @return true if the file has been read and loaded, false otherwise
     */
    public Boolean hasLoaded() {
        if (this.isFileRead()) {
            return this.hasLoaded;
        }
        return null;
    }

    /**
     * Loads the values from the lines into the maze, start point, and end point.
     * This is done after the lines have been loaded.
     * It goes through each line, replacing certain characters with others to represent the maze, start point, and end point,
     * and then splits the line into an array of strings, which it converts into an array of integers to represent a row in the maze.
     */
    public void loadValues() {
        ArrayList<String> lines = this.getLines();
        this.lineScanner = new Scanner(this.getLines().get(0));

        int FloorSize = this.getLines().get(0).trim().length();
        this.maze = new int[FloorSize][lines.size()];
        int lineCount = 0;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            int[] floor = new int[FloorSize];
            this.lineScanner = new Scanner(line);
            int counter = 0;
            while (lineScanner.hasNext()) {
                if (counter < FloorSize) {
                    String li = lineScanner.nextLine();
                    li = li.replace("0", "1");
                    li = li.replace(".", "0");

                    if (li.contains("S")) {
                        this.startPoint = new int[]{lineCount, li.indexOf("S")};
                        li = li.replace("S", "0");
                    }
                    if (li.contains("F")) {
                        this.endPoint = new int[]{lineCount, li.indexOf("F")};
                        li = li.replace("F", "0");
                    }
                    String[] string = li.split("");

                    for (int j = 0; j < string.length; j++) {
                        floor[j] = Integer.parseInt(string[j]);
                    }
                    lineCount++;
                }
                counter++;
            }
            lineScanner.close();
            lineScanner = null;
            maze[i] = floor;
        }
    }
}