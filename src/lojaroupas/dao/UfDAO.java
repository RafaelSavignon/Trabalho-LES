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
import lojaroupas.model.Uf;

/**
 *
 * @author dougl
 */
public class UfDAO {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    
    public List<String> listar() {
        String sql = "SELECT * FROM uf";
        List<String> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Uf uf = new Uf();
                uf.setNome(resultado.getString("nomeUf"));
                
                retorno.add(uf.getNome());
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(UfDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
}
