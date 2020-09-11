package utilidades;

import dao.ConexaoDAO;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Utilidades {

    private static final Logger logger = LogManager.getLogger(Utilidades.class.getName());

    // Variaveis
    public static final String PERMISSAO_INSERIR = "Inserir";
    public static final String PERMISSAO_VISUALIZAR = "Visualizar";
    public static final String PERMISSAO_EDITAR = "Editar";
    public static final String PERMISSAO_EXCLUIR = "Excluir";

    private static TreeMap<String,String> mensagemErros;

    private Utilidades() {}

    private static Properties properties() {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("config.properties"));
        } catch (IOException e) {
            logger.catching(Level.ERROR, e);
        }
        return prop;
    }

    public static boolean testaConexao() {
        return ConexaoDAO.getInstance().getConnection() != null;
    }

    public static String getVariavel(String variavel) {
        return properties().getProperty(variavel);
    }

    public static String getDataAtual() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getMesAno(String data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date dt = null;
        try {
            dt = sdf.parse(data.replace("-", "/"));
        } catch (Exception e) {
            logger.catching(Level.ERROR, e);
        }
        sdf = new SimpleDateFormat("MM/yy");
        return sdf.format(dt).replace("/", "");
    }

    private static ConfiguracaoEmail ConfiguracaoEmail() {
        ConfiguracaoEmail e = new ConfiguracaoEmail();

        e.setLogin(getVariavel("mail.smtp.login"));
        e.setSenha(getVariavel("mail.smtp.password"));
        e.setPorta(getVariavel("mail.smtp.port"));
        e.setSmtp(getVariavel("mail.smtp.host"));
        e.setTpAmb(1);
        e.setRequerAutenticacao(true);
        e.setSsl(true);
        return e;
    }

    public static boolean enviarEmail(String assunto, String mensagem, String emailDestinatario) {
        boolean boo = false;

        SendMail send = new SendMail();
        //String emailDestinatario = Utilidades.getVariavel("mail.destinatarios");
        //String cco = Utilidades.getVariavel("mail.destinatarios.cco");
        String cco = "";
        String resp = "";
        HashMap anexos = null;
        ConfiguracaoEmail conf;

        try {
            conf = Utilidades.ConfiguracaoEmail();
            send.sendMail(conf, assunto, mensagem, emailDestinatario, cco, resp, anexos);

            boo = true;
        } catch (Exception e) {
            logger.error("Erro EnviaEmailAlerta.enviaEmailAlerta()");
            logger.catching(Level.ERROR, e);
        }

        return boo;
    }

    public static String montaQuery(Map<String, List<String>> map) {
        StringJoiner joiner = new StringJoiner(" AND ");

        map.get("id");
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            joiner.add(entry.getKey() + "=" + entry.getValue().get(0));
            logger.info("Key: " + entry.getKey() + ", Value: " + entry.getValue().get(0));
        }

        return joiner.toString();
    }

    public static String gerarToken() {
        byte[] randomBytes = new byte[24];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().encodeToString(randomBytes);
    }

    private static String byteParaHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public static String criptografaSenha(String senha) {
        String sha1 = "";
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(senha.getBytes(StandardCharsets.UTF_8));
            sha1 = byteParaHex(crypt.digest());
        } catch (Exception e) {
            logger.catching(Level.ERROR, e);
        }

        return sha1.toUpperCase();
    }

    public static String somenteNumeros(String str) {
        return str.replaceAll("[^\\d]", "");
    }

    public static String formataCpf(String cpf) {
        StringBuilder retorno = new StringBuilder();
        for (int i = 0; i < cpf.length(); i++) {
            retorno.append(cpf.charAt(i));
            if (i == 2 || i == 5) {
                retorno.append(".");
            } else if (i == 8) {
                retorno.append("-");
            }
        }
        return retorno.toString();
    }

    public static void setErro(){
        mensagemErros = new TreeMap();

        //- Erros genericos
        mensagemErros.put("EG.01","Não foi possível carregar os dados no servidor");

        //- Animal
        mensagemErros.put("A.01","Erro ao cadastrar animal");
        mensagemErros.put("A.02","Erro ao atualizar animal");
        mensagemErros.put("A.03","Erro ao buscar animal");
        mensagemErros.put("A.04","Erro ao listar animais");
        mensagemErros.put("A.05","Erro ao remover animal");
        mensagemErros.put("A.06","Erro ao identificar os dados do animal");
        mensagemErros.put("A.07","Erro ao identificar o endereço do animal");
        mensagemErros.put("A.10","Dados do animal inválidos ou incompletos. Verifique o formulário");
        mensagemErros.put("A.11","Erro ao inderificar o(s) nome(s) do(s) arquivo(s)");
        mensagemErros.put("A.12","Erro ao salvar um ou mais arquivos");
        mensagemErros.put("A.13","Erro ao vincular arquivo com o animal");
        mensagemErros.put("A.14","Erro ao salvar em disco o(s) arquivo(s) do animal");
        mensagemErros.put("A.15","Erro ao atualizar os arquivos do animal");

        //- Adocao
        mensagemErros.put("AD.01","Erro ao cadastrar adoção");
        mensagemErros.put("AD.02","Erro ao atualizar adoção");
        mensagemErros.put("AD.03","Erro ao buscar adoção");
        mensagemErros.put("AD.04","Erro ao listar adoções");
        mensagemErros.put("AD.05","Erro ao remover adoção");
        mensagemErros.put("AD.06","Erro ao identificar os dados da adoção");
        mensagemErros.put("AD.07","Data de adoção inválida");
        mensagemErros.put("AD.08","Este animal não está disponível para adoção");
        mensagemErros.put("AD.09","Este tudor não está liberado para realizar adoções");
        mensagemErros.put("AD.10","Dados da adoção inválidos ou incompletos. Verifique o formulário");
        mensagemErros.put("AD.13","Erro ao vincular arquivo com a adoção");
        mensagemErros.put("AD.14","Erro ao salvar em disco o(s) arquivo(s) da adoção");

        //- Adocao Status
        mensagemErros.put("AS.06","Erro ao identificar os dados do status da adoção");

        //- Contato
        mensagemErros.put("C.01","Erro ao cadastrar contato");
        mensagemErros.put("C.02","Erro ao atualizar contato");
        mensagemErros.put("C.03","Erro ao buscar contato");
        mensagemErros.put("C.04","Erro ao listar contatos");
        mensagemErros.put("C.05","Erro ao remover contato");
        mensagemErros.put("C.06","Um ou mais contatos inválidos");

        //- Doacao
        mensagemErros.put("D.01","Erro ao cadastrar doação");
        mensagemErros.put("D.02","Erro ao atualizar doação");
        mensagemErros.put("D.03","Erro ao buscar doação");
        mensagemErros.put("D.04","Erro ao listar doações");
        mensagemErros.put("D.05","Erro ao remover doação");
        mensagemErros.put("D.06","Erro ao identificar os dados da doação");
        mensagemErros.put("D.07","Erro ao identificar os dados do doador");
        mensagemErros.put("D.08","Erro ao identificar o tipo de doação");
        mensagemErros.put("D.10","Dados da doação inválidos ou incompletos. Verifique o formulário");

        //- Endereço
        mensagemErros.put("E.01","Erro ao cadastrar endereço");
        mensagemErros.put("E.02","Erro ao atualizar endereço");
        mensagemErros.put("E.03","Erro ao buscar endereço");
        mensagemErros.put("E.04","Erro ao listar endereços");
        mensagemErros.put("E.05","Erro ao remover endereço");
        mensagemErros.put("E.10","Dados de endereço inválidos ou incompletos. Verifique o formulário");

        //- Endereco do animal
        mensagemErros.put("EA.01","Erro ao vincular endereço com o animal");

        //- Endereco da pessoa
        mensagemErros.put("EP.01","Erro ao vincular endereço com a pessoa");

        //- Endereco do Voluntário
        mensagemErros.put("EV.01","Erro ao vincular endereço com o usuário");

        //- Pessoa
        mensagemErros.put("P.01","Erro ao cadastrar pessoa");
        mensagemErros.put("P.02","Erro ao atualizar pessoa");
        mensagemErros.put("P.03","Erro ao buscar pessoa");
        mensagemErros.put("P.04","Erro ao listar pessoas");
        mensagemErros.put("P.05","Erro ao remover pessoa");
        mensagemErros.put("P.06","Erro ao identificar os dados da pessoa");
        mensagemErros.put("P.07","Erro ao identificar o endereço da pessoa");
        mensagemErros.put("P.08","Erro ao identificar contato(s) da pessoa. Informe ao menos uma forma de contato");
        mensagemErros.put("P.10","Dados da pessoa inválidos ou incompletos. Verifique o formulário");

        //- Pessoa Física
        mensagemErros.put("PF.01","CPF inválido");

        //- Pessoa Jurídica
        mensagemErros.put("PJ.01","CNPJ inválido");

        //- Tratamento
        mensagemErros.put("T.01","Erro ao cadastrar tratamento");
        mensagemErros.put("T.02","Erro ao atualizar tratamento");
        mensagemErros.put("T.03","Erro ao buscar tratamento");
        mensagemErros.put("T.04","Erro ao listar tratamento");
        mensagemErros.put("T.05","Erro ao remover tratamento");
        mensagemErros.put("T.06","Erro ao identificar os dados do tratamento");
        mensagemErros.put("T.10","Dados do tratamento inválidos ou incompletos. Verifique o formulário");

        //- Tipo Doação
        mensagemErros.put("TD.01","Erro ao cadastrar tipo de doação");
        mensagemErros.put("TD.02","Erro ao atualizar tipo de doação");
        mensagemErros.put("TD.03","Erro ao buscar tipo de doação");
        mensagemErros.put("TD.04","Erro ao listar tipos de dação");
        mensagemErros.put("TD.05","Erro ao remover tipo de doação");
        mensagemErros.put("TD.06","Erro ao identificar os dados do tipo de doação");
        mensagemErros.put("TD.10","Dados do tipo de doação inválidos ou incompletos. Verifique o formulário");

        //- Tipo de Tratamento
        mensagemErros.put("TT.01","Erro ao cadastrar tipo de tratamento");
        mensagemErros.put("TT.02","Erro ao atualizar tipo de tratamento");
        mensagemErros.put("TT.03","Erro ao buscar tipo de tratamento");
        mensagemErros.put("TT.04","Erro ao listar tipo de tratamento");
        mensagemErros.put("TT.05","Erro ao remover tipo de tratamento");
        mensagemErros.put("TT.06","Erro ao identificar os dados do tipo de tratamento");
        mensagemErros.put("TT.10","Dados do tipo de tratamento inválidos ou incompletos. Verifique o formulário");

        //- Voluntário
        mensagemErros.put("V.01","Erro ao cadastrar voluntário");
        mensagemErros.put("V.02","Erro ao atualizar voluntário");
        mensagemErros.put("V.03","Erro ao buscar voluntário");
        mensagemErros.put("V.04","Erro ao listar voluntários");
        mensagemErros.put("V.05","Erro ao remover voluntário");
        mensagemErros.put("V.06","Erro ao identificar os dados do voluntário");
        mensagemErros.put("V.07","Erro ao identificar o endereço do voluntário");
        mensagemErros.put("V.08","Erro ao identificar contato(s) do voluntário");
        mensagemErros.put("V.09","O nome de usuário já está cadastrado, escolha outro");
        mensagemErros.put("V.10","Dados de usuário inválidos ou incompletos. Verifique o formulário");
    }

    public static String getErro(String codigo){
        if(mensagemErros == null){
            setErro();
        }

        if(!mensagemErros.containsKey(codigo)){
            return "Erro desconhecido ("+codigo+"). Contate o suporte";
        }

        return mensagemErros.get(codigo);
    }

    public static String getEncodedFile(String origin) {
        String encodedFile = "";

        try {
            File file = new File(origin);
            if(file.exists() && !file.isDirectory()){
                BufferedImage img = ImageIO.read(file);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//              ImageIO.write(img, file.getName().split("\\.")[1], baos);
                ImageIO.write(img, file.getName().substring(file.getName().lastIndexOf(".") + 1), baos);
                baos.flush();
                encodedFile = Base64.getEncoder().encodeToString(baos.toByteArray());
                baos.close();
                encodedFile = java.net.URLEncoder.encode(encodedFile, "ISO-8859-1");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return encodedFile;
    }

    public static File getDecodedFile(String fileEncoded, String fileName) {
        File file = null;

        try {
            // cria o arquivo na pasta padrao do sistema
            File folder = new File(Utilidades.getVariavel("var.folder"));
            if (!folder.exists()) folder.mkdir();
            file = new File(folder, fileName);

            // decodifica o arquivo
            fileEncoded = java.net.URLDecoder.decode(fileEncoded, "ISO-8859-1");
            byte[] fileAsBytes = Base64.getDecoder().decode(fileEncoded);

            // salva imagem no arquivo criado
            FileOutputStream os = new FileOutputStream(file, false);
            os.write(fileAsBytes);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }

    public static void resizeImage(File file) {

        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
            ImageWriteParam iwp = writer.getDefaultWriteParam();
            iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            iwp.setCompressionQuality(0.7f);

//            File file = new File(...);
            FileImageOutputStream output = new FileImageOutputStream(file);

            writer.setOutput(output);
            IIOImage image = new IIOImage(bufferedImage, null, null);
            writer.write(null, image, iwp);
            writer.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getExtensionOfFile(String nome) {
        return "." + nome.substring(nome.lastIndexOf(".") + 1);
    }

    /**
     * Para utilizar na hora de inserir os dados no banco
     * @param data
     * @return
     */
    public static java.sql.Date converteString2Date(String data){
        if(data == null || data.trim().isEmpty() || data.trim().length() != 10){
            return null;
        }

        try {
            return new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(data).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
