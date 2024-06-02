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
import lojaroupas.model.Tipo;

/**
 *
 * @author dougl
 */
public class TipoDAO {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    
    public List<String> listar() {
        String sql = "SELECT * FROM tipo";
        List<String> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Tipo tipo = new Tipo();
                tipo.setDescricao(resultado.getString("descricaoTipo"));
                
                retorno.add(tipo.getDescricao());
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(TipoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
    
    
    public boolean inserir(Tipo tipo) {
        String sql = "INSERT INTO tipo(descricaoTipo) VALUES(?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, tipo.getDescricao());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            //Logger.getLogger(TipoDAO.class.getName()).log(Level.SEVERE, null, ex);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Tipo já cadastrado!");
            alert.show();
            return false;
        }
    }
}
