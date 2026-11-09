module co.edu.uniquindio.poo.parcial3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;


    opens co.edu.uniquindio.poo.parcial3 to javafx.fxml;
    exports co.edu.uniquindio.poo.parcial3;
}