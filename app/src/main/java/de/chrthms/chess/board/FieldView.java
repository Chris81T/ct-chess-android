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

package de.chrthms.chess.board;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import de.chrthms.chess.Chessboard;
import de.chrthms.chess.R;
import de.chrthms.chess.figures.AbstractFigureView;

/**
 * Created by christian on 01.01.17.
 */

public class FieldView extends FrameLayout {

    private Chessboard chessboard = null;

    private String coordStr = null;

    private AbstractFigureView figureView = null;

    public FieldView(Context context) {
        super(context);
        initField(context, null, 0);
    }

    public FieldView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initField(context, attrs, 0);
    }

    public FieldView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initField(context, attrs, defStyleAttr);
    }

    public FieldView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initField(context, attrs, defStyleAttr);
    }

    private void initField(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray array = context
                .getTheme()
                .obtainStyledAttributes(attrs, R.styleable.FieldView, 0, 0);

        try {
            coordStr = array.getString(R.styleable.FieldView_coord);

        } finally {
            array.recycle();
        }

    }

    public AbstractFigureView getFigureView() {
        return figureView;
    }

    public void setFigureView(AbstractFigureView figureView) {
        this.figureView = figureView;
    }

    public Chessboard getChessboard() {
        return chessboard;
    }

    public void setChessboard(Chessboard chessboard) {
        this.chessboard = chessboard;
    }

    public String getCoordStr() {
        return coordStr;
    }
}
