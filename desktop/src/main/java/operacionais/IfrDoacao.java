package operacionais;

import cadastros.DfrTutorDoador;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import componentes.*;
import entidades.*;
import org.json.JSONArray;
import org.json.JSONObject;
import utilidades.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

public class IfrDoacao extends JInternalFrame {
    // Variavel de controle

    private ArrayList<Doacao> doacoes;
    private Doacao doacao;

    private boolean reload;

    private Toolkit tk = Toolkit.getDefaultToolkit();
    private Dimension d = tk.getScreenSize();

    private JTextField jBusca = new JTextField();

    private JLabel jlBusca = new JLabel();
    private JLabel jCabecalho = new JLabel();
    private JLabel jLogoCabecalho = new JLabel();

    private JButton btnNovo = new JButton();
    private JButton btnSair = new JButton();
    private JButton btnBusca = new JButton();
    private JButton btnEditar = new JButton();
    private JButton btnExcluir = new JButton();
    private JButton btnVisualizar = new JButton();
    private JButton btnRelatorio = new JButton();

    private JTable jTabela = new JTable();

    private JPanel PanelFundoCadastro = new JPanel();
    private JPanel PanelCabecalho = new JPanel(new BorderLayout());
    private JPanel PanelMenu = new JPanel(new BorderLayout());

    FrConfig config = new FrConfig();
    ComponentTable table = new ComponentTable();
    private ComponentLabel lbl = new ComponentLabel();

    public IfrDoacao() {
        jCabecalho.setText("Doação");
        JLabel ImgLogo = new JLabel();
        lbl.labelDoacao(ImgLogo);
        PanelCabecalho.add(ImgLogo, BorderLayout.NORTH);
        btnBusca.addActionListener(e -> busca());

        setPosicao();
        setResizable(false);
        defineComponentes();
        focus();
        defineAcoes();
        carregaDados();
//        System.out.println("IfrDoacaoFinal");
    }

