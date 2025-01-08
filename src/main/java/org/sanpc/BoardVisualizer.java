package org.sanpc;

import org.sanpc.model.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class BoardVisualizer extends JPanel {
    private final int boardLength;
    private final int boardWidth;
    private final int k;
    private final int operationSize;
    private final int resetPointSize;

    private List<Point> operations;
    private List<Point> resetPoints;

    private double scale = 30.0;
    private double offsetX = 40.0;
    private double offsetY = 40.0;
    private int dragStartX = 0;
    private int dragStartY = 0;

    public BoardVisualizer(Board board) {
        this.boardLength = board.getLength();
        this.boardWidth = board.getWidth();
        this.k = board.getK();
        this.operationSize = board.getOperationPoints().size();
        this.resetPointSize = board.getResetPoints().size();

        this.operations = board.getOperationPoints();
        this.resetPoints = board.getResetPoints();

        addMouseWheelListener(this::handleZoom);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragStartX = e.getX();
                dragStartY = e.getY();
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                offsetX += e.getX() - dragStartX;
                offsetY += e.getY() - dragStartY;
                dragStartX = e.getX();
                dragStartY = e.getY();
                repaint();
            }
        });
    }

    public void rerollBoard() {
        Board newBoard = new Board(boardLength, boardWidth, k, operationSize, resetPointSize);

        this.operations = newBoard.getOperationPoints();
        this.resetPoints = newBoard.getResetPoints();

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = getGraphics2D((Graphics2D) g);

        int x_o = (int) (0 * scale);
        int y_o = (int) (0 * scale);
        drawCircle(g2d, x_o, y_o, Color.GREEN, new Point(-1, "X", 0, 0));

        for (Point point : operations) {
            int x = (int) (point.getX() * scale);
            int y = (int) (point.getY() * scale);
            drawCircle(g2d, x, y, Color.BLUE, point);
        }
        for (Point reset : resetPoints) {
            int x = (int) (reset.getX() * scale);
            int y = (int) (reset.getY() * scale);
            drawCircle(g2d, x, y, Color.RED, reset);
        }
    }

    private Graphics2D getGraphics2D(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.translate(offsetX, offsetY);
        g.scale(scale / 50.0, scale / 50.0);

        g.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i <= boardWidth; i++) {
            int x = (int) (i * scale);
            g.drawLine(x, 0, x, (int) (boardLength * scale));
            g.drawString(String.valueOf(i), x + 5, -5);
        }
        for (int i = 0; i <= boardLength; i++) {
            int y = (int) (i * scale);
            g.drawLine(0, y, (int) (boardWidth * scale), y);
            g.drawString(String.valueOf(i), -20, y + 5);
        }
        return g;
    }

    private void drawCircle(Graphics2D g2d, int x, int y, Color color, Point point) {
        g2d.setColor(color);
        int diameter = (int) (scale / 2);
        g2d.fillOval(x - diameter / 2, y - diameter / 2, diameter, diameter);
        g2d.setColor(Color.BLACK);
        g2d.drawString("(" + point.getX() + "," + point.getY() + ")", x - diameter / 2, y - diameter / 2 - 5);
    }

    private void handleZoom(MouseWheelEvent e) {
        double delta = -e.getPreciseWheelRotation() * 5.0;
        double newScale = scale + delta;
        if (newScale >= 10.0 && newScale <= 200.0) {
            scale = newScale;
            repaint();
        }
    }

    public static void visualizeBoard(Board board) {
        JFrame frame = new JFrame("Board Visualizer");
        BoardVisualizer visualizer = new BoardVisualizer(board);

        JPanel buttonPanel = new JPanel();

        JButton connectButton = new JButton("Connect");

        buttonPanel.add(connectButton);

        frame.setLayout(new BorderLayout());
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.add(visualizer, BorderLayout.CENTER);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setVisible(true);
    }

}