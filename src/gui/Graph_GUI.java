package gui;

import algorithms.Graph_Algo;
import com.sun.org.apache.xerces.internal.parsers.CachingParserPool;
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

public class Graph_GUI extends JFrame implements ActionListener {

    private graph graph;
    private Graph_Algo algoGraph;
    private  int mc;
    final int NODE_WIDTH_HEIGHT=10;

    public Graph_GUI(graph g)  {
        this.graph=g;
       algoGraph = new Graph_Algo();
        mc=g.getMC();
        initGui(1000, 1000);
    }
    public Graph_GUI()
    {
        this.graph=new DGraph();
        algoGraph=new Graph_Algo();
        mc=0;
        initGui(1000,1000);
    }

    private void initGui(int width, int height)  {
       this.setBounds(200,0,width,height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Graph_GUI");
        MenuBar menuBar=new MenuBar();
        Menu menu=new Menu("File");
        Menu menu2=new Menu("Algorithms");
        menu.setFont(new Font("deafult", Font.BOLD,12));
        menu2.setFont(new Font("deafult", Font.BOLD,12));
        this.setMenuBar(menuBar);
        menuBar.add(menu);
        menuBar.add(menu2);
        MenuItem itemAlgo1=new MenuItem("ShortestPathDist");
        itemAlgo1.setFont(new Font("deafult", Font.BOLD,12));
        itemAlgo1.addActionListener(this);
        MenuItem item1=new MenuItem("Save");
        item1.setFont(new Font("deafult", Font.BOLD,12));
        item1.addActionListener(this);
        menu.add(item1);
        menu2.add(itemAlgo1);
        MenuItem item2=new MenuItem("Load");
        item2.setFont(new Font("deafult", Font.BOLD,12));
        item2.addActionListener(this);
        menu.add(item2);
        setlocations();
        this.setVisible(true);
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    synchronized (graph) {
                        if (mc != graph.getMC()) {
                            mc = graph.getMC();
                            repaint();
                        }
                    }
                }
            }
        });
        t.start();
    }
    @Override
    public void actionPerformed(ActionEvent e) {

        String str = e.getActionCommand();
        if (str.equals("Save")) saveGraph();
        else if(str.equals("Load")) loadGraph();
        else if(str.equals("ShortestPathDist")) ShortestPathDistCalc();
    }
    public void paint(Graphics g)
    {
        super.paint(g);
        for (node_data node: graph.getV()) {
            g.setColor(Color.BLUE);
            g.fillOval( node.getLocation().ix()-3, node.getLocation().iy()-3, NODE_WIDTH_HEIGHT, NODE_WIDTH_HEIGHT);
            String id=node.getKey()+"";
            g.setFont(new Font("deafult", Font.BOLD,14));
            g.drawString(id, node.getLocation().ix()+3, node.getLocation().iy());
        }
        for (node_data node: graph.getV() ){
            if(graph.getE(node.getKey())!=null) {
                for (edge_data edge : graph.getE(node.getKey())) {
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("deafult", Font.BOLD,14));
                    String weight = edge.getWeight() + "";
                    node_data dst = graph.getNode(edge.getDest());
                    g.drawLine(node.getLocation().ix(), node.getLocation().iy(), dst.getLocation().ix(), dst.getLocation().iy());
                    double dist = node.getLocation().distance2D(dst.getLocation());
                    g.drawString(weight, (int) ((node.getLocation().x() + dst.getLocation().x()) / 2), (int) ((node.getLocation().y() + dst.getLocation().y()) / 2));
                    g.setColor(Color.RED);
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
            algoGraph.init(graph);
                    FileDialog fd = new FileDialog(this, "Save graph", FileDialog.SAVE);
                    fd.setFile("*.txt");
                    fd.setFilenameFilter(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            return name.endsWith(".txt");
                        }
                    });
                    fd.setVisible(true);
                    if(fd.getDirectory()!=null&&fd.getFile()!=null) {
                        algoGraph.save(fd.getDirectory() + fd.getFile());
                        JOptionPane.showMessageDialog(this, "Graph saved to " + fd.getFile(), "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(this, "File did not saved" , "ERROR", JOptionPane.ERROR_MESSAGE);

                    }
        }
        private void loadGraph()
        {
            algoGraph.init(graph);
            FileDialog fd = new FileDialog(this, "Open text file", FileDialog.LOAD);
            fd.setFile("*.txt");
            fd.setFilenameFilter(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".txt");
                }
            });
            fd.setVisible(true);
                algoGraph.init(fd.getDirectory() + fd.getFile());
                this.graph = algoGraph.copy();
                repaint();
        }
        private void ShortestPathDistCalc()
        {
            algoGraph.init(graph);
            JFrame j=new JFrame();
            j.setBounds(500,0,300,300);
            j.setLayout(new FlowLayout());
            j.setTitle("ShortestPathDistCalc");
            JButton send=new JButton("Submit");
            JTextField t1=new JTextField("Src",25);
            JTextField t2=new JTextField("Dst",25);
            j.add(t1);
            j.add(t2);
            j.add(send);
            j.setVisible(true);
            send.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int src=Integer.parseInt(t1.getText());
                    int dst=Integer.parseInt(t2.getText());
                    try {
                        double path = algoGraph.shortestPathDist(src, dst);
                        JOptionPane.showMessageDialog(j, "shortestPathDist is: " + path, "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
                    }
                    catch (RuntimeException ex)
                    {
                        JOptionPane.showMessageDialog(j, "shortestPathDist is: " + ex.getMessage(), "INFORMATION", JOptionPane.ERROR_MESSAGE);

                    }
                    finally {

                        j.setVisible(false);
                    }
                }
            });

        }
    public void setlocations() {
        Random rand = new Random();
        for (node_data node : graph.getV()) {
            double x = rand.nextInt((int) (this.getWidth() / 1.5)) + 50;
            double y = rand.nextInt((int) (this.getHeight() / 1.5)) + 70;
            Point3D p = new Point3D(x, y);
            node.setLocation(p);
        }
    }
    public static void main(String[] args) throws InterruptedException {
        DGraph g2 = new DGraph(4);
        g2.connect(0, 1, 10);
        g2.connect(1, 2, 20);
        g2.connect(2, 4, 30);
        g2.connect(4, 2, 40);
        g2.connect(2, 3, 1);
        g2.connect(3, 0, 2);
        Graph_GUI gu=new Graph_GUI(g2);
//        Thread.sleep(1000);
//        g2.removeNode(4);
//        Thread.sleep(1000);
//        g2.removeNode(1);
    }
}
