package com.alura.literalura.model;

import com.alura.literalura.DTO.DadosLivros;
import jakarta.persistence.*;

import static java.lang.Integer.getInteger;

@Entity
public class Livros {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;

    private String idioma;

    private Double numeroDownloads;

    @ManyToOne()
    private Autor autor;

    public Livros() {
    }

    public Livros(DadosLivros dados, Autor autor) {
        this.titulo = dados.titulo();
        this.idioma = dados.idioma().get(0);
        this.numeroDownloads = dados.numeroDeDownloads();
        this.autor = autor;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getIdioma() {
        return idioma;
    }

    public Double getNumeroDownloads() {
        return numeroDownloads;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public void setNumeroDownloads(Double numeroDownloads) {
        this.numeroDownloads = numeroDownloads;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    @Override
    public String toString() {
        return "título= " + titulo  +
                ", idioma= " + idioma  +
                ", downloads= " + numeroDownloads +
                ", autor= " + autor.getNome() ;
    }
}

