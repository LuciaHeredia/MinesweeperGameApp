package com.example.minesweeper.Logic;

import java.util.Random;

class Generator {
    static int[][] generate(int bomb, final int width, final int height){
        Random r = new Random();

        int [][] grid = new int[width][height];
        for(int x=0; x<width; x++){
            grid[x] = new int[height];
        }

        while(bomb>0){  // where to put the bombs
            int x = r.nextInt(width);
            int y = r.nextInt(height);

            if(grid[x][y] != -1) { // -1 = bomb
                grid[x][y] = -1;
                bomb--;
            }
        }
        grid = calcNeighbours(grid,width,height);
        return grid;
    }

    private static int[][] calcNeighbours(int[][] grid, final int width, final int height) {
        for(int x=0; x<width; x++){
            for(int y=0; y<height; y++){
                grid[x][y] = getNeighboursNumber(grid,x,y,width,height);
            }
        }
        return grid;
    }

    private static int getNeighboursNumber(int[][] grid, final int x, final int y, final int width, final int height) {
        if(grid[x][y] == -1)
            return -1;

        int count = 0;

        if(isMineAt(grid, x-1, y+1, width, height)) // top-left
            count ++;
        if(isMineAt(grid, x, y+1, width, height)) // top
            count ++;
        if(isMineAt(grid, x+1, y+1, width, height)) // top-right
            count ++;
        if(isMineAt(grid, x-1, y, width, height)) //left
            count ++;
        if(isMineAt(grid, x+1, y, width, height)) // right
            count ++;
        if(isMineAt(grid, x-1, y-1, width, height)) //bottom-left
            count ++;
        if(isMineAt(grid, x, y-1, width, height)) //bottom
            count ++;
        if(isMineAt(grid, x+1, y-1, width, height)) // bottom-right
            count ++;

        return count;
    }

    private static boolean isMineAt( final int [][] grid, final int x, final int y, final int width, final int height) {
        if(x >= 0 && y >=0 && x < width && y < height)
            return grid[x][y] == -1;   // -1 = bomb
        return false;
    }

}
