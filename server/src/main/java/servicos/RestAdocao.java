package servicos;

import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.*;
import entidades.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilidades.*;

import java.io.File;
import java.io.IOException;
import java.text.Format;
import java.util.List;
import java.util.UUID;

public class RestAdocao extends Network implements HttpHandler {

    private static final Logger logger = LogManager.getLogger(RestAdocao.class.getName());
    private Erro erro;

    @Override
    public void handle(HttpExchange he) throws IOException {
        if (!loadData(he))
            return;

        Adocao adocao = null;
        AdocaoDAO dao = new AdocaoDAO();

        try {
            switch (he.getRequestMethod().toUpperCase()) {
                case METHOD_POST:
                    // CONVERT json
                    try {
                        adocao = gson.fromJson(body, Adocao.class);

                        if(!validaDados(adocao)){
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        //- Inicia cadastros
                        if(!dao.cadastra(adocao)){
                            this.erro = new Erro("AD.01");
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        if(adocao.getStatus().getId() == 3){ //- Aprovado
                            Animal animal = new AnimalDAO().busca(adocao.getAnimal().getId());
                            animal.setDataAdocao(Utilidades.getDataAtual());
                            animal.getAnimalStatus().setId(3); //- Adotado
                            new AnimalDAO().atualiza(animal);

                            enviaEmail(adocao);
                        }

                        if(adocao.getArquivos() != null && !cadastraArquivos(adocao.getArquivos(),adocao.getId())){
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        // RESPONSE CREATED
                        logger.info("Adoção " + adocao.getId() + " recebida!");
                        sendMessange(he, gson.toJson(adocao), STATUS_OK);
                        break;

                    } catch (JsonParseException e) {
                        this.erro = new Erro("EG.01");
                        sendMessange(he, gson.toJson(this.erro), STATUS_ERROR);
                        logger.catching(Level.ERROR, e);
                        return;
                    }
                case METHOD_GET:
                    String json = "";
                    List<Adocao> lista;

                    Integer id = parameters.get("id") == null ? null : Integer.parseInt(parameters.get("id").get(0));
                    Integer status = parameters.get("status") == null ? null : Integer.parseInt(parameters.get("status").get(0));
                    Integer offset = parameters.get("offset") == null ? null : Integer.parseInt(parameters.get("offset").get(0));
                    Integer limit = parameters.get("limit") == null ? null : Integer.parseInt(parameters.get("limit").get(0));

                    if(id != null){
                        adocao = dao.busca(id);
                        if (adocao != null)
                            json = gson.toJson(adocao);
                    } else {
                        lista = dao.lista(status,offset,limit);
                        json = gson.toJson(lista);
                    }
                    sendMessange(he, json, STATUS_OK);
                    break;
                case METHOD_PUT:
                    // CONVERT json
                    try {
                        adocao = gson.fromJson(body, Adocao.class);

                        if(!validaDados(adocao)){
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        //- Inicia atualizacao
                        if(!dao.atualiza(adocao)){
                            this.erro = new Erro("AD.02");
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        if(adocao.getStatus().getId() == 3){ //- Aprovado
                            Animal animal = new AnimalDAO().busca(adocao.getAnimal().getId());
                            if(animal.getAnimalStatus().getId() != 3 && animal.getAnimalStatus().getId() != 4){ //- 3 = Adotado | 4 = Obito
                                animal.setDataAdocao(Utilidades.getDataAtual());
                                animal.getAnimalStatus().setId(3); //- Adotado
                                new AnimalDAO().atualiza(animal);

                                enviaEmail(adocao);
                            }
                        }

                        if(adocao.getArquivos() != null && !cadastraArquivos(adocao.getArquivos(),adocao.getId())){
                            sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                            return;
                        }

                        // RESPONSE CREATED
                        logger.info("Adoção " + adocao.getId() + " recebida!");
                        sendMessange(he, gson.toJson(adocao), STATUS_OK);
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

                    adocao = dao.busca(Integer.parseInt(parameters.get("id").get(0)));
                    if(adocao == null){
                        this.erro = new Erro("AD.03");
                        sendMessange(he, gson.toJson(this.erro), STATUS_ACCEPTED);
                        return;
                    }

                    adocao.setStatus(new AdocaoStatusDAO().busca(5));
                    if (parameters.get("observacao") != null)
                        adocao.setObservacao(parameters.get("observacao").get(0));
                    if (dao.atualiza(adocao)) {
                        sendMessange(he, gson.toJson(adocao), STATUS_OK);
                    } else {
                        this.erro = new Erro("AD.05");
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

    private void enviaEmail(Adocao adocao){
        new Thread(){
            @Override
            public void run(){
                Animal animal = new AnimalDAO().busca(adocao.getAnimal().getId());
                PessoaFisica tutor = new PessoaFisicaDAO().busca(adocao.getTutor().getId());
                List<AnimalTratamento> tratamentos = new AnimalTratamentoDAO().listaTratamentosAnimal(animal.getId(),1);

                if(tratamentos == null || tratamentos.size() <= 0){
                    return;
                }

                String html = "";
                html += "<!DOCTYPE html>";
                html += "<html>";
                html +=     "<head></head>";
                html +=     "<body style='margin:0;padding:0;'>";
                html +=         "<div style='width:600px;margin:auto;padding:50px 15px;'>";
                html +=             "<div style='width:100%;float:left;text-align:center;maring-bottom:30px;'>";
                html +=                 "<img src='http://apamalajeado.org/images/logo.png'>";
                html +=             "</div>";
                html +=             "<h2>Parabéns, você acaba de adotar "+animal.getNome()+"</h2>";
                html +=             "<p>Segue abaixo a relação de tratamentos que já foram realizados em seu novo PET</p>";
                html +=             "<table style='width:100%;text-align:left;' border='1'>";
                html +=                 "<thead>";
                html +=                     "<tr>";
                html +=                         "<th>Data</th>";
                html +=                         "<th>Tratamento</th>";
                html +=                         "<th>Observação</th>";
                html +=                     "</tr>";
                html +=                 "</thead>";
                html +=                 "<tbody>";
                for(AnimalTratamento tratamento : tratamentos){
                    html +=                 "<tr>";
                    html +=                     "<td>"+Formatacao.ajustaDataDMA(tratamento.getDataTratamento())+"</td>";
                    html +=                     "<td>"+tratamento.getTipoTratamento().getDescricao()+"</td>";
                    html +=                     "<td>"+tratamento.getObservacao()+"</td>";
                    html +=                 "</tr>";
                }
                html +=                 "</tbody>";
                html +=             "</table>";
                html +=         "</div>";
                html +=     "</body>";
                html += "</html>";

                Utilidades.enviarEmail("Informações sobre "+animal.getNome(),html,new PessoaDAO().getEmail(tutor.getId()));
            }
        }.start();
    }

    private boolean cadastraArquivos(List<Arquivo> arquivos, Integer idAdocao){
        ImagemDAO imagemDAO = new ImagemDAO();
        for(Arquivo arquivo : arquivos){
            if(arquivo == null){
                continue; //- Classe em branco, vai pro proximo arquivo
            }

            String nome = "AD"+idAdocao+"_" + UUID.randomUUID().toString() + Utilidades.getExtensionOfFile(arquivo.getNome());
            File file = Utilidades.getDecodedFile(arquivo.getArquivo(), nome);
            if(file == null || !file.exists()){
                this.erro = new Erro("AD.14");
                return false;
            }

            arquivo.setNome(nome);
            if(!imagemDAO.cadastra(arquivo)){
                this.erro = new Erro("A.12");
                return false;
            }

            if(!imagemDAO.cadastraAdocaoImagem(idAdocao, arquivo.getId())){
                this.erro = new Erro("AD.13");
                return false;
            }
        }

        return true;
    }

    private boolean validaDados(Adocao adocao){
        if(adocao == null){
            this.erro = new Erro("AD.06");
            return false;
        }else if(adocao.getAnimal() == null || adocao.getAnimal().getId() == null || adocao.getAnimal().getId() <= 0){
            this.erro = new Erro("A.06");
            return false;
        }/*else if(adocao.getAnimal().getAnimalStatus().getId() == 3 || adocao.getAnimal().getAnimalStatus().getId() == 4){
            this.erro = new Erro("AD.08");
            return false;
        }*/else if(adocao.getTutor() == null || adocao.getTutor().getId() == null || adocao.getTutor().getId() <= 0){
            this.erro = new Erro("P.06");
            return false;
        }else if(adocao.getTutor().getLiberarAdocao() != 1){
            this.erro = new Erro("AD.09");
            return false;
        }else if(adocao.getArquivos() != null && !validaArquivos(adocao.getArquivos())){
            this.erro = new Erro("A.11");
            return false;
        }else if(adocao.getUsuario() == null || adocao.getUsuario().getId() == null || adocao.getUsuario().getId() <= 0){
            this.erro = new Erro("V.06");
            return false;
        }else if(adocao.getStatus() == null || adocao.getStatus().getId() == null || adocao.getStatus().getId() <= 0){
            this.erro = new Erro("AS.06");
            return false;
        }else if(adocao.getStatus().getId() == 3 && (adocao.getDataAdocao() == null || !Validacao.validarData(adocao.getDataAdocao()))){
            this.erro = new Erro("AD.07");
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
}