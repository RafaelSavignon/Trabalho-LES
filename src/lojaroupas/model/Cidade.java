/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lojaroupas.model;

/**
 *
 * @author dougl
 */
public class Cidade {
    private String nome;
    private int uf;
    
    public Cidade() {
        
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the uf
     */
    public int getUf() {
        return uf;
    }

    /**
     * @param uf the uf to set
     */
    public void setUf(int uf) {
        this.uf = uf;
    }
}
