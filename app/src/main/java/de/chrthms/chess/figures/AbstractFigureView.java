package de.chrthms.chess.figures;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import de.chrthms.chess.Chessboard;

/**
 * Created by christian on 31.12.16.
 */

public abstract class AbstractFigureView extends ImageView {

    private final int drawableId;
    private final int figureColor;

    private Drawable drawable;

    protected abstract int getDrawableId(int figureColor);

    public AbstractFigureView(Context context, int figureColor) {
        super(context);
        this.drawableId = getDrawableId(figureColor);
        this.figureColor = figureColor;
        init();
    }

    public AbstractFigureView(Context context, AttributeSet attrs, int figureColor) {
        super(context, attrs);
        this.drawableId = getDrawableId(figureColor);
        this.figureColor = figureColor;
        init();
    }

    public AbstractFigureView(Context context, AttributeSet attrs, int defStyleAttr, int figureColor) {
        super(context, attrs, defStyleAttr);
        this.drawableId = getDrawableId(figureColor);
        this.figureColor = figureColor;
        init();
    }

    private void init() {
        drawable = getResources().getDrawable(drawableId, null);
        setImageDrawable(drawable);
    }

    public int getFigureColor() {
        return figureColor;
    }


}

