package com.alura.literalura.repository;

import com.alura.literalura.model.Livros;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivroRepository extends JpaRepository<Livros, Long> {

    Livros findByTitulo(String titulo);

    List<Livros> findByIdiomaIgnoreCase(String idioma);

    List<Livros> findTop10ByOrderByNumeroDownloadsDesc();
}
