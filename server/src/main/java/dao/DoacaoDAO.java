package dao;

import entidades.Adocao;
import entidades.Doacao;
import entidades.Pessoa;
import entidades.TipoDoacao;
import interfaces.Daos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilidades.Utilidades;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DoacaoDAO extends DAO implements Daos<Doacao> {

    private static final Logger logger = LogManager.getLogger(DoacaoDAO.class.getName());

    @Override
    public Doacao carregaObjeto(ResultSet rs) {
        Doacao doacao = new Doacao();
        try{
            try{
                Integer idPF = rs.getInt("idPF");
                doacao.setPessoaFisica(new PessoaFisicaDAO().busca(idPF));
            }catch (SQLException epf){
                try{
                    Integer idPJ = rs.getInt("idPJ");
                    doacao.setPessoaJuridica(new PessoaJuridicaDAO().busca(idPJ));
                }catch(SQLException epj){ /*Azar é dus guri*/ }
            }

            doacao.setId(rs.getInt("id"));
            doacao.setTipoDoacao(new TipoDoacaoDAO().busca(rs.getInt("id_tipo_doacao")));
            doacao.setValor(rs.getDouble("valor"));
            doacao.setObservacao(rs.getString("observacao"));
            doacao.setData(rs.getString("data"));
            doacao.setStatus(rs.getInt("status"));
            doacao.setJustificativa(rs.getString("justificativa"));
        } catch (SQLException e) {
            logger.catching(e);
        }
        return doacao;
    }

    @Override
    public boolean cadastra(Doacao doacao) {
        String query = "" +
                "INSERT INTO doacao (id_pessoa,id_tipo_doacao,valor,observacao,data,justificativa) " +
                "VALUES " +
                "(?,?,?,?,NOW(),?)";
        try {
            Integer idPessoa = doacao.getPessoaFisica() != null ? doacao.getPessoaFisica().getId() : doacao.getPessoaJuridica().getId();

            pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1,idPessoa);
            pst.setInt(2,doacao.getTipoDoacao().getId());
            pst.setDouble(3,doacao.getValor());
            pst.setString(4, doacao.getObservacao());
            pst.setString(5,doacao.getJustificativa());

            pst.execute();
            rs = pst.getGeneratedKeys();
            if(rs.next()){
                doacao.setId(rs.getInt(1));
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
    public boolean atualiza(Doacao doacao) {
        String query = "" +
                "UPDATE doacao SET " +
                "id_pessoa = ?, " +
                "id_tipo_doacao = ?, " +
                "valor = ?, " +
                "observacao = ?, " +
                "status = ?, " +
                "justificativa = ? " +
                "WHERE id = "+doacao.getId();
        try {
            Integer idPessoa = doacao.getPessoaFisica() != null ? doacao.getPessoaFisica().getId() : doacao.getPessoaJuridica().getId();

            pst = con.prepareStatement(query);
            pst.setInt(1,idPessoa);
            pst.setInt(2,doacao.getTipoDoacao().getId());
            pst.setDouble(3,doacao.getValor());
            pst.setString(4,doacao.getObservacao());
            pst.setInt(5,doacao.getStatus());
            pst.setString(6,doacao.getJustificativa());

            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            logger.catching(e);
            return false;
        } finally {
            close();
        }
    }

    @Override
    public Doacao busca(Integer id) {
        Doacao doacao = null;
        try {
            String query = "" +
                    "SELECT d.*, pf.id_pessoa AS idPF, pj.id_pessoa AS idPJ " +
                    "FROM doacao d " +
                    "LEFT JOIN pessoa_fisica pf ON (pf.id_pessoa = d.id_pessoa) " +
                    "LEFT JOIN pessoa_juridica pj ON (pj.id_pessoa = d.id_pessoa) " +
                    "WHERE d.id = ?";
            pst = con.prepareStatement(query);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                doacao = this.carregaObjeto(rs);
            }
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            close();
        }

        return doacao;
    }

    @Override
    public List<Doacao> lista(Integer status, Integer offset, Integer limit) {
        List<Doacao> doacoes = new ArrayList();

        try {
            String query = "" +
                    "SELECT d.*, pf.id_pessoa AS idPF, pj.id_pessoa AS idPJ " +
                    "FROM doacao d " +
                    "LEFT JOIN pessoa_fisica pf ON (pf.id_pessoa = d.id_pessoa) " +
                    "LEFT JOIN pessoa_juridica pj ON (pj.id_pessoa = d.id_pessoa) ";
            if(status != null){
                query += " WHERE d.status = "+status;
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
                Doacao doacao = this.carregaObjeto(rs);
                doacoes.add(doacao);
            }
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            close();
        }

        return doacoes;
    }
}
