package main;

import com.sun.net.httpserver.HttpServer;
import dao.EnderecoDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import servicos.*;
import utilidades.Utilidades;

import java.io.IOException;
import java.net.*;

public class Server implements Runnable {

    private static final Logger logger = LogManager.getLogger(Server.class.getName());

    private static String HOSTNAME;
    private static int PORT;
    private static final int BACKLOG = 1;

    public Server() {
        HOSTNAME = Utilidades.getVariavel("con.ip");
        PORT = Integer.parseInt(Utilidades.getVariavel("con.port"));
    }

    public void run() {
        startServer();
    }

    private void startServer() {
        HttpServer server;
        try {
            server = HttpServer.create(new InetSocketAddress(HOSTNAME, PORT), BACKLOG);

            server.createContext("/Apama/login",            new RestLogin());

            // Cruds
            server.createContext("/Apama/usuario",          new RestUsuario());
            server.createContext("/Apama/tutorDoador",      new RestTutorDoador());
            server.createContext("/Apama/pessoaFisica",     new RestPessoaFisica());
            server.createContext("/Apama/pessoaJuridica",   new RestPessoaJuridica());
            server.createContext("/Apama/animal",           new RestAnimal());
            server.createContext("/Apama/cidade",           new RestCidade());
            server.createContext("/Apama/imagem",           new RestImagem());

            // Operacional
            server.createContext("/Apama/doacao",           new RestDoacao());
            server.createContext("/Apama/tipoDoacao",       new RestTipoDoacao());
            server.createContext("/Apama/adocao",           new RestAdocao());
            server.createContext("/Apama/tratamento",       new RestAnimalTratamento());


            server.start();

        } catch (Exception e) {
            logger.catching(e);
        }
    }
}
