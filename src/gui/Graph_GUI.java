package gui;

import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import utils.Point3D;
import utils.Range;
import utils.StdDraw;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Random;

public class Graph_GUI extends JFrame implements ActionListener, MouseListener{

    private graph graph;
    final int NODE_WIDTH_HEIGHT=10;

    public Graph_GUI(graph g){
        this.graph=g;
        initGui(1000,1000);

    }

    private void initGui(int width, int height) {
       this.setSize(width,height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MenuBar menuBar=new MenuBar();
        Menu menu=new Menu("Menu");
        this.setMenuBar(menuBar);
        menuBar.add(menu);
        MenuItem item1=new MenuItem("Save");
        item1.addActionListener(this);
        menu.add(item1);
        this.addMouseListener(this);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String str = e.getActionCommand();
        Graph_Algo algoGraph = new Graph_Algo();
        algoGraph.init(graph);
        if (str.equals("Save")) {
            try {
                algoGraph.save("file.txt");
                JOptionPane.showMessageDialog(this, "Graph saved to file.txt", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                System.out.println(ex.getCause());
                JOptionPane.showMessageDialog(this, "Eror bla bla", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    public void paint(Graphics g)
    {
        super.paint(g);
        Random rand=new Random();
        for (node_data node: graph.getV()) {
            g.setColor(Color.BLUE);
            double x = rand.nextInt((int)(this.getWidth()/1.5))+50;
            double y = rand.nextInt((int)(this.getHeight()/1.5))+70;
            Point3D p = new Point3D(x, y);
            node.setLocation(p);
            System.out.println(p.toString());
            g.fillOval( node.getLocation().ix(), node.getLocation().iy(), NODE_WIDTH_HEIGHT, NODE_WIDTH_HEIGHT);
            String id=node.getKey()+"";
            g.setFont(new Font("deafult", Font.BOLD,14));
            g.drawString(id, node.getLocation().ix()+3, node.getLocation().iy());
        }
        for (node_data node: graph.getV() ){
            if(graph.getE(node.getKey())!=null) {
                for (edge_data edge : graph.getE(node.getKey())) {
                    g.setColor(Color.RED);
                    g.setFont(new Font("deafult", Font.BOLD,14));
                    String weight = edge.getWeight() + "";
                    node_data dst = graph.getNode(edge.getDest());
                    g.drawLine(node.getLocation().ix(), node.getLocation().iy(), dst.getLocation().ix(), dst.getLocation().iy());
                    double dist = node.getLocation().distance2D(dst.getLocation());
                    g.drawString(weight, (int) ((node.getLocation().x() + dst.getLocation().x()) / 2), (int) ((node.getLocation().y() + dst.getLocation().y()) / 2));
                    g.setColor(Color.YELLOW);
                    int mid_x = ((node.getLocation().ix() + dst.getLocation().ix()) / 2);
                    int mid_y = ((node.getLocation().iy() + dst.getLocation().iy()) / 2);
                   int d_x=(((((mid_x+dst.getLocation().ix())/2)+dst.getLocation().ix())/2)+dst.getLocation().ix())/2;
                   int d_y=(((((mid_y+dst.getLocation().iy())/2)+dst.getLocation().iy())/2)+dst.getLocation().iy())/2;

                   g.fillOval(d_x-3,d_y-3,10,10);
                }
            }
            }
        }

    @Override
    public void mouseClicked(MouseEvent e) {


    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println(this.getLocation().toString());

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
    public static void main(String[] args) {
        DGraph g2 = new DGraph(4);
        g2.connect(0, 1, 10);
        g2.connect(1, 2, 20);
        g2.connect(2, 4, 30);
 //       g2.connect(4, 2, 40);
        g2.connect(2, 3, 1);
        g2.connect(3, 0, 2);
        Graph_GUI gu=new Graph_GUI(g2);

    }
}
