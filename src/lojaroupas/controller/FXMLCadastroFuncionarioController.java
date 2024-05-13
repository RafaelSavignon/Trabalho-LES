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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import lojaroupas.dao.CidadeDAO;
import lojaroupas.dao.FuncionarioDAO;
import lojaroupas.dao.UfDAO;
import lojaroupas.database.Database;
import lojaroupas.database.DatabaseFactory;
import lojaroupas.model.Cidade;
import lojaroupas.model.Funcionario;
import lojaroupas.model.Uf;
import static org.postgresql.jdbc.PgResultSet.toFloat;
import static org.postgresql.jdbc.PgResultSet.toInt;

/**
 * FXML Controller class
 *
 * @author dougl
 */
public class FXMLCadastroFuncionarioController implements Initializable {
    @FXML
    private TableView<Funcionario> tableViewFuncionarios;
    @FXML
    private TableColumn<Funcionario, String> tableColumnFuncionarioNome;
    @FXML
    private TableColumn<Funcionario, String> tableColumnFuncionarioCpf;
    @FXML
    private TextField textFieldFuncionarioNome;
    @FXML
    private TextField textFieldFuncionarioCpf;
    @FXML
    private TextField textFieldFuncionarioSalario;
    @FXML
    private ComboBox<String> comboBoxFuncionarioUf;
    @FXML
    private ComboBox<String> comboBoxFuncionarioCidade;
    @FXML
    private CheckBox checkBoxFuncionarioGerente;
    @FXML
    private Button buttonAlterar;
    @FXML
    private Button buttonInserir;
    @FXML
    private Button buttonRemover;
    
    private List<Funcionario> listFuncionario;
    private ObservableList<Funcionario> observableListFuncionario;
    
    private List<String> listUf;
    private ObservableList<String> observableListUf;
    
    private List<String> listCidade;
    private ObservableList<String> observableListCidade;

    //Atributos para manipulação de Banco de Dados
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        funcionarioDAO.setConnection(connection);
        
        carregarTableViewFuncionarios();
        //carregarComboBoxUf();
        //carregarComboBoxCidade();

        // Limpando a exibição dos detalhes do funcionario
        selecionarItemTableViewFuncionarios(null);

