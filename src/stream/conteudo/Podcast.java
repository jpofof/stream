package stream.conteudo;

import java.time.Duration;

import stream.usuario.SuperClassUsuarios;

/**
 * Podcast: cada objeto representa um EPISODIO de podcast (audio).
 */
public class Podcast extends Conteudo {

    private static final double VALOR_POR_REPRODUCAO = 0.10; // R$ por reproducao

    private int numeroEpisodio;
    private Duration duracao; // tempo de audio

    public Podcast(String nome, String categoria, int faixa, String criador,
                   int numeroEpisodio, Duration duracao) {
        super(nome, categoria, faixa, criador);
        setNumeroEpisodio(numeroEpisodio);
        setDuracao(duracao);
    }

    public int getNumeroEpisodio() {
        return numeroEpisodio;
    }

    public final void setNumeroEpisodio(int numeroEpisodio) {
        if (numeroEpisodio <= 0) {
            throw new IllegalArgumentException("O numero do episodio deve ser maior que zero.");
        }
        this.numeroEpisodio = numeroEpisodio;
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
        return "Podcast";
    }

    @Override
    public double calcularRoyalties() {
        return getTotalReproducoes() * VALOR_POR_REPRODUCAO;
    }

    @Override
    public boolean reproduzir(SuperClassUsuarios usuario) {
        boolean liberado = super.reproduzir(usuario);
        if (liberado) {
            System.out.println("Tocando o episodio " + numeroEpisodio + " do podcast \"" + nome
                    + "\" (" + getDuracaoFormatada() + " de audio). Boa audicao, "
                    + usuario.getNome() + "!");
        }
        return liberado;
    }

    @Override
    public void exibirDetalhes() {
        super.exibirDetalhes();
        System.out.println("   Episodio.......: #" + numeroEpisodio);
    }
}
