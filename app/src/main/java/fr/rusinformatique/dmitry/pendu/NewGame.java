package fr.rusinformatique.dmitry.pendu;

import fr.rusinformatique.dmitry.pendu.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;



public class NewGame extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
        btnClick();

        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final View contentView = findViewById(R.id.fullscreen_content);

    }

    Button GO;

    public void btnClick() {
        GO = (Button) findViewById(R.id.GO);
        GO.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent game = new Intent(getApplicationContext(), Game.class);
                startActivity(game);
            }
        });

    };
}

