package stream.conteudo;

import stream.contrato.Avaliavel;
import stream.contrato.Remuneravel;
import stream.usuario.SuperClassUsuarios;

/**
 * Classe mae (abstrata) de tudo que existe no catalogo: Filme, Serie e Podcast.
 * Ela guarda o que e igual para todos. O que muda de um para outro fica
 * como metodo abstract, e cada filha e obrigada a escrever o seu.
 */
public abstract class Conteudo implements Avaliavel, Remuneravel {

    // private: so esta classe mexe direto. As filhas usam os getters.
    private String nome;
    private String categoria;
    private String criador;
    private int faixa = -1;   // -1 = ainda nao informada. Validas: 0 (livre), 10, 12, 14, 16 e 18
    private double nota;      // media das notas (0 enquanto ninguem avaliar)

    // private: so esta classe mexe. Por isso estes nao tem setter:
    // ninguem de fora pode "inventar" uma nota ou uma reproducao.
    private int somaNotas;
    private int quantidadeNotas;
    private int totalReproducoes;

    public Conteudo(String nome, String categoria, int faixa, String criador) {
        this.setNome(nome);
        this.setCategoria(categoria);
        this.setFaixa(faixa);
        this.setCriador(criador);
    }

    // ---------- Getters e Setters (os setters recusam valor invalido) ----------

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.equals("")) {
            System.out.println("Erro: o nome nao pode ser vazio.");
        } else {
            this.nome = nome;
        }
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        if (categoria == null || categoria.equals("")) {
            System.out.println("Erro: a categoria nao pode ser vazia.");
        } else {
            this.categoria = categoria;
        }
    }

    @Override
    public String getCriador() {
        return criador;
    }

    public void setCriador(String criador) {
        if (criador == null || criador.equals("")) {
            System.out.println("Erro: o criador nao pode ser vazio.");
        } else {
            this.criador = criador;
        }
    }

    public int getFaixa() {
        return faixa;
    }

    public void setFaixa(int faixa) {
        if (faixa == 0 || faixa == 10 || faixa == 12 || faixa == 14 || faixa == 16 || faixa == 18) {
            this.faixa = faixa;
        } else {
            System.out.println("Erro: faixa etaria invalida. Use 0 (livre), 10, 12, 14, 16 ou 18.");
        }
    }

    @Override
    public double getNota() {
        return nota;
    }

    public int getTotalReproducoes() {
        return totalReproducoes;
    }

    // ---------- Avaliacao (contrato Avaliavel) ----------

    @Override
    public boolean avaliar(int estrelas) {
        if (estrelas < 1 || estrelas > 5) {
            System.out.println("Erro: a nota deve ser de 1 a 5 estrelas.");
            return false;
        }
        somaNotas = somaNotas + estrelas;
        quantidadeNotas = quantidadeNotas + 1;
        nota = (double) somaNotas / quantidadeNotas;   // recalcula a media
        return true;
    }

    // ---------- Metodos abstratos: cada filha escreve o seu (sobreposicao) ----------

    // Duracao total em MINUTOS (o mesmo padrao para todos).
    public abstract int getDuracaoTotal();

    // "Filme", "Serie" ou "Podcast".
    public abstract String getTipo();

    // O calcularRoyalties() vem da interface Remuneravel: nao escrevemos aqui,
    // cada filha (Filme, Serie, Podcast) escreve o seu, porque cada tipo paga um valor.

    // ---------- Regras que valem para todos ----------

    // Diz se todos os dados foram aceitos pelos setters.
    // As filhas reescrevem (@Override) para conferir os dados delas tambem.
    public boolean estaValido() {
        return nome != null && categoria != null && criador != null && faixa >= 0;
    }

    // Tenta reproduzir para um usuario. Devolve true se tocou e false se foi bloqueado.
    // Quem decide se pode assistir e o usuario (Infantil ou Adulto), pelo metodo podeAssistir.
    // As filhas reescrevem este metodo, chamam super.reproduzir() e mostram a mensagem delas.
    public boolean reproduzir(SuperClassUsuarios usuario) {
        if (usuario.podeAssistir(this)) {
            totalReproducoes = totalReproducoes + 1;
            return true;
        }
        System.out.println("ACESSO NEGADO: \"" + nome + "\" tem classificacao " + getFaixaTexto()
                + " e o usuario " + usuario.getNome() + " tem " + usuario.getIdade() + " anos.");
        return false;
    }

    public String getFaixaTexto() {
        if (faixa == 0) {
            return "Livre";
        }
        return faixa + " anos";
    }

    // Transforma os minutos em texto, por exemplo 135 vira "2h 15min".
    public String getDuracaoFormatada() {
        int total = getDuracaoTotal();
        int horas = total / 60;
        int minutos = total % 60;
        if (horas == 0) {
            return minutos + "min";
        }
        return horas + "h " + minutos + "min";
    }

    public String getNotaTexto() {
        if (quantidadeNotas == 0) {
            return "sem avaliacoes";
        }
        double arredondada = Math.round(nota * 10) / 10.0;   // 1 casa decimal
        return arredondada + " estrelas (" + quantidadeNotas + " avaliacoes)";
    }

    // Resumo de uma linha, usado nas listagens.
    public String getResumo() {
        return "[" + getTipo() + "] " + nome + " | " + getFaixaTexto() + " | "
                + getDuracaoFormatada() + " | " + getNotaTexto();
    }
}
