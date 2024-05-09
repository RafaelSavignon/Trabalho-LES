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
import lojaroupas.dao.ClienteDAO;
import lojaroupas.dao.UfDAO;
import lojaroupas.database.Database;
import lojaroupas.database.DatabaseFactory;
import lojaroupas.model.Cidade;
import lojaroupas.model.Cliente;
import lojaroupas.model.Uf;
import static org.postgresql.jdbc.PgResultSet.toFloat;
import static org.postgresql.jdbc.PgResultSet.toInt;

/**
 * FXML Controller class
 *
 * @author dougl
 */
public class FXMLCadastroClienteController implements Initializable {
    @FXML
    private TableView<Cliente> tableViewClientes;
    @FXML
    private TableColumn<Cliente, String> tableColumnClienteNome;
    @FXML
    private TableColumn<Cliente, String> tableColumnClienteCpf;
    @FXML
    private TextField textFieldClienteNome;
    @FXML
    private TextField textFieldClienteCpf;
    @FXML
    private TextField textFieldClienteTelefone;
    @FXML
    private ComboBox<String> comboBoxClienteUf;
    @FXML
    private ComboBox<String> comboBoxClienteCidade;
    @FXML
    private Button buttonAlterar;
    @FXML
    private Button buttonInserir;
    @FXML
    private Button buttonRemover;
    
    private List<Cliente> listCliente;
    private ObservableList<Cliente> observableListCliente;
    
    private List<String> listUf;
    private ObservableList<String> observableListUf;
    
    private List<String> listCidade;
    private ObservableList<String> observableListCidade;

    //Atributos para manipulação de Banco de Dados
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clienteDAO.setConnection(connection);
        
        carregarTableViewClientes();
        //carregarComboBoxUf();
        //carregarComboBoxCidade();

        // Limpando a exibição dos detalhes do cliente
        selecionarItemTableViewClientes(null);

        // Listen acionado diante de quaisquer alterações na seleção de itens do TableView
        tableViewClientes.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewClientes(newValue));
    }

    public void carregarTableViewClientes() {
        tableColumnClienteNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnClienteCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));

        listCliente = clienteDAO.listar();

        observableListCliente = FXCollections.observableArrayList(listCliente);
        tableViewClientes.setItems(observableListCliente);
        
        Cliente apagar = new Cliente();
        selecionarItemTableViewClientes(apagar);
    }
    
    public void carregarComboBoxUf() {
        UfDAO ufDAO = new UfDAO();
        ufDAO.setConnection(connection);
        listUf = ufDAO.listar();
        observableListUf = FXCollections.observableArrayList(listUf);
        //comboBoxClienteUf.getItems().add(null);
        comboBoxClienteUf.getItems().clear();
        comboBoxClienteUf.getItems().addAll(observableListUf);
    }
    
    public void carregarComboBoxCidade() {
        CidadeDAO cidadeDAO = new CidadeDAO();
        cidadeDAO.setConnection(connection);
        
        if(comboBoxClienteUf.getValue() == null) {
            listCidade = cidadeDAO.listar();
            observableListCidade = FXCollections.observableArrayList(listCidade);
            comboBoxClienteCidade.getItems().clear();
            comboBoxClienteCidade.getItems().addAll(observableListCidade);
        } else {
            listCidade = cidadeDAO.listarPorUf(comboBoxClienteUf.getValue());
            observableListCidade = FXCollections.observableArrayList(listCidade);
            comboBoxClienteCidade.getItems().clear();
            comboBoxClienteCidade.getItems().addAll(observableListCidade);
        }
        
        comboBoxClienteCidade.setVisibleRowCount(5);
        
    }

    public void selecionarItemTableViewClientes(Cliente cliente) {
        if (cliente != null) {
            textFieldClienteNome.setText(cliente.getNome());
            textFieldClienteCpf.setText(cliente.getCpf());
            textFieldClienteTelefone.setText(cliente.getTelefone());
            comboBoxClienteUf.setValue(pegarNomeUf(cliente.getCidade()));
            comboBoxClienteCidade.setValue(pegarNomeCidade(cliente.getCidade()));
        } else {
            textFieldClienteNome.setText("");
            textFieldClienteCpf.setText("");
            textFieldClienteTelefone.setText("");
        //    comboBoxClienteUf.setValue(null);
        //    comboBoxClienteCidade.setText("");
        }
    }

    @FXML
    public void handleButtonInserir() throws IOException, SQLException {
        Cliente cliente = new Cliente();
        if (validarEntradaDeDados()) {
            cliente.setNome(textFieldClienteNome.getText());
            cliente.setCpf(textFieldClienteCpf.getText());
            cliente.setTelefone(textFieldClienteTelefone.getText());
            cliente.setCidade(pegarIdCidade(comboBoxClienteCidade.getValue()));
            
            clienteDAO.inserir(cliente);
            carregarTableViewClientes();
        }
    }

    @FXML
    public void handleButtonAlterar() throws IOException, SQLException {
        Cliente cliente = tableViewClientes.getSelectionModel().getSelectedItem();//Obtendo cliente selecionado
        Cliente resultado = new Cliente();
        if (cliente != null) {
            if (validarEntradaDeDados()) {
                cliente.setCpf(tableColumnClienteCpf.getText());

                resultado = clienteDAO.buscar(cliente);
                
                resultado.setNome(textFieldClienteNome.getText());
                resultado.setCpf(textFieldClienteCpf.getText());
                resultado.setTelefone(textFieldClienteTelefone.getText());
                resultado.setCidade(pegarIdCidade(comboBoxClienteCidade.getValue()));
                
                clienteDAO.alterar(resultado);
                
                carregarTableViewClientes();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um Cliente na Tabela!");
            alert.show();
        }
    }

    @FXML
    public void handleButtonRemover() throws IOException {
        Cliente cliente = tableViewClientes.getSelectionModel().getSelectedItem();
        if (cliente != null) {
            clienteDAO.remover(cliente);
            carregarTableViewClientes();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um Cliente na Tabela!");
            alert.show();
        }
    }
    
    private boolean validarEntradaDeDados() {
        String errorMessage = "";

        if (textFieldClienteNome.getText() == null || textFieldClienteNome.getText().length() == 0) {
            errorMessage += "Nome inválido!\n";
        }
        if (textFieldClienteCpf.getText() == null || textFieldClienteCpf.getText().length() != 14) {
            errorMessage += "CPF inválido!\n";
        }
        if (textFieldClienteTelefone.getText() == null || textFieldClienteTelefone.getText().length() == 0) {
            errorMessage += "Telefone inválido!\n";
        }
        if ("".equals(comboBoxClienteUf.getValue())) {
            errorMessage += "UF inválida!\n";
        }
        if ("".equals(comboBoxClienteCidade.getValue())) {
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
