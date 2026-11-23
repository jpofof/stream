package stream.contrato;

/**
 * Contrato de pagamento ao criador do conteudo (royalties).
 */
public interface Remuneravel {

    // Nome de quem recebe os royalties.
    String getCriador();

    // Valor em reais que o criador recebe, de acordo com as reproducoes.
    double calcularRoyalties();
}
