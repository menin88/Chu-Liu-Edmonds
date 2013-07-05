package com.example.Edmond2.drawing;

import java.util.ArrayList;

import java.util.List;

import android.content.Context;

import android.graphics.Canvas;

import android.graphics.Color;

import android.graphics.Paint;

import android.graphics.Point;

import android.util.AttributeSet;
import android.util.Log;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//
public class GameBoard extends View implements OnTouchListener {

	private Paint paint;
	private List<Point> starField = null;
	private int starAlpha = 80;
	private int starFade = 2;
	private static final int NUM_OF_STARS = 25;
	
	private boolean costSetted = true;

	private EditText cost = null;
	private TextView arcName = null;

	private boolean equiDistanced = false;
	private List<Node> nodeList = null;
	private List<Edge> edgeList = null;
	private int counterNode = 0;
	private int counterEdge = 0;
	private float x;
	private float y;
	private final static float radius = 10;
	private boolean clear = false;
	private Node lastNode = null;
	private Edge lastEdge = null;
	private int distance;
	private Node selectedNode = null;
	private List<Object> lastAddedList = null;
	private Node fromNode = null;
	private Node toNode = null;
	private int weight = 0;

	public GameBoard(Context context, AttributeSet aSet) {
		super(context, aSet);
		// it's best not to create any new objects in the on draw
		// initialize them as class variables here
		paint = new Paint();
		lastAddedList = new ArrayList<Object>();
		this.setOnTouchListener(this);
		

	}

	

	protected void clearRect(Canvas canv) {
		canv.drawColor(Color.BLACK);
		nodeList = null;
		edgeList = null;
		lastAddedList = null;
		counterNode = 0;
		counterEdge = 0;
		cost.setText("");
		costSetted = true;
		cost.setVisibility(View.GONE);
		clear = false;
	}

	@Override
	synchronized public void onDraw(Canvas canvas) {
		// create a black canvas
		if (clear) {
			clearRect(canvas);
			return;
		}
		paint.setColor(Color.BLACK);
		paint.setAlpha(255);
		paint.setStrokeWidth(1);
		canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
		paint.setColor(Color.RED);

		// paint.setStrokeWidth(5);
		if (nodeList != null) {
			drawTouched(radius, canvas, paint);
			canvas.drawText("value of (x,y): (" + x + "," + y + ")", 10, 10,
					paint);
			canvas.drawText("#ofNode: " + counterNode, 10, 20, paint);// +
																		// " counterEdge #"
																		// +counterEdge
			canvas.drawText(" counterEdge #" + counterEdge, 10, 30, paint);
		}
		if (counterNode >= 2) {
			drawArc(canvas, paint);
		}
		if (nodeList != null) {
			drawLabels(canvas, paint);
		}
		//

	}

	public void drawTouched(float radius, Canvas canvas, Paint paint) {
		if (!nodeList.isEmpty()) {
			for (Node n : nodeList) {
				canvas.drawCircle(n.getX(), n.getY(), radius, paint);
			}
		} else {
			// Log.d(VIEW_LOG_TAG,"nodeList is empty");
			nodeList = null;
			return;
		}
		// lastNode = nodeList.get(counterNode-1);
	}

	public void drawLabels(Canvas canvas, Paint paint) {
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(10);
		paint.setTextSize(25);
		if (!nodeList.isEmpty()) {
			for (Node n : nodeList) {
				canvas.drawText("" + n.name, n.getX() + 5, n.getY() + 5, paint);
				// canvas.drawText("Size: "+nodeList.size(), 10, 40, paint);

			}
		} else {
			// Log.d(VIEW_LOG_TAG,"nodeList is empty");
			nodeList = null;
			paint.setColor(Color.RED);
			paint.setTextSize(10);
			return;
		}
		paint.setColor(Color.RED);
		paint.setTextSize(10);

	}

