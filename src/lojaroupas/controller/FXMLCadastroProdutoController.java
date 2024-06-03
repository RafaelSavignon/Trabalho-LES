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
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lojaroupas.dao.TipoDAO;
import lojaroupas.dao.MarcaDAO;
import lojaroupas.dao.TecidoDAO;
import lojaroupas.dao.ProdutoDAO;
import lojaroupas.database.Database;
import lojaroupas.database.DatabaseFactory;
import lojaroupas.model.Produto;

/**
 * FXML Controller class
 *
 * @author dougl
 */
public class FXMLCadastroProdutoController implements Initializable {
    @FXML
    private TableView<Produto> tableViewProdutos;

    @FXML
    private TableColumn<Produto, String> tableColumnProdutoNome;

    @FXML
    private TableColumn<Produto, Float> tableColumnProdutoValor;

    @FXML
    private TextField textFieldProdutoDescricao;

    @FXML
    private ComboBox<String> comboBoxProdutoTipo;

    @FXML
    private Button buttonAdicionarTipo;

    @FXML
    private ComboBox<String> comboBoxProdutoMarca;

    @FXML
    private Button buttonAdicionarMarca;

    @FXML
    private ComboBox<String> comboBoxProdutoTecido;

    @FXML
    private Button buttonAdicionarTecido;

    @FXML
    private TextField textFieldProdutoValor;

    @FXML
    private Button buttonAlterar;

    @FXML
    private Button buttonInserir;

    @FXML
    private Button buttonRemover;
    
    
    private List<Produto> listProduto;
    private ObservableList<Produto> observableListProduto;
    
    private List<String> listTipo;
    private ObservableList<String> observableListTipo;
    
    private List<String> listMarca;
    private ObservableList<String> observableListMarca;
    
    private List<String> listTecido;
    private ObservableList<String> observableListTecido;
    //Atributos para manipulação de Banco de Dados
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        produtoDAO.setConnection(connection);
        
        carregarTableViewProdutos();
        //carregarComboBoxTipo();
        //carregarComboBoxTipo();

        // Limpando a exibição dos detalhes do produto
        selecionarItemTableViewProdutos(null);

