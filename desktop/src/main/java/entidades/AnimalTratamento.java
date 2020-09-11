package entidades;

public class AnimalTratamento {

    private Integer id;
    private TipoTratamento tipoTratamento;  //outra tabela
    private Animal animal;              //outra tabela
    private String dataTratamento;
    private String proximaDataTratamento;
    private String observacao;
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TipoTratamento getTipoTratamento() {
        return tipoTratamento;
    }

    public void setTipoTratamento(TipoTratamento tipoTratamento) {
        this.tipoTratamento = tipoTratamento;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public String getDataTratamento() {
        return dataTratamento;
    }

    public void setDataTratamento(String dataTratamento) {
        this.dataTratamento = dataTratamento;
    }

    public String getProximaDataTratamento() {
        return proximaDataTratamento;
    }

    public void setProximaDataTratamento(String proximaDataTratamento) {
        this.proximaDataTratamento = proximaDataTratamento;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}