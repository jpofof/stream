package stream.usuario;

import stream.conteudo.Conteudo;

/**
 * Classe mae (abstrata) dos usuarios. A conta pode ser Infantil (menor de 18 anos)
 * ou Adulto (18 anos ou mais), e cada uma decide o que pode assistir.
 */
public abstract class SuperClassUsuarios {

    private String nome;
    private int idade = -1;   // -1 = ainda nao informada

    public SuperClassUsuarios(String nome, int idade) {
        this.setNome(nome);
        this.setIdade(idade);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().equals("")) {
            System.out.println("Erro: o nome do usuario nao pode ser vazio.");
        } else {
            this.nome = nome;
        }
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        if (idade < 0 || idade > 120) {
            System.out.println("Erro: idade invalida. Use um valor entre 0 e 120.");
        } else {
            this.idade = idade;
        }
    }

    // Diz se o nome e a idade foram aceitos pelos setters.
    public boolean estaValido() {
        return nome != null && idade >= 0;
    }

    // Controle parental: cada tipo de conta escreve a sua regra (sobreposicao).
    public abstract boolean podeAssistir(Conteudo conteudo);

    // "Infantil" ou "Adulta".
    public abstract String getTipoConta();

    public String getResumo() {
        return nome + " (" + idade + " anos, conta " + getTipoConta() + ")";
    }
}
