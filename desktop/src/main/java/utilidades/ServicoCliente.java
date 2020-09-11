package utilidades;

import com.google.gson.Gson;
import entidades.Erro;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class ServicoCliente {

    private Object object;

    public static final String POST = "POST";     // Criacao de item(ns)
    public static final String GET = "GET";       // Listagem completa ou busca por item
    public static final String PUT = "PUT";       // Atualizacao completa do item
    public static final String DELETE = "DELETE"; // Ocultacao de um item especifico

    public static final HashMap<String, String> EMPTY_PARAMS = new HashMap<>();

    public Object getObject() {
        return object;
    }

    public <T> boolean enviaDados(String path, String method, String json, HashMap<String, String> params, Class<T> tClass) {
        boolean sucesso = false;

        System.out.println("Enviando dados...");

        StringBuilder response = new StringBuilder();
        HttpURLConnection conn = null;
        BufferedWriter writer = null;

        AllowSSL.allowAllSSL();
        try {
            URL url = new URL(Utilidades.getVariavel("con.url") + path + getDataString(params));

            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestProperty("Token", Sessao.shared().getUsuarioLogado().getToken());
            conn.setRequestProperty("Content-type", "application/json");
            String basicAuth = Base64.getEncoder().encodeToString(("admin:admin").getBytes(StandardCharsets.UTF_8));
            conn.setRequestProperty("Authorization", "Basic " + basicAuth);

            conn.setDoInput(true);
            conn.setRequestMethod(method);

            if (json != null && !json.isEmpty()) {
                conn.setDoOutput(true);
                writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8));
                writer.write(json);
                writer.flush();
                writer.close();
            }

            conn.connect();

            int responseCode = conn.getResponseCode();

            if (!isErro(responseCode)) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                while ((line = br.readLine()) != null)
                    response.append(line);

                Gson gson = new Gson();
                if(responseCode == HttpsURLConnection.HTTP_ACCEPTED){
                    this.object = gson.fromJson(response.toString(), Erro.class);
                }else{
                    this.object = gson.fromJson(response.toString(), tClass);
                    sucesso = true;
                }
            } else {
                this.object = new Erro("SV.02", "Erro de comunicação com o servidor!");
            }

        } catch (Exception e) {
            this.object = new Erro("SV.01", "Erro de comunicação com o servidor!");
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) writer.close();
                if (conn != null) conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sucesso;
    }

    public String buscaDados(String path, String method, String json, HashMap<String, String> params) {
        return buscaDados(path, method, json, params, false);
    }

    public String buscaDados(String path, String method, String json, HashMap<String, String> params, boolean isLogin) {
        StringBuilder response = new StringBuilder();
        HttpURLConnection conn = null;
        BufferedWriter writer = null;

        AllowSSL.allowAllSSL();
        try {
            URL url = new URL(Utilidades.getVariavel("con.url") + path + getDataString(params));
            String basicAuth = Base64.getEncoder().encodeToString(("admin:admin").getBytes(StandardCharsets.UTF_8));

            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestProperty("Content-type", "application/json");
            conn.setRequestProperty("Authorization", "Basic " + basicAuth);
            if (!isLogin)
                conn.setRequestProperty("Token", Sessao.shared().getUsuarioLogado().getToken());

            conn.setDoInput(true);
            conn.setRequestMethod(method);

            if (json != null && !json.isEmpty()) {
                conn.setDoOutput(true);
                writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8));
                writer.write(json);
                writer.flush();
                writer.close();
            }

            conn.connect();

            int responseCode = conn.getResponseCode();

            if (responseCode != HttpsURLConnection.HTTP_UNAUTHORIZED) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                while ((line = br.readLine()) != null)
                    response.append(line);

            } else {
                this.object = new Erro("SV.02", "Erro de comunicação com o servidor!");
            }

        } catch (Exception e) {
            this.object = new Erro("SV.01", "Erro de comunicação com o servidor!");
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) writer.close();
                if (conn != null) conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return response.toString();
    }

    private static String getDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first) {
                result.append("?");
                first = false;
            } else {
                result.append("&");
            }

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    public boolean isErro(int code){
        System.out.println(code);
        if(
            code == HttpsURLConnection.HTTP_BAD_REQUEST ||
            code == HttpsURLConnection.HTTP_UNAUTHORIZED ||
            code == HttpsURLConnection.HTTP_FORBIDDEN ||
            code == HttpsURLConnection.HTTP_NOT_FOUND ||
            code == HttpsURLConnection.HTTP_INTERNAL_ERROR
        ){
            return true;
        }
        return false;
    }

}
