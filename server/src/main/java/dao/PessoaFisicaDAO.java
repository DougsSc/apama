package dao;

import entidades.Endereco;
import entidades.Pessoa;
import entidades.PessoaFisica;
import interfaces.Daos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class PessoaFisicaDAO extends DAO implements Daos<PessoaFisica> {

    private static final Logger logger = LogManager.getLogger(PessoaFisicaDAO.class.getName());

    @Override
    public PessoaFisica carregaObjeto(ResultSet rs){
        PessoaFisica pf = null;
        try{
            pf = new PessoaFisica();
            pf.setId(rs.getInt("id"));
            pf.setIdTipoPessoa(rs.getInt("id_tipo_pessoa"));
            pf.setStatus(rs.getInt("status"));
            pf.setLiberarAdocao(rs.getInt("liberar_adocao"));
            pf.setNome(rs.getString("nome"));
            pf.setCpf(rs.getString("cpf"));
            pf.setEndereco(new PessoaDAO().getEndereco(pf.getId()));
            pf.setContatos(new PessoaDAO().getContatos(pf.getId()));
        } catch (SQLException e) {
            logger.catching(e);
        }
        return pf;
    }

    @Override
    public boolean cadastra(PessoaFisica pessoaFisica) {
        //- Primeiro tenta cadastrar os dados da pessoa
        if(!new PessoaDAO().cadastra(pessoaFisica)){
            return false;
        }

        //- Cadastro da pessoa realizado com sucesso, agora registra os dados de pessoa fisica
        String query = "INSERT INTO pessoa_fisica (id_pessoa,nome,cpf) VALUES (?,?,?)";
        System.out.println(query);
        try {
            pst = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1,pessoaFisica.getId());
            pst.setString(2,pessoaFisica.getNome());
            pst.setString(3,pessoaFisica.getCpf());

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
    public boolean atualiza(PessoaFisica pessoaFisica) {
        //- Primeiro tenta atualizar os dados da pessoa
        if(!new PessoaDAO().atualiza(pessoaFisica)){
            return false;
        }

        //- Atualizacao da pessoa realizado com sucesso, agora atualiza os dados de pessoa fisica
        String query = "" +
                "UPDATE pessoa_fisica SET " +
                "nome = ?, " +
                "cpf = ? " +
                "WHERE id_pessoa = "+pessoaFisica.getId();
        try {
            pst = con.prepareStatement(query);
            pst.setString(1,pessoaFisica.getNome());
            pst.setString(2,pessoaFisica.getCpf());

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.catching(e);
            return false;
        } finally {
            close();
        }
    }

    @Override
    public PessoaFisica busca(Integer id) {
        PessoaFisica pessoa = null;

        String query = "" +
                "SELECT p.id,p.id_tipo_pessoa,p.status,p.liberar_adocao," +
                "pf.nome,pf.cpf " +
                "FROM pessoa p " +
                "INNER JOIN pessoa_fisica pf ON (p.id = pf.id_pessoa) " +
                "WHERE p.id = ?";

        try {
            pst = con.prepareStatement(query);
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
    public List<PessoaFisica> lista(Integer status, Integer offset, Integer limit) {
        List<PessoaFisica> pessoas = null;

        String query = "" +
                "SELECT p.id,p.id_tipo_pessoa,p.status,p.liberar_adocao," +
                "pf.nome,pf.cpf " +
                "FROM pessoa_fisica pf " +
                "INNER JOIN pessoa p ON (p.id = pf.id_pessoa)";
        if(status != null){
            query += " WHERE p.status = " + status;
        }
        query += " ORDER BY pf.nome ASC ";
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
                PessoaFisica pf = this.carregaObjeto(rs);
                pessoas.add(pf);
            }
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            close();
        }

        return pessoas;
    }
}
