package fr.rusinformatique.dmitry.pendu;


import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class Game extends Activity {

    //the mots
    private String[] mots;
    //random for word selection
    private Random rand;
    //store the current word
    private String motenCours;
    //the layout holding the answer
    private LinearLayout wordLayout;
    //text views for each letter in the answer
    private TextView[] charViews;
    //letter button grid
    private GridView letters;
    //letter button adapter
    private LetterAdapter ltrAdapt;
    //body part images
    private ImageView[] corpPendu;
    //total parts
    private int nbrPartie=10;
    //current part
    private int partAct;
    //num chars in word
    private int numChars;
    //num correct so far
    private int numCorr;
    //help
    private AlertDialog helpAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //read answer mots in
        Resources res = getResources();
        mots = res.getStringArray(R.array.words);

        //initialize random
        rand = new Random();
        //initialize word
        motenCours ="";

        //get answer area
        wordLayout = (LinearLayout)findViewById(R.id.word);

        //get letter button grid
        letters = (GridView)findViewById(R.id.letters);

        //get body part images
        corpPendu = new ImageView[nbrPartie];
        corpPendu[0] = (ImageView)findViewById(R.id.pendu1);
        corpPendu[1] = (ImageView)findViewById(R.id.pendu2);
        corpPendu[2] = (ImageView)findViewById(R.id.pendu3);
        corpPendu[3] = (ImageView)findViewById(R.id.pendu4);
        corpPendu[4] = (ImageView)findViewById(R.id.pendu5);
        corpPendu[5] = (ImageView)findViewById(R.id.pendu6);
        corpPendu[6] = (ImageView)findViewById(R.id.pendu7);
        corpPendu[7] = (ImageView)findViewById(R.id.pendu8);
        corpPendu[8] = (ImageView)findViewById(R.id.pendu9);
        corpPendu[9] = (ImageView)findViewById(R.id.pendu10);

        //set home as up
        getActionBar().setDisplayHomeAsUpEnabled(true);

        //start gameplay
        playGame();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_help:
                showHelp();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //play a new game
    private void playGame(){

        //choose a word
        String newWord = mots[rand.nextInt(mots.length)];
        //make sure not same word as last time
        while(newWord.equals(motenCours)) newWord = mots[rand.nextInt(mots.length)];
        //update current word
        motenCours = newWord;

        //create new array for character text views
        charViews = new TextView[motenCours.length()];

        //remove any existing letters
        wordLayout.removeAllViews();

        //loop through characters
        for(int c=0; c< motenCours.length(); c++){
            charViews[c] = new TextView(this);
            //set the current letter
            charViews[c].setText(""+ motenCours.charAt(c));
            //set layout
            charViews[c].setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));
            charViews[c].setGravity(Gravity.CENTER);
            charViews[c].setTextColor(Color.WHITE);
            charViews[c].setBackgroundResource(R.drawable.letter_bg);
            //add to display
            wordLayout.addView(charViews[c]);
        }

        //reset adapter
        ltrAdapt=new LetterAdapter(this);
        letters.setAdapter(ltrAdapt);

        //start part at zero
        partAct =0;
        //set word length and correct choices
        numChars= motenCours.length();
        numCorr=0;

        //hide all parts
        for(int p=0; p< nbrPartie; p++){
            corpPendu[p].setVisibility(View.INVISIBLE);
        }
    }

    //letter pressed method
    public void letterPressed(View view){
        //find out which letter was pressed
        String ltr=((TextView)view).getText().toString();
        char letterChar = ltr.charAt(0);
        //disable view
        view.setEnabled(false);
        view.setBackgroundResource(R.drawable.letter_down);
        //check if correct
        boolean correct=false;
        for(int k=0; k< motenCours.length(); k++){
            if(motenCours.charAt(k)==letterChar){
                correct=true;
                numCorr++;
                charViews[k].setTextColor(Color.BLACK);
            }
        }
        //check in case won
        if(correct){
            if(numCorr==numChars){
                //disable all buttons
                disableBtns();
                //let user know they have won, ask if they want to play again
                AlertDialog.Builder winBuild = new AlertDialog.Builder(this);
                winBuild.setTitle("OH!");
                winBuild.setMessage("T'as gagné\n\nLa reponse est bien:\n\n"+ motenCours);
                winBuild.setPositiveButton("Jouer encore",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Game.this.playGame();
                            }});
                winBuild.setNegativeButton("Quitter",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Game.this.finish();
                            }});
                winBuild.show();
            }
        }
        //check if user still has guesses
        else if(partAct < nbrPartie){
            //show next part
            corpPendu[partAct].setVisibility(View.VISIBLE);
            partAct++;
        }
        else{
            //user has lost
            disableBtns();
            //let the user know they lost, ask if they want to play again
            AlertDialog.Builder loseBuild = new AlertDialog.Builder(this);
            loseBuild.setTitle("AIE");
            loseBuild.setMessage("T'as perdu!\n\nLa reponse:\n\n"+ motenCours);
            loseBuild.setPositiveButton("Jouer encore",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Game.this.playGame();
                        }});
            loseBuild.setNegativeButton("Quitter",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Game.this.finish();
                        }});
            loseBuild.show();
        }
    }

    //disable letter buttons
    public void disableBtns(){
        int numLetters = letters.getChildCount();
        for(int l=0; l<numLetters; l++){
            letters.getChildAt(l).setEnabled(false);
        }
    }

    //show help information
    public void showHelp(){
        AlertDialog.Builder helpBuild = new AlertDialog.Builder(this);
        helpBuild.setTitle("Aide");
        helpBuild.setMessage("Trouves le mot en appuyant sur les lettres.\n\n"
                + "Tu as 9 essaies (normalement)");
        helpBuild.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        helpAlert.dismiss();
                    }});
        helpAlert = helpBuild.create();
        helpBuild.show();
    }

}