	public void drawArc(Canvas canvas, Paint paint) {
		
		paint.setStrokeWidth(5);
		// draw all the edges
		for (Edge e : edgeList) {
			paint.setColor(Color.GREEN);
			canvas.drawLine(e.from.getX(), e.from.getY(), e.to.getX(),
					e.to.getY(), paint);
			paint.setColor(Color.RED);
			paint.setTextSize(25);
			canvas.drawText(""+e.getWeight(), (e.from.x + e.to.x)/2, (e.from.y + e.to.y)/2, paint);
		}
		//lastEdge = edgeList.get(counterEdge - 1);// for each 2 nodes there is
													// one edge
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent e) {
		// TODO Auto-generated method stub
		if (e.getAction() == MotionEvent.ACTION_DOWN) {
			if (nodeList != null) {
				if(costSetted == true){
				Node nodeDetected = checkExistNode(e.getX(), e.getY());
				if (nodeDetected == null) { //it's a new node
					// add edge from last Node drawn

					x = e.getX();
					y = e.getY();
					Node n = new Node(counterNode, (int) x, (int) y);
					initializeNodeList(n);
					invalidate();
					 Log.d(VIEW_LOG_TAG, "NEW Node");
					// +" Distance: "+ distance+ " counterEdge #" +counterEdge);

					return true;
				} else {
					initializeNodeList(nodeDetected);
					Log.d(VIEW_LOG_TAG, "ALREADY EXIST NODE ");
					return false;
				}
				}else{
					//need to set the cost of the arc first to draw another arc or node
					//Toast.makeText(getContext(), "Set the arc cost first!", Toast.LENGTH_SHORT).show();
					return false;
				}
				
				//end if nodeList!=null
			}
			x = e.getX();
			y = e.getY();
			Node n = new Node(counterNode, (int) x, (int) y);
			lastAddedList = new ArrayList<Object>();
			initializeNodeList(n);
			
			invalidate();
			// Log.d(VIEW_LOG_TAG, "click #" +counterNode+" #" +counterEdge);
			
			return true;
		}
		else
			return false;
	}
	
	private void initializeNodeList(Node n) {

		if (nodeList == null) {
			nodeList = new ArrayList<Node>();
			nodeList.add(n);
			lastAddedList.add(n);
			counterNode = counterNode + 1;
			fromNode = n;
			Log.d(VIEW_LOG_TAG, "fromNode is setted");
		} else {
			
			//Log.d(VIEW_LOG_TAG, "INSIDE ELSE");
			if (!nodeList.contains(n)) {
				nodeList.add(n);
				lastAddedList.add(n);
				counterNode = counterNode + 1;
				if(fromNode == null){
					fromNode = n;
				}else{
					toNode = n;
					costSetted = false;
					//Log.d(VIEW_LOG_TAG, "toNode is Setted");
				}

				//Log.d(VIEW_LOG_TAG, "NOT PRESENT");
			} else {
				if(fromNode == null){
					fromNode = n;
					//Log.d(VIEW_LOG_TAG, "FromNode = existNode");
				}else{
					if(fromNode != n ){
					toNode = n;
					costSetted = false;
					//Log.d(VIEW_LOG_TAG, "FromNode = different from N");
					}
				}
				//Log.d(VIEW_LOG_TAG, "NODE ALREAY PRESENT");
			}
		}
		if (counterNode >= 2) { //costSetted
			Edge ed = null;
			if (edgeList == null) {
				edgeList = new ArrayList<Edge>();
				if(fromNode != null && toNode != null){
					ed = new Edge(fromNode, toNode);
					
					cost.setVisibility(View.VISIBLE);
						
						
						edgeList.add(ed);
						lastEdge = ed;
						lastAddedList.add(ed);
						counterEdge = counterEdge + 1;
						fromNode = null;
						toNode = null;
					
						
						
				}
				
		
			} else {

				if(fromNode != null && toNode != null){
					ed = checkExistEdge(new Edge(fromNode, toNode));
					
					if(ed != null){
						Toast.makeText(getContext(), "edge already set", Toast.LENGTH_SHORT).show();
					}else{
					ed = new Edge(fromNode, toNode);
					cost.setVisibility(View.VISIBLE);
					
					
					
					/*arcName.setText("("+ed.from.name+"-->"+ed.to.name+")");
					arcName.setVisibility(View.VISIBLE);*/
					edgeList.add(ed);
					lastAddedList.add(ed);
					lastEdge = ed;
					counterEdge = counterEdge + 1;
					fromNode = null;
					toNode = null;
					}
				}
				
			}
		}
	}
	public void setArc(){
	
		if(cost.getText().length() != 0){
			weight = Integer.parseInt(cost.getText().toString());
			lastEdge.setWeight(weight);
			costSetted = true;
			cost.setText("");
			//arcName.setText("");
			//arcName.setVisibility(View.GONE);
			cost.setVisibility(View.GONE);
		}else{
			Toast.makeText(getContext(), "Enter a value for arc-cost", Toast.LENGTH_LONG).show();
		}
		
	}
	/*
	 * public boolean checkExistNode(float x, float y){ distance = (int)
	 * Math.abs(lastNode.getX()- x); int d =
	 * (int)Math.sqrt((lastNode.getX()-x)*(
	 * lastNode.getX()-x)+((lastNode.getY()-y)*(lastNode.getY()-y))); if(d <=
	 * 30){ return false; } return true; }
	 */
	public Node checkExistNode(float x, float y) {
		// distance = (int) Math.abs(lastNode.getX()- x);
		// int distanceArray[] = new int[nodeList.size()];
		int minDistance = 0;
		Node nodeNull = null;
		for (Node n : nodeList) {
			minDistance = (int) Math.sqrt((n.getX() - x) * (n.getX() - x)
					+ ((n.getY() - y) * (n.getY() - y)));
			// Log.d(VIEW_LOG_TAG,"NodeLis numer of elements: "+nodeList.size());
			if (minDistance <= 30) {
				return n;
			}
			
		}
		return nodeNull;
	}
	public Edge checkExistEdge(Edge ed){
		for(Edge e: edgeList){
			if(e.from.x == ed.from.x && e.from.y == ed.from.y && e.to.x == ed.to.x && e.to.y == ed.to.y ){
				return e;
			}
		}
		return null;
		
	}

