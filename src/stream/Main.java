package stream;

import java.time.Duration;
import java.util.Scanner;

import stream.catalogo.Catalogo;
import stream.conteudo.Conteudo;
import stream.conteudo.Filme;
import stream.conteudo.Podcast;
import stream.conteudo.Serie;
import stream.usuario.Adulto;
import stream.usuario.Infantil;
import stream.usuario.SuperClassUsuarios;

/**
 * Menu interativo do StreamHub (console).
 */
public class Main {

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        Catalogo catalogo = new Catalogo();
        carregarDadosIniciais(catalogo);

        // Pode ser Infantil ou Adulto: a variavel e do tipo da superclasse (polimorfismo)
        SuperClassUsuarios usuarioAtual = new Adulto("Convidado", 18);
        int opcao = -1;

        while (opcao != 0) {
            exibirMenu(usuarioAtual);
            opcao = lerInteiro(entrada, "Escolha uma opcao: ");
            System.out.println();

            switch (opcao) {
                case 1:
                    cadastrarConteudo(entrada, catalogo);
                    break;
                case 2:
                    listarCatalogo(entrada, catalogo, usuarioAtual);
                    break;
                case 3:
                    verDetalhes(entrada, catalogo);
                    break;
                case 4:
                    reproduzir(entrada, catalogo, usuarioAtual);
                    break;
                case 5:
                    avaliar(entrada, catalogo);
                    break;
                case 6:
                    usuarioAtual = trocarUsuario(entrada, usuarioAtual);
                    break;
                case 7:
                    relatorioRoyalties(entrada, catalogo);
                    break;
                case 8:
                    System.out.println("--- LOG DE AUDITORIA ---");
                    catalogo.exibirLogAuditoria();
                    break;
                case 9:
                    executarCasoDeUso(catalogo);
                    break;
                case 0:
                    System.out.println("Encerrando o StreamHub. Ate logo!");
                    break;
                default:
                    System.out.println("Opcao invalida. Escolha um numero de 0 a 9.");
            }
        }

