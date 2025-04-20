package src;

import java.util.*;
import java.awt.*;

class Board{
    static int[][] grid = new int[App.ROWS][App.COLUMNS];
    static Color[][] tile_color = new Color[App.ROWS][App.COLUMNS];
    public static int puzzleCount = 0;
    public static int rotateState = 0;
    public static int rot_x;
    public static int rot_y;

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

    public boolean canRotate(int x, int y){
        int current_puzzle = puzzleQueue[puzzleCount];
        switch (current_puzzle) {
            
            case 2:
                rot_x = x;
                rot_y = y;
                return true;

            case 3:
            case 4:
                if(rotateState % 4 == 0){
                        if(x + 3 > App.COLUMNS){
                            rotateState++;
                            rot_x = x;
                            rot_y = y;
                            return true;
                        }else{
                            return false;
                        }
                }
                if(rotateState % 4 == 1){
                    if(y + 3 > App.ROWS){
                        x++;
                        rotateState++;
                        rot_x = x;
                        rot_y = y;
                        return true;
                    }
                }
                if(rotateState % 4 == 2){
                    if(x - 1 < 0){
                        x--;
                        y++;
                        rotateState++;
                        rot_x = x;
                        rot_y = y;
                        return true;
                    }
                }
                if(rotateState % 4 == 3){
                    if(y - 1 < 0){
                        y--;
                        rotateState++;
                        rot_x = x;
                        rot_y = y;
                        return true;
                    }
                }
                break;
                case 6:
                case 5:
                case 1:
                if(rotateState % 4 == 3){
                    if(x + 3 > App.COLUMNS){
                        rotateState++;
                        rot_x = x;
                        rot_y = y;
                        return true;
                    }else{
                        return false;
                    }
            }
            if(rotateState % 4 == 2){
                if(y + 3 > App.ROWS){
                    x++;
                    rotateState++;
                    rot_x = x;
                    rot_y = y;
                    return true;
                }
            }
            if(rotateState % 4 == 1){
                if(x - 1 < 0){
                    x--;
                    y++;
                    rotateState++;
                    return true;
                }
            }
            if(rotateState % 4 == 0){
                if(y - 1 < 0){
                    y--;
                    rotateState++;
                    return true;
                }
            }
            break;
            case 0:
        if(rotateState % 2 == 0){
            if(x - 1 < 0 || x + 3 > App.COLUMNS){
                return false;
            }
            y++;
            x--;
            rotateState++;
            rot_x = x;
            rot_y = y;
            return true;
        }
        if(rotateState % 2 == 1){
            if(y + 3 > App.ROWS || y - 1 < 0){
                return false;
            }
            x++;
            y--;
            rotateState++;
            rot_x = x;
            rot_y = y;
            return true;
        }
        break;

        }        
        return false;
    }

    public void rotateRight(int x, int y){
        int current_puzzle = puzzleQueue[puzzleCount];
        if(!canRotate(x, y)){
            return;
        }
        for(int i = 0; i < Puzzle.shape[current_puzzle].length; i++){
            for(int j = 0; j < Puzzle.shape[current_puzzle][0].length; j++){
                grid[i + y][j + x] = 0;
            }
        }
        int[][] tab;
        tab = rotateMatrix(Puzzle.shape[current_puzzle]);
        
        for(int i = 0; i < tab.length; i++){
            for(int j = 0; j < tab[0].length; j++){
                if(grid[rot_y + i][rot_x + j] == 1){
                    for(int k = 0; k < Puzzle.shape[current_puzzle].length; k++){
                        for(int l = 0; l < Puzzle.shape[current_puzzle][0].length; l++){
                            grid[k + y][l + x] = 1;
                        }
                    }
                    return;
                }
            }
        }
        for(int i = 0; i < tab.length; i++){
            for(int j = 0; j < tab[0].length; j++){
                grid[rot_y + i][rot_x + j] = 1;
                }
            }
            App.x = rot_x * App.TILE_SIZE;
            App.y = rot_y * App.TILE_SIZE;
        }

    

    // public void rotateRight(int x, int y){
    //     int current_puzzle = puzzleQueue[puzzleCount];
        
    //     int[][] tab;

    //     tab = rotateMatrix(Puzzle.shape[current_puzzle]);

    //     if(current_puzzle == 3 || current_puzzle == 4){
    //         //stan 0 stopni dla klocka 3 i 4
    //         if(rotateState % 4 == 0){
    //             if(x + 3 > App.COLUMNS){
    //                 return;
    //             }

    //             for(int i = 0; i < tab.length; i++){
    //                 for(int j = 0; j < tab[0].length; j++){
    //                     if(grid[i][j] != 1){
    //                         grid[x + i][y + j] = tab[i][j];
    //                     }else{
    //                         return;
    //                     }
    //                 }
    //             }
    //             rotateState++;
    //         }
    //         //stan 90 stopni dla klocka 3 i 4
    //         if(rotateState % 4 == 1){
    //             if(y + 3 > App.ROWS){
    //                 return;
    //             }

    //             for(int i = 0; i < tab.length; i++){
    //                 for(int j = 0; j < tab[0].length; j++){
    //                     if(grid[i][j] != 1){
    //                         grid[x + i][y + j] = tab[i][j];
    //                     }else{
    //                         return;
    //                     }
    //                 }
    //             }
    //             App.x += App.TILE_SIZE;
    //             rotateState++;
    //         }
    //         if(rotateState % 4 == 2){
    //             if(x + 3 > App.COLUMNS){
    //                 return;
    //             }

    //             for(int i = 0; i < tab.length; i++){
    //                 for(int j = 0; j < tab[0].length; j++){
    //                     if(grid[i][j] != 1){
    //                         grid[x + i][y + j] = tab[i][j];
    //                     }else{
    //                         return;
    //                     }
    //                 }
    //             }
    //             App.y += App.TILE_SIZE;
    //             App.x -= App.TILE_SIZE;
    //             rotateState++;
    //         }
    //         if(rotateState % 4 == 3){
    //             if(y - 1 > App.ROWS){
    //                 return;
    //             }

    //             for(int i = 0; i < tab.length; i++){
    //                 for(int j = 0; j < tab[0].length; j++){
    //                     if(grid[i][j] != 1){
    //                         grid[x + i][y + j] = tab[i][j];
    //                     }else{
    //                         return;
    //                     }
    //                 }
    //             }
    //             rotateState++;
    //             App.y -= App.TILE_SIZE;
    //         }
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



    public static void main(String[] args) {

    
    }
    }