    private void defineAcoes() {
        // Listenner da seleção
        jTabela.getSelectionModel().addListSelectionListener(event -> {
            try {
                doacao = doacoes.get(jTabela.getSelectedRow());
            } catch (Exception igoner) {}
        });

        // Listenner os dois cliques
        jTabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) editar();
            }
        });

        // Nova Doação
        btnNovo.addActionListener(e -> {
            DfrDoacao dfrdoacao = new DfrDoacao(null, true, 1, doacao);
            dfrdoacao.setVisible(true);
        });

        // Edita doação
        btnEditar.addActionListener(e -> {
            editar();
        });

        // Visualiza Doacao
        btnVisualizar.addActionListener(e -> {
            DfrDoacao dfrdoacao = new DfrDoacao(null, true, 2, doacao);
            dfrdoacao.setVisible(true);
        });

        btnRelatorio.addActionListener(e -> {

            DfrDoacaoRelatorio relatorioAnimal = new DfrDoacaoRelatorio(null, true, doacao, this);
            relatorioAnimal.setVisible(true);
        });

        btnExcluir.addActionListener(e -> excluir());

        // Fecha a tela
        btnSair.addActionListener(e -> {
            setVisible(false);
            dispose();
        });
    }

    public void setPosicao() {
        config.ajusteInterFrame(this);
    }

    // Personaliza todos os componentes da tela
    public void defineComponentes() {
        Dimension f = this.getSize();

        getRootPane().setDefaultButton(btnBusca);

        //Ajuste os Botões
        ComponentButton button = new ComponentButton();

        button.botaoNovo(btnNovo);
        button.botaoSair(btnSair);
        button.botaoBuscaIFR(btnBusca);
        button.botaoEditar(btnEditar);
        button.botaoExcluir(btnExcluir);
        button.botaoVisualizar(btnVisualizar);
        button.botaoRelatorio(btnRelatorio);

        //Ajusta Campo Texto
        ComponentText text = new ComponentText();
        text.formatarBusca(jlBusca, jBusca);
        jBusca.setSize((d.width - (d.width / 7)) - 90, 20);

        // Cria o fundo cinza
        getContentPane().add(PanelFundoCadastro, BorderLayout.CENTER);
        config.ajusteFundo(PanelFundoCadastro);

        // Adiciona o cabecalho
        PanelFundoCadastro.add(PanelCabecalho, BorderLayout.NORTH);
        config.ajusteCabecalho(PanelCabecalho);
        PanelCabecalho.setSize(f.width * 2, f.height / 6);
        PanelCabecalho.add(jCabecalho, BorderLayout.NORTH);
        config.ajusteCabecalho(jCabecalho);
        jCabecalho.setSize(500, f.height / 7);
        PanelCabecalho.add(jLogoCabecalho, BorderLayout.NORTH);
        jLogoCabecalho.setSize(200, f.height / 7);

        //Adiciona Menu
        PanelFundoCadastro.add(PanelMenu, BorderLayout.NORTH);
        config.ajusteMenuLateral(PanelMenu);

        //Ajusta o local dos campo busca
        PanelFundoCadastro.add(jBusca, BorderLayout.NORTH);
        PanelFundoCadastro.add(jlBusca, BorderLayout.NORTH);


        //Adiciona os Botões
        PanelFundoCadastro.add(btnBusca, BorderLayout.NORTH);
        PanelMenu.add(btnVisualizar, BorderLayout.CENTER);
        PanelMenu.add(btnNovo, BorderLayout.CENTER);
        PanelMenu.add(btnEditar, BorderLayout.CENTER);
        PanelMenu.add(btnExcluir, BorderLayout.CENTER);
        PanelMenu.add(btnRelatorio, BorderLayout.CENTER);
        PanelMenu.add(btnSair, BorderLayout.CENTER);

        //Adiciona a Tabela
        PanelFundoCadastro.add(jTabela, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(jTabela);
        PanelFundoCadastro.add(scrollPane, BorderLayout.NORTH);
        table.ConfigTabelaPrincipal(jTabela, scrollPane);

        scrollPane.getVerticalScrollBar().addAdjustmentListener(ae -> {
            int extent = scrollPane.getVerticalScrollBar().getModel().getExtent();
            int value = scrollPane.getVerticalScrollBar().getValue();

            if (reload) {
                reload = false;
                return;
            }

            if ((value + extent) == scrollPane.getVerticalScrollBar().getMaximum())
                carregaDados();
        });


    }

    public void focus() {
        final Focus focus = new Focus();
        getRootPane().setDefaultButton(btnBusca);

        jBusca.addFocusListener(new FocusAdapter() {

            public void focusGained(FocusEvent evt) {
                focus.focusBuscaGained(jlBusca, jBusca);
            }

            public void focusLost(FocusEvent evt) {
                focus.focusBuscaLost(jlBusca, jBusca);
            }

        });
    }

    public void carregaDados() {
        ServicoCliente sv = new ServicoCliente();
        String json = sv.buscaDados("doacao", ServicoCliente.GET, "", ServicoCliente.EMPTY_PARAMS);

        System.out.println(json);
        Gson gson = new Gson();

        HashMap<String, String> map = Sessao.shared().getMapLO(this);
        JSONArray retornoArray = new JSONArray(sv.buscaDados("doacao", ServicoCliente.GET, "", map));
        doacoes = new ArrayList<>();
        try {
            for (int i = 0; i < retornoArray.length(); i++) {
                JSONObject obj = retornoArray.getJSONObject(i);
                System.out.println("Object: " + obj.toString());
                Doacao doacao = gson.fromJson(obj.toString(), Doacao.class);
                doacao.setPessoaFisica(null);
                doacao.setPessoaJuridica(null);

                if (obj.has("cpf")) {
                    PessoaFisica pf = gson.fromJson(obj.toString(), PessoaFisica.class);
                    doacao.setPessoaFisica(pf);
                } else {
                    PessoaJuridica pj = gson.fromJson(obj.toString(), PessoaJuridica.class);
                    doacao.setPessoaJuridica(pj);
                }

                doacoes.add(doacao);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Size " + doacoes.size());

        reload = true;
        PopularTabela.doacoes(jTabela, doacoes);
        jTabela.invalidate();
    }

    public void editar() {
        if (jTabela.getSelectedRowCount() == 0) {
            Utilidades.showAlerta(this, "Por favor, selecione uma Doação!");
        } else {
            DfrDoacao dfrdoacao = new DfrDoacao(this, true, 3, doacao);
            dfrdoacao.setVisible(true);
        }
    }

    public void busca() {

    }

    public void excluir() {

        if (jTabela.getSelectedRowCount() == 0) {
            Utilidades.showAlerta(this, "Por favor, selecione uma Doação!");
        } else {
            ImageIcon imagem = new ImageIcon(System.getProperty("user.dir")+"/src/main/resources/imagens/alerta.png");

            int i = JOptionPane.showConfirmDialog(
                    IfrDoacao.this,
                    "Deseja deletar este cadastro?","Excluir",JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE, imagem
            );
            if (i == JOptionPane.YES_OPTION) {
                ServicoCliente sv = new ServicoCliente();

                HashMap<String, String> params = new HashMap<>();
                params.put("id", doacao.getId().toString());

                if (sv.enviaDados("doacao", ServicoCliente.DELETE, "", params, Erro.class)) {
                    System.out.println("Doação " + doacao.getId() + " excluída!");
                    resetaDados();
                } else {
                    Utilidades.showAlerta(this, sv.getObject().toString());
                }
            }
        }
    }

    public void resetaDados() {
        doacao = null;
        Sessao.shared().resetOffset();
        doacoes.clear();
        carregaDados();
    }
}
