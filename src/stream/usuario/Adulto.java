package stream.usuario;

import stream.conteudo.Conteudo;

/**
 * Conta adulta (18 anos ou mais): a maior faixa etaria e 18, entao pode assistir tudo.
 */
public class Adulto extends SuperClassUsuarios {

    public Adulto(String nome, int idade) {
        super(nome, idade);
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
