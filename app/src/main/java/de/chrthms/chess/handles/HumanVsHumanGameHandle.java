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

package de.chrthms.chess.handles;

import java.util.List;

import de.chrthms.chess.board.FieldView;
import de.chrthms.chess.constants.GameHandleMode;
import de.chrthms.chess.engine.core.Coord;
import de.chrthms.chess.engine.core.MoveResult;
import de.chrthms.chess.engine.core.constants.CastlingType;
import de.chrthms.chess.engine.core.constants.ColorType;
import de.chrthms.chess.engine.core.constants.GameState;
import de.chrthms.chess.figures.AbstractFigureView;

/**
 * @author Christian Thomas
 */
public class HumanVsHumanGameHandle extends AbstractHumanGameHandle {

    private final int gameMode; // same side OR face2face

    private int figureRotation = ColorType.WHITE;

    public HumanVsHumanGameHandle(int gameMode) {
        this.gameMode = gameMode;
    }

    private void toggleFigureRotation() {
        toggleFigureRotation(false);
    }

    private void toggleFigureRotation(boolean withoutAnimation) {

        switch (figureRotation) {
            case ColorType.WHITE:
                chessboard.rotateFiguresToBlackSide(withoutAnimation);
                figureRotation = ColorType.BLACK;
                break;
            case ColorType.BLACK:
                chessboard.rotateFiguresToWhiteSide(withoutAnimation);
                figureRotation = ColorType.WHITE;
                break;
        }

    }

    @Override
    protected void handleStatePreStart() {

        switch (gameMode) {

            case GameHandleMode.HUMAN_VS_HUMAN_SAME_SIDE:
                break;
            case GameHandleMode.HUMAN_VS_HUMAN_FACE2FACE:

                if (handle.isBlackActivePlayer()) {
                    toggleFigureRotation(true);
                }

                break;

        }

        setNewState(STATE_IDLE);
    }

    @Override
    protected void handleStateIdle() {

        final FieldView fieldView = getFieldViewTrigger();

        AbstractFigureView figureView = fieldView.getFigureView();

        if (figureView != null && figureView.getFigureColor() == handle.getActivePlayer()) {

            hideMayKingsFieldMenace();

            List<Coord> possibleMoves = chessEngine.possibleMoves(handle, new Coord(fieldView.getCoordStr()));
            setPossibleMoves(possibleMoves);

            if (!possibleMoves.isEmpty()) {
                setSourceFieldView(fieldView);
                chessboard.showPossibleMoves(fieldView, possibleMoves);

                setNewState(STATE_POSSIBLE_MOVES);

            } else {
                fieldView.toggleMenace();
            }

        } else {
            fieldView.toggleMenace();
        }

    }

    @Override
    protected void handleStatePossibleMoves() {

        final FieldView fieldView = getFieldViewTrigger();

        boolean fieldAccepted = false;

        final String toCoord = fieldView.getCoordStr();

        for (Coord coord : getPossibleMoves()) {
            if (coord.getStrCoord().equals(toCoord)) {
                fieldAccepted = true;
                break;
            }
        }

        if (fieldAccepted) {
            final String fromCoord = getSourceFieldView().getCoordStr();

            chessboard.hidePossibleMoves(getSourceFieldView(), getPossibleMoves());
            clearPossibleMovesAndSourceFieldView();

            MoveResult moveResult = chessEngine.moveTo(handle, new Coord(fromCoord), new Coord(toCoord));
            setMoveResult(moveResult);

            final int castlingType = moveResult.getCastlingType();

            if (castlingType == CastlingType.KINGSIDE || castlingType == CastlingType.QUEENSIDE) {
                chessboard.moveFigureCastling(fromCoord, toCoord, getFromRookCastlingCoord(), getToRookCastlingCoord());
            } else {

                if (moveResult.isEnPassant()) {
                    chessboard.hideFigure(moveResult.getLastMoveResult().getToField().getStrCoord());
                }

                chessboard.moveFigure(fromCoord, toCoord);
            }

            setNewState(STATE_MOVED);
            checkState();

        } else if (getSourceFieldView().getCoordStr().equals(fieldView.getCoordStr())) {
            chessboard.hidePossibleMoves(getSourceFieldView(), getPossibleMoves());
            clearPossibleMovesAndSourceFieldView();
            setNewState(STATE_IDLE);

        } else {
            chessboard.hidePossibleMoves(getSourceFieldView(), getPossibleMoves());
            clearPossibleMovesAndSourceFieldView();
            setNewState(STATE_IDLE);
            checkState();
        }

    }

    @Override
    protected void handleStateMoved() {

        MoveResult moveResult = getMoveResult();

        if (moveResult.isPawnTransformation()) {
            setNewState(STATE_PENDING_DECISION);
        } else {
            setNewState(STATE_COMPLETE);
        }

        checkState();
    }

    @Override
    protected void handleStatePendingDecision() {

        // TODO prepare and show dialog...

        // FAKE:
        new Runnable() {

            @Override
            public void run() {

                // decision is given... set it to MoveResult

                setNewState(STATE_COMPLETE);
                checkState();

            }
        };

    }


    @Override
    protected void handleStateComplete() {

        final int gameState = chessEngine.completeMoveTo(handle, getMoveResult());
        final int activePlayerAfterComplete = handle.getActivePlayer();

        clearFieldViewTrigger();

        switch (gameState) {
            case GameState.CHECK:
                showKingsFieldMenace(activePlayerAfterComplete);

            case GameState.NORMAL:

                if (gameMode == GameHandleMode.HUMAN_VS_HUMAN_FACE2FACE) {
                    toggleFigureRotation();
                }

                setNewState(STATE_IDLE);
                break;

            case GameState.CHECKMATE:
            case GameState.DEADLOCK:
                // if this case occurs, the chess-engine has not switched the active player while game over!
                showKingsFieldMenace(activePlayerAfterComplete == ColorType.BLACK ? ColorType.WHITE : ColorType.BLACK);

            case GameState.DRAWN:
                setNewState(STATE_FINISHED);
                checkState();
                break;
        }

    }

    @Override
    protected void handleStateFinished() {
        // show final dialog...

    }

}
