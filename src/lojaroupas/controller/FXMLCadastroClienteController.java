/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lojaroupas.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import static org.postgresql.jdbc.PgResultSet.toFloat;
import static org.postgresql.jdbc.PgResultSet.toInt;

/**
 * FXML Controller class
 *
 * @author dougl
 */
public class FXMLCadastroClienteController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    public void handleButtonInserir() throws IOException, SQLException {
    }

    @FXML
    public void handleButtonAlterar() throws IOException, SQLException {
    }

    @FXML
    public void handleButtonRemover() throws IOException {
    }
}
