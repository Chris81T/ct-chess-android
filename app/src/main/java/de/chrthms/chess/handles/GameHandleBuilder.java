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

import de.chrthms.chess.GameHandle;
import de.chrthms.chess.constants.GameHandleMode;

/**
 * @author Christian Thomas
 */
public abstract class GameHandleBuilder {

    public static GameHandle build(int gameHandleMode) {
        return build(gameHandleMode, GameHandle.class);
    }

    public static <T> T build(int gameHandleMode, Class<T> expectedClass) {

        GameHandle gameHandle = null;

        switch (gameHandleMode) {
            case GameHandleMode.HUMAN_VS_HUMAN_SAME_SIDE:
                gameHandle = new HumanVsHumanGameHandle(gameHandleMode);
                break;
            case GameHandleMode.HUMAN_VS_HUMAN_FACE2FACE:
                gameHandle = new HumanVsHumanGameHandle(gameHandleMode);
                break;
            case GameHandleMode.HUMAN_VS_CPU:
                gameHandle = new HumanVsCpuGameHandle();
                break;
            // TODO if some other game modes will be implemented...
        }

        if (gameHandle != null && gameHandle.getClass().equals(expectedClass)) {
            return (T) gameHandle;
        }

        return null;
    }

}
