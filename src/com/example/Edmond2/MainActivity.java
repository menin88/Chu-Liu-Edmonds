package com.example.Edmond2;


import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;


import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.view.View;
import android.widget.*;
import com.example.Edmond2.drawing.DrawingActivity;

public class MainActivity extends Activity {
    public TableLayout tContainer = null;
    private List<TableRow> tableList = new ArrayList<TableRow>();
    public Button runButton = null;
    public Button addButton = null;
    public Button removeButton = null;
    public Button runGraph = null;
    private Button drawButton = null;
    public TextView tv = null;
    public TextView tv2 = null;
    public String mst = "";

    //private int MAX_WIDTH;
    //private int Max_EIGTH;
    private TableRow tb1,tb2;
    private int arcCounter;//tiene conto dei numero archi
    //filePicker
    private static final int REQUEST_CODE = 1234;
    private static final String CHOOSER_TITLE = "Select a file";
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //take control of the parent layout for the list
        context = getApplicationContext();
        initialize();//set all the variables

        setListeners();//set the listeners

    }

    private void initialize() {
        tContainer = (TableLayout) findViewById(R.id.tableContainer);
        tableList = new ArrayList<TableRow>();

        runButton = (Button) findViewById(R.id.find_mst);
        addButton = (Button) findViewById(R.id.add_list);
        removeButton = (Button) findViewById(R.id.remove);
        runGraph = (Button) findViewById(R.id.run_graph);
        drawButton = (Button) findViewById(R.id.drawer);
        tv = (TextView) findViewById(R.id.intro_text);
        tb1 = (TableRow)findViewById(R.id.tableRow);
        tb2 = (TableRow)findViewById(R.id.tableRow1);

        //add the first 2 tabletrow presented
        tableList.add(tb1);
        tableList.add(tb2);
        arcCounter = 2;

    }

    private void setListeners() {
        runGraph.setOnClickListener(runGraphListener);
        removeButton.setOnClickListener(removeListener);
        runButton.setOnClickListener(runListener);
        addButton.setOnClickListener(addListener);
        drawButton.setOnClickListener(drawListener);



    }


    private View.OnClickListener runListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            // TODO Auto-generated method stub

            edmondWorks();

        }
    };
    private View.OnClickListener addListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            // TODO Auto-generated method stub

            TableRow tr = new TableRow(context);

            for(int i=0;i< 4;i++){
                    EditText text = new EditText(context);
                    text.setBackgroundResource(R.layout.rounded_edittext);


                switch(i){
                    case 0:  text.setHint("arc");
                        text.setInputType(InputType.TYPE_CLASS_TEXT);
                        //text.setWidth(70);
                        break;
                    case 1:  text.setHint("f-node");
                        text.setInputType(InputType.TYPE_CLASS_NUMBER);
                        break;
                    case 2:  text.setHint("t-node");
                        text.setInputType(InputType.TYPE_CLASS_NUMBER);
                        break;
                    case 3:  text.setHint("cost");
                        text.setInputType(InputType.TYPE_CLASS_NUMBER);
                        break;
                    default: break;
                }

                tr.addView(text,i);
            }
            tContainer.addView(tr);
            tableList.add(tr);
            arcCounter = arcCounter +1; //update number of arcs
            tv.setText("Arc's number: " + arcCounter);

        }
    };


    private View.OnClickListener removeListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            // TODO Auto-generated method stub
            if (arcCounter != 0)   {
                removeList();
            tv.setText("Arc's number: " + arcCounter);
            }
            else {
                //do nothing
                tv.setText("no more elements, Add them");
            }
        }
    };

    public void removeList() {

        if(arcCounter > 1){
        tContainer.removeView(tableList.get(arcCounter-1));
        tableList.remove(arcCounter-1);
        arcCounter = arcCounter - 1;
            } else{
                 tv.setText("Please add arcs to the graph");
            }

        }

    private View.OnClickListener runGraphListener = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            run_Graph_Listener();
        }
    };

    public void edmondWorks() {
        String root = "1";
        Driver driver = new Driver(tv2, this.getApplicationContext());

        driver.readFile();
        //fillForm( driver.adj,root);
        Node n = driver.nodes.get(root);
        int partial = 0;

        AdjacencyList result = driver.ed.getMinBranching(n, driver.adj);
        mst = "The Edmond's MST is : \n";
        for (Edge e : result.getAllEdges()) {
            String out = " ( " + Integer.toString(e.from.name) + "-->" + Integer.toString(e.to.name) + " )";
            partial = partial + e.weight;

            mst = mst + out + "\n";
        }
        mst = mst + "The minmun sum of arcs is: " + partial + "\n";
        tv.setText(mst);


    }
    /*private void fillForm(AdjacencyList list,String root){
        for(Edge e: list.getAllEdges()){
            if(e.from.name == root){

            }
        }

    } */

    /*
     * */
    public void run_Graph_Listener() {
        boolean proceed = true;
        Driver d = new Driver(tv);
        String current = "";
        String temp = "";
        ArrayList<String> nodeList = new ArrayList<String>();
        //String root = "1";
        if (!tableList.isEmpty()) {
            for (TableRow tr : tableList) {
                for(int i=0; i < tr.getChildCount(); i++){
                    EditText et = (EditText)tr.getChildAt(i);
                    temp = et.getText().toString();
                    if( i == 1 || i == 2){
                        if(!nodeList.contains(temp)){
                        nodeList.add(temp);
                        }
                    }
                        if(i>=1){
                            current = current + " " +temp;
                            }else{
                            current = current + temp;
                             }

                }

                if (!d.parseMachine(current)){
                   proceed = false;
                }
                current = "";
            }
        }

        if (proceed) {
            EditText t = (EditText)findViewById(R.id.from_node);
            Node root = d.nodes.get(t.getText().toString());
            int partial = 0;
            if(isConnectedGraph(d.adj,root,nodeList)){
                AdjacencyList result = d.ed.getMinBranching(root, d.adj);
                mst = "The Edmond's MST is : \n";
                    for (Edge e : result.getAllEdges()) {
                    String out = " ( " + Integer.toString(e.from.name) + "-->" + Integer.toString(e.to.name) + " )";
                    partial = partial + e.weight;
                    //System.out.println(out);
                    mst = mst + out + "\n";
                    }
                // System.out.println(partial);
                mst = mst + "The minmun sum of arcs is: " + partial + "\n";
                tv.setText(mst);
                }else{
                    tv.setText("The graph is not connected");
                }
        } else {
            tv.setText("Insert Valid Value");

        }
    }

    private boolean isConnectedGraph(AdjacencyList list, Node root,ArrayList<String> nodeList){
        ArrayList<Node> visitedList = new ArrayList<Node>();
        AdjacencyList reversedList = list.getReversedList();
        int nodeNumber = 0;
        String text = "";

        mark(root,list,reversedList,visitedList);

        if(visitedList.size() == nodeList.size()){
               return true;
        }else{
            return false;
        }
    }

    private void mark(Node n, AdjacencyList list,AdjacencyList reversedList, ArrayList<Node> visitedList){
        if(!n.visited){
            n.visited = true;
            if(!visitedList.contains(n))   {
                visitedList.add(n);
                if(list.getAdjacent(n)!=null){
                    for(Edge e: list.getAdjacent(n)){
                        mark(e.to,list,reversedList,visitedList);

                    }
                }

            }
            if(reversedList.getAdjacent(n)!=null){
                for(Edge e: reversedList.getAdjacent(n)){
                    mark(e.to,list,reversedList,visitedList);
                }

            }
        }


    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("myString", mst);
    }

    private View.OnClickListener drawListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent drawIntent = new Intent(context,com.example.Edmond2.drawing.DrawingActivity.class);
            //Log.d("MainActivity","Initialized intent");
            startActivityForResult(drawIntent, 1);
            //startActivity(drawIntent);

        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {

            if(resultCode == RESULT_OK){
                String result = data.getStringExtra("RESULT");
                if(result.length() != 0)  {
                executeGraphFromDraw(result);
                fillTableRowsWithGraph(result);
                }
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }


    public void executeGraphFromDraw(String rawGraph){
        Driver d = new Driver(tv);
        int partial = 0;
        String delimiter = "\n";
        String[] temp ;
        temp = rawGraph.split(delimiter);        //temp now contains all lines
        for(int i = 0; i < temp.length; i++){
            d.parseMachine(temp[i]);
        }
        Node root = d.nodes.get("0");
        AdjacencyList result = d.ed.getMinBranching(root, d.adj);
        mst = "The Edmond's MST is : \n";
        for (Edge e : result.getAllEdges()) {
            String out = " ( " + Integer.toString(e.from.name) + "-->" + Integer.toString(e.to.name) + " )";
            partial = partial + e.weight;
            //System.out.println(out);
            mst = mst + out + "\n";
        }
        // System.out.println(partial);
        mst = mst + "The minmun sum of arcs is: " + partial + "\n";
        tv.setText(mst);
    }

    public void fillTableRowsWithGraph(String graph){

        EditText t = (EditText)findViewById(R.id.from_node);
        TableRow firstTable = (TableRow)findViewById(R.id.tableRow);
        TableRow secondTable = (TableRow)findViewById(R.id.tableRow1);
        TableRow tr;
        EditText text;
        String delimiter = "\n";
        String[] temp ;
        temp = graph.split(delimiter);
        String[] valueInLine;
        for(int i = 0; i < temp.length; i++){
            valueInLine  = temp[i].split(" ");

            for(int j=0; j < valueInLine.length; j++){
                if(i == 0)((EditText)firstTable.getChildAt(j)).setText(valueInLine[j]);
                if(i == 1)((EditText)secondTable.getChildAt(j)).setText(valueInLine[j]);

            }
            if(i>1){
                tr = new TableRow(context);

                for(int j=0; j < valueInLine.length; j++)     {
                    text = new EditText(context);
                    text.setBackgroundResource(R.layout.rounded_edittext);
                    switch(j){
                        case 0:  text.setHint("arc");
                            text.setInputType(InputType.TYPE_CLASS_TEXT);
                            text.setText(valueInLine[j]);
                            //text.setWidth(70);
                            break;
                        case 1:  text.setHint("f-node");
                            text.setInputType(InputType.TYPE_CLASS_NUMBER);
                            text.setText(valueInLine[j]);
                            break;
                        case 2:  text.setHint("t-node");
                            text.setInputType(InputType.TYPE_CLASS_NUMBER);
                            text.setText(valueInLine[j]);
                            break;
                        case 3:  text.setHint("cost");
                            text.setInputType(InputType.TYPE_CLASS_NUMBER);
                            text.setText(valueInLine[j]);
                            break;
                        default: break;
                    }

                    tr.addView(text,j);
                }
                //add the row
                tContainer.addView(tr);
                tableList.add(tr);
                arcCounter = arcCounter +1; //update number of arcs
                //tv.setText("Arc's number: " + arcCounter);

            }



        }
        }
            /*

            String valueInLine[];
            valueInLine  = temp[i].split(" ");

            tr = new TableRow(context);
            text = new EditText(context);
            for(int j=0; j < valueInLine.length; j++)     {
                if(i == 0)((EditText)firstTable.getChildAt(j)).setText(valueInLine[j]);
                if(i == 1)((EditText)secondTable.getChildAt(j)).setText(valueInLine[j]);
                if(i>1){

                    text.setBackgroundResource(R.layout.rounded_edittext);
                    switch(j){
                        case 0:  text.setHint("arc");
                            text.setInputType(InputType.TYPE_CLASS_TEXT);
                            text.setText(valueInLine[j]);
                            //text.setWidth(70);
                            break;
                        case 1:  text.setHint("f-node");
                            text.setInputType(InputType.TYPE_CLASS_NUMBER);
                            text.setText(valueInLine[j]);
                            break;
                        case 2:  text.setHint("t-node");
                            text.setInputType(InputType.TYPE_CLASS_NUMBER);
                            text.setText(valueInLine[j]);
                            break;
                        case 3:  text.setHint("cost");
                            text.setInputType(InputType.TYPE_CLASS_NUMBER);
                            text.setText(valueInLine[j]);
                            break;
                        default: break;
                    }

                    tr.addView(text,i);
                }
                }
            if(i>1){
                tContainer.addView(tr);
                tableList.add(tr);
                arcCounter = arcCounter +1; //update number of arcs
                tv.setText("Arc's number: " + arcCounter);
            }
            } */

        //}




    //endOfActivity
}
