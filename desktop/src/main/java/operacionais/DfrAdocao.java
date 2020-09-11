/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operacionais;

import com.google.gson.Gson;
import componentes.ComponentButton;
import componentes.ComponentLabel;
import componentes.ComponentText;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Date;
import java.util.List;
import javax.rmi.CORBA.Util;
import javax.swing.*;

import componentes.Focus;
import entidades.*;
import principais.DfrBusca;
import utilidades.*;

public final class DfrAdocao extends javax.swing.JDialog {

    private Adocao adocao;

    private JTextField jAnimal = new JTextField();
    private JTextField jTutor = new JTextField();

    private JTextArea jObservacao = new JTextArea();

    private JFormattedTextField jData = new JFormattedTextField();

    private JLabel jlAnimal = new JLabel();
    private JLabel jlTutor = new JLabel();
    private JLabel jlObservacao = new JLabel();
    private JLabel jlStatus = new JLabel();
    private JLabel jlCabecalho = new JLabel();
    private JLabel jlData = new JLabel();
    private JPanel PanelFundo = new JPanel(new BorderLayout());
    private JPanel PanelCabecalho = new JPanel(new BorderLayout());

    private JButton btnSalvarEditar = new JButton();
    private JButton btnCancelar = new JButton();
    private JButton btnAnimal = new JButton();
    private JButton btnTutor = new JButton();
    private JButton btnAnimalVer = new JButton();
    private JButton btnTutorVer = new JButton();
    private JButton btnImagem = new JButton();

    private JRadioButton btnAguardandoAprovacao = new JRadioButton();
    private JRadioButton btnAusenciaInformacoes = new JRadioButton();
    private JRadioButton btnAprovado = new JRadioButton();
    private JRadioButton btnRejeitado = new JRadioButton();

    private ButtonGroup tipoStatus = new ButtonGroup();

    private FrConfig config = new FrConfig();
    private ComponentLabel lbl = new ComponentLabel();
    private ComponentButton btn = new ComponentButton();

    private IfrAdocao parent;
    int tamCabecalho;
    int tipo;

