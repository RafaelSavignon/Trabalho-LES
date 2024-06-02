/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lojaroupas.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lojaroupas.dao.TecidoDAO;
import lojaroupas.database.Database;
import lojaroupas.database.DatabaseFactory;
import lojaroupas.model.Tecido;

/**
 * FXML Controller class
 *
 * @author dougl
 */
public class FXMLAdicionarTecidoController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private Stage dialogStage;
    
    @FXML
    private TextField textFieldTecido;

    @FXML
    private Button buttonConfirmarTecido;
    
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final TecidoDAO tecidoDAO = new TecidoDAO();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tecidoDAO.setConnection(connection);
    }    
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    @FXML
    public void handleButtonConfirmarTecido() throws IOException {
        Tecido tecido = new Tecido();
        if (textFieldTecido.getText() != null || textFieldTecido.getText().length() != 0) {
            tecido.setDescricao(textFieldTecido.getText());
            
            tecidoDAO.inserir(tecido);
        }
        
        dialogStage.close();
    }
    
    
    
}
