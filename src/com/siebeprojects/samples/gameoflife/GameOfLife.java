/*
 * This file is part of Siebe Projects samples.
 *
 * Siebe Projects samples is free software: you can redistribute it and/or modify
 * it under the terms of the Lesser GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Siebe Projects samples is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Lesser GNU General Public License for more details.
 *
 * You should have received a copy of the Lesser GNU General Public License
 * along with Siebe Projects samples.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.siebeprojects.samples.gameoflife;

/**
 * Class implementing Conway's GameOfLife 
 *
 * http://en.wikipedia.org/wiki/Conway's_Game_of_Life
 *
 * Conventions
 *  int[][] board;   // The game board is a 2D array.
 *  board[4][5] = 1; // Means that the cell at (4,5) is live.
 *  board[4][5] = 0; // Means that the cell at (4,5) is dead.
 *
 * Rules of the game
 * 1. Any live cell with fewer than two live neighbours dies, as if caused by under-population.
 * 2. Any live cell with two or three live neighbours lives on to the next generation.
 * 3. Any live cell with more than three live neighbours dies, as if by over-population.
 * 4. Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
 */
public final class GameOfLife {

    // The value representing a dead cell
    public final static int DEAD    = 0x00;

    // The value representing a live cell
    public final static int LIVE    = 0x01;

    /** 
     * Start the GameOfLife example
     * 
     * @param args      arguments, not used for this example
     */
    public final static void main(String[] args) {

        // test the game of life implementation
        GameOfLife gof = new GameOfLife();
        gof.test(5);
    }


    /**
     * Test the gameoflife implementation, change the array 
     * values to test each condition in the game of life.
     *
     * @param nrIterations      the number of times the board should be played
     */
    private void test(int nrIterations) {

        // the starting playing board with life and dead cells
        int[][] board = {{DEAD, DEAD, DEAD, DEAD, DEAD},
                         {DEAD, DEAD, DEAD, LIVE, DEAD},
                         {DEAD, DEAD, LIVE, LIVE, DEAD},
                         {DEAD, DEAD, DEAD, LIVE, DEAD},
                         {DEAD, DEAD, DEAD, DEAD, DEAD}}; 
        
        System.out.println("Conway's GameOfLife");
        printBoard(board);

        for (int i = 0 ; i < nrIterations ; i++) {
            System.out.println();
            board = getNextBoard(board);
            printBoard(board);
        }
    }

    /** 
     * Print one board field to System.out
     * 
     * @param board The board to be printed to System.out
     */
    private void printBoard(int[][] board) {

        for (int i = 0, e = board.length ; i < e ; i++) {

            for (int j = 0, f = board[i].length ; j < f ; j++) {
                System.out.print(Integer.toString(board[i][j]) + ",");
            } 
            System.out.println();
        }
    }

    /**
     * get the next game board, this will calculate if cells live on or die or new
     * ones should be created by reproduction.
     * 
     * @param b        The current board field
     * @return A newly created game buffer
     */
    public int[][] getNextBoard(int[][] board) {

        // The board does not have any values so return the newly created
        // playing field.
        if (board.length == 0 || board[0].length == 0) {
            throw new IllegalArgumentException("Board must have a positive amount of rows and/or columns");
        }

        int nrRows = board.length;
        int nrCols = board[0].length;

        // temporary board to store new values
        int[][] buf = new int[nrRows][nrCols];

        for (int row = 0 ; row < nrRows ; row++) {
	
            for (int col = 0 ; col < nrCols ; col++) {
                buf[row][col] = getNewCellState(board[row][col], getLiveNeighbours(row, col, board));
            }
        }   
        return buf;
    }

    /**
     * Get the number of the live neighbours given the cell position
     * 
     * @param cellRow       the column position of the cell
     * @param cellCol       the row position of the cell
     * @return the number of live neighbours given the position in the array
     */
    private int getLiveNeighbours(int cellRow, int cellCol, int[][] board) {

        int liveNeighbours = 0;
        int rowEnd = Math.min(board.length , cellRow + 2);
        int colEnd = Math.min(board[0].length, cellCol + 2);

        for (int row = Math.max(0, cellRow - 1) ; row < rowEnd ; row++) {
            
            for (int col = Math.max(0, cellCol - 1) ; col < colEnd ; col++) {
                
                // make sure to exclude the cell itself from calculation
                if ((row != cellRow || col != cellCol) && board[row][col] == LIVE) {
                    liveNeighbours++;
                }
            }
        }
        return liveNeighbours;
    }


    /** 
     * Get the new state of the cell given the current state and
     * the number of live neighbours of the cell.
     * 
     * @param curState          The current state of the cell, either DEAD or ALIVE
     * @param liveNeighbours    The number of live neighbours of the given cell.
     * 
     * @return The new state of the cell given the number of live neighbours 
     *         and the current state
     */
    private int getNewCellState(int curState, int liveNeighbours) {

        int newState = curState;

        switch (curState) {
        case LIVE:

            // Any live cell with fewer than two 
            // live neighbours dies
            if (liveNeighbours < 2) {
                newState = DEAD;
            }

            // Any live cell with two or three live   
            // neighbours lives on to the next generation.
            if (liveNeighbours == 2 || liveNeighbours == 3) {
                newState = LIVE;
            }

            // Any live cell with more than three live neighbours
            // dies, as if by overcrowding.
            if (liveNeighbours > 3) {
                newState = DEAD;
            }
            break;

        case DEAD:
            // Any dead cell with exactly three live neighbours becomes a 
            // live cell, as if by reproduction.
            if (liveNeighbours == 3) {
                newState = LIVE;
            }
            break;

        default:
            throw new IllegalArgumentException("State of cell must be either LIVE or DEAD");
        }			
        return newState;
    }
}

