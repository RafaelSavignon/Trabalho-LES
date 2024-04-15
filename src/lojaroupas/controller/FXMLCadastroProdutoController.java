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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author dougl
 */
public class FXMLCadastroProdutoController implements Initializable {

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
    
    @FXML
    public void handleButtonAdicionarTipo() throws IOException, SQLException {
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAdicionarTipoController.class.getResource("/lojaroupas/view/FXMLAdicionarTipo.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Adicionar tipo");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        FXMLAdicionarTipoController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        dialogStage.showAndWait();
    }

    @FXML
    public void handleButtonAdicionarMarca() throws IOException, SQLException {
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAdicionarMarcaController.class.getResource("/lojaroupas/view/FXMLAdicionarMarca.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Adicionar marca");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        FXMLAdicionarMarcaController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        dialogStage.showAndWait();
    }

    @FXML
    public void handleButtonAdicionarTecido() throws IOException {
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAdicionarTecidoController.class.getResource("/lojaroupas/view/FXMLAdicionarTecido.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Adicionar tecido");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        FXMLAdicionarTecidoController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        dialogStage.showAndWait();
    }
    
}
