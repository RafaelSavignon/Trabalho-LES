/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lojaroupas.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author dougl
 */
public class FXMLPrincipalController implements Initializable {

    @FXML
    private MenuItem menuItemCadastrosClientes;

    @FXML
    private MenuItem menuItemCadastrosFornecedores;

    @FXML
    private MenuItem menuItemCadastrosFuncionarios;
    
    @FXML
    private MenuItem menuItemCadastrosProdutos;
    
    @FXML
    private MenuItem menuItemProcessosRecebimento;
    
    @FXML
    private MenuItem menuItemProcessosVenda;
    
    @FXML
    private MenuItem menuItemRelatoriosProdutosTecido;
    
    @FXML
    private MenuItem menuItemRelatoriosVendasFuncionario;

    @FXML
    private AnchorPane anchorPane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    public void handleMenuItemCadastrosClientes() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/lojaroupas/view/FXMLCadastroCliente.fxml"));
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    public void handleMenuItemCadastrosFornecedores() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/lojaroupas/view/FXMLCadastroFornecedor.fxml"));
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    public void handleMenuItemCadastrosFuncionarios() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/lojaroupas/view/FXMLCadastroFuncionario.fxml"));
        anchorPane.getChildren().setAll(a);
    }
    
    @FXML
    public void handleMenuItemCadastrosProdutos() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/lojaroupas/view/FXMLCadastroProduto.fxml"));
        anchorPane.getChildren().setAll(a);
    }
    
    @FXML
    public void handleMenuItemProcessosRecebimento() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/lojaroupas/view/FXMLProcessoRecebimento.fxml"));
        anchorPane.getChildren().setAll(a);
    }
    
    @FXML
    public void handleMenuItemProcessosVenda() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/lojaroupas/view/FXMLProcessoVenda.fxml"));
        anchorPane.getChildren().setAll(a);
    }
    
    @FXML
    public void handleMenuItemRelatoriosProdutosTecido() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/lojaroupas/view/FXMLRelatorioProdutoTecido.fxml"));
        anchorPane.getChildren().setAll(a);
    }
    
    @FXML
    public void handleMenuItemRelatoriosVendasFuncionario() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/lojaroupas/view/FXMLRelatorioVendaFuncionario.fxml"));
        anchorPane.getChildren().setAll(a);
    }
}
