package entidades;

import java.io.Serializable;
import java.util.List;

public class Usuario extends PessoaFisica implements Serializable {

    private String login;
    private String senha;
    private Integer idTipoUsuario;
    private Integer statusUsuario;
    private List<Permissao> permissoes;

    public String getLogin()
    {
        return login;
    }

    public void setLogin(String login)
    {
        this.login = login;
    }

    public String getSenha()
    {
        return senha;
    }

    public void setSenha(String senha)
    {
        this.senha = senha;
    }

    public Integer getIdTipoUsuario()
    {
        return idTipoUsuario;
    }

    public void setIdTipoUsuario(Integer idTipoUsuario)
    {
        this.idTipoUsuario = idTipoUsuario;
    }

    public Integer getStatusUsuario()
    {
        return statusUsuario;
    }

    public void setStatusUsuario(Integer statusUsuario)
    {
        this.statusUsuario = statusUsuario;
    }

    public List<Permissao> getPermissoes() {
        return permissoes;
    }

    public void setPermissoes(List<Permissao> permissoes) {
        this.permissoes = permissoes;
    }
}
