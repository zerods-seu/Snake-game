package com.zerods.snake;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class Egg {
    private int col, row;
    private static final int width = GameFrame.BLOCK_SIZE;
    private static final int height = GameFrame.BLOCK_SIZE;
    // һ�����������
    private static Random r = new Random();
    private Color color = Color.pink;
    
    private Egg(int col, int row) {
        this.col = col;
        this.row = row;
    }
    
    public Egg() {
        this(r.nextInt(GameFrame.COLS), r.nextInt(GameFrame.ROWS - 2) + 2);
    }
    
    // ���³���
    void reAppear() {
        this.row = r.nextInt(GameFrame.ROWS - 2) + 2 ;
        this.col = r.nextInt(GameFrame.COLS);
    }
    
    // �õ��������ڵķ���
    Rectangle getRect() {
        return new Rectangle(GameFrame.BLOCK_SIZE * col,
                GameFrame.BLOCK_SIZE * row, width, height);
    }
    
    // ������
    void draw(Graphics g) {
        Color c = g.getColor();
        
        g.setColor(color);
        g.fillOval(GameFrame.BLOCK_SIZE * col,
                GameFrame.BLOCK_SIZE * row, width, height);
        g.setColor(c);
        // �л���ɫ
        if (color == Color.pink) {
            color = Color.YELLOW;
        } else {
            color = Color.pink;
        }
    }
}
