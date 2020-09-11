package entidades;

import utilidades.Utilidades;

public class PessoaFisica extends Pessoa {
    
    private String nome;
    private String cpf;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return Utilidades.somenteNumeros(cpf);
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
