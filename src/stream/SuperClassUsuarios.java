
package com.mycompany.stream;

public class SuperClassUsuarios {
    private int idade;
    private String Sexo;
    private boolean modelconta;

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getSexo() {
        return Sexo;
    }

    public void setSexo(String Sexo) {
        this.Sexo = Sexo;
    }
    
    public void Verificacao(){
        if(this.idade < 18){
            System.out.println("Sua Conta é Infantil");
            this.modelconta = false;
            
        }
        else{
            System.out.println("Sua conta é Adulta");
            this.modelconta = true;
            
        }
    }
}
