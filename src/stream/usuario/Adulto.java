package stream.usuario;

import stream.conteudo.Conteudo;

/**
 * Conta adulta (18 anos ou mais): como a maior faixa etaria e 18, pode assistir tudo.
 */
public class Adulto extends SuperClassUsuarios {

    public Adulto(String nome, int idade) {
        super(nome, idade);
        validarIdadeAdulta(idade);
    }

    @Override
    public void setIdade(int idade) {
        validarIdadeAdulta(idade);
        super.setIdade(idade);
    }

    private void validarIdadeAdulta(int idade) {
        if (idade < 18) {
            throw new IllegalArgumentException("A conta adulta e somente para maiores de 18 anos.");
        }
    }

    @Override
    public boolean podeAssistir(Conteudo conteudo) {
        return true;
    }

    @Override
    public String getTipoConta() {
        return "Adulta";
    }
}
