package com.example.Edmond2.drawing;



public class Node implements Comparable<Node> {
    final int name;
    final float x,y;
    public boolean visited = false; // used for Kosaraju's algorithm and Edmonds's algorithm

    public Node(final int argName,float x, float y) {
        name = argName;
        this.x = x;
        this.y = y;
    }
    
    protected float getX(){
    	return this.x;
    }
    protected float getY(){
    	return this.y;
    }
   
    public int compareTo(final Node argNode) {
        return argNode == this ? 0 : -1;
    }
}
