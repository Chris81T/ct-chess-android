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

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.chrthms.chess.Chessboard;
import de.chrthms.chess.GameHandle;

import de.chrthms.chess.board.ChessboardView;
import de.chrthms.chess.board.FieldView;
import de.chrthms.chess.engine.ChessEngine;
import de.chrthms.chess.engine.core.Coord;
import de.chrthms.chess.engine.core.FigurePosition;
import de.chrthms.chess.engine.core.Handle;
import de.chrthms.chess.engine.core.MoveResult;
import de.chrthms.chess.engine.core.constants.CastlingType;
import de.chrthms.chess.engine.core.constants.ColorType;
import de.chrthms.chess.engine.impl.ChessEngineBuilder;
import de.chrthms.chess.exceptions.GameHandleException;

/**
 * @author Christian Thomas
 */
public abstract class AbstractGameHandle implements GameHandle, Serializable {

    protected static final int STATE_OFF = 0;
    protected static final int STATE_PRE_START = 1;
    protected static final int STATE_IDLE = 2;
    protected static final int STATE_POSSIBLE_MOVES = 3;
    protected static final int STATE_MOVED = 4;
    protected static final int STATE_PENDING_DECISION = 5;
    protected static final int STATE_COMPLETE = 6;
    protected static final int STATE_FINISHED = 7;

    protected transient ChessboardView chessboard = null;
    protected transient ChessEngine chessEngine = ChessEngineBuilder.build();

    protected Handle handle;

    private int state = STATE_OFF;

    /**
     * With state POSSIBLE_MOVES_REQUEST some possible moves should be set here. With state MOVE_TO the possibleMoves
     * are obsolete and can be cleared. Also the sourceField
     */
    private FieldView sourceFieldView = null;
    private List<Coord> possibleMoves = new ArrayList<>();

    private MoveResult moveResult = null;

    private FieldView fieldViewTrigger = null;

    protected List<Coord> getPossibleMoves() {
        return possibleMoves;
    }

    protected void setPossibleMoves(List<Coord> possibleMoves) {
        this.possibleMoves = possibleMoves;
    }

    protected FieldView getSourceFieldView() {
        return sourceFieldView;
    }

    protected void setSourceFieldView(FieldView sourceFieldView) {
        this.sourceFieldView = sourceFieldView;
    }

    protected void clearPossibleMovesAndSourceFieldView() {
        possibleMoves.clear();
        sourceFieldView = null;
    }

    protected FieldView getFieldViewTrigger() {
        return fieldViewTrigger;
    }

    protected void clearFieldViewTrigger() {
        fieldViewTrigger = null;
    }

    public MoveResult getMoveResult() {
        return moveResult;
    }

    public void setMoveResult(MoveResult moveResult) {
        this.moveResult = moveResult;
    }

    private void setChessboardAndPrepareFigureViews(Chessboard chessboard) {

        if (chessboard instanceof ChessboardView) {
            this.chessboard = (ChessboardView) chessboard;
            this.chessboard.initChessboard(this);
        } else {
            throw new GameHandleException("As implementation of Chessboard the ChessboardView class is expected!");
        }

        // TODO remove dump!
        for (FigurePosition figurePosition : chessEngine.getFigurePositions(handle)) {
            Log.i("DUMP", figurePosition.getFieldCoord() + " <-- (P=0;K=1;Q=2;R=3;B=4;N=5) " + figurePosition.getFigureType() + " color (b=0;w=1): " + figurePosition.getColorType());
        }

        this.chessboard.prepareChessboard(chessEngine.getFigurePositions(handle));
    }

    @Override
    public void activate(Handle handle, Chessboard chessboard) {
        this.handle = handle;
        setChessboardAndPrepareFigureViews(chessboard);
        setNewState(STATE_PRE_START);
        checkState();
    }

    @Override
    public void reactivate(Chessboard chessboard) {
        setChessboardAndPrepareFigureViews(chessboard);
        checkState();
    }

    @Override
    public void fieldTrigger(String coord) {
        fieldTrigger(chessboard.getFieldView(coord));
    }

    @Override
    public void fieldTrigger(FieldView fieldView) {
        fieldViewTrigger = fieldView;
        checkState();

    }

    protected void setNewState(int newState) {
        state = newState;
    }

    private int getRookCastlingY() {
        return moveResult.getMovedColorType() == ColorType.WHITE ? 1 : 8;
    }

    protected String getFromRookCastlingCoord() {
        final int y = getRookCastlingY();

        switch (moveResult.getCastlingType()) {
            case CastlingType.KINGSIDE:
                return new Coord(8, y).getStrCoord();
            case CastlingType.QUEENSIDE:
                return new Coord(1, y).getStrCoord();
        }

        throw new GameHandleException("No castling performed!");
    }

    protected String getToRookCastlingCoord() {
        final int y = getRookCastlingY();

        switch (moveResult.getCastlingType()) {
            case CastlingType.KINGSIDE:
                return new Coord(6, y).getStrCoord();
            case CastlingType.QUEENSIDE:
                return new Coord(4, y).getStrCoord();
        }

        throw new GameHandleException("No castling performed!");
    }

    protected void checkState() {

        switch (state) {
            case STATE_OFF:
                throw new GameHandleException("State is off. Activate it first!");
            case STATE_PRE_START:
                handleStatePreStart();
                break;
            case STATE_IDLE:
                handleStateIdle();
                break;
            case STATE_POSSIBLE_MOVES:
                handleStatePossibleMoves();
                break;
            case STATE_MOVED:
                handleStateMoved();
                break;
            case STATE_PENDING_DECISION:
                handleStatePendingDecision();
                break;
            case STATE_COMPLETE:
                handleStateComplete();
                break;
            case STATE_FINISHED:
                handleStateFinished();
                break;
        }

    }

    protected abstract void handleStatePreStart();

    protected abstract void handleStateIdle();

    protected abstract void handleStatePossibleMoves();

    protected abstract void handleStateMoved();

    protected abstract void handleStatePendingDecision();

    protected abstract void handleStateComplete();

    protected abstract void handleStateFinished();

}
