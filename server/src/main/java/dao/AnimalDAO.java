package dao;

import com.sun.org.apache.bcel.internal.generic.ANEWARRAY;
import entidades.*;
import interfaces.Daos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilidades.Utilidades;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import java.sql.SQLException;

public class AnimalDAO extends DAO implements Daos<Animal> {

    private static final Logger logger = LogManager.getLogger(AnimalDAO.class.getName());

    @Override
    public Animal carregaObjeto(ResultSet rs) {
        Animal animal = null;
        try {
            animal = new Animal();
            animal.setId(rs.getInt("id"));
            animal.setDataResgate(rs.getString("data_resgate"));
            animal.setDataAdocao(rs.getString("data_adocao"));
            animal.setNome(rs.getString("nome"));
            animal.setObservacao(rs.getString("observacao"));
            animal.setAnimalPorte(this.buscaPorte(rs.getInt("id_animal_porte")));
            animal.setAnimalStatus(this.buscaStatus(rs.getInt("id_animal_status")));
            animal.setArquivos(new ImagemDAO().buscaAnimalImagens(animal.getId()));
            animal.setEndereco(new AnimalDAO().getEndereco(animal.getId()));
        } catch (SQLException e) {
            logger.catching(e);
        }
        return animal;
    }

    public AnimalPorte carregaObjetoPorte(ResultSet rs) {
        AnimalPorte porte = null;
        try {
            porte = new AnimalPorte();
            porte.setId(rs.getInt("id"));
            porte.setDescricao(rs.getString("descricao"));
            porte.setStatus(rs.getInt("status"));
        } catch (SQLException e) {
            logger.catching(e);
        }
        return porte;
    }

    public AnimalStatus carregaObjetoStatus(ResultSet rs) {
        AnimalStatus status = null;
        try {
            status = new AnimalStatus();
            status.setId(rs.getInt("id"));
            status.setDescricao(rs.getString("descricao"));
            status.setStatus(rs.getInt("status"));
        } catch (SQLException e) {
            logger.catching(e);
        }
        return status;
    }

    @Override
    public boolean cadastra(Animal animal) {
        String query = "" +
                "INSERT INTO animal " +
                "(id_animal_porte,id_animal_status,data_resgate,data_adocao,nome,observacao) " +
                "VALUES " +
                "(?,?,?,?,?,?)";
        try {
            java.util.Date dataResgate = new java.util.Date();
            if (animal.getDataResgate() != null) {
                dataResgate = new SimpleDateFormat("yyyy-MM-dd").parse(animal.getDataResgate());
            }

            java.util.Date dataAdocao = null;
            if (animal.getDataAdocao() != null) {
                dataAdocao = new SimpleDateFormat("yyyy-MM-dd").parse(animal.getDataAdocao());
            }

//            Date dataResgate = animal.getDataResgate().length() != 10 ? null : (Date) (new SimpleDateFormat("yyyy-MM-dd").parse(animal.getDataResgate())).getTime();
//            Date dataAdocao = animal.getDataAdocao().length() != 10 ? null : (Date) new SimpleDateFormat("yyyy-MM-dd").parse(animal.getDataAdocao());

            pst = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setInt(1, animal.getAnimalPorte().getId());
            pst.setInt(2, animal.getAnimalStatus().getId());
            pst.setDate(3, new java.sql.Date(dataResgate.getTime()));
            if (dataAdocao != null) pst.setDate(4, new java.sql.Date(dataAdocao.getTime()));
            else pst.setDate(4, null);
            pst.setString(5, animal.getNome());
            pst.setString(6, animal.getObservacao());

            pst.execute();
            rs = pst.getGeneratedKeys();
            if (rs.next()) {
                animal.setId(rs.getInt(1));
                return true;
            } else {
                return false;
            }
        } catch (SQLException | ParseException e) {
            logger.catching(e);
            return false;
        } finally {
            close();
        }
    }

