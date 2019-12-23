package gui;

import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import utils.Point3D;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Random;

public class Graph_GUI extends JFrame implements ActionListener{

    private graph graph;
    private Graph_Algo algoGraph;
    final int NODE_WIDTH_HEIGHT=10;

    public Graph_GUI(graph g){
        this.graph=g;
        initGui(1000,1000);

    }

    private void initGui(int width, int height) {
       this.setBounds(200,0,width,height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Graph_GUI");
        MenuBar menuBar=new MenuBar();
        Menu menu=new Menu("File");
        menu.setFont(new Font("deafult", Font.BOLD,12));
        this.setMenuBar(menuBar);
        menuBar.add(menu);
        MenuItem item1=new MenuItem("Save");
        item1.setFont(new Font("deafult", Font.BOLD,12));
        item1.addActionListener(this);
        menu.add(item1);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String str = e.getActionCommand();
        if (str.equals("Save")) saveGraph();
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

                   g.fillOval(d_x-3,d_y-3,NODE_WIDTH_HEIGHT,NODE_WIDTH_HEIGHT);
                }
            }
            }
        }

        private void saveGraph()
        {
            Graph_Algo algoGraph = new Graph_Algo();
            algoGraph.init(graph);
                    FileDialog fd = new FileDialog(this, "Open text file", FileDialog.SAVE);
                    fd.setFile("*.txt");
                    fd.setFilenameFilter(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            return name.endsWith(".txt");
                        }
                    });
                    fd.setVisible(true);
                    algoGraph.save(fd.getDirectory()+fd.getFile());
                    JOptionPane.showMessageDialog(this, "Graph saved to "+fd.getFile(), "INFORMATION", JOptionPane.INFORMATION_MESSAGE);


        }

    public static void main(String[] args) {
        DGraph g2 = new DGraph(50);
        g2.connect(0, 1, 10);
        g2.connect(1, 2, 20);
        g2.connect(2, 4, 30);
 //       g2.connect(4, 2, 40);
        g2.connect(2, 3, 1);
        g2.connect(3, 0, 2);
        Graph_GUI gu=new Graph_GUI(g2);

    }
}
