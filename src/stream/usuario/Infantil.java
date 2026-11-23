package stream.usuario;

import stream.conteudo.Conteudo;

/**
 * Conta infantil (menor de 18 anos): so pode assistir o que tem faixa etaria
 * menor ou igual a sua idade.
 */
public class Infantil extends SuperClassUsuarios {

    public Infantil(String nome, int idade) {
        super(nome, idade);
        validarIdadeInfantil(idade);
    }

    @Override
    public void setIdade(int idade) {
        validarIdadeInfantil(idade);
        super.setIdade(idade);
    }

    private void validarIdadeInfantil(int idade) {
        if (idade >= 18) {
            throw new IllegalArgumentException("A conta infantil e somente para menores de 18 anos.");
        }
    }

    @Override
    public boolean podeAssistir(Conteudo conteudo) {
        return conteudo.getFaixa() <= getIdade();
    }

    @Override
    public String getTipoConta() {
        return "Infantil";
    }
}
