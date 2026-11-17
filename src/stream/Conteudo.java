package stream;

import java.time.Duration;

public abstract class Conteudo {
    protected String nome;
    protected String categoria;
    protected int faixa;
    protected float nota;
    protected Duration duracao;

    public Conteudo(String nome, String categoria, int faixa,float nota) {
        this.categoria = categoria;
        this.faixa = faixa;
        this.nome = nome;
        this.setNota(nota);
    }

    public String getNome() {
        return nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public int getFaixa() {
        return faixa;
    }

    public float getNota() {
        return nota;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setNota(float nota) {
        if(nota >= 0 && nota <=5) {
            this.nota = nota;
        } else {
            System.out.println("Nota Inválida");
        }
    }

    
    
    public Duration getDuracao() {
    return duracao;
}

public void setDuracao(Duration duracao) {
    this.duracao = duracao;
}