    @Override
    public boolean atualiza(Animal animal) {
        String query = "" +
                "UPDATE animal SET " +
                "id_animal_porte = ?, " +
                "id_animal_status = ?, " +
                "data_resgate = ?, " +
                "data_adocao = ?, " +
                "nome = ?, " +
                "observacao = ? " +
                "WHERE id = " + animal.getId();
        try {
            Date dataResgate = Utilidades.converteString2Date(animal.getDataResgate());
            Date dataAdocao = Utilidades.converteString2Date(animal.getDataAdocao());

            pst = con.prepareStatement(query);
            pst.setInt(1, animal.getAnimalPorte().getId());
            pst.setInt(2, animal.getAnimalStatus().getId());
            pst.setDate(3, dataResgate);
            pst.setDate(4, dataAdocao);
            pst.setString(5, animal.getNome());
            pst.setString(6, animal.getObservacao());

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
    public Animal busca(Integer id) {
        Animal animal = null;

        try {
            String query = "SELECT * FROM animal WHERE id = ?";
            pst = con.prepareStatement(query);
            pst.setInt(1, id);

            rs = pst.executeQuery();
            if (rs.next()) {
                animal = this.carregaObjeto(rs);
            }
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            close();
        }

        return animal;
    }

    @Override
    public List<Animal> lista(Integer status, Integer offset, Integer limit) {
        List<Animal> animais = new ArrayList();
        ResultSet rs = null;

        String query = "SELECT * FROM animal WHERE id_animal_status ";
        query += status != null ? " = "+status : " != 4"; //- 4 = Obito
        query += " ORDER BY nome ASC ";
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
                Animal animal = this.carregaObjeto(rs);
                animais.add(animal);
            }
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            close();
        }

        return animais;
    }

    public Endereco getEndereco(int idAnimal) {
        Endereco endereco = null;

        String query = "" +
                "SELECT e.* " +
                "FROM animal_endereco ae " +
                "INNER JOIN endereco e ON (e.id = ae.id_endereco) " +
                "WHERE id_animal = " + idAnimal + " " +
                "AND data_inativacao IS NULL";
        try {
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                endereco = new Endereco();
                endereco.setId(rs.getInt("id"));
                endereco.setCidade(new CidadeDAO().busca(rs.getInt("id_cidade")));
                endereco.setLogradouro(rs.getString("logradouro"));
                endereco.setNumero(rs.getString("numero"));
                endereco.setComplemento(rs.getString("complemento"));
                endereco.setBairro(rs.getString("bairro"));
                endereco.setCep(rs.getString("cep"));
            }
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            close();
        }

        return endereco;
    }

    private AnimalPorte buscaPorte(Integer id){
        AnimalPorte porte = null;

        try {
            String query = "SELECT * FROM animal_porte WHERE id = ?";
            pst = con.prepareStatement(query);
            pst.setInt(1, id);

            rs = pst.executeQuery();
            if (rs.next()) {
                porte = this.carregaObjetoPorte(rs);
            }

        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            close();
        }

        return porte;
    }

    private AnimalStatus buscaStatus(Integer id){
        AnimalStatus status = null;

        try {
            String query = "SELECT * FROM animal_status WHERE id = ?";
            pst = con.prepareStatement(query);
            pst.setInt(1, id);

            rs = pst.executeQuery();
            if (rs.next()) {
                status = this.carregaObjetoStatus(rs);
            }

        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            close();
        }

        return status;
    }

    public List<Animal> listaDisponivelAdocao(Integer offset, Integer limit) {
        List<Animal> animais = new ArrayList();
        ResultSet rs = null;

        String query = "" +
                "SELECT * " +
                "FROM animal " +
                "WHERE " +
                "id_animal_status IN (1,2) " +
                "ORDER BY nome ASC ";
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
                Animal animal = this.carregaObjeto(rs);
                animais.add(animal);
            }
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            close();
        }

        return animais;
    }
}