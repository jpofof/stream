package stream.contrato;

/**
 * Contrato de remuneracao do criador do conteudo.
 * Cada tipo de conteudo calcula os royalties do seu jeito.
 */
public interface Remuneravel {

    // Nome do criador que recebe os royalties.
    String getCriador();

    // Valor (em reais) devido ao criador com base nas reproducoes reais.
    double calcularRoyalties();
}
