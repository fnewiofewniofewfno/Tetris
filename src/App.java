package src;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import src.Board.RotationResult;

import java.util.*;

class App extends JPanel implements Runnable, KeyListener{

    private JLabel gameOverLabel;
    private JButton restart;

    static final int COLUMNS = 10;
    static final int ROWS = 20;
    static final int TILE_SIZE = 30;
    static final int HEIGHT = ROWS * TILE_SIZE;
    static final int WIDTH = COLUMNS * TILE_SIZE;

    static int x = 4 * TILE_SIZE;
    static int y = 0;
    
    int fallSpeed = 600;

    boolean running = false;
    boolean right = false;
    boolean left = false;
    boolean down = false;
    boolean rotate_right = false;
    boolean gameOver = false;
    
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
            if(gameOver()){
                gameOver = true;
                gameOverLabel.setVisible(true);
                restart.setVisible(true);
                x = 4 * TILE_SIZE;
                y = 0;

            }else{
            
            board.removeLine();
            x = 4 * TILE_SIZE;
            y = 0;}
            board.resetMatrix(Board.puzzleQueue[Board.puzzleCount],Puzzle.rotateState);
            Puzzle.rotateState = 0;
            if(Board.puzzleCount == 6){
                Board.puzzleCount = 0;
                Board.puzzleQueue = Board.Random7bag();
            }else{
                Board.puzzleCount++;
            }
        
        }

        if(down == true && board.is_landed(x/TILE_SIZE, y/TILE_SIZE) == false){
            y+=TILE_SIZE;
        }

        if (rotate_right) {
            RotationResult result = board.canRotate(x / TILE_SIZE, y / TILE_SIZE);
            if (result.success()) {
                int new_x = result.x() * TILE_SIZE;
                int new_y = result.y() * TILE_SIZE;
                int current_puzzle = result.current_puzzle();
                Puzzle.shape[current_puzzle] = result.new_shape();
                x = new_x;
                y = new_y;
                Puzzle.rotateState++;
                rotate_right = false;
            }
        }
        if(left && board.canMoveLeft(x/TILE_SIZE, y/TILE_SIZE)){
            board.clear_puzzle(x/TILE_SIZE, y/TILE_SIZE);
            x -= TILE_SIZE;
            left = false;
        }
        if(right && board.canMoveRight(x/TILE_SIZE, y/TILE_SIZE)){
            board.clear_puzzle(x/TILE_SIZE, y/TILE_SIZE);
            x += TILE_SIZE;
            right = false;
        }
        board.clear_puzzle(x/TILE_SIZE, y/TILE_SIZE);
        board.spawn_puzzle(x/TILE_SIZE, y/TILE_SIZE);
    }

    App(){
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        setPreferredSize(new Dimension(WIDTH,HEIGHT));
        requestFocusInWindow();
        start_thread();
        
        gameOverLabel = new JLabel("GAME OVER");
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 48));
        gameOverLabel.setForeground(Color.magenta);
        int labelWidth = 300, labelHeight = 60;
        int x = (COLUMNS * TILE_SIZE - labelWidth) / 2;
        int y = (HEIGHT - labelHeight) / 2;
        gameOverLabel.setBounds(x, y, labelWidth, labelHeight);
        gameOverLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gameOverLabel.setVisible(false);  // na razie niewidoczny
        add(gameOverLabel);

        restart = new JButton("RESTART");

        restart.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Board.clear_grid();
            gameOver = false;
            gameOverLabel.setVisible(false);
            restart.setVisible(false);
        }
        });

        restart.setFont(new Font("Arial", Font.BOLD, 30));
        restart.setBounds(x, 2*y, labelWidth, labelHeight);
        restart.setBackground(Color.magenta);
        restart.setForeground(Color.WHITE);
        restart.setVisible(false);
        add(restart);

    }

    public boolean gameOver(){
        for(int i = 0; i < Puzzle.shape[Board.puzzleQueue[Board.puzzleCount]].length; i++){
            for( int j =0;j<Puzzle.shape[Board.puzzleQueue[Board.puzzleCount]][0].length;j++){
                if(board.grid[0 + i][4 + j] == 1){
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        //grid drawing
        if(!gameOver){
            for(int i = 0; i < HEIGHT; i+=TILE_SIZE){
                for(int j = 0; j < WIDTH; j+=TILE_SIZE){
                    g.drawRect(j, i, TILE_SIZE, TILE_SIZE);
                }
            }
        }


        //tetermino drawing
       if(!gameOver){
             for(int i = 0; i < ROWS;i++){
                 for(int j = 0; j < COLUMNS; j++){
                   if(Board.grid[i][j] == 1){
                      g.setColor(Board.tile_color[i][j]);
                      g.fillRect(j*TILE_SIZE, i*TILE_SIZE, TILE_SIZE, TILE_SIZE);
                   }
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
                Thread.sleep(fallSpeed);
            }catch(Exception e){
                System.err.println("Katastrofa");
            }
        }
    }
    @Override
    public void keyPressed(KeyEvent e){
        int keyCode  = e.getKeyCode();
        if(keyCode  == KeyEvent.VK_RIGHT){
            right = true;
        }
        if(keyCode  == KeyEvent.VK_LEFT){
            left = true;
        }
        if(keyCode  == KeyEvent.VK_DOWN){
            fallSpeed = 100;
        }
        if(keyCode  == KeyEvent.VK_UP){
            rotate_right = true;
        }

    }
    @Override
    public void keyReleased(KeyEvent e){
        int keyCode = e.getKeyCode();
        if(keyCode  == KeyEvent.VK_DOWN){
            fallSpeed = 600;
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
        // panel.setLayout(new GridLayout(2,1));
        window.add(panel);
        window.pack();
        panel.requestFocus();
        panel.requestFocusInWindow();
    }
}
