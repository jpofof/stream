package stream.catalogo;

import java.util.ArrayList;

import stream.conteudo.Conteudo;
import stream.usuario.SuperClassUsuarios;

/**
 * Classe gerenciadora do catalogo.
 * Agregacao ("tem um"): o Catalogo TEM uma lista de Conteudo.
 * Como a lista e de Conteudo (a classe mae), ela guarda Filme, Serie e Podcast juntos.
 */
public class Catalogo {

    // private: ninguem de fora mexe direto nas listas.
    private ArrayList<Conteudo> conteudos;
    private ArrayList<String> logAuditoria;

    public Catalogo() {
        conteudos = new ArrayList<Conteudo>();
        logAuditoria = new ArrayList<String>();
    }

    // ---------- Cadastro ----------

    // Devolve true se cadastrou e false se recusou.
    public boolean adicionar(Conteudo conteudo) {
        if (conteudo == null || !conteudo.estaValido()) {
            System.out.println("Cadastro cancelado: algum dado e invalido (veja os erros acima).");
            return false;
        }
        if (buscar(conteudo.getNome()) != null) {
            System.out.println("Cadastro cancelado: ja existe um conteudo chamado \""
                    + conteudo.getNome() + "\".");
            return false;
        }
        conteudos.add(conteudo);
        return true;
    }

    public int getQuantidade() {
        return conteudos.size();
    }

    // Devolve uma COPIA da lista. Assim quem chamar nao consegue mexer na lista original.
    public ArrayList<Conteudo> getConteudos() {
        ArrayList<Conteudo> copia = new ArrayList<Conteudo>();
        for (Conteudo c : conteudos) {
            copia.add(c);
        }
        return copia;
    }

    public ArrayList<String> getLogAuditoria() {
        ArrayList<String> copia = new ArrayList<String>();
        for (String registro : logAuditoria) {
            copia.add(registro);
        }
        return copia;
    }

    // ---------- Busca (sobrecarga: o mesmo nome buscar com parametros diferentes) ----------

    // Busca pelo nome. Retorna null se nao achar.
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

    // Busca pelo numero que aparece na listagem (o primeiro e o 1). Retorna null se nao existir.
    public Conteudo buscar(int posicao) {
        if (posicao < 1 || posicao > conteudos.size()) {
            return null;
        }
        return conteudos.get(posicao - 1);
    }

    // ---------- Listagem (sobrecarga: tres jeitos de chamar listar) ----------
    // Em todas, o numero mostrado e o do catalogo completo (o mesmo usado em buscar(int)).

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

    // Lista so o que o usuario pode assistir (controle parental).
    public void listar(SuperClassUsuarios usuario) {
        int ocultos = 0;
        for (int i = 0; i < conteudos.size(); i++) {
            if (usuario.podeAssistir(conteudos.get(i))) {
                System.out.println((i + 1) + " - " + conteudos.get(i));
            } else {
                ocultos = ocultos + 1;
            }
        }
        if (ocultos > 0) {
            System.out.println("(" + ocultos + " conteudo(s) oculto(s) pela classificacao indicativa)");
        }
    }

    // Lista so um tipo: "Filme", "Serie" ou "Podcast".
    public void listar(String tipo) {
        int encontrados = 0;
        for (int i = 0; i < conteudos.size(); i++) {
            if (conteudos.get(i).getTipo().equalsIgnoreCase(tipo)) {
                System.out.println((i + 1) + " - " + conteudos.get(i));
                encontrados = encontrados + 1;
            }
        }
        if (encontrados == 0) {
            System.out.println("Nenhum conteudo do tipo \"" + tipo + "\" encontrado.");
        }
    }

    // ---------- Reproducao e log de auditoria ----------

    // Reproduz o conteudo para o usuario. O reproduzir() que roda aqui e o da filha
    // (Filme, Serie ou Podcast), porque cada uma sobrepoe o metodo da classe mae.
    // Se o controle parental bloquear, a tentativa fica registrada no log.
    public boolean reproduzir(Conteudo conteudo, SuperClassUsuarios usuario) {
        boolean liberado = conteudo.reproduzir(usuario);
        if (!liberado) {
            logAuditoria.add("BLOQUEADO - usuario " + usuario.getNome() + " (" + usuario.getIdade()
                    + " anos, conta " + usuario.getTipoConta() + ") tentou reproduzir \""
                    + conteudo.getNome() + "\" [" + conteudo.getTipo() + ", classificacao "
                    + conteudo.getFaixaTexto() + "]");
        }
        return liberado;
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

    // ---------- Royalties (sobrecarga: com e sem o nome do criador) ----------

    // Soma os royalties de todo o catalogo.
    public double calcularTotalRoyalties() {
        double total = 0;
        for (Conteudo c : conteudos) {
            total = total + c.calcularRoyalties();   // cada filha calcula do seu jeito
        }
        return total;
    }

    // Soma os royalties so dos conteudos de um criador.
    public double calcularTotalRoyalties(String criador) {
        double total = 0;
        if (criador == null) {
            return total;
        }
        for (Conteudo c : conteudos) {
            if (c.getCriador().equalsIgnoreCase(criador.trim())) {
                total = total + c.calcularRoyalties();
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
            System.out.printf("%-30s | %-20s | %3d reproducoes | R$ %.2f%n",
                    c.getNome(), c.getCriador(), c.getTotalReproducoes(), c.calcularRoyalties());
        }
        System.out.printf("TOTAL DE ROYALTIES: R$ %.2f%n", calcularTotalRoyalties());
    }
}
