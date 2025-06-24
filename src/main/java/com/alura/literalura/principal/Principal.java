package com.alura.literalura.principal;

import com.alura.literalura.DTO.DadosAutor;
import com.alura.literalura.model.Autor;
import com.alura.literalura.DTO.DadosLivros;
import com.alura.literalura.model.Livros;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LivroRepository;
import com.alura.literalura.service.ConsumoApi;
import com.alura.literalura.service.ConverteDados;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Scanner;

@Component
public class Principal {

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;


    private final String ENDERECO = "http://gutendex.com/books/";
    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private String json;

    @Autowired
    public Principal(LivroRepository livroRepository, AutorRepository autorRepository) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
    }

    public void exibeMenu() {
        int opcao = -1;

        while (opcao != 0) {
            String menu = """
                    *************
                    Escolha o número da sua opção:
                    *************
                    1 - Buscar livro por título:
                    2 - Listar livros registrados:
                    3 - listar autores registrados:
                    4 - Listar autores vivos em um ano determinado:
                    5 - Listar livros em um determinado idioma:
                    6 - Top 10 livros com mais downloads:
                    
                    0 - sair
                    *************
                    """;

            json = consumo.obterDados(ENDERECO);
            System.out.println(menu);
            System.out.println("Digite a opção desejada!");
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarLivroPorTitulo();
                    break;
                case 2:
                    listandoLivrosRegistrados();
                    break;
                case 3:
                    listandoAutoresRegistrados();
                    break;
                case 4:
                    listandoAutoresVivos();
                    break;
                case 5:
                    listandoLivrosPorIdioma();
                    break;
                case 6:
                    top10LivrosComMaisDownloads();
                    break;
                case 0:
                    System.out.println("Encerrando o programa...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }




    private void buscarLivroPorTitulo() {
        DadosLivros dadosLivros = obterdadosLivros();
        if (dadosLivros != null){
            DadosAutor dadosAutor = dadosLivros.autor().get(0);
            Livros livros;
            Autor autorExistente = autorRepository.findByNome(dadosAutor.nome());

            if(autorExistente != null){
                livros = new Livros(dadosLivros, autorExistente);
            }else{
                Autor novoAutor = new Autor(dadosAutor);
                livros = new Livros(dadosLivros, novoAutor);
                autorRepository.save(novoAutor);
            }
            try{
                livroRepository.save(livros);
                System.out.println(livros);
            } catch (Exception e){
                System.out.println("Livro ja cadastrado no Banco de Dados!");
            }
        }
    }

    private DadosLivros obterdadosLivros() {
        System.out.println("Digite o nome do livro: ");
        var nomeDoLivro = leitura.nextLine();
        json = consumo.obterDados(ENDERECO + "?search=" + nomeDoLivro.replace(" ", "+"));

        var dadosBusca = conversor.obterDados(json, DadosLivros.class);

        // Pega o primeiro resultado sem filtro adicional
        return dadosBusca.resultados().stream()
                .findFirst()
                .orElse(null);
    }


    private void listandoLivrosRegistrados() {
        var livros = livroRepository.findAll();

        if (livros.isEmpty()) {
            System.out.println("Nenhum livro registrado.");
        } else {
            System.out.println("Lista de livros registrados:");
            livros.forEach(livro -> System.out.printf("""
                --------------------------
                Título: %s
                Idioma: %s
                Número de downloads: %.0f
                Autor: %s
                --------------------------
                """,
                    livro.getTitulo(),
                    livro.getIdioma(),
                    livro.getNumeroDownloads(),
                    livro.getAutor().getNome()));
            }
        }

    private void listandoAutoresRegistrados() {
        var autores = autorRepository.findAll();

        if (autores.isEmpty()) {
            System.out.println("Nenhum autor registrado.");
        } else {
            System.out.println("Autores registrados:");
            autores.forEach(a -> {
                System.out.printf("""
                -----------------------------
                Nome: %s
                Ano nascimento: %s
                Ano falecimento: %s
                -----------------------------
                """,
                        a.getNome(),
                        a.getAnoNascimento(),
                        a.getAnoFalecimento());
            });
        }
    }



    private void listandoAutoresVivos() {
            System.out.print("Digite o ano para verificar autores vivos: ");
            int ano = leitura.nextInt();
            leitura.nextLine();

            var autoresVivos = autorRepository.findByAnoNascimentoLessThanEqualAndAnoFalecimentoGreaterThanEqual(ano, ano);

            if (autoresVivos.isEmpty()) {
                System.out.println("Nenhum autor vivo no ano informado.");
            } else {
                System.out.println("Autores vivos no ano " + ano + ":");
                autoresVivos.forEach(a -> {
                    System.out.printf("""
                -----------------------------
                Nome: %s
                Nascimento: %d
                Falecimento: %d
                -----------------------------
                """,
                            a.getNome(),
                            a.getAnoNascimento(),
                            a.getAnoFalecimento());
                });
            }
        }

    private void listandoLivrosPorIdioma() {
        System.out.print("Digite o idioma (ex: 'en', 'pt', 'es'): ");
        String idioma = leitura.nextLine();

        var livros = livroRepository.findByIdiomaIgnoreCase(idioma);

        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado nesse idioma.");
        } else {
            System.out.println("Livros no idioma '" + idioma + "':");
            livros.forEach(l -> {
                System.out.printf("""
                -----------------------------
                Título: %s
                Autor: %s
                Downloads: %.0f
                -----------------------------
                """,
                        l.getTitulo(),
                        l.getAutor().getNome(),
                        l.getNumeroDownloads());
            });
        }
    }

    private void top10LivrosComMaisDownloads() {
        var livros = livroRepository.findTop10ByOrderByNumeroDownloadsDesc();

        if (livros.isEmpty()) {
            System.out.println("Nenhum livro registrado.");
        } else {
            System.out.println("Top 10 livros com mais downloads:");
            livros.forEach(l -> {
                System.out.printf("""
                -----------------------------
                Título: %s
                Autor: %s
                Downloads: %.0f
                -----------------------------
                """,
                        l.getTitulo(),
                        l.getAutor().getNome(),
                        l.getNumeroDownloads());
            });
        }
    }

}
