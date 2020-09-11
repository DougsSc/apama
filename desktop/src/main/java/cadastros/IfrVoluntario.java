package cadastros;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import componentes.*;
import entidades.*;
import principais.DfrBusca;
import utilidades.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

public class IfrVoluntario extends javax.swing.JInternalFrame {

    // Variavel de controle
    public ArrayList<Usuario> usuarios;
    private Usuario usuario;

    private boolean reload; // Controle do reload da tabela

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

    private JTable jTabela = new JTable();

    private JPanel PanelFundoCadastro = new JPanel();
    private JPanel PanelCabecalho = new JPanel(new BorderLayout());
    private JPanel PanelMenu = new JPanel(new BorderLayout());

    FrConfig config = new FrConfig();
    ComponentTable table = new ComponentTable();
    private ComponentLabel lbl = new ComponentLabel();

    public IfrVoluntario() {
        usuarios = new ArrayList<>();
        jCabecalho.setText("Voluntário");
        JLabel ImgLogo = new JLabel();
        lbl.labelVoluntario(ImgLogo);
        PanelCabecalho.add(ImgLogo, BorderLayout.NORTH);
        btnBusca.addActionListener(e -> busca());

        setPosicao();
        setResizable(false);
        defineComponentes();
        focus();
        defineAcoes();
        carregaDados();
//        System.out.println("IfrVoluntarioFinal");
    }

    private void defineAcoes() {

        // Listenner da seleção
        jTabela.getSelectionModel().addListSelectionListener(event -> {
            try {
                usuario = usuarios.get(jTabela.getSelectedRow());
            } catch (Exception igoner) {
            }
        });

        // Listenner os dois cliques
        jTabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editar();
                }
            }
        });

        // Novo Voluntário
        btnNovo.addActionListener(e -> {
            DfrVoluntario voluntario = new DfrVoluntario(null, true, 1, usuario, this);
            voluntario.setVisible(true);
        });

        // Edita Voluntário
        btnEditar.addActionListener(e -> {
            if (usuario != null) {
                editar();
            } else {
                Utilidades.showAlerta(this, "Por favor, selecione um Voluntário!");
            }
        });

        // Visualiza Voluntário
        btnVisualizar.addActionListener(e -> {
            if (usuario != null) {
                DfrVoluntario verVoluntario = new DfrVoluntario(null, true, 2, usuario, this); // aqui tem que passar o id tabela
                verVoluntario.setVisible(true);
            } else {
                Utilidades.showAlerta(this, "Por favor, selecione um Voluntário!");
            }
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
        PanelCabecalho.setSize(f.width * 2, f.height / 7);
        PanelCabecalho.add(jCabecalho, BorderLayout.NORTH);
        config.ajusteCabecalho(jCabecalho);
        jCabecalho.setSize(500, f.height / 7);

        //Adiciona Arquivo Cabecalho
        JLabel ImgLogo = new JLabel();
        lbl.labelVoluntario(ImgLogo);
        PanelCabecalho.add(ImgLogo, BorderLayout.CENTER);


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

            System.out.println("Value: " + (scrollPane.getVerticalScrollBar().getValue() + extent) + " Max: " + scrollPane.getVerticalScrollBar().getMaximum());
//            System.out.println(jTabela.getHeight());
        });

//        // Lazyloading
//        scrollPane.getVerticalScrollBar().getModel().addChangeListener(event -> {
//            BoundedRangeModel model = (BoundedRangeModel) event.getSource();
//            int maximum = model.getMaximum();
//            int value = model.getExtent() + model.getValue();
//
//            System.out.println("Value: " + value + " - Maximum: " + maximum);
//
//            if (value == maximum)
//                carregaDados(true);
//        });
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

    public void recarregaTabela() {
        reload = true;

        PopularTabela.voluntarios(jTabela, usuarios);
        jTabela.invalidate();
    }

    public void carregaDados() {

        ServicoCliente sv = new ServicoCliente();

        HashMap<String, String> map = Sessao.shared().getMapLO(this);
        map.put("status", String.valueOf(1)); // Usuarios ativos

        String json = sv.buscaDados("usuario", ServicoCliente.GET, "", map);

        System.out.println(json);

        try {
            usuarios.addAll(new Gson().fromJson(json, new TypeToken<ArrayList<Usuario>>() {
            }.getType()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        recarregaTabela();
    }

    public void editar() {
        DfrVoluntario verVoluntario = new DfrVoluntario(null, true, 3, usuario, this);
        verVoluntario.setVisible(true);
    }

    public void busca() {


    }

    public void excluir() {

        if (jTabela.getSelectedRowCount() == 0) {
            Utilidades.showAlerta(this, "Por favor, selecione um Voluntário!");
        } else {
            ImageIcon imagem = new ImageIcon(System.getProperty("user.dir")+"/src/main/resources/imagens/alerta.png");

            int i = JOptionPane.showConfirmDialog(
                    IfrVoluntario.this,
                    "Deseja deletar este cadastro?","Excluir",JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE, imagem
            );
            if (i == JOptionPane.YES_OPTION) {
                ServicoCliente sv = new ServicoCliente();

                HashMap<String, String> params = new HashMap<>();
                params.put("id", usuario.getId().toString());

                if (sv.enviaDados("usuario", ServicoCliente.DELETE, "", params, Erro.class)) {
                    resetaDados();
                } else {
                    Utilidades.showAlerta(this, sv.getObject().toString());
                }
            }
        }
    }

    public void resetaDados() {
        usuario = null;
        Sessao.shared().resetOffset();
        usuarios.clear();
        carregaDados();
    }
}

