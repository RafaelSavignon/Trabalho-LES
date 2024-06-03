/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lojaroupas.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lojaroupas.dao.ProdutoDAO;
import lojaroupas.database.Database;
import lojaroupas.database.DatabaseFactory;
import lojaroupas.model.Produto;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 * FXML Controller class
 *
 * @author dougl
 */
public class FXMLRelatorioProdutoTecidoController implements Initializable {

    @FXML
    private TableView<Produto> tableViewProdutos;
    @FXML
    private TableColumn<Produto, String> tableColumnProduto;
    @FXML
    private TableColumn<Produto, String> tableColumnTecido;
    @FXML
    private Button buttonImprimir;
    
    private List<Produto> listProduto;
    private ObservableList<Produto> observableListProduto;

    //Atributos para manipulação de Banco de Dados
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        produtoDAO.setConnection (connection);
        
        carregarTableViewProdutos();
    }    
    
    public void carregarTableViewProdutos() {
        tableColumnProduto.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        tableColumnTecido.setCellValueFactory(new PropertyValueFactory<>("tecido"));

        listProduto = produtoDAO.listar();

        observableListProduto = FXCollections.observableArrayList(listProduto);
        tableViewProdutos.setItems(observableListProduto);
        System.out.print(observableListProduto);
    }   
    
    @FXML
    public void handleButtonImprimir() throws IOException, JRException {
        URL url = getClass().getResource("/lojaroupas/relatorio/relatorioProdutos.jasper");
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(url);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, connection);//null: caso não existam filtros
        JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);//false: não deixa fechar a aplicação principal
        jasperViewer.setVisible(true);
    }
    
}
