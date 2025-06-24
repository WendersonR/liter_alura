package com.alura.literalura.DTO;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosLivros(@JsonAlias("title") String titulo,
                          @JsonAlias("authors") List<DadosAutor> autor,
                          @JsonAlias("languages") List<String> idioma,
                          @JsonAlias("results") List<DadosLivros> resultados,
                          @JsonAlias("download_count") Double numeroDeDownloads) {
}
