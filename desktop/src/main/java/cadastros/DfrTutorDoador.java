/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cadastros;

import com.google.gson.Gson;
import componentes.ComponentButton;
import componentes.ComponentLabel;
import componentes.ComponentText;
import componentes.Focus;
import entidades.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.*;

import principais.DfrBusca;
import utilidades.*;

public final class DfrTutorDoador extends javax.swing.JDialog {

    private Object tutorDoador;
    private Cidade cidade;

    private JTextField jNome = new JTextField();
    private JTextField jNomeFantasia = new JTextField();
    private JTextField jLogradouro = new JTextField();
    private JTextField jNumero = new JTextField();
    private JTextField jComplemento = new JTextField();
    private JTextField jBairro = new JTextField();
    private JTextField jEmail = new JTextField();
    private JTextField jCidade = new JTextField();

    private JFormattedTextField jTelefone1 = new JFormattedTextField();
    private JFormattedTextField jCPFCNPJ = new JFormattedTextField();
    private JFormattedTextField jTelefone2 = new JFormattedTextField();
    private JFormattedTextField jCEP = new JFormattedTextField();

    private JLabel jlNome = new JLabel();
    private JLabel jlNomeFantasia = new JLabel();
    private JLabel jlCPFCNPJ = new JLabel();
    private JLabel jlLogradouro = new JLabel();
    private JLabel jlNumero = new JLabel();
    private JLabel jlComplemento = new JLabel();
    private JLabel jlBairro = new JLabel();
    private JLabel jlCidade = new JLabel();
    private JLabel jlContato1 = new JLabel();
    private JLabel jlContato2 = new JLabel();
    private JLabel jlEmail = new JLabel();
    private JLabel jlCEP = new JLabel();
    private JLabel jCabecalho = new JLabel();
    private JPanel PanelFundo = new JPanel(new BorderLayout());
    private JPanel PanelCabecalho = new JPanel(new BorderLayout());

    private JComboBox jTipoTelefone1 = new JComboBox();
    private JComboBox jTipoTelefone2 = new JComboBox();

    private JButton btnSalvarEditar = new JButton();
    private JButton btnCancelar = new JButton();
    private JButton btnCidade = new JButton();
    private JButton btnCEP = new JButton();

    private JRadioButton btnPessoaFisica = new JRadioButton();
    private JRadioButton btnPessoaJuridica = new JRadioButton();
    private JRadioButton btnTutor = new JRadioButton();
    private JRadioButton btnLiberaAdocao = new JRadioButton();
    private JRadioButton btnTipoFixo1 = new JRadioButton();
    private JRadioButton btnTipoCelular1 = new JRadioButton();
    private JRadioButton btnTipoFixo2 = new JRadioButton();
    private JRadioButton btnTipoCelular2 = new JRadioButton();

    private ButtonGroup tipoContato1 = new ButtonGroup();
    private ButtonGroup tipoContato2 = new ButtonGroup();

    private ButtonGroup tipoPessoa = new ButtonGroup();

    private FrConfig config = new FrConfig();
    private ComponentLabel lbl = new ComponentLabel();
    private ComponentButton btn = new ComponentButton();
    final Focus focus = new Focus();
    ComponentText txt = new ComponentText();

    private IfrTutorDoador parent;

    int tamCabecalho;
    int tipo;

    DfrTutorDoador(IfrTutorDoador parent, int tipo, Object tutorDoador) {
//        super(parent, modal);
        this.parent = parent;
        this.setLocationRelativeTo(null);
        ajusteTela();
        ajusteComponents(0);
        pessoaFisica();
        this.tipo = tipo;

        btnPessoaFisica.setSelected(true);
        jNomeFantasia.setVisible(false);
        txt.formatarNome(jlNome, jNome);
        txt.formatarCPF(jlCPFCNPJ, jCPFCNPJ);
        if(tutorDoador != null){
            this.tutorDoador = tutorDoador;
        }else{
            this.tutorDoador = new Pessoa();
        }

        tipoContato1.add(btnTipoFixo1);
        tipoContato1.add(btnTipoCelular1);
        tipoContato2.add(btnTipoFixo2);
        tipoContato2.add(btnTipoCelular2);

        tipoPessoa.add(btnPessoaFisica);
        tipoPessoa.add(btnPessoaJuridica);

        btnTipoFixo1.setSelected(true);
        btnTipoFixo2.setSelected(true);

        if (tipo == 1) {        // Construtor Tela Novo
            jCabecalho.setText("Novo Tutor/Doador");
            focus();
            btnSalvarEditar.addActionListener(e -> salvar());
            btnCancelar.addActionListener(e -> cancelar());
        } else if (tipo == 2) { //Construtor Tela Ver
            jCabecalho.setText("Ver Tutor/Doador");
            verTutorDoador();
            setLabels();
            carregaDados();
            btnCancelar.addActionListener(e -> dispose());
        } else if (tipo == 3) { // Construtor Tela Editar
            jCabecalho.setText("Editar Tutor/Doador");
            setLabels();
            btnSalvarEditar.addActionListener(e -> editar());
            btnCancelar.addActionListener(e -> cancelar());
            carregaDados();
            focus();
        }
    }

