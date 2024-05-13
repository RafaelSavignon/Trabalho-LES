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
import lojaroupas.model.Funcionario;

/**
 *
 * @author dougl
 */
public class FuncionarioDAO {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    
    public List<Funcionario> listar() {
        String sql = "SELECT * FROM funcionario";
        List<Funcionario> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Funcionario funcionario = new Funcionario();
                funcionario.setCpf(resultado.getString("cpfFuncionario"));
                funcionario.setNome(resultado.getString("nomeFuncionario"));
                funcionario.setSalario(resultado.getFloat("salarioFuncionario"));
                funcionario.setCidade(resultado.getInt("cidadeFuncionario"));
                funcionario.setGerente(resultado.getBoolean("eGerente"));
                
                retorno.add(funcionario);
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(FuncionarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public boolean inserir(Funcionario funcionario) {
        String sql = "INSERT INTO funcionario(cpfFuncionario, nomeFuncionario, salarioFuncionario, cidadeFuncionario, eGerente) VALUES(?,?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, funcionario.getCpf());
            stmt.setString(2, funcionario.getNome());
            stmt.setFloat(3, funcionario.getSalario());
            stmt.setInt(4, funcionario.getCidade());
            stmt.setBoolean(5, funcionario.isGerente());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            //Logger.getLogger(FuncionarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Funcionário já cadastrado!");
            alert.setContentText("O CPF inserido já está cadastrado, por favor confirme os dados de entrada.");
            alert.show();
            return false;
        }
    }
    
    public void alterar(Funcionario funcionario) {
        String sql = "UPDATE funcionario SET nomeFuncionario = ?, salarioFuncionario = ?, cidadeFuncionario = ?, eGerente = ? WHERE cpfFuncionario = ?";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, funcionario.getNome());
            stmt.setFloat(2, funcionario.getSalario());
            stmt.setInt(3, funcionario.getCidade());
            stmt.setBoolean(4, funcionario.isGerente());
            stmt.setString(5, funcionario.getCpf());
            stmt.execute();


        } catch (SQLException e) {
            System.out.println("Erro ao alterar o funcionário: " + e.getMessage());
        }
    }

    public boolean remover(Funcionario funcionario) {
        String sql = "DELETE FROM funcionario WHERE cpfFuncionario=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, funcionario.getCpf());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(FuncionarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public Funcionario buscar(Funcionario funcionario) {
        String sql = "SELECT * FROM funcionario WHERE cpfFuncionario=?";
        Funcionario retorno = new Funcionario();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, funcionario.getCpf());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                funcionario.setCpf(resultado.getString("cpfFuncionario"));
                funcionario.setNome(resultado.getString("nomeFuncionario"));
                funcionario.setSalario(resultado.getFloat("salarioFuncionario"));
                funcionario.setCidade(resultado.getInt("cidadeFuncionario"));
                funcionario.setGerente(resultado.getBoolean("eGerente"));
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(FuncionarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
}
