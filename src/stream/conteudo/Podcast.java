package stream.conteudo;

import stream.usuario.SuperClassUsuarios;

/**
 * Podcast: cada objeto e um EPISODIO de podcast (so audio).
 */
public class Podcast extends Conteudo {

    private int numeroEpisodio;
    private int duracao;                           // minutos de audio
    private double valorPorReproducao = 0.10;      // R$ pagos ao criador a cada reproducao

    public Podcast(String nome, String categoria, int faixa, String criador,
                   int numeroEpisodio, int duracao) {
        super(nome, categoria, faixa, criador);
        this.setNumeroEpisodio(numeroEpisodio);
        this.setDuracao(duracao);
    }

    public int getNumeroEpisodio() {
        return numeroEpisodio;
    }

    public void setNumeroEpisodio(int numeroEpisodio) {
        if (numeroEpisodio <= 0) {
            System.out.println("Erro: o numero do episodio deve ser maior que zero.");
        } else {
            this.numeroEpisodio = numeroEpisodio;
        }
    }

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {
        if (duracao <= 0) {
            System.out.println("Erro: os minutos de audio devem ser maiores que zero.");
        } else {
            this.duracao = duracao;
        }
    }

    // ---------- Sobreposicao (@Override) ----------

    @Override
    public int getDuracaoTotal() {
        return duracao;
    }

    @Override
    public String getTipo() {
        return "Podcast";
    }

    @Override
    public double calcularRoyalties() {
        return getTotalReproducoes() * valorPorReproducao;
    }

    @Override
    public boolean estaValido() {
        return super.estaValido() && numeroEpisodio > 0 && duracao > 0;
    }

    @Override
    public boolean reproduzir(SuperClassUsuarios usuario) {
        boolean liberado = super.reproduzir(usuario);
        if (liberado) {
            System.out.println("Tocando o episodio " + numeroEpisodio + " do podcast \"" + getNome()
                    + "\" (" + getDuracaoFormatada() + " de audio). Boa audicao, "
                    + usuario.getNome() + "!");
        }
        return liberado;
    }
}
