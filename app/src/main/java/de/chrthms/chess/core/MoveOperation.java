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

package de.chrthms.chess.core;

import java.util.ArrayList;
import java.util.List;

import de.chrthms.chess.board.FieldView;

/**
 * Created by christian on 29.12.16.
 */

public class MoveOperation {

    public static final int STATE_NEW = 0;
    public static final int STATE_STARTED = 1;
    public static final int STATE_MOVE_TO_AND_COMPLETE = 2;

    private int state = STATE_NEW;

    private List<String> possibleMoves = new ArrayList<>();

    private FieldView sourceField = null;

    public List<String> getPossibleMoves() {
        return possibleMoves;
    }

    public void setPossibleMoves(List<String> possibleMoves) {
        this.possibleMoves = possibleMoves;
    }

    public FieldView getSourceField() {
        return sourceField;
    }

    public void setSourceField(FieldView sourceField) {
        this.sourceField = sourceField;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}

