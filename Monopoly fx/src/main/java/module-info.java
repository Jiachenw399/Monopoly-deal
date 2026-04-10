module Monopoly.fx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swt;
    requires java.datatransfer;
    requires java.desktop;

    opens GUI to javafx.fxml; // 启动时使用
    exports GUI; // 导出 GUI 模块
}