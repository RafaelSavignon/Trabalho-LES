/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lojaroupas.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import lojaroupas.dao.CidadeDAO;
import lojaroupas.dao.FornecedorDAO;
import lojaroupas.dao.UfDAO;
import lojaroupas.database.Database;
import lojaroupas.database.DatabaseFactory;
import lojaroupas.model.Cidade;
import lojaroupas.model.Fornecedor;
import lojaroupas.model.Uf;
import static org.postgresql.jdbc.PgResultSet.toFloat;
import static org.postgresql.jdbc.PgResultSet.toInt;

/**
 * FXML Controller class
 *
 * @author dougl
 */
public class FXMLCadastroFornecedorController implements Initializable {
    @FXML
    private TableView<Fornecedor> tableViewFornecedores;
    @FXML
    private TableColumn<Fornecedor, String> tableColumnFornecedorNome;
    @FXML
    private TableColumn<Fornecedor, String> tableColumnFornecedorCnpj;
    @FXML
    private TextField textFieldFornecedorNome;
    @FXML
    private TextField textFieldFornecedorCnpj;
    @FXML
    private ComboBox<String> comboBoxFornecedorUf;
    @FXML
    private ComboBox<String> comboBoxFornecedorCidade;
    @FXML
    private Button buttonAlterar;
    @FXML
    private Button buttonInserir;
    @FXML
    private Button buttonRemover;
    
    private List<Fornecedor> listFornecedor;
    private ObservableList<Fornecedor> observableListFornecedor;
    
    private List<String> listUf;
    private ObservableList<String> observableListUf;
    
    private List<String> listCidade;
    private ObservableList<String> observableListCidade;

    //Atributos para manipulação de Banco de Dados
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final FornecedorDAO fornecedorDAO = new FornecedorDAO();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fornecedorDAO.setConnection(connection);
        
        carregarTableViewFornecedores();
        //carregarComboBoxUf();
        //carregarComboBoxCidade();

        // Limpando a exibição dos detalhes do fornecedor
        selecionarItemTableViewFornecedores(null);

