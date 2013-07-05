package com.example.Edmond2;


public class Node implements Comparable<Node> {
    final int name;
    public boolean visited = false;

    public Node(final int argName) {
        name = argName;
    }

    public int compareTo(final Node argNode) {
        return argNode == this ? 0 : -1;
    }
}
