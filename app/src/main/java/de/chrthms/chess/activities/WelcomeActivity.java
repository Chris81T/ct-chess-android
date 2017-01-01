package de.chrthms.chess.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import de.chrthms.ct_chess_android.R;

public class WelcomeActivity extends AppCompatActivity {

    private FloatingActionButton welcomeFab = null;


    private void initFloatingActionButtion() {

        welcomeFab = (FloatingActionButton) findViewById(R.id.fab_welcome);

        welcomeFab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // first try
                final Intent intent = new Intent(WelcomeActivity.this, GameActivity.class);
                startActivity(intent);

            }

        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        initFloatingActionButtion();
    }


}
