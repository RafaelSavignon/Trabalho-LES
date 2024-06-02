/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lojaroupas.model;

import java.time.LocalDate;

public class Produto {
    
    private int id;
    private int tipo;
    private int marca;
    private int tecido;
    private String descricao;
    private int quantidade;
    private float valor;

    
    public Produto(){
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the marca
     */
    public int getMarca() {
        return marca;
    }

    /**
     * @param marca the marca to set
     */
    public void setMarca(int marca) {
        this.marca = marca;
    }

    /**
     * @return the tecido
     */
    public int getTecido() {
        return tecido;
    }

    /**
     * @param tecido the tecido to set
     */
    public void setTecido(int tecido) {
        this.tecido = tecido;
    }

    /**
     * @return the tipo
     */
    public int getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * @return the quantidade
     */
    public int getQuantidade() {
        return quantidade;
    }

    /**
     * @param quantidade the quantidade to set
     */
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    /**
     * @return the valor
     */
    public float getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(float valor) {
        this.valor = valor;
    }

    
}
