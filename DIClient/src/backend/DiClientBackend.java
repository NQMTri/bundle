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
package backend;

import frontend.DiClientFrontend;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author khoi
 */
public class DiClientBackend extends Thread {

    private final String host;
    private final int port;
    private DiClientFrontend frontend;
    private volatile boolean keepListening;

    public DiClientBackend(String host, int port) {
        // Client thread is for sending/controlling client
        Thread.currentThread().setName("ClientThread");
        this.host = host;
        this.port = port;
    }

    public void setFrontend(DiClientFrontend frontend) {
        this.frontend = frontend;
    }

    @Override
    public void run() {
        try (Socket socket = new Socket(host, port)) {

            new ListenToServer(socket).start();

        } catch (IOException ex) {
            frontend.log(ex.getMessage());
        }
    }

    public void stopClient() {

    }

    class ListenToServer extends Thread {

        private ListenToServer(Socket socket) {
            Thread.currentThread().setName("ListenToServerThread");
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void run() {

        }
    }
}
