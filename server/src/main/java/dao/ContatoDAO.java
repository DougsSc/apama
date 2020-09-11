package dao;

import entidades.Contato;
import entidades.Pessoa;
import entidades.TipoContato;
import interfaces.Daos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilidades.Utilidades;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ContatoDAO extends DAO implements Daos<Contato> {

    private static final Logger logger = LogManager.getLogger(ContatoDAO.class.getName());

    @Override
    public Contato carregaObjeto(ResultSet rs) {
        return null;
    }

    @Override
    public boolean cadastra(Contato contato) {
        String query = "INSERT INTO contato (id_pessoa,id_tipo_contato,contato,observacao) VALUES (?,?,?,?)";
        try {
            String cont = contato.getContato();
            if(contato.getTipoContato().getId() == 1 || contato.getTipoContato().getId() == 2){
                //- Telefone ou celular
                cont = Utilidades.somenteNumeros(cont);
            }

            pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1,contato.getIdPessoa());
            pst.setInt(2,contato.getTipoContato().getId());
            pst.setString(3,cont);
            pst.setString(4,contato.getObservacao());

            pst.execute();
            rs = pst.getGeneratedKeys();
            if(rs.next()){
                contato.setId(rs.getInt(1));
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            logger.catching(e);
            return false;
        } finally {
            close();
        }
    }

    @Override
    public boolean atualiza(Contato contato) {
        String query = "" +
                "UPDATE contato SET " +
                "id_tipo_contato = ?, " +
                "contato = ?, " +
                "observacao = ?, " +
                "status = ? " +
                "WHERE id = "+contato.getId();
        try {
            pst = con.prepareStatement(query);
            pst.setInt(1,contato.getTipoContato().getId());
            pst.setString(2,contato.getContato());
            pst.setString(3,contato.getObservacao());
            pst.setInt(4,contato.getStatus());

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.catching(e);
            return false;
        } finally {
            close();
        }
    }

    @Override
    public Contato busca(Integer id) {
        Contato contato = null;

        try {
            pst = con.prepareStatement("SELECT * FROM contato WHERE id = ?");
            pst.setInt(1, id);

            rs = pst.executeQuery();
            if (rs.next()) {
                TipoContato tipoContato = new TipoContatoDAO().busca(rs.getInt("id_tipo_contato"));
                //Pessoa pessoa = new PessoaDAO().busca(rs.getInt("id_pessoa"));

                contato = new Contato();
                contato.setId(rs.getInt("id"));
                //contato.setPessoa(pessoa);
                contato.setIdPessoa(rs.getInt("id_pessoa"));
                contato.setTipoContato(tipoContato);
                contato.setContato(rs.getString("contato"));
                contato.setObservacao(rs.getString("observacao"));
            }
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            close();
        }

        return contato;
    }

    @Override
    public List<Contato> lista(Integer status, Integer offset, Integer limit) {
        List<Contato> contatos = new ArrayList();

        try {
            String query = "" +
                    "SELECT * " +
                    "FROM contato " +
                    "INNER JOIN pessoa ON contato.id_pessoa = pessoa.id " +
                    "INNER JOIN tipo_contato ON contato.id_tipo_contato = tipo_contato.id ";
            if (status != null) {
                query += "WHERE contato.status = " + status; // quando estiver ativo, status = 1";
            }
            if(offset != null){
                query += " OFFSET "+offset;
            }
            if(limit != null){
                query += " LIMIT "+limit;
            }

            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            while(rs.next()){

                Contato contato = new Contato();
                contato.setId(rs.getInt("id"));
                contato.setContato(rs.getString("contato"));
                contato.setObservacao(rs.getString("observacao"));
                contato.setStatus(rs.getInt("status"));
                contato.setIdPessoa(rs.getInt("id_pessoa"));

                /*Pessoa pessoa = new Pessoa();
                pessoa.setId(rs.getInt("id"));
                pessoa.setIdTipoPessoa(rs.getInt("id_tipo_pessoa"));
                pessoa.setStatus(rs.getInt("status"));
                pessoa.setLiberarAdocao(rs.getInt("liberar_adocao"));*/
                //contato.setPessoa(pessoa);


                TipoContato tipoContato = new TipoContato();
                tipoContato.setId(rs.getInt("id"));
                tipoContato.setDescricao(rs.getString("descricao"));
                tipoContato.setStatus(rs.getInt("status"));
                contato.setTipoContato(tipoContato);

                contatos.add(contato);
            }
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            close();
        }

        return contatos;
    }
}
