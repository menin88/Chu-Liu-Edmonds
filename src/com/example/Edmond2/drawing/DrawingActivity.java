package com.example.Edmond2.drawing;


import android.content.Intent;


import android.os.Bundle;

import android.os.Handler;

import android.app.Activity;

import android.view.View;

import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.Edmond2.R;

//implements OnClickListener
public class DrawingActivity extends Activity {


        private Handler frame = new Handler();
        private GameBoard canvas = null;
        public EditText costField = null;
        private Button resetButton = null;
        private Button backButton = null;
        private Button done = null;
        private Button resolver = null;
        public TextView arcName = null;
        public String result = "";
        //Divide the frame by 1000 to calculate how many times per second the screen will update.

        private static final int FRAME_RATE = 30; //50 frames per second

        @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

            setContentView(R.layout.maindrawing);

        Handler h = new Handler();
        //initialize graphics element
        canvas = (GameBoard)findViewById(R.id.the_canvas);
        resetButton = (Button)findViewById(R.id.the_button);
        backButton = (Button)findViewById(R.id.back);
        costField = (EditText)findViewById(R.id.cost);
        done = (Button)findViewById(R.id.done);
        resolver = (Button)findViewById(R.id.resolve);

        arcName = (TextView)findViewById(R.id.arc);
        //set listeners
        resetButton.setOnClickListener(reset);
        backButton.setOnClickListener(goBack);
        done.setOnClickListener(setArcCost);
        resolver.setOnClickListener(resolveGraph);
        //We can't initialize the graphics immediately because the layout manager

        //needs to run first, thus we call back in a sec.

        h.postDelayed(new Runnable() {

                        @Override

                        public void run() {

                                initGfx(3);
                               

                        }

         }, 1000);

    }


    synchronized public void initGfx(int i) {
    	if(i==0){
        canvas.resetCanvas();
    	}
    	if(i==1){
    		  canvas.eraseNode();       
    	}
    	    resetButton.setEnabled(true);
    	    backButton.setEnabled(true);
    	 done.setEnabled(true);
            resolver.setEnabled(true);
    	 
    	 //canvas.setArcName(arcName);
    	 canvas.setCost(costField);
        //It's a good idea to remove any existing callbacks to keep

        //them from inadvertently stacking up.

        frame.removeCallbacks(frameUpdate);

        frame.postDelayed(frameUpdate, FRAME_RATE);

    }

     public OnClickListener reset= new OnClickListener(){
    	
    	@Override
    	public void onClick(View view){
    		 initGfx(0);
    	}
    	
    };
 public OnClickListener goBack= new OnClickListener(){
    	
    	@Override
    	public void onClick(View view){
    		 initGfx(1);
    	}
    	
    };
public OnClickListener setArcCost= new OnClickListener(){
    	
    	@Override
    	public void onClick(View view){
    		canvas.setArc();
    		//Toast.makeText(getBaseContext(), "Arc weight setted", Toast.LENGTH_LONG).show();
    	}
    	
    };

    public OnClickListener resolveGraph= new OnClickListener(){

        @Override
        public void onClick(View view){
            result = canvas.getResult();
            Intent returnIntent = new Intent();
            returnIntent.putExtra("RESULT",result);
            setResult(RESULT_OK,returnIntent);
            finish();
        }

    };
    /*@Override
    synchronized public void onClick(View v) {

             initGfx();

    }*/

    private Runnable frameUpdate = new Runnable() {

          @Override

          synchronized public void run() {

                  frame.removeCallbacks(frameUpdate);

                  //make any updates to on screen objects here

                  //then invoke the on draw by invalidating the canvas

                  canvas.invalidate();

                  frame.postDelayed(frameUpdate, FRAME_RATE);

          }

     };

}