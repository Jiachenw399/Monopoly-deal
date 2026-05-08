package net;

import java.util.ArrayList;
import java.util.List;

/**
 * 最小文本行协议：每条消息一行，字段用 '|' 分隔。
 * <p>
 * 字段内若包含 '\' 或 '|'，会以反斜杠转义。
 */
public final class Protocol {

    public static final String JOIN = "JOIN";
    public static final String READY = "READY";

    public static final String ASSIGN_SEAT = "ASSIGN_SEAT";
    public static final String LOBBY = "LOBBY";
    public static final String ERROR = "ERROR";

    private Protocol() {}

    public record Message(String type, String[] args) {
        public Message {
            if (type == null) type = "";
            if (args == null) args = new String[0];
        }
    }

    public static String encode(Message msg) {
        StringBuilder sb = new StringBuilder();
        sb.append(escape(msg.type()));
        for (String a : msg.args()) {
            sb.append('|').append(escape(a == null ? "" : a));
        }
        return sb.toString();
    }

    public static Message decode(String line) {
        if (line == null || line.isEmpty()) {
            return new Message("", new String[0]);
        }
        List<String> parts = split(line);
        String type = parts.isEmpty() ? "" : parts.get(0);
        String[] args = parts.size() <= 1 ? new String[0] : parts.subList(1, parts.size()).toArray(String[]::new);
        return new Message(type, args);
    }

    private static String escape(String s) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\\' || c == '|') b.append('\\');
            b.append(c);
        }
        return b.toString();
    }

    private static List<String> split(String line) {
        ArrayList<String> out = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '\\' && i + 1 < line.length()) {
                cur.append(line.charAt(++i));
                continue;
            }
            if (c == '|') {
                out.add(cur.toString());
                cur.setLength(0);
            } else {
                cur.append(c);
            }
        }
        out.add(cur.toString());
        return out;
    }
}

