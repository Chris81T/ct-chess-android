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

package de.chrthms.chess.handles;


import de.chrthms.chess.board.FieldView;

/**
 * @author Christian Thomas
 */
public abstract class AbstractHumanGameHandle extends AbstractGameHandle {

    private FieldView threatenKingsField = null;

    protected void showKingsFieldMenace(int figureColor) {
        final String kingsCoord = chessEngine.getKingsField(handle, figureColor).getStrCoord();
        threatenKingsField = chessboard.getFieldView(kingsCoord);
        threatenKingsField.showMenace();
    }

    protected void hideMayKingsFieldMenace() {
        if (threatenKingsField != null) {
            threatenKingsField.hideMenace();
            threatenKingsField = null;
        }
    }

}
