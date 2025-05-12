package src;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.*;

class App extends JPanel implements Runnable, KeyListener{

    static final int COLUMNS = 10;
    static final int ROWS = 20;
    static final int TILE_SIZE = 30;
    static final int HEIGHT = ROWS * TILE_SIZE;
    static final int WIDTH = COLUMNS * TILE_SIZE;

    static int x = 4 * TILE_SIZE;
    static int y = 0;

    boolean running = false;
    boolean right = false;
    boolean left = false;
    boolean rotate_right = false;
    
    Thread gameThread;

    Board board = new Board();

    public void start_thread(){
        gameThread = new Thread(this);
        running = true;
        gameThread.start();
    }

    public void update(){
        if(!board.is_landed(x/TILE_SIZE, y/TILE_SIZE)){
            board.clear_puzzle(x/TILE_SIZE, y/TILE_SIZE);
            y += TILE_SIZE;
        }
        else{
            x = 4 * TILE_SIZE;
            y = 0;
            Puzzle.rotateState = 0;
            if(Board.puzzleCount == 6){
                Board.puzzleCount = 0;
                Board.puzzleQueue = Board.Random7bag();
            }else{
                Board.puzzleCount++;
            }
        }

        if(left && board.canMoveLeft(x/TILE_SIZE, y/TILE_SIZE)){
            board.clear_puzzle(x/TILE_SIZE, y/TILE_SIZE);
            x -= TILE_SIZE;
        }
        if(right && board.canMoveRight(x/TILE_SIZE, y/TILE_SIZE)){
            board.clear_puzzle(x/TILE_SIZE, y/TILE_SIZE);
            x += TILE_SIZE;
        }
        board.clear_puzzle(x/TILE_SIZE, y/TILE_SIZE);
        board.spawn_puzzle(x/TILE_SIZE, y/TILE_SIZE);
    }

    App(){
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(WIDTH,HEIGHT));
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);
        start_thread();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        //rysowanie siatki
        for(int i = 0; i < HEIGHT; i+=TILE_SIZE){
            for(int j = 0; j < WIDTH; j+=TILE_SIZE){
                g.drawRect(j, i, TILE_SIZE, TILE_SIZE);
            }
        }


        //malowanie planszy na podstawie Board.grid
        for(int i = 0; i < ROWS;i++){
            for(int j = 0; j < COLUMNS; j++){
                if(Board.grid[i][j] == 1){
                    g.setColor(Board.tile_color[i][j]);
                    g.fillRect(j*TILE_SIZE, i*TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
        }

    }

    @Override
    public void run(){
        while(running){
            update();
            repaint();
            try{
                Thread.sleep(100);
            }catch(Exception e){
                System.err.println("Katastrofa");
            }
        }
    }
    @Override
    public void keyPressed(KeyEvent e){
        int direction = e.getKeyCode();
        if(direction == KeyEvent.VK_RIGHT){
            right = true;
        }
        if(direction == KeyEvent.VK_LEFT){
            left = true;
        }
        if(direction == KeyEvent.VK_UP){
            rotate_right = true;
        }
    }
    @Override
    public void keyReleased(KeyEvent e){
        int direction = e.getKeyCode();
        if(direction == KeyEvent.VK_RIGHT){
            right = false;
        }
        if(direction == KeyEvent.VK_LEFT){
            left = false;
        }
        if(direction == KeyEvent.VK_UP){
            rotate_right = false;
        }
    }
    @Override
    public void keyTyped(KeyEvent e){

    }
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        App panel = new App();
        window.add(panel);
        window.pack();
        panel.requestFocus();
        //System.out.println(Puzzle.shape[1][0].length);

    }
}
