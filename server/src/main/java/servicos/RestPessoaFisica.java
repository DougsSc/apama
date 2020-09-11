package servicos;

import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import dao.*;
import entidades.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilidades.Network;
import utilidades.Utilidades;
import utilidades.Validacao;

import javax.rmi.CORBA.Util;
import java.io.IOException;
import java.util.*;

public class RestPessoaFisica extends Network implements HttpHandler {

    private static final Logger logger = LogManager.getLogger(RestPessoaFisica.class.getName());
    private Erro erro;

    @Override
    public void handle(HttpExchange he) throws IOException {
        if (!loadData(he))
            return;

        PessoaFisica pessoa = null;
        PessoaFisicaDAO dao = new PessoaFisicaDAO();

        try {
            switch (he.getRequestMethod().toUpperCase()) {
                case METHOD_POST:
                    // CONVERT json
                    try {
                        pessoa = gson.fromJson(body, PessoaFisica.class);

                        if(!validaDados(pessoa)){
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        //- Inicia cadastros
                        if(!new EnderecoDAO().cadastra(pessoa.getEndereco())){
                            this.erro = new Erro("E.01");
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        pessoa.setCpf(Utilidades.somenteNumeros(pessoa.getCpf()));
                        if(!dao.cadastra(pessoa)){
                            this.erro = new Erro("P.01");
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        if(!new EnderecoDAO().cadastraPessoaEndereco(pessoa.getId(),pessoa.getEndereco().getId())){
                            this.erro = new Erro("EP.01");
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        for(Contato contato : pessoa.getContatos()){
                            //contato.setPessoa(usuario);
                            contato.setIdPessoa(pessoa.getId());
                            if(!new ContatoDAO().cadastra(contato)){
                                this.erro = new Erro("C.01");
                                sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                                return;
                            }
                        }

                        // RESPONSE CREATED
                        logger.info("Pessoa Física " + pessoa.getId() + " recebida!");
                        sendMessange(he, gson.toJson(pessoa), STATUS_OK);
                        break;

                    } catch (JsonParseException e) {
                        this.erro = new Erro("EG.01");
                        sendMessange(he, gson.toJson(this.erro), STATUS_ERROR);
                        logger.catching(Level.ERROR, e);
                        return;
                    }
                case METHOD_GET:
                    String json = "";
                    List<PessoaFisica> lista;

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
                        pessoa = gson.fromJson(body, PessoaFisica.class);

                        if(!validaDados(pessoa)){
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        //- Inicia atualizacao
                        //- Cadastra um novo endereço para manter o historico
                        if(!new EnderecoDAO().cadastra(pessoa.getEndereco())){
                            this.erro = new Erro("E.01");
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        pessoa.setCpf(Utilidades.somenteNumeros(pessoa.getCpf()));
                        if(!dao.atualiza(pessoa)){
                            this.erro = new Erro("P.01");
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        if(!new EnderecoDAO().cadastraPessoaEndereco(pessoa.getId(),pessoa.getEndereco().getId())){
                            this.erro = new Erro("EP.01");
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        if(!new PessoaDAO().inativaContatos(pessoa.getId())){
                            this.erro = new Erro("C.01");
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        for(Contato contato : pessoa.getContatos()){
                            //contato.setPessoa(usuario);
                            contato.setIdPessoa(pessoa.getId());
                            if(!new ContatoDAO().cadastra(contato)){
                                this.erro = new Erro("C.01");
                                sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                                return;
                            }
                        }

                        // RESPONSE CREATED
                        logger.info("Pessoa física " + pessoa.getId() + " recebida!");
                        sendMessange(he, gson.toJson(pessoa), STATUS_OK);
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

                    pessoa = new PessoaFisicaDAO().busca(Integer.parseInt(parameters.get("id").get(0)));
                    if(pessoa == null){
                        this.erro = new Erro("P.03");
                        sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                        return;
                    }

                    pessoa.setStatus(0);
                    if (dao.atualiza(pessoa)) {
                        sendMessange(he, gson.toJson(pessoa), STATUS_OK);
                    } else {
                        this.erro = new Erro("P.05");
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

    private boolean validaDados(PessoaFisica pessoa){
        if(pessoa == null){
            this.erro = new Erro("P.06");
            return false;
        }else if(!validaPessoa(pessoa)){
            this.erro = new Erro("P.10");
            return false;
        }else if(!Validacao.validarCPF(Utilidades.somenteNumeros(pessoa.getCpf()))){
            this.erro = new Erro("PF.01");
            return false;
        }

        if(pessoa.getEndereco() == null){
            this.erro = new Erro("P.07");
            return false;
        }else if(!validaEndereco(pessoa.getEndereco())){
            this.erro = new Erro("E.10");
            return false;
        }

        if(pessoa.getContatos() == null || pessoa.getContatos().size() <= 0){
            this.erro = new Erro("P.08");
            return false;
        }else{
            for(Contato contato : pessoa.getContatos()){
                if(!validaContato(contato)){
                    this.erro = new Erro("C.06");
                    return false;
                }
            }
        }

        return true;
    }

    private boolean validaPessoa(PessoaFisica pessoa){
        if(pessoa.getNome().trim().isEmpty()){
            return false;
        }else if(Utilidades.somenteNumeros(pessoa.getCpf()).isEmpty()){
            return false;
        }

        return true;
    }

    private boolean validaEndereco(Endereco endereco){
        if(endereco.getLogradouro().trim().isEmpty()){
            return false;
        }else if(endereco.getNumero().trim().isEmpty()){
            return false;
        }else if(endereco.getBairro().trim().isEmpty()){
            return false;
        }else if(endereco.getCidade() == null || endereco.getCidade().getId() <= 0){
            return false;
        }else if(endereco.getCep().trim().length() != 8){
            return false;
        }/*else if(!validarCepPorAPI (ViaCEP) =) ){
            return false;
        }*/
        return true;
    }

    private boolean validaContato(Contato contato){
        if(contato.getTipoContato().getId() == 1 && Utilidades.somenteNumeros(contato.getContato()).length() != 10){
            return false;
        }else if(contato.getTipoContato().getId() == 2 && Utilidades.somenteNumeros(contato.getContato()).length() != 11){
            return false;
        }else if(contato.getTipoContato().getId() == 3 && !Validacao.validarEmail(contato.getContato())){
            return false;
        }
        return true;
    }
}