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

package de.chrthms.chess;

import java.util.List;

import de.chrthms.chess.board.FieldView;
import de.chrthms.chess.engine.core.Coord;

/**
 * The implementation of the chessboard must be stateless. Remember re-creating the board by the android system.
 *
 * @author Christian Thomas
 *
 */
public interface Chessboard {

    /**
     * Show figures of possible move-fields animated.
     *
     * @param possibleMoves
     */
    void showPossibleMoves(FieldView sourceFieldView, List<Coord> possibleMoves);

    /**
     * Hide figures of possible move-fields animated.
     *
     * @param possibleMoves
     */
    void hidePossibleMoves(FieldView sourceFieldView, List<Coord> possibleMoves);

    /**
     * Move figure from coord to coord animated.
     *
     * @param fromCoord
     * @param toCoord
     */
    void moveFigure(String fromCoord, String toCoord);

    /**
     * Move figure from coord to coord animated. After the figure animation the rook movement will also be animated
     *
     * @param fromCoord
     * @param toCoord
     * @param fromRookCoord
     * @param toRookCoord
     */
    void moveFigureCastling(String fromCoord, String toCoord, String fromRookCoord, String toRookCoord);

    /**
     * Hide figure for given coordinate animated.
     *
     * @param coord
     */
    void hideFigure(String coord);

    /**
     * Gives the according field for given coordinate.
     *
     * @param coord
     * @return
     */
    FieldView getFieldView(String coord);

    /**
     * Rotates the figures to the white side animated.
     */
    void rotateFiguresToWhiteSide();

    /**
     * Rotates the figures to the white side.
     *
     * @param withoutAnimation controls the animation
     */
    void rotateFiguresToWhiteSide(boolean withoutAnimation);

    /**
     * Rotates the figures to the white black animated.
     */
    void rotateFiguresToBlackSide();

    /**
     * Rotates the figures to the black side.
     *
     * @param withoutAnimation controls the animation
     */
    void rotateFiguresToBlackSide(boolean withoutAnimation);

}
