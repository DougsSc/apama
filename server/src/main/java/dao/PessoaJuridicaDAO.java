package dao;

import entidades.Endereco;
import entidades.Pessoa;
import entidades.PessoaJuridica;
import interfaces.Daos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class PessoaJuridicaDAO extends DAO implements Daos<PessoaJuridica> {

    private static final Logger logger = LogManager.getLogger(PessoaJuridicaDAO.class.getName());

    @Override
    public PessoaJuridica carregaObjeto(ResultSet rs) {
        PessoaJuridica pj = null;
        try{
            pj = new PessoaJuridica();
            pj.setId(rs.getInt("id"));
            pj.setIdTipoPessoa(rs.getInt("id_tipo_pessoa"));
            pj.setStatus(rs.getInt("status"));
            pj.setLiberarAdocao(rs.getInt("liberar_adocao"));
            pj.setRazaoSocial(rs.getString("razao_social"));
            pj.setNomeFantasia(rs.getString("nome_fantasia"));
            pj.setCnpj(rs.getString("cnpj"));
            pj.setEndereco(new PessoaDAO().getEndereco(pj.getId()));
            pj.setContatos(new PessoaDAO().getContatos(pj.getId()));
        } catch (SQLException e) {
            logger.catching(e);
        }
        return pj;
    }

    @Override
    public boolean cadastra(PessoaJuridica pessoaJuridica) {
        //- Primeiro tenta cadastrar os dados da pessoa
        if(!new PessoaDAO().cadastra(pessoaJuridica)){
            return false;
        }

        //- Cadastro da pessoa realizado com sucesso, agora registra os dados de pessoa juridica
        String query = "INSERT INTO pessoa_juridica (id_pessoa,razao_social,nome_fantasia,cnpj) VALUES (?,?,?,?)";
        try {
            pst = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1,pessoaJuridica.getId());
            pst.setString(2,pessoaJuridica.getRazaoSocial());
            pst.setString(3,pessoaJuridica.getNomeFantasia());
            pst.setString(4,pessoaJuridica.getCnpj());

            pst.execute();
            return pst.getGeneratedKeys().next();
        } catch (SQLException e) {
            logger.catching(e);
            return false;
        } finally {
            close();
        }
    }

    @Override
    public boolean atualiza(PessoaJuridica pessoaJuridica) {
        //- Primeiro tenta atualizar os dados da pessoa
        if(!new PessoaDAO().atualiza(pessoaJuridica)){
            return false;
        }

        //- Atualizacao da pessoa realizado com sucesso, agora atualiza os dados de pessoa juridica
        String query = "" +
                "UPDATE pessoa_juridica SET " +
                "razao_social = ?, " +
                "nome_fantasia = ?, " +
                "cnpj = ? " +
                "WHERE id_pessoa = "+pessoaJuridica.getId();
        try {
            pst = con.prepareStatement(query);
            pst.setString(1,pessoaJuridica.getRazaoSocial());
            pst.setString(2,pessoaJuridica.getNomeFantasia());
            pst.setString(3,pessoaJuridica.getCnpj());

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.catching(e);
            return false;
        } finally {
            close();
        }
    }

    @Override
    public PessoaJuridica busca(Integer id) {
        PessoaJuridica pessoa = null;

        try {
            pst = con.prepareStatement("SELECT * FROM pessoa p INNER JOIN pessoa_juridica pj ON (p.id = pj.id_pessoa) WHERE p.id = ?");
            pst.setInt(1, id);

            rs = pst.executeQuery();
            if (rs.next()) {
                pessoa = new PessoaJuridica();
                pessoa.setId(rs.getInt("id"));
                pessoa.setIdTipoPessoa(rs.getInt("id_tipo_pessoa"));
                pessoa.setLiberarAdocao(rs.getInt("liberar_adocao"));
                pessoa.setStatus(rs.getInt("status"));
                pessoa.setRazaoSocial(rs.getString("razao_social"));
                pessoa.setNomeFantasia(rs.getString("nome_fantasica"));
                pessoa.setCnpj(rs.getString("cnpj"));
            }
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            close();
        }

        return pessoa;
    }

    @Override
    public List<PessoaJuridica> lista(Integer status, Integer offset, Integer limit) {
        List<PessoaJuridica> pessoas = null;

        String query = "" +
                "SELECT p.id,p.id_tipo_pessoa,p.status,p.liberar_adocao," +
                "pj.razao_social,pj.nome_fantasia,pj.cnpj " +
                "FROM pessoa_juridica pj " +
                "INNER JOIN pessoa p ON (p.id = pj.id_pessoa)";
        if(status != null){
            query += " WHERE p.status = " + status;
        }
        query += " ORDER BY pj.razao_social ASC ";
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
                PessoaJuridica pj = this.carregaObjeto(rs);
                pessoas.add(pj);
            }
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            close();
        }

        return pessoas;
    }
}
