package dataStructure;

import java.io.*;
import java.util.Collection;
import java.util.LinkedHashMap;

public class DGraph implements graph , Serializable {

	private LinkedHashMap<Integer,node_data> nodes;
	private LinkedHashMap<Integer,LinkedHashMap<Integer,edge_data>> edges;
	private int numVer=0;
	private int numEdg=0;
	private  int change=0;

	public DGraph()
	{
		nodes =new LinkedHashMap<>();
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
		return this.nodes.get(key);
	}

	@Override
	public edge_data getEdge(int src, int dest) {
		if(this.edges.get(src)==null)return null;
		return this.edges.get(src).get(dest);
	}

	@Override
	public void addNode(node_data n) {
		if(n==null) throw new RuntimeException("Invalid input");
		this.nodes.put(n.getKey(),n);
		numVer++;
		change++;
	}

	@Override
	public void connect(int src, int dest, double w) {
		node_data s=this.nodes.get(src);
		node_data d=this.nodes.get(dest);
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
		else
		{
			throw new RuntimeException("Invalid source and destination nodes input");
		}
	}

	@Override
	public Collection<node_data> getV() {
		return this.nodes.values();
	}

	@Override
	public Collection<edge_data> getE(int node_id) {
		if(this.edges.get(node_id)==null)
			return null;
		return this.edges.get(node_id).values();
	}

	@Override
	public node_data removeNode(int key) {
		node_data rm=this.nodes.remove(key);
		if(rm!=null) {
			numVer--;
			change--;
			if(this.edges.get(key)!=null) {
				numEdg-=this.edges.get(key).size();
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

}
