package entidades;

import java.util.List;

public class Adocao {

    private Integer id;
    private Animal animal;        //outra tabela
    private PessoaFisica tutor;   //outra tabela
    private Usuario usuario;      //outra tabela
    private AdocaoStatus status;
    private String dataRegistro;
    private String dataAdocao;
    private String observacao;
    private List<Arquivo> arquivos;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public PessoaFisica getTutor() {
        return tutor;
    }

    public void setTutor(PessoaFisica tutor) {
        this.tutor = tutor;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(String dataRegistro) {
        this.dataRegistro = dataRegistro;
    }
    
    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public AdocaoStatus getStatus() {
        return status;
    }

    public void setStatus(AdocaoStatus status) {
        this.status = status;
    }

    public List<Arquivo> getArquivos() {
        return arquivos;
    }

    public void setArquivos(List<Arquivo> arquivos) {
        this.arquivos = arquivos;
    }

    public String getDataAdocao() {
        return dataAdocao;
    }

    public void setDataAdocao(String dataAdocao) {
        this.dataAdocao = dataAdocao;
    }
}
