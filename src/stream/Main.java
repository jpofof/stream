package stream;

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
 * Menu do StreamHub (console).
 */
public class Main {

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        Catalogo catalogo = new Catalogo();
        carregarDadosIniciais(catalogo);

        // A variavel e do tipo da classe mae: guarda uma conta Infantil ou Adulto.
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
                    reproduzir(entrada, catalogo, usuarioAtual);
                    break;
                case 4:
                    avaliar(entrada, catalogo);
                    break;
                case 5:
                    usuarioAtual = trocarUsuario(entrada, usuarioAtual);
                    break;
                case 6:
                    relatorioRoyalties(entrada, catalogo);
                    break;
                case 7:
                    System.out.println("--- LOG DE AUDITORIA ---");
                    catalogo.exibirLogAuditoria();
                    break;
                case 8:
                    executarCasoDeUso(catalogo);
                    break;
                case 0:
                    System.out.println("Encerrando o StreamHub. Ate logo!");
                    break;
                default:
                    System.out.println("Opcao invalida. Escolha um numero de 0 a 8.");
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
        System.out.println("3 - Reproduzir conteudo");
        System.out.println("4 - Avaliar conteudo (1 a 5 estrelas)");
        System.out.println("5 - Trocar de perfil");
        System.out.println("6 - Relatorio de royalties");
        System.out.println("7 - Log de auditoria");
        System.out.println("8 - Executar o caso de uso do roteiro (demonstracao)");
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

        // Se algum dado for invalido, o setter mostra o erro e o catalogo recusa o cadastro.
        Conteudo novo;
        if (tipo == 1) {
            int duracao = lerInteiro(entrada, "Duracao em minutos: ");
            novo = new Filme(nome, categoria, faixa, criador, duracao);
        } else if (tipo == 2) {
            int temporadas = lerInteiro(entrada, "Numero de temporadas: ");
            int episodios = lerInteiro(entrada, "Episodios por temporada: ");
            int duracaoEpisodio = lerInteiro(entrada, "Duracao de cada episodio (minutos): ");
            novo = new Serie(nome, categoria, faixa, criador, temporadas, episodios, duracaoEpisodio);
        } else {
            int numero = lerInteiro(entrada, "Numero do episodio: ");
            int duracao = lerInteiro(entrada, "Minutos de audio: ");
            novo = new Podcast(nome, categoria, faixa, criador, numero, duracao);
        }

        if (catalogo.adicionar(novo)) {
            System.out.println("Conteudo cadastrado com sucesso: " + novo);
        }
    }

    private static void listarCatalogo(Scanner entrada, Catalogo catalogo, SuperClassUsuarios usuario) {
        System.out.println("--- LISTAR CATALOGO ---");
        System.out.println("1 - Tudo");
        System.out.println("2 - Somente o liberado para o perfil atual (" + usuario.getNome() + ")");
        System.out.println("3 - Por tipo");
        int escolha = lerInteiro(entrada, "Opcao: ");
        System.out.println();

        if (escolha == 1) {
            catalogo.listar();
        } else if (escolha == 2) {
            catalogo.listar(usuario);
        } else if (escolha == 3) {
            int tipo = escolherTipo(entrada);
            if (tipo != 0) {
                System.out.println();
                catalogo.listar(nomeDoTipo(tipo));
            }
        } else {
            System.out.println("Opcao invalida.");
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
        if (escolhido.avaliar(estrelas)) {
            System.out.println("Avaliacao registrada! \"" + escolhido.getNome()
                    + "\" agora tem " + escolhido.getNotaTexto() + ".");
        }
    }

    // A idade decide o tipo da conta: menor de 18 = Infantil, 18 ou mais = Adulto.
    private static SuperClassUsuarios trocarUsuario(Scanner entrada, SuperClassUsuarios usuarioAtual) {
        System.out.println("--- TROCAR DE PERFIL ---");
        String nome = lerTexto(entrada, "Nome do perfil: ");
        int idade = lerInteiro(entrada, "Idade: ");

        SuperClassUsuarios novo;
        if (idade < 18) {
            novo = new Infantil(nome, idade);
        } else {
            novo = new Adulto(nome, idade);
        }

        if (novo.estaValido()) {
            System.out.println("Perfil ativo agora: " + novo);
            return novo;
        }
        System.out.println("Perfil nao alterado: algum dado e invalido (veja os erros acima).");
        return usuarioAtual;
    }

    private static void relatorioRoyalties(Scanner entrada, Catalogo catalogo) {
        System.out.println("--- RELATORIO DE ROYALTIES ---");
        catalogo.exibirRelatorioRoyalties();
        System.out.println();

        String criador = lerTexto(entrada, "Ver o total de um criador? (digite o nome ou Enter para pular): ");
        if (!criador.isEmpty()) {
            double total = catalogo.calcularTotalRoyalties(criador);
            System.out.printf("Royalties de %s: R$ %.2f%n", criador, total);
        }
    }

    // Este e o caso de uso de teste que o roteiro pede para o Tema 6.
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

    // ---------- Leitura do teclado ----------

    // Pede um numero inteiro. Se o usuario digitar letras, avisa e pergunta de novo.
    private static int lerInteiro(Scanner entrada, String mensagem) {
        System.out.print(mensagem);
        while (!entrada.hasNextInt()) {
            System.out.println("Valor invalido. Digite apenas numeros inteiros.");
            entrada.next();                  // joga fora o que foi digitado
            System.out.print(mensagem);
        }
        int numero = entrada.nextInt();
        entrada.nextLine();                  // limpa o Enter que ficou no teclado
        return numero;
    }

    private static String lerTexto(Scanner entrada, String mensagem) {
        System.out.print(mensagem);
        return entrada.nextLine().trim();
    }

    // Mostra os tipos e devolve 1, 2 ou 3 (ou 0 se digitar um tipo que nao existe).
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
        if (tipo == 1) {
            return "Filme";
        }
        if (tipo == 2) {
            return "Serie";
        }
        return "Podcast";
    }

    // ---------- Dados de exemplo ----------

    private static void carregarDadosIniciais(Catalogo catalogo) {
        catalogo.adicionar(new Filme("Noite Sem Fim", "Terror", 18, "Estudio Lua Negra", 112));
        catalogo.adicionar(new Filme("Aventura nas Nuvens", "Animacao", 0, "Estudio Horizonte", 95));
        catalogo.adicionar(new Serie("Codigo Alfa", "Ficcao cientifica", 14, "Roteiristas Unidos", 3, 10, 45));
        catalogo.adicionar(new Serie("Turma do Bairro", "Infantil", 10, "Estudio Horizonte", 2, 12, 22));
        catalogo.adicionar(new Podcast("Papo de Dev #12", "Tecnologia", 0, "Ana Souza", 12, 48));
        catalogo.adicionar(new Podcast("Historias da Meia-Noite #3", "Misterio", 16, "Carlos Lima", 3, 35));

        // Algumas notas de exemplo, so para a media ja aparecer na listagem
        Conteudo aventura = catalogo.buscar("Aventura nas Nuvens");
        aventura.avaliar(5);
        aventura.avaliar(4);
        Conteudo codigo = catalogo.buscar("Codigo Alfa");
        codigo.avaliar(5);
        codigo.avaliar(3);
        codigo.avaliar(4);
    }
}
