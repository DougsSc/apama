package servicos;

import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.CidadeDAO;
import dao.EnderecoDAO;
import dao.PessoaDAO;
import entidades.Cidade;
import entidades.Endereco;
import entidades.Erro;
import entidades.Pessoa;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilidades.Network;

import java.io.IOException;
import java.util.List;

public class RestTutor extends Network implements HttpHandler {

    private static final Logger logger = LogManager.getLogger(RestTutor.class.getName());
    private String codigoErro = "";

    @Override
    public void handle(HttpExchange he) throws IOException {
        if (!loadData(he))
           return;

        Pessoa pessoa = null;
        PessoaDAO dao = new PessoaDAO();

        try {
            switch (he.getRequestMethod().toUpperCase()) {

                case METHOD_POST:

                    // CONVERT json
                    try {
                        pessoa = gson.fromJson(body, Pessoa.class);
                    } catch (JsonParseException e) {
                        he.sendResponseHeaders(STATUS_ERROR, NO_RESPONSE_LENGTH);
                        logger.catching(Level.ERROR, e);
                    }

                    if (pessoa != null) {
                        if (dao.cadastra(pessoa)) {
                            // RESPONSE CREATED
                            logger.info("Tutor " + pessoa.getId() + " recebido!");
                            sendMessange(he, gson.toJson(pessoa), STATUS_ERROR);
                        } else {
                            Erro erro = new Erro("U.1", "Erro ao cadastrar Tutor!");
                            sendMessange(he, gson.toJson(erro), STATUS_ERROR);
                        }
                    } else {
                        Erro erro = new Erro(codigoErro, "Erro ao cadastrar Tutor!");
                        sendMessange(he, gson.toJson(erro), STATUS_ERROR);
                    }
                    break;

                case METHOD_GET:
                    String json = "";
                    List<Pessoa> lista;

                    Integer id = parameters.get("id") == null ? null : Integer.parseInt(parameters.get("id").get(0));
                    Integer status = parameters.get("status") == null ? null : Integer.parseInt(parameters.get("status").get(0));
                    Integer offset = parameters.get("offset") == null ? null : Integer.parseInt(parameters.get("offset").get(0));
                    Integer limit = parameters.get("limit") == null ? null : Integer.parseInt(parameters.get("limit").get(0));

                    if(id != null){
                        pessoa = dao.busca(id);
                        if (pessoa != null)
                            json = gson.toJson(pessoa);
                    } else {
                        lista = dao.lista(status,offset,limit);
                        json = gson.toJson(lista);
                    }
                    sendMessange(he, json, STATUS_OK);
                    break;

                case METHOD_PUT:
                    // CONVERT json
                    try {
                        pessoa = gson.fromJson(body.toString(), Pessoa.class);
                    } catch (JsonParseException e) {
                        Erro erro = new Erro(codigoErro, "Erro ao atualizar Tutor!");
                        sendMessange(he, gson.toJson(erro), STATUS_ERROR);
                    }

                    // RESPONSE CREATED
                    if (pessoa != null) {
                        if (dao.atualiza(pessoa)) {
                            logger.info("Tutor " + pessoa.getId() + " atualizado!");
                            sendMessange(he, gson.toJson(pessoa), STATUS_OK);
                        } else {
                            Erro erro = new Erro(codigoErro, "Erro ao atualizar Tutor!");
                            sendMessange(he, gson.toJson(erro), STATUS_ERROR);
                        }
                    } else {
                        Erro erro = new Erro(codigoErro, "Erro ao atualizar Tutor!");
                        sendMessange(he, gson.toJson(erro), STATUS_ERROR);
                    }

                    break;
                case METHOD_DELETE:

                    if (!parameters.isEmpty()) {

                        pessoa = new Pessoa();
                        // Falta Status...

                        if (dao.atualiza(pessoa)) {
                            sendMessange(he, gson.toJson(pessoa), STATUS_OK);
                        } else {
                            Erro erro = new Erro(codigoErro, "Erro ao deletar Tutor!");
                            sendMessange(he, gson.toJson(erro), STATUS_ERROR);
                        }
                    } else {
                        Erro erro = new Erro(codigoErro, "Erro ao deletar Tutor!");
                        sendMessange(he, gson.toJson(erro), STATUS_ERROR);
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

    private boolean validaEndereco(Endereco endereco) {
        boolean retorno = false;
        if (endereco != null) {
            // Se inserir a cidade, prossegue
            if (validaCidade(endereco.getCidade())) {
                // Se possuí um Endereço, então utiliza-o para cadastro da pessoa
                if (endereco.getId() == null || endereco.getId() < 1) {
                    retorno = new EnderecoDAO().cadastra(endereco);
                } else retorno = true;
            } else codigoErro = "E.2";
        } else codigoErro = "E.1";

        return retorno;
    }

    private boolean validaCidade(Cidade cidade) {
        boolean retorno = false;
        CidadeDAO dao = new CidadeDAO();
        if (cidade != null) {
            if (cidade.getId() == null || cidade.getId() < 1) {
                retorno = new CidadeDAO().cadastra(cidade);
            } else {
                Cidade cidadeAtt = dao.busca(cidade.getId());
                cidade.setId(cidadeAtt.getId());
                cidade.setDescricao(cidadeAtt.getDescricao());
                cidade.setEstado(cidadeAtt.getEstado());
                cidade.setUf(cidadeAtt.getUf());
                retorno = true;
            }
        } else codigoErro = "C.1";

        return retorno;
    }

}
