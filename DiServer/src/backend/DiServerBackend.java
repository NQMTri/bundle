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

import frontend.DiServerFrontend;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;
import protocol.Login;
import protocol.Message;
import protocol.Package;

/**
 *
 * @author khoi
 */
public class DiServerBackend extends Thread {

    private int uniqueId;
    private int portNumber;
    private boolean keepGoing = false;
    private final Map<Integer, ListenToClient> clients;
    private DiServerFrontend frontend;

    public boolean setPortNumber(int portNumber) {
        if (!keepGoing) {
            this.portNumber = portNumber;
            return true;
        }

        return false;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public DiServerBackend(int portNumber) {
        this.setName("ServerThread: " + portNumber);
        this.uniqueId = 0;
        this.clients = new TreeMap<>();
        this.portNumber = portNumber;
    }

    public void setFrontend(DiServerFrontend frontend) {
        this.frontend = frontend;
    }

    @Override
    public void run() {
        /* Pre-checking */
        frontend.log("Prechecking...");
        if (this.frontend == null) {
            System.err.println("Implementation error: Frontend was not set");
            System.exit(1);
        }
        frontend.log("Prechecking passed");
        keepGoing = true;

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            frontend.log("Server is now running");
            frontend.setServerUp(true);
            while (keepGoing) {
                frontend.log("Waiting for connection");
                // IO block
                Socket socket = serverSocket.accept();
                frontend.log("Connection from " + socket.getInetAddress());
                frontend.log("Keep going = " + keepGoing);

                if (!keepGoing) {
                    break;
                }

                ListenToClient client = new ListenToClient(socket);
                clients.put(uniqueId++, client);
                client.start();
            }
            frontend.log("Server is stopping");
        } catch (IOException ex) {
            frontend.log(ex.getMessage());
        }

        for (Map.Entry<Integer, ListenToClient> it : clients.entrySet()) {
            try {
                disconnectClient(it.getValue());
            } catch (IOException ex) {
                frontend.log(ex.getMessage());
            }
        }
        frontend.log("Server stopped");
        frontend.setServerUp(false);
    }

    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public boolean stopServer() {
        frontend.log("Received signal to stop");
        keepGoing = false;
        try {
            new Socket("localhost", portNumber);
        } catch (IOException ex) {
            frontend.log(ex.getMessage());
            return false;
        }
        return true;
    }

    public boolean disconnectClient(int id) {
        ListenToClient target = clients.get(id);
        if (target != null) {
            clients.remove(id);
            try {
                disconnectClient(target);
            } catch (IOException ex) {
                frontend.log(ex.getMessage());
                return false;
            }
        }
        return true;
    }

    private synchronized void disconnectClient(ListenToClient clientThread)
            throws IOException {
        clientThread.disconnect();
    }

    public boolean banClient(int id) {
        ListenToClient target = clients.get(id);
        if (target != null) {
            target.ban();
        }

        return true;
    }

    private synchronized void broadcastMsg(Message msg) {
        for (Map.Entry<Integer, ListenToClient> it : clients.entrySet()) {
            it.getValue().write(msg);
        }
    }

    /**
     * Server spawns these threads to deal with each connection concurrently
     */
    class ListenToClient extends Thread {

        private final Socket socket;
        private final ObjectInputStream instream;
        private final ObjectOutputStream outstream;
        private String handle;
        boolean keepGoing = true;

        public ListenToClient(Socket socket) throws IOException {
            this.setName("ListenToClientThread: " + socket.getInetAddress());
            this.socket = socket;
            this.instream = new ObjectInputStream(socket.getInputStream());
            this.outstream = new ObjectOutputStream(socket.getOutputStream());
            if (!validate()) {
                refuse();
            }
        }

        private boolean validate() throws IOException {
            try {
                Login login = (Login) instream.readObject();
                handle = login.getHandle();
                handle = handle.isEmpty() ? "Anonymous" : handle;
                /* Add checking password heres */
            } catch (ClassNotFoundException ex) {
                /* Invalid protocol */
                return false;
            }
            return true;
        }

        private void disconnect() throws IOException {
            instream.close();
            outstream.close();
            socket.close();
        }

        private void refuse() throws IOException {
            disconnect();
        }

        private boolean write(Message msg) {
            try {
                outstream.writeObject(msg);
            } catch (IOException ex) {
                return false;
            }
            return true;
        }

        private Package receive() throws IOException,
                ClassNotFoundException {
            Package ret = (Package) instream.readObject();
            return ret;
        }

        private boolean ban() {
            return true;
        }

        @Override
        public void run() {
            frontend.log("Server thread is listening to client");
            // TODO: Refactor this shit
            while (keepGoing) {
                try {
                    Package com = receive();
                    switch (com.getCommunicationType()) {
                        case COMMAND:
                            
                            break;
                        case MESSAGE:
                            break;
//                        case LOGOUT:
//                            keepGoing = false;
//                            disconnectClient(this);
//                            break;
//                        case MESSAGE:
//                            broadcastMsg((Message) com);
//                            break;
//                        case WHOISIN:
//                            whoisin();
//                            break;
                        default:
                            break;
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    frontend.log(ex.getMessage());
                }
            }
        }

        private void whoisin() {
        }

    }
}
