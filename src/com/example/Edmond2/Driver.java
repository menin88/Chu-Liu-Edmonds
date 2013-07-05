package com.example.Edmond2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.Integer;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.widget.TextView;

public class Driver {
    public AdjacencyList adj;
    public Edmond ed;
    public Map<String, Node> nodes;
    public int total;
    public TextView tv = null;
    public Context c = null;

    public Driver() {
        this.adj = new AdjacencyList();
        this.ed = new Edmond();
        this.nodes = new HashMap<String, Node>();
    }

    public Driver(TextView v) {
        this.adj = new AdjacencyList();
        this.ed = new Edmond();
        this.nodes = new HashMap<String, Node>();
        tv = v;

    }

    public Driver(TextView v, Context ctx) {
        this.adj = new AdjacencyList();
        this.ed = new Edmond();
        this.nodes = new HashMap<String, Node>();
        tv = v;
        c = ctx;
    }

    protected void readFile() {
        InputStream stream = null;
        BufferedReader br = null;
        try {
            stream = c.getResources().openRawResource(R.raw.testgraph);
            br = new BufferedReader(new InputStreamReader(stream));
            String currLine;

            while ((currLine = br.readLine()) != null) {
                parseMachine(currLine);

            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (br != null) {
            try {
                br.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    protected boolean parseMachine(String line) {

        String[] arr = line.split("\\s+"); //this groups all whitespaces as a delimiter
        String strValue = "";
        if (arr.length < 4) {
            return false;
        }
        String index = arr[0];
        String start = arr[1];

        Node source;
        if (this.nodes.containsKey(start)) {
            source = this.nodes.get(start);
        } else {

            source = new Node(Integer.parseInt(start));
            this.nodes.put(start, source);
        }
        String end = arr[2];
        Node dest = null;
        if (this.nodes.containsKey(end)) {
            dest = this.nodes.get(end);
        } else {
            dest = new Node(Integer.parseInt(end));
            this.nodes.put(end, dest);
        }
        int cost = Integer.parseInt(arr[3]);
        this.adj.addEdge(source, dest, cost);
        return true;
    }

}
