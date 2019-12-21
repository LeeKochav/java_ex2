package dataStructure;

import java.io.Serializable;

public class Edge implements edge_data, Serializable {

    private node_data src;
    private node_data dst;
    private int tag;
    private double weight;
    private String info;

    public Edge(node_data src,node_data dst ,double weight)
    {
        this.src=src;
        this.dst=dst;
        this.setTag(1);
        this.weight=weight;
        this.setInfo("");
    }

    @Override
    public int getSrc() {
        return this.src.getKey();
    }

    @Override
    public int getDest() {
        return this.dst.getKey();
    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    @Override
    public String getInfo() {
        return new String("[src:"+this.src.toString()+" ,dst:"+this.dst.toString()+ ", weight:"+this.weight+"]");
    }
    @Override
    public String toString()
    {
        return this.getInfo();
    }

    @Override
    public void setInfo(String s) {
        this.info=new String(s);
    }

    @Override
    public int getTag() {
        return this.tag;
    }

    @Override
    public void setTag(int t) {
        if(t>=1&&t<=3) {
            this.tag = t;
        }

    }
}
