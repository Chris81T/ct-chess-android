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

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.RelativeLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.chrthms.chess.Chessboard;
import de.chrthms.chess.R;
import de.chrthms.chess.engine.ChessEngine;
import de.chrthms.chess.engine.core.Coord;
import de.chrthms.chess.engine.core.FigurePosition;
import de.chrthms.chess.engine.impl.ChessEngineBuilder;
import de.chrthms.chess.figures.AbstractFigureView;
import de.chrthms.chess.figures.FigureViewBuilder;

/**
 * Created by christian on 05.01.17.
 */
public class ChessboardView extends RelativeLayout implements Chessboard {

    private static final int ANIMATION_MOVE_FIGURE_DURATION = 250;
    private static final float ANIMATION_MOVE_FIGURE_RAISE_UP_DOWN = 2f;

    private Map<String, FieldView> fields = new HashMap<>();

    private GridLayout chessboardGrid;

    private final ChessEngine chessEngine = ChessEngineBuilder.build();

    public ChessboardView(Context context) {
        super(context);
        initChessboard();
    }

    public ChessboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initChessboard();
    }

    public ChessboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initChessboard();
    }

    public ChessboardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initChessboard();
    }

    private void initChessboard() {
        Log.d("CHESSBOARD", "init chessboard");
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.chessboard_basic, this, true);

        chessboardGrid = (GridLayout) findViewById(R.id.chessboard_grid);

        detectAndPrepareFields();
    }

    private void detectAndPrepareFields() {

        final int fieldCount = chessboardGrid.getChildCount();
        for (int i=0; i<fieldCount; i++) {
            FieldView field = (FieldView) chessboardGrid.getChildAt(i);
            field.setChessboard(this);
            fields.put(field.getCoordStr(), field);
        }

    }

    private PointF getFromTranslation(AbstractFigureView figure) {
        return new PointF(figure.getTranslationX(), figure.getTranslationY());
    }

    private PointF getFieldTranslation(String coord) {
        FieldView field = fields.get(coord);

        if (field != null) {
            return new PointF(chessboardGrid.getLeft() + field.getLeft(), chessboardGrid.getTop() + field.getTop());
        }

        return null;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        final int gridWidth = MeasureSpec.getSize(widthMeasureSpec);
        final int gridHeight = MeasureSpec.getSize(heightMeasureSpec);

        View chessboardRootLayout = findViewById(R.id.chessboard_basic_layout);

        final int paddingLeft = chessboardRootLayout.getPaddingLeft();
        final int fieldCount = fields.size();
        final int fieldSize = ((gridWidth > gridHeight ? gridHeight : gridWidth) - 2 * paddingLeft) / (int) Math.sqrt(fieldCount);

        for (Map.Entry<String, FieldView> fieldEntry : fields.entrySet()) {
            FieldView field = fieldEntry.getValue();
            final ViewGroup.LayoutParams fieldLayoutParams = field.getLayoutParams();
            fieldLayoutParams.height = fieldSize;
            fieldLayoutParams.width = fieldSize;
            field.setLayoutParams(fieldLayoutParams);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void placeFigure(AbstractFigureView figure, String coord) {

        FieldView field = fields.get(coord);

        if (field != null) {

            field.setFigureView(figure);

            figure.setTranslationX(chessboardGrid.getLeft() + field.getLeft());
            figure.setTranslationY(chessboardGrid.getTop() + field.getTop());

            figure.setLayoutParams(new ViewGroup.LayoutParams(field.getRight() - field.getLeft(), field.getBottom() - field.getTop()));

            addView(figure);

        } else {
            Log.e("CHESSBOARD", "Field for coord = " + coord + " not found!");
        }

    }

    public void prepareChessboard(final List<FigurePosition> figurePositions) {

        /**
         * Possibly this prepareChessboard is called before this view is ready to be drawn. So this guarantees, that
         * the measured sizes are available
         */
        post(new Runnable() {
            @Override
            public void run() {

                final Context context = getContext();

                for (FigurePosition figurePosition : figurePositions) {

                    AbstractFigureView figure = FigureViewBuilder.createFigureView(context, figurePosition.getFigureType(), figurePosition.getColorType());
                    placeFigure(figure, figurePosition.getFieldCoord());

                }

            }
        });

    }

    @Override
    public void showPossibleMoves(FieldView sourceFieldView, List<Coord> possibleMoves) {

        AnimatorSet animations = new AnimatorSet();

        final AnimatorSet.Builder builder = animations.play(sourceFieldView.getSourceFieldFadeInAnimation());

        for (String coord : moveOperation.getPossibleMoves()) {
            final FieldView fieldView = fields.get(coord);
            builder.with(fieldView.getPossibleMovesFadeInAnimation());
        }

        animations.start();

    }

    @Override
    public void hidePossibleMoves(FieldView sourceFieldView, List<Coord> possibleMoves) {

        AnimatorSet animations = new AnimatorSet();

        final AnimatorSet.Builder builder = animations.play(sourceFieldView.getSourceFieldFadeOutAnimation());

        for (String coord : moveOperation.getPossibleMoves()) {
            final FieldView fieldView = fields.get(coord);
            builder.with(fieldView.getPossibleMovesFadeOutAnimation());
        }

        animations.start();

    }

    @Override
    public void moveFigure(String fromCoord, String toCoord) {

        final FieldView fromField = fields.get(fromCoord);
        final FieldView toField = fields.get(toCoord);

        final AbstractFigureView figureView = fromField.takeFigureView();

        if (figureView != null) {

            final AbstractFigureView mayHitFigureView = toField.getFigureView();
            toField.setFigureView(figureView);

            final PointF fromPoint = getFromTranslation(figureView);
            final PointF toPoint = getFieldTranslation(toField.getCoordStr());

            final ObjectAnimator moveX = ObjectAnimator
                    .ofFloat(figureView, "translationX", fromPoint.x, toPoint.x);

            final ObjectAnimator moveY = ObjectAnimator
                    .ofFloat(figureView, "translationY", fromPoint.y, toPoint.y);

            final ObjectAnimator raiseUpAndDownX = ObjectAnimator.ofFloat(figureView, "scaleX", ANIMATION_MOVE_FIGURE_RAISE_UP_DOWN);
            raiseUpAndDownX.setRepeatCount(1);
            raiseUpAndDownX.setRepeatMode(ValueAnimator.REVERSE);

            final ObjectAnimator raiseUpAndDownY = ObjectAnimator.ofFloat(figureView, "scaleY", ANIMATION_MOVE_FIGURE_RAISE_UP_DOWN);
            raiseUpAndDownY.setRepeatCount(1);
            raiseUpAndDownY.setRepeatMode(ValueAnimator.REVERSE);

            AnimatorSet animatorSet = new AnimatorSet();
            final AnimatorSet.Builder animationBuilder = animatorSet
                    .play(moveY)
                    .with(moveX)
                    .with(raiseUpAndDownX)
                    .with(raiseUpAndDownY);

            if (mayHitFigureView != null) {
                final ObjectAnimator fadeOutAnimation = ObjectAnimator.ofFloat(mayHitFigureView, "alpha", 1f, 0f);

                fadeOutAnimation.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        removeView(mayHitFigureView);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });

                animationBuilder.with(fadeOutAnimation);
            }

            animatorSet.setDuration(ANIMATION_MOVE_FIGURE_DURATION);
            animatorSet.start();


        } else {
            Log.e("CHESSBOARD", "No figure found at 'from' field!");
        }

    }

    @Override
    public void moveFigureCastling(String fromCoord, String toCoord, String fromRookCoord, String toRookCoord) {
TODO
    }

    @Override
    public void hideFigure(String coord) {

    }

    @Override
    public FieldView getFieldView(String coord) {
        return null;
    }

    @Override
    public void rotateFiguresToWhiteSide() {

    }

    @Override
    public void rotateFiguresToWhiteSide(boolean withoutAnimation) {

    }

    @Override
    public void rotateFiguresToBlackSide() {

    }

    @Override
    public void rotateFiguresToBlackSide(boolean withoutAnimation) {

    }

}
