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

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

import principais.DfrBusca;
import utilidades.*;

public final class DfrVoluntario extends javax.swing.JDialog {

    private Usuario usuario;
    private Cidade cidade;

    private JTextField jNome = new JTextField();
    private JTextField jLogradouro = new JTextField();
    private JTextField jNumero = new JTextField();
    private JTextField jComplemento = new JTextField();
    private JTextField jBairro = new JTextField();
    private JTextField jEmail = new JTextField();
    private JTextField jLogin = new JTextField();
    private JTextField jCidade = new JTextField();

    private JFormattedTextField jTelefone1 = new JFormattedTextField();
    private JFormattedTextField jCPF = new JFormattedTextField();
    private JFormattedTextField jTelefone2 = new JFormattedTextField();
    private JFormattedTextField jCEP = new JFormattedTextField();

    private JPasswordField jSenha = new JPasswordField();
    private JPasswordField jSenha1 = new JPasswordField();

    private JLabel jlNome = new JLabel();
    private JLabel jlCPF = new JLabel();
    private JLabel jlLogradouro = new JLabel();
    private JLabel jlNumero = new JLabel();
    private JLabel jlComplemento = new JLabel();
    private JLabel jlBairro = new JLabel();
    private JLabel jlCidade = new JLabel();
    private JLabel jlContato1 = new JLabel();
    private JLabel jlContato2 = new JLabel();
    private JLabel jlEmail = new JLabel();
    private JLabel jlLogin = new JLabel();
    private JLabel jlSenha = new JLabel();
    private JLabel jlSenha1 = new JLabel();
    private JLabel jlCEP = new JLabel();
    private JLabel jCabecalho = new JLabel();
    private JPanel PanelFundo = new JPanel(new BorderLayout());
    private JPanel PanelCabecalho = new JPanel(new BorderLayout());
    private JLabel jlTipoUser = new JLabel();
    private JLabel jTipoUser = new JLabel();

    private JButton btnSalvarEditar = new JButton();
    private JButton btnCancelar = new JButton();
    private JButton btnCidade = new JButton();
    private JButton btnCEP = new JButton();

    private JRadioButton btnTipoFixo1 = new JRadioButton();
    private JRadioButton btnTipoCelular1 = new JRadioButton();
    private JRadioButton btnTipoFixo2 = new JRadioButton();
    private JRadioButton btnTipoCelular2 = new JRadioButton();
    private JRadioButton btnAdministrador = new JRadioButton();
    private JRadioButton btnOperador = new JRadioButton();

    private ButtonGroup tipoContato1 = new ButtonGroup();
    private ButtonGroup tipoContato2 = new ButtonGroup();
    private ButtonGroup tipoUsuario = new ButtonGroup();

    private FrConfig config = new FrConfig();
    private ComponentLabel lbl = new ComponentLabel();
    private ComponentButton btn = new ComponentButton();
    final Focus focus = new Focus();
    int tipo;

    private IfrVoluntario tela;

    int tamCabecalho;

