/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operacionais;

import cadastros.DfrAnimal;
import com.google.gson.Gson;
import componentes.ComponentButton;
import componentes.ComponentLabel;
import componentes.ComponentText;
import componentes.Focus;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.*;

import entidades.*;
import principais.DfrBusca;
import utilidades.Formatacao;
import utilidades.FrConfig;
import utilidades.ServicoCliente;
import utilidades.Utilidades;

public final class DfrDoacao extends javax.swing.JDialog {

    private Doacao doacao;

    private JTextField jDoador = new JTextField();
    private JTextField jTipoDoacao = new JTextField();
    private JTextField jValor = new JTextField();
   
    private JTextArea jObservacao = new JTextArea();
    
    private JFormattedTextField jData = new JFormattedTextField();

    private JLabel jlDoador = new JLabel();
    private JLabel jlTipoDoacao = new JLabel();
    private JLabel jlValor = new JLabel();
    private JLabel jlObservacao = new JLabel();
    private JLabel jlData = new JLabel();
    private JLabel jlMascara = new JLabel();

    private JLabel jCabecalho = new JLabel();
    private JPanel PanelFundo = new JPanel(new BorderLayout());
    private JPanel PanelCabecalho = new JPanel(new BorderLayout());

    private JButton btnSalvarEditar = new JButton();
    private JButton btnCancelar = new JButton();
    private JButton btnDoador = new JButton();
    private JButton btnTipoDoacao = new JButton();



    private FrConfig config = new FrConfig();
    private ComponentLabel lbl = new ComponentLabel();
    private ComponentButton btn = new ComponentButton();
    private IfrDoacao parent;
    int tamCabecalho;
    int tipo;

    DfrDoacao(IfrDoacao parent, boolean modal, int tipo, Doacao doacao) {
        this.setLocationRelativeTo(null);
        ajusteTela();
        ajusteComponents();
        if (doacao != null) this.doacao = doacao;
        else this.doacao = new Doacao();

        this.parent = parent;
        this.tipo = tipo;

        // Construtor Tela Novo
        if (tipo == 1) {
            jCabecalho.setText("Nova Doação");
            novaDoacao();
            focus();
            btnSalvarEditar.addActionListener(e -> salvar());
            btnCancelar.addActionListener(e -> cancelar());
            jData.setText(Utilidades.getDataAtual());
        } //Construtor Tela Ver
        else if (tipo == 2) {
            jCabecalho.setText("Ver Doação");
            verDoacao();
            carregaDados();
            btnCancelar.addActionListener(e -> dispose());
        } else if (tipo == 3) {
            // Construtor Tela Editar
            jCabecalho.setText("Editar Doação");
            btnSalvarEditar.addActionListener(e -> salvar());
            carregaDados();
            focus();
            btnCancelar.addActionListener(e -> cancelar());
        }
    }

    private DfrDoacao(JFrame jFrame, boolean b) {
        
    }

