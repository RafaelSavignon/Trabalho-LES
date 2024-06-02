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
import lojaroupas.model.Tecido;

/**
 *
 * @author dougl
 */
public class TecidoDAO {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    
    public List<String> listar() {
        String sql = "SELECT * FROM tecido";
        List<String> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Tecido tecido = new Tecido();
                tecido.setDescricao(resultado.getString("descricaoTecido"));
                
                retorno.add(tecido.getDescricao());
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(TecidoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
    
    
    public boolean inserir(Tecido tecido) {
        String sql = "INSERT INTO tecido(descricaoTecido) VALUES(?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, tecido.getDescricao());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            //Logger.getLogger(TecidoDAO.class.getName()).log(Level.SEVERE, null, ex);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Tecido já cadastrado!");
            alert.show();
            return false;
        }
    }
}
