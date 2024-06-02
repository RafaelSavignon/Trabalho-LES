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
import lojaroupas.model.Marca;

/**
 *
 * @author dougl
 */
public class MarcaDAO {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    
    public List<String> listar() {
        String sql = "SELECT * FROM marca";
        List<String> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Marca marca = new Marca();
                marca.setDescricao(resultado.getString("descricaoMarca"));
                
                retorno.add(marca.getDescricao());
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(MarcaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
    
    
    public boolean inserir(Marca marca) {
        String sql = "INSERT INTO marca(descricaoMarca) VALUES(?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, marca.getDescricao());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            //Logger.getLogger(MarcaDAO.class.getName()).log(Level.SEVERE, null, ex);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Marca já cadastrada!");
            alert.show();
            return false;
        }
    }
}