    /*
    Ajusta o tamanho da tela
     */
    public void ajusteTela() {
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(788, 700));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                if (tipo == 2) {
                    DfrDoacao.this.dispose();
                }
                else{
                    Utilidades.showCloseAlert(DfrDoacao.this);
                }
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 788, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
        lbl.labelDoacao(ImgLogo);
        PanelCabecalho.add(ImgLogo, BorderLayout.CENTER);

    }

    /*Ajusta os componentes para ficarem sem borda*/
    public void ajusteComponents() {

        int i = tamCabecalho+100;

        //Ajusta o local dos campos
        Dimension f = PanelFundo.getSize();
        PanelFundo.add(jDoador, BorderLayout.NORTH);
        jDoador.setLocation(20, i+50);
        jDoador.setSize(500, 20);
        jDoador.setEditable(false);

        PanelFundo.add(jlDoador, BorderLayout.NORTH);
        jlDoador.setLocation(20, i+25);
        jlDoador.setSize(250, 20);

        PanelFundo.add(btnDoador, BorderLayout.NORTH);
        btnDoador.setLocation(520, i+50);
        btnDoador.setSize(20, 20);

        PanelFundo.add(jTipoDoacao, BorderLayout.NORTH);
        jTipoDoacao.setLocation(20, i+100);
        jTipoDoacao.setSize(500, 20);
        jTipoDoacao.setEditable(false);

        PanelFundo.add(jlTipoDoacao, BorderLayout.NORTH);
        jlTipoDoacao.setLocation(20, i+75);
        jlTipoDoacao.setSize(250, 20);

        PanelFundo.add(btnTipoDoacao, BorderLayout.NORTH);
        btnTipoDoacao.setLocation(520, i+100);
        btnTipoDoacao.setSize(20, 20);

        PanelFundo.add(jlMascara, BorderLayout.NORTH);
        jlMascara.setLocation(20, i+150);
        jlMascara.setSize((f.width/2)-40, 20);
        
        PanelFundo.add(jlValor, BorderLayout.NORTH);
        jlValor.setLocation(40, i+125);
        jlValor.setSize((f.width/2)-40, 20);
        
        PanelFundo.add(jValor, BorderLayout.NORTH);
        jValor.setLocation(40, i+150);
        jValor.setSize(100, 20);
        jValor.setDocument(new utilidades.MonetarioDocument());

        PanelFundo.add(jlData, BorderLayout.NORTH);
        jlData.setLocation(160, i+125);
        jlData.setSize(100, 20);

        PanelFundo.add(jData, BorderLayout.NORTH);
        jData.setLocation(160, i+150);
        jData.setSize(100, 20);

        JScrollPane spDescricao = new JScrollPane(jObservacao); //Adiciona Scroll a TextArea
        PanelFundo.add(spDescricao);
        spDescricao.setLocation(20, i+225);
        spDescricao.setSize((f.width)-40, 225);
        
        PanelFundo.add(jlObservacao, BorderLayout.NORTH);
        jlObservacao.setLocation(20, i+200);
        jlObservacao.setSize(100, 20);
        jlObservacao.setText("Observação");

        PanelFundo.add(btnCancelar, BorderLayout.NORTH);
        btnCancelar.setLocation(600, f.height - 80);

        PanelFundo.add(btnSalvarEditar, BorderLayout.NORTH);
        btnSalvarEditar.setLocation(500, f.height - 80);

        ComponentText txt = new ComponentText();
        txt.formatarDoador(jlDoador, jDoador);
        txt.formatarTipoDoacao(jlTipoDoacao, jTipoDoacao);
        txt.formatarObservacao(jObservacao);
        txt.formatarValor(jlValor, jValor);
        txt.formatarDataDoacao(jlData, jData);

        //Formata os botões
        btn.botaoSalvar(btnSalvarEditar);
        btn.botaoCancelar(btnCancelar);
        btn.botaoBusca(btnDoador);
        btn.botaoBusca(btnTipoDoacao);

        btnCancelar.addActionListener(e -> dispose());
        btnDoador.addActionListener(e -> selecionaDoador());
        btnTipoDoacao.addActionListener(e -> selecionaTipoDoacao());

    }
    public void cancelar(){
        Utilidades.showCloseAlert(DfrDoacao.this);
    }

    public void setLabels() {
        jlDoador.setText("Doador");
        jlTipoDoacao.setText("Tipo Doação");
        jlValor.setText("Valor");
        jlData.setText("Data Doação");
    }
    private void carregaDados() {
        if (!jDoador.getText().isEmpty()) jlDoador.setText("Doador");
        if (!jTipoDoacao.getText().isEmpty()) jlTipoDoacao.setText("Tipo Doação");
    }
    
    public void verDoacao() {
        
        btnDoador.setVisible(false);
        btnTipoDoacao.setVisible(false);
        jObservacao.setEditable(false);
        jValor.setEditable(false);

        btn.botaoEditar(btnSalvarEditar);
    }
    
    public void novaDoacao(){

    }
    
    public void editarDoacao() {
        setLabels();
    }

    public void salvar() {
        if (!validaCampos())
            return;

        if (doacao.getTipoDoacao().getExigeValor() == 1) doacao.setValor(Double.valueOf(jValor.getText().replace(",",".")));
        doacao.setObservacao(jObservacao.getText());

        Gson gson = new Gson();

        String json = gson.toJson(doacao);
        ServicoCliente sv = new ServicoCliente();
        System.out.println("Json: " +json);
        if (sv.enviaDados("doacao", ServicoCliente.POST, json, ServicoCliente.EMPTY_PARAMS, Usuario.class)) {
            Utilidades.showSucesso(this,"Doação registrada com sucesso!");

            parent.resetaDados();
            this.dispose();
        } else {
            Utilidades.showErro(this, (Erro) sv.getObject());
        }
    }

    private boolean validaCampos() {

        return true;
    }

    public void selecionaDoador() {
        DfrBusca buscaDoador = new DfrBusca(this, 1);
        buscaDoador.setVisible(true);
    }
    public void selecionaTipoDoacao() {
        DfrBusca buscaDoador = new DfrBusca(this, 2);
        buscaDoador.setVisible(true);
    }

    public void focus() {

        final Focus focus = new Focus();

        jValor.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent evt) {
                focus.focusValorGained(jlValor, jValor);
            }

            @Override
            public void focusLost(FocusEvent evt) {
                focus.focusValorLost(jlValor, jValor);
            }
        });

        jData.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent evt) {
                focus.focusDataDoacaoGained(jlData, jData);
            }

            @Override
            public void focusLost(FocusEvent evt) {
                if (Formatacao.removerFormatacao(jData.getText()).length() == 0) {
                    focus.focusDataDoacaoLost(jlData, jData);
                }
            }
        });
    }

    public void setTipoDoacao(TipoDoacao tipoDoacao) {
        doacao.setTipoDoacao(tipoDoacao);
        jTipoDoacao.setText(tipoDoacao.getDescricao() + " - " + tipoDoacao.getUnidadeMedida());
        jlMascara.setText(tipoDoacao.getMascara());
        jlTipoDoacao.setText("Tipo Doação");
    }

    public void setDoador(Object object) {

        if (object instanceof PessoaFisica) {
            PessoaFisica pf = (PessoaFisica) object;

            jDoador.setText(pf.getNome() + " - " + pf.getCpf());
            doacao.setPessoaFisica(pf);

        } else if (object instanceof PessoaJuridica) {
            PessoaJuridica pj = (PessoaJuridica) object;
            jDoador.setText(pj.getNomeFantasia() + " - " + pj.getCnpj());
            doacao.setPessoaJuridica(pj);
        }
    }
}