        // Listen acionado diante de quaisquer alterações na seleção de itens do TableView
        tableViewFuncionarios.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewFuncionarios(newValue));
    }

    public void carregarTableViewFuncionarios() {
        tableColumnFuncionarioNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnFuncionarioCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));

        listFuncionario = funcionarioDAO.listar();

        observableListFuncionario = FXCollections.observableArrayList(listFuncionario);
        tableViewFuncionarios.setItems(observableListFuncionario);
        
        Funcionario apagar = new Funcionario();
        selecionarItemTableViewFuncionarios(apagar);
    }
    
    public void carregarComboBoxUf() {
        UfDAO ufDAO = new UfDAO();
        ufDAO.setConnection(connection);
        listUf = ufDAO.listar();
        observableListUf = FXCollections.observableArrayList(listUf);
        //comboBoxFuncionarioUf.getItems().add(null);
        comboBoxFuncionarioUf.getItems().clear();
        comboBoxFuncionarioUf.getItems().addAll(observableListUf);
    }
    
    public void carregarComboBoxCidade() {
        CidadeDAO cidadeDAO = new CidadeDAO();
        cidadeDAO.setConnection(connection);
        
        if(comboBoxFuncionarioUf.getValue() == null) {
            listCidade = cidadeDAO.listar();
            observableListCidade = FXCollections.observableArrayList(listCidade);
            comboBoxFuncionarioCidade.getItems().clear();
            comboBoxFuncionarioCidade.getItems().addAll(observableListCidade);
        } else {
            listCidade = cidadeDAO.listarPorUf(comboBoxFuncionarioUf.getValue());
            observableListCidade = FXCollections.observableArrayList(listCidade);
            comboBoxFuncionarioCidade.getItems().clear();
            comboBoxFuncionarioCidade.getItems().addAll(observableListCidade);
        }
        
        comboBoxFuncionarioCidade.setVisibleRowCount(5);
        
    }

    public void selecionarItemTableViewFuncionarios(Funcionario funcionario) {
        if (funcionario != null) {
            textFieldFuncionarioNome.setText(funcionario.getNome());
            textFieldFuncionarioCpf.setText(funcionario.getCpf());
            textFieldFuncionarioSalario.setText(String.valueOf(funcionario.getSalario()));
            comboBoxFuncionarioUf.setValue(pegarNomeUf(funcionario.getCidade()));
            comboBoxFuncionarioCidade.setValue(pegarNomeCidade(funcionario.getCidade()));
            checkBoxFuncionarioGerente.setSelected(funcionario.isGerente());
        } else {
            textFieldFuncionarioNome.setText("");
            textFieldFuncionarioCpf.setText("");
            textFieldFuncionarioSalario.setText("");
        //    comboBoxFuncionarioUf.setValue(null);
        //    comboBoxFuncionarioCidade.setText("");
        }
    }

    @FXML
    public void handleButtonInserir() throws IOException, SQLException {
        Funcionario funcionario = new Funcionario();
        if (validarEntradaDeDados()) {
            funcionario.setNome(textFieldFuncionarioNome.getText());
            funcionario.setCpf(textFieldFuncionarioCpf.getText());
            funcionario.setSalario(Float.parseFloat(textFieldFuncionarioSalario.getText()));
            funcionario.setCidade(pegarIdCidade(comboBoxFuncionarioCidade.getValue()));
            funcionario.setGerente(checkBoxFuncionarioGerente.isSelected());
            
            funcionarioDAO.inserir(funcionario);
            carregarTableViewFuncionarios();
        }
    }

    @FXML
    public void handleButtonAlterar() throws IOException, SQLException {
        Funcionario funcionario = tableViewFuncionarios.getSelectionModel().getSelectedItem();//Obtendo funcionario selecionado
        Funcionario resultado = new Funcionario();
        if (funcionario != null) {
            if (validarEntradaDeDados()) {
                funcionario.setCpf(tableColumnFuncionarioCpf.getText());

                resultado = funcionarioDAO.buscar(funcionario);
                
                resultado.setNome(textFieldFuncionarioNome.getText());
                resultado.setCpf(textFieldFuncionarioCpf.getText());
                funcionario.setSalario(Float.parseFloat(textFieldFuncionarioSalario.getText()));
                funcionario.setCidade(pegarIdCidade(comboBoxFuncionarioCidade.getValue()));
                funcionario.setGerente(checkBoxFuncionarioGerente.isSelected());
                
                funcionarioDAO.alterar(resultado);
                
                carregarTableViewFuncionarios();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um Funcionario na Tabela!");
            alert.show();
        }
    }

    @FXML
    public void handleButtonRemover() throws IOException {
        Funcionario funcionario = tableViewFuncionarios.getSelectionModel().getSelectedItem();
        if (funcionario != null) {
            funcionarioDAO.remover(funcionario);
            carregarTableViewFuncionarios();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um Funcionario na Tabela!");
            alert.show();
        }
    }
    
    private boolean validarEntradaDeDados() {
        String errorMessage = "";

        if (textFieldFuncionarioNome.getText() == null || textFieldFuncionarioNome.getText().length() == 0) {
            errorMessage += "Nome inválido!\n";
        }
        if (textFieldFuncionarioCpf.getText() == null || textFieldFuncionarioCpf.getText().length() != 14) {
            errorMessage += "CPF inválido!\n";
        }
        if (textFieldFuncionarioSalario.getText() == null || textFieldFuncionarioSalario.getText().length() == 0) {
            errorMessage += "Salário inválido!\n";
        }
        if ("".equals(comboBoxFuncionarioUf.getValue())) {
            errorMessage += "UF inválida!\n";
        }
        if ("".equals(comboBoxFuncionarioCidade.getValue())) {
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
