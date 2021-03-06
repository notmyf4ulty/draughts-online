/*
 * Copyright (c) 2013, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.catnbear;

import java.net.*;
import java.io.*;

public class App {
    public static void main(String[] args) {

        try {
            int portNumber = Integer.parseInt(args[0]);
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(portNumber);
            } catch (IOException e) {
                System.out.println("Cannot create socket.");
            }
            if (serverSocket != null) {
                while (true) {
                    try {
                        Socket clientSocket_1 = serverSocket.accept();
                        GameThread gameThread_1 = new GameThread(clientSocket_1, portNumber);
                        gameThread_1.start();
                        System.out.println("Client 1 connected.");
                        Socket clientSocket_2 = serverSocket.accept();
                        GameThread gameThread_2 = new GameThread(clientSocket_2, portNumber);
                        gameThread_2.start();
                        System.out.println("Client 2 connected.");
                    } catch (IOException e) {
                        System.out.println("Connection problem. Reseting game.");
                    }
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Inappropriate port number.");
        }
    }
}
