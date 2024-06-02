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
import lojaroupas.dao.MarcaDAO;
import lojaroupas.database.Database;
import lojaroupas.database.DatabaseFactory;
import lojaroupas.model.Marca;

/**
 * FXML Controller class
 *
 * @author dougl
 */
public class FXMLAdicionarMarcaController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private Stage dialogStage;
    
    @FXML
    private TextField textFieldMarca;

    @FXML
    private Button buttonConfirmarMarca;
    
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final MarcaDAO marcaDAO = new MarcaDAO();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        marcaDAO.setConnection(connection);
    }    
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    @FXML
    public void handleButtonConfirmarMarca() throws IOException {
        Marca marca = new Marca();
        if (textFieldMarca.getText() != null || textFieldMarca.getText().length() != 0) {
            marca.setDescricao(textFieldMarca.getText());
            
            marcaDAO.inserir(marca);
        }
        
        dialogStage.close();
    }
    
    
    
}
