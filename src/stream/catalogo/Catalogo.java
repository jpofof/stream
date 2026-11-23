package stream.catalogo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import stream.conteudo.Conteudo;
import stream.usuario.SuperClassUsuarios;

/**
 * Classe gerenciadora do catalogo (Agregacao: o Catalogo "TEM" varios Conteudos).
 * A lista e do tipo da SUPERCLASSE, entao guarda filmes, series e podcasts juntos.
 */
public class Catalogo {

    // private: ninguem de fora mexe direto nas listas.
    private final ArrayList<Conteudo> conteudos;
    private final ArrayList<String> logAuditoria;

    public Catalogo() {
        conteudos = new ArrayList<>();
        logAuditoria = new ArrayList<>();
    }

    // ---------- Cadastro ----------

    public void adicionar(Conteudo conteudo) {
        if (conteudo == null) {
            throw new IllegalArgumentException("Nao e possivel cadastrar um conteudo nulo.");
        }
        if (buscar(conteudo.getNome()) != null) {
            throw new IllegalArgumentException("Ja existe um conteudo chamado \""
                    + conteudo.getNome() + "\" no catalogo.");
        }
        conteudos.add(conteudo);
    }

    public int getQuantidade() {
        return conteudos.size();
    }

    // Devolve uma COPIA da lista, assim quem chamar nao consegue alterar a lista interna.
    public ArrayList<Conteudo> getConteudos() {
        return new ArrayList<>(conteudos);
    }

    public ArrayList<String> getLogAuditoria() {
        return new ArrayList<>(logAuditoria);
    }

    // ---------- Busca (sobrecarga: mesmo nome, parametros diferentes) ----------

    // Busca pelo nome (ignora maiusculas/minusculas). Retorna null se nao achar.
    public Conteudo buscar(String nome) {
        if (nome == null) {
            return null;
        }
        for (Conteudo c : conteudos) {
            if (c.getNome().equalsIgnoreCase(nome.trim())) {
                return c;
            }
        }
        return null;
    }

    // Busca pela posicao mostrada nas listagens (comeca em 1). Retorna null se for invalida.
    public Conteudo buscar(int posicao) {
        if (posicao < 1 || posicao > conteudos.size()) {
            return null;
        }
        return conteudos.get(posicao - 1);
    }

    // ---------- Listagem (sobrecarga: 3 assinaturas) ----------
    // Em todas, o numero mostrado e a posicao no catalogo completo (serve para buscar(int)).

    // Lista tudo.
    public void listar() {
        if (conteudos.isEmpty()) {
            System.out.println("O catalogo esta vazio.");
            return;
        }
        for (int i = 0; i < conteudos.size(); i++) {
            System.out.println((i + 1) + " - " + conteudos.get(i));
        }
    }

    // Lista somente o que o usuario pode assistir (controle parental).
    public void listar(SuperClassUsuarios usuario) {
        int ocultos = 0;
        for (int i = 0; i < conteudos.size(); i++) {
            Conteudo c = conteudos.get(i);
            if (usuario.podeAssistir(c)) {
                System.out.println((i + 1) + " - " + c);
            } else {
                ocultos++;
            }
        }
        if (ocultos > 0) {
            System.out.println("(" + ocultos + " conteudo(s) oculto(s) pela classificacao indicativa)");
        }
    }

    // Lista somente um tipo: "Filme", "Serie" ou "Podcast".
    public void listar(String tipo) {
        int encontrados = 0;
        for (int i = 0; i < conteudos.size(); i++) {
            Conteudo c = conteudos.get(i);
            if (c.getTipo().equalsIgnoreCase(tipo)) {
                System.out.println((i + 1) + " - " + c);
                encontrados++;
            }
        }
        if (encontrados == 0) {
            System.out.println("Nenhum conteudo do tipo \"" + tipo + "\" encontrado.");
        }
    }

    // ---------- Reproducao e auditoria ----------

    /**
     * Reproduz o conteudo para o usuario. O metodo reproduzir() chamado aqui e o da SUBCLASSE
     * (polimorfismo) e quem libera ou bloqueia e o tipo de conta do usuario (outro polimorfismo).
     * Se o controle parental bloquear, a tentativa vai para o log de auditoria.
     */
    public boolean reproduzir(Conteudo conteudo, SuperClassUsuarios usuario) {
        if (conteudo == null) {
            throw new IllegalArgumentException("Conteudo nao encontrado.");
        }
        boolean liberado = conteudo.reproduzir(usuario);
        if (!liberado) {
            registrarLog("BLOQUEADO - usuario " + usuario.getNome() + " (" + usuario.getIdade()
                    + " anos, conta " + usuario.getTipoConta() + ") tentou reproduzir \""
                    + conteudo.getNome() + "\" [" + conteudo.getTipo() + ", classificacao "
                    + conteudo.getFaixaTexto() + "]");
        }
        return liberado;
    }

    private void registrarLog(String mensagem) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        logAuditoria.add("[" + LocalDateTime.now().format(formato) + "] " + mensagem);
    }

    public void exibirLogAuditoria() {
        if (logAuditoria.isEmpty()) {
            System.out.println("Nenhuma tentativa bloqueada registrada.");
            return;
        }
        for (String registro : logAuditoria) {
            System.out.println(registro);
        }
    }

    // ---------- Royalties (sobrecarga: com e sem criador) ----------

    // Soma os royalties de todo o catalogo.
    public double calcularTotalRoyalties() {
        double total = 0;
        for (Conteudo c : conteudos) {
            total += c.calcularRoyalties(); // cada subclasse calcula do seu jeito
        }
        return total;
    }

    // Soma os royalties apenas dos conteudos de um criador.
    public double calcularTotalRoyalties(String criador) {
        double total = 0;
        if (criador == null) {
            return total;
        }
        for (Conteudo c : conteudos) {
            if (c.getCriador().equalsIgnoreCase(criador.trim())) {
                total += c.calcularRoyalties();
            }
        }
        return total;
    }

    public void exibirRelatorioRoyalties() {
        if (conteudos.isEmpty()) {
            System.out.println("O catalogo esta vazio.");
            return;
        }
        for (Conteudo c : conteudos) {
            System.out.println(String.format("%-30s | %-20s | %4d reproducoes | R$ %8.2f",
                    c.getNome(), c.getCriador(), c.getTotalReproducoes(), c.calcularRoyalties()));
        }
        System.out.println(String.format("TOTAL DE ROYALTIES: R$ %.2f", calcularTotalRoyalties()));
    }
}