    DfrAdocao(IfrAdocao parent, int tipo, Adocao adocao) {
        this.setLocationRelativeTo(null);
        ajusteTela();
        ajusteComponents();
        this.tipo = tipo;

        if (adocao != null) this.adocao = adocao;
        else this.adocao = new Adocao();
        this.parent = parent;

        tipoStatus.add(btnAguardandoAprovacao);
        tipoStatus.add(btnAusenciaInformacoes);
        tipoStatus.add(btnAprovado);
        tipoStatus.add(btnRejeitado);

        // Construtor Tela Novo
        if (tipo == 1) {
            jlCabecalho.setText("Nova Adoção");
            novaAdocao();
            btnSalvarEditar.addActionListener(e -> salvar(1));
            focus();
            btnCancelar.addActionListener(e -> cancelar());
            jData.setText(Utilidades.getDataAtual());
        } //Construtor Tela Ver
        else if (tipo == 2) {
            jlCabecalho.setText("Ver Adoção");
            carregaDados();
            verAdocao();
            btnCancelar.addActionListener(e -> dispose());
        } else if (tipo == 3) {
            // Construtor Tela Editar
            jlCabecalho.setText("Editar Adoção");
            btnSalvarEditar.addActionListener(e -> salvar(2));
            btnCancelar.addActionListener(e -> cancelar());
            carregaDados();
            focus();
        }
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
                    DfrAdocao.this.dispose();
                }
                else{
                    Utilidades.showCloseAlert(DfrAdocao.this);
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
        PanelCabecalho.add(jlCabecalho);
        PanelCabecalho.setSize(f.width * 2, f.height / 7);
        jlCabecalho.setSize(f.width * 2, f.height / 7);
        config.ajusteCabecalho(jlCabecalho);

        //Adiciona Arquivo Cabecalho
        JLabel ImgLogo = new JLabel();
        lbl.labelAdocao(ImgLogo);
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

        PanelFundo.add(btnAnimal, BorderLayout.NORTH);
        btnAnimal.setLocation(520, i + 50);
        btnAnimal.setSize(20, 20);

        PanelFundo.add(btnAnimalVer, BorderLayout.NORTH);
        btnAnimalVer.setLocation(550, i + 50);
        btnAnimalVer.setSize(150, 20);

        PanelFundo.add(jTutor, BorderLayout.NORTH);
        jTutor.setLocation(20, i + 100);
        jTutor.setSize(500, 20);
        jTutor.setEditable(false);

        PanelFundo.add(jlTutor, BorderLayout.NORTH);
        jlTutor.setLocation(20, i + 75);
        jlTutor.setSize(250, 20);

        PanelFundo.add(btnTutor, BorderLayout.NORTH);
        btnTutor.setLocation(520, i + 100);
        btnTutor.setSize(20, 20);

        PanelFundo.add(btnTutorVer, BorderLayout.NORTH);
        btnTutorVer.setLocation(550, i + 100);
        btnTutorVer.setSize(150, 20);

        PanelFundo.add(jlData, BorderLayout.NORTH);
        jlData.setLocation(20, i+125);
        jlData.setSize(100, 20);

        PanelFundo.add(jData, BorderLayout.NORTH);
        jData.setLocation(20, i+150);
        jData.setSize(100, 20);

        PanelFundo.add(btnImagem, BorderLayout.NORTH);
        btnImagem.setLocation(140, i + 150);
        btnImagem.setSize(100, 20);

        PanelFundo.add(jlStatus, BorderLayout.NORTH);
        jlStatus.setLocation(20, i + 200);
        jlStatus.setSize((f.width / 2) - 10, 20);
        jlStatus.setText("Status");

        PanelFundo.add(btnAguardandoAprovacao, BorderLayout.NORTH);
        btnAguardandoAprovacao.setLocation(20, i + 225);
        btnAguardandoAprovacao.setSize(200, 20);

        PanelFundo.add(btnAusenciaInformacoes, BorderLayout.NORTH);
        btnAusenciaInformacoes.setLocation(240, i + 225);
        btnAusenciaInformacoes.setSize(210, 20);

        PanelFundo.add(btnAprovado, BorderLayout.NORTH);
        btnAprovado.setLocation(480, i + 225);
        btnAprovado.setSize(95, 20);

        PanelFundo.add(btnRejeitado, BorderLayout.NORTH);
        btnRejeitado.setLocation(600, i + 225);
        btnRejeitado.setSize(90, 20);

        PanelFundo.add(jlObservacao, BorderLayout.NORTH);
        jlObservacao.setLocation(20, i + 250);
        jlObservacao.setSize(240, 20);
        jlObservacao.setText("Observação");

        JScrollPane spDescricao = new JScrollPane(jObservacao); //Adiciona Scroll a TextArea
        PanelFundo.add(spDescricao);
        spDescricao.setLocation(20, i + 275);
        spDescricao.setSize((f.width)-40, 200);

        PanelFundo.add(btnCancelar, BorderLayout.NORTH);
        btnCancelar.setLocation(600, f.height - 80);

        PanelFundo.add(btnSalvarEditar, BorderLayout.NORTH);
        btnSalvarEditar.setLocation(500, f.height - 80);

        ComponentText txt = new ComponentText();
        // Formata os campos
        txt.formatarAnimal(jlAnimal, jAnimal);
        txt.formatarTutor(jlTutor, jTutor);
        txt.formatarTitulo(jlStatus);
        txt.formatarDataAdocao(jlData, jData);

        //Formata os botões
        btn.botaoSalvar(btnSalvarEditar);
        btn.botaoCancelar(btnCancelar);
        btn.botaoBusca(btnTutor);
        btn.botaoBusca(btnAnimal);
        btn.botaoDadosAnimal(btnAnimalVer);
        btn.botaoDadosTutor(btnTutorVer);
        btn.botaoVerImagem(btnImagem);
        btn.botaoAguardandoAprovacao(btnAguardandoAprovacao);
        btn.botaoAusenciaInformacoes(btnAusenciaInformacoes);
        btn.botaoAprovado(btnAprovado);
        btn.botaoRejeitado(btnRejeitado);


        btnCancelar.addActionListener(e -> dispose());
        btnImagem.addActionListener(e -> buscaArquivos());
        btnTutor.addActionListener(e -> buscaTutor());
        btnAnimal.addActionListener(e -> buscaAnimal());

    }
    public void cancelar(){
        Utilidades.showCloseAlert(DfrAdocao.this);
    }

