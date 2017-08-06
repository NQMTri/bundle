/*
 * Copyright (C) 2017 khoi
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package protocol;

import java.io.Serializable;

/**
 *
 * @author khoi
 */
public abstract class Package implements Serializable {
     
    /**
     * There are several type of Communication
     *  MESSAGE         : Message
     *  LOGOUT          : Tells server the client is logging out
     *  LOGIN           : Holds login information
     *  WHOISIN         : Asks servers who is in the room
     */
    public enum CommunicationType {
        MESSAGE,
        COMMAND
    }
    static final long serialVersionUID = 1L;

    protected long timestamp;

    /**
     * @return Type of Communication Object
     */
    public abstract CommunicationType getCommunicationType();

    public Package() {
        timestamp = System.currentTimeMillis() / 1000L;
    }
}
