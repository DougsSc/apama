package utilidades;

import entidades.Cidade;
import entidades.Endereco;
import entidades.Erro;
import org.json.JSONObject;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Properties;

public class Utilidades {

    private Utilidades() {}

    private static Properties properties() {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }
    
    public static String getVariavel(String variavel) {
        return properties().getProperty(variavel);
    }

    public static String somenteNumeros(String str){
        return str.replaceAll("[^\\d]","");
    }
    
    public static String getDataAtual() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return dateFormat.format(date); 
    }

    public static String getExtensionOfFile(String nome) {
        return "." + nome.substring(nome.lastIndexOf(".") + 1);
    }

    public static String encodeFileToBase64Binary(File file) throws IOException {
        byte[] bytes = loadFile(file);
        byte[] encoded = Base64.getEncoder().encode(bytes);

        String encodedFile = new String(encoded, StandardCharsets.ISO_8859_1);
        return java.net.URLEncoder.encode(encodedFile, "ISO-8859-1");
    }

    private static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int)length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }

        is.close();
        return bytes;
    }

    public static String getEncodedFile(File file) {
        String encodedFile = "";

        try {
//            File file = new File(origin);
            BufferedImage img = ImageIO.read(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            ImageIO.write(img, file.getName().split("\\.")[1], baos);
            ImageIO.write(img, getExtensionOfFile(file.getName()), baos);
            baos.flush();
            encodedFile = Base64.getEncoder().encodeToString(baos.toByteArray());
            baos.close();
            encodedFile = java.net.URLEncoder.encode(encodedFile, "ISO-8859-1");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return encodedFile;
    }

    public static File getDecodedFile(String fileEncoded, String fileName) {
        File file = null;

        try {
            //File folder = new File("C:\\Users\\Christian F. Kroth\\Desktop");
            //if (!folder.exists())
            //   folder.mkdir();

//            file = new File(folder + fileName);
            //file = File.createTempFile("Temp_", getExtensionOfFile(fileName), folder);
            file = File.createTempFile("Temp_", getExtensionOfFile(fileName));

            FileOutputStream os = new FileOutputStream(file, false);
            fileEncoded = java.net.URLDecoder.decode(fileEncoded, "ISO-8859-1");
            byte[] fileAsBytes = Base64.getDecoder().decode(fileEncoded);
            os.write(fileAsBytes);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }

    public static File getTempFile(String fileEncoded, String fileName) {
        File file = null;

        try {
            file = File.createTempFile("Temp_", getExtensionOfFile(fileName));
            file.deleteOnExit();

            FileOutputStream os = new FileOutputStream(file, false);
            fileEncoded = java.net.URLDecoder.decode(fileEncoded, "ISO-8859-1");
            byte[] pdfAsBytes = Base64.getDecoder().decode(fileEncoded);
            os.write(pdfAsBytes);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public static String semAcentos(String s) {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]+", "");
        return s;
    }

    public static Endereco getEnderecoByViaCep(String cep) {
        Endereco endereco = null;

        cep = cep.replaceAll("-", "");
        try {
            URL url = new URL("https://viacep.com.br/ws/" + cep + "/json/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(false);
            connection.setConnectTimeout(10000);
            connection.connect();

            StringBuilder resposta = new StringBuilder();

            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            while ((line = br.readLine()) != null)
                resposta.append(line);

            JSONObject jsonObj = new JSONObject(resposta.toString());

            endereco = new Endereco();

            endereco.setCep(jsonObj.getString("cep"));
            endereco.setCidade(Utilidades.getCidadeByName(jsonObj.getString("localidade")));
            endereco.setBairro(jsonObj.getString("bairro"));
            endereco.setLogradouro(jsonObj.getString("logradouro"));

        } catch (Exception e) {
            e.printStackTrace();
            Utilidades.showAlerta(null,"Não foi possível consultar o CEP. Verifique o CEP ou sua conexão com a internet.");
        }

        return endereco;
    }

    public static Cidade getCidadeByName(String nome) {
        Cidade cidade = null;
        if (nome != null && !nome.isEmpty()) {
            for (Cidade c : Sessao.shared().getCidades()) {
                if (semAcentos(c.getDescricao()).equalsIgnoreCase(semAcentos(nome))) {
                    cidade = c;
                }
            }
        }

        return cidade;
    }

    public static void showErro(Component parent, Erro erro) {
        JOptionPane.showMessageDialog(parent, erro.getMensagem(), "ERRO "+erro.getCodigoErro(), JOptionPane.ERROR_MESSAGE , new ImageIcon(System.getProperty("user.dir")+"/src/main/resources/imagens/erro.png"));
    }

    public static void showAlerta(Component parentComponent, String msg){
        JOptionPane.showMessageDialog(parentComponent, msg,"ALERTA", JOptionPane.PLAIN_MESSAGE, new ImageIcon(System.getProperty("user.dir")+"/src/main/resources/imagens/alerta.png"));
    }

    public static void showSucesso(Component parentComponent, String msg){
        JOptionPane.showMessageDialog(parentComponent, msg,"SUCESSO", JOptionPane.PLAIN_MESSAGE, new ImageIcon(System.getProperty("user.dir")+"/src/main/resources/imagens/sucesso.png"));
    }

    public static void showCloseAlert(Dialog parent) {
        int i = JOptionPane.showConfirmDialog(parent, "Deseja sair sem salvar?", "Sair", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, new ImageIcon(System.getProperty("user.dir")+"/src/main/resources/imagens/alerta.png"));
        if (i == JOptionPane.YES_OPTION) parent.dispose();
//        JOptionPane.showConfirmDialog(parent, "Deseja sair sem salvar?", "Sair", JOptionPane.YES_NO_OPTION);
    }

    public static boolean isPessoaFisica(String json) {
        System.out.println(json);
        JSONObject jsonObj = new JSONObject(json);

        try {
            String cpf = jsonObj.getString("cpf");
            if (cpf != null && !cpf.isEmpty()) return true;
        } catch (Exception igonred) {}

        return false;
    }

    public static BufferedImage arredondaBordas(Image image, int cornerRadius) {
        //int w = image.getWidth(null);
        //int h = image.getHeight(null);
        int w = 100;
        int h = 100;
        BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = output.createGraphics();
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);

        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(image, 0, 0, null);

        g2.dispose();

        return output;
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

    public static BufferedImage resize(BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }
}
