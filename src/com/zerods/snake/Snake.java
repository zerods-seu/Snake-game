package com.zerods.snake;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class Snake {
    private Unit head = null;
    private Unit tail = null;
    private int size = 0;
    private GameFrame gFrame;
    
    public Snake(GameFrame gFrame) {
        // 蛇的初始位置设定为中心
        head = new Unit(GameFrame.COLS / 2, GameFrame.ROWS / 2, Direction.L);
        tail = head;
        this.gFrame = gFrame;
        size = 1;
    }
    public int getSize() {
        return size;
    }

    // 画出这条蛇
    void draw(Graphics g) {
        move();
        for (Unit unit = head; unit != null; unit = unit.next) {
            unit.draw(g);
        }
    }
    
    // 得到当前蛇的头部的小矩形块，来帮助判断是否吃到蛋
    private Rectangle getRect() {
        return new Rectangle(head.col * GameFrame.BLOCK_SIZE,
                head.row * GameFrame.BLOCK_SIZE, Unit.WIDTH, Unit.HEIGHT);
    }
    
    public void eat(Egg e) {
        if (getRect().intersects(e.getRect())) {
            e.reAppear();
            this.grow();
            size++;
        }
    }
    
    private void grow() {
        Unit newHead = null;
        switch (head.direction) {
            case L:
                newHead = new Unit(head.col - 1, head.row, head.direction);
                break;
            case R:
                newHead = new Unit(head.col + 1, head.row, head.direction);
                break;
            case U:
                newHead = new Unit(head.col, head.row - 1, head.direction);
                break;
            case D:
                newHead = new Unit(head.col, head.row + 1, head.direction);
                break;
        }
        newHead.next = head;
        head.prev = newHead;
        head = newHead;
    }
    
    // 删除尾部节点
    private void deleteFromTail() {
        if (size == 0) {
            return;
        }
        tail = tail.prev;
        tail.next = null;
    }

    // 往前移动, 即删除尾部节点，增加头节点
    private void move() {
        grow();
        deleteFromTail();
        checkDead();
    }

    // 检查蛇是否碰到边界或碰到自身
    private void checkDead() {
        if (head.row < 3|| head.col < 0
                || head.row > GameFrame.ROWS - 3
                || head.col > GameFrame.COLS - 3) {
            gFrame.stop();
        }
        for (Unit u = head.next; u != null; u = u.next) {
            if (head.col == u.col && head.row == u.row) {
                gFrame.stop();
                System.out.println("here2");
            }
        }
    }
    
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
        // 不能朝与当前方向相反的方向运动
        case KeyEvent.VK_LEFT:
            if (head.direction != Direction.R) {
                head.direction = Direction.L;
            }
            break;
        case KeyEvent.VK_UP:
            if (head.direction != Direction.D) {
                head.direction = Direction.U;
            }
            break;
        case KeyEvent.VK_RIGHT:
            if (head.direction != Direction.L) {
                head.direction = Direction.R;
            }
            break;
        case KeyEvent.VK_DOWN:
            if (head.direction != Direction.U) {
                head.direction = Direction.D;
            }
            break;
        }
    }

    private class Unit {
        // 蛇的基本组成单位
        static final int WIDTH = GameFrame.BLOCK_SIZE;
        static final int HEIGHT = GameFrame.BLOCK_SIZE;
        // unit左上角所在位置
        int row, col;
        // 方向
        Direction direction;
        // 前面和后面的单位的引用
        Unit next = null;
        Unit prev = null;

        Unit(int col, int row, Direction direction) {
            this.col = col;
            this.row = row;
            this.direction = direction;
        }

        // 自己画自己
        void draw(Graphics g) {
            Color c = g.getColor();
            
            g.setColor(Color.BLACK);
            g.fillRect(GameFrame.BLOCK_SIZE * col, GameFrame.BLOCK_SIZE * row,
                    WIDTH, HEIGHT);
            
            g.setColor(c);
        }
    }
}
