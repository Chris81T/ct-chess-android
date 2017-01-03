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
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import de.chrthms.chess.Chessboard;
import de.chrthms.chess.R;
import de.chrthms.chess.board.markers.InvalidFieldView;
import de.chrthms.chess.board.markers.PossibleFieldView;
import de.chrthms.chess.board.markers.SourceFieldView;
import de.chrthms.chess.core.MoveOperation;
import de.chrthms.chess.figures.AbstractFigureView;

/**
 * Created by christian on 01.01.17.
 */
public class FieldView extends FrameLayout {

    private static final int ANIMATION_INVALID_FIELD_DURATION = 200;
    private static final int ANIMATION_SOURCE_FIELD_DURATION = 250;
    private static final int ANIMATION_POSSIBLE_FIELDS_DURATION = 250;

    private Chessboard chessboard = null;

    private String coordStr = null;

    private AbstractFigureView figureView = null;

    private InvalidFieldView invalidFieldView = null;
    private PossibleFieldView possibleFieldView = null;
    private SourceFieldView sourceFieldView = null;

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

        initChildViews(context);
    }

    private void initChildViews(Context context) {
        invalidFieldView = new InvalidFieldView(context);
        possibleFieldView = new PossibleFieldView(context);
        sourceFieldView = new SourceFieldView(context);
    }

    public AbstractFigureView getFigureView() {
        return figureView;
    }

    public AbstractFigureView takeFigureView() {
        AbstractFigureView figureView = this.figureView;
        this.figureView = null;
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

    private void animateAsInvalidField() {

        addView(invalidFieldView);

        final ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(invalidFieldView, "alpha", 0f, 1f);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(ValueAnimator.REVERSE);
        alphaAnimation.setDuration(ANIMATION_INVALID_FIELD_DURATION);

        alphaAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                removeView(invalidFieldView);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        alphaAnimation.start();

    }

    public ObjectAnimator getSourceFieldFadeInAnimation() {
        addView(sourceFieldView);
        final ObjectAnimator fadeInAnimation = ObjectAnimator.ofFloat(sourceFieldView, "alpha", 0f, 1f);
        fadeInAnimation.setDuration(ANIMATION_SOURCE_FIELD_DURATION);
        return fadeInAnimation;
    }

    public ObjectAnimator getSourceFieldFadeOutAnimation() {
        final ObjectAnimator fadeOutAnimation = ObjectAnimator.ofFloat(sourceFieldView, "alpha", 1f, 0f);
        fadeOutAnimation.setDuration(ANIMATION_SOURCE_FIELD_DURATION);

        fadeOutAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                removeView(sourceFieldView);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        return fadeOutAnimation;
    }

    public ObjectAnimator getPossibleMovesFadeInAnimation() {
        addView(possibleFieldView);
        final ObjectAnimator fadeInAnimation = ObjectAnimator.ofFloat(possibleFieldView, "alpha", 0f, 1f);
        fadeInAnimation.setDuration(ANIMATION_POSSIBLE_FIELDS_DURATION);
        return fadeInAnimation;
    }

        public ObjectAnimator getPossibleMovesFadeOutAnimation() {
        final ObjectAnimator fadeOutAnimation = ObjectAnimator.ofFloat(possibleFieldView, "alpha", 1f, 0f);
        fadeOutAnimation.setDuration(ANIMATION_POSSIBLE_FIELDS_DURATION);

            fadeOutAnimation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    removeView(possibleFieldView);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            return fadeOutAnimation;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if (!chessboard.isMoveOperationAvailable()) {

                    if (getFigureView() != null) {
                        boolean succeed = chessboard.startMoveOperation(this);

                        if (!succeed) {
                            animateAsInvalidField();
                        }

                        return succeed;
                    } else {
                        animateAsInvalidField();
                        return false;
                    }

                } else {
                    final MoveOperation moveOperation = chessboard.getAvailableMoveOperation();
                    if (moveOperation.getPossibleMoves().contains(getCoordStr())) {
                        return chessboard.performMoveOperation(this);
                    } else {
                        // TODO animate invalid field...
                        return true;
                    }

                }

            case MotionEvent.ACTION_MOVE:
                return false;
            case MotionEvent.ACTION_UP:
                return false;
            default:
                return false;

        }

    }
}
