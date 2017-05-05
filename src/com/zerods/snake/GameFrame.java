package com.zerods.snake;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameFrame extends Frame {
    private Snake snake = new Snake(this);
    private Egg egg = new Egg();
    private PaintThread paintThread = new PaintThread();
    private int score = 0;
    
    public static final int ROWS = 30;
    public static final int COLS = 30;
    public static final int BLOCK_SIZE = 20;
    // 定义游戏结束的字体
    private Font fontGameOver = new Font("宋体", Font.BOLD, 30);
    
    // 游戏是否结束的标志
    private boolean gameOver = false;
    
    public void launch() {
        this.setTitle("zerods的贪吃蛇游戏1.0");
        this.setSize(COLS * BLOCK_SIZE, ROWS * BLOCK_SIZE);
        // 窗口可见
        this.setVisible(true);
        // 增加窗口关闭时而不是关闭后的监听器
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        // 增加键盘监听器
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // 拿到键码
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_1) {
                    paintThread.reStart();
                } else if (key == KeyEvent.VK_2) {
                    stop();
                } else if (key == KeyEvent.VK_3) {
                    paintThread.pause = true;
                } /*else if (key == KeyEvent.VK_4) {
                    paintThread.gameOver();
                }*/ else {
                    snake.keyPressed(e);
                }
            }
        });
        
        // 重画线程启动
        new Thread(paintThread).start();
    }
    
    // 游戏结束
    public void stop() {
        gameOver = true;
    }
    
    private class PaintThread implements Runnable {
        public boolean running = true;
        public boolean pause = false;
        @Override
        public void run() {
            while (running) {
                // 刷新的频率
                if (pause) continue;
                else repaint();
                
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void reStart() {
            System.out.println("restart");
            this.pause = false;
            snake = new Snake(GameFrame.this);
            gameOver = false;
        }

        public void gameOver() {
            running = false;
        }
    }
    
   @Override
    public void paint(Graphics g) {
       Color color = g.getColor();
       
       // 画出背景色
       g.setColor(Color.WHITE);
       g.fillRect(0, 0, COLS * BLOCK_SIZE , ROWS * BLOCK_SIZE);
       // 画出框架线
       g.setColor(Color.GRAY);
       for (int i = 1; i < COLS; i++) {
           g.drawLine(i * BLOCK_SIZE, 0, i * BLOCK_SIZE, ROWS * BLOCK_SIZE);
       }
       for (int i = 1; i < ROWS; i++) {
           g.drawLine(0, i * BLOCK_SIZE, COLS * BLOCK_SIZE, i * BLOCK_SIZE);
       }
       // 返还画笔颜色
       g.setColor(color);
       
       // 画出得分的字符串
       g.setColor(Color.RED);
       g.drawString("score: " + getScore(), 10, 50);

       if (gameOver) {
           g.setFont(fontGameOver);
           g.drawString("游戏结束 你的得分是 " + getScore(), 120, 240);
           System.out.println("设置pause");
           paintThread.pause = true;
       }
//       g.setColor(c);
       
       // 画出蛇和蛋
       snake.draw(g);
       egg.draw(g);
       snake.eat(egg);
   }

    public int getScore() {
        return (snake.getSize() - 1) * 5;
    }

    public static void main(String[] args) {
        new GameFrame().launch();
    }
}
