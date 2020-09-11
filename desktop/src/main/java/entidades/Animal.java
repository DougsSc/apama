package entidades;

import java.util.List;

public class Animal {

    private Integer id;
    private AnimalPorte animalPorte;      // outra tabela
    private AnimalStatus animalStatus;    // outra tabela
    private Endereco endereco;            // outra tabela
    private String dataResgate;
    private String dataAdocao;
    private String nome;
    private String observacao;

    private List<Arquivo> arquivos;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDataResgate() {
        return dataResgate;
    }

    public void setDataResgate(String dataResgate) {
        this.dataResgate = dataResgate;
    }

    public String getDataAdocao() {
        return dataAdocao;
    }

    public void setDataAdocao(String dataAdocao) {
        this.dataAdocao = dataAdocao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public AnimalPorte getAnimalPorte() {
        return animalPorte;
    }

    public void setAnimalPorte(AnimalPorte animalPorte) {
        this.animalPorte = animalPorte;
    }

    public Endereco getEndereco() {
        if (this.endereco == null)
            endereco = new Endereco();
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public AnimalStatus getAnimalStatus() {
        return animalStatus;
    }

    public void setAnimalStatus(AnimalStatus animalStatus) {
        this.animalStatus = animalStatus;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public List<Arquivo> getArquivos() {
        return arquivos;
    }

    public void setArquivos(List<Arquivo> arquivos) {
        this.arquivos = arquivos;
    }
}
