package net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

/**
 * 最小客户端：连接房主并收发 {@link Protocol} 消息。
 * <p>
 * 仅提供大厅阶段所需能力（JOIN/READY + 接收 LOBBY/ASSIGN_SEAT）。
 */
public final class RemoteClient {

    private Socket socket;
    private BufferedWriter writer;
    private BufferedReader reader;
    private volatile Consumer<Protocol.Message> listener;

    public void connect(String host, int port, String nickname) throws IOException {
        socket = new Socket(host, port);
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        send(new Protocol.Message(Protocol.JOIN, new String[] {nickname}));
        Thread t = new Thread(this::readLoop, "net-client-read");
        t.setDaemon(true);
        t.start();
    }

    public void setListener(Consumer<Protocol.Message> listener) {
        this.listener = listener;
    }

    public void sendReady(boolean ready) throws IOException {
        send(new Protocol.Message(Protocol.READY, new String[] {Boolean.toString(ready)}));
    }

    public void send(Protocol.Message msg) throws IOException {
        writer.write(Protocol.encode(msg));
        writer.newLine();
        writer.flush();
    }

    private void readLoop() {
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                Protocol.Message m = Protocol.decode(line);
                Consumer<Protocol.Message> l = listener;
                if (l != null) {
                    l.accept(m);
                }
            }
        } catch (IOException ignored) {
        }
    }

    public void close() throws IOException {
        if (socket != null) socket.close();
    }
}

