package cadastros;

import componentes.*;
import entidades.Erro;
import entidades.Pessoa;
import entidades.PessoaFisica;
import utilidades.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

public class IfrTutorDoador extends JInternalFrame {

    // Variavel de controle
    private ArrayList<Object> tutoresDoadores;
    private Object tutorDoador;

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

    private JTable jTabela = new JTable();

    private JPanel PanelFundoCadastro = new JPanel();
    private JPanel PanelCabecalho = new JPanel(new BorderLayout());
    private JPanel PanelMenu = new JPanel(new BorderLayout());

    FrConfig config = new FrConfig();
    ComponentTable table = new ComponentTable();
    private ComponentLabel lbl = new ComponentLabel();

    public IfrTutorDoador() {
        jCabecalho.setText("Tutor/Doador");
        tutoresDoadores = new ArrayList<>();
        JLabel ImgLogo = new JLabel();
        lbl.labelTutorDoador(ImgLogo);
        PanelCabecalho.add(ImgLogo, BorderLayout.NORTH);
        btnBusca.addActionListener(e -> busca());

        setPosicao();
        setResizable(false);
        defineComponentes();
        focus();
        defineAcoes();
        carregaDados();

//        System.out.println("IfrTutorDoadorFinal");
    }

    private void defineAcoes() {
        jTabela.getSelectionModel().addListSelectionListener(event -> {
            try {
                tutorDoador = tutoresDoadores.get(jTabela.getSelectedRow());
            } catch (Exception igoner) {}
        });
        jTabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) editar();
            }
        });

        // Novo Tutor Doador
        btnNovo.addActionListener(e -> {
            DfrTutorDoador dfr = new DfrTutorDoador(this, 1, tutorDoador);
            dfr.setVisible(true);
        });

        // Edita Tutor Doador
        btnEditar.addActionListener(e -> {
            if (tutorDoador != null) {
                editar();
            } else {
                Utilidades.showAlerta(this, "Por favor, selecione um Tutor/Doador!");
            }
        });

        // Visualiza Tutor Doador
        btnVisualizar.addActionListener(e -> {
            if (tutorDoador != null) {
                DfrTutorDoador dfr = new DfrTutorDoador(this, 2, tutorDoador);
                dfr.setVisible(true);
            } else {
                Utilidades.showAlerta(this, "Por favor, selecione um Tutor/Doador!");
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
//        tutorDoador = null;

//        ServicoCliente sv = new ServicoCliente();
//        HashMap<String, String> map = Sessao.shared().getMapLO(this);
//        String json = sv.buscaDados("tutorDoador", ServicoCliente.GET, "", map);

//        System.out.println(json);
        try {
            tutoresDoadores.addAll(Sessao.shared().getTutorDoador(this,true));
        } catch (Exception e) {
            e.printStackTrace();
        }

        reload = true;
        PopularTabela.tutoresDoadores(jTabela, tutoresDoadores);
        jTabela.invalidate();
    }

    public void editar() {
        DfrTutorDoador verTutorDoador = new DfrTutorDoador(this, 3, tutorDoador);
        verTutorDoador.setVisible(true);
    }

    public void busca() {

    }

    public void excluir() {

        if (jTabela.getSelectedRowCount() == 0) {
            Utilidades.showAlerta(this, "Por favor, selecione um Tutor/Doador!");
        } else {
            ImageIcon imagem = new ImageIcon(System.getProperty("user.dir")+"/src/main/resources/imagens/alerta.png");

            int i = JOptionPane.showConfirmDialog(
                    IfrTutorDoador.this,
                    "Deseja deletar este cadastro?","Excluir",JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE, imagem
            );
            if (i == JOptionPane.YES_OPTION) {
                ServicoCliente sv = new ServicoCliente();

                if (tutorDoador instanceof Pessoa) {

                    Pessoa p = (Pessoa) tutorDoador;
                    HashMap<String, String> params = new HashMap<>();
                    params.put("id", p.getId().toString());

                    if (tutorDoador instanceof PessoaFisica) {
                        if (sv.enviaDados("pessoaFisica", ServicoCliente.DELETE, "", params, Erro.class)) {
                            System.out.println("tutorDoador" + p.getId() + " excluído!");
                            resetaDados();
                        } else {
                            Utilidades.showAlerta(this, sv.getObject().toString());
                        }
                    } else {
                        if (sv.enviaDados("pessoaJuridica", ServicoCliente.DELETE, "", params, Erro.class)) {
                            System.out.println("tutorDoador" + p.getId() + " excluído!");
                            resetaDados();
                        } else {
                            Utilidades.showAlerta(this, sv.getObject().toString());
                        }
                    }

                }
            }
        }
    }

    public void resetaDados() {
        tutorDoador = null;
        Sessao.shared().resetOffset();
        tutoresDoadores.clear();
        carregaDados();
    }

}
