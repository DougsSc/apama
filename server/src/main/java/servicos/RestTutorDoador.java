package servicos;

import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.DoacaoDAO;
import dao.PessoaDAO;
import entidades.Doacao;
import entidades.Erro;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilidades.Network;

import java.io.IOException;
import java.util.List;

public class RestTutorDoador extends Network implements HttpHandler {

    private static final Logger logger = LogManager.getLogger(RestTutorDoador.class.getName());
    private Erro erro;

    @Override
    public void handle(HttpExchange he) throws IOException {
        if (!loadData(he))
            return;

        Object pessoa = null;
        PessoaDAO dao = new PessoaDAO();

        try {
            switch (he.getRequestMethod().toUpperCase()) {
                case METHOD_POST:
                case METHOD_PUT:
                case METHOD_DELETE:
                    //- UTILIZAR O RestPessoaFisica ou RestPessoaJuridica PARA SALVAR, ATUALIZAR OU INATIVAR OS DADOS
                    this.erro = new Erro("EG.01");
                    sendMessange(he, gson.toJson(this.erro), STATUS_ERROR);
                    //logger.catching(Level.ERROR, e);
                    return;
                case METHOD_GET:
                    String json = "";
                    List<Object> lista;

                    Integer id = parameters.get("id") == null ? null : Integer.parseInt(parameters.get("id").get(0));
                    Integer status = parameters.get("status") == null ? null : Integer.parseInt(parameters.get("status").get(0));
                    Integer offset = parameters.get("offset") == null ? null : Integer.parseInt(parameters.get("offset").get(0));
                    Integer limit = parameters.get("limit") == null ? null : Integer.parseInt(parameters.get("limit").get(0));
                    Integer liberarAdocao = parameters.get("liberarAdocao") == null ? null : Integer.parseInt(parameters.get("liberarAdocao").get(0));

                    if(id != null){
                        pessoa = dao.buscaTutorDoador(id);
                        if(pessoa != null){
                            json = gson.toJson(pessoa);
                        }
                    } else {
                        lista = dao.listaTudoresDoadores(status,offset,limit,liberarAdocao);
                        json = gson.toJson(lista);
                    }

                    sendMessange(he, json, STATUS_OK);
                    break;
                default:
                    //headers.set(HEADER_ALLOW, Arrays.toString(ALLOWED_METHODS));
                    he.sendResponseHeaders(STATUS_NOT_FOUND, NO_RESPONSE_LENGTH);
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