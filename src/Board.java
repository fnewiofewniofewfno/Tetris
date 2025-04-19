package test;

import java.util.*;
import java.awt.*;

class Board{
    static int[][] grid = new int[App.ROWS][App.COLUMNS];
    static Color[][] tile_color = new Color[App.ROWS][App.COLUMNS];
    public static int puzzleCount = 0;
    public static int rotateState = 0;

    public static int[] puzzleQueue = new int[7];

    public String print_board(){
        String gowno = "";
        for(int i = 0; i < App.ROWS; i++){
            for( int j = 0; j < App.COLUMNS; j++){
                gowno+=String.valueOf(grid[i][j]);
            }
            gowno+="\n";
        }
        return gowno;
    }

    public int[][] rotateMatrix(int tab[][]){
        
        int rows = tab.length;
        int col = tab[0].length;

        int [][] ret_tab = new int[col][rows];

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < col; j++){
                ret_tab[j][rows - i - 1] = tab[i][j];
            }
        }
        return ret_tab;
    }

    public void removeLine(){
        for(int i = 0; i < App.ROWS; i++){
            boolean remove = true;
            for(int j = 0; j < App.COLUMNS - 1; j++){
                if(grid[i][j] != grid[i][j + 1] || tile_color[i][j] != tile_color[i][j + 1] || grid[i][j] == 0){
                    remove = false;
                }
            }
            if(remove){
                for(int j = 0; j < App.COLUMNS; j++){
                    grid[i][j] = 0;
                }
                for(int k = i; k > 0; k--){
                    for(int j = 0; j < App.COLUMNS; j++){
                        grid[k][j] = grid[k - 1][j];
                        tile_color[k][j] = tile_color[k - 1][j];
                    }
                }

            }
        }
    }

    public void clear_puzzle(int current_x, int current_y){
        final int x = current_x;
        int puzzle = puzzleQueue[puzzleCount];
        for(int i = 0; i < Puzzle.shape[puzzle].length; i++){
            current_x = x;
            for(int j = 0; j < Puzzle.shape[puzzle][i].length; j++){
                if(Puzzle.shape[puzzle][i][j] == 1){
                    grid[current_y][current_x] = 0;
                }
                current_x++;
            }
            current_y++;
        }
    }
    
    public void spawn_puzzle(int current_x, int current_y){
        final int x = current_x;
        int puzzle = puzzleQueue[puzzleCount];
        for(int i = 0; i < Puzzle.shape[puzzle].length; i++){
            current_x = x;
            for(int j = 0; j < Puzzle.shape[puzzle][i].length; j++){
                if(Puzzle.shape[puzzle][i][j] == 1){
                        grid[current_y][current_x] = 1;
                        tile_color[current_y][current_x] = Puzzle.COLOR[puzzle];
                }
                current_x++;
            }
            current_y++;

        }
    }

    public boolean canMoveLeft(int x, int y){
        int current_puzzle = puzzleQueue[puzzleCount];
        if(x == 0){
            return false;
        }
        for(int i = 0; i < Puzzle.shape[current_puzzle].length; i++){
            int first_tile = 0;
            while(true){
                if(Puzzle.shape[current_puzzle][i][first_tile] == 1){
                    break;
                }
                first_tile++;
            }
            if(grid[y + i][x + first_tile - 1] == 1){
                return false;
            }

        }
        return true;
    }

    public boolean canMoveRight(int x, int y){
        int current_puzzle = puzzleQueue[puzzleCount];
        //sprawdzić czy if ponizej jest okej
        if(x + Puzzle.shape[current_puzzle][0].length >= App.COLUMNS){
            return false;
        }
        for(int i = 0; i < Puzzle.shape[current_puzzle].length; i++){
            int first_tile = Puzzle.shape[current_puzzle][0].length - 1;
            while(true){
                if(Puzzle.shape[current_puzzle][i][first_tile] == 1){
                    break;
                }
                first_tile--;
            }
            if(grid[y + i][x + first_tile + 1] == 1){
                return false;
            }

        }
        return true;
    }

    public boolean is_landed(int x, int y){
        int current_puzzle = puzzleQueue[puzzleCount];
        for(int i = 0; i < Puzzle.shape[current_puzzle][0].length; i++){
            int last_tile = Puzzle.shape[current_puzzle].length - 1;
            while (true) {
                if(Puzzle.shape[current_puzzle][last_tile][i] == 1){
                    break;
                }
                last_tile --;
            }
            if(y + last_tile >= App.ROWS - 1){
                return true;
            }
            if(grid[y + last_tile + 1][x + i] == 1){
                return true;
            }
        }
        return false;

    }

    

    public void rotateRight(int x, int y){
        int current_puzzle = puzzleQueue[puzzleCount];
        
        int[][] tab;

        tab = rotateMatrix(Puzzle.shape[current_puzzle]);

        if(current_puzzle == 3 || current_puzzle == 4){
            //stan 0 stopni dla klocka 3 i 4
            if(rotateState % 4 == 0){
                if(x + 3 > App.COLUMNS){
                    return;
                }

                for(int i = 0; i < tab.length; i++){
                    for(int j = 0; j < tab[0].length; j++){
                        if(grid[i][j] != 1){
                            grid[x + i][y + j] = tab[i][j];
                        }else{
                            return;
                        }
                    }
                }
                rotateState++;
            }
            //stan 90 stopni dla klocka 3 i 4
            if(rotateState % 4 == 1){
                if(y + 3 > App.ROWS){
                    return;
                }

                for(int i = 0; i < tab.length; i++){
                    for(int j = 0; j < tab[0].length; j++){
                        if(grid[i][j] != 1){
                            grid[x + i][y + j] = tab[i][j];
                        }else{
                            return;
                        }
                    }
                }
                App.x += App.TILE_SIZE;
                rotateState++;
            }
            if(rotateState % 4 == 2){
                if(x + 3 > App.COLUMNS){
                    return;
                }

                for(int i = 0; i < tab.length; i++){
                    for(int j = 0; j < tab[0].length; j++){
                        if(grid[i][j] != 1){
                            grid[x + i][y + j] = tab[i][j];
                        }else{
                            return;
                        }
                    }
                }
                App.y += App.TILE_SIZE;
                App.x -= App.TILE_SIZE;
                rotateState++;
            }
            if(rotateState % 4 == 3){
                if(y - 1 > App.ROWS){
                    return;
                }

                for(int i = 0; i < tab.length; i++){
                    for(int j = 0; j < tab[0].length; j++){
                        if(grid[i][j] != 1){
                            grid[x + i][y + j] = tab[i][j];
                        }else{
                            return;
                        }
                    }
                }
                rotateState++;
                App.y -= App.TILE_SIZE;
            }
        }
        
    }

    
    
    //obracanie klocków

    // public void rotateRight(int x, int y){
    //     int current_puzzle = puzzleQueue[puzzleCount];
    //     if(current_puzzle == 0){
    //         if(rotateState % 4 == 0){
    //             if(x >= 1 || x <= App.COLUMNS - 3){
    //                 if(grid[y + 1][x - 1] == 1 || grid[y + 1][x] == 1 || grid[y + 1][x + 1] == 1 || grid[y + 1][x + 2] == 1){
    //                     return;
    //                 }
    //                 App.x -= App.TILE_SIZE;
    //                 App.y += App.TILE_SIZE;
    //                 int[][] tab = {{1,1,1,1}};
    //                 Puzzle.shape[current_puzzle] = tab;
    //             }
    //             else{
    //                 return;
    //             }
    //         }
    //         if(rotateState % 4 == 1){
    //             if(y <= ){

    //             }
    //         }
    //     }
    //     if(current_puzzle == 2){
    //         return;
    //     }
        
    // }

    //losowanie klockow
    public static int[] Random7bag(){
        Random rand = new Random();
        int[] array = new int[7];
        array[0] = rand.nextInt(7);
        for(int i = 1; i < 7; i++){
            boolean is_unique = false;
            int number;
            while(!is_unique){
                number = rand.nextInt(7);
                for(int j = 0; j < i; j++){
                    if(number == array[j]){
                        j = -1;
                        number = rand.nextInt(7);
                    }
                }
                is_unique = true;
                array[i] = number;
            }
        }
        return array;
    }

    Board(){
        puzzleQueue = Random7bag();
    }

    public static void print2DArray(int[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                // drukuj wartość i spację
                System.out.print(array[i][j] + " ");
            }
            // po każdej linii wiersza przejdź do nowej linii
            System.out.println();
        }
    }

    public static void main(String[] args) {
        int[][] tab = new int[4][1];
        tab = Puzzle.shape[0];
        print2DArray(tab);
    
    }
    }