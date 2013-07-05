package com.example.Edmond2;


import java.util.ArrayList;

public class Edmond {
    private ArrayList<Node> cycle;
    private ArrayList<ArrayList<Node>> cycles;
    private boolean gotCycle = false;

    public AdjacencyList getMinBranching(Node root, AdjacencyList list) {

        AdjacencyList reversed = list.getReversedList();
        // STEP1a: remove all edges entering the root
        if (reversed.getAdjacent(root) != null) {
            reversed.getAdjacent(root).clear();
        }
        AdjacencyList outEdges = new AdjacencyList();
        // STEP1b: for each node, select the edge entering it with smallest weight
        for (Node n : reversed.getSourceNodeSet()) {
            ArrayList<Edge> inEdges = reversed.getAdjacent(n);
            if (inEdges.size() == 0) {
                continue;
            }

            Edge min = inEdges.get(0);
            for (Edge e : inEdges) {
                // I check if it remains the minimum
                if (e.weight < min.weight) {
                    min = e;
                }
            }
            //reverse again all the edges back to the original graph
            outEdges.addEdge(min.to, min.from, min.weight);
        }

        // STEP2: detect cycles
        hasCycles(root, outEdges);

        // STEP3/STEP4: resolve each cycle formed
        AdjacencyList outEdgesReverse = outEdges.getReversedList();

        for (int i = 0; i < cycles.size(); i++) {
            if (cycles.get(i).contains(root)) {
                continue;
            }
            mergeCycles(cycles.get(i), list, reversed, outEdges,
                    outEdgesReverse);
        }
        while (gotCycle) {

            for (Node n : outEdges.getSourceNodeSet()) {
                n.visited = false;
            }
            gotCycle = false;
            // STEP2: detect new cycles
            hasCycles(root, outEdges);
            // STEP3/STEP4: resolve each cycle formed
            AdjacencyList outEdgesReverse1 = outEdges.getReversedList();

            for (int i = 0; i < cycles.size(); i++) {

                if (cycles.get(i).contains(root)) {
                    continue;
                }
                mergeCycles(cycles.get(i), list, reversed, outEdges,
                        outEdgesReverse1);
            }
        }
        //the MST returned
        return outEdges;
    }

    // find recursively all the cycle
    private void getCycle(Node n, AdjacencyList outEdges) {
        n.visited = true;
        cycle.add(n);
        if (outEdges.getAdjacent(n) == null) {
            return;
        }
        for (Edge e : outEdges.getAdjacent(n)) {
            if (!e.to.visited) {
                getCycle(e.to, outEdges);
            }
        }
    }

    private void mergeCycles(ArrayList<Node> cycle, AdjacencyList list,
                             AdjacencyList reverse, AdjacencyList outEdges,
                             AdjacencyList outEdgesReverse) {

        // collect all the minimum edges inside each cycles
        ArrayList<Edge> cycleAllInEdges = new ArrayList<Edge>();
        Edge minInternalEdge = null;
        // find the minimum internal edge weight
        for (Node n : cycle) {
            for (Edge e : reverse.getAdjacent(n)) {
                if (cycle.contains(e.to)) {
                    if (minInternalEdge == null
                            || minInternalEdge.weight > e.weight) {
                        minInternalEdge = e;
                        continue;
                    }
                } else {
                    cycleAllInEdges.add(e);
                }
            }
        }
        // find the incoming edge with minimum modified cost
        Edge minExternalEdge = null;
        int minModifiedWeight = 0;
        for (Edge e : cycleAllInEdges) {
            int w = e.weight
                    - (outEdgesReverse.getAdjacent(e.from).get(0).weight - minInternalEdge.weight);
            if (minExternalEdge == null || minModifiedWeight > w) {
                minExternalEdge = e;
                minModifiedWeight = w;
            }
        }
        // STEP4: add the incoming edge and remove the inner-circuit incoming edge
        Edge removing = outEdgesReverse.getAdjacent(minExternalEdge.from)
                .get(0);
        outEdgesReverse.getAdjacent(minExternalEdge.from).clear();
        outEdgesReverse.addEdge(minExternalEdge.to, minExternalEdge.from,
                minExternalEdge.weight);
        ArrayList<Edge> adj = outEdges.getAdjacent(removing.to);
        for (int i = 0; i < adj.size(); i++) {
            if (adj.get(i).to == removing.from) {
                adj.remove(i);
                break;
            }
        }
        outEdges.addEdge(minExternalEdge.to, minExternalEdge.from,
                minExternalEdge.weight);
    }

    private void hasCycles(Node root, AdjacencyList outEdges) {
        cycles = new ArrayList<ArrayList<Node>>();
        cycle = new ArrayList<Node>();
        //just mark visited
        // all the set of nodes with root inside
        getCycle(root, outEdges);
        cycles.add(cycle);
        for (Node n : outEdges.getSourceNodeSet()) {
            if (!n.visited) {
                cycle = new ArrayList<Node>();
                getCycle(n, outEdges);
                cycles.add(cycle);
            }
        }
        for (int i = 0; i < cycles.size(); i++) {
            // avoid to check if the connected component with root has a cycle (obviously it doesn't)
            if (cycles.get(i).contains(root)) {
                continue;
            }
            checkCycle(cycles.get(i), outEdges);

        }

    }

    private void checkCycle(ArrayList<Node> cycle, AdjacencyList outEdges) {
        // TODO Auto-generated method stub
        Node firstNode = null;
        Node lastNode;
        if (cycle.get(0) != null)
            firstNode = cycle.get(0);
        for (Node n : cycle) {
            if (outEdges.getAdjacent(n) != null) {
                for (Edge e : outEdges.getAdjacent(n)) {
                    lastNode = e.to;
                    if (firstNode == lastNode) {
                        gotCycle = true;
                    }
                }
            }
        }

    }
}
