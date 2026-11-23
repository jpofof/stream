package stream.conteudo;

import java.time.Duration;
import java.util.ArrayList;

import stream.usuario.SuperClassUsuarios;

/**
 * Serie: dividida em temporadas, e cada temporada tem uma lista com a duracao dos episodios.
 * A duracao total e a soma de todos os episodios.
 */
public class Serie extends Conteudo {

    private static final double VALOR_POR_REPRODUCAO = 0.30; // R$ por reproducao

    private final ArrayList<ArrayList<Duration>> temporadas;

    public Serie(String nome, String categoria, int faixa, String criador) {
        super(nome, categoria, faixa, criador);
        temporadas = new ArrayList<>();
    }

    // Cria uma nova temporada (vazia) no final da serie.
    public void addTemporada() {
        temporadas.add(new ArrayList<>());
    }

    // Adiciona um episodio na temporada informada (a primeira temporada e a 1).
    public void addEpisodio(int temporada, Duration duracao) {
        if (temporada < 1 || temporada > temporadas.size()) {
            throw new IllegalArgumentException("A temporada " + temporada + " nao existe. A serie tem "
                    + temporadas.size() + " temporada(s).");
        }
        validarDuracao(duracao);
        temporadas.get(temporada - 1).add(duracao);
    }

    public int getQuantidadeTemporadas() {
        return temporadas.size();
    }

    public int getQuantidadeEpisodios() {
        int total = 0;
        for (ArrayList<Duration> temporada : temporadas) {
            total += temporada.size();
        }
        return total;
    }

    @Override
    public Duration getDuracaoTotal() {
        Duration total = Duration.ZERO;

        for (ArrayList<Duration> temporada : temporadas) {
            for (Duration episodio : temporada) {
                total = total.plus(episodio);
            }
        }

        return total;
    }

    @Override
    public String getTipo() {
        return "Serie";
    }

    @Override
    public double calcularRoyalties() {
        return getTotalReproducoes() * VALOR_POR_REPRODUCAO;
    }

    @Override
    public boolean reproduzir(SuperClassUsuarios usuario) {
        boolean liberado = super.reproduzir(usuario);
        if (liberado) {
            System.out.println("Reproduzindo a serie \"" + nome + "\" (" + getQuantidadeTemporadas()
                    + " temporadas, " + getQuantidadeEpisodios() + " episodios). Boa maratona, "
                    + usuario.getNome() + "!");
        }
        return liberado;
    }

    @Override
    public void exibirDetalhes() {
        super.exibirDetalhes();
        System.out.println("   Temporadas.....: " + getQuantidadeTemporadas());
        System.out.println("   Episodios......: " + getQuantidadeEpisodios());
    }
}
