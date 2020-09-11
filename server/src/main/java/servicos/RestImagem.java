package servicos;

import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import entidades.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilidades.Network;
import utilidades.Utilidades;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class RestImagem extends Network implements HttpHandler {

    private static final Logger logger = LogManager.getLogger(RestAnimal.class.getName());
    private String codigoErro;

    @Override
    public void handle(HttpExchange he) throws IOException {
        if (!loadData(he)) {
            return;
        }

        Arquivo arquivo = null;

        switch (he.getRequestMethod().toUpperCase()) {
            case METHOD_POST:
                // CONVERT json
                try {
                    arquivo = gson.fromJson(body, Arquivo.class);
                } catch (JsonParseException e) {
                    he.sendResponseHeaders(STATUS_ERROR, NO_RESPONSE_LENGTH);
                    logger.catching(Level.ERROR, e);
                }

                if (arquivo != null) {
                    String nome = UUID.randomUUID().toString() + ".jpeg";
//                    System.out.println("Nome > " + nome);
                    File file = Utilidades.getDecodedFile(arquivo.getArquivo(), nome);

                    if (file != null) {
//                        System.out.println(file.getAbsolutePath());

                        arquivo.setId(1);
                        arquivo.setNome("Apama.jpg");
                        sendMessange(he, gson.toJson(arquivo), STATUS_OK);
//                        he.sendResponseHeaders(STATUS_OK, NO_RESPONSE_LENGTH);
                    }

//                    if (dao.cadastra(animal)) {
//                        // RESPONSE CREATED
//                        logger.info("Usuario " + animal.getId() + " recebido!");
//                        sendMessange(he, gson.toJson(animal), STATUS_ERROR);
//                    } else {
//                        Erro erro = new Erro(codigoErro, "Erro ao cadastrar Usuario!");
//                        sendMessange(he, gson.toJson(erro), STATUS_ERROR);
//                    }

                } else {
                    Erro erro = new Erro(codigoErro, "Erro ao cadastrar Usuario!");
                    sendMessange(he, gson.toJson(erro), STATUS_ERROR);
                }
                break;
            default:
//                headers.set(HEADER_ALLOW, Arrays.toString(ALLOWED_METHODS));
                he.sendResponseHeaders(STATUS_NOT_FOUND, NO_RESPONSE_LENGTH);
                break;
        }

        he.close();
    }
}
