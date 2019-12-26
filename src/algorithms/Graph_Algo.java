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

	@Override
	public void init(graph g) {
		graphAlgo = g;
	}

	@Override
	public void init(String file_name) {

	    try {
            FileInputStream file = new FileInputStream(file_name);
            ObjectInputStream in = new ObjectInputStream(file);

            graphAlgo = (graph) in.readObject();

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

            out.writeObject(graphAlgo);

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
		for (node_data nodeSrc: graphAlgo.getV()) {
			for (node_data nodeDst: graphAlgo.getV()) {
					if (shortestPathDist(nodeSrc.getKey(), nodeDst.getKey()) == Double.MAX_VALUE)
						return false;
					 if (shortestPathDist(nodeDst.getKey(), nodeSrc.getKey()) == Double.MAX_VALUE)
						return false;
			}
		}
		return true;
	}

	@Override
	public double shortestPathDist(int src, int dest) {
		node_data srC=graphAlgo.getNode(src);
		node_data dsT=graphAlgo.getNode(dest);
		try {
			Collection<node_data> vertices = graphAlgo.getV();
			PriorityBlockingQueue<node_data> q = new PriorityBlockingQueue<>(graphAlgo.nodeSize(), new VER_COMP());
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
				Collection<edge_data> edgesTmp = graphAlgo.getE(tmp_src.getKey());
				if (edgesTmp != null) {
					for (edge_data edge : edgesTmp) {
						node_data tmp_dst = graphAlgo.getNode(edge.getDest());
						if (tmp_dst.getTag() != 3) {
							double tmp_w = tmp_src.getWeight() + edge.getWeight();
							if (tmp_dst.getWeight() > tmp_w) {
								tmp_dst.setWeight(tmp_w);
								tmp_dst.setInfo(tmp_src.getKey() + "");
								PriorityBlockingQueue<node_data> tmp_q = new PriorityBlockingQueue<>(graphAlgo.nodeSize(), new VER_COMP());
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
		while(graphAlgo.getNode(t).getKey()!=src)
		{
			if(graphAlgo.getNode(t).getInfo()==null)
				return null;
			int next=Integer.parseInt(graphAlgo.getNode(t).getInfo());
			res.add(graphAlgo.getNode(next));
			t=next;
		}
		res.add(graphAlgo.getNode(dest));
		res.sort(comp);
		return res;

	}


//	@Override
//	public List<node_data> TSP(List<Integer> targets) {
//		LinkedList<node_data> res=new LinkedList<node_data>();
//		for(int i=0; i<targets.size(); i++) {
//			for (node_data n: graphAlgo.getV()) {
//				n.setTag(1);
//			}
//			node_data srcCur = graphAlgo.getNode(targets.get(i)); //try
//			srcCur.setTag(3);
//			res.add(srcCur);
//			while(graphAlgo.getE(srcCur.getKey()).size()>0)
//			{
//				node_data next=null;
//				for (edge_data edge:graphAlgo.getE(srcCur.getKey())) {
//					next=graphAlgo.getNode(edge.getDest());
//					edge.setTag(3);
//					if(next.getTag()==3)
//						break;
//				}
//				if(next!=null) {
//					res.add(next);
//				}
//
//			}
//
//		}
//	}






	@Override
	public List<node_data> TSP(List<Integer> targets) {
			/*
		Since nodes IDS are increase by 1, when creating a new node, source always will be zero.
		 */
		Collections.sort(targets);
		LinkedList<node_data> ans=new LinkedList<node_data>();
		for(int j=0; j<targets.size(); j++) {
			ans.add(graphAlgo.getNode(targets.get(j)));
			List<node_data> tmp = null;
			int src = j;
			for (int i = 1; i < targets.size(); i++) {
				int dst = targets.get(i);
				if (!ans.contains(graphAlgo.getNode(dst))) {
					tmp = shortestPath(src, dst);
					if (tmp == null)  ans.clear();
					else {
						tmp.remove(graphAlgo.getNode(src));
						ans.addAll(tmp);
					}
					src = dst;
				}
			}
		}
		return ans;
	}

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
	public void print()
	{
		for(node_data node: this.graphAlgo.getV()) {
			System.out.println(node.toString());
			System.out.println("Edges:\n");
			System.out.println(this.graphAlgo.getE(node.getKey()));
		}
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
		Graph_Algo algo2 = new Graph_Algo();
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
		TMP.add(1);
		TMP.add(4);
		TMP.add(5);
		System.out.println(TMP);
		//TMP.add(5);
		System.out.println(algo.TSP(TMP));

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
	}
}
