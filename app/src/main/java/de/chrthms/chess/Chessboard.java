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
import de.chrthms.chess.core.GameHandle;
import de.chrthms.chess.core.MoveOperation;
import de.chrthms.chess.engine.core.FigurePosition;
import de.chrthms.chess.figures.AbstractFigureView;

/**
 * Created by christian on 01.01.17.
 */
public interface Chessboard {

    void placeFigure(AbstractFigureView figure, String coord);

    boolean isMoveOperationAvailable();

    MoveOperation getAvailableMoveOperation();

    boolean startMoveOperation(FieldView fromField);

    boolean performMoveOperation(FieldView toField);

    void moveFigure(FieldView fromField, FieldView toField);
    void moveFigure(String fromCoord, String toCoord);

    void setGameHandle(GameHandle gameHandle);

    void prepareChessboard(List<FigurePosition> figurePositions);

}
