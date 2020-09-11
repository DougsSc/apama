package entidades;

import java.util.List;

public class Pessoa {

    private Integer id;
    private Integer idTipoPessoa;
    private Integer liberarAdocao;
    private Integer status;

    private List<Contato> contatos;
    private Endereco endereco;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdTipoPessoa() {
        return idTipoPessoa;
    }

    public void setIdTipoPessoa(Integer idTipoPessoa) {
        this.idTipoPessoa = idTipoPessoa;
    }

    public Integer getLiberarAdocao() {
        return liberarAdocao;
    }

    public void setLiberarAdocao(Integer liberarAdocao) {
        this.liberarAdocao = liberarAdocao;
    }
    
    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public List<Contato> getContatos() {
        return contatos;
    }

    public void setContatos(List<Contato> contatos) {
        this.contatos = contatos;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }
}