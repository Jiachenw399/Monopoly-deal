package network;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.net.Inet4Address;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Entry from the main menu: host the embedded {@link GameServer} or open {@link OnlinePlayWindow}.
 */
public final class OnlineLauncher {

    private static final Object SERVER_LOCK = new Object();
    private static Thread gameServerThread;

    private OnlineLauncher() {
    }

    public static void openLanMenu(Stage owner) {
        ChoiceDialog<String> choice = new ChoiceDialog<>("Join", "Join", "Host");
        choice.setTitle("LAN / OnlinePlayWindow");
        choice.setHeaderText("Host starts a server on this PC (port 5555). Join connects to the host's IP.");
        choice.setContentText("Choose role:");

        Optional<String> result = choice.showAndWait();
        if (result.isEmpty()) {
            return;
        }
        if ("Host".equals(result.get())) {
            startHostServerIfNeeded();
            showHostInfo(owner);
        } else {
            promptJoin(owner);
        }
    }

    private static void startHostServerIfNeeded() {
        synchronized (SERVER_LOCK) {
            if (gameServerThread != null && gameServerThread.isAlive()) {
                return;
            }
            gameServerThread = new Thread(() -> new GameServer().start(), "GameServer");
            gameServerThread.setDaemon(false);
            gameServerThread.start();
        }
    }

    private static void showHostInfo(Stage owner) {
        String ips = formatLanAddresses();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(owner);
        alert.setTitle("LAN host");
        alert.setHeaderText("Server is starting (or already running) on port 5555.");
        alert.setContentText(
                "Other computers: main menu → L → Join → enter this computer's IPv4 address.\n\n"
                        + "On this computer to play as a client: L → Join → 127.0.0.1\n\n"
                        + "Detected IPv4 addresses:\n"
                        + ips
                        + "\n\n"
                        + "Commands in the online window are the same as the console client "
                        + "(START_GAME when at least two players are connected, etc.)."
        );
        alert.getButtonTypes().setAll(ButtonType.OK);
        alert.showAndWait();
    }

    private static void promptJoin(Stage owner) {
        TextInputDialog dialog = new TextInputDialog("127.0.0.1");
        dialog.initOwner(owner);
        dialog.setTitle("Join LAN game");
        dialog.setHeaderText("Enter the host computer's IP address (or 127.0.0.1 on the same PC).");
        dialog.setContentText("Host IP:");

        Optional<String> ip = dialog.showAndWait();
        if (ip.isEmpty()) {
            return;
        }
        String host = ip.get().trim();
        if (host.isEmpty()) {
            return;
        }

        OnlinePlayWindow window = new OnlinePlayWindow(owner, host);
        window.show();
    }

    private static String formatLanAddresses() {
        List<String> lines = new ArrayList<>();
        try {
            for (NetworkInterface ni : java.util.Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (!ni.isUp() || ni.isLoopback()) {
                    continue;
                }
                for (InterfaceAddress ifAddr : ni.getInterfaceAddresses()) {
                    if (ifAddr.getAddress() instanceof Inet4Address addr && !addr.isLoopbackAddress()) {
                        lines.add("• " + addr.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            lines.add("(Could not list interfaces: " + e.getMessage() + ")");
        }
        if (lines.isEmpty()) {
            lines.add("• (none found — try ipconfig / ifconfig on the host)");
        }
        return String.join("\n", lines);
    }
}