        // Listen acionado diante de quaisquer alterações na seleção de itens do TableView
        tableViewProdutos.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewProdutos(newValue));
    }

    public void carregarTableViewProdutos() {
        tableColumnProdutoNome.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        tableColumnProdutoValor.setCellValueFactory(new PropertyValueFactory<>("valor"));

        listProduto = produtoDAO.listar();

        observableListProduto = FXCollections.observableArrayList(listProduto);
        tableViewProdutos.setItems(observableListProduto);
        
        Produto apagar = new Produto();
        selecionarItemTableViewProdutos(apagar);
    }
    
    public void carregarComboBoxTipo() {
        TipoDAO tipoDAO = new TipoDAO();
        tipoDAO.setConnection(connection);
        listTipo = tipoDAO.listar();
        observableListTipo = FXCollections.observableArrayList(listTipo);
        //comboBoxProdutoTipo.getItems().add(null);
        comboBoxProdutoTipo.getItems().clear();
        comboBoxProdutoTipo.getItems().addAll(observableListTipo);
    }
    
    public void carregarComboBoxMarca() {
        MarcaDAO marcaDAO = new MarcaDAO();
        marcaDAO.setConnection(connection);
        listMarca = marcaDAO.listar();
        observableListMarca = FXCollections.observableArrayList(listMarca);
        //comboBoxProdutoTipo.getItems().add(null);
        comboBoxProdutoMarca.getItems().clear();
        comboBoxProdutoMarca.getItems().addAll(observableListMarca);
    }
    
    public void carregarComboBoxTecido() {
        TecidoDAO tecidoDAO = new TecidoDAO();
        tecidoDAO.setConnection(connection);
        listTecido = tecidoDAO.listar();
        observableListTecido = FXCollections.observableArrayList(listTecido);
        //comboBoxProdutoTipo.getItems().add(null);
        comboBoxProdutoTecido.getItems().clear();
        comboBoxProdutoTecido.getItems().addAll(observableListTecido);
    }
    
    public void selecionarItemTableViewProdutos(Produto produto) {
        if (produto != null) {
            textFieldProdutoDescricao.setText(produto.getDescricao());
            comboBoxProdutoTipo.setValue(pegarNomeTipo(produto.getTipo()));
            comboBoxProdutoMarca.setValue(pegarNomeMarca(produto.getMarca()));
            comboBoxProdutoTecido.setValue(pegarNomeTecido(produto.getTecido()));
            if(produto.getValor() != 0) {
                textFieldProdutoValor.setText(String.valueOf(produto.getValor()));
            }
        } else {
            textFieldProdutoDescricao.setText("");
            textFieldProdutoValor.setText("");
        }
    }

    @FXML
    public void handleButtonInserir() throws IOException, SQLException {
        Produto produto = new Produto();
        if (validarEntradaDeDados()) {
            produto.setTipo(pegarIdTipo(comboBoxProdutoTipo.getValue()));
            produto.setMarca(pegarIdMarca(comboBoxProdutoMarca.getValue()));
            produto.setTecido(pegarIdTecido(comboBoxProdutoTecido.getValue()));
            produto.setDescricao(textFieldProdutoDescricao.getText());
            produto.setQuantidade(0);
            produto.setValor(Float.parseFloat(textFieldProdutoValor.getText()));
            
            produtoDAO.inserir(produto);
            carregarTableViewProdutos();
        }
    }

    @FXML
    public void handleButtonAlterar() throws IOException, SQLException {
        Produto produto = tableViewProdutos.getSelectionModel().getSelectedItem();//Obtendo produto selecionado
        Produto resultado = new Produto();
        if (produto != null) {
            if (validarEntradaDeDados()) {
                produto.setDescricao(tableColumnProdutoNome.getText());

                resultado = produtoDAO.buscar(produto);
                
                resultado.setTipo(pegarIdTipo(comboBoxProdutoTipo.getValue()));
                resultado.setMarca(pegarIdMarca(comboBoxProdutoMarca.getValue()));
                resultado.setTecido(pegarIdTecido(comboBoxProdutoTecido.getValue()));
                resultado.setDescricao(textFieldProdutoDescricao.getText());
                resultado.setQuantidade(0);
                resultado.setValor(Float.parseFloat(textFieldProdutoValor.getText()));
                
                produtoDAO.alterar(resultado);
                
                carregarTableViewProdutos();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um Produto na Tabela!");
            alert.show();
        }
    }

    @FXML
    public void handleButtonRemover() throws IOException {
        Produto produto = tableViewProdutos.getSelectionModel().getSelectedItem();
        if (produto != null) {
            produtoDAO.remover(produto);
            carregarTableViewProdutos();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um Produto na Tabela!");
            alert.show();
        }
    }
    
    private boolean validarEntradaDeDados() {
        String errorMessage = "";

        if (textFieldProdutoDescricao.getText() == null || textFieldProdutoDescricao.getText().length() == 0) {
            errorMessage += "Descrição inválida!\n";
        }
        if ("".equals(comboBoxProdutoTipo.getValue())) {
            errorMessage += "Tipo inválido!\n";
        }
        if ("".equals(comboBoxProdutoMarca.getValue())) {
            errorMessage += "Marca inválida!\n";
        }
        if ("".equals(comboBoxProdutoTecido.getValue())) {
            errorMessage += "Tecido inválido!\n";
        }
        if (textFieldProdutoValor.getText() == null || textFieldProdutoValor.getText().length() == 0) {
            errorMessage += "Valor inválido!\n";
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
    
    private int pegarIdTipo(String nome) {
        int id = 0;
        String sql = "SELECT idTipo FROM tipo WHERE descricaoTipo = '" + nome + "'";
        try {
            PreparedStatement stmt_tipo = connection.prepareStatement(sql);
            ResultSet resultado = stmt_tipo.executeQuery();
            while(resultado.next()) {
                id = resultado.getInt("idTipo");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(TipoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }
    
    private String pegarNomeTipo(int id) {
        String nome = "";
        String sql = "SELECT descricaoTipo FROM tipo WHERE idTipo = " + id;
        try {
            PreparedStatement stmt_tipo = connection.prepareStatement(sql);
            ResultSet resultado = stmt_tipo.executeQuery();
            while(resultado.next()) {
                nome = resultado.getString("descricaoTipo");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(TipoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nome;
    }
    
    private int pegarIdMarca(String nome) {
        int id = 0;
        String sql = "SELECT idMarca FROM marca WHERE descricaoMarca = '" + nome + "'";
        try {
            PreparedStatement stmt_marca = connection.prepareStatement(sql);
            ResultSet resultado = stmt_marca.executeQuery();
            while(resultado.next()) {
                id = resultado.getInt("idMarca");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(MarcaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }
    
    private String pegarNomeMarca(int id) {
        String nome = "";
        String sql = "SELECT descricaoMarca FROM marca WHERE idMarca = " + id;
        try {
            PreparedStatement stmt_marca = connection.prepareStatement(sql);
            ResultSet resultado = stmt_marca.executeQuery();
            while(resultado.next()) {
                nome = resultado.getString("descricaoMarca");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(MarcaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nome;
    }
    
    private int pegarIdTecido(String nome) {
        int id = 0;
        String sql = "SELECT idTecido FROM tecido WHERE descricaoTecido = '" + nome + "'";
        try {
            PreparedStatement stmt_tecido = connection.prepareStatement(sql);
            ResultSet resultado = stmt_tecido.executeQuery();
            while(resultado.next()) {
                id = resultado.getInt("idTecido");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(TecidoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }
    
    private String pegarNomeTecido(int id) {
        String nome = "";
        String sql = "SELECT descricaoTecido FROM tecido WHERE idTecido = " + id;
        try {
            PreparedStatement stmt_tecido = connection.prepareStatement(sql);
            ResultSet resultado = stmt_tecido.executeQuery();
            while(resultado.next()) {
                nome = resultado.getString("descricaoTecido");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(TecidoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nome;
    }
    
}
