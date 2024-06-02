/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lojaroupas.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import lojaroupas.model.Fornecedor;

/**
 *
 * @author dougl
 */
public class FornecedorDAO {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    
    public List<Fornecedor> listar() {
        String sql = "SELECT * FROM fornecedor";
        List<Fornecedor> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Fornecedor fornecedor = new Fornecedor();
                fornecedor.setNome(resultado.getString("nomeFornecedor"));
                fornecedor.setCnpj(resultado.getString("cnpjFornecedor"));
                fornecedor.setCidade(resultado.getInt("cidadeFornecedor"));
                
                retorno.add(fornecedor);
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(FornecedorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public boolean inserir(Fornecedor fornecedor) {
        String sql = "INSERT INTO fornecedor(cnpjFornecedor, nomeFornecedor, cidadeFornecedor) VALUES(?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, fornecedor.getCnpj());
            stmt.setString(2, fornecedor.getNome());
            stmt.setInt(3, fornecedor.getCidade());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            //Logger.getLogger(FornecedorDAO.class.getName()).log(Level.SEVERE, null, ex);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Fornecedor já cadastrado!");
            alert.setContentText("O CNPJ inserido já está cadastrado, por favor confirme os dados de entrada.");
            alert.show();
            alert.show();
            return false;
        }
    }
    
    public void alterar(Fornecedor fornecedor) {
        String sql = "UPDATE fornecedor SET nomeFornecedor = ?, cidadeFornecedor = ? WHERE cnpjFornecedor = ?";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, fornecedor.getNome());
            stmt.setInt(2, fornecedor.getCidade());
            stmt.setString(3, fornecedor.getCnpj());
            stmt.execute();


        } catch (SQLException e) {
            System.out.println("Erro ao alterar o fornecedor: " + e.getMessage());
        }
    }

    public boolean remover(Fornecedor fornecedor, String cnpj) {
        String sql = "DELETE FROM fornecedor WHERE cnpjFornecedor='" + cnpj + "'";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(FornecedorDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public Fornecedor buscar(Fornecedor fornecedor, String cnpj) {
        String sql = "SELECT * FROM fornecedor WHERE cnpjFornecedor='" + cnpj + "'";
        Fornecedor retorno = new Fornecedor();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                fornecedor.setNome(resultado.getString("nomeFornecedor"));
                fornecedor.setCnpj(resultado.getString("cnpjFornecedor"));
                fornecedor.setCidade(resultado.getInt("cidadeFornecedor"));
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(FornecedorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
}
