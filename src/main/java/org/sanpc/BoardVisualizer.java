package org.sanpc;

import org.sanpc.heuristics.LS.tabusearch.TabuSearch;
import org.sanpc.heuristics.greedy.Greedy;
import org.sanpc.heuristics.LS.twoopt.TwoOpt;
import org.sanpc.heuristics.PSO.PSO;
import org.sanpc.model.Route;
import org.sanpc.model.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class BoardVisualizer extends JPanel {
    private final int boardLength;
    private final int boardWidth;
    private final int k;
    private final int operationSize;
    private final int resetPointSize;

    private List<Point> operations;
    private List<Point> resetPoints;
    private List<Point> route;

    private double scale = 25.0;
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
        this.route = new ArrayList<>();

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
        this.route = new ArrayList<>();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = getGraphics2D((Graphics2D) g);

        drawCircle(g2d, 0, 0, Color.GREEN, new Point(-1, "Origin", 0, 0));

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

        if (!route.isEmpty()) {
            int startX = 0, startY = 0;
            Point firstPoint = route.getFirst();
            int firstX = (int) (firstPoint.getX() * scale);
            int firstY = (int) (firstPoint.getY() * scale);
            g2d.setColor(Color.ORANGE);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawLine(startX, startY, firstX, firstY);

            int consecutiveOs = 0;

            for (int i = 0; i < route.size(); i++) {
                Point currentPoint = route.get(i);
                String currentType = currentPoint.getType();

                if (i > 0) {
                    Point prevPoint = route.get(i-1);
                    int x1 = (int) (prevPoint.getX() * scale);
                    int y1 = (int) (prevPoint.getY() * scale);
                    int x2 = (int) (currentPoint.getX() * scale);
                    int y2 = (int) (currentPoint.getY() * scale);

                    g2d.setColor(calculateLineColor(consecutiveOs));
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawLine(x1, y1, x2, y2);

                    drawArrow(g2d, x1, y1, x2, y2);
                }

                consecutiveOs = currentType.equals("O") ? consecutiveOs + 1 : 0;
            }

            Point lastPoint = route.getLast();
            int lastX = (int) (lastPoint.getX() * scale);
            int lastY = (int) (lastPoint.getY() * scale);
            g2d.setColor(calculateLineColor(consecutiveOs));
            g2d.drawLine(lastX, lastY, startX, startY);
            drawArrow(g2d, lastX, lastY, startX, startY);
        }
    }

    private void drawArrow(Graphics2D g2d, int x1, int y1, int x2, int y2) {
        double angle = Math.atan2(y2 - y1, x2 - x1);
        int arrowSize = 15;

        int[] xPoints = {
                x2,
                x2 - (int) (arrowSize * Math.cos(angle - Math.PI / 6)),
                x2 - (int) (arrowSize * Math.cos(angle + Math.PI / 6))
        };
        int[] yPoints = {
                y2,
                y2 - (int) (arrowSize * Math.sin(angle - Math.PI / 6)),
                y2 - (int) (arrowSize * Math.sin(angle + Math.PI / 6))
        };

        g2d.fillPolygon(xPoints, yPoints, 3);
    }

    private Color calculateLineColor(int consecutiveOs) {
        int k = Constants.K;
        if (consecutiveOs > k) {
            return new Color(128, 0, 128);
        } else {
            int redIntensity = (int) (255 * ((float) consecutiveOs / k));
            return new Color(Math.min(redIntensity, 255), 0, 0);
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

        JPanel buttonPanel = getJPanel(visualizer);

        frame.setLayout(new BorderLayout());
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.add(visualizer, BorderLayout.CENTER);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setVisible(true);
    }

    private static JPanel getJPanel(BoardVisualizer visualizer) {
        JPanel buttonPanel = new JPanel();

        JButton psoButton = new JButton("PSO");
        psoButton.addActionListener(_ -> {
            PSO pso = new PSO(visualizer.operations);
            visualizer.setRoute(new Route(pso.optimize()).getPoints());
            visualizer.repaint();
        });

        JButton greedyButton = new JButton("Greedy");
        greedyButton.addActionListener(_ -> {
            visualizer.setRoute(Greedy.nearestNeighbor(visualizer.operations, visualizer.resetPoints));
            visualizer.repaint();
        });

        JButton tabuButton = new JButton("Tabu");
        tabuButton.addActionListener(_ -> {
            TabuSearch ts = new TabuSearch(visualizer.operations, visualizer.resetPoints, visualizer.route);
            visualizer.setRoute(new Route(ts.optimize()).getPoints());
            visualizer.repaint();
        });

        JButton twoOptButton = new JButton("2-opt");
        twoOptButton.addActionListener(_ -> {
            visualizer.setRoute(TwoOpt.apply2OptImprovement(visualizer.route));
            visualizer.repaint();
        });

        JButton rerollButton = new JButton("Reroll");
        rerollButton.addActionListener(_ -> visualizer.rerollBoard());

        buttonPanel.add(psoButton);
        buttonPanel.add(greedyButton);
        buttonPanel.add(twoOptButton);
        buttonPanel.add(tabuButton);
        buttonPanel.add(rerollButton);

        return buttonPanel;
    }

    public void setRoute(List<Point> route) {
        this.route = route;
    }

}
