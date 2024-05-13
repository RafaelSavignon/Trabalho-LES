/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lojaroupas.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lojaroupas.controller.FXMLLoginController;
import javafx.stage.Stage;
import lojaroupas.Main;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import lojaroupas.database.Database;
import lojaroupas.database.DatabaseFactory;
import lojaroupas.model.Funcionario;


public class FXMLLoginController implements Initializable {

    @FXML
    private Button BtnLogin;

    @FXML
    private TextField TextFieldLogin;

    @FXML
    private PasswordField TextFieldSenha;
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    
    @FXML
    void handleCadastroLogin(ActionEvent event) {

    } 
    
    
    @FXML
    void handleLogin(ActionEvent event) throws IOException {
        Funcionario func = new Funcionario();


        if(TextFieldLogin.getText().length() != 0 && TextFieldSenha.getText().length() != 0){
            func.setNome(TextFieldLogin.getText());
            func.setCpf(TextFieldSenha.getText());
            func.setGerente(pegarGerente(func.getCpf()));

            Parent root = FXMLLoader.load(getClass().getResource("/lojaroupas/view/FXMLPrincipal.fxml"));

            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.setScene(scene);
            stage.setTitle("Sistema de Gestão de Estoque");
            stage.setResizable(false);
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro!");
            alert.setHeaderText("Ocorreu um erro!");
            alert.setContentText("Preencha os campos de login!");
            alert.show();
        }

    }

    private boolean pegarGerente(String cpf) {
        boolean gerente = false;
        String sql = "SELECT eGerente FROM funcionario WHERE cpfFuncionario = '" + cpf + "'";
        try {
            PreparedStatement stmt_uf = connection.prepareStatement(sql);
            ResultSet resultado = stmt_uf.executeQuery();
            while(resultado.next()) {
                gerente = resultado.getBoolean("eGerente");
            }
            
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro!");
            alert.setHeaderText("Ocorreu um erro!");
            alert.setContentText(ex.toString());
            alert.show();
        }
        return gerente;
    }
    
}
