package stream;


import java.time.Duration;

public class Serie extends Conteudo {

    public Serie(String nome, String categoria, int faixa, float nota, Duration duracao) {
        super(nome, categoria, faixa, nota, duracao);
    }

    public void addTemporada(int temporada, Duration duracao) {
        temporada.get(temporada-1).add(duracao);
    }

    public int getQuantidadeTemporadas() {
    return temporadas.size();
    }

    public Duration getDuracaoTotal() {
    Duration total = Duration.ZERO;

    for (ArrayList<Duration> temporada : temporadas) {
        for (Duration episodio : temporada) {
            total = total.plus(episodio);
        }
    }

    return total;
}
}