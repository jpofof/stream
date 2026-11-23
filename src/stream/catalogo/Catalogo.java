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

    public int getQuantidadeLog() {
        return logAuditoria.size();
    }

    // ---------- Busca (sobrecarga: o mesmo nome buscar com parametros diferentes) ----------

    // Busca pelo nome. Retorna null se nao achar.
    public Conteudo buscar(String nome) {
        for (int i = 0; i < conteudos.size(); i++) {
            if (conteudos.get(i).getNome().equals(nome)) {
                return conteudos.get(i);
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
        if (conteudos.size() == 0) {
            System.out.println("O catalogo esta vazio.");
        }
        for (int i = 0; i < conteudos.size(); i++) {
            System.out.println((i + 1) + " - " + conteudos.get(i).getResumo());
        }
    }

    // Lista so o que o usuario pode assistir (controle parental).
    public void listar(SuperClassUsuarios usuario) {
        int ocultos = 0;
        for (int i = 0; i < conteudos.size(); i++) {
            if (usuario.podeAssistir(conteudos.get(i))) {
                System.out.println((i + 1) + " - " + conteudos.get(i).getResumo());
            } else {
                ocultos++;
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
            if (conteudos.get(i).getTipo().equals(tipo)) {
                System.out.println((i + 1) + " - " + conteudos.get(i).getResumo());
                encontrados++;
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
        if (logAuditoria.size() == 0) {
            System.out.println("Nenhuma tentativa bloqueada registrada.");
        }
        for (int i = 0; i < logAuditoria.size(); i++) {
            System.out.println(logAuditoria.get(i));
        }
    }

    // ---------- Royalties (sobrecarga: com e sem o nome do criador) ----------

    // Soma os royalties de todo o catalogo.
    public double calcularTotalRoyalties() {
        double total = 0;
        for (int i = 0; i < conteudos.size(); i++) {
            total += conteudos.get(i).calcularRoyalties();   // cada filha calcula do seu jeito
        }
        return total;
    }

    // Soma os royalties so dos conteudos de um criador.
    public double calcularTotalRoyalties(String criador) {
        double total = 0;
        for (int i = 0; i < conteudos.size(); i++) {
            if (conteudos.get(i).getCriador().equals(criador)) {
                total += conteudos.get(i).calcularRoyalties();
            }
        }
        return total;
    }

    public void exibirRelatorioRoyalties() {
        for (int i = 0; i < conteudos.size(); i++) {
            Conteudo c = conteudos.get(i);
            System.out.println(c.getNome() + " | " + c.getCriador() + " | "
                    + c.getTotalReproducoes() + " reproducoes | R$ " + reais(c.calcularRoyalties()));
        }
        System.out.println("TOTAL DE ROYALTIES: R$ " + reais(calcularTotalRoyalties()));
    }

    // Arredonda para 2 casas decimais (1.2345 vira 1.23).
    public double reais(double valor) {
        return Math.round(valor * 100) / 100.0;
    }
}
