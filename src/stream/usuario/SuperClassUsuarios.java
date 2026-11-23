package stream.usuario;

import stream.conteudo.Conteudo;

/**
 * Superclasse abstrata dos usuarios da plataforma.
 * A conta pode ser Infantil (menor de 18 anos) ou Adulto (18 anos ou mais),
 * e cada uma decide, do seu jeito, o que pode assistir.
 */
public abstract class SuperClassUsuarios {

    private String nome;
    private int idade;

    public SuperClassUsuarios(String nome, int idade) {
        setNome(nome);
        validarIdade(idade);
        this.idade = idade;
    }

    public String getNome() {
        return nome;
    }

    public final void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do usuario nao pode ser vazio.");
        }
        this.nome = nome.trim();
    }

    public int getIdade() {
        return idade;
    }

    // Nao e final: Infantil e Adulto reescrevem para garantir que a idade combina com o tipo de conta.
    public void setIdade(int idade) {
        validarIdade(idade);
        this.idade = idade;
    }

    private void validarIdade(int idade) {
        if (idade < 0 || idade > 120) {
            throw new IllegalArgumentException("Idade invalida. Use um valor entre 0 e 120.");
        }
    }

    // Controle parental: cada tipo de conta tem a sua regra.
    public abstract boolean podeAssistir(Conteudo conteudo);

    // "Infantil" ou "Adulta"
    public abstract String getTipoConta();

    @Override
    public String toString() {
        return nome + " (" + idade + " anos, conta " + getTipoConta() + ")";
    }
}
