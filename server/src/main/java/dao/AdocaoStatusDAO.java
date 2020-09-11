package dao;

import entidades.Adocao;
import entidades.AdocaoStatus;
import entidades.Animal;
import entidades.AnimalStatus;
import interfaces.Daos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilidades.Utilidades;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AdocaoStatusDAO extends DAO implements Daos<AdocaoStatus> {

    private static final Logger logger = LogManager.getLogger(AdocaoStatusDAO.class.getName());

    @Override
    public AdocaoStatus carregaObjeto(ResultSet rs) {
        AdocaoStatus status = new AdocaoStatus();
        try{
            status.setId(rs.getInt("id"));
            status.setDescricao(rs.getString("descricao"));
            status.setStatus(rs.getInt("status"));
        } catch (SQLException e) {
            logger.catching(e);
        }
        return status;
    }

    @Override
    public boolean cadastra(AdocaoStatus status) {
        String query = "INSERT INTO adocao_status (descricao) VALUES (?)";
        try {
            pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1,status.getDescricao());

            pst.execute();
            rs = pst.getGeneratedKeys();
            if(rs.next()){
                status.setId(rs.getInt(1));
                status.setStatus(1);
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
    public boolean atualiza(AdocaoStatus status) {
        String query = "" +
                "UPDATE adocao_status SET " +
                "descricao = ?, " +
                "status = ? " +
                "WHERE id = "+status.getId();
        try {
            pst = con.prepareStatement(query);
            pst.setString(1,status.getDescricao());
            pst.setInt(2,status.getStatus());

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
    public AdocaoStatus busca(Integer id) {
        AdocaoStatus status = null;

        try {
            pst = con.prepareStatement("SELECT * FROM adocao_status WHERE id = ?");
            pst.setInt(1, id);

            rs = pst.executeQuery();
            if (rs.next()) {
                status = this.carregaObjeto(rs);
            }
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            close();
        }

        return status;
    }

    @Override
    public List<AdocaoStatus> lista(Integer status, Integer offset, Integer limit) {
        List<AdocaoStatus> dados = new ArrayList();

        try {
            String query = "SELECT * FROM adocao_status";
            if (status != null) {
                query += " WHERE status = " + status;
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
                AdocaoStatus dado = this.carregaObjeto(rs);
                dados.add(dado);
            }
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            close();
        }

        return dados;
    }
}
