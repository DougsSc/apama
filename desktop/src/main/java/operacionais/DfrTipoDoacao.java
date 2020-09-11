package operacionais;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import cadastros.DfrTutorDoador;
import cadastros.DfrVoluntario;
import com.google.gson.Gson;
import componentes.*;
import entidades.*;
import principais.DfrBusca;
import utilidades.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public final class DfrTipoDoacao extends JDialog {



    private JPanel PanelFundo = new JPanel(new BorderLayout());
    private JPanel PanelCabecalho = new JPanel(new BorderLayout());

    private JTextField jDescricao = new JTextField();
    private JTextField jUnidade = new JTextField();
    private JTextField jMascara = new JTextField();


    private JLabel jCabecalho = new JLabel();
    private JLabel jlDescricao = new JLabel();
    private JLabel jlUnidade = new JLabel();
    private JLabel jlMascara = new JLabel();

    private FrConfig config = new FrConfig();

    private JButton btnOk = new JButton();
    private JButton btnCancelar = new JButton();

    private ComponentButton btn = new ComponentButton();
    private ComponentText txt = new ComponentText();
    private ComponentLabel lbl = new ComponentLabel();


    public DfrTipoDoacao(DfrBusca parent) {
        super(parent, true);
        iniciaTela();
    }

    private void iniciaTela() {
        ajusteTela();
        ajusteComponents();
        focus();
        btnCancelar.addActionListener(e -> cancelar());


        jCabecalho.setText("Tipo Doação");

    }

    public void ajusteTela() {
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(788, 700));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                    Utilidades.showCloseAlert(DfrTipoDoacao.this);
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

        config.ajusteDialogBusca(this);

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

    public void ajusteComponents() {
        int i = 100;
        Dimension f = PanelFundo.getSize();

        PanelFundo.add(jDescricao, BorderLayout.NORTH);
        jDescricao.setLocation(20, i+50);
        jDescricao.setSize((f.width/2)+200, 20);

        PanelFundo.add(jlDescricao, BorderLayout.NORTH);
        jlDescricao.setLocation(20, i+25);
        jlDescricao.setSize(250, 20);

        PanelFundo.add(jUnidade, BorderLayout.NORTH);
        jUnidade.setLocation(20, i+100);
        jUnidade.setSize((f.width/2)+200, 20);

        PanelFundo.add(jlUnidade, BorderLayout.NORTH);
        jlUnidade.setLocation(20, i+75);
        jlUnidade.setSize(250, 20);

        PanelFundo.add(jMascara, BorderLayout.NORTH);
        jMascara.setLocation(20, i+150);
        jMascara.setSize((f.width/2)-40, 20);

        PanelFundo.add(jlMascara, BorderLayout.NORTH);
        jlMascara.setLocation(20, i+125);
        jlMascara.setSize((f.width/2)-40, 20);

        PanelFundo.add(btnCancelar, BorderLayout.NORTH);
        btnCancelar.setLocation(f.width - 150, 550);

        PanelFundo.add(btnOk, BorderLayout.NORTH);
        btnOk.setLocation(f.width - 250, 550);

        btn.botaoOk(btnOk);
        btn.botaoCancelar(btnCancelar);

        txt.formatarDescricao(jlDescricao, jDescricao);
        txt.formatarUnidadedeMedida(jlUnidade, jUnidade);
        txt.formatarMascara(jlMascara, jMascara);

        btnOk.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> dispose());
    }

    public void cancelar(){
        Utilidades.showCloseAlert(DfrTipoDoacao.this);
    }

    public void focus() {
        final Focus focus = new Focus();


        jDescricao.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent evt) {

                focus.focusDescricaoGained(jlDescricao, jDescricao);
            }

            @Override
            public void focusLost(FocusEvent evt) {

                focus.focusDescricaoLost(jlDescricao, jDescricao);
            }
        });
        jMascara.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent evt) {

                focus.focusMascaraGained(jlMascara, jMascara);
            }

            @Override
            public void focusLost(FocusEvent evt) {

                focus.focusMascaraLost(jlMascara, jMascara);
            }
        });
        jUnidade.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent evt) {

                focus.focusUnidadeGained(jlUnidade, jUnidade);
            }

            @Override
            public void focusLost(FocusEvent evt) {

                focus.focusUnidadeLost(jlUnidade, jUnidade);
            }
        });

    }

    private void salvar() {
        if (!validaCampos())
            return;

        TipoDoacao tp = new TipoDoacao();
        tp.setDescricao(jDescricao.getText());
        tp.setExigeValor(1);
        tp.setUnidadeMedida(jUnidade.getText());
        tp.setMascara(jMascara.getText());

        Gson gson = new Gson();
        String json = gson.toJson(tp);

        ServicoCliente sv = new ServicoCliente();
        if (sv.enviaDados("tipoDoacao", ServicoCliente.POST, json, ServicoCliente.EMPTY_PARAMS, Usuario.class)) {
            Utilidades.showSucesso(this,"Tipo de Doação registrado com sucesso!");

            if (getParent() instanceof DfrBusca) {
                Sessao.shared().getTiposDoacao(true);
                ((DfrBusca) getParent()).iniciaTiposDoacao();
            }

            this.dispose();
        } else {
            Utilidades.showErro(this, (Erro) sv.getObject());
        }
    }

    private boolean validaCampos() {

        return true;
    }
}
