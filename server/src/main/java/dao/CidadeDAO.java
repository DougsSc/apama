package dao;

import entidades.Cidade;
import interfaces.Daos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CidadeDAO extends DAO implements Daos<Cidade> {

    private static final Logger logger = LogManager.getLogger(UsuarioDAO.class.getName());

    @Override
    public Cidade carregaObjeto(ResultSet rs) {
        return null;
    }

    @Override
    public boolean cadastra(Cidade cidade) {
        String query = "INSERT INTO cidade (id,descricao,uf,estado) VALUES (?,?,?,?)";
        try {
            pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1,cidade.getId());
            pst.setString(2,cidade.getDescricao());
            pst.setString(3,cidade.getUf());
            pst.setString(4,cidade.getEstado());

            pst.execute();
            rs = pst.getGeneratedKeys();
            if(rs.next()){
                cidade.setId(rs.getInt(1));
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
    public boolean atualiza(Cidade cidade) {
        String query = "" +
                "UPDATE cidade SET " +
                "descricao = ?, " +
                "uf = ?, " +
                "estado = ? " +
                "WHERE id = "+cidade.getId();
        try {
            pst = con.prepareStatement(query);
            pst.setInt(1,cidade.getId());
            pst.setString(2,cidade.getDescricao());
            pst.setString(3,cidade.getUf());
            pst.setString(4,cidade.getEstado());

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.catching(e);
            return false;
        } finally {
            close();
        }
    }

    @Override
    public Cidade busca(Integer id) {
        Cidade cidade = null;

        try {
            pst = con.prepareStatement("SELECT * FROM cidade WHERE id = ?");
            pst.setInt(1, id);

            rs = pst.executeQuery();
            if (rs.next()) {

                cidade = new Cidade();
                cidade.setId(rs.getInt("id"));
                cidade.setDescricao(rs.getString("descricao"));
                cidade.setUf(rs.getString("uf"));
                cidade.setEstado(rs.getString("estado"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return cidade;
    }

    @Override
    public List<Cidade> lista(Integer status, Integer offset, Integer limit) {
        List<Cidade> cidades = new ArrayList();

        try {
            String query = "SELECT * FROM cidade";
            if(status != null){
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

                Cidade cidade = new Cidade();
                cidade.setId(rs.getInt("id"));
                cidade.setDescricao(rs.getString("descricao"));
                cidade.setUf(rs.getString("uf"));
                cidade.setEstado(rs.getString("estado"));

                cidades.add(cidade);
            }
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            close();
        }

        return cidades;
    }
}
