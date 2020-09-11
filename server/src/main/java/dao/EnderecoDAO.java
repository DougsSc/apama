package dao;

import entidades.Cidade;
import entidades.Endereco;
import interfaces.Daos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilidades.Utilidades;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EnderecoDAO extends DAO implements Daos<Endereco> {

    private static final Logger logger = LogManager.getLogger(EnderecoDAO.class.getName());

    @Override
    public Endereco carregaObjeto(ResultSet rs) {
        return null;
    }

    @Override
    public boolean cadastra(Endereco endereco) {
        String query = "" +
                "INSERT INTO endereco (id_cidade,logradouro,numero,complemento,bairro,cep) " +
                "VALUES " +
                "(?,?,?,?,?,?)";
        try {
            pst = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setInt(1,endereco.getCidade().getId());
            pst.setString(2,endereco.getLogradouro());
            pst.setString(3,endereco.getNumero());
            pst.setString(4,endereco.getComplemento());
            pst.setString(5,endereco.getBairro());
            pst.setString(6, Utilidades.somenteNumeros(endereco.getCep()));

            pst.execute();
            rs = pst.getGeneratedKeys();
            if(rs.next()){
                endereco.setId(rs.getInt(1));
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
    public boolean atualiza(Endereco endereco) {
        String query = "" +
                "UPDATE endereco SET " +
                "id_cidade = ?, " +
                "logradouro = ?, " +
                "numero = ?, " +
                "complemento = ?, " +
                "bairro = ?, " +
                "cep = ? " +
                "WHERE id = "+endereco.getId();
        try {
            pst = con.prepareStatement(query);
            pst.setInt(1,endereco.getCidade().getId());
            pst.setString(2,endereco.getLogradouro());
            pst.setString(3,endereco.getNumero());
            pst.setString(4,endereco.getComplemento());
            pst.setString(5,endereco.getBairro());
            pst.setString(6,Utilidades.somenteNumeros(endereco.getCep()));

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.catching(e);
            return false;
        } finally {
            close();
        }
    }

    @Override
    public Endereco busca(Integer id) {
        Endereco endereco = null;

        try {
            pst = con.prepareStatement("SELECT * FROM endereco WHERE id = ?");
            pst.setInt(1, id);

            rs = pst.executeQuery();
            if (rs.next()) {
                CidadeDAO cidadeDAO = new CidadeDAO();
                Cidade cidade = cidadeDAO.busca(rs.getInt("id_cidade"));

                endereco = new Endereco();
                endereco.setId(rs.getInt("id"));
                endereco.setCidade(cidade);
                endereco.setLogradouro(rs.getString("logradouro"));
                endereco.setNumero(rs.getString("numero"));
                endereco.setComplemento(rs.getString("complemento"));
                endereco.setBairro(rs.getString("bairro"));
                endereco.setCep(rs.getString("cep"));
            }
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            close();
        }

        return endereco;
    }

    @Override
    public List<Endereco> lista(Integer status, Integer offset, Integer limit) {
        List<Endereco> enderecos = new ArrayList();
        CidadeDAO cidadeDAO = new CidadeDAO();
        Endereco endereco = null;


        try {
            String query = "SELECT * FROM endereco";
            if(status >= 0){
                query += " WHERE status = "+status;
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
                Cidade cidade = cidadeDAO.busca(rs.getInt("id_cidade"));

                endereco = new Endereco();
                endereco.setId(rs.getInt("id"));
                endereco.setCidade(cidade);
                endereco.setLogradouro(rs.getString("logradouro"));
                endereco.setNumero(rs.getString("numero"));
                endereco.setComplemento(rs.getString("complemento"));
                endereco.setBairro(rs.getString("bairro"));
                endereco.setCep("cep");

                enderecos.add(endereco);
            }
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            close();
        }

        return enderecos;
    }

    public boolean cadastraPessoaEndereco(Integer idPessoa, Integer idEndereco){
        String query = "" +
                "UPDATE pessoa_endereco SET " +
                "data_inativacao = NOW() " +
                "WHERE id_pessoa = " + idPessoa + " " +
                "AND data_inativacao IS NULL";
        try {
            pst = con.prepareStatement(query);
            pst.executeUpdate();

            query = "" +
                    "INSERT INTO pessoa_endereco (id_pessoa,id_endereco,data_ativacao) " +
                    "VALUES " +
                    "(?,?,NOW())";
            pst = con.prepareStatement(query);
            pst.setInt(1,idPessoa);
            pst.setInt(2,idEndereco);
            pst.execute();

            return true;
        } catch (SQLException e) {
            logger.catching(e);
            return false;
        } finally {
            close();
        }
    }

    public boolean cadastraAnimalEndereco(Integer idAnimal, Integer idEndereco){
        String query = "" +
                "UPDATE animal_endereco SET " +
                "data_inativacao = NOW() " +
                "WHERE id_animal = " + idAnimal + " " +
                "AND data_inativacao IS NULL";
        try {
            pst = con.prepareStatement(query);
            pst.executeUpdate();

            query = "" +
                    "INSERT INTO animal_endereco (id_animal,id_endereco,data_ativacao) " +
                    "VALUES " +
                    "(?,?,NOW())";
            pst = con.prepareStatement(query);
            pst.setInt(1,idAnimal);
            pst.setInt(2,idEndereco);
            pst.execute();

            return true;
        } catch (SQLException e) {
            logger.catching(e);
            return false;
        } finally {
            close();
        }
    }
}
