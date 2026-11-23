package stream;

import stream.catalogo.Catalogo;
import stream.conteudo.Filme;
import stream.conteudo.Podcast;
import stream.conteudo.Serie;
import stream.usuario.Adulto;
import stream.usuario.Infantil;

/**
 * Demonstracao do StreamHub: cria os objetos aqui mesmo e mostra o resultado.
 * Nao precisa digitar nada.
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
        System.out.printf("Royalties do Estudio Horizonte: R$ %.2f%n",
                catalogo.calcularTotalRoyalties("Estudio Horizonte"));

        // ---------- 10. Validacoes: dados invalidos sao recusados ----------
        System.out.println();
        System.out.println("=== VALIDACOES ===");
        Filme invalido = new Filme("", "Drama", 12, "Alguem", -10);
        catalogo.adicionar(invalido);
        catalogo.adicionar(new Filme("Noite Sem Fim", "Terror", 18, "Outro", 100));   // nome repetido
        new Infantil("Bebe", -3);
    }
}
