package utilidades;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import dao.UsuarioDAO;
import entidades.Permissao;
import entidades.Usuario;

import javax.rmi.CORBA.Util;
import javax.xml.ws.spi.http.HttpContext;
import javax.xml.ws.spi.http.HttpHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;

public class Network {

    protected static final String HEADER_ALLOW = "Allow";
    protected static final String HEADER_TOKEN = "Token";
    protected static final String HEADER_AUTHORIZATION = "Authorization";
    protected static final String HEADER_CONTENT_TYPE = "Content-Type";
    protected static final String CHARSET = StandardCharsets.UTF_8.toString();

    // Sucesso
    protected static final int STATUS_OK = 200;             // Estas requisição foi bem sucedida
    protected static final int STATUS_CREATED = 201;        // A requisição foi bem sucessida e um novo recurso foi criado como resultado
    protected static final int STATUS_ACCEPTED = 202;       // A requisição foi recebida mas nenhuma ação foi tomada sobre ela.
    protected static final int STATUS_NO_CONTENT = 204;     // Não há conteúdo para enviar para esta solicitação

    // Erros do Cliente
    protected static final int STATUS_BAD_REQUEST = 400;
    protected static final int STATUS_UNAUTHORIZED = 401;   // O cliente deve se autenticar para obter a resposta solicitada
    protected static final int STATUS_FORBIDDEN = 403;      // O cliente não tem direitos de acesso ao conteúdo portanto o servidor está rejeitando dar a resposta
    protected static final int STATUS_NOT_FOUND = 404;      // O servidor não pode encontrar o recurso solicitado

    // Erro do Servidor
    protected static final int STATUS_ERROR = 500;          // Erro interno de processamento

    protected static final int NO_RESPONSE_LENGTH = -1;

    // Metodos da API
    protected static final String METHOD_POST = "POST";     // Criacao de item(ns)
    protected static final String METHOD_GET = "GET";       // Listagem completa ou busca por item
    protected static final String METHOD_PUT = "PUT";       // Atualizacao completa do item
    protected static final String METHOD_DELETE = "DELETE"; // Ocultacao de um item especifico
    protected static final String[] ALLOWED_METHODS = {METHOD_POST, METHOD_GET, METHOD_PUT, METHOD_DELETE};

    // Variaveis para uso generico
    protected static final Gson gson = new Gson();

    protected String body;
    protected Map<String, List<String>> parameters;
//    protected List<Permissao> permissoes;

    /**
     * void loadData(HttpExchange he) {}
     * Info: Carrega os dados genéricos e valida as permissoes
     * do usuário de acordo com o Metodo requisitado;
     */
    protected boolean loadData(HttpExchange he) throws IOException {
        try {
            Headers headers = he.getRequestHeaders();

            parameters = getRequestParameters(he.getRequestURI());

            BufferedReader bufferedReader;
            StringBuilder body = new StringBuilder();

            List<Permissao> permissoes = new UsuarioDAO().getPermissoesByUsuario(headers.get(HEADER_TOKEN).get(0), null);

            if (!validateAuth(headers.get(HEADER_AUTHORIZATION).get(0))
                    || !validateContentType(headers.get(HEADER_CONTENT_TYPE).get(0))
                    || !validatePermissions(he.getRequestMethod().toUpperCase(), permissoes)) {
                he.sendResponseHeaders(STATUS_UNAUTHORIZED, NO_RESPONSE_LENGTH);
                return false;
            }

            // READING MESSAGE
            bufferedReader = new BufferedReader(new InputStreamReader(he.getRequestBody()));
            String line;
            while ((line = bufferedReader.readLine()) != null)
                body.append(line);

            this.body = body.toString();
            System.out.println("MESSAGE: " + body.toString());

            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


        return true;
    }

    /**
     * Map<String, List<String>> getRequestParameters(URI requestUri) {}
     * Info: Carrega os parametros passados para inclusão nas consultas;
     */
    protected static Map<String, List<String>> getRequestParameters(URI requestUri) {
        final Map<String, List<String>> requestParameters = new LinkedHashMap<>();
        final String requestQuery = requestUri.getRawQuery();
        if (requestQuery != null) {
            final String[] rawRequestParameters = requestQuery.split("[&;]", -1);
            for (final String rawRequestParameter : rawRequestParameters) {
                final String[] requestParameter = rawRequestParameter.split("=", 2);
                final String requestParameterName = decodeUrlComponent(requestParameter[0]);
                requestParameters.putIfAbsent(requestParameterName, new ArrayList<>());
                final String requestParameterValue = requestParameter.length > 1 ? decodeUrlComponent(requestParameter[1]) : null;
                requestParameters.get(requestParameterName).add(requestParameterValue);
            }
        }
        return requestParameters;
    }

    private static String decodeUrlComponent(final String urlComponent) {
        try {
            return URLDecoder.decode(urlComponent, CHARSET);
        } catch (final UnsupportedEncodingException ex) {
            throw new InternalError(ex);
        }
    }

    private static String decodeAuth(String encoded) {
        encoded = encoded.replaceAll("Basic ", "");
        byte[] decoded = Base64.getDecoder().decode(encoded);

        return new String(decoded);
    }

    protected static boolean validateAuth(String authorization) {
        boolean valid = false;

        String[] connection = decodeAuth(authorization).split(":");

        String userC = connection[0];
        String passwordC = connection[1];

        String[] properties = decodeAuth(Utilidades.getVariavel("var.basicauth")).split(":");
        String userP = properties[0];
        String passwordP = properties[1];

        if (userC.equals(userP) && passwordC.equals(passwordP))
            valid = true;

        return valid;
    }

    protected static boolean validateContentType(String type) {
        return type.equals(Utilidades.getVariavel("var.contenttype"));
    }

    protected static boolean validatePermissions(String method, List<Permissao> permissoes) {

        String permissao = "";
        switch (method) {
            case METHOD_POST:
                permissao = Utilidades.PERMISSAO_INSERIR;
                break;
            case METHOD_GET:
                permissao = Utilidades.PERMISSAO_VISUALIZAR;
                break;
            case METHOD_PUT:
                permissao = Utilidades.PERMISSAO_EDITAR;
                break;
            case METHOD_DELETE:
                permissao = Utilidades.PERMISSAO_EXCLUIR;
                break;
        }

        if (!permissao.isEmpty()) {
            for (Permissao p : permissoes) {
                if (p.getPermissao().equalsIgnoreCase(permissao)) return true;
            }
        }

        return false;
    }

    protected static void sendMessange(HttpExchange he, String responseBody, int httpStatus) throws IOException {
        he.getResponseHeaders().set(HEADER_CONTENT_TYPE, String.format("application/json; charset=%s", CHARSET));
        final byte[] rawResponseBody = responseBody.getBytes(CHARSET);
        if (responseBody.isEmpty()) he.sendResponseHeaders(STATUS_NO_CONTENT, rawResponseBody.length);
        else he.sendResponseHeaders(httpStatus, rawResponseBody.length);
        he.getResponseBody().write(rawResponseBody);
    }

//    protected static String getToken(Headers headers) {
//        String token = "";
//        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
//            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
//            if (entry.getKey().equalsIgnoreCase("token")) {
//                token = entry.getValue().toString();
//            }
//        }
//
//        return token;
//    }
}