        entrada.close();
    }

    // ---------- Menu ----------

    private static void exibirMenu(SuperClassUsuarios usuarioAtual) {
        System.out.println();
        System.out.println("=============================================");
        System.out.println("   STREAMHUB - Plataforma de Streaming");
        System.out.println("   Perfil ativo: " + usuarioAtual);
        System.out.println("=============================================");
        System.out.println("1 - Cadastrar conteudo");
        System.out.println("2 - Listar catalogo");
        System.out.println("3 - Ver detalhes de um conteudo");
        System.out.println("4 - Reproduzir conteudo");
        System.out.println("5 - Avaliar conteudo (1 a 5 estrelas)");
        System.out.println("6 - Trocar de perfil");
        System.out.println("7 - Relatorio de royalties");
        System.out.println("8 - Log de auditoria");
        System.out.println("9 - Executar o caso de uso do roteiro (demonstracao)");
        System.out.println("0 - Sair");
        System.out.println("---------------------------------------------");
    }

    // ---------- Opcoes do menu ----------

    private static void cadastrarConteudo(Scanner entrada, Catalogo catalogo) {
        System.out.println("--- CADASTRAR CONTEUDO ---");
        int tipo = escolherTipo(entrada);
        if (tipo == 0) {
            return;
        }

        String nome = lerTexto(entrada, "Nome: ");
        String categoria = lerTexto(entrada, "Categoria (ex: Terror, Comedia): ");
        String criador = lerTexto(entrada, "Criador (quem recebe os royalties): ");
        int faixa = lerInteiro(entrada, "Faixa etaria (0=livre, 10, 12, 14, 16 ou 18): ");

        // Os construtores e setters validam os dados e lancam IllegalArgumentException se algo estiver errado.
        try {
            Conteudo novo;
            if (tipo == 1) {
                int minutos = lerInteiro(entrada, "Duracao em minutos: ");
                novo = new Filme(nome, categoria, faixa, criador, Duration.ofMinutes(minutos));
            } else if (tipo == 2) {
                int temporadas = lerInteiro(entrada, "Numero de temporadas: ");
                int episodios = lerInteiro(entrada, "Episodios por temporada: ");
                int minutos = lerInteiro(entrada, "Duracao de cada episodio (minutos): ");
                Serie serie = new Serie(nome, categoria, faixa, criador);
                montarSerie(serie, temporadas, episodios, minutos);
                novo = serie;
            } else {
                int numero = lerInteiro(entrada, "Numero do episodio: ");
                int minutos = lerInteiro(entrada, "Minutos de audio: ");
                novo = new Podcast(nome, categoria, faixa, criador, numero, Duration.ofMinutes(minutos));
            }

            catalogo.adicionar(novo);
            System.out.println("Conteudo cadastrado com sucesso: " + novo);
        } catch (IllegalArgumentException e) {
            System.out.println("Cadastro cancelado: " + e.getMessage());
        }
    }

    // Preenche a serie: cria as temporadas e coloca os episodios (todos com a mesma duracao).
    private static void montarSerie(Serie serie, int temporadas, int episodios, int minutos) {
        if (temporadas <= 0) {
            throw new IllegalArgumentException("A serie deve ter pelo menos 1 temporada.");
        }
        if (episodios <= 0) {
            throw new IllegalArgumentException("Cada temporada deve ter pelo menos 1 episodio.");
        }
        for (int t = 1; t <= temporadas; t++) {
            serie.addTemporada();
            for (int e = 1; e <= episodios; e++) {
                serie.addEpisodio(t, Duration.ofMinutes(minutos));
            }
        }
    }

    private static void listarCatalogo(Scanner entrada, Catalogo catalogo, SuperClassUsuarios usuario) {
        System.out.println("--- LISTAR CATALOGO ---");
        System.out.println("1 - Tudo");
        System.out.println("2 - Somente o liberado para o perfil atual (" + usuario.getNome() + ")");
        System.out.println("3 - Por tipo");
        int escolha = lerInteiro(entrada, "Opcao: ");
        System.out.println();

        switch (escolha) {
            case 1:
                catalogo.listar();
                break;
            case 2:
                catalogo.listar(usuario);
                break;
            case 3: {
                int tipo = escolherTipo(entrada);
                if (tipo != 0) {
                    System.out.println();
                    catalogo.listar(nomeDoTipo(tipo));
                }
                break;
            }
            default:
                System.out.println("Opcao invalida.");
        }
    }

    private static void verDetalhes(Scanner entrada, Catalogo catalogo) {
        System.out.println("--- DETALHES DE UM CONTEUDO ---");
        catalogo.listar();
        int numero = lerInteiro(entrada, "Numero do conteudo: ");
        System.out.println();

        Conteudo escolhido = catalogo.buscar(numero);
        if (escolhido == null) {
            System.out.println("Numero invalido. Escolha um numero da lista.");
        } else {
            escolhido.exibirDetalhes();
        }
    }

    private static void reproduzir(Scanner entrada, Catalogo catalogo, SuperClassUsuarios usuario) {
        System.out.println("--- REPRODUZIR (perfil: " + usuario.getNome() + ") ---");
        catalogo.listar();
        int numero = lerInteiro(entrada, "Numero do conteudo: ");
        System.out.println();

        Conteudo escolhido = catalogo.buscar(numero);
        if (escolhido == null) {
            System.out.println("Numero invalido. Escolha um numero da lista.");
        } else {
            catalogo.reproduzir(escolhido, usuario);
        }
    }

    private static void avaliar(Scanner entrada, Catalogo catalogo) {
        System.out.println("--- AVALIAR CONTEUDO ---");
        catalogo.listar();
        int numero = lerInteiro(entrada, "Numero do conteudo: ");

        Conteudo escolhido = catalogo.buscar(numero);
        if (escolhido == null) {
            System.out.println("Numero invalido. Escolha um numero da lista.");
            return;
        }

        int estrelas = lerInteiro(entrada, "Nota de 1 a 5 estrelas: ");
        try {
            escolhido.avaliar(estrelas);
            System.out.println("Avaliacao registrada! \"" + escolhido.getNome()
                    + "\" agora tem " + escolhido.getNotaTexto() + ".");
        } catch (IllegalArgumentException e) {
            System.out.println("Avaliacao nao registrada: " + e.getMessage());
        }
    }

    // A idade decide o tipo de conta: menor de 18 anos = Infantil, 18 ou mais = Adulto.
    private static SuperClassUsuarios trocarUsuario(Scanner entrada, SuperClassUsuarios usuarioAtual) {
        System.out.println("--- TROCAR DE PERFIL ---");
        String nome = lerTexto(entrada, "Nome do perfil: ");
        int idade = lerInteiro(entrada, "Idade: ");

        try {
            SuperClassUsuarios novo;
            if (idade < 18) {
                novo = new Infantil(nome, idade);
            } else {
                novo = new Adulto(nome, idade);
            }
            System.out.println("Perfil ativo agora: " + novo);
            return novo;
        } catch (IllegalArgumentException e) {
            System.out.println("Perfil nao alterado: " + e.getMessage());
            return usuarioAtual;
        }
    }

    private static void relatorioRoyalties(Scanner entrada, Catalogo catalogo) {
        System.out.println("--- RELATORIO DE ROYALTIES ---");
        catalogo.exibirRelatorioRoyalties();
        System.out.println();

        String criador = lerTexto(entrada, "Ver o total de um criador? (digite o nome ou Enter para pular): ");
        if (!criador.isEmpty()) {
            double total = catalogo.calcularTotalRoyalties(criador);
            System.out.println(String.format("Royalties de %s: R$ %.2f", criador, total));
        }
    }

    // Reproduz exatamente o caso de uso de teste que o roteiro pede para o Tema 6.
    private static void executarCasoDeUso(Catalogo catalogo) {
        System.out.println("=== CASO DE USO DO ROTEIRO ===");
        System.out.println("Um usuario de 14 anos tenta dar play em um filme de terror classificado como 18 anos.");
        System.out.println();

        Conteudo terror = catalogo.buscar("Noite Sem Fim");
        if (terror == null) {
            System.out.println("O filme de exemplo \"Noite Sem Fim\" nao esta no catalogo.");
            return;
        }

        SuperClassUsuarios adolescente = new Infantil("Perfil Teen", 14);
        System.out.println("Perfil: " + adolescente);
        System.out.println("Tentando reproduzir: " + terror);
        catalogo.reproduzir(terror, adolescente);

        System.out.println();
        System.out.println("Log de auditoria do catalogo:");
        catalogo.exibirLogAuditoria();

        System.out.println();
        SuperClassUsuarios adulto = new Adulto("Perfil Adulto", 30);
        System.out.println("Agora o mesmo filme com o perfil " + adulto + ":");
        catalogo.reproduzir(terror, adulto);
    }

    // ---------- Leitura segura do teclado ----------

    // Usa nextLine() + parseInt() para nao deixar "sobras" no buffer do Scanner
    // e repete a pergunta ate o usuario digitar um numero inteiro valido.
    private static int lerInteiro(Scanner entrada, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String texto = entrada.nextLine().trim();
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                System.out.println("Valor invalido. Digite apenas numeros inteiros (ex: 10).");
            }
        }
    }

    private static String lerTexto(Scanner entrada, String mensagem) {
        System.out.print(mensagem);
        return entrada.nextLine().trim();
    }

    // Mostra os tipos e devolve 1 a 3 (ou 0 se o usuario digitar um tipo invalido).
    private static int escolherTipo(Scanner entrada) {
        System.out.println("1 - Filme");
        System.out.println("2 - Serie");
        System.out.println("3 - Podcast (episodio)");
        int tipo = lerInteiro(entrada, "Tipo: ");
        if (tipo < 1 || tipo > 3) {
            System.out.println("Tipo invalido. Escolha de 1 a 3.");
            return 0;
        }
        return tipo;
    }

    private static String nomeDoTipo(int tipo) {
        switch (tipo) {
            case 1:
                return "Filme";
            case 2:
                return "Serie";
            default:
                return "Podcast";
        }
    }

    // ---------- Dados de exemplo ----------

    private static void carregarDadosIniciais(Catalogo catalogo) {
        catalogo.adicionar(new Filme("Noite Sem Fim", "Terror", 18, "Estudio Lua Negra",
                Duration.ofMinutes(112)));
        catalogo.adicionar(new Filme("Aventura nas Nuvens", "Animacao", 0, "Estudio Horizonte",
                Duration.ofMinutes(95)));

        Serie codigoAlfa = new Serie("Codigo Alfa", "Ficcao cientifica", 14, "Roteiristas Unidos");
        montarSerie(codigoAlfa, 3, 10, 45);
        catalogo.adicionar(codigoAlfa);

        Serie turma = new Serie("Turma do Bairro", "Infantil", 10, "Estudio Horizonte");
        montarSerie(turma, 2, 12, 22);
        catalogo.adicionar(turma);

        catalogo.adicionar(new Podcast("Papo de Dev #12", "Tecnologia", 0, "Ana Souza",
                12, Duration.ofMinutes(48)));
        catalogo.adicionar(new Podcast("Historias da Meia-Noite #3", "Misterio", 16, "Carlos Lima",
                3, Duration.ofMinutes(35)));

        // Algumas notas de exemplo para a media ja aparecer no catalogo
        Conteudo aventura = catalogo.buscar("Aventura nas Nuvens");
        aventura.avaliar(5);
        aventura.avaliar(4);
        codigoAlfa.avaliar(5);
        codigoAlfa.avaliar(3);
        codigoAlfa.avaliar(4);
    }
}
