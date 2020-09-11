package entidades;

public class TipoDoacao {

    private Integer id;
    private String descricao;
    private String unidadeMedida;
    private String mascara;
    private Integer exigeValor;

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
    
    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public String getMascara() {
        return mascara;
    }

    public void setMascara(String mascara) {
        this.mascara = mascara;
    }

    public Integer getExigeValor() {
        return exigeValor;
    }

    public void setExigeValor(Integer exigeValor) {
        this.exigeValor = exigeValor;
    }
}