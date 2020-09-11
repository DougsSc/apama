package dao;

import entidades.*;
import interfaces.Daos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilidades.Utilidades;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AdocaoDAO extends DAO implements Daos<Adocao> {

    private static final Logger logger = LogManager.getLogger(AdocaoDAO.class.getName());

    @Override
    public Adocao carregaObjeto(ResultSet rs) {
        Adocao adocao = new Adocao();
        try{
            adocao.setId(rs.getInt("id"));
            adocao.setAnimal(new AnimalDAO().busca(rs.getInt("id_animal")));
            adocao.setTutor(new PessoaFisicaDAO().busca(rs.getInt("id_pessoa_tutor")));
            adocao.setUsuario(new UsuarioDAO().busca(rs.getInt("id_pessoa_usuario")));
            adocao.setStatus(new AdocaoStatusDAO().busca(rs.getInt("id_adocao_status")));
            adocao.setDataRegistro(rs.getString("data_registro"));
            adocao.setDataAdocao(rs.getString("data_adocao"));
            adocao.setObservacao(rs.getString("observacao"));
            adocao.setArquivos(new ImagemDAO().buscaAdocaoImagens(adocao.getId()));
        } catch (SQLException e) {
            logger.catching(e);
        }
        return adocao;
    }

    @Override
    public boolean cadastra(Adocao adocao) {
        String query = "" +
                "INSERT INTO adocao " +
                "(id_animal,id_pessoa_tutor,id_pessoa_usuario,id_adocao_status,data_registro,data_adocao,observacao) " +
                "VALUES " +
                "(?,?,?,?,NOW(),?,?)";
        try {
            pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1,adocao.getAnimal().getId());
            pst.setInt(2,adocao.getTutor().getId());
            pst.setInt(3,adocao.getUsuario().getId());
            pst.setInt(4,adocao.getStatus().getId());
            pst.setDate(5,Utilidades.converteString2Date(adocao.getDataAdocao()));
            pst.setString(6, adocao.getObservacao());

            pst.execute();
            rs = pst.getGeneratedKeys();
            if(rs.next()){
                adocao.setId(rs.getInt(1));
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
    public boolean atualiza(Adocao adocao) {
        String query = "" +
                "UPDATE adocao SET " +
                "id_animal = ?, " +
                "id_pessoa_tutor = ?, " +
                "id_pessoa_usuario = ?, " +
                "id_adocao_status = ?, " +
                "data_adocao = ?, " +
                "observacao = ? " +
                "WHERE id = "+adocao.getId();
        try {
            pst = con.prepareStatement(query);
            pst.setInt(1,adocao.getAnimal().getId());
            pst.setInt(2,adocao.getTutor().getId());
            pst.setInt(3,adocao.getUsuario().getId());
            pst.setInt(4,adocao.getStatus().getId());
            pst.setDate(5,Utilidades.converteString2Date(adocao.getDataAdocao()));
            pst.setString(6,adocao.getObservacao());

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
    public Adocao busca(Integer id) {
        Adocao adocao = null;

        try {
            pst = con.prepareStatement("SELECT * FROM adocao WHERE id = ?");
            pst.setInt(1, id);

            rs = pst.executeQuery();
            if (rs.next()) {
                adocao = this.carregaObjeto(rs);
            }
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            close();
        }

        return adocao;
    }

    @Override
    public List<Adocao> lista(Integer status, Integer offset, Integer limit) {
        List<Adocao> adocoes = new ArrayList();

        try {
            String query = "SELECT * FROM adocao";
            if (status != null) {
                query += " WHERE id_adocao_status = " + status;
            }
            query += " ORDER BY data_adocao DESC ";
            if(offset != null){
                query += " OFFSET "+offset;
            }
            if(limit != null){
                query += " LIMIT "+limit;
            }

            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            while(rs.next()){
                Adocao adocao = this.carregaObjeto(rs);
                adocoes.add(adocao);
            }
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            close();
        }

        return adocoes;
    }
}
