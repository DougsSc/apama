package servicos;

import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.TipoTratamentoDAO;
import entidades.TipoTratamento;
import entidades.Erro;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilidades.Network;
import utilidades.Validacao;

import java.io.IOException;
import java.util.List;

public class RestTipoTratamento extends Network implements HttpHandler {

    private static final Logger logger = LogManager.getLogger(RestTipoTratamento.class.getName());
    private Erro erro;

    @Override
    public void handle(HttpExchange he) throws IOException {
        if (!loadData(he))
           return;

        TipoTratamento tipo = null;
        TipoTratamentoDAO dao = new TipoTratamentoDAO();

        try {
            switch (he.getRequestMethod().toUpperCase()) {
                case METHOD_POST:
                    // CONVERT json
                    try {
                        tipo = gson.fromJson(body, TipoTratamento.class);

                        if(!validaDados(tipo)){
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        if(!dao.cadastra(tipo)){
                            this.erro = new Erro("TT.01");
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        // RESPONSE CREATED
                        logger.info("TipoTratamento " + tipo.getId() + " recebido!");
                        sendMessange(he, gson.toJson(tipo), STATUS_OK);
                        break;
                    } catch (JsonParseException e) {
                        this.erro = new Erro("EG.01");
                        sendMessange(he, gson.toJson(this.erro), STATUS_ERROR);
                        logger.catching(Level.ERROR, e);
                        return;
                    }
                case METHOD_GET:
                    String json = "";
                    List<TipoTratamento> lista;

                    Integer id = parameters.get("id") == null ? null : Integer.parseInt(parameters.get("id").get(0));
                    Integer status = parameters.get("status") == null ? null : Integer.parseInt(parameters.get("status").get(0));
                    Integer offset = parameters.get("offset") == null ? null : Integer.parseInt(parameters.get("offset").get(0));
                    Integer limit = parameters.get("limit") == null ? null : Integer.parseInt(parameters.get("limit").get(0));

                    if(id != null){
                        tipo = dao.busca(id);
                        if(tipo != null){
                            json = gson.toJson(tipo);
                        }
                    } else {
                        lista = dao.lista(status,offset,limit);
                        json = gson.toJson(lista);
                    }

                    sendMessange(he, json, STATUS_OK);
                    break;
                case METHOD_PUT:
                    // CONVERT json
                    try {
                        tipo = gson.fromJson(body.toString(), TipoTratamento.class);

                        if(!validaDados(tipo)){
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        //- Inicia atualizacao
                        if(!dao.atualiza(tipo)){
                            this.erro = new Erro("TT.02");
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        // RESPONSE CREATED
                        logger.info("TipoTratamento " + tipo.getId() + " recebido!");
                        sendMessange(he, gson.toJson(tipo), STATUS_OK);
                        break;
                    } catch (JsonParseException e) {
                        this.erro = new Erro("EG.01");
                        sendMessange(he, gson.toJson(this.erro), STATUS_ERROR);
                        logger.catching(Level.ERROR, e);
                        return;
                    }
                case METHOD_DELETE:
                    if(parameters.isEmpty()){
                        this.erro = new Erro("EG.01");
                        sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                        return;
                    }

                    tipo = new TipoTratamentoDAO().busca(Integer.parseInt(parameters.get("id").get(0)));
                    if(tipo == null){
                        this.erro = new Erro("TT.03");
                        sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                        return;
                    }

                    tipo.setStatus(0);
                    if (dao.atualiza(tipo)) {
                        sendMessange(he, gson.toJson(tipo), STATUS_OK);
                    } else {
                        this.erro = new Erro("TT.05");
                        sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                        return;
                    }
                    break;
                default:
//                headers.set(HEADER_ALLOW, Arrays.toString(ALLOWED_METHODS));
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

    private boolean validaDados(TipoTratamento tipo){
        if(tipo == null){
            this.erro = new Erro("TT.06");
            return false;
        }else if(tipo.getDescricao().trim().isEmpty()){
            this.erro = new Erro("TT.10");
            return false;
        }

        return true;
    }
}
