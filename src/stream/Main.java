package stream;

import java.util.Scanner;

import stream.catalogo.Catalogo;
import stream.conteudo.Filme;
import stream.conteudo.Podcast;
import stream.conteudo.Serie;
import stream.conteudo.Conteudo;
import stream.usuario.Adulto;
import stream.usuario.Infantil;
import stream.usuario.SuperClassUsuarios;

/**
 * Demonstracao do StreamHub: cria os objetos aqui mesmo e mostra o resultado.
 * Depois da demonstracao abre um menu para testar tudo digitando.
 */
public class Main {

    public static void main(String[] args) {

        // ---------- 1. Criando conteudos (heranca: Filme, Serie e Podcast sao Conteudo) ----------
        Filme terror = new Filme("Noite Sem Fim", "Terror", 18, "Estudio Lua Negra", 112);
        Filme animacao = new Filme("Aventura nas Nuvens", "Animacao", 0, "Estudio Horizonte", 95);
        Serie ficcao = new Serie("Codigo Alfa", "Ficcao cientifica", 14, "Roteiristas Unidos", 3, 10, 45);
        Serie infantil = new Serie("Turma do Bairro", "Infantil", 10, "Estudio Horizonte", 2, 12, 22);
        Podcast tecnologia = new Podcast("Papo de Dev #12", "Tecnologia", 0, "Ana Souza", 12, 48);

        // ---------- 2. Catalogo (agregacao: o catalogo tem uma lista de Conteudo) ----------
        Catalogo catalogo = new Catalogo();
        catalogo.adicionar(terror);
        catalogo.adicionar(animacao);
        catalogo.adicionar(ficcao);
        catalogo.adicionar(infantil);
        catalogo.adicionar(tecnologia);

        System.out.println("=== CATALOGO COMPLETO ===");
        catalogo.listar();

        // ---------- 3. Usuarios ----------
        Infantil teen = new Infantil("Perfil Teen", 14);
        Infantil crianca = new Infantil("Perfil Crianca", 8);
        Adulto adulto = new Adulto("Perfil Adulto", 30);

        // ---------- 4. Caso de uso do roteiro: 14 anos tenta ver filme 18+ ----------
        System.out.println();
        System.out.println("=== CASO DE USO: 14 anos tenta ver filme de terror 18+ ===");
        catalogo.reproduzir(terror, teen);

        // ---------- 5. Reproducoes liberadas e bloqueadas ----------
        System.out.println();
        System.out.println("=== REPRODUCOES ===");
        catalogo.reproduzir(terror, adulto);
        catalogo.reproduzir(ficcao, teen);
        catalogo.reproduzir(ficcao, crianca);      // serie 14+ para quem tem 8 anos: bloqueado
        catalogo.reproduzir(animacao, crianca);
        catalogo.reproduzir(animacao, crianca);
        catalogo.reproduzir(tecnologia, adulto);

        // ---------- 6. Log de auditoria ----------
        System.out.println();
        System.out.println("=== LOG DE AUDITORIA ===");
        catalogo.exibirLogAuditoria();

        // ---------- 7. Avaliacoes (media recalculada) ----------
        System.out.println();
        System.out.println("=== AVALIACOES ===");
        animacao.avaliar(5);
        animacao.avaliar(4);
        ficcao.avaliar(3);
        ficcao.avaliar(4);
        ficcao.avaliar(5);
        animacao.avaliar(9);                       // invalida: mostra erro
        System.out.println(animacao.getNome() + ": " + animacao.getNotaTexto());
        System.out.println(ficcao.getNome() + ": " + ficcao.getNotaTexto());

        // ---------- 8. Sobrecarga: o mesmo listar() com parametros diferentes ----------
        System.out.println();
        System.out.println("=== listar(crianca): so o que o perfil Crianca pode ver ===");
        catalogo.listar(crianca);
        System.out.println();
        System.out.println("=== listar(\"Podcast\"): so um tipo ===");
        catalogo.listar("Podcast");

        // ---------- 9. Royalties ----------
        System.out.println();
        System.out.println("=== ROYALTIES ===");
        catalogo.exibirRelatorioRoyalties();
        System.out.println("Royalties do Estudio Horizonte: R$ "
                + catalogo.reais(catalogo.calcularTotalRoyalties("Estudio Horizonte")));

        // ---------- 10. Validacoes: dados invalidos sao recusados ----------
        System.out.println();
        System.out.println("=== VALIDACOES ===");
        Filme invalido = new Filme("", "Drama", 12, "Alguem", -10);
        catalogo.adicionar(invalido);
        catalogo.adicionar(new Filme("Noite Sem Fim", "Terror", 18, "Outro", 100));   // nome repetido
        new Infantil("Bebe", -3);

        // ---------- 11. Menu interativo (Scanner + while + switch) ----------
        Scanner teclado = new Scanner(System.in);
        int opcao = -1;

        while (opcao != 0) {
            System.out.println();
            System.out.println("===== MENU STREAMHUB =====");
            System.out.println("1 - Listar catalogo");
            System.out.println("2 - Cadastrar filme");
            System.out.println("3 - Reproduzir conteudo");
            System.out.println("4 - Avaliar conteudo");
            System.out.println("5 - Ver log de auditoria e royalties");
            System.out.println("0 - Sair");
            opcao = lerNumero(teclado, "Escolha: ");

            switch (opcao) {
                case 1:
                    catalogo.listar();
                    break;
                case 2:
                    System.out.print("Nome: ");
                    String nome = teclado.nextLine();
                    System.out.print("Categoria: ");
                    String categoria = teclado.nextLine();
                    System.out.print("Criador: ");
                    String criador = teclado.nextLine();
                    int faixa = lerNumero(teclado, "Faixa etaria (0, 10, 12, 14, 16 ou 18): ");
                    int duracao = lerNumero(teclado, "Duracao em minutos: ");
                    Filme novo = new Filme(nome, categoria, faixa, criador, duracao);
                    if (catalogo.adicionar(novo)) {
                        System.out.println("Filme cadastrado!");
                    }
                    break;
                case 3:
                    catalogo.listar();
                    Conteudo escolhido = catalogo.buscar(lerNumero(teclado, "Numero do conteudo: "));
                    if (escolhido == null) {
                        System.out.println("Numero invalido.");
                        break;
                    }
                    System.out.print("Nome do usuario: ");
                    String nomeUsuario = teclado.nextLine();
                    int idade = lerNumero(teclado, "Idade: ");
                    SuperClassUsuarios usuario;
                    if (idade < 18) {
                        usuario = new Infantil(nomeUsuario, idade);
                    } else {
                        usuario = new Adulto(nomeUsuario, idade);
                    }
                    if (usuario.estaValido()) {
                        catalogo.reproduzir(escolhido, usuario);
                    }
                    break;
                case 4:
                    catalogo.listar();
                    Conteudo avaliado = catalogo.buscar(lerNumero(teclado, "Numero do conteudo: "));
                    if (avaliado == null) {
                        System.out.println("Numero invalido.");
                        break;
                    }
                    int estrelas = lerNumero(teclado, "Nota de 1 a 5: ");
                    if (avaliado.avaliar(estrelas)) {
                        System.out.println("Agora: " + avaliado.getNotaTexto());
                    }
                    break;
                case 5:
                    catalogo.exibirLogAuditoria();
                    catalogo.exibirRelatorioRoyalties();
                    break;
                case 0:
                    System.out.println("Ate logo!");
                    break;
                default:
                    System.out.println("Opcao invalida. Digite de 0 a 5.");
            }
        }
        teclado.close();
    }

    // Le um numero inteiro. Se digitar letra, avisa e pergunta de novo.
    // (static porque o main tambem e static)
    public static int lerNumero(Scanner teclado, String mensagem) {
        System.out.print(mensagem);
        while (!teclado.hasNextInt()) {
            System.out.println("Digite apenas numeros.");
            teclado.next();                 // joga fora o que foi digitado
            System.out.print(mensagem);
        }
        int numero = teclado.nextInt();
        teclado.nextLine();                 // limpa o Enter que sobrou
        return numero;
    }
}
