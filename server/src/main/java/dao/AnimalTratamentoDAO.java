package dao;

import entidades.AnimalTratamento;
import entidades.TipoTratamento;
import interfaces.Daos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilidades.Utilidades;

import javax.management.QueryEval;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AnimalTratamentoDAO extends DAO implements Daos<AnimalTratamento> {

    private static final Logger logger = LogManager.getLogger(AnimalTratamentoDAO.class.getName());

    @Override
    public AnimalTratamento carregaObjeto(ResultSet rs) {
        AnimalTratamento tratamento = null;
        try {
            tratamento = new AnimalTratamento();
            tratamento.setId(rs.getInt("id"));
            tratamento.setTipoTratamento(new TipoTratamentoDAO().busca(rs.getInt("id_tipo_tratamento")));
            tratamento.setAnimal(new AnimalDAO().busca(rs.getInt("id_animal")));
            tratamento.setObservacao(rs.getString("observacao"));
            tratamento.setDataTratamento(rs.getString("data_tratamento"));
            tratamento.setProximaDataTratamento(rs.getString("proxima_data_tratamento"));
            tratamento.setStatus(rs.getInt("status"));
        } catch (SQLException e) {
            logger.catching(e);
        }
        return tratamento;
    }

    @Override
    public boolean cadastra(AnimalTratamento animalTratamento) {
        String query = "" +
                "INSERT INTO animal_tratamento " +
                "(id_tipo_tratamento,id_animal,observacao,data_tratamento,proxima_data_tratamento) " +
                "VALUES " +
                "(?,?,?,?,?)";
        try {
            Date dataTratamento = Utilidades.converteString2Date(animalTratamento.getDataTratamento());
            Date proximaData = Utilidades.converteString2Date(animalTratamento.getProximaDataTratamento());

            pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1,animalTratamento.getTipoTratamento().getId());
            pst.setInt(2,animalTratamento.getAnimal().getId());
            pst.setString(3,animalTratamento.getObservacao());
            pst.setDate(4, dataTratamento);
            pst.setDate(5, proximaData);

            pst.execute();
            rs = pst.getGeneratedKeys();
            if(rs.next()){
                animalTratamento.setId(rs.getInt(1));
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
    public boolean atualiza(AnimalTratamento tratamento) {
        String query = "" +
                "UPDATE animal_tratamento SET " +
                "id_tipo_tratamento = ?, " +
                "id_animal = ?, " +
                "observacao = ?, " +
                "data_tratamento = ?, " +
                "proxima_data_tratamento = ?," +
                "status = ? " +
                "WHERE id = "+tratamento.getId();
        try {
            Date dataTratamento = Utilidades.converteString2Date(tratamento.getDataTratamento());
            Date proximaData = Utilidades.converteString2Date(tratamento.getProximaDataTratamento());

            pst = con.prepareStatement(query);
            pst.setInt(1,tratamento.getTipoTratamento().getId());
            pst.setInt(2,tratamento.getAnimal().getId());
            pst.setString(3,tratamento.getObservacao());
            pst.setDate(4,dataTratamento);
            pst.setDate(5,proximaData);
            pst.setInt(6,tratamento.getStatus());

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
    public AnimalTratamento busca(Integer id) {
        AnimalTratamento animalTratamento = null;

        try {
            pst = con.prepareStatement("SELECT * FROM animal_tratamento WHERE id = ?");
            pst.setInt(1, id);

            rs = pst.executeQuery();
            if (rs.next()) {
                animalTratamento = this.carregaObjeto(rs);
            }
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            close();
        }

        return animalTratamento;
    }

    @Override
    public List<AnimalTratamento> lista(Integer status, Integer offset, Integer limit) {
        List<AnimalTratamento> animalTratamentos = new ArrayList();

        try {
            String query = "SELECT * FROM animal_tratamento ";
            if (status != null) {
                query += " WHERE status = " + status;
            }
            query += " ORDER BY data_tratamento DESC ";
            if(offset != null){
                query += " OFFSET "+offset;
            }
            if(limit != null){
                query += " LIMIT "+limit;
            }

            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            while(rs.next()){
                AnimalTratamento animalTratamento = this.carregaObjeto(rs);
                animalTratamentos.add(animalTratamento);
            }
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            close();
        }

        return animalTratamentos;
    }

    public List<AnimalTratamento> listaTratamentosAnimal(Integer idAnimal,Integer status) {
        List<AnimalTratamento> animalTratamentos = new ArrayList();

        try {
            String query = "SELECT * FROM animal_tratamento WHERE 1 = 1 ";
            if(idAnimal != null){
                query += " AND id_animal = "+idAnimal;
            }
            if (status != null) {
                query += " AND status = " + status;
            }
            query += " ORDER BY data_tratamento DESC";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            while(rs.next()){
                AnimalTratamento animalTratamento = this.carregaObjeto(rs);
                animalTratamentos.add(animalTratamento);
            }
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            close();
        }

        return animalTratamentos;
    }
}
