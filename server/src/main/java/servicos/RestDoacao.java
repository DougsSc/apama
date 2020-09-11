package servicos;

import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.*;
import entidades.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilidades.Network;

import java.io.IOException;
import java.util.List;

public class RestDoacao extends Network implements HttpHandler {

    private static final Logger logger = LogManager.getLogger(RestDoacao.class.getName());
    private Erro erro;

    @Override
    public void handle(HttpExchange he) throws IOException {
        if (!loadData(he))
            return;

        Doacao doacao = null;
        DoacaoDAO dao = new DoacaoDAO();

        try {
            switch (he.getRequestMethod().toUpperCase()) {
                case METHOD_POST:
                    // CONVERT json
                    try {
                        doacao = gson.fromJson(body, Doacao.class);

                        if(!validaDados(doacao)){
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        //- Inicia cadastros
                        if(!dao.cadastra(doacao)){
                            this.erro = new Erro("D.01");
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        // RESPONSE CREATED
                        logger.info("Doação " + doacao.getId() + " recebida!");
                        sendMessange(he, gson.toJson(doacao), STATUS_OK);
                        break;

                    } catch (JsonParseException e) {
                        this.erro = new Erro("EG.01");
                        sendMessange(he, gson.toJson(this.erro), STATUS_ERROR);
                        logger.catching(Level.ERROR, e);
                        return;
                    }
                case METHOD_GET:
                    String json = "";
                    List<Doacao> lista;

                    Integer id = parameters.get("id") == null ? null : Integer.parseInt(parameters.get("id").get(0));
                    Integer status = parameters.get("status") == null ? null : Integer.parseInt(parameters.get("status").get(0));
                    Integer offset = parameters.get("offset") == null ? null : Integer.parseInt(parameters.get("offset").get(0));
                    Integer limit = parameters.get("limit") == null ? null : Integer.parseInt(parameters.get("limit").get(0));

                    if(id != null){
                        doacao = dao.busca(id);
                        if (doacao != null)
                            json = gson.toJson(doacao);
                    } else {
                        lista = dao.lista(status,offset,limit);
                        json = gson.toJson(lista);
                    }
                    sendMessange(he, json, STATUS_OK);
                    break;
                case METHOD_PUT:
                    // CONVERT json
                    try {
                        doacao = gson.fromJson(body, Doacao.class);

                        if(!validaDados(doacao)){
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        //- Inicia atualizacao
                        if(!dao.atualiza(doacao)){
                            this.erro = new Erro("D.02");
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        // RESPONSE CREATED
                        logger.info("Doação " + doacao.getId() + " recebida!");
                        sendMessange(he, gson.toJson(doacao), STATUS_OK);
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

                    doacao = dao.busca(Integer.parseInt(parameters.get("id").get(0)));
                    if(doacao == null){
                        this.erro = new Erro("D.03");
                        sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                        return;
                    }

                    doacao.setStatus(0);
                    doacao.setJustificativa(parameters.get("justificativa").get(0));
                    if (dao.atualiza(doacao)) {
                        sendMessange(he, gson.toJson(doacao), STATUS_OK);
                    } else {
                        this.erro = new Erro("D.05");
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

    private boolean validaDados(Doacao doacao){
        if(doacao == null){
            this.erro = new Erro("D.06");
            return false;
        }else if(doacao.getPessoaFisica() == null && doacao.getPessoaJuridica() == null){
            this.erro = new Erro("D.07");
            return false;
        }else if(doacao.getPessoaFisica() != null && (doacao.getPessoaFisica().getId() == null || doacao.getPessoaFisica().getId() <= 0)){
            this.erro = new Erro("D.07");
            return false;
        }else if(doacao.getPessoaJuridica() != null && (doacao.getPessoaJuridica().getId() == null || doacao.getPessoaJuridica().getId() <= 0)){
            this.erro = new Erro("D.07");
            return false;
        }else if(doacao.getTipoDoacao() == null || doacao.getTipoDoacao().getId() == null || doacao.getTipoDoacao().getId() <= 0){
            this.erro = new Erro("D.08");
            return false;
        }else if(doacao.getTipoDoacao().getExigeValor() == 1 && (doacao.getValor() == null || doacao.getValor() <= 0)){
            this.erro = new Erro("D.10");
            return false;
        }

        return true;
    }
}