    private DfrTutorDoador(JFrame jFrame, boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //Ajusta o tamanho da tela
    public void ajusteTela() {
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(788, 700));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                if (tipo == 2) {
                    DfrTutorDoador.this.dispose();
                }
                else{
                    Utilidades.showCloseAlert(DfrTutorDoador.this);
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
        lbl.labelTutorDoador(ImgLogo);
        PanelCabecalho.add(ImgLogo, BorderLayout.CENTER);

    }

    /*Ajusta os componentes para ficarem sem borda*/
    public void ajusteComponents(int j) {
        //Ajusta o local dos campos
        Dimension f = PanelFundo.getSize();
        int i = tamCabecalho+60;

        PanelFundo.add(btnPessoaFisica, BorderLayout.NORTH);
        btnPessoaFisica.setLocation(20, i + 50);
        btnPessoaFisica.setSize(150, 20);

        PanelFundo.add(btnPessoaJuridica, BorderLayout.NORTH);
        btnPessoaJuridica.setLocation(200, i+50);
        btnPessoaJuridica.setSize(200, 20);

        PanelFundo.add(btnTutor, BorderLayout.NORTH);
        btnTutor.setLocation(20, i+100);
        btnTutor.setSize(150, 20);

        PanelFundo.add(btnLiberaAdocao, BorderLayout.NORTH);
        btnLiberaAdocao.setLocation(200, i+100);
        btnLiberaAdocao.setSize(200, 20);

        //Ajusta o local dos campos
        PanelFundo.add(jNome, BorderLayout.NORTH);
        jNome.setLocation(20, i + 200 - j);
        jNome.setSize((f.width / 2) + 100, 20);

        PanelFundo.add(jlNome, BorderLayout.NORTH);
        jlNome.setLocation(20, i + 175 - j);
        jlNome.setSize(250, 20);

        PanelFundo.add(jCPFCNPJ, BorderLayout.NORTH);
        jCPFCNPJ.setLocation((f.width / 2) + 140, i + 200);
        jCPFCNPJ.setSize((f.width / 2) - 180, 20);

        PanelFundo.add(jlCPFCNPJ, BorderLayout.NORTH);
        jlCPFCNPJ.setLocation((f.width / 2) + 140, i + 175);
        jlCPFCNPJ.setSize((f.width / 2) - 180, 20);

        PanelFundo.add(jNomeFantasia, BorderLayout.NORTH);
        jNomeFantasia.setLocation(20, i + 200);
        jNomeFantasia.setSize((f.width / 2) + 100, 20);

        PanelFundo.add(jlNomeFantasia, BorderLayout.NORTH);
        jlNomeFantasia.setLocation(20, i + 175);
        jlNomeFantasia.setSize(250, 20);

        PanelFundo.add(jEmail, BorderLayout.NORTH);
        jEmail.setLocation(20, i + 250);
        jEmail.setSize(f.width - 50, 20);

        PanelFundo.add(jlEmail, BorderLayout.NORTH);
        jlEmail.setLocation(20, i + 225);
        jlEmail.setSize(350, 20);

        PanelFundo.add(btnTipoFixo1, BorderLayout.NORTH);
        btnTipoFixo1.setLocation(20, i + 300);
        btnTipoFixo1.setSize(50, 20);

        PanelFundo.add(btnTipoCelular1, BorderLayout.NORTH);
        btnTipoCelular1.setLocation((f.width / 2) - 280, i + 300);
        btnTipoCelular1.setSize(75, 20);

        PanelFundo.add(btnTipoFixo2, BorderLayout.NORTH);
        btnTipoFixo2.setLocation(20, i + 350);
        btnTipoFixo2.setSize(50, 20);

        PanelFundo.add(btnTipoCelular2, BorderLayout.NORTH);
        btnTipoCelular2.setLocation((f.width / 2) - 280, i + 350);
        btnTipoCelular2.setSize(75, 20);

        PanelFundo.add(jTelefone1, BorderLayout.NORTH);
        jTelefone1.setLocation((f.width / 2) - 280 + (f.width / 2) - 280, i + 300);
        jTelefone1.setSize(250, 20);

        PanelFundo.add(jTelefone2, BorderLayout.NORTH);
        jTelefone2.setLocation((f.width / 2) - 280 + (f.width / 2) - 280, i + 350);
        jTelefone2.setSize(250, 20);

        PanelFundo.add(jlContato1, BorderLayout.NORTH);
        jlContato1.setLocation((f.width / 2) - 280 + (f.width / 2) - 280, i + 275);
        jlContato1.setSize(250, 20);

        PanelFundo.add(jlContato2, BorderLayout.NORTH);
        jlContato2.setLocation((f.width / 2) - 280 + (f.width / 2) - 280, i + 325);
        jlContato2.setSize(250, 20);

        PanelFundo.add(jCEP, BorderLayout.NORTH);
        jCEP.setLocation(20, i + 400);
        jCEP.setSize((f.width / 2) - 200, 20);

        PanelFundo.add(jlCEP, BorderLayout.NORTH);
        jlCEP.setLocation(20, i + 375);
        jlCEP.setSize((f.width / 2) - 200, 20);

        PanelFundo.add(btnCEP, BorderLayout.NORTH);
        btnCEP.setLocation((f.width / 2) - 180, i + 400);
        btnCEP.setSize(20, 20);

        PanelFundo.add(jCidade, BorderLayout.NORTH);
        jCidade.setLocation((f.width / 2) - 150, i + 400);
        jCidade.setSize((f.width / 2) + 80, 20);
        jCidade.setEditable(false);

        PanelFundo.add(jlCidade, BorderLayout.NORTH);
        jlCidade.setLocation((f.width / 2) - 150, i + 375);
        jlCidade.setSize(250, 20);

        PanelFundo.add(btnCidade, BorderLayout.NORTH);
        btnCidade.setLocation((f.width) - 70, i + 400);
        btnCidade.setSize(20, 20);

        PanelFundo.add(jLogradouro, BorderLayout.NORTH);
        jLogradouro.setLocation(20, i + 450);
        jLogradouro.setSize((f.width / 2) + 200, 20);

        PanelFundo.add(jlLogradouro, BorderLayout.NORTH);
        jlLogradouro.setLocation(20, i + 425);
        jlLogradouro.setSize(250, 20);

        PanelFundo.add(jNumero, BorderLayout.NORTH);
        jNumero.setLocation((f.width / 2) + 240, i + 450);
        jNumero.setSize((f.width / 2) - 280, 20);

        PanelFundo.add(jlNumero, BorderLayout.NORTH);
        jlNumero.setLocation((f.width / 2) + 240, i + 425);
        jlNumero.setSize(100, 20);

        PanelFundo.add(jComplemento, BorderLayout.NORTH);
        jComplemento.setLocation(20, i + 500);
        jComplemento.setSize((f.width / 2) - 40, 20);

        PanelFundo.add(jlComplemento, BorderLayout.NORTH);
        jlComplemento.setLocation(20, i + 475);
        jlComplemento.setSize(320, 20);

        PanelFundo.add(jBairro, BorderLayout.NORTH);
        jBairro.setLocation((f.width / 2), i + 500);
        jBairro.setSize((f.width / 2) - 40, 20);

        PanelFundo.add(jlBairro, BorderLayout.NORTH);
        jlBairro.setLocation((f.width / 2), i + 475);
        jlBairro.setSize((f.width / 2) - 40, 20);

        PanelFundo.add(btnCancelar, BorderLayout.NORTH);
        btnCancelar.setLocation(600, f.height - 80);

        PanelFundo.add(btnSalvarEditar, BorderLayout.NORTH);
        btnSalvarEditar.setLocation(500, f.height - 80);

        // Formata os campos
        txt.formatarNomeFantasia(jlNomeFantasia, jNomeFantasia);
        txt.formatarLogradouro(jlLogradouro, jLogradouro);
        txt.formatarNumero(jlNumero, jNumero);
        txt.formatarComplemento(jlComplemento, jComplemento);
        txt.formatarBairro(jlBairro, jBairro);
        txt.formatarCidade(jlCidade, jCidade);
        txt.formatarTipoTelefone(jTipoTelefone1);
        txt.formatarTipoTelefone(jTipoTelefone2);
        txt.formatarTelefone(jlContato1, jTelefone1);
        txt.formatarTelefone(jlContato2, jTelefone2);
        txt.formatarEmail(jlEmail, jEmail);
        txt.formatarCEP(jlCEP, jCEP);

        //Formata os botõeses
        btn.botaoSalvar(btnSalvarEditar);
        btn.botaoCancelar(btnCancelar);
        btn.botaoPessoaFisica(btnPessoaFisica);
        btn.botaoPessoaJuridica(btnPessoaJuridica);
        btn.botaoTutor(btnTutor);
        btn.botaoLiberaDoacao(btnLiberaAdocao);
        btn.botaoBusca(btnCidade);
        btn.botaoBusca(btnCEP);
        btn.botaoContatoFixo(btnTipoFixo1);
        btn.botaoContatoFixo(btnTipoFixo2);
        btn.botaoContatoCelular(btnTipoCelular1);
        btn.botaoContatoCelular(btnTipoCelular2);

        btnCancelar.addActionListener(e -> dispose());
        btnCidade.addActionListener(e -> selecionaCidade());
        btnCEP.addActionListener(e -> consultaCep());

        btnPessoaFisica.addActionListener(e -> {
            pessoaFisica();
            ajusteComponents(0);
        });

        btnPessoaJuridica.addActionListener(e -> {
            pessoaJuridica();
            ajusteComponents(50); // Ajuste para dar espaço para Nome Fantasia
        });

        btnTipoFixo1.addActionListener(e -> {
            if (Formatacao.removerFormatacao(jTelefone1.getText()).length() > 0) {
                focus.focusTelefoneLost(jlContato1, jTelefone1);
            }
        });

        //add disallow listener
        btnTipoCelular1.addActionListener(e -> {
            if (Formatacao.removerFormatacao(jTelefone1.getText()).length() > 0) {
                focus.focusTelefoneLost(jlContato1, jTelefone1);
            }
        });
        btnTipoFixo2.addActionListener(e -> {
            if (Formatacao.removerFormatacao(jTelefone2.getText()).length() > 0) {
                focus.focusTelefoneLost(jlContato2, jTelefone2);
            }
        });

        //add disallow listener
        btnTipoCelular2.addActionListener(e -> {
            if (Formatacao.removerFormatacao(jTelefone2.getText()).length() > 0) {
                focus.focusTelefoneLost(jlContato2, jTelefone2);
            }
        });
    }

    public void cancelar() {
        Utilidades.showCloseAlert(DfrTutorDoador.this);
    }

    public void pessoaFisica() {

        if (Formatacao.removerFormatacao(jCPFCNPJ.getText()).length() > 0) {
            focus.focusCPFLost(jlCPFCNPJ, jCPFCNPJ);
        }
        txt.formatarNome(jlNome, jNome);
        jNomeFantasia.setVisible(false);

        jNome.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent evt) {

                focus.focusNomeGained(jlNome, jNome);
            }

            @Override
            public void focusLost(FocusEvent evt) {

                focus.focusNomeLost(jlNome, jNome);
            }
        });
        jNomeFantasia.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent evt) {

                focus.focusNomeFantasiaGained(jlNomeFantasia, jNomeFantasia);
            }

            @Override
            public void focusLost(FocusEvent evt) {

                focus.focusNomeFantasiaLost(jlNomeFantasia, jNomeFantasia);
            }
        });
    }

    public void pessoaJuridica() {
        txt.formatarRazaoSocial(jlNome, jNome);
        jNomeFantasia.setVisible(true);

        final Focus focus = new Focus();

        if (Formatacao.removerFormatacao(jCPFCNPJ.getText()).length() > 0) {
            focus.focusCNPJLost(jlCPFCNPJ, jCPFCNPJ);
        }

        jNome.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent evt) {

                focus.focusRazaoSocialGained(jlNome, jNome);
            }

            @Override
            public void focusLost(FocusEvent evt) {

                focus.focusRazaoSocialLost(jlNome, jNome);
            }
        });
    }

    public void selecionaCidade() {
        DfrBusca buscaCidade = new DfrBusca(DfrTutorDoador.this);
        buscaCidade.setVisible(true);
    }

    public void setLabels() {
//        jlCPFCNPJ.setText("CPF / CNPJ");
        jlLogradouro.setText("Logradouro");
        jlNumero.setText("Número");
        jlComplemento.setText("Complemento");
        jlBairro.setText("Bairro");
        jlCidade.setText("Cidade");
        jlCEP.setText("CEP");
        jlContato1.setText("Contato");
        jlEmail.setText("E-mail");
        jlNomeFantasia.setText("Nome Fantasia");
        jlNome.setText("Nome");
    }

    private void carregaDados() {

        if (tutorDoador instanceof Pessoa) {
            Pessoa pessoa = (Pessoa) tutorDoador;

            if (pessoa.getLiberarAdocao() > 0)
                btnLiberaAdocao.setSelected(true);

            Endereco endereco = pessoa.getEndereco();
            if (endereco != null) {
                jLogradouro.setText(endereco.getLogradouro());
                jNumero.setText(endereco.getNumero());
                jComplemento.setText(endereco.getComplemento());
                jBairro.setText(endereco.getBairro());
                jCEP.setText(Formatacao.formataCEP(endereco.getCep()));

                Cidade cidade = endereco.getCidade();
                if (cidade != null) {
                    jCidade.setText(cidade.getDescricao() + " - " + cidade.getUf());
                    this.cidade = cidade;
                }
            }

            List<Contato> contatos = pessoa.getContatos();
            if (contatos != null) {
                String email = "";
                String fone = "";
                String celular = "";

                for (Contato contato : contatos) {
                    if (contato.getTipoContato().getId() == 3)
                        email = contato.getContato();
                    if (contato.getTipoContato().getId() == 2) {
                        if (fone.isEmpty())
                            fone = contato.getContato();
                        else celular = contato.getContato();
                    }

                    if (contato.getTipoContato().getId() == 1) {
                        if (celular.isEmpty())
                            celular = contato.getContato();
                        else fone = contato.getContato();
                    }
                }

                if (fone.length() == 10) {
                    fone = Formatacao.formataTelefone(fone);
                    btnTipoFixo1.setSelected(true);
                } else if (fone.length() == 11) {
                    fone = Formatacao.formataCelular(fone);
                    btnTipoCelular1.setSelected(true);
                }
                if (celular.length() == 10) {
                    celular = Formatacao.formataTelefone(celular);
                    btnTipoFixo2.setSelected(true);
                } else if (celular.length() == 11) {
                    celular = Formatacao.formataCelular(celular);
                    btnTipoCelular2.setSelected(true);
                }

                jEmail.setText(email);
                jTelefone1.setText(fone);
                jTelefone2.setText(celular);
            }

            // Valida se é pessoa fisica ou juridica
            if (tutorDoador instanceof PessoaFisica) {
                PessoaFisica pf = (PessoaFisica) tutorDoador;

                jNome.setText(pf.getNome());
                jCPFCNPJ.setText(Formatacao.formataCPF(pf.getCpf()));
                btnPessoaFisica.setSelected(true);
                jlNomeFantasia.setVisible(false);
                jlCPFCNPJ.setText("CPF");

            } else if (tutorDoador instanceof PessoaJuridica) {
                PessoaJuridica pj = (PessoaJuridica) tutorDoador;

                jNome.setText(pj.getNomeFantasia());
                jCPFCNPJ.setText(Formatacao.formataCNPJ(pj.getCnpj()));
                btnPessoaJuridica.setSelected(true);
                jlNome.setVisible(false);
                jlCPFCNPJ.setText("CNPJ");
            }

        }
    }

    public void verTutorDoador() {
        Dimension f = PanelFundo.getSize();
        jNome.setEditable(false);
        jLogradouro.setEditable(false);
        jNumero.setEditable(false);
        jComplemento.setEditable(false);
        jBairro.setEditable(false);
        jCPFCNPJ.setEditable(false);
        jEmail.setEditable(false);
        jTelefone1.setEditable(false);
        jTelefone2.setEditable(false);
        jCidade.setEditable(false);
        btnCidade.setVisible(false);
        jTipoTelefone1.setVisible(false);
        jTipoTelefone2.setVisible(false);
        btnCEP.setVisible(false);
        btnTutor.setEnabled(false); // fica cinza claro mas n pode editar
        btnLiberaAdocao.setEnabled(false); // fica cinza claro mas n pode editar
        btn.botaoEditarDialog(btnSalvarEditar);

        btnSalvarEditar.addActionListener(e -> {
            DfrTutorDoador editar = new DfrTutorDoador(this.parent, 3, tutorDoador);
            editar.setVisible(true);
        });
    }

    public void salvar() {
        if (!validaCampos())
            return;

        Gson gson = new Gson();
        Pessoa pessoa = (Pessoa) tutorDoador;

        // Endereço
        Endereco endereco = pessoa.getEndereco();
        endereco.setLogradouro(jLogradouro.getText());
        endereco.setNumero(jNumero.getText());
        endereco.setComplemento(jComplemento.getText());
        endereco.setBairro(jBairro.getText());
        endereco.setCep(Utilidades.somenteNumeros(jCEP.getText()));
        endereco.setCidade(this.cidade);

        // Contato
        ArrayList<Contato> contatos = new ArrayList<>();
        String contato1 = jTelefone1.getText();
        String contato2 = jTelefone2.getText();
        String email = jEmail.getText();

        Contato contato;
        if (!contato1.isEmpty()) {
            contato = new Contato();
            TipoContato tipoContato = btnTipoFixo1.isSelected() ? new TipoContato(1) : new TipoContato(2);

            contato.setContato(contato1);
            contato.setStatus(1);
            contato.setTipoContato(tipoContato);
            contatos.add(contato);
        }

        if (!contato2.isEmpty()) {
            contato = new Contato();
            TipoContato tipoContato = btnTipoFixo2.isSelected() ? new TipoContato(1) : new TipoContato(2);

            contato.setContato(contato2);
            contato.setStatus(1);
            contato.setTipoContato(tipoContato);
            contatos.add(contato);
        }

        if (!email.isEmpty()) {
            contato = new Contato();
            TipoContato tipoContato = new TipoContato(3);

            contato.setContato(email);
            contato.setStatus(1);
            contato.setTipoContato(tipoContato);
            contatos.add(contato);
        }

        int tipoPessoa = btnTutor.isSelected() ? 2 : 3; //- 2=Tutor, 3=Doador
        System.out.println("Libera Adoção:" + btnLiberaAdocao.isSelected());
        int liberarAdocao = btnLiberaAdocao.isSelected() ? 1 : 0; //- 1=Sim, 0=Nao

        if (btnPessoaFisica.isSelected()) {
            PessoaFisica pf = new PessoaFisica();

            pf.setNome(jNome.getText());
            pf.setCpf(jCPFCNPJ.getText());
            pf.setContatos(contatos);
            pf.setEndereco(endereco);
            pf.setIdTipoPessoa(tipoPessoa);
            pf.setLiberarAdocao(liberarAdocao);

            String json = gson.toJson(pf);
            ServicoCliente sv = new ServicoCliente();
            if (sv.enviaDados("pessoaFisica", ServicoCliente.POST, json, ServicoCliente.EMPTY_PARAMS, PessoaFisica.class)) {
                Utilidades.showSucesso(this, "Tutor/Doador inserido com seucesso!");

                parent.resetaDados();
                this.dispose();
            } else {
                Utilidades.showErro(this, (Erro) sv.getObject());
            }
        } else if (btnPessoaJuridica.isSelected()) {
//            PessoaJuridica pj = (PessoaJuridica) pessoa;
            PessoaJuridica pj = new PessoaJuridica();

            pj.setCnpj(jCPFCNPJ.getText());
            pj.setNomeFantasia(jNomeFantasia.getText());
            pj.setRazaoSocial(jNome.getText());

            pj.setContatos(contatos);
            pj.setEndereco(endereco);
            pj.setIdTipoPessoa(tipoPessoa);
            pj.setLiberarAdocao(liberarAdocao);

            String json = gson.toJson(pj);

            ServicoCliente sv = new ServicoCliente();
            if (sv.enviaDados("pessoaJuridica", ServicoCliente.POST, json, ServicoCliente.EMPTY_PARAMS, PessoaJuridica.class)) {
                Utilidades.showSucesso(this, "Tutor/Doador inserido com seucesso!");

                parent.resetaDados();
                this.dispose();
            } else {
                Utilidades.showErro(this, (Erro) sv.getObject());
            }

        }
    }

    public void editar() {
        if (this.tutorDoador == null) {
            Utilidades.showErro(this, new Erro("DF.02", "Erro ao identificar tutor/doador que deve ser editado"));
            return;
        }

        if (!validaCampos())
            return;

        Gson gson = new Gson();
        if (tutorDoador instanceof Pessoa) {
            Pessoa pessoa = (Pessoa) tutorDoador;

            // Endereço
            Endereco endereco = pessoa.getEndereco();
            endereco.setLogradouro(jLogradouro.getText());
            endereco.setNumero(jNumero.getText());
            endereco.setComplemento(jComplemento.getText());
            endereco.setBairro(jBairro.getText());
            endereco.setCep(Utilidades.somenteNumeros(jCEP.getText()));
            endereco.setCidade(this.cidade);

            // Contato
            ArrayList<Contato> contatos = new ArrayList<>();
            String contato1 = jTelefone1.getText();
            String contato2 = jTelefone2.getText();
            String email = jEmail.getText();

            Contato contato;
            if (!contato1.isEmpty()) {
                contato = new Contato();
                TipoContato tipoContato = btnTipoFixo1.isSelected() ? new TipoContato(1) : new TipoContato(2);

                contato.setContato(contato1);
                contato.setStatus(1);
                contato.setTipoContato(tipoContato);
                contatos.add(contato);
            }

            if (!contato2.isEmpty()) {
                contato = new Contato();
                TipoContato tipoContato = btnTipoFixo2.isSelected() ? new TipoContato(1) : new TipoContato(2);

                contato.setContato(contato2);
                contato.setStatus(1);
                contato.setTipoContato(tipoContato);
                contatos.add(contato);
            }

            if (!email.isEmpty()) {
                contato = new Contato();
                TipoContato tipoContato = new TipoContato(3);

                contato.setContato(email);
                contato.setStatus(1);
                contato.setTipoContato(tipoContato);
                contatos.add(contato);
            }
            pessoa.setContatos(contatos);
            pessoa.setLiberarAdocao(btnLiberaAdocao.isSelected() ? 1 : 0);

            if (tutorDoador instanceof PessoaFisica) {
                PessoaFisica pf = (PessoaFisica) tutorDoador;

                pf.setCpf(jCPFCNPJ.getText());
                pf.setNome(jNome.getText());

                String json = gson.toJson(pf);

                HashMap<String, String> map = new HashMap<>();
                map.put("id", pf.getId().toString());

                ServicoCliente sv = new ServicoCliente();
                if (sv.enviaDados("pessoaFisica", ServicoCliente.PUT, json, map, Usuario.class)) {

                    String mensagem = "Tutor/Doador atualizado com seucesso!";
                    if (map.isEmpty())
                        mensagem = "Tutor/Doador inserido com seucesso!";

                    Utilidades.showSucesso(this, mensagem);

                    parent.resetaDados();
                    this.dispose();
                } else {
                    Utilidades.showErro(this, (Erro) sv.getObject());
                }
            } else if (tutorDoador instanceof PessoaJuridica) {
                PessoaJuridica pj = (PessoaJuridica) tutorDoador;

                pj.setCnpj(jCPFCNPJ.getText());
                pj.setNomeFantasia(jNomeFantasia.getText());
                pj.setRazaoSocial(jNome.getText());

                String json = gson.toJson(pj);

                HashMap<String, String> map = new HashMap<>();
                map.put("id", pj.getId().toString());

                ServicoCliente sv = new ServicoCliente();
                if (sv.enviaDados("pessoaJuridica", ServicoCliente.PUT, json, map, Usuario.class)) {

                    String mensagem = "Tutor/Doador atualizado com seucesso!";
                    if (map.isEmpty())
                        mensagem = "Tutor/Doador inserido com seucesso!";

                    Utilidades.showSucesso(this, mensagem);

                    parent.resetaDados();
                    this.dispose();
                } else {
                    Utilidades.showErro(this, (Erro) sv.getObject());
                }
            }
        }
    }

    private boolean validaCampos() {
        if (!Validacao.validarNome(jNome.getText())) {
            Utilidades.showAlerta(this, "Digite um nome válido!");
            return false;
        }
        if (!Validacao.validarEmail(jEmail.getText())) {
            Utilidades.showAlerta(this, "Digite um e-mail válido!");
            return false;
        }
        if (!jLogradouro.getText().isEmpty()) {
            if (jCidade.getText().isEmpty()) {
                Utilidades.showAlerta(this, "Digite o endereço correto!");
                return false;
            }
        }

        return true;
    }

    public void focus() {

        jLogradouro.addFocusListener(new FocusAdapter() {

            public void focusGained(FocusEvent evt) {
                focus.focusLogradouroGained(jlLogradouro, jLogradouro);
            }

            public void focusLost(FocusEvent evt) {
                focus.focusLogradouroLost(jlLogradouro, jLogradouro);
            }

        });
        jNumero.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                focus.focusNumeroGained(jlNumero, jNumero);
            }

            public void focusLost(FocusEvent evt) {
                focus.focusNumeroLost(jlNumero, jNumero);
            }

        });
        jComplemento.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                focus.focusComplementoGained(jlComplemento, jComplemento);
            }

            public void focusLost(FocusEvent evt) {
                focus.focusComplementoLost(jlComplemento, jComplemento);
            }

        });
        jBairro.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                focus.focusBairroGained(jlBairro, jBairro);
            }

            public void focusLost(FocusEvent evt) {
                focus.focusBairroLost(jlBairro, jBairro);
            }

        });
        jTelefone1.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                String tipoContato;
                if (btnTipoCelular1.isSelected()) {
                    tipoContato = "Celular";
                } else {
                    tipoContato = "";
                }
                focus.focusTelefoneGained(jlContato1, jTelefone1, tipoContato);
            }

            public void focusLost(FocusEvent evt) {


                if (Formatacao.removerFormatacao(jTelefone1.getText()).length() == 0) {
                    focus.focusTelefoneLost(jlContato1, jTelefone1);

                }
            }

        });
        jTelefone2.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                String tipoContato;
                if (btnTipoCelular2.isSelected()) {
                    tipoContato = "Celular";
                } else {
                    tipoContato = "";
                }
                focus.focusTelefoneGained(jlContato2, jTelefone2, tipoContato);
            }

            public void focusLost(FocusEvent evt) {

                if (Formatacao.removerFormatacao(jTelefone2.getText()).length() == 0) {
                    focus.focusTelefoneLost(jlContato2, jTelefone2);

                }
            }
        });
        jEmail.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                focus.focusEmailGained(jlEmail, jEmail);
            }

            public void focusLost(FocusEvent evt) {
                focus.focusEmailLost(jlEmail, jEmail);
            }
        });

        jCEP.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                focus.focusCEPGained(jlCEP, jCEP);
            }

            public void focusLost(FocusEvent evt) {
                if (Formatacao.removerFormatacao(jCEP.getText()).length() == 0) {
                    focus.focusCEPLost(jlCEP, jCEP);
                }
            }

        });

        jCPFCNPJ.addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent evt) {
                if (btnPessoaFisica.isSelected()) {
                    focus.focusCPFGained(jlCPFCNPJ, jCPFCNPJ);
                } else {
                    focus.focusCNPJGained(jlCPFCNPJ, jCPFCNPJ);
                }

            }

            @Override
            public void focusLost(FocusEvent evt) {
                if (btnPessoaFisica.isSelected()) {
                    if (Formatacao.removerFormatacao(jCPFCNPJ.getText()).length() == 0) {
                        focus.focusCPFLost(jlCPFCNPJ, jCPFCNPJ);
                    }
                } else {
                    if (Formatacao.removerFormatacao(jCPFCNPJ.getText()).length() == 0) {
                        focus.focusCNPJLost(jlCPFCNPJ, jCPFCNPJ);
                    }
                }
            }
        });
        jCEP.addActionListener(e -> consultaCep());
    }

    public void consultaCep() {
        String cep = jCEP.getText();
        if (cep.length() > 8) {

            Endereco endereco = Utilidades.getEnderecoByViaCep(cep);
            if (endereco != null) {

                Cidade cidade = endereco.getCidade();
                if (cidade != null){
                    jCidade.setText(cidade.getDescricao() + " - " + cidade.getUf());
                    this.cidade = cidade;
                }
                if (endereco.getLogradouro() != null) jLogradouro.setText(endereco.getLogradouro());
                if (endereco.getBairro() != null) jBairro.setText(endereco.getBairro());

                if (!jLogradouro.getText().isEmpty()) jlLogradouro.setText("Logradouro");
                else {
                    jLogradouro.setText("Logradouro");
                }
                if (!jBairro.getText().isEmpty()) jlBairro.setText("Bairro");
                else {
                    jBairro.setText("Bairro");
                }
                jlCidade.setText("Cidade");
            }

        } else {
            Utilidades.showAlerta(this, "Por favor, digite um CEP válido!");
        }
    }

    public void setCidade(Cidade cidade) {
        if (tutorDoador instanceof Pessoa) {
            ((Pessoa) tutorDoador).getEndereco().setCidade(cidade);
            jCidade.setText(cidade.getDescricao() + " - " + cidade.getUf());
        }
        this.cidade = cidade;
    }
}
