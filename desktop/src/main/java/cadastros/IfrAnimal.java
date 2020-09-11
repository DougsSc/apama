package cadastros;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import componentes.*;
import entidades.Animal;
import entidades.Erro;
import operacionais.DfrAdocao;
import utilidades.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

public class IfrAnimal extends JInternalFrame {

    // Variavel de controle
    private ArrayList<Animal> animais;
    private Animal animal;

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
    ComponentLabel lbl = new ComponentLabel();

    public IfrAnimal() {
        jCabecalho.setText("Animal");
        JLabel ImgLogo = new JLabel();
        animais = new ArrayList<>();
        lbl.labelAnimal(ImgLogo);
        PanelCabecalho.add(ImgLogo, BorderLayout.NORTH);
        btnBusca.addActionListener(e -> busca());

        setPosicao();
        setResizable(false);
        defineComponentes();
        focus();
        defineAcoes();
        carregaDados();
    }

    private void defineAcoes() {
        // Listenner da seleção
        jTabela.getSelectionModel().addListSelectionListener(event -> {
            try {
                animal = animais.get(jTabela.getSelectedRow());
            } catch (Exception igoner) {}
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

        // Novo Animal
        btnNovo.addActionListener(e -> {
                DfrAnimal novoanimal = new DfrAnimal(1, animal, this);
                novoanimal.setVisible(true);
        });

        // Edita Animal
        btnEditar.addActionListener(e -> {
            if (animal != null) {
                editar();
            } else {
                Utilidades.showAlerta(this, "Por favor, selecione um Animal!");
            }
        });

        // Visualiza Animal
        btnVisualizar.addActionListener(e -> {
            if (animal != null) {
                DfrAnimal verAnimal = new DfrAnimal(2, animal, this);
                verAnimal.setVisible(true);
            } else {
                Utilidades.showAlerta(this, "Por favor, selecione um Animal!");
            }

        });
        // Relatório Animal
        btnRelatorio.addActionListener(e -> {

            DfrAnimalRelatorio relatorioAnimal = new DfrAnimalRelatorio(null, true, animal, this);
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
        ComponentLabel label = new ComponentLabel();

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
        jBusca.setSize((d.width-(d.width/7))-90, 20);

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
        PanelMenu.add(btnRelatorio, BorderLayout.CENTER);
        PanelMenu.add(btnSair, BorderLayout.CENTER);

        //Adiciona a Tabela
        PanelFundoCadastro.add(jTabela, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(jTabela);
        PanelFundoCadastro.add( scrollPane, BorderLayout.NORTH);
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

        jBusca.addFocusListener(new FocusAdapter(){

        public void focusGained(FocusEvent evt) {
            focus.focusBuscaGained(jlBusca, jBusca);
        }

        public void focusLost(FocusEvent evt) {
            focus.focusBuscaLost(jlBusca, jBusca);
        }

    });
}

    private void carregaDados() {

        ServicoCliente sv = new ServicoCliente();

        HashMap<String, String> map = Sessao.shared().getMapLO(this);
//        map.put("status", String.valueOf(1)); // Usuarios ativos

        String json = sv.buscaDados("animal", ServicoCliente.GET, "", map);

        System.out.println(json);

        try {
            animais.addAll(new Gson().fromJson(json, new TypeToken<ArrayList<Animal>>() {}.getType()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        reload = true;

        PopularTabela.animais(jTabela, animais);
        jTabela.invalidate();

        /*
        animal = null;

        ServicoCliente sv = new ServicoCliente();
        String json = sv.buscaDados("animal", ServicoCliente.GET, "", ServicoCliente.EMPTY_PARAMS);

        System.out.println(json);

        animais = new Gson().fromJson(json, new TypeToken<ArrayList<Animal>>() {}.getType());

        PopularTabela.animais(jTabela, animais);
        jTabela.invalidate(); */
    }

    public void editar() {
            DfrAnimal verAnimal = new DfrAnimal(3, animal, this);
            verAnimal.setVisible(true);
    }

    public void busca() {


    }

    public void excluir() {

        if (jTabela.getSelectedRowCount() == 0) {
            Utilidades.showAlerta(this, "Por favor, selecione um Animal!");
        } else {
            ImageIcon imagem = new ImageIcon(System.getProperty("user.dir")+"/src/main/resources/imagens/alerta.png");

            int i = JOptionPane.showConfirmDialog(
                    IfrAnimal.this,
                    "Deseja deletar este cadastro?","Excluir",JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE, imagem
            );
            if (i == JOptionPane.YES_OPTION) {
                ServicoCliente sv = new ServicoCliente();

                HashMap<String, String> params = new HashMap<>();
                params.put("id", animal.getId().toString());

                System.out.println(animal.getId());

                if (sv.enviaDados("animal", ServicoCliente.DELETE, "", params, Erro.class)) {
                    System.out.println("Usuario " + animal.getId() + " excluído!");
                    resetaDados();
                } else {
                    Utilidades.showAlerta(this, sv.getObject().toString());
                }
            }
        }
    }

    public void resetaDados() {
        animal = null;
        Sessao.shared().resetOffset();
        animais.clear();
        carregaDados();
    }
}
