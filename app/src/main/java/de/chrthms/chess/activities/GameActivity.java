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

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import de.chrthms.chess.Chessboard;
import de.chrthms.chess.engine.ChessEngine;
import de.chrthms.chess.engine.core.Handle;
import de.chrthms.chess.engine.impl.ChessEngineBuilder;
import de.chrthms.chess.R;

public class GameActivity extends AppCompatActivity {

    private final ChessEngine chessEngine = ChessEngineBuilder.build();

    private Chessboard chessboard = null;

    // TODO first start to get the board running ... Must be refactored ;-)
    private void prepareBoard() {

        final Handle handle = chessEngine.newGame();
        Log.i("GAME_ACTIVITY", "PrepareBoard --> Created handle = " + handle);

        chessboard.prepareChessboard(chessEngine.getFigurePositions(handle));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        chessboard = (Chessboard) findViewById(R.id.chessboard);

        prepareBoard();
    }


}
