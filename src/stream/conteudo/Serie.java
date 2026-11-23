package stream.conteudo;

import stream.usuario.SuperClassUsuarios;

/**
 * Serie: dividida em temporadas e episodios.
 * Duracao total = temporadas x episodios por temporada x duracao de cada episodio.
 */
public class Serie extends Conteudo {

    private int temporadas;
    private int episodiosPorTemporada;
    private int duracaoEpisodio;                   // em minutos
    private double valorPorReproducao = 0.30;      // R$ pagos ao criador a cada reproducao

    public Serie(String nome, String categoria, int faixa, String criador,
                 int temporadas, int episodiosPorTemporada, int duracaoEpisodio) {
        super(nome, categoria, faixa, criador);
        this.setTemporadas(temporadas);
        this.setEpisodiosPorTemporada(episodiosPorTemporada);
        this.setDuracaoEpisodio(duracaoEpisodio);
    }

    public int getTemporadas() {
        return temporadas;
    }

    public void setTemporadas(int temporadas) {
        if (temporadas <= 0) {
            System.out.println("Erro: a serie deve ter pelo menos 1 temporada.");
        } else {
            this.temporadas = temporadas;
        }
    }

    public int getEpisodiosPorTemporada() {
        return episodiosPorTemporada;
    }

    public void setEpisodiosPorTemporada(int episodiosPorTemporada) {
        if (episodiosPorTemporada <= 0) {
            System.out.println("Erro: cada temporada deve ter pelo menos 1 episodio.");
        } else {
            this.episodiosPorTemporada = episodiosPorTemporada;
        }
    }

    public int getDuracaoEpisodio() {
        return duracaoEpisodio;
    }

    public void setDuracaoEpisodio(int duracaoEpisodio) {
        if (duracaoEpisodio <= 0) {
            System.out.println("Erro: a duracao do episodio deve ser maior que zero.");
        } else {
            this.duracaoEpisodio = duracaoEpisodio;
        }
    }

    // ---------- Sobreposicao (@Override) ----------

    @Override
    public int getDuracaoTotal() {
        return temporadas * episodiosPorTemporada * duracaoEpisodio;
    }

    @Override
    public String getTipo() {
        return "Serie";
    }

    @Override
    public double calcularRoyalties() {
        return getTotalReproducoes() * valorPorReproducao;
    }

    @Override
    public boolean estaValido() {
        return super.estaValido() && temporadas > 0 && episodiosPorTemporada > 0 && duracaoEpisodio > 0;
    }

    @Override
    public boolean reproduzir(SuperClassUsuarios usuario) {
        boolean liberado = super.reproduzir(usuario);
        if (liberado) {
            System.out.println("Reproduzindo a serie \"" + nome + "\" (" + temporadas + " temporadas, "
                    + (temporadas * episodiosPorTemporada) + " episodios). Boa maratona, "
                    + usuario.getNome() + "!");
        }
        return liberado;
    }
}
