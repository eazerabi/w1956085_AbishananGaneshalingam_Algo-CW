package com.uow.dsa;

import java.util.LinkedList;
import java.util.Queue;


/**
 * This class represents an algorithm for finding the shortest path in a maze from a ball to a hole.
 * It uses a breadth-first search approach to find the shortest path.
 */
public class ChosenAlgorithm {

    /**
     * This class represents a node in the path of the algorithm.
     * It contains the row and column of the node, the distance from the start, and the path taken to reach this node.
     * It also implements Comparable interface to allow comparison between nodes based on their distance from the start.
     */
    private static class PathNode implements Comparable<PathNode> {
        int row, col, distanceFromStart;
        String path;

        /**
         * Constructs a new PathNode with the given parameters.
         *
         * @param row               the row of the node
         * @param col               the column of the node
         * @param distanceFromStart the distance from the start to this node
         * @param path              the path taken to reach this node
         */
        PathNode(int row, int col, int distanceFromStart, String path) {
            this.row = row;
            this.col = col;
            this.distanceFromStart = distanceFromStart;
            this.path = path;
        }

        /**
         * Compares this PathNode to another PathNode.
         * The comparison is based first on the distance from the start, and then on the path.
         * This is used to determine the order of nodes in the priority queue.
         *
         * @param other the other PathNode to compare to
         * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object
         */
        @Override
        public int compareTo(PathNode other) {
            return this.distanceFromStart == other.distanceFromStart ?
                    this.path.compareTo(other.path) :
                    this.distanceFromStart - other.distanceFromStart;
        }
    }


    /**
     * Finds the shortest path in the given maze from the ball to the hole.
     * It uses a breadth-first search approach, where it explores all possible paths from the ball to the hole,
     * and returns the shortest one.
     *
     * @param maze a 2D array representing the maze, where 0s represent open spaces and 1s represent walls
     * @param ball a 2-element array representing the row and column of the ball's starting position
     * @param hole a 2-element array representing the row and column of the hole's position
     * @return a string representing the shortest path from the ball to the hole, or "No path found!" if no path exists
     */
    public String shortestDistance(int[][] maze, int[] ball, int[] hole) {
        int numRows = maze.length, numCols = maze[0].length;
        boolean[][] visited = new boolean[numRows][numCols];
        Queue<PathNode> queue = new LinkedList<>();
        queue.offer(new PathNode(ball[0], ball[1], 0, "Start at (" + (ball[1] + 1) + ", " + (ball[0] + 1) + ")"));

        String[] directions = {"Move Right to", "Move Down to", "Move Left to", "Move Up to"};
        int[][] moves = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

        while (!queue.isEmpty()) {
            PathNode currentNode = queue.poll();
            if (currentNode.row == hole[0] && currentNode.col == hole[1]) return formatPath(currentNode);
            for (int i = 0; i < moves.length; i++) {
                int newRow = currentNode.row, newCol = currentNode.col, newDist = currentNode.distanceFromStart;
                String newPath = currentNode.path;
                while (newRow >= 0 && newRow < numRows && newCol >= 0 && newCol < numCols &&
                        maze[newRow][newCol] == 0 &&
                        (newRow != hole[0] || newCol != hole[1])) {
                    newRow += moves[i][0];
                    newCol += moves[i][1];
                    newDist++;
                }
                if (newRow != hole[0] || newCol != hole[1]) {
                    newRow -= moves[i][0];
                    newCol -= moves[i][1];
                    newDist--;
                }
                if (!visited[newRow][newCol]) {
                    visited[newRow][newCol] = true;
                    queue.offer(new PathNode(newRow, newCol, newDist, newPath + "\n" + directions[i] +
                            " (" + (newCol + 1) + ", " + (newRow + 1) + ")"));
                }
            }
        }
        return "No path found!";
    }


    /**
     * Formats the path of the given PathNode into a string.
     * It takes the path of the node and formats it into a human-readable string.
     *
     * @param node the PathNode whose path to format
     * @return a string representing the path of the given PathNode
     */
    private String formatPath(PathNode node) {
        String[] steps = node.path.split("\n");
        StringBuilder formattedPath = new StringBuilder();
        for (int i = 0; i < steps.length; i++) {
            formattedPath.append((i + 1)).append(".  ").append(steps[i]);
            if (i < steps.length - 1) formattedPath.append("\n");
        }
        return formattedPath + "\n" + (steps.length + 1) + ". Done!";
    }
}