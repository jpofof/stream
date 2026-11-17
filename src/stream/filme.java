
package stream;

public class filme extends Conteudo{
    public filme(String nome, String categoria, int faixa, float nota) {
        super(nome, categoria, faixa, nota);
    }

    @Override
    public Duration getDuracao() {
        return duracao;
    }
}
