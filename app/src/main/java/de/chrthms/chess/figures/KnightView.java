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
import android.util.AttributeSet;

import de.chrthms.chess.R;
import de.chrthms.chess.engine.core.constants.ColorType;
import de.chrthms.chess.engine.core.constants.FigureType;

import static de.chrthms.chess.R.drawable.*;

/**
 * Created by christian on 01.01.17.
 */

public class KnightView extends AbstractFigureView {

    public KnightView(Context context, int figureColor) {
        super(context, figureColor);
    }

    public KnightView(Context context, AttributeSet attrs, int figureColor) {
        super(context, attrs, figureColor);
    }

    public KnightView(Context context, AttributeSet attrs, int defStyleAttr, int figureColor) {
        super(context, attrs, defStyleAttr, figureColor);
    }

    @Override
    protected int getDrawableId(int figureColor) {
        return figureColor == ColorType.WHITE ? basic_knight_white: basic_knight_black;
    }

}
