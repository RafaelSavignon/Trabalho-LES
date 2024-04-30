/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lojaroupas.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
        carregarComboBoxUf();
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
        //System.out.print(observableListUf);
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
        //    comboBoxClienteUf.setText(cliente.getCidade());
        //    comboBoxClienteCidade.setValue(cliente.getCidade());
        } else {
            textFieldClienteNome.setText("");
            textFieldClienteCpf.setText("");
            textFieldClienteTelefone.setText("");
        //    comboBoxClienteUf.setValue();
        //    comboBoxClienteCidade.setText("");
        }
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
