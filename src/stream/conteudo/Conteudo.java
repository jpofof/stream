package stream.conteudo;

import java.time.Duration;

import stream.contrato.Avaliavel;
import stream.contrato.Remuneravel;
import stream.usuario.SuperClassUsuarios;

/**
 * Superclasse abstrata de tudo que existe no catalogo (filme, serie, podcast).
 * Guarda o que e comum a todos e obriga as subclasses a definirem o que muda:
 * a duracao total, o tipo e o calculo de royalties.
 */
public abstract class Conteudo implements Avaliavel, Remuneravel {

    // protected: as subclasses podem LER estes atributos diretamente.
    // Para ALTERAR, so pelos setters (que validam).
    protected String nome;
    protected String categoria;
    protected int faixa;      // classificacao indicativa: 0 (livre), 10, 12, 14, 16 ou 18
    protected String criador;
    protected float nota;     // media das avaliacoes (0 enquanto ninguem avaliou)

    // private: so a propria classe mexe. Nao existem setters publicos para estes,
    // porque ninguem pode "inventar" uma nota ou uma reproducao de fora.
    private int somaNotas;
    private int quantidadeAvaliacoes;
    private int totalReproducoes;

    public Conteudo(String nome, String categoria, int faixa, String criador) {
        setNome(nome);
        setCategoria(categoria);
        setFaixa(faixa);
        setCriador(criador);
    }

    // ---------- Getters e Setters com validacao ----------
    // Os setters sao "final": como o construtor chama eles, nenhuma subclasse pode
    // reescrever um setter e burlar a validacao.

    public String getNome() {
        return nome;
    }

    public final void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do conteudo nao pode ser vazio.");
        }
        this.nome = nome.trim();
    }

    public String getCategoria() {
        return categoria;
    }

    public final void setCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("A categoria nao pode ser vazia.");
        }
        this.categoria = categoria.trim();
    }

    public int getFaixa() {
        return faixa;
    }

    public final void setFaixa(int faixa) {
        if (faixa < 0) {
            throw new IllegalArgumentException("A faixa etaria nao pode ser negativa.");
        }
        boolean valida = faixa == 0 || faixa == 10 || faixa == 12
                || faixa == 14 || faixa == 16 || faixa == 18;
        if (!valida) {
            throw new IllegalArgumentException("Faixa etaria invalida. Use 0 (livre), 10, 12, 14, 16 ou 18.");
        }
        this.faixa = faixa;
    }

    @Override
    public String getCriador() {
        return criador;
    }

    public final void setCriador(String criador) {
        if (criador == null || criador.trim().isEmpty()) {
            throw new IllegalArgumentException("O criador nao pode ser vazio.");
        }
        this.criador = criador.trim();
    }

    public int getTotalReproducoes() {
        return totalReproducoes;
    }

    // ---------- Contrato Avaliavel ----------

    @Override
    public void avaliar(int estrelas) {
        if (estrelas < 1 || estrelas > 5) {
            throw new IllegalArgumentException("A nota deve ser de 1 a 5 estrelas.");
        }
        somaNotas += estrelas;
        quantidadeAvaliacoes++;
        // Recalcula a media a cada nova avaliacao
        nota = (float) somaNotas / quantidadeAvaliacoes;
    }

    @Override
    public float getNota() {
        return nota;
    }

    @Override
    public int getQuantidadeAvaliacoes() {
        return quantidadeAvaliacoes;
    }

    // ---------- Contrato Remuneravel ----------
    // getCriador() ja foi implementado acima.
    // calcularRoyalties() fica abstrato: cada subclasse tem o seu valor por reproducao.
    @Override
    public abstract double calcularRoyalties();

    // ---------- Metodos abstratos (cada subclasse e obrigada a escrever o seu) ----------

    // Duracao total padronizada em Duration:
    // filme = duracao unica, serie = soma dos episodios, podcast = tempo de audio.
    public abstract Duration getDuracaoTotal();

    // Nome do tipo ("Filme", "Serie" ou "Podcast")
    public abstract String getTipo();

    // ---------- Regras de negocio comuns ----------

    // Validacao de duracao usada pelas subclasses (rejeita nula, zero ou negativa).
    protected static void validarDuracao(Duration duracao) {
        if (duracao == null || duracao.isZero() || duracao.isNegative()) {
            throw new IllegalArgumentException("A duracao deve ser maior que zero.");
        }
    }

    /**
     * Tenta reproduzir o conteudo para um usuario.
     * Quem decide se pode assistir e o proprio usuario (Infantil ou Adulto reescrevem podeAssistir).
     * Retorna true se foi reproduzido e false se foi bloqueado pela faixa etaria.
     * As subclasses reescrevem (@Override) este metodo para mostrar a sua propria mensagem.
     */
    public boolean reproduzir(SuperClassUsuarios usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("E necessario informar um usuario para reproduzir.");
        }
        if (!usuario.podeAssistir(this)) {
            System.out.println("ACESSO NEGADO: \"" + nome + "\" tem classificacao "
                    + getFaixaTexto() + " e o usuario " + usuario.getNome()
                    + " tem " + usuario.getIdade() + " anos.");
            return false;
        }
        totalReproducoes++;
        return true;
    }

    public String getFaixaTexto() {
        if (faixa == 0) {
            return "Livre";
        }
        return faixa + " anos";
    }

    // Converte a duracao total para um texto como "2h 15min".
    public String getDuracaoFormatada() {
        long total = getDuracaoTotal().toMinutes();
        long horas = total / 60;
        long minutos = total % 60;
        if (horas == 0) {
            return minutos + "min";
        }
        if (minutos == 0) {
            return horas + "h";
        }
        return horas + "h " + minutos + "min";
    }

    public String getNotaTexto() {
        if (quantidadeAvaliacoes == 0) {
            return "sem avaliacoes";
        }
        return String.format("%.1f estrelas (%d avaliacoes)", nota, quantidadeAvaliacoes);
    }

    // As subclasses reescrevem este metodo e chamam super.exibirDetalhes() para completar a ficha.
    public void exibirDetalhes() {
        System.out.println("[" + getTipo() + "] " + nome);
        System.out.println("   Categoria......: " + categoria);
        System.out.println("   Criador........: " + criador);
        System.out.println("   Classificacao..: " + getFaixaTexto());
        System.out.println("   Duracao total..: " + getDuracaoFormatada());
        System.out.println("   Nota media.....: " + getNotaTexto());
        System.out.println("   Reproducoes....: " + totalReproducoes);
    }

    // Resumo de uma linha, usado nas listagens do catalogo.
    @Override
    public String toString() {
        return "[" + getTipo() + "] " + nome + " | " + getFaixaTexto()
                + " | " + getDuracaoFormatada() + " | " + getNotaTexto();
    }
}
