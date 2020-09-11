package entidades;

public class TipoPessoa {

    private Integer id;
    private String descricao;
    private Integer acessaSistema;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getDescricao()
    {
        return descricao;
    }

    public void setDescricao(String descricao)
    {
        this.descricao = descricao;
    }

    public Integer isAcessaSistema()
    {
        return acessaSistema;
    }

    public void setAcessaSistema(Integer acessaSistema)
    {
        this.acessaSistema = acessaSistema;
    }
}