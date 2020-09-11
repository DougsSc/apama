package dao;

import entidades.TipoContato;
import entidades.TipoDoacao;
import interfaces.Daos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TipoDoacaoDAO extends DAO implements Daos<TipoDoacao> {

    private static final Logger logger = LogManager.getLogger(TipoDoacaoDAO.class.getName());

    @Override
    public TipoDoacao carregaObjeto(ResultSet rs) {
        TipoDoacao tipoDoacao = null;
        try {
            tipoDoacao = new TipoDoacao();
            tipoDoacao.setId(rs.getInt("id"));
            tipoDoacao.setDescricao(rs.getString("descricao"));
            tipoDoacao.setUnidadeMedida(rs.getString("unidade_medida"));
            tipoDoacao.setMascara(rs.getString("mascara"));
            tipoDoacao.setExigeValor(rs.getInt("exige_valor"));
        } catch (SQLException e) {
            logger.catching(e);
        }

        return tipoDoacao;
    }

    @Override
    public boolean cadastra(TipoDoacao tipoDoacao) {
        String query = "INSERT INTO tipo_doacao (descricao,unidade_medida,mascara,exige_valor) VALUES (?,?,?,?)";
        try {
            pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, tipoDoacao.getDescricao());
            pst.setString(2, tipoDoacao.getUnidadeMedida());
            pst.setString(3, tipoDoacao.getMascara());
            pst.setInt(4,tipoDoacao.getExigeValor());

            pst.execute();
            rs = pst.getGeneratedKeys();
            if (rs.next()) {
                tipoDoacao.setId(rs.getInt(1));
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
    public boolean atualiza(TipoDoacao tipoDoacao) {
        String query = "" +
                "UPDATE tipo_doacao SET " +
                "descricao = ?, " +
                "unidade_medida = ?, " +
                "mascara = ?, " +
                "exige_valor = ? " +
                "WHERE id = " + tipoDoacao.getId();
        try {
            pst = con.prepareStatement(query);
            pst.setString(1, tipoDoacao.getDescricao());
            pst.setString(2, tipoDoacao.getUnidadeMedida());
            pst.setString(3,tipoDoacao.getMascara());
            pst.setInt(4,tipoDoacao.getExigeValor());

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.catching(e);
            return false;
        } finally {
            close();
        }
    }

    @Override
    public TipoDoacao busca(Integer id) {
        TipoDoacao tipoDoacao = null;

        try {
            pst = con.prepareStatement("SELECT * FROM tipo_doacao WHERE id = ?");
            pst.setInt(1, id);

            rs = pst.executeQuery();
            if (rs.next()) {
                tipoDoacao = carregaObjeto(rs);
            }
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            close();
        }

        return tipoDoacao;
    }

    @Override
    public List<TipoDoacao> lista(Integer status, Integer offset, Integer limit) {
        List<TipoDoacao> tipoDoacoes = new ArrayList<>();

        String query = "SELECT * FROM tipo_doacao";
        if(status != null){ //- Por enquanto nao tem status
            query += " WHERE status = "+status;
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
                TipoDoacao tipoDoacao = this.carregaObjeto(rs);
                tipoDoacoes.add(tipoDoacao);
            }
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            close();
        }

        return tipoDoacoes;
    }
}
