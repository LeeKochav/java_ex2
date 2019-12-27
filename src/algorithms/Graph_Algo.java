package algorithms;

import java.io.*;
import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;
import dataStructure.*;


/**
 * This empty class represents the set of graph-theory algorithms
 * which should be implemented as part of Ex2 - Do edit this class.
 * @author
 *
 */
public class Graph_Algo implements graph_algorithms {

	private static final VER_COMP comp = new VER_COMP();
	private graph graphAlgo;

	public void Graph_Algo()
	{
		this.graphAlgo=new DGraph();
	}

	@Override
	public void init(graph g) {
		this.graphAlgo = g;
	}

	@Override
	public void init(String file_name) {

		try {
			FileInputStream file = new FileInputStream(file_name);
			ObjectInputStream in = new ObjectInputStream(file);

			this.graphAlgo = (graph) in.readObject();

			in.close();
			file.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void save(String file_name){

		try {
			FileOutputStream file = new FileOutputStream(file_name);
			ObjectOutputStream out = new ObjectOutputStream(file);

			out.writeObject(this.graphAlgo);

			out.close();
			file.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public boolean isConnected() {
		if(this.graphAlgo==null) return false;
		else if(this.graphAlgo.getV().size()==0) return false;
		for (node_data nodeSrc: this.graphAlgo.getV()) {
			for (node_data nodeDst: this.graphAlgo.getV()) {
				if (shortestPathDist(nodeSrc.getKey(), nodeDst.getKey()) == Double.MAX_VALUE)
					return false;
				if (shortestPathDist(nodeDst.getKey(), nodeSrc.getKey()) == Double.MAX_VALUE)
					return false;
			}
		}
		return true;
	}

	/*
	Shortest path algorithm using Dijkstra
	 */
	@Override
	public double shortestPathDist(int src, int dest) {
		try {
		node_data srC=this.graphAlgo.getNode(src);
		node_data dsT=this.graphAlgo.getNode(dest);
			Collection<node_data> vertices = this.graphAlgo.getV();
			PriorityBlockingQueue<node_data> q = new PriorityBlockingQueue<>(this.graphAlgo.nodeSize(), new VER_COMP());
			for (node_data node : vertices) {
				if (node.getKey() == src) {
					node.setWeight(0);
				} else {
					node.setWeight(Double.MAX_VALUE);
				}
				node.setTag(1);
				q.add(node);
			}
			while (!q.isEmpty() && dsT.getTag() != 3) {
				node_data tmp_src = q.poll();
				Collection<edge_data> edgesTmp = this.graphAlgo.getE(tmp_src.getKey());
				if (edgesTmp != null) {
					for (edge_data edge : edgesTmp) {
						node_data tmp_dst = this.graphAlgo.getNode(edge.getDest());
						if (tmp_dst.getTag() != 3) {
							double tmp_w = tmp_src.getWeight() + edge.getWeight();
							if (tmp_dst.getWeight() > tmp_w) {
								tmp_dst.setWeight(tmp_w);
								tmp_dst.setInfo(tmp_src.getKey() + "");
								PriorityBlockingQueue<node_data> tmp_q = new PriorityBlockingQueue<>(this.graphAlgo.nodeSize(), new VER_COMP());
								while (!q.isEmpty()) {
									tmp_q.add(q.poll());
								}
								q = tmp_q;
							}
						}
					}
				}
				tmp_src.setTag(3);
			}
			return dsT.getWeight();
		}
		catch (RuntimeException error)
		{
			throw  new RuntimeException("Invalid input for source and destination");
		}
	}

	@Override
	public List<node_data> shortestPath(int src, int dest) {
		shortestPathDist(src,dest);
		LinkedList<node_data> res=new LinkedList<>();
		int t=dest;
		while(this.graphAlgo.getNode(t).getKey()!=src)
		{
			if(this.graphAlgo.getNode(t).getInfo()==null)
				return null;
			int next=Integer.parseInt(this.graphAlgo.getNode(t).getInfo());
			res.add(this.graphAlgo.getNode(next));
			t=next;
		}
		res.add(this.graphAlgo.getNode(dest));
		res.sort(comp);
		return res;

	}

	/*
            Given a list of targets find if exist a path in the graph that travels targets list by the insertion order.
             */
	@Override
	public List<node_data> TSP(List<Integer> targets) {

		LinkedList<node_data> res=new LinkedList<node_data>();
		LinkedList<node_data> tmp;
		for(int i=0; i<targets.size()-1; i++)
		{
			tmp= (LinkedList<node_data>) shortestPath(targets.get(i),targets.get(i+1));
			if(tmp==null) return null;

			else if (tmp.size() > 1 && res.contains(graphAlgo.getNode(targets.get(i))))
				tmp.remove(graphAlgo.getNode(targets.get(i)));

			res.addAll(tmp);
		}
		return res;
	}

	/*
    Graph deep copy
     */
	@Override
	public graph copy() {
		graph g = new DGraph();
		Collection<node_data> vertices = this.graphAlgo.getV();
		for (node_data n : vertices)
			g.addNode(new Node(n));
		for (node_data n : vertices) {
			Collection<edge_data> edges = this.graphAlgo.getE(n.getKey());
			if(edges!=null) {
				for (edge_data e : edges) {
					g.connect(e.getSrc(), e.getDest(), e.getWeight());
				}
			}
		}
		return g;

	}

	public static void main(String[] args) throws Exception {

		DGraph g = new DGraph(6);
		g.connect(0, 5, 10);
		g.connect(0, 2, 20);
		g.connect(5, 1, 25);
		g.connect(5, 3, 7);
		g.connect(2, 3, 30);
		g.connect(1, 4, 4);
		g.connect(3, 4, 2);
		g.connect(1,6,1);
		g.connect(4,6,3);

		Graph_Algo algo = new Graph_Algo();
		algo.init(g);
//		algo.print();
//		graph m=algo.copy();
//		System.out.println("**************");
//		for(node_data node: m.getV()) {
//			System.out.println(node.toString());
//			System.out.println("Edges:\n");
//			System.out.println(m.getE(node.getKey()));
//		}
		LinkedList<Integer> TMP=new LinkedList<Integer>();
		TMP.add(3);
		TMP.add(5);
		//	TMP.add(4);
		TMP.add(6);
		//TMP.add(5);
	//	System.out.println(algo.TSP(TMP));

//		DGraph g2 = new DGraph(4);
//		g2.connect(0, 1, 10);
//		g2.connect(1, 2, 20);
//		g2.connect(2, 4, 30);
//		//g2.connect(4, 2, 40);
//		g2.connect(2, 3, 1);
//		g2.connect(3, 0, 2);
//		algo.init(g2);
//		algo.save("file.txt");
//		algo2.init("file.txt");
//		algo2.print();
		//System.out.println(algo.isConnected());
		Graph_Algo algo2 = new Graph_Algo();
		System.out.println(algo2.isConnected());
	}
}
