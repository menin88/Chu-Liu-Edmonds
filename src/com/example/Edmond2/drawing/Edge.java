package com.example.Edmond2.drawing;


public class Edge implements Comparable<Edge> {
    Node from, to;
    private int weight;
   
    public Edge(Node argFrom, Node argTo){
    	this.from = argFrom;
        this.to = argTo;
    	
    }
    public Edge( Node argFrom,  Node argTo, int argWeight) {
        from = argFrom;
        to = argTo;
        weight = argWeight;
    }
    
    protected void setWeight(int w){
    	this.weight = w;
    }
    protected int getWeight(){
    	return this.weight;
    }
   
    public int compareTo(final Edge argEdge) {
        return weight - argEdge.weight;
    }
}
