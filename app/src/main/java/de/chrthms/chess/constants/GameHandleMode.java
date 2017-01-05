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

package de.chrthms.chess.constants;

/**
 * @author Christian Thomas
 */
public abstract class GameHandleMode {

    public static final int UNKNOWN = 0;
    public static final int HUMAN_VS_HUMAN_SAME_SIDE = 1;
    public static final int HUMAN_VS_HUMAN_FACE2FACE = 2;
    public static final int HUMAN_VS_CPU = 3;
    public static final int VIEW_ONLY = 0;
    public static final int FREESTYLE = 0;
    public static final int POSTAL_CHESS = 6;

}