    private DfrVoluntario(JFrame jFrame, boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    DfrVoluntario(java.awt.Frame parent, boolean modal, int tipo, Usuario usuario, IfrVoluntario tela) {
        super(parent, modal);
        this.setLocationRelativeTo(null);
        ajusteTela();
        ajusteComponents();
        tipoContatoTelefone();

        this.tela = tela;
        this.tipo = tipo;

        if (usuario == null) this.usuario = new Usuario();
        else this.usuario = usuario;

        tipoContato1.add(btnTipoFixo1);
        tipoContato1.add(btnTipoCelular1);
        tipoContato2.add(btnTipoFixo2);
        tipoContato2.add(btnTipoCelular2);

        tipoUsuario.add(btnAdministrador);
        tipoUsuario.add(btnOperador);

        btnTipoFixo1.setSelected(true);
        btnTipoFixo2.setSelected(true);

        if (tipo == 1) {        // Construtor Tela Novo
            focus();
            jCabecalho.setText("Novo Voluntário");
            btnSalvarEditar.addActionListener(e -> salvar());
            btnCancelar.addActionListener(e -> cancelar());
        } else if (tipo == 2) { // Construtor Tela Ver
            jCabecalho.setText("Ver Voluntário");
            carregaDados();
            this.setLabels();
            verVoluntario();
            btnCancelar.addActionListener(e -> dispose());
        } else if (tipo == 3) { // Construtor Tela Editar
            focus();
            jCabecalho.setText("Editar Voluntário");
            btnSalvarEditar.addActionListener(e -> editar());
            btnCancelar.addActionListener(e -> cancelar());
            this.setLabels();
            carregaDados();
            jSenha.setEchoChar('•');
            jSenha1.setEchoChar('•');
        }
    }

    public void ajusteTela() {
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(788, 700));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                if (tipo == 2) {
                    DfrVoluntario.this.dispose();
                }
                else{
                    Utilidades.showCloseAlert(DfrVoluntario.this);
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
        lbl.labelVoluntario(ImgLogo);
        PanelCabecalho.add(ImgLogo, BorderLayout.CENTER);
    }

    private void tipoContatoTelefone() {
        btnTipoFixo1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Formatacao.removerFormatacao(jTelefone1.getText()).length() > 0) {
                    focus.focusTelefoneLost(jlContato1, jTelefone1);
                }
            }
        });
        btnTipoCelular1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Formatacao.removerFormatacao(jTelefone1.getText()).length() > 0) {
                    focus.focusTelefoneLost(jlContato1, jTelefone1);
                }
            }
        });
        btnTipoFixo2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Formatacao.removerFormatacao(jTelefone2.getText()).length() > 0) {
                    focus.focusTelefoneLost(jlContato2, jTelefone2);
                }
            }
        });

        //add disallow listener
        btnTipoCelular2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Formatacao.removerFormatacao(jTelefone2.getText()).length() > 0) {
                    focus.focusTelefoneLost(jlContato2, jTelefone2);
                }
            }
        });
    }

    /*Ajusta os componentes para ficarem sem borda*/
    public void ajusteComponents() {

        //Ajusta o local dos campos
        int i = tamCabecalho+100;

        Dimension f = PanelFundo.getSize();
        PanelFundo.add(jNome, BorderLayout.NORTH);
        jNome.setLocation(20, i + 50);
        jNome.setSize((f.width / 2) + 100, 20);

        PanelFundo.add(jlNome, BorderLayout.NORTH);
        jlNome.setLocation(20, i + 25);
        jlNome.setSize(250, 20);

        PanelFundo.add(jCPF, BorderLayout.NORTH);
        jCPF.setLocation((f.width / 2) + 140, i + 50);
        jCPF.setSize((f.width / 2) + 100, 20);

        PanelFundo.add(jlCPF, BorderLayout.NORTH);
        jlCPF.setLocation((f.width / 2) + 140, i + 25);
        jlCPF.setSize((f.width / 2) - 10, 20);

        PanelFundo.add(jEmail, BorderLayout.NORTH);
        jEmail.setLocation(20, i + 100);
        jEmail.setSize((f.width / 2) + 100, 20);

        PanelFundo.add(jlEmail, BorderLayout.NORTH);
        jlEmail.setLocation(20, i + 75);
        jlEmail.setSize(350, 20);

        PanelFundo.add(jlTipoUser, BorderLayout.NORTH);
        jlTipoUser.setLocation((f.width / 2) + 140, i + 100);
        jlTipoUser.setSize((f.width / 2) - 40, 20);

        PanelFundo.add(jLogin, BorderLayout.NORTH);
        jLogin.setLocation(20, i + 150);
        jLogin.setSize((f.width / 2) + 100, 20);

        PanelFundo.add(jlLogin, BorderLayout.NORTH);
        jlLogin.setLocation(20, i + 125);
        jlLogin.setSize((f.width / 2) - 40, 20);

        PanelFundo.add(btnOperador, BorderLayout.NORTH);
        btnOperador.setLocation((f.width / 2) + 140, i + 125);
        btnOperador.setSize((f.width / 2) - 200, 20);

        PanelFundo.add(jTipoUser, BorderLayout.NORTH);
        jTipoUser.setLocation((f.width / 2) + 140, i + 125);
        jTipoUser.setSize((f.width / 2) - 200, 20);
        jTipoUser.setVisible(false);

        PanelFundo.add(btnAdministrador, BorderLayout.NORTH);
        btnAdministrador.setLocation((f.width / 2) + 140, i + 150);
        btnAdministrador.setSize((f.width / 2) - 200, 20);

        PanelFundo.add(jSenha, BorderLayout.NORTH);
        jSenha.setLocation(20, i + 200);
        jSenha.setSize((f.width / 2) - 40, 20);

        PanelFundo.add(jlSenha, BorderLayout.NORTH);
        jlSenha.setLocation(20, i + 175);
        jlSenha.setSize((f.width / 2) - 40, 20);

        PanelFundo.add(jSenha1, BorderLayout.NORTH);
        jSenha1.setLocation((f.width / 2), i + 200);
        jSenha1.setSize((f.width / 2) - 40, 20);

        PanelFundo.add(jlSenha1, BorderLayout.NORTH);
        jlSenha1.setLocation((f.width / 2), i + 175);
        jlSenha1.setSize((f.width / 2) - 40, 20);

        PanelFundo.add(btnTipoFixo1, BorderLayout.NORTH);
        btnTipoFixo1.setLocation(20, i + 250);
        btnTipoFixo1.setSize(50, 20);

        PanelFundo.add(btnTipoCelular1, BorderLayout.NORTH);
        btnTipoCelular1.setLocation((f.width / 2) - 280, i + 250);
        btnTipoCelular1.setSize(75, 20);

        PanelFundo.add(btnTipoFixo2, BorderLayout.NORTH);
        btnTipoFixo2.setLocation(20, i + 300);
        btnTipoFixo2.setSize(50, 20);

        PanelFundo.add(btnTipoCelular2, BorderLayout.NORTH);
        btnTipoCelular2.setLocation((f.width / 2) - 280, i + 300);
        btnTipoCelular2.setSize(75, 20);

        PanelFundo.add(jTelefone1, BorderLayout.NORTH);
        jTelefone1.setLocation((f.width / 2) - 280 + (f.width / 2) - 280, i + 250);
        jTelefone1.setSize(250, 20);

        PanelFundo.add(jTelefone2, BorderLayout.NORTH);
        jTelefone2.setLocation((f.width / 2) - 280 + (f.width / 2) - 280, i + 300);
        jTelefone2.setSize(250, 20);

        PanelFundo.add(jlContato1, BorderLayout.NORTH);
        jlContato1.setLocation((f.width / 2) - 280 + (f.width / 2) - 280, i + 225);
        jlContato1.setSize(250, 20);

        PanelFundo.add(jlContato2, BorderLayout.NORTH);
        jlContato2.setLocation((f.width / 2) - 280 + (f.width / 2) - 280, i + 275);
        jlContato2.setSize(250, 20);

        PanelFundo.add(jCEP, BorderLayout.NORTH);
        jCEP.setLocation(20, i + 350);
        jCEP.setSize((f.width / 2) - 200, 20);

        PanelFundo.add(jlCEP, BorderLayout.NORTH);
        jlCEP.setLocation(20, i + 325);
        jlCEP.setSize((f.width / 2) - 200, 20);

        PanelFundo.add(btnCEP, BorderLayout.NORTH);
        btnCEP.setLocation((f.width / 2) - 180, i + 350);
        btnCEP.setSize(20, 20);

        PanelFundo.add(jCidade, BorderLayout.NORTH);
        jCidade.setLocation((f.width / 2) - 150, i + 350);
        jCidade.setSize((f.width / 2) + 80, 20);
        jCidade.setEditable(false);

        PanelFundo.add(jlCidade, BorderLayout.NORTH);
        jlCidade.setLocation((f.width / 2) - 150, i + 325);
        jlCidade.setSize(250, 20);

        PanelFundo.add(btnCidade, BorderLayout.NORTH);
        btnCidade.setLocation((f.width) - 70, i + 350);
        btnCidade.setSize(20, 20);

        PanelFundo.add(jLogradouro, BorderLayout.NORTH);
        jLogradouro.setLocation(20, i + 400);
        jLogradouro.setSize((f.width / 2) + 200, 20);

        PanelFundo.add(jlLogradouro, BorderLayout.NORTH);
        jlLogradouro.setLocation(20, i + 375);
        jlLogradouro.setSize(250, 20);

        PanelFundo.add(jNumero, BorderLayout.NORTH);
        jNumero.setLocation((f.width / 2) + 240, i + 400);
        jNumero.setSize((f.width / 2) - 280, 20);

        PanelFundo.add(jlNumero, BorderLayout.NORTH);
        jlNumero.setLocation((f.width / 2) + 240, i + 375);
        jlNumero.setSize(100, 20);

        PanelFundo.add(jComplemento, BorderLayout.NORTH);
        jComplemento.setLocation(20, i + 450);
        jComplemento.setSize((f.width / 2) - 40, 20);

        PanelFundo.add(jlComplemento, BorderLayout.NORTH);
        jlComplemento.setLocation(20, i + 425);
        jlComplemento.setSize(320, 20);

        PanelFundo.add(jBairro, BorderLayout.NORTH);
        jBairro.setLocation((f.width / 2), i + 450);
        jBairro.setSize((f.width / 2) - 40, 20);

        PanelFundo.add(jlBairro, BorderLayout.NORTH);
        jlBairro.setLocation((f.width / 2), i + 425);
        jlBairro.setSize((f.width / 2) - 40, 20);

        PanelFundo.add(btnCancelar, BorderLayout.NORTH);
        btnCancelar.setLocation(600, f.height - 80);

        PanelFundo.add(btnSalvarEditar, BorderLayout.NORTH);
        btnSalvarEditar.setLocation(500, f.height - 80);

        ComponentText txt = new ComponentText();
        // Formata os campos
        txt.formatarNome(jlNome, jNome);
        txt.formatarCPF(jlCPF, jCPF);
        txt.formatarLogradouro(jlLogradouro, jLogradouro);
        txt.formatarNumero(jlNumero, jNumero);
        txt.formatarComplemento(jlComplemento, jComplemento);
        txt.formatarBairro(jlBairro, jBairro);
        txt.formatarCidade(jlCidade, jCidade);
        txt.formatarTelefone(jlContato1, jTelefone1);
        txt.formatarTelefone(jlContato2, jTelefone2);
        txt.formatarEmail(jlEmail, jEmail);
        txt.formatarLogin(jlLogin, jLogin);
        txt.formatarSenha(jlSenha, jSenha);
        txt.formatarConfirmarSenha(jlSenha1, jSenha1);
        txt.formatarCEP(jlCEP, jCEP);
        txt.formatarTipoUser(jlTipoUser);

        //Formata os botões
        btn.botaoSalvar(btnSalvarEditar);
        btn.botaoCancelar(btnCancelar);
        btn.botaoBusca(btnCidade);
        btn.botaoBusca(btnCEP);
        btn.botaoContatoFixo(btnTipoFixo1);
        btn.botaoContatoFixo(btnTipoFixo2);
        btn.botaoContatoCelular(btnTipoCelular1);
        btn.botaoContatoCelular(btnTipoCelular2);
        btn.botaoAdmnistrador(btnAdministrador);
        btn.botaoOperador(btnOperador);

        btnCEP.addActionListener(e -> consultaCep());
        btnCidade.addActionListener(e -> selecionaCidade());
    }

    public void setLabels() {
        jlNome.setText("Nome");
        jlCPF.setText("CPF");
        jlLogradouro.setText("Logradouro");
        jlNumero.setText("Número");
        jlComplemento.setText("Complemento");
        jlBairro.setText("Bairro");
        jlCidade.setText("Cidade");
        jlContato1.setText("Contato");
        jlContato2.setText("Contato");
        jlEmail.setText("E-mail");
        jlLogin.setText("Login");
        jlSenha.setText("Senha");
        jlSenha1.setText("Senha");
        jlCEP.setText("CPF");
        jlTipoUser.setText("Tipo de Acesso");
    }

    private void carregaDados() {
        Endereco endereco = usuario.getEndereco();
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

        List<Contato> contatos = usuario.getContatos();
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
            //jlContato1.setText(fone);
            //jlContato2.setText(celular);
            jTelefone1.setText(fone);
            jTelefone2.setText(celular);
        }

        jNome.setText(usuario.getNome());
        jCPF.setText(Formatacao.formataCPF(usuario.getCpf()));
        jLogin.setText(usuario.getLogin());
        jSenha.setText(usuario.getSenha());
        jSenha1.setText(usuario.getSenha());

        //jTipoUser.setText("Administrador"); // PEGAR NO BANCO SETAR NO VER E NO EDITAR
        if (usuario.getIdTipoUsuario() == 1) {
            btnAdministrador.setSelected(true);
        } else if (usuario.getIdTipoUsuario() == 2) {
            btnOperador.setSelected(true);
        }
    }

    public void cancelar() {
        Utilidades.showCloseAlert(DfrVoluntario.this);
    }

    public void verVoluntario() {
        int i = 100; //Define o inicio dos campos
        Dimension f = PanelFundo.getSize();

        jNome.setEditable(false);
        jLogradouro.setEditable(false);
        jNumero.setEditable(false);
        jComplemento.setEditable(false);
        jBairro.setEditable(false);
        jCPF.setEditable(false);
        jLogin.setEditable(false);
        jEmail.setEditable(false);
        jLogin.setEditable(false);
        jTelefone1.setEditable(false);
        jTelefone2.setEditable(false);
        jSenha.setVisible(false);
        jSenha1.setVisible(false);
        jTipoUser.setVisible(true);
        btnTipoFixo1.setVisible(false);
        btnTipoFixo2.setVisible(false);
        btnTipoCelular1.setVisible(false);
        btnTipoCelular2.setVisible(false);
        jlSenha.setVisible(false);
        jlSenha1.setVisible(false);
        btn.botaoEditarDialog(btnSalvarEditar);
        btnCidade.setVisible(false);
        btnCEP.setVisible(false);

        btnSalvarEditar.addActionListener(e -> {
            DfrVoluntario verVoluntario = new DfrVoluntario(null, true, 3, usuario, tela);
            verVoluntario.setVisible(true);
            this.dispose();
        });

        PanelFundo.add(jTelefone1, BorderLayout.NORTH);
        jTelefone1.setLocation(20, i + 250);
        jTelefone1.setSize(250, 20);

        PanelFundo.add(jTelefone2, BorderLayout.NORTH);
        jTelefone2.setLocation(20, i + 300);
        jTelefone2.setSize(250, 20);

        PanelFundo.add(jlContato1, BorderLayout.NORTH);
        jlContato1.setLocation(20, i + 225);
        jlContato1.setSize(250, 20);

        PanelFundo.add(jlContato2, BorderLayout.NORTH);
        jlContato2.setLocation(20, i + 275);
        jlContato2.setSize(250, 20);

       if(btnAdministrador.isSelected()){
           btnOperador.setEnabled(false);
       }
       else{
           btnAdministrador.setEnabled(false);
       }
    }

    public void editar() {
        if (!validaCampos())
            return;

        if (this.usuario == null) {
            Utilidades.showErro(this, new Erro("DF.02", "Erro ao identificar voluntário que deve ser editado"));
            return;
        }


        Endereco endereco = usuario.getEndereco();
        endereco.setLogradouro(jLogradouro.getText());
        endereco.setNumero(jNumero.getText());
        endereco.setComplemento(jComplemento.getText());
        endereco.setBairro(jBairro.getText());
        endereco.setCep(Utilidades.somenteNumeros(jCEP.getText()));
        endereco.setCidade(this.cidade);

        Usuario usuario = this.usuario;
        usuario.setNome(jNome.getText());
        usuario.setCpf(jCPF.getText());
        usuario.setLogin(jLogin.getText());
        usuario.setSenha(jSenha1.getText());
        usuario.setEndereco(endereco);

        if (btnAdministrador.isSelected()) {
            usuario.setIdTipoPessoa(4);
            usuario.setIdTipoUsuario(1);
        } else if (btnOperador.isSelected()) {
            usuario.setIdTipoPessoa(1);
            usuario.setIdTipoUsuario(2);
        }

        usuario.setStatusUsuario(1);
        usuario.setLiberarAdocao(1);
        usuario.setStatus(1);

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
        usuario.setContatos(contatos);

        Gson gson = new Gson();

        String json = gson.toJson(usuario);

        ServicoCliente sv = new ServicoCliente();
        if (sv.enviaDados("usuario", ServicoCliente.PUT, json, ServicoCliente.EMPTY_PARAMS, Usuario.class)) {
            this.usuario = (Usuario) sv.getObject();

            Utilidades.showSucesso(this,"Voluntário atualizado com sucesso!");

            tela.resetaDados();
            this.dispose();
        } else {
            Utilidades.showErro(this, (Erro) sv.getObject());
        }
    }

    public void salvar() {
        if (!validaCampos())
            return;

        Endereco endereco = usuario.getEndereco();
        endereco.setLogradouro(jLogradouro.getText());
        endereco.setNumero(jNumero.getText());
        endereco.setComplemento(jComplemento.getText());
        endereco.setBairro(jBairro.getText());
        endereco.setCep(Utilidades.somenteNumeros(jCEP.getText()));
        endereco.setCidade(this.cidade);

        Usuario usuario = new Usuario();
        usuario.setNome(jNome.getText());
        usuario.setCpf(jCPF.getText());
        usuario.setLogin(jLogin.getText());
        usuario.setSenha(jSenha1.getText());
        usuario.setEndereco(endereco);

        if (btnAdministrador.isSelected()) {
            usuario.setIdTipoPessoa(4);
            usuario.setIdTipoUsuario(1);
        } else if (btnOperador.isSelected()) {
            usuario.setIdTipoPessoa(1);
            usuario.setIdTipoUsuario(2);
        }

        usuario.setStatusUsuario(1);
        usuario.setLiberarAdocao(1);
        usuario.setStatus(1);

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
        usuario.setContatos(contatos);

        Gson gson = new Gson();

        String json = gson.toJson(usuario);

        ServicoCliente sv = new ServicoCliente();
        if (sv.enviaDados("usuario", ServicoCliente.POST, json, ServicoCliente.EMPTY_PARAMS, Usuario.class)) {
            Usuario usuarioAtt = (Usuario) sv.getObject();
            this.usuario = usuarioAtt;
            System.out.println("Usuário cadastrado " + usuarioAtt.getId());

            Utilidades.showSucesso(this,"Voluntário registrado com sucesso!");

            tela.resetaDados();
            this.dispose();
        } else {
            Utilidades.showErro(this, (Erro) sv.getObject());
        }
    }

    private boolean validaCampos() {
        if (!Validacao.validarNome(jNome.getText())) {
            Utilidades.showAlerta(this, "Digite um nome válido. (Este campo é obrigatório)");
            return false;
        }
        if (!Validacao.validarEmail(jEmail.getText())) {
            Utilidades.showAlerta(this, "Digite um e-mail válido!");
            return false;
        }
        if (!jLogradouro.getText().isEmpty()) {
            if (jCidade.getText().isEmpty()) {
                Utilidades.showAlerta(this, "Digite o endereço correto");
                return false;
            }
        }

        if (!jSenha.getText().equals(jSenha1.getText())) {
            Utilidades.showErro(this, new Erro("DF.01", "As senhas estão diferentes!"));
            return false;
        }

        return true;
    }

    public void focus() {

        final Focus focus = new Focus();

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

        jCPF.addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent evt) {

                focus.focusCPFGained(jlCPF, jCPF);
            }

            @Override
            public void focusLost(FocusEvent evt) {
                if (Formatacao.removerFormatacao(jCPF.getText()).length() == 0) {

                    focus.focusCPFLost(jlCPF, jCPF);
                }
            }

        });

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
        jLogin.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                focus.focusLoginGained(jlLogin, jLogin);
            }

            public void focusLost(FocusEvent evt) {
                focus.focusLoginLost(jlLogin, jLogin);
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

        jSenha.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                focus.focusSenhaGained(jlSenha, jSenha);
            }

            public void focusLost(FocusEvent evt) {
                focus.focusSenhaLost(jlSenha, jSenha);
            }

        });
        jSenha1.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                focus.focusRepetirSenhaGained(jlSenha1, jSenha1);
            }

            public void focusLost(FocusEvent evt) {
                focus.focusRepetirSenhaLost(jlSenha1, jSenha1);
            }

        });

        jCEP.addActionListener(e -> consultaCep());
    }

    public void selecionaCidade() {
        DfrBusca buscaCidade = new DfrBusca(this);
        buscaCidade.setVisible(true);
    }

    public void consultaCep() {
        String cep = jCEP.getText();
        if (cep.length() > 8) {

            Endereco endereco = Utilidades.getEnderecoByViaCep(cep);
            if (endereco != null) {

                Cidade cidade = endereco.getCidade();
                if (cidade != null) {
                    jCidade.setText(cidade.getDescricao() + " - " + cidade.getUf());
                    this.cidade = cidade;
                }
                if (endereco.getLogradouro() != null) jLogradouro.setText(endereco.getLogradouro());
                if (endereco.getBairro() != null) jBairro.setText(endereco.getBairro());

                if (!jLogradouro.getText().isEmpty()) jlLogradouro.setText("Logradouro");
                else jLogradouro.setText("Logradouro");
                if (!jBairro.getText().isEmpty()) jlBairro.setText("Bairro");
                else jBairro.setText("Bairro");
                jlCidade.setText("Cidade");
            }

        } else {
            Utilidades.showAlerta(this, "Por favor, digite um CEP válido!");
        }
    }

    public void setCidade(Cidade cidade) {
        usuario.getEndereco().setCidade(cidade);
        jCidade.setText(cidade.getDescricao() + " - " + cidade.getUf());
        this.cidade = cidade;
    }
}
