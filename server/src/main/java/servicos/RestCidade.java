package servicos;

import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.CidadeDAO;
import dao.EnderecoDAO;
import dao.UsuarioDAO;
import entidades.Cidade;
import entidades.Endereco;
import entidades.Erro;
import entidades.Usuario;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilidades.Network;

import java.io.IOException;
import java.util.List;

public class RestCidade extends Network implements HttpHandler {

    private static final Logger logger = LogManager.getLogger(RestCidade.class.getName());
    private String codigoErro = "";

    @Override
    public void handle(HttpExchange he) throws IOException {
        if (!loadData(he))
            return;

        Cidade cidade;
        CidadeDAO dao = new CidadeDAO();

        try {
            switch (he.getRequestMethod().toUpperCase()) {
                case METHOD_GET:
                    String json = "";
                    List<Cidade> lista;

                    Integer id = parameters.get("id") == null ? null : Integer.parseInt(parameters.get("id").get(0));
                    Integer status = parameters.get("status") == null ? null : Integer.parseInt(parameters.get("status").get(0));
                    Integer offset = parameters.get("offset") == null ? null : Integer.parseInt(parameters.get("offset").get(0));
                    Integer limit = parameters.get("limit") == null ? null : Integer.parseInt(parameters.get("limit").get(0));

                    if(id != null){
                        cidade = dao.busca(id);
                        if (cidade != null)
                            json = gson.toJson(cidade);
                    } else {
                        lista = dao.lista(status,offset,limit);
                        json = gson.toJson(lista);
                    }

                    sendMessange(he, json, STATUS_OK);
                    break;
            }
        } catch (Exception e) {
            logger.catching(e);
        } finally {
            he.getRequestHeaders().clear();
            he.close();
        }
    }
}