	public void eraseNode() {
		// TODO Auto-generated method stub
		if(lastAddedList != null){
			Log.d(VIEW_LOG_TAG,"lastAddList contains: " +lastAddedList.size());
		if(!lastAddedList.isEmpty()){
			int lastAddedPos = lastAddedList.size()-1;
			if(nodeList.contains(lastAddedList.get(lastAddedPos))){
			nodeList.remove(lastAddedList.get(lastAddedPos));
			fromNode = null;
			counterNode = counterNode - 1;
			lastAddedList.remove(lastAddedPos);
			}
			else if(edgeList.contains(lastAddedList.get(lastAddedPos))){
				//need to erase and check the cost value
				Edge e = (Edge)lastAddedList.get(lastAddedPos);
				//if the to-node is the last node added to nodeList
				//the to-node is a new node
				if(e.to == nodeList.get(nodeList.size() - 1)){
				//remove the new arc and the new node	
				edgeList.remove(lastAddedList.get(lastAddedPos));
				fromNode = nodeList.get(nodeList.size() - 2);
				nodeList.remove(nodeList.size() - 1);
				
				//remove 2 entries
				lastAddedList.remove(lastAddedPos);
				lastAddedList.remove(lastAddedPos - 1);
				counterNode = counterNode - 1;
				counterEdge = counterEdge - 1;
				cost.setVisibility(View.GONE);
				
				toNode = null;
				costSetted = true;
				}else{
					edgeList.remove(e);
					counterEdge = counterEdge - 1;
					lastAddedList.remove(lastAddedPos);
					cost.setVisibility(View.GONE);
					costSetted = true;
					fromNode = null;
					toNode = null;
				}
			}
	
		}
		}
	}

	public void addEdgeFromNode(int fromNode) {

	}

	synchronized public void resetCanvas() {
		clear = true;
	}

	synchronized public void setCost(EditText costField) {
		this.cost = costField;
	}

    public String getResult() {
        String tempResult = "";
        int i = 0;
        if(edgeList != null){
            for(Edge e: edgeList){
                tempResult = tempResult + i + " " + e.from.name + " " + e.to.name + " " +e.getWeight() + "\n";
                i ++;
            }
        }
        return tempResult;
    }

	synchronized public void setArcName(TextView text) {
		// TODO Auto-generated method stub
		this.arcName = text;
		
	}

    
}