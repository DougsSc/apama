package servicos;

import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
//import com.sun.org.apache.bcel.internal.generic.RETURN;
import dao.*;
import entidades.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilidades.Network;
import utilidades.Utilidades;
import utilidades.Validacao;

//import javax.rmi.CORBA.Util;
import java.io.IOException;
import java.util.*;

public class RestUsuario extends Network implements HttpHandler {

    private static final Logger logger = LogManager.getLogger(RestUsuario.class.getName());
    private Erro erro;
    
    @Override
    public void handle(HttpExchange he) throws IOException {
        if (!loadData(he))
            return;

        Usuario usuario = null;
        UsuarioDAO dao = new UsuarioDAO();

        try {
            switch (he.getRequestMethod().toUpperCase()) {
                case METHOD_POST:
                    // CONVERT json
                    try {
                        usuario = gson.fromJson(body, Usuario.class);

                        if(!validaDados(usuario)){
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        //- Inicia cadastros
                        if(!new EnderecoDAO().cadastra(usuario.getEndereco())){
                            this.erro = new Erro("E.01");
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        usuario.setSenha(Utilidades.criptografaSenha(usuario.getSenha()));
                        usuario.setCpf(Utilidades.somenteNumeros(usuario.getCpf()));
                        if(!dao.cadastra(usuario)){
                            this.erro = new Erro("V.01");
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        if(!new EnderecoDAO().cadastraPessoaEndereco(usuario.getId(),usuario.getEndereco().getId())){
                            this.erro = new Erro("EV.01");
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        for(Contato contato : usuario.getContatos()){
                            //contato.setPessoa(usuario);
                            contato.setIdPessoa(usuario.getId());
                            if(!new ContatoDAO().cadastra(contato)){
                                this.erro = new Erro("C.01");
                                sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                                return;
                            }
                        }

                        // RESPONSE CREATED
                        logger.info("Usuário " + usuario.getId() + " recebido!");
                        sendMessange(he, gson.toJson(usuario), STATUS_OK);
                        break;
                    } catch (JsonParseException e) {
                        this.erro = new Erro("EG.01");
                        sendMessange(he, gson.toJson(this.erro), STATUS_ERROR);
                        logger.catching(Level.ERROR, e);
                        return;
                    }
                case METHOD_GET:
                    String json = "";
                    List<Usuario> lista;

                    Integer id = parameters.get("id") == null ? null : Integer.parseInt(parameters.get("id").get(0));
                    Integer status = parameters.get("status") == null ? null : Integer.parseInt(parameters.get("status").get(0));
                    Integer offset = parameters.get("offset") == null ? null : Integer.parseInt(parameters.get("offset").get(0));
                    Integer limit = parameters.get("limit") == null ? null : Integer.parseInt(parameters.get("limit").get(0));

                    if(id != null){
                        usuario = dao.busca(id);
                        if(usuario != null){
                            json = gson.toJson(usuario);
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
                        usuario = gson.fromJson(body, Usuario.class);

                        if(!validaDados(usuario)){
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        //- Inicia atualizacao
                        //- Cadastra um novo endereço para manter o historico
                        if(!new EnderecoDAO().cadastra(usuario.getEndereco())){
                            this.erro = new Erro("E.01");
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        if(!usuario.getSenha().isEmpty()){
                            usuario.setSenha(Utilidades.criptografaSenha(usuario.getSenha()));
                        }
                        usuario.setCpf(Utilidades.somenteNumeros(usuario.getCpf()));
                        if(!dao.atualiza(usuario)){
                            this.erro = new Erro("V.01");
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        if(!new EnderecoDAO().cadastraPessoaEndereco(usuario.getId(),usuario.getEndereco().getId())){
                            this.erro = new Erro("EV.01");
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        if(!new PessoaDAO().inativaContatos(usuario.getId())){
                            this.erro = new Erro("C.01");
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        for(Contato contato : usuario.getContatos()){
                            //contato.setPessoa(usuario);
                            contato.setIdPessoa(usuario.getId());
                            if(!new ContatoDAO().cadastra(contato)){
                                this.erro = new Erro("C.01");
                                sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                                return;
                            }
                        }

                        // RESPONSE CREATED
                        logger.info("Usuário " + usuario.getId() + " recebido!");
                        sendMessange(he, gson.toJson(usuario), STATUS_OK);
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

                    usuario = new UsuarioDAO().busca(Integer.parseInt(parameters.get("id").get(0)));
                    if(usuario == null){
                        this.erro = new Erro("V.03");
                        sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                        return;
                    }

                    usuario.setStatusUsuario(0);
                    //usuario.setStatus(0);
                    if (dao.atualiza(usuario)) {
                        sendMessange(he, gson.toJson(usuario), STATUS_OK);
                    } else {
                        this.erro = new Erro("V.05");
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

    private boolean validaDados(Usuario usuario){
        if(usuario == null){
            this.erro = new Erro("V.06");
            return false;
        }else if(!validaUsuario(usuario)){
            this.erro = new Erro("V.10");
            return false;
        }else if(!Validacao.validarCPF(Utilidades.somenteNumeros(usuario.getCpf()))){
            this.erro = new Erro("PF.01");
            return false;
        }else if(!new UsuarioDAO().isLoginDisponivel(usuario.getLogin(),usuario.getId())){
            this.erro = new Erro("V.09");
            return false;
        }

        if(usuario.getEndereco() == null){
            this.erro = new Erro("V.07");
            return false;
        }else if(!validaEndereco(usuario.getEndereco())){
            this.erro = new Erro("E.10");
            return false;
        }

        if(usuario.getContatos() == null || usuario.getContatos().size() <= 0){
            this.erro = new Erro("V.08");
            return false;
        }else{
            for(Contato contato : usuario.getContatos()){
                if(!validaContato(contato)){
                    this.erro = new Erro("C.06");
                    return false;
                }
            }
        }

        return true;
    }
    
    private boolean validaUsuario(Usuario usuario){
        if(usuario.getNome().trim().isEmpty()){
            return false;
        }else if(Utilidades.somenteNumeros(usuario.getCpf()).isEmpty()){
            return false;
        }else if(usuario.getLogin().trim().isEmpty()){
            return false;
        }else if((usuario.getId() == null || usuario.getId() <= 0) && usuario.getSenha().isEmpty()){ //- Nao remover espacos da senha
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