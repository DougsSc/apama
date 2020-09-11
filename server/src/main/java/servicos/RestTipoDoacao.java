package servicos;

import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.TipoDoacaoDAO;
import entidades.TipoDoacao;
import entidades.Erro;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilidades.Network;

import java.io.IOException;
import java.util.List;

public class RestTipoDoacao extends Network implements HttpHandler {

    private static final Logger logger = LogManager.getLogger(RestTipoDoacao.class.getName());
    private Erro erro;

    @Override
    public void handle(HttpExchange he) throws IOException {
        if (!loadData(he))
            return;

        TipoDoacao tipoDoacao = null;
        TipoDoacaoDAO dao = new TipoDoacaoDAO();

        try {
            switch (he.getRequestMethod().toUpperCase()) {
                case METHOD_POST:
                    // CONVERT json
                    try {
                        tipoDoacao = gson.fromJson(body, TipoDoacao.class);

                        if(!validaDados(tipoDoacao)){
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        //- Inicia cadastros
                        if(!new TipoDoacaoDAO().cadastra(tipoDoacao)){
                            this.erro = new Erro("D.01");
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        // RESPONSE CREATED
                        logger.info("Doação " + tipoDoacao.getId() + " recebida!");
                        sendMessange(he, gson.toJson(tipoDoacao), STATUS_OK);
                        break;

                    } catch (JsonParseException e) {
                        this.erro = new Erro("EG.01");
                        sendMessange(he, gson.toJson(this.erro), STATUS_ERROR);
                        logger.catching(Level.ERROR, e);
                        return;
                    }
                case METHOD_GET:
                    String json = "";
                    List<TipoDoacao> lista;

                    Integer id = parameters.get("id") == null ? null : Integer.parseInt(parameters.get("id").get(0));
                    Integer status = parameters.get("status") == null ? null : Integer.parseInt(parameters.get("status").get(0));
                    Integer offset = parameters.get("offset") == null ? null : Integer.parseInt(parameters.get("offset").get(0));
                    Integer limit = parameters.get("limit") == null ? null : Integer.parseInt(parameters.get("limit").get(0));

                    if(id != null){
                        tipoDoacao = dao.busca(id);
                        if (tipoDoacao != null)
                            json = gson.toJson(tipoDoacao);
                    } else {
                        lista = dao.lista(status,offset,limit);
                        json = gson.toJson(lista);
                    }
                    sendMessange(he, json, STATUS_OK);
                    break;
                case METHOD_PUT:
                    // CONVERT json
                    try {
                        tipoDoacao = gson.fromJson(body, TipoDoacao.class);

                        if(!validaDados(tipoDoacao)){
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        //- Inicia atualizacao
                        if(!dao.atualiza(tipoDoacao)){
                            this.erro = new Erro("D.02");
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        // RESPONSE CREATED
                        logger.info("Doação " + tipoDoacao.getId() + " recebida!");
                        sendMessange(he, gson.toJson(tipoDoacao), STATUS_OK);
                        break;

                    } catch (JsonParseException e) {
                        this.erro = new Erro("EG.01");
                        sendMessange(he, gson.toJson(this.erro), STATUS_ERROR);
                        logger.catching(Level.ERROR, e);
                        return;
                    }
                case METHOD_DELETE:
                    //- Vai ter status?????
                    /*
                    if(parameters.isEmpty()){
                        this.erro = new Erro("EG.01");
                        sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                        return;
                    }

                    tipoDoacao = new TipoDoacaoDAO().busca(Integer.parseInt(parameters.get("id").get(0)));
                    if(tipoDoacao == null){
                        this.erro = new Erro("D.03");
                        sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                        return;
                    }

                    tipoDoacao.setStatus(0);
                    tipoDoacao.setJustificativa(tipoDoacao.getJustificativa());
                    if (dao.atualiza(tipoDoacao)) {
                        sendMessange(he, gson.toJson(tipoDoacao), STATUS_OK);
                    } else {
                        this.erro = new Erro("V.05");
                        sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                        return;
                    }
                     */
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

    private boolean validaDados(TipoDoacao tipoDoacao){
        if(tipoDoacao == null){
            this.erro = new Erro("TD.06");
            return false;
        }else if(tipoDoacao.getDescricao().trim().isEmpty()){
            this.erro = new Erro("TD.10");
        }

        return true;
    }
}