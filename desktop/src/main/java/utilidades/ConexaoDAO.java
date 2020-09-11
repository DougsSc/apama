package utilidades;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoDAO {

    private static ConexaoDAO instancia = null;
    private Connection conexao = null;

    private ConexaoDAO() {
        try {
            // Carrega informações do arquivo de propriedades
            String dbdriver = Utilidades.getVariavel("db.driver");
            String dburl = Utilidades.getVariavel("db.url");
            String dbuser = Utilidades.getVariavel("db.user");
            String dbsenha = Utilidades.getVariavel("db.password");

            // Carrega Driver do Banco de Dados
            Class.forName(dbdriver);

            if (dbuser.length() != 0) // conexão COM usuário e senha
            {
                conexao = DriverManager.getConnection(dburl, dbuser, dbsenha);
            } else // conexão SEM usuário e senha
            {
                conexao = DriverManager.getConnection(dburl);
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    // Retorna instância
    public static ConexaoDAO getInstance() {
        if (instancia == null) {
            instancia = new ConexaoDAO();
        }
        return instancia;
    }

    // Retorna conexão
    public Connection getConnection() {
        return conexao;
    }
    
    public void disconnect() {
//        try {
//            conexao.close();
//        } catch (SQLException e) {
//            logger.error("Erro disconnect()");
//            logger.catching(Level.ERROR, e);
//        }
    }

    // Efetua fechamento da conexão
    public void shutDown() {
        try {
            conexao.close();
            instancia = null;
            conexao = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

