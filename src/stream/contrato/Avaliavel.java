package stream.contrato;

/**
 * Contrato de avaliacao por estrelas.
 * Todo conteudo que pode receber notas de 1 a 5 deve cumprir este contrato.
 */
public interface Avaliavel {

    // Registra uma nota de 1 a 5 estrelas e recalcula a media.
    void avaliar(int estrelas);

    // Media atual das notas (0 se ainda nao houver avaliacoes).
    float getNota();

    // Quantidade de avaliacoes recebidas ate agora.
    int getQuantidadeAvaliacoes();
}
