package dao;

import entidades.*;
import interfaces.Daos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilidades.Utilidades;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsuarioDAO extends DAO implements Daos<Usuario> {

    private static final Logger logger = LogManager.getLogger(UsuarioDAO.class.getName());

    public Usuario carregaObjeto(ResultSet rs){
        Usuario usuario;
        try{
            usuario = new Usuario();
            usuario.setId(rs.getInt("id"));
            usuario.setIdTipoUsuario(rs.getInt("id_tipo_usuario"));
            usuario.setLogin(rs.getString("login"));
            usuario.setStatusUsuario(rs.getInt("statusUser"));
            usuario.setNome(rs.getString("nome"));
            usuario.setCpf(rs.getString("cpf"));
            usuario.setIdTipoPessoa(rs.getInt("id_tipo_pessoa"));
            usuario.setStatus(rs.getInt("status"));
            usuario.setLiberarAdocao(rs.getInt("liberar_adocao"));
            usuario.setEndereco(new PessoaDAO().getEndereco(usuario.getId()));
            usuario.setContatos(new PessoaDAO().getContatos(usuario.getId()));
            usuario.setPermissoes(getPermissoesByUsuario(null, usuario.getId()));
        } catch (SQLException e) {
            logger.catching(e);
            usuario = null;
        }
        return usuario;
    }

    @Override
    public boolean cadastra(Usuario usuario) {
        //- Primeiro tenta cadastrar os dados da pessoa
        if(!new PessoaFisicaDAO().cadastra(usuario)){
            return false;
        }

        //- Cadastro da pessoa fisica realizado com sucesso, agora registra os dados de usuario
        String query = "INSERT INTO usuario (id_pessoa,login,senha,id_tipo_usuario) VALUES (?,?,?,?)";
        try {
            pst = con.prepareStatement(query);
            pst.setInt(1,usuario.getId());
            pst.setString(2,usuario.getLogin());
            pst.setString(3,usuario.getSenha());
            pst.setInt(4,usuario.getIdTipoUsuario());

            pst.execute();

            usuario.setSenha(""); //- Por segurança, remove a senha do objeto do usuário
            return true;
        } catch (SQLException e) {
            logger.catching(e);
            return false;
        } finally {
            close();
        }
    }

    @Override
    public boolean atualiza(Usuario usuario) {
        //- Primeiro tenta atualizar os dados da pessoa
        if(!new PessoaFisicaDAO().atualiza(usuario)){
            return false;
        }

        //- Atualizacao da pessoa realizado com sucesso, agora atualiza os dados de usuario
        String query = "" +
                "UPDATE usuario SET " +
                "login = ?, " +
                "id_tipo_usuario = ?," +
                "status = ? ";
        if(usuario.getSenha() != null && !usuario.getSenha().isEmpty()){
            query += ", senha = ? ";
        }
        query += "WHERE id_pessoa = "+usuario.getId();
        try {
            pst = con.prepareStatement(query);
            pst.setString(1,usuario.getLogin());
            pst.setInt(2,usuario.getIdTipoUsuario());
            pst.setInt(3,usuario.getStatusUsuario());

            if(usuario.getSenha() != null && !usuario.getSenha().isEmpty()){
                pst.setString(4,usuario.getSenha());
            }

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.catching(e);
            return false;
        } finally {
            close();
        }
    }

    @Override
    public Usuario busca(Integer id) {
        Usuario usuario = null;

        try {
            String query = "" +
                    "SELECT u.id_tipo_usuario, u.login, u.status AS statusUser, " +
                    "pf.nome, pf.cpf, " +
                    "p.id, p.id_tipo_pessoa, p.status, p.liberar_adocao " +
                    "FROM usuario u " +
                    "INNER JOIN pessoa_fisica pf ON (pf.id_pessoa = u.id_pessoa) " +
                    "INNER JOIN pessoa p ON (p.id = pf.id_pessoa) " +
                    "WHERE u.id_pessoa = ?";

            pst = con.prepareStatement(query);
            pst.setInt(1, id);

            rs = pst.executeQuery();
            if (rs.next()) {
                usuario = this.carregaObjeto(rs);
            }
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            close();
        }

        return usuario;
    }

    @Override
    public List<Usuario> lista(Integer status, Integer offset, Integer limit) {
        List<Usuario> usuarios = new ArrayList<>();

        ResultSet rs = null;
        String query = "" +
                "SELECT u.status AS statusUser, *  " +
                "FROM usuario u " +
                "INNER JOIN pessoa_fisica pf ON (pf.id_pessoa = u.id_pessoa) " +
                "INNER JOIN pessoa p ON (p.id = pf.id_pessoa)";
        if(status != null){
            query += " WHERE u.status = " + status;
        }
        query += " ORDER BY pf.nome ASC ";
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
                Usuario usuario = this.carregaObjeto(rs);
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { /* ignored */ }
            close();
        }
        return usuarios;
    }

    public List<Usuario> buscaCustom(HashMap<String,String> args) {
        List<Usuario> usuarios = new ArrayList<>();

        ResultSet rs = null;
        String query = "" +
                "SELECT u.status AS statusUser, *  " +
                "FROM usuario u " +
                "INNER JOIN pessoa_fisica pf ON (pf.id_pessoa = u.id_pessoa) " +
                "INNER JOIN pessoa p ON (p.id = pf.id_pessoa) " +
                "WHERE 1 = 1 ";
        if(args.containsKey("nome")){
            query += " AND pf.nome ILIKE ? ";
        }

        try {
            int index = 0;
            pst = con.prepareStatement(query);

            if(args.containsKey("nome")){
                pst.setString(++index,"%" + args.get("nome") + "%");
            }

            rs = pst.executeQuery();
            while(rs.next()){
                Usuario usuario = this.carregaObjeto(rs);
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { /* ignored */ }
            close();
        }
        return usuarios;
    }

    public Usuario login(String login, String senha){
        Usuario usuario = null;
        senha = Utilidades.criptografaSenha(senha);

        String query = "SELECT id_pessoa FROM usuario WHERE login = ? AND senha = ? AND status = 1";
        try {
            pst = con.prepareStatement(query);
            pst.setString(1, login);
            pst.setString(2, senha.toUpperCase());

            rs = pst.executeQuery();
            if (rs.next())
                usuario = busca(rs.getInt("id_pessoa"));
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            close();
        }

        return usuario;
    }

    public boolean salvarToken(Integer idUsuario, String token) {
        String query = "INSERT INTO token (id_pessoa,token,data_validade) VALUES (?,?,NOW())";
        try {
            pst = con.prepareStatement(query);
            pst.setInt(1, idUsuario);
            pst.setString(2, token);

            pst.execute();
            return true;
        } catch (SQLException e) {
            logger.catching(e);
            return false;
        } finally {
            close();
        }
    }

    public List<Permissao> getPermissoesByUsuario(String token, Integer id) {
        List<Permissao> permissoes = new ArrayList<>();

        try {
            String query = "" +
                    "SELECT tp.* " +
                    "FROM usuario us " +
                    "INNER JOIN tipo_usuario tu ON (us.id_tipo_usuario = tu.id) " +
                    "INNER JOIN tipo_usuario_permissao tup ON (tu.id = tup.id_tipo_usuario) " +
                    "INNER JOIN tipo_permissao tp ON (tp.id = tup.id_tipo_permissao) ";

            if (token != null) {
                query += "INNER JOIN token tk ON (us.id_pessoa = tk.id_pessoa) " +
                         "WHERE tk.token = ?";
                pst = con.prepareStatement(query);
                pst.setString(1, token);
            } else if (id != null) {
                query += "WHERE us.id_pessoa = ?";
                pst = con.prepareStatement(query);
                pst.setInt(1, id);
            }

            rs = pst.executeQuery(); //- Tem erro aqui
            while (rs.next()) {
                Permissao permissao = new Permissao();
                permissao.setPermissao(rs.getString("descricao"));

                permissoes.add(permissao);
            }
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            close();
        }

        return permissoes;
    }

    public boolean isLoginDisponivel(String login, Integer ignorar){
        String query = "SELECT * FROM usuario WHERE login = ?";
        if(ignorar != null && ignorar > 0){
            query += " AND id_pessoa != ?";
        }

        try {
            pst = con.prepareStatement(query);
            pst.setString(1, login);
            if(ignorar != null && ignorar > 0){
                pst.setInt(2,ignorar);
            }

            rs = pst.executeQuery();
            if (!rs.next()){
                return true;
            }

        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            close();
        }

        return false;
    }

//    public List<Permissao> getPermissoesByTokenUsuario(String token) {
//        List<Permissao> permissoes = new ArrayList<>();
//
//        try {
//            String query = "" +
//                    "SELECT tp.* " +
//                    "FROM usuario us " +
//                    "INNER JOIN token tk ON (us.id = tk.usuario_id) " +
//                    "INNER JOIN tipo_usuario tu ON (us.id_tipo_usuario = tu.id) " +
//                    "INNER JOIN tipo_usuario_permissao tup ON (tu.id = tup.id_tipo_usuario) " +
//                    "INNER JOIN tipo_permissao tp ON (tp.id = tup.id_tipo_permissao) " +
//                    "WHERE tk.token = ?";
//
//            pst = con.prepareStatement(query);
//            pst.setString(1, token);
//
//            rs = pst.executeQuery();
//            while (rs.next()) {
//               Permissao permissao = new Permissao();
//               permissao.setPermissao(rs.getString("permissao"));
//
//               permissoes.add(permissao);
//            }
//        } catch (SQLException e) {
//            logger.catching(e);
//        } finally {
//            close();
//        }
//
//        return permissoes;
//    }
}