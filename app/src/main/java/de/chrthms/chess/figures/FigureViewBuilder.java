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

package de.chrthms.chess.figures;

import android.content.Context;
import android.util.Log;

import de.chrthms.chess.engine.core.constants.FigureType;

/**
 * Created by christian on 01.01.17.
 */

public abstract class FigureViewBuilder {

    public static AbstractFigureView createFigureView(Context context, int figureType, int figureColor) {

        switch (figureType) {
            case FigureType.BISHOP: return new BishopView(context, figureColor);
            case FigureType.KING:   return new KingView(context, figureColor);
            case FigureType.KNIGHT: return new KnightView(context, figureColor);
            case FigureType.PAWN:   return new PawnView(context, figureColor);
            case FigureType.QUEEN:  return new QueenView(context, figureColor);
            case FigureType.ROOK:   return new RookView(context, figureColor);
        }

        Log.e("FIGUREVIEW_BUILDER", "Invalid figureType = " + figureType + " given!");
        return null;
    }

}