        // Listen acionado diante de quaisquer alterações na seleção de itens do TableView
        tableViewFornecedores.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewFornecedores(newValue));
    }

    public void carregarTableViewFornecedores() {
        tableColumnFornecedorNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnFornecedorCnpj.setCellValueFactory(new PropertyValueFactory<>("cnpj"));

        listFornecedor = fornecedorDAO.listar();

        observableListFornecedor = FXCollections.observableArrayList(listFornecedor);
        tableViewFornecedores.setItems(observableListFornecedor);
        
        Fornecedor apagar = new Fornecedor();
        selecionarItemTableViewFornecedores(apagar);
    }
    
    public void carregarComboBoxUf() {
        UfDAO ufDAO = new UfDAO();
        ufDAO.setConnection(connection);
        listUf = ufDAO.listar();
        observableListUf = FXCollections.observableArrayList(listUf);
        //comboBoxFornecedorUf.getItems().add(null);
        comboBoxFornecedorUf.getItems().clear();
        comboBoxFornecedorUf.getItems().addAll(observableListUf);
    }
    
    public void carregarComboBoxCidade() {
        CidadeDAO cidadeDAO = new CidadeDAO();
        cidadeDAO.setConnection(connection);
        
        if(comboBoxFornecedorUf.getValue() == null || "".equals(comboBoxFornecedorUf.getValue())) {
            listCidade = cidadeDAO.listar();
            observableListCidade = FXCollections.observableArrayList(listCidade);
            comboBoxFornecedorCidade.getItems().clear();
            comboBoxFornecedorCidade.getItems().addAll(observableListCidade);
        } else {
            listCidade = cidadeDAO.listarPorUf(comboBoxFornecedorUf.getValue());
            observableListCidade = FXCollections.observableArrayList(listCidade);
            comboBoxFornecedorCidade.getItems().clear();
            comboBoxFornecedorCidade.getItems().addAll(observableListCidade);
        }
        
        comboBoxFornecedorCidade.setVisibleRowCount(5);
        
    }

    public void selecionarItemTableViewFornecedores(Fornecedor fornecedor) {
        if (fornecedor != null) {
            textFieldFornecedorNome.setText(fornecedor.getNome());
            textFieldFornecedorCnpj.setText(fornecedor.getCnpj());
            comboBoxFornecedorUf.setValue(pegarNomeUf(fornecedor.getCidade()));
            comboBoxFornecedorCidade.setValue(pegarNomeCidade(fornecedor.getCidade()));
        } else {
            textFieldFornecedorNome.setText("");
            textFieldFornecedorCnpj.setText("");
        //    comboBoxFornecedorUf.setValue(null);
        //    comboBoxFornecedorCidade.setText("");
        }
    }

    @FXML
    public void handleButtonInserir() throws IOException, SQLException {
        Fornecedor fornecedor = new Fornecedor();
        if (validarEntradaDeDados()) {
            fornecedor.setNome(textFieldFornecedorNome.getText());
            fornecedor.setCnpj(textFieldFornecedorCnpj.getText());
            fornecedor.setCidade(pegarIdCidade(comboBoxFornecedorCidade.getValue()));
            
            fornecedorDAO.inserir(fornecedor);
            carregarTableViewFornecedores();
        }
    }

    @FXML
    public void handleButtonAlterar() throws IOException, SQLException {
        Fornecedor fornecedor = tableViewFornecedores.getSelectionModel().getSelectedItem();//Obtendo fornecedor selecionado
        Fornecedor resultado = new Fornecedor();
        if (fornecedor != null) {
            if (validarEntradaDeDados()) {
                String cnpj = textFieldFornecedorCnpj.getText();

                resultado = fornecedorDAO.buscar(fornecedor, cnpj);
                
                resultado.setNome(textFieldFornecedorNome.getText());
                resultado.setCidade(pegarIdCidade(comboBoxFornecedorCidade.getValue()));
                resultado.setCnpj(textFieldFornecedorCnpj.getText());
                
                fornecedorDAO.alterar(resultado);
                
                carregarTableViewFornecedores();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um Fornecedor na Tabela!");
            alert.show();
        }
    }

    @FXML
    public void handleButtonRemover() throws IOException {
        Fornecedor fornecedor = tableViewFornecedores.getSelectionModel().getSelectedItem();
        if (fornecedor != null) {
            String cnpj = textFieldFornecedorCnpj.getText();

            fornecedorDAO.remover(fornecedor, cnpj);
            carregarTableViewFornecedores();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um Fornecedor na Tabela!");
            alert.show();
        }
    }
    
    private boolean validarEntradaDeDados() {
        String errorMessage = "";

        if (textFieldFornecedorNome.getText() == null || textFieldFornecedorNome.getText().length() == 0) {
            errorMessage += "Nome inválido!\n";
        }
        if (textFieldFornecedorCnpj.getText() == null || textFieldFornecedorCnpj.getText().length() == 0) {
            errorMessage += "CNPJ inválido!\n";
        }
        if ("".equals(comboBoxFornecedorUf.getValue())) {
            errorMessage += "UF inválida!\n";
        }
        if ("".equals(comboBoxFornecedorCidade.getValue())) {
            errorMessage += "Cidade inválida!\n";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Mostrando a mensagem de erro
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Campos inválidos, por favor, corrija...");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }
    
    private int pegarIdCidade(String nome) {
        int id = 0;
        String sql = "SELECT idCidade FROM cidade WHERE nomeCidade = '" + nome + "'";
        try {
            PreparedStatement stmt_uf = connection.prepareStatement(sql);
            ResultSet resultado = stmt_uf.executeQuery();
            while(resultado.next()) {
                id = resultado.getInt("idCidade");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(CidadeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }
    
    private String pegarNomeCidade(int id) {
        String nome = "";
        String sql = "SELECT nomeCidade FROM cidade WHERE idCidade = " + id;
        try {
            PreparedStatement stmt_uf = connection.prepareStatement(sql);
            ResultSet resultado = stmt_uf.executeQuery();
            while(resultado.next()) {
                nome = resultado.getString("nomeCidade");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(CidadeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nome;
    }
    
    private String pegarNomeUf(int id_cidade) {
        String nome = "";
        int id_uf = 0;
        String sql = "SELECT ufCidade FROM cidade WHERE idCidade = " + id_cidade;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while(resultado.next()) {
                id_uf = resultado.getInt("ufCidade");
            }
            
            String sql_uf = "SELECT nomeUf FROM uf WHERE idUf = " + id_uf;
            try {
                PreparedStatement stmt_uf = connection.prepareStatement(sql_uf);
                ResultSet resultado_uf = stmt_uf.executeQuery();
                while(resultado_uf.next()) {
                    nome = resultado_uf.getString("nomeUf");
                }
            } catch (SQLException ex) {
                Logger.getLogger(CidadeDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(CidadeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nome;
    }
}
