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

import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author khoi
 */
public class DiServerFrontendCLI extends DiServerFrontend {

    private boolean keepGoing;
    private final Scanner scanner;

    public DiServerFrontendCLI() {
        keepGoing = true;
        scanner = new Scanner(System.in);
    }

    @Override
    public void log(String msg) {
        System.out.println("LOG: " + msg);
    }

    @Override
    public void start() {
        String command;
        while (keepGoing) {
            System.out.print("> ");
            command = scanner.nextLine();
            switch (command) {
                case "stop":
                    stop();
                    while (isServerUp()) {
                        // Wait until server is down
                    }
                    break;
                case "exit":
                    keepGoing = false;
                    stop();
                    break;
                case "start":
                    if (!serverUp) {
                        this.setBackend(8080);
                        server.setFrontend(this);
                        server.start();
                        while (!isServerUp()) {
                            // Wait until server is up
                        }
                    } else {
                        log("Server is already started");
                    }
                    break;
                case "threads":
                    Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
                    for (Thread it : threadSet) {
                        System.out.println(it);
                    }
                    break;
                default:
                    log("Operation not supported");
                    break;
            }
        }
        log("Frontned Stopped");
    }

    @Override
    public void stop() {
        server.stopServer();
    }

    @Override
    public void disconnectClient(int id) {
        server.disconnectClient(id);
    }

    @Override
    public void banClient(int id) {
        server.banClient(id);
    }
}
