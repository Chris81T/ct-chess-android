/*
 *    ct-chess-android, a chess android ui app playing chess games.
 *    Copyright (C) 2016-2017 Christian Thomas
 *
 *    This program ct-chess-android is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.chrthms.chess.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import de.chrthms.chess.R;

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

        Log.i("WELCOME_ACTIVITY", "Create and prepare relevant components");

        initFloatingActionButtion();
    }


}
