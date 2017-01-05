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

/**
 * @author Christian Thomas
 */
public class HumanVsCpuGameHandle extends AbstractHumanGameHandle {

    // TODO CpuPlayerMode abstract class...
    public static final int CPU_PLAYER_UNKNOWN = -1;
    public static final int CPU_PLAYER_BLACK = 0;
    public static final int CPU_PLAYER_WHITE = 1;

    private int cpuPlayer = CPU_PLAYER_UNKNOWN;

    public int getCpuPlayer() {
        return cpuPlayer;
    }

    public void setCpuPlayer(int cpuPlayer) {
        this.cpuPlayer = cpuPlayer;
    }

    @Override
    protected void handleStatePreStart() {

        // initiate the KI engine...

    }

    @Override
    protected void handleStateIdle() {


    }

    @Override
    protected void handleStatePossibleMoves() {


    }

    @Override
    protected void handleStateMoved() {


    }

    @Override
    protected void handleStatePendingDecision() {


    }

    @Override
    protected void handleStateComplete() {


    }

    @Override
    protected void handleStateFinished() {


    }


}
