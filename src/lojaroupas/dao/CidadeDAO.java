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
import lojaroupas.model.Cidade;
import lojaroupas.model.Uf;

/**
 *
 * @author dougl
 */
public class CidadeDAO {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    
    public List<String> listar() {
        String sql = "SELECT * FROM cidade";
        List<String> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Cidade cidade = new Cidade();
                cidade.setNome(resultado.getString("nomeCidade"));
                cidade.setUf(resultado.getInt("ufCidade"));
                
                retorno.add(cidade.getNome());
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(CidadeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
    
    public List<String> listarPorUf(String uf) {
        List<String> retorno = new ArrayList<>();
        int resultado_uf = 0;
        String sql_uf = "SELECT idUf FROM uf WHERE nomeUf = '" + uf + "'";
        try {
            PreparedStatement stmt_uf = connection.prepareStatement(sql_uf);
            ResultSet resultado = stmt_uf.executeQuery();
            while(resultado.next()) {
                resultado_uf = resultado.getInt("idUf");
            }
            
            String sql = "SELECT * FROM cidade WHERE ufCidade = '" + resultado_uf + "'";
            //List<String> retorno = new ArrayList<>();
            try {
                PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet resultado_cidade = stmt.executeQuery();
                while (resultado_cidade.next()) {
                    Cidade cidade = new Cidade();
                    cidade.setNome(resultado_cidade.getString("nomeCidade"));
                    cidade.setUf(resultado_cidade.getInt("ufCidade"));

                    retorno.add(cidade.getNome());

                }
            } catch (SQLException ex) {
                Logger.getLogger(CidadeDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            return retorno;
            
        } catch (SQLException ex) {
            Logger.getLogger(CidadeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
}
