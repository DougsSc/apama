package entidades;

public class AnimalStatus {

    private Integer id;
    private String descricao;
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer isAtivo() {
        return status;
    }

    public void setAtivo(Integer status) {
        this.status = status;
    }
}
