package stream.conteudo;

import java.time.Duration;

import stream.usuario.SuperClassUsuarios;

/**
 * Filme: tem uma unica duracao.
 */
public class Filme extends Conteudo {

    private static final double VALOR_POR_REPRODUCAO = 0.50; // R$ por reproducao

    private Duration duracao;

    public Filme(String nome, String categoria, int faixa, String criador, Duration duracao) {
        super(nome, categoria, faixa, criador);
        setDuracao(duracao);
    }

    public Duration getDuracao() {
        return duracao;
    }

    public final void setDuracao(Duration duracao) {
        validarDuracao(duracao);
        this.duracao = duracao;
    }

    @Override
    public Duration getDuracaoTotal() {
        return duracao;
    }

    @Override
    public String getTipo() {
        return "Filme";
    }

    @Override
    public double calcularRoyalties() {
        return getTotalReproducoes() * VALOR_POR_REPRODUCAO;
    }

    @Override
    public boolean reproduzir(SuperClassUsuarios usuario) {
        boolean liberado = super.reproduzir(usuario); // aplica o controle parental
        if (liberado) {
            System.out.println("Reproduzindo o filme \"" + nome + "\" ("
                    + getDuracaoFormatada() + "). Bom filme, " + usuario.getNome() + "!");
        }
        return liberado;
    }
}
