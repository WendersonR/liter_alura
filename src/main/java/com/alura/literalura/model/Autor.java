package com.alura.literalura.model;

import com.alura.literalura.DTO.DadosAutor;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private Integer anoNascimento;

    private Integer anoFalecimento;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Livros> livros = new ArrayList<>();

    public Autor() {
    }

    public Autor(DadosAutor dados) {
        this.nome = dados.nome();
        this.anoNascimento = dados.anoDeNascimento();
        this.anoFalecimento = dados.anoDeFalecimento();
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Integer getAnoNascimento() {
        return anoNascimento;
    }

    public Integer getAnoFalecimento() {
        return anoFalecimento;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setAnoNascimento(Integer anoNascimento) {
        this.anoNascimento = anoNascimento;
    }

    public void setAnoFalecimento(Integer anoFalecimento) {
        this.anoFalecimento = anoFalecimento;
    }

    @Override
    public String toString() {
        return "nome= " + nome  +
                ", nascimento= " + anoNascimento +
                ", falecimento= " + anoFalecimento;
    }
}

