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
import lojaroupas.dao.TipoDAO;
import lojaroupas.database.Database;
import lojaroupas.database.DatabaseFactory;
import lojaroupas.model.Tipo;

/**
 * FXML Controller class
 *
 * @author dougl
 */
public class FXMLAdicionarTipoController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private Stage dialogStage;
    
    @FXML
    private TextField textFieldTipo;

    @FXML
    private Button buttonConfirmarTipo;
    
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final TipoDAO tipoDAO = new TipoDAO();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tipoDAO.setConnection(connection);
    }    
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    @FXML
    public void handleButtonConfirmarTipo() throws IOException {
        Tipo tipo = new Tipo();
        if (textFieldTipo.getText() != null || textFieldTipo.getText().length() != 0) {
            tipo.setDescricao(textFieldTipo.getText());
            
            tipoDAO.inserir(tipo);
        }
        
        dialogStage.close();
    }
    
    
    
}
