package dataStructure;

import java.io.*;
import java.util.Collection;
import java.util.LinkedHashMap;

public class DGraph implements graph , Serializable {

	private LinkedHashMap<Integer,node_data> vertices;
	private LinkedHashMap<Integer,LinkedHashMap<Integer,edge_data>> edges;
	private int numVer=0;
	private int numEdg=0;
	private  int change=0;

	public DGraph()
	{
		vertices =new LinkedHashMap<>();
		edges=new LinkedHashMap<>();
	}
	public DGraph(int num) {
		this();
		for (int i = 0; i <= num; i++) {
			node_data new_node = new Node();
			addNode(new_node);
		}
	}

	@Override
	public node_data getNode(int key) {
		return this.vertices.get(key);
	}

	@Override
	public edge_data getEdge(int src, int dest) {
		return this.edges.get(src).get(dest);
	}

	@Override
	public void addNode(node_data n) {
		this.vertices.put(n.getKey(),n);
		numVer++;
		change++;
	}

	@Override
	public void connect(int src, int dest, double w) {
		node_data s=this.vertices.get(src);
		node_data d=this.vertices.get(dest);
		if(s!=null&&d!=null) {
			edge_data edge = new Edge(s, d, w);
			LinkedHashMap<Integer, edge_data> newConnection;
			if (edges.get(src) == null) {
				newConnection = new LinkedHashMap<Integer, edge_data>();
			} else {
				newConnection = edges.get(src);
			}
			newConnection.put(dest, edge);
			this.edges.put(src, newConnection);
			numEdg++;
			change++;
		}
	}

	@Override
	public Collection<node_data> getV() {
		return this.vertices.values();
	}

	@Override
	public Collection<edge_data> getE(int node_id) {
		if(this.edges.get(node_id)==null)
			return null;
		return this.edges.get(node_id).values();
	}

	@Override
	public node_data removeNode(int key) {
		node_data rm=this.vertices.remove(key);
		if(rm!=null) {
			numVer--;
			change--;
			if(this.edges.get(key)!=null) {
				numEdg-=this.edges.get(key).values().size();
				this.edges.remove(key);
			}
			for (LinkedHashMap<Integer, edge_data> tmp : this.edges.values()) {
				edge_data rm2 = tmp.remove(key);
				if (rm2 != null) {
					numEdg--;
				}
			}
		}
		return rm;
	}

	@Override
	public edge_data removeEdge(int src, int dest) {

		edge_data rm=this.edges.get(src).remove(dest);
		if(rm!=null)
		{
			numEdg--;
			change--;
		}
		return rm;
	}

	@Override
	public int nodeSize() {
		return this.numVer;
	}

	@Override
	public int edgeSize() {
		return this.numEdg;
	}
	public void print()
	{

		for(int keySrc:this.edges.keySet())
		{
			String ans="";
			ans+= "src:Node"+keySrc+"\nEdges:\n";
			ans += this.getE(keySrc).toString();
			System.out.println(ans);
			}

	}

	@Override
	public int getMC() {
		return this.change;
	}


	public static void main(String[] args) {
		Node n1=new Node();
		Node n2=new Node();
		Node n3=new Node();
		Node n4=new Node();
		DGraph g=new DGraph();
		g.addNode(n1);
		g.addNode(n2);
		g.addNode(n3);
		g.addNode(n4);
		g.connect(n1.getKey(),n2.getKey(),10);
		g.connect(n1.getKey(),n3.getKey(),10);
		g.connect(n2.getKey(),n3.getKey(),1);
		g.print();
		System.out.println(g.nodeSize());
		System.out.println(g.edgeSize());
		g.removeEdge(2,4);
		System.out.println("***********");
		g.print();
		System.out.println(g.getV().toString());
		System.out.println(g.nodeSize());
		System.out.println(g.edgeSize());
		//g.removeEdge(2,3);
		//g.print();
	}
}
