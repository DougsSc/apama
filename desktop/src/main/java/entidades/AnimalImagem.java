package entidades;

public class AnimalImagem {

    private Integer id;
    private Animal animal;
    private Arquivo arquivo;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Animal getAnimal()
    {
        return animal;
    }

    public void setAnimal(Animal animal)
    {
        this.animal = animal;
    }

    public Arquivo getArquivo()
    {
        return arquivo;
    }

    public void setArquivo(Arquivo arquivo)
    {
        this.arquivo = arquivo;
    }
}