    private void carregaDados() {

        if (adocao.getAnimal() != null)
            jAnimal.setText(adocao.getAnimal().getNome() + " - " + adocao.getAnimal().getAnimalStatus().getDescricao());

        if (adocao.getTutor() != null)
            jTutor.setText(adocao.getTutor().getNome() + " - " + Formatacao.formataCPF(adocao.getTutor().getCpf()));

        if (adocao.getDataAdocao() != null)
            jData.setText(Formatacao.ajustaDataDMA(adocao.getDataAdocao()));

        if (adocao.getStatus() != null) {
            switch (adocao.getStatus().getId()) {
                case 1:
                    btnAguardandoAprovacao.setSelected(true);
                    break;
                case 2:
                    btnAusenciaInformacoes.setSelected(true);
                    break;
                case 3:
                    btnAprovado.setSelected(true);
                    break;
                case 4:
                    btnRejeitado.setSelected(true);
                    break;
            }
        }

        if (adocao.getObservacao() != null)
            jObservacao.setText(adocao.getObservacao());
    }
    public void setLabels() {
        jlAnimal.setText("Animla");
        jlTutor.setText("Tutor");
        jlData.setText("Data Doação");
    }

    public void verAdocao() {
        btnAnimal.setVisible(false);
        btnTutor.setVisible(false);
        jObservacao.setEditable(false);
        btn.botaoEditarDialog(btnSalvarEditar);
    }

    public void novaAdocao() {
        btnAnimalVer.setEnabled(false);
        btnTutorVer.setEnabled(false);

        if (adocao != null) {
            if (adocao.getAnimal() != null)
                btnAnimalVer.setEnabled(true);

            if (adocao.getTutor() != null)
                btnTutorVer.setEnabled(true);
        }
    }

    public void editarAdocao() {
        setLabels();


    }

    public void salvar(int tipo) {
        if (!validaCampos())
            return;

        adocao.setObservacao(jObservacao.getText());
        adocao.setUsuario(Sessao.shared().getUsuarioLogado());
        adocao.setDataAdocao(Formatacao.ajustaDataAMD(jData.getText()));

        AdocaoStatus status = new AdocaoStatus();
        if (btnAguardandoAprovacao.isSelected()) {
            status.setId(1);
        } else if (btnAusenciaInformacoes.isSelected()) {
            status.setId(2);
        } else if (btnAprovado.isSelected()) {
            status.setId(3);
        } else if (btnRejeitado.isSelected()) {
            status.setId(4);
        }
        adocao.setStatus(status);

        Gson gson = new Gson();
        String json = gson.toJson(adocao);

        boolean sucesso = false;
        ServicoCliente sv = new ServicoCliente();
        if (tipo == 1) {
            sucesso = sv.enviaDados("adocao", ServicoCliente.POST, json, ServicoCliente.EMPTY_PARAMS, Animal.class);
        } else if (tipo == 2) {
            sucesso = sv.enviaDados("adocao", ServicoCliente.PUT, json, ServicoCliente.EMPTY_PARAMS, Animal.class);
        }

        if (sucesso) {
            Utilidades.showSucesso(this,"Adoção registrada/alterada com sucesso!");
            parent.resetaDados();
            this.dispose();
        } else {
            Utilidades.showErro(this, (Erro) sv.getObject());
        }
    }

    private void focus(){
        final Focus focus = new Focus();
        jData.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent evt) {
                focus.focusDataAdocaoGained(jlData, jData);
            }

            @Override
            public void focusLost(FocusEvent evt) {
                if (Formatacao.removerFormatacao(jData.getText()).length() == 0) {
                    focus.focusDataAdocaoLost(jlData, jData);
                }
            }
        });

    }

    private boolean validaCampos() {

        return true;
    }

    public void buscaArquivos() {
        DfrBusca verArquivos = new DfrBusca(this, adocao.getArquivos());
        verArquivos.setVisible(true);
    }

    public void buscaTutor() {
        DfrBusca verTutor = new DfrBusca(this, 1);
        verTutor.setVisible(true);
    }

    public void buscaAnimal() {
        DfrBusca verAnimal = new DfrBusca(this, 2);
        verAnimal.setVisible(true);
    }

    public void setArquivos(List<Arquivo> arquivos) {
        adocao.setArquivos(arquivos);
    }

    public void setAnimal(Animal animal) {
        jAnimal.setText(animal.getNome() + " - " + animal.getAnimalStatus().getDescricao());
        adocao.setAnimal(animal);
        jlAnimal.setText("Animal");
    }

    public void setTutor(PessoaFisica tutor) {
        jTutor.setText(tutor.getNome() + " - " + Formatacao.formataCPF(tutor.getCpf()));
        adocao.setTutor(tutor);
        jlTutor.setText("Tutor");
    }
}
