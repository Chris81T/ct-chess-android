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

package de.chrthms.chess.board.markers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import de.chrthms.chess.R;

/**
 * Created by christian on 30.12.16.
 */
public class SourceFieldView extends View {

    private static final int STROKE_WIDTH = 10;

    public SourceFieldView(Context context) {
        super(context);
    }

    public SourceFieldView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SourceFieldView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SourceFieldView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setColor(ContextCompat.getColor(getContext(), R.color.basicFieldPossibleColor));
        paint.setStrokeWidth((getWidth() * 1f) / 100 * STROKE_WIDTH);

        float[] points = {
                getLeft(), getTop(), getRight(), getTop(),
                getRight(), getTop(), getRight(), getBottom(),
                getRight(), getBottom(), getLeft() , getBottom(),
                getLeft(), getBottom(), getLeft(), getTop()
        };

        canvas.drawLines(points, paint);

    }
}
