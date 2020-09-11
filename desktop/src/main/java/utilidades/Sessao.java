package utilidades;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import entidades.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Sessao {

    // Variaveis de sessao
    private Usuario usuarioLogado;

    private static final Integer LIMIT = 20;
    private Integer offset;
    private Component currentFrame;
    private HashMap<String, String> paramsLO;

    private ArrayList<Cidade> cidades;
    private ArrayList<TipoDoacao> tiposDoacao;

    private ArrayList<Object> doadores;

    private Sessao() {}

    private static Sessao INSTANCIA = new Sessao();

    public static Sessao shared() {
        if (INSTANCIA == null) {
            INSTANCIA = new Sessao();
        }
        return INSTANCIA;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }

    public ArrayList<Cidade> getCidades() {
        if (cidades == null) {
            ServicoCliente sv = new ServicoCliente();
            String json = sv.buscaDados("cidade", ServicoCliente.GET, "", ServicoCliente.EMPTY_PARAMS);
            cidades = new Gson().fromJson(json, new TypeToken<ArrayList<Cidade>>() {
            }.getType());
        }
        return cidades;
    }

    public ArrayList<TipoDoacao> getTiposDoacao(boolean atualizar) {
        if (tiposDoacao == null || atualizar) {
            ServicoCliente sv = new ServicoCliente();
            String json = sv.buscaDados("tipoDoacao", ServicoCliente.GET, "", ServicoCliente.EMPTY_PARAMS);
            tiposDoacao = new Gson().fromJson(json, new TypeToken<ArrayList<TipoDoacao>>() {
            }.getType());
        }

        return tiposDoacao;
    }

    public ArrayList<Object> getTutorDoador(Component parent, boolean atualizar) {
        if (doadores == null || atualizar) {
            ServicoCliente sv = new ServicoCliente();
            Gson gson = new Gson();
            doadores = new ArrayList<>();

            HashMap<String, String> map = Sessao.shared().getMapLO(parent);
            JSONArray retornoArray = new JSONArray(sv.buscaDados("tutorDoador", ServicoCliente.GET, "", map));
            try {
                for (int i = 0; i < retornoArray.length(); i++) {
                    JSONObject obj = retornoArray.getJSONObject(i);
                    if (obj.has("cpf")) {
                        PessoaFisica pf = gson.fromJson(obj.toString(), PessoaFisica.class);
                        doadores.add(pf);
                    } else {
                        PessoaJuridica pj = gson.fromJson(obj.toString(), PessoaJuridica.class);
                        doadores.add(pj);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return doadores;
    }

    public void resetOffset() {
        this.offset = (LIMIT * -1);
    }

    public HashMap<String, String> getMapLO(Component current) {

        if (currentFrame == current) {
            offset += LIMIT;
        }

        if (paramsLO == null || paramsLO.isEmpty() || currentFrame != current) {
            currentFrame = current;
            paramsLO = new HashMap<>();
            offset = 0;
        }

        paramsLO.put("limit", LIMIT.toString());
        paramsLO.put("offset", offset.toString());

        System.out.println(LIMIT + " - " + offset);

        return paramsLO;
    }
}
