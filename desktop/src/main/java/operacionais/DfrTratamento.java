/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operacionais;

import componentes.ComponentButton;
import componentes.ComponentLabel;
import componentes.ComponentText;
import componentes.Focus;
import entidades.*;
import utilidades.Formatacao;
import utilidades.FrConfig;
import utilidades.Utilidades;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public final class DfrTratamento extends JDialog {

    private AnimalTratamento tratamento;

    private JTextField jAnimal = new JTextField();
    private JTextField jTipoTratamento = new JTextField();

    private JTextArea jObservacao = new JTextArea();

    private JFormattedTextField jDataTratamento = new JFormattedTextField();
    private JFormattedTextField jDataProximo = new JFormattedTextField();

    private JLabel jlAnimal = new JLabel();
    private JLabel jlTipoTratamento = new JLabel();
    private JLabel jlDataTratamento = new JLabel();
    private JLabel jlDataProximo = new JLabel();
    private JLabel jlObservacao = new JLabel();

    private JLabel jCabecalho = new JLabel();
    private JPanel PanelFundo = new JPanel(new BorderLayout());
    private JPanel PanelCabecalho = new JPanel(new BorderLayout());

    private JButton btnSalvarEditar = new JButton();
    private JButton btnCancelar = new JButton();
    private JButton btnAnimal = new JButton();
    private JButton btnTipoTratamento = new JButton();
    private JButton btnAnimalVer = new JButton();

    private FrConfig config = new FrConfig();
    private ComponentLabel lbl = new ComponentLabel();
    private ComponentButton btn = new ComponentButton();

    private IfrTratamento tela;
    int tamCabecalho;
    int tipo;

    DfrTratamento(java.awt.Frame parent, boolean modal, int tipo, AnimalTratamento tratamento) {

        super(parent, modal);
        this.setLocationRelativeTo(null);
        ajusteTela();
        ajusteComponents();
        this.tratamento = tratamento;
        this.tela = tela;
        this.tipo = tipo;

        // Construtor Tela Novo
        if (tipo == 1) {
            jCabecalho.setText("Novo Tratamento");
            novaDoacao();
            btnSalvarEditar.addActionListener(e -> salvar());
            btnCancelar.addActionListener(e -> cancelar());
            focus();
        } //Construtor Tela Ver
        else if (tipo == 2) {
            jCabecalho.setText("Ver Tratamento");
            carregaDados();
            verDoacao();
            btnCancelar.addActionListener(e -> dispose());
        } else if (tipo == 3) {
            // Construtor Tela Editar
            jCabecalho.setText("Editar Tratamento");;
            btnSalvarEditar.addActionListener(e -> salvar());
            btnCancelar.addActionListener(e -> cancelar());
            carregaDados();
            focus();
        }
    }

    private DfrTratamento(JFrame jFrame, boolean b) {

    }

    /*
    Ajusta o tamanho da tela
     */
    public void ajusteTela() {
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(788, 700));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                if (tipo == 2) {
                    DfrTratamento.this.dispose();
                }
                else{
                    Utilidades.showCloseAlert(DfrTratamento.this);
                }
            }
        });

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 788, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 697, Short.MAX_VALUE)
        );

        pack();

        config.ajusteDialog(this);

        // Cria o fundo cinza
        Dimension f = this.getSize();
        config.ajusteFundo(PanelFundo);
        getContentPane().add(PanelFundo, BorderLayout.CENTER);
        PanelFundo.setSize(f.width, f.height);
        config.ajusteCabecalho(PanelCabecalho);
        PanelFundo.add(PanelCabecalho, BorderLayout.CENTER);
        PanelCabecalho.add(jCabecalho);
        PanelCabecalho.setSize(f.width * 2, f.height / 7);
        jCabecalho.setSize(f.width * 2, f.height / 7);
        config.ajusteCabecalho(jCabecalho);

        //Adiciona Arquivo Cabecalho
        JLabel ImgLogo = new JLabel();
        lbl.labelTratamento(ImgLogo);
        PanelCabecalho.add(ImgLogo, BorderLayout.CENTER);

    }

    /*Ajusta os componentes para ficarem sem borda*/
    public void ajusteComponents() {

        int i = tamCabecalho+100;

        //Ajusta o local dos campos
        Dimension f = PanelFundo.getSize();
        PanelFundo.add(jAnimal, BorderLayout.NORTH);
        jAnimal.setLocation(20, i + 50);
        jAnimal.setSize(500, 20);
        jAnimal.setEditable(false);

        PanelFundo.add(jlAnimal, BorderLayout.NORTH);
        jlAnimal.setLocation(20, i + 25);
        jlAnimal.setSize(250, 20);
        jlAnimal.setText("Animal");

        PanelFundo.add(btnAnimal, BorderLayout.NORTH);
        btnAnimal.setLocation(520, i + 50);
        btnAnimal.setSize(20, 20);

        PanelFundo.add(btnAnimalVer, BorderLayout.NORTH);
        btnAnimalVer.setLocation(550, i + 50);
        btnAnimalVer.setSize(150, 20);

        PanelFundo.add(jTipoTratamento, BorderLayout.NORTH);
        jTipoTratamento.setLocation(20, i + 100);
        jTipoTratamento.setSize(500, 20);
        jTipoTratamento.setEditable(false);

        PanelFundo.add(jlTipoTratamento, BorderLayout.NORTH);
        jlTipoTratamento.setLocation(20, i + 75);
        jlTipoTratamento.setSize(250, 20);
        jlTipoTratamento.setText("Tipo Tratamento");

        PanelFundo.add(btnTipoTratamento, BorderLayout.NORTH);
        btnTipoTratamento.setLocation(520, i + 100);
        btnTipoTratamento.setSize(20, 20);

        PanelFundo.add(jDataTratamento, BorderLayout.NORTH);
        jDataTratamento.setLocation(20, i + 150);
        jDataTratamento.setSize(200, 20);

        PanelFundo.add(jlDataTratamento, BorderLayout.NORTH);
        jlDataTratamento.setLocation(20, i + 125);
        jlDataTratamento.setSize(150, 20);

        PanelFundo.add(jDataProximo, BorderLayout.NORTH);
        jDataProximo.setLocation(240, i + 150);
        jDataProximo.setSize(200, 20);

        PanelFundo.add(jlDataProximo, BorderLayout.NORTH);
        jlDataProximo.setLocation(240, i + 125);
        jlDataProximo.setSize(150, 20);

        PanelFundo.add(jlObservacao, BorderLayout.NORTH);
        jlObservacao.setLocation(20, i + 175);
        jlObservacao.setSize(240, 20);
        jlObservacao.setText("Observação");

        JScrollPane spDescricao = new JScrollPane(jObservacao); //Adiciona Scroll a TextArea
        PanelFundo.add(spDescricao);
        spDescricao.setLocation(20, i + 200);
        spDescricao.setSize((f.width)-40, 200);

        PanelFundo.add(btnCancelar, BorderLayout.NORTH);
        btnCancelar.setLocation(600, f.height - 80);

        PanelFundo.add(btnSalvarEditar, BorderLayout.NORTH);
        btnSalvarEditar.setLocation(500, f.height - 80);
        ComponentText txt = new ComponentText();


        txt.formatarAnimal(jlAnimal, jAnimal);
        txt.formatarTipoTratamento(jlTipoTratamento, jTipoTratamento);
        txt.formatarDataTratamento(jlDataTratamento, jDataTratamento);
        txt.formatarDataTratamentoProximo(jlDataProximo, jDataProximo);
        txt.formatarObservacao(jObservacao);

        //Formata os botões
        btn.botaoSalvar(btnSalvarEditar);
        btn.botaoCancelar(btnCancelar);
        btn.botaoBusca(btnAnimal);
        btn.botaoBusca(btnTipoTratamento);
        btn.botaoDadosAnimal(btnAnimalVer);

    }
    public void cancelar(){
        Utilidades.showCloseAlert(DfrTratamento.this);
    }

    private void carregaDados() {

    }

    public void setLabels() {
        jlAnimal.setText("Animal");
        jlTipoTratamento.setText("Tipo Tratamento");
        jlDataTratamento.setText("Data Tratamento");
        jlDataProximo.setText("Data Próximo Tratamento");
    }
    
    public void verDoacao() {
        btnAnimal.setVisible(false);
        btnTipoTratamento.setVisible(false);
        jObservacao.setEditable(false);
        jDataProximo.setEditable(false);
        jDataTratamento.setEditable(false);
        btn.botaoEditarDialog(btnSalvarEditar);
    }

    public void novaDoacao() {
        btnAnimalVer.setEnabled(false);
    }

    public void editarDoacao() {

    }

    public void salvar() {

    }

    public void focus() {

        final Focus focus = new Focus();

        jDataTratamento.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent evt) {
                focus.focusDataTratamentoGained(jlDataTratamento, jDataTratamento);
            }

            @Override
            public void focusLost(FocusEvent evt) {
                if (Formatacao.removerFormatacao( jDataTratamento.getText()).length() == 0) {
                    focus.focusDataLost(jlDataTratamento, jDataTratamento);
                }
    }
});

        jDataProximo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent evt) {
                focus.focusDataProximoGained(jlDataProximo, jDataProximo);
            }

            @Override
            public void focusLost(FocusEvent evt) {
                if (Formatacao.removerFormatacao(jDataProximo.getText()).length() == 0) {
                    focus.focusDataLost(jlDataProximo, jDataProximo);
                }
            }
        });
    }


    public void setjTipoTratamento(TipoDoacao tipoDoacao) {
     //   jlTipoTratamento.setText(tipoDoacao.getDescricao() + " - " + tipoDoacao.getUnidadeMedida());
    }

    public void setTratamento(Object object) {
/*
        if (object instanceof PessoaFisica) {
            PessoaFisica pf = (PessoaFisica) object;

            jDoador.setText(pf.getNome() + " - " + pf.getCpf());
        } else if (object instanceof PessoaJuridica) {
            PessoaJuridica pj = (PessoaJuridica) object;
            jDoador.setText(pj.getNomeFantasia() + " - " + pj.getCnpj());
        }*/
    }
}
