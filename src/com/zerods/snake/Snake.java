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
        // �ߵĳ�ʼλ���趨Ϊ����
        head = new Unit(GameFrame.COLS / 2, GameFrame.ROWS / 2, Direction.L);
        tail = head;
        this.gFrame = gFrame;
        size = 1;
    }
    public int getSize() {
        return size;
    }

    // ����������
    void draw(Graphics g) {
        move();
        for (Unit unit = head; unit != null; unit = unit.next) {
            unit.draw(g);
        }
    }
    
    // �õ���ǰ�ߵ�ͷ����С���ο飬�������ж��Ƿ�Ե���
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
    
    // ɾ��β���ڵ�
    private void deleteFromTail() {
        if (size == 0) {
            return;
        }
        tail = tail.prev;
        tail.next = null;
    }

    // ��ǰ�ƶ�, ��ɾ��β���ڵ㣬����ͷ�ڵ�
    private void move() {
        grow();
        deleteFromTail();
        checkDead();
    }

    // ������Ƿ������߽����������
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
        // ���ܳ��뵱ǰ�����෴�ķ����˶�
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
        // �ߵĻ�����ɵ�λ
        static final int WIDTH = GameFrame.BLOCK_SIZE;
        static final int HEIGHT = GameFrame.BLOCK_SIZE;
        // unit���Ͻ�����λ��
        int row, col;
        // ����
        Direction direction;
        // ǰ��ͺ���ĵ�λ������
        Unit next = null;
        Unit prev = null;

        Unit(int col, int row, Direction direction) {
            this.col = col;
            this.row = row;
            this.direction = direction;
        }

        // �Լ����Լ�
        void draw(Graphics g) {
            Color c = g.getColor();
            
            g.setColor(Color.BLACK);
            g.fillRect(GameFrame.BLOCK_SIZE * col, GameFrame.BLOCK_SIZE * row,
                    WIDTH, HEIGHT);
            
            g.setColor(c);
        }
    }
}
