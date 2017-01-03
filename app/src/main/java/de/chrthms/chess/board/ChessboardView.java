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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.chrthms.chess.Chessboard;
import de.chrthms.chess.R;
import de.chrthms.chess.core.GameHandle;
import de.chrthms.chess.core.MoveOperation;
import de.chrthms.chess.engine.ChessEngine;
import de.chrthms.chess.engine.core.Coord;
import de.chrthms.chess.engine.core.FigurePosition;
import de.chrthms.chess.engine.core.Handle;
import de.chrthms.chess.engine.core.MoveResult;
import de.chrthms.chess.engine.core.backports.StreamBuilder;
import de.chrthms.chess.engine.core.constants.FigureType;
import de.chrthms.chess.engine.core.constants.MoveResultType;
import de.chrthms.chess.engine.exceptions.ChessEngineException;
import de.chrthms.chess.engine.impl.ChessEngineBuilder;
import de.chrthms.chess.figures.AbstractFigureView;
import de.chrthms.chess.figures.FigureViewBuilder;
import java8.util.stream.Collectors;

/**
 * Created by christian on 01.01.17.
 */

public class ChessboardView extends RelativeLayout implements Chessboard {

    private static final int ANIMATION_MOVE_FIGURE_DURATION = 250;
    private static final float ANIMATION_MOVE_FIGURE_RAISE_UP_DOWN = 2f;

    private Map<String, FieldView> fields = new HashMap<>();

    private GridLayout chessboardGrid;

    private MoveOperation moveOperation = null;

    private final ChessEngine chessEngine = ChessEngineBuilder.build();

    private GameHandle gameHandle = null;

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

    @Override
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

    @Override
    public boolean isMoveOperationAvailable() {
        return moveOperation != null;
    }

    @Override
    public MoveOperation getAvailableMoveOperation() {
        return moveOperation;
    }

    @Override
    public boolean startMoveOperation(FieldView fromFieldView) {

        if (isMoveOperationAvailable()) return false;

        try {

            final Handle handle = gameHandle.getHandle();
            final AbstractFigureView figureToMove = fromFieldView.getFigureView();

            if (figureToMove.getFigureColor() != handle.getActivePlayer()) return false;


            final List<Coord> possibleMoves = chessEngine.possibleMoves(handle, new Coord(fromFieldView.getCoordStr()));

            if (possibleMoves.isEmpty()) return false;

            final List<String> mappedMoves = new ArrayList<>();
            for (Coord possibleMove : possibleMoves) {
                mappedMoves.add(possibleMove.getStrCoord());
            }

            moveOperation = new MoveOperation();
            moveOperation.setSourceField(fromFieldView);
            moveOperation.setPossibleMoves(mappedMoves);

            moveOperation.setState(MoveOperation.STATE_STARTED);

            showPossibleMoves(moveOperation);


        } catch (ChessEngineException e) {
            Log.e("CHESSBOARD", "startMoveOperation failed. ChessEngineException Message = " + e.getMessage());
        }

        return true;
    }

    private void showPossibleMoves(MoveOperation moveOperation) {
        // TODO
    }

    @Override
    public boolean performMoveOperation(FieldView toFieldView) {

        try {

            if (isMoveOperationAvailable() && moveOperation.getState() == MoveOperation.STATE_STARTED) {

                final Handle handle = gameHandle.getHandle();
                final FieldView fromFieldView = moveOperation.getSourceField();
                final Coord fromCoord = new Coord(fromFieldView.getCoordStr());
                final Coord toCoord = new Coord(toFieldView.getCoordStr());

                final MoveResult moveResult = chessEngine.moveTo(handle, fromCoord, toCoord);

                moveFigure(fromFieldView, toFieldView);

                if (moveResult.getMoveResultType() == MoveResultType.DECISION_NEEDED) {
                    // TODO Dialog is needed to show!

                    // adjust the moveResult with new figure for pawn transformation
                }

                completeMoveOperation(handle, moveResult);

                return true;
            }


        } catch (ChessEngineException e) {
            Log.e("CHESSBOARD", "performMoveOperation failed. ChessEngineException Message = " + e.getMessage());
        }

        return false;
    }

    private void completeMoveOperation(Handle handle, MoveResult moveResult) {

        chessEngine.completeMoveTo(handle, moveResult);

    }


    @Override
    public void moveFigure(String fromCoord, String toCoord) {

        final FieldView fromField = fields.get(fromCoord);
        final FieldView toField = fields.get(toCoord);

        moveFigure(fromField, toField);

    }

    @Override
    public void moveFigure(FieldView fromField, FieldView toField) {
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
    public void setGameHandle(GameHandle gameHandle) {
        this.gameHandle = gameHandle;
    }

    @Override
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

}
