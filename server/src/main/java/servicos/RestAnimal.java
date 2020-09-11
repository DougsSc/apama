package servicos;

import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.EnderecoDAO;
import dao.AnimalDAO;
import dao.ImagemDAO;
import entidades.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilidades.Network;
import utilidades.Utilidades;
import utilidades.Validacao;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class RestAnimal extends Network implements HttpHandler {

    private static final Logger logger = LogManager.getLogger(RestAnimal.class.getName());
    private Erro erro;

    @Override
    public void handle(HttpExchange he) throws IOException {
        if (!loadData(he))
           return;

        Animal animal = null;
        AnimalDAO dao = new AnimalDAO();

        try {
            switch (he.getRequestMethod().toUpperCase()) {
                case METHOD_POST:
                    // CONVERT json
                    try {
                        animal = gson.fromJson(body, Animal.class);

                        if(!validaDados(animal)){
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        //- Inicia cadastros
                        if(!new EnderecoDAO().cadastra(animal.getEndereco())){
                            this.erro = new Erro("E.01");
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        if(!dao.cadastra(animal)){
                            this.erro = new Erro("A.01");
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        if(!new EnderecoDAO().cadastraAnimalEndereco(animal.getId(),animal.getEndereco().getId())){
                            this.erro = new Erro("EA.01");
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        if(animal.getArquivos() != null && !cadastraArquivos(animal.getArquivos(),animal.getId())){
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        // RESPONSE CREATED
                        logger.info("Animal " + animal.getId() + " recebido!");
                        sendMessange(he, gson.toJson(animal), STATUS_OK);
                        break;
                    } catch (JsonParseException e) {
                        this.erro = new Erro("EG.01");
                        sendMessange(he, gson.toJson(this.erro), STATUS_ERROR);
                        logger.catching(Level.ERROR, e);
                        return;
                    }
                case METHOD_GET:
                    String json = "";
                    List<Animal> lista;

                    Integer id = parameters.get("id") == null ? null : Integer.parseInt(parameters.get("id").get(0));
                    Integer status = parameters.get("status") == null ? null : Integer.parseInt(parameters.get("status").get(0));
                    Integer offset = parameters.get("offset") == null ? null : Integer.parseInt(parameters.get("offset").get(0));
                    Integer limit = parameters.get("limit") == null ? null : Integer.parseInt(parameters.get("limit").get(0));
                    boolean adotaveis = parameters.get("adotaveis") != null;

                    if(id != null){
                        animal = dao.busca(id);
                        if(animal != null){
                            json = gson.toJson(animal);
                        }
                    } else if(adotaveis) {
                        lista = dao.listaDisponivelAdocao(offset,limit);
                        json = gson.toJson(lista);
                    } else {
                        lista = dao.lista(status,offset,limit);
                        json = gson.toJson(lista);
                    }

                    sendMessange(he, json, STATUS_OK);
                    break;
                case METHOD_PUT:
                    // CONVERT json
                    try {
                        animal = gson.fromJson(body.toString(), Animal.class);

                        if(!validaDados(animal)){
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        //- Inicia atualizacao
                        //- Cadastra um novo endereço para manter o historico
                        if(!new EnderecoDAO().cadastra(animal.getEndereco())){
                            this.erro = new Erro("E.01");
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        if(!dao.atualiza(animal)){
                            this.erro = new Erro("A.02");
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        if(!new EnderecoDAO().cadastraAnimalEndereco(animal.getId(),animal.getEndereco().getId())){
                            this.erro = new Erro("EA.01");
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        if(animal.getArquivos() != null && !cadastraArquivos(animal.getArquivos(),animal.getId())){
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        // RESPONSE CREATED
                        logger.info("Animal " + animal.getId() + " recebido!");
                        sendMessange(he, gson.toJson(animal), STATUS_OK);
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

                    animal = new AnimalDAO().busca(Integer.parseInt(parameters.get("id").get(0)));
                    if(animal == null){
                        this.erro = new Erro("A.03");
                        sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                        return;
                    }

                    animal.getAnimalStatus().setId(4); //- Obito ??????
                    if (dao.atualiza(animal)) {
                        sendMessange(he, gson.toJson(animal), STATUS_OK);
                    } else {
                        this.erro = new Erro("A.05");
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

    private boolean cadastraArquivos(List<Arquivo> arquivos, Integer idAnimal){
        ImagemDAO imagemDAO = new ImagemDAO();

        if(!imagemDAO.removeAnimalImagens(idAnimal)){
            this.erro = new Erro("A.15");
            return false;
        }

        for(Arquivo arquivo : arquivos){
            if(arquivo == null){
                continue; //- Classe em branco, vai pro proximo arquivo
            }

            String nome = "AN"+idAnimal+"_" + UUID.randomUUID().toString() + Utilidades.getExtensionOfFile(arquivo.getNome());
            File file = Utilidades.getDecodedFile(arquivo.getArquivo(), nome);
            if(file == null || !file.exists()){
                this.erro = new Erro("A.14");
                return false;
            }

            arquivo.setNome(nome);
            if(!imagemDAO.cadastra(arquivo)){
                this.erro = new Erro("A.12");
                return false;
            }

            if(!imagemDAO.cadastraAnimalImagem(idAnimal, arquivo.getId())){
                this.erro = new Erro("A.13");
                return false;
            }
        }

        return true;
    }

    private boolean validaDados(Animal animal){
        if(animal == null){
            this.erro = new Erro("A.06");
            return false;
        }else if(!validaAnimal(animal)){
            this.erro = new Erro("A.10");
            return false;
        }else if(animal.getArquivos() != null && !validaArquivos(animal.getArquivos())){
            this.erro = new Erro("A.11");
            return false;
        }

        if(animal.getEndereco() == null){
            this.erro = new Erro("A.07");
            return false;
        }else if(!validaEndereco(animal.getEndereco())){
            this.erro = new Erro("E.10");
            return false;
        }

        return true;
    }

    private boolean validaArquivos(List<Arquivo> arquivos){
        for(Arquivo arquivo : arquivos){
            if(arquivo.getArquivo().trim().isEmpty()){
                return false;
            }
        }
        return true;
    }

    private boolean validaAnimal(Animal animal){
        if(animal.getAnimalPorte() == null || animal.getAnimalPorte().getId() == null || animal.getAnimalPorte().getId() <= 0){
            return false;
        }else if(animal.getAnimalStatus() == null || animal.getAnimalStatus().getId() == null || animal.getAnimalStatus().getId() <= 0){
            return false;
        }else if(animal.getDataResgate().trim().isEmpty() || !Validacao.validarData(animal.getDataResgate())){
            return false;
        }else if(animal.getNome().trim().isEmpty()){
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
        }else if(endereco.getCidade() == null || endereco.getCidade().getId() == null || endereco.getCidade().getId() <= 0){
            return false;
        }else if(endereco.getCep().trim().length() != 8){
            return false;
        }/*else if(!validarCepPorAPI (ViaCEP) =) ){
            return false;
        }*/
        return true;
    }
}
