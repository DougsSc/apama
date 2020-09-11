package entidades;

public class TipoUsuarioPermissao {

    private Integer id;
    private TipoUsuario tipoUsuario;      //outra tabela
    private TipoPermissao tipoPermissao;  //outra tabela

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public TipoUsuario getTipoUsuario()
    {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario)
    {
        this.tipoUsuario = tipoUsuario;
    }

    public TipoPermissao getTipoPermissao()
    {
        return tipoPermissao;
    }

    public void setTipoPermissao(TipoPermissao tipoPermissao)
    {
        this.tipoPermissao = tipoPermissao;
    }
}
