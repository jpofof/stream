package stream.conteudo;

import stream.usuario.SuperClassUsuarios;

/**
 * Filme: tem uma duracao unica, em minutos.
 */
public class Filme extends Conteudo {

    private int duracao;                           // em minutos
    private double valorPorReproducao = 0.50;      // R$ pagos ao criador a cada reproducao

    public Filme(String nome, String categoria, int faixa, String criador, int duracao) {
        super(nome, categoria, faixa, criador);
        this.setDuracao(duracao);
    }

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {
        if (duracao <= 0) {
            System.out.println("Erro: a duracao do filme deve ser maior que zero.");
        } else {
            this.duracao = duracao;
        }
    }

    // ---------- Sobreposicao (@Override): escrevendo os metodos da classe mae ----------

    @Override
    public int getDuracaoTotal() {
        return duracao;
    }

    @Override
    public String getTipo() {
        return "Filme";
    }

    @Override
    public double calcularRoyalties() {
        return getTotalReproducoes() * valorPorReproducao;
    }

    @Override
    public boolean estaValido() {
        return super.estaValido() && duracao > 0;
    }

    @Override
    public boolean reproduzir(SuperClassUsuarios usuario) {
        boolean liberado = super.reproduzir(usuario);
        if (liberado) {
            System.out.println("Reproduzindo o filme \"" + getNome() + "\" (" + getDuracaoFormatada()
                    + "). Bom filme, " + usuario.getNome() + "!");
        }
        return liberado;
    }
}
