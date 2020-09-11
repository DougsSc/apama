package dao;

import entidades.Arquivo;
import interfaces.Daos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilidades.Utilidades;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ImagemDAO extends DAO implements Daos<Arquivo> {

    private static final Logger logger = LogManager.getLogger(ImagemDAO.class.getName());

    @Override
    public Arquivo carregaObjeto(ResultSet rs) {
        Arquivo arquivo = null;
        try {
            arquivo = new Arquivo();
            arquivo.setId(rs.getInt("id"));
            arquivo.setNome(rs.getString("arquivo"));
            arquivo.setArquivo(Utilidades.getEncodedFile(Utilidades.getVariavel("var.folder") + arquivo.getNome())); //- Nao tem, aqui vem a imagem em base64
        } catch (SQLException e) {
            logger.catching(e);
        }
        return arquivo;
    }

    @Override
    public boolean cadastra(Arquivo arquivo) {
        String query = "" +
                "INSERT INTO imagem (arquivo) " +
                "VALUES " +
                "(?)";
        try {
            pst = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setString(1, arquivo.getNome());

            pst.execute();
            rs = pst.getGeneratedKeys();
            if(rs.next()){
                arquivo.setId(rs.getInt(1));
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
    public boolean atualiza(Arquivo arquivo) {
        String query = "" +
                "UPDATE imagem SET " +
                "arquivo = ? " +
                "WHERE id = "+ arquivo.getId();
        try {
            pst = con.prepareStatement(query);
            pst.setString(1, arquivo.getNome());

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.catching(e);
            return false;
        } finally {
            close();
        }
    }

    @Override
    public Arquivo busca(Integer id) {
        Arquivo arquivo = null;

        try {
            String query = "SELECT * FROM imagem WHERE id = ?";
            pst = con.prepareStatement(query);
            pst.setInt(1, id);

            rs = pst.executeQuery();
            if (rs.next()) {
                arquivo = this.carregaObjeto(rs);
            }
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            close();
        }

        return arquivo;
    }

    @Override
    public List<Arquivo> lista(Integer status, Integer offset, Integer limit) {
        List<Arquivo> arquivos = new ArrayList();
        ResultSet rs = null;

        String query = "SELECT * FROM imagem";
            /*if(status != null){ //- Nao tem status
                query += " WHERE status = "+status;
            }*/
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
                Arquivo arquivo = this.carregaObjeto(rs);
                arquivos.add(arquivo);
            }
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            close();
        }

        return arquivos;
    }

    public boolean cadastraAnimalImagem(Integer idAnimal, Integer idImagem){
        String query = "INSERT INTO animal_imagem (id_animal,id_imagem) VALUES (?,?)";
        try {
            pst = con.prepareStatement(query);
            pst.setInt(1,idAnimal);
            pst.setInt(2,idImagem);
            pst.execute();

            return true;
        } catch (SQLException e) {
            logger.catching(e);
            return false;
        } finally {
            close();
        }
    }

    public List<Arquivo> buscaAnimalImagens(Integer id){
        List<Arquivo> arquivos = new ArrayList();
        ResultSet rs = null;

        String query = "" +
                "SELECT i.* " +
                "FROM animal_imagem ai " +
                "INNER JOIN imagem i ON (ai.id_imagem = i.id) " +
                "WHERE id_animal = ?";
        try {
            pst = con.prepareStatement(query);
            pst.setInt(1,id);

            rs = pst.executeQuery();
            while(rs.next()){
                Arquivo arquivo = this.carregaObjeto(rs);
                arquivos.add(arquivo);
            }
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            close();
        }

        return arquivos;
    }

    public boolean removeAnimalImagens(Integer idAnimal){
        ResultSet rs = null;
        String query = "DELETE FROM animal_imagem WHERE id_animal = ?";
        try {
            pst = con.prepareStatement(query);
            pst.setInt(1,idAnimal);
            pst.execute();
        } catch (SQLException e) {
            logger.catching(e);
            return false;
        } finally {
            close();
        }

        return true;
    }

    public boolean cadastraAdocaoImagem(Integer idAdocao, Integer idImagem){
        String query = "INSERT INTO adocao_imagem (id_adocao,id_imagem) VALUES (?,?)";
        try {
            pst = con.prepareStatement(query);
            pst.setInt(1,idAdocao);
            pst.setInt(2,idImagem);
            pst.execute();

            return true;
        } catch (SQLException e) {
            logger.catching(e);
            return false;
        } finally {
            close();
        }
    }

    public List<Arquivo> buscaAdocaoImagens(Integer id){
        List<Arquivo> arquivos = new ArrayList();
        ResultSet rs = null;

        String query = "" +
                "SELECT i.* " +
                "FROM adocao_imagem ai " +
                "INNER JOIN imagem i ON (ai.id_imagem = i.id) " +
                "WHERE id_adocao = ?";
        try {
            pst = con.prepareStatement(query);
            pst.setInt(1,id);

            rs = pst.executeQuery();
            while(rs.next()){
                Arquivo arquivo = this.carregaObjeto(rs);
                arquivos.add(arquivo);
            }
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            close();
        }

        return arquivos;
    }
}
