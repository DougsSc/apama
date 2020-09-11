package entidades;

public class Arquivo {

    private Integer id;
    private String arquivo;
    private String nome;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getArquivo()
    {
        return arquivo;
    }

    public void setArquivo(String arquivo)
    {
        this.arquivo = arquivo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
