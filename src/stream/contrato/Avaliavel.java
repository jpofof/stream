package stream.contrato;

/**
 * Contrato de avaliacao: quem assinar este contrato (implements Avaliavel)
 * e obrigado a escrever os metodos abaixo.
 */
public interface Avaliavel {

    // Registra uma nota de 1 a 5 estrelas. Devolve true se a nota foi aceita.
    boolean avaliar(int estrelas);

    // Media das notas recebidas.
    double getNota();
}
