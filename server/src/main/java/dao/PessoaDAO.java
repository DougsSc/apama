package dao;

import entidades.*;
import interfaces.Daos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PessoaDAO extends DAO implements Daos<Pessoa> {

    private static final Logger logger = LogManager.getLogger(PessoaDAO.class.getName());

    @Override
    public Pessoa carregaObjeto(ResultSet rs) {
        Pessoa pessoa = null;
        try{
            pessoa = new Pessoa();
            pessoa.setId(rs.getInt("id"));
            pessoa.setIdTipoPessoa(rs.getInt("id_tipo_pessoa"));
            pessoa.setStatus(rs.getInt("status"));
            pessoa.setLiberarAdocao(rs.getInt("liberar_adocao"));
            pessoa.setEndereco(this.getEndereco(pessoa.getId()));
            pessoa.setContatos(this.getContatos(pessoa.getId()));
        } catch (SQLException e) {
            logger.catching(e);
        }
        return pessoa;
    }

    @Override
    public boolean cadastra(Pessoa pessoa){
        String query = "INSERT INTO pessoa (id_tipo_pessoa,liberar_adocao) VALUES (?,?)";
        try {
            pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1,pessoa.getIdTipoPessoa());
            pst.setInt(2,pessoa.getLiberarAdocao());

            pst.execute();
            rs = pst.getGeneratedKeys();
            if(rs.next()){
                pessoa.setId(rs.getInt(1));
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
    public boolean atualiza(Pessoa pessoa) {
        String query = "" +
                "UPDATE pessoa SET " +
                "id_tipo_pessoa = ?, " +
                "liberar_adocao = ?, " +
                "status = ? " +
                "WHERE id = "+pessoa.getId();
        try {
            pst = con.prepareStatement(query);
            pst.setInt(1,pessoa.getIdTipoPessoa());
            pst.setInt(2,pessoa.getLiberarAdocao());
            pst.setInt(3,pessoa.getStatus());

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.catching(e);
            return false;
        } finally {
            close();
        }
    }

    @Override
    public Pessoa busca(Integer id) {
        Pessoa pessoa = null;

        try {
            pst = con.prepareStatement("SELECT * FROM pessoa WHERE id = ?");
            pst.setInt(1, id);

            rs = pst.executeQuery();
            if (rs.next()) {
                pessoa = this.carregaObjeto(rs);
            }
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            close();
        }

        return pessoa;
    }

    @Override
    public List<Pessoa> lista(Integer status, Integer offset, Integer limit) {
        List<Pessoa> pessoas = null;

        String query = "SELECT * FROM pessoa";
        if(status != null){
            query += " WHERE status = " + status;
        }
        if(offset != null){
            query += " OFFSET "+offset;
        }
        if(limit != null){
            query += " LIMIT "+limit;
        }

        try {
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            while(rs.next()){
                Pessoa pessoa = this.carregaObjeto(rs);
                pessoas.add(pessoa);
            }
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            close();
        }

        return pessoas;
    }

    public Endereco getEndereco(int idPessoa){
        Endereco endereco = null;

        String query = "" +
                "SELECT e.* " +
                "FROM pessoa_endereco pe " +
                "INNER JOIN endereco e ON (e.id = pe.id_endereco) " +
                "WHERE id_pessoa = "+idPessoa+" " +
                "AND data_inativacao IS NULL";
        try {
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            while(rs.next()){
                endereco = new Endereco();
                endereco.setId(rs.getInt("id"));
                endereco.setCidade(new CidadeDAO().busca(rs.getInt("id_cidade")));
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

    public List<Contato> getContatos(Integer idPessoa){
        ArrayList<Contato> contatos = null;
        //Pessoa pessoa = new PessoaDAO().busca(idPessoa);

        String query = "SELECT * FROM contato WHERE id_pessoa = "+idPessoa+" AND status = 1";
        try {
            contatos = new ArrayList<>();

            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            while(rs.next()){
                TipoContato tipoContato = new TipoContatoDAO().busca(rs.getInt("id_tipo_contato"));

                Contato contato = new Contato();
                contato.setId(rs.getInt("id"));
                contato.setIdPessoa(idPessoa);
                contato.setTipoContato(tipoContato);
                contato.setContato(rs.getString("contato"));
                contato.setObservacao(rs.getString("observacao"));
                contato.setStatus(rs.getInt("status"));

                contatos.add(contato);
            }
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            close();
        }

        return contatos;
    }

    public Object buscaTutorDoador(Integer id) {
        Object pessoa = null;
        try {
            String query = "" +
                    "SELECT pf.id_pessoa AS idPF, pj.id_pessoa AS idPJ " +
                    "FROM pessoa p " +
                    "LEFT JOIN pessoa_fisica pf ON (pf.id_pessoa = p.id) " +
                    "LEFT JOIN pessoa_juridica pj ON (pf.id_pessoa = p.id) " +
                    "WHERE p.id = ?";
            pst = con.prepareStatement(query);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                try{
                    Integer idP = rs.getInt("idPF");
                    pessoa = new PessoaFisicaDAO().busca(idP);
                }catch (SQLException epf){
                    try{
                        Integer idP = rs.getInt("idPJ");
                        pessoa = new PessoaJuridicaDAO().busca(idP);
                    }catch (SQLException epj){
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            close();
        }

        return pessoa;
    }

    public List<Object> listaTudoresDoadores(Integer status, Integer offset, Integer limit, Integer liberarAdocao){
        List<Object> pessoas = new ArrayList<>();

        String query = "" +
                "SELECT pf.id_pessoa AS idPF,pj.id_pessoa AS idPJ " +
                "FROM pessoa p " +
                "LEFT JOIN pessoa_fisica pf ON (pf.id_pessoa = p.id) " +
                "LEFT JOIN pessoa_juridica pj ON (pj.id_pessoa = p.id) " +
                "WHERE p.id_tipo_pessoa IN (2,3) ";
        if(status != null){
            query += " AND p.status = " + status;
        }
        if(liberarAdocao != null){
            query += " AND p.liberar_adocao = " + liberarAdocao;
        }
        query += " ORDER BY pf.nome ASC, pj.razao_social ASC ";
        if(offset != null){
            query += " OFFSET "+offset;
        }
        if(limit != null){
            query += " LIMIT "+limit;
        }

        try {
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            while(rs.next()){
                try {
                    Integer id = rs.getInt("idPF");
                    PessoaFisica pessoa = new PessoaFisicaDAO().busca(id);
                    pessoas.add(pessoa);
                }catch (SQLException epf){
                    try {
                        Integer id = rs.getInt("idPJ");
                        PessoaJuridica pessoa = new PessoaJuridicaDAO().busca(id);
                        pessoas.add(pessoa);
                    }catch (SQLException epj){
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            close();
        }

        return pessoas;
    }

    public boolean inativaContatos(Integer idPessoa){
        String query = "UPDATE contato SET status = 0 WHERE id_pessoa = "+idPessoa;
        try {
            pst = con.prepareStatement(query);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.catching(e);
            return false;
        } finally {
            close();
        }
    }

    public String getEmail(Integer idPessoa){
        String email = null;
        String query = "" +
                "SELECT contato " +
                "FROM contato " +
                "WHERE id_pessoa = "+idPessoa+" " +
                "AND id_tipo_contato = 3 " +
                "AND status = 1";
        try {
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            if(rs.next()){
                email = rs.getString("contato");
            }
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            close();
        }
        return email;
    }
}
