package ru.spbau.mit.kurbanov.server;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.spbau.mit.kurbanov.protocol.ChatMessage;
import ru.spbau.mit.kurbanov.protocol.ChatProtocol;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

@Slf4j
public class Server {

    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
    private final ServerSocket serverSocket;
    @Setter
    private BiConsumer<InetAddress, ChatMessage> messageHandler;

    /**
     * Creates server socket at given port and starts main server loop which
     * listens for this socket connections.
     *
     * @throws IOException if error occurred during socket creation
     */
    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        log.info("Started server at port " + port);
        executor.submit(this::runServerLoop);
    }

    /**
     * Accepts client connections and submits them to thread pool.
     */
    private void runServerLoop() {
        while (!serverSocket.isClosed()) {
            try {
                final Socket socket = serverSocket.accept();
                executor.submit(() -> processClient(socket));
            } catch (SocketException e) {
                log.info("Socket is closed");
            } catch (IOException e) {
                log.error("Connection to client failed: " + e.getMessage());
            }
        }
    }

    /**
     * Receives single message from client socket and closes connection after receiving.
     */
    private void processClient(Socket socket) {
        try {
            final DataInputStream input = new DataInputStream(socket.getInputStream());
            final ChatMessage message = ChatProtocol.receiveMessage(input);
            log.info("Received message from " + socket.getInetAddress());
            if (messageHandler != null) {
                messageHandler.accept(socket.getInetAddress(), message);
            }
            socket.close();
        } catch (IOException e) {
            log.error("Failed to receive message: " + e.getMessage());
        }
    }

    /**
     * Closes server socket.
     */
    public void shutdown() {
        log.info("Shutdown server");
        executor.shutdownNow();
        try {
            serverSocket.close();
        } catch (IOException e) {
            log.error("Failed to close server socket");
        }
    }
}
