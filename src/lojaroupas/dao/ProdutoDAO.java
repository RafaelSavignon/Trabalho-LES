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
import lojaroupas.model.Produto;

/**
 *
 * @author dougl
 */
public class ProdutoDAO {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    
    public List<Produto> listar() {
        String sql = "SELECT * FROM produto";
        List<Produto> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Produto produto = new Produto();
                produto.setTipo(resultado.getInt("tipoProduto"));
                produto.setMarca(resultado.getInt("marcaProduto"));
                produto.setTecido(resultado.getInt("tecidoProduto"));
                produto.setDescricao(resultado.getString("descricaoProduto"));
                produto.setQuantidade(resultado.getInt("quantidadeProduto"));
                produto.setValor(resultado.getFloat("valorProduto"));
                
                retorno.add(produto);
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public boolean inserir(Produto produto) {
        String sql = "INSERT INTO produto(tipoProduto, marcaProduto, tecidoProduto, descricaoProduto, quantidadeProduto, valorProduto) VALUES(?,?,?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, produto.getTipo());
            stmt.setInt(2, produto.getMarca());
            stmt.setInt(3, produto.getTecido());
            stmt.setString(4, produto.getDescricao());
            stmt.setInt(5, 0);
            stmt.setFloat(6, produto.getValor());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            //Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Produto já cadastrado!");
            alert.show();
            return false;
        }
    }
    
    public void alterar(Produto produto) {
        String sql = "UPDATE produto SET tipoProduto = ?, marcaProduto = ?, tecidoProduto = ?, descricaoProduto = ?, valorProduto = ? WHERE idProduto = ?";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, produto.getTipo());
            stmt.setInt(2, produto.getMarca());
            stmt.setInt(3, produto.getTecido());
            stmt.setString(4, produto.getDescricao());
            stmt.setFloat(5, produto.getValor());
            stmt.setInt(6, produto.getId());
            stmt.execute();


        } catch (SQLException e) {
            System.out.println("Erro ao alterar o produto: " + e.getMessage());
        }
    }

    public boolean remover(Produto produto) {
        String sql = "DELETE FROM produto WHERE idProduto=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, produto.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public Produto buscar(Produto produto) {
        String sql = "SELECT * FROM produto WHERE descricaoProduto=?";
        Produto retorno = new Produto();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, produto.getId());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                produto.setTipo(resultado.getInt("tipoProduto"));
                produto.setMarca(resultado.getInt("marcaProduto"));
                produto.setTecido(resultado.getInt("tecidoProduto"));
                produto.setDescricao(resultado.getString("descricaoProduto"));
                produto.setQuantidade(resultado.getInt("quantidadeProduto"));
                produto.setValor(resultado.getFloat("valorProduto"));
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
}
