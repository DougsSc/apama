package dao;

import entidades.TipoTratamento;
import interfaces.Daos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class TipoTratamentoDAO extends DAO implements Daos<TipoTratamento> {

    private static final Logger logger = LogManager.getLogger(TipoTratamentoDAO.class.getName());

    @Override
    public TipoTratamento carregaObjeto(ResultSet rs) {
        TipoTratamento tipoTratamento = new TipoTratamento();
        try {
            tipoTratamento = new TipoTratamento();
            tipoTratamento.setId(rs.getInt("id"));
            tipoTratamento.setDescricao(rs.getString("descricao"));
            tipoTratamento.setStatus(rs.getInt("status"));
        } catch (SQLException e) {
            logger.catching(e);
        }

        return tipoTratamento;
    }

    @Override
    public boolean cadastra(TipoTratamento tipoTratamento) {
        String query = "INSERT INTO tipo_tratamento (descricao) VALUES (?)";
        try {
            pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, tipoTratamento.getDescricao());

            pst.execute();
            rs = pst.getGeneratedKeys();
            if (rs.next()) {
                tipoTratamento.setId(rs.getInt(1));
                return true;
            } else {
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
    public boolean atualiza(TipoTratamento tipoTratamento) {
        String query = "" +
                "UPDATE tipo_tratamento SET " +
                "descricao = ?, " +
                "status = ? " +
                "WHERE id = " + tipoTratamento.getId();
        try {
            pst = con.prepareStatement(query);
            pst.setString(1, tipoTratamento.getDescricao());
            pst.setInt(2, tipoTratamento.getStatus());

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.catching(e);
            return false;
        } finally {
            close();
        }
    }

    @Override
    public TipoTratamento busca(Integer id) {
        TipoTratamento tipoTratamento = null;

        try {
            pst = con.prepareStatement("SELECT * FROM tipo_tratamento WHERE id = ?");
            pst.setInt(1, id);

            rs = pst.executeQuery();
            if (rs.next()) {
                tipoTratamento = carregaObjeto(rs);
            }
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            close();
        }

        return tipoTratamento;
    }

    @Override
    public List<TipoTratamento> lista(Integer status, Integer offset, Integer limit) {
        List<TipoTratamento> tipoTratamentos = null;

        String query = "SELECT * FROM tipo_tratamento";
        if (status != null) {
            query += " WHERE status = " + status;
        }
        query += " ORDER BY descricao ASC ";
        if(offset != null){
            query += " OFFSET "+offset;
        }
        if(limit != null){
            query += " LIMIT "+limit;
        }

        try {
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                TipoTratamento tipoTratamento = this.carregaObjeto(rs);
                tipoTratamentos.add(tipoTratamento);
            }
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            close();
        }

        return tipoTratamentos;
    }
}
