package servicos;

import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.AnimalTratamentoDAO;
import entidades.AnimalTratamento;
import entidades.Erro;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilidades.Network;
import utilidades.Validacao;

import java.io.IOException;
import java.util.List;

public class RestAnimalTratamento extends Network implements HttpHandler {

    private static final Logger logger = LogManager.getLogger(RestAnimalTratamento.class.getName());
    private Erro erro;

    @Override
    public void handle(HttpExchange he) throws IOException {
        if (!loadData(he))
           return;

        AnimalTratamento tratamento = null;
        AnimalTratamentoDAO dao = new AnimalTratamentoDAO();

        try {
            switch (he.getRequestMethod().toUpperCase()) {
                case METHOD_POST:
                    // CONVERT json
                    try {
                        tratamento = gson.fromJson(body, AnimalTratamento.class);

                        if(!validaDados(tratamento)){
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        if(!dao.cadastra(tratamento)){
                            this.erro = new Erro("T.01");
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        // RESPONSE CREATED
                        logger.info("AnimalTratamento " + tratamento.getId() + " recebido!");
                        sendMessange(he, gson.toJson(tratamento), STATUS_OK);
                        break;
                    } catch (JsonParseException e) {
                        this.erro = new Erro("EG.01");
                        sendMessange(he, gson.toJson(this.erro), STATUS_ERROR);
                        logger.catching(Level.ERROR, e);
                        return;
                    }
                case METHOD_GET:
                    String json = "";
                    List<AnimalTratamento> lista;

                    Integer id = parameters.get("id") == null ? null : Integer.parseInt(parameters.get("id").get(0));
                    Integer status = parameters.get("status") == null ? null : Integer.parseInt(parameters.get("status").get(0));
                    Integer offset = parameters.get("offset") == null ? null : Integer.parseInt(parameters.get("offset").get(0));
                    Integer limit = parameters.get("limit") == null ? null : Integer.parseInt(parameters.get("limit").get(0));

                    if(id != null){
                        tratamento = dao.busca(id);
                        if(tratamento != null){
                            json = gson.toJson(tratamento);
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
                        tratamento = gson.fromJson(body.toString(), AnimalTratamento.class);

                        if(!validaDados(tratamento)){
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        //- Inicia atualizacao
                        if(!dao.atualiza(tratamento)){
                            this.erro = new Erro("T.02");
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        // RESPONSE CREATED
                        logger.info("AnimalTratamento " + tratamento.getId() + " recebido!");
                        sendMessange(he, gson.toJson(tratamento), STATUS_OK);
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

                    tratamento = new AnimalTratamentoDAO().busca(Integer.parseInt(parameters.get("id").get(0)));
                    if(tratamento == null){
                        this.erro = new Erro("T.03");
                        sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                        return;
                    }

                    tratamento.setStatus(0);
                    if (dao.atualiza(tratamento)) {
                        sendMessange(he, gson.toJson(tratamento), STATUS_OK);
                    } else {
                        this.erro = new Erro("T.05");
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

    private boolean validaDados(AnimalTratamento tratamento){
        if(tratamento == null){
            this.erro = new Erro("T.06");
            return false;
        }else if(tratamento.getAnimal() == null || tratamento.getAnimal().getId() == null || tratamento.getAnimal().getId() <= 0){
            this.erro = new Erro("A.6");
            return false;
        }else if(tratamento.getTipoTratamento() == null || tratamento.getTipoTratamento().getId() == null || tratamento.getTipoTratamento().getId() <= 0){
            this.erro = new Erro("TT.6");
            return false;
        }else if(tratamento.getDataTratamento().trim().isEmpty() || !Validacao.validarData(tratamento.getDataTratamento())){
            this.erro = new Erro("T.10");
            return false;
        }

        return true;
    }
}