package servicos;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.UsuarioDAO;
import entidades.Usuario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilidades.Network;
import utilidades.Utilidades;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class RestLogin extends Network implements HttpHandler {

    private static final Logger logger = LogManager.getLogger(RestLogin.class.getName());

    @Override
    public void handle(HttpExchange he) throws IOException {
        Headers headers = he.getRequestHeaders();

        if (!validateAuth(headers.get(HEADER_AUTHORIZATION).get(0)) || !validateContentType(headers.get(HEADER_CONTENT_TYPE).get(0))) {
            he.sendResponseHeaders(STATUS_UNAUTHORIZED, NO_RESPONSE_LENGTH);
            logger.info(STATUS_UNAUTHORIZED + " UNAUTHORIZED");
            return;
        }

        try {
            switch (he.getRequestMethod().toUpperCase()) {
                case METHOD_GET:
                    String login = "";
                    String senha = "";
                    for (Map.Entry<String, List<String>> entry : getRequestParameters(he.getRequestURI()).entrySet()) {

                        if (entry.getKey().equalsIgnoreCase("login"))
                            login = entry.getValue().get(0);

                        if (entry.getKey().equalsIgnoreCase("senha"))
                            senha = entry.getValue().get(0);
                    }

                    UsuarioDAO usuarioDAO = new UsuarioDAO();
                    Usuario usuario = usuarioDAO.login(login, senha);

                    if (usuario != null) {
                        String token = Utilidades.gerarToken();
                        if (usuarioDAO.salvarToken(usuario.getId(), token)) {
                            JsonElement jsonElement = gson.toJsonTree(usuario);
                            jsonElement.getAsJsonObject().addProperty("token", token);
                            String json = gson.toJson(jsonElement);

//                            System.out.println("json " + json);

//                        JsonObject object = new JsonObject();
//                        object.addProperty("token", token);
//                        System.out.println(object.toString());

                            sendMessange(he, json, STATUS_OK);
                            //he.sendResponseHeaders(STATUS_OK, NO_RESPONSE_LENGTH);
                        } else {
                            he.sendResponseHeaders(STATUS_UNAUTHORIZED, NO_RESPONSE_LENGTH);
                        }
                    } else {
                        he.sendResponseHeaders(STATUS_UNAUTHORIZED, NO_RESPONSE_LENGTH);
                    }
                    break;
                default:
//                headers.set(HEADER_ALLOW, Arrays.toString(ALLOWED_METHODS));
                    he.sendResponseHeaders(STATUS_NOT_FOUND, NO_RESPONSE_LENGTH);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            he.close();
        }
    }
}
