package dao;

import entidades.Contato;
import entidades.Pessoa;
import entidades.TipoContato;
import interfaces.Daos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class TipoContatoDAO extends DAO implements Daos<TipoContato> {

    private static final Logger logger = LogManager.getLogger(TipoContatoDAO.class.getName());

    @Override
    public TipoContato carregaObjeto(ResultSet rs) {
        TipoContato tipoContato = null;
        try {
            tipoContato = new TipoContato();
            tipoContato.setId(rs.getInt("id"));
            tipoContato.setDescricao(rs.getString("descricao"));
            tipoContato.setStatus(rs.getInt("status"));
        } catch (SQLException e) {
            logger.catching(e);
        }

        return tipoContato;
    }

    @Override
    public boolean cadastra(TipoContato tipoContato) {
        String query = "INSERT INTO contato (descricao) VALUES (?)";
        try {
            pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, tipoContato.getDescricao());

            pst.execute();
            rs = pst.getGeneratedKeys();
            if (rs.next()) {
                tipoContato.setId(rs.getInt(1));
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
    public boolean atualiza(TipoContato tipoContato) {
        String query = "" +
                "UPDATE tipo_contato SET " +
                "descricao = ?, " +
                "status = ? " +
                "WHERE id = " + tipoContato.getId();
        try {
            pst = con.prepareStatement(query);
            pst.setString(1, tipoContato.getDescricao());
            pst.setInt(2, tipoContato.getStatus());

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.catching(e);
            return false;
        } finally {
            close();
        }
    }

    @Override
    public TipoContato busca(Integer id) {
        TipoContato tipoContato = null;

        try {
            pst = con.prepareStatement("SELECT * FROM tipo_contato WHERE id = ?");
            pst.setInt(1, id);

            rs = pst.executeQuery();
            if (rs.next()) {
                tipoContato = carregaObjeto(rs);
            }
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            close();
        }

        return tipoContato;
    }

    @Override
    public List<TipoContato> lista(Integer status, Integer offset, Integer limit) {
        List<TipoContato> tipoContatos = null;

        String query = "SELECT * FROM tipo_contato";
        if (status != null) {
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
            while (rs.next()) {
                TipoContato tipoContato = this.carregaObjeto(rs);
                tipoContatos.add(tipoContato);
            }
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            close();
        }

        return tipoContatos;
    }
}
