package interfaces;

import java.sql.ResultSet;
import java.util.List;

public interface Daos<T> {

    public T carregaObjeto(ResultSet rs);

    /**
     * Cadastra os registros contidos no objeto
     * @param t - Objeto com os dados que serao cadastrados
     * @return true - em caso de sucesso | false - em caso de erro
     */
    public boolean cadastra(T t);

    /**
     * Atualiza os registros contidos no Objeto
     * @param t - Objeto com os dados que serao atualizados
     * @return true - em caso de sucesso | false - em caso de erro
     */
    public boolean atualiza(T t);

    /**
     * Busca os dados referentes ao ID passado, carrega e retorna Objeto com os dados encontrados
     * @param id
     * @return T
     */
    public T busca(Integer id);

    /**
     * Lista os registros cadastrados conforme o status escolhido
     * @param status - Status dos registros [null=Todos; 0=Inativos; 1=Ativos]
     * @return List com os dados encontrados
     */
    public List<T> lista(Integer status, Integer offset, Integer limit);
}