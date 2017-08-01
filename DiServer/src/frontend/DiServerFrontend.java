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
package frontend;

import backend.DiServerBackend;

/**
 *
 * @author khoi
 */
public abstract class DiServerFrontend {
    
    protected DiServerBackend server;
    protected volatile boolean serverReady = false;

    public abstract void log(String msg);
    public abstract void start();
    public abstract void stop();
    public abstract void disconnectClient(int id);
    public abstract void banClient(int id);

    public void setServerReady(boolean b) {
        serverReady = b;
    }

    public boolean isServerReady() {
        return serverReady;
    }
}
