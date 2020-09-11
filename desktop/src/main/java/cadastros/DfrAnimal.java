package cadastros;

import com.google.gson.Gson;
import componentes.ComponentButton;
import componentes.ComponentLabel;
import componentes.ComponentText;
import componentes.Focus;
import entidades.*;
import principais.DfrBusca;
import utilidades.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;

/**
 *
 * @author cami0
 */
public final class DfrAnimal extends javax.swing.JDialog {

    private Animal animal;
    private Cidade cidade;
    private ArrayList<Arquivo> arquivos;

    private JFormattedTextField jDataResgate = new JFormattedTextField();

    private JTextField jNome = new JTextField();
    private JTextField jLogradouro = new JTextField();
    private JTextField jNumero = new JTextField();
    private JTextField jComplemento = new JTextField();
    private JTextField jBairro = new JTextField();
    private JTextField jCidade = new JTextField();

    private JTextArea jObservacao = new JTextArea();
    private JFormattedTextField jCEP = new JFormattedTextField();

    private JLabel jlCabecalho = new JLabel();
    private JLabel jlDataResgate = new JLabel();
    private JLabel jlDadosdoResgate = new JLabel();
    private JLabel jlNome = new JLabel();
    private JLabel jlPorte = new JLabel();
    private JLabel jlLogradouro = new JLabel();
    private JLabel jlNumero = new JLabel();
    private JLabel jlComplemento = new JLabel();
    private JLabel jlBairro = new JLabel();
    private JLabel jlCidade = new JLabel();
    private JLabel jlStatus = new JLabel();
    private JLabel jlCEP = new JLabel();
    private JLabel jlObservacao = new JLabel();

    private JButton btnCancelar = new JButton();
    private JButton btnSalvarEditar = new JButton();
    private JButton btnTratamento = new JButton();
    private JButton btnAdocao = new JButton();
    private JButton btnCEP = new JButton();
    private JButton btnCidade = new JButton();
    private JButton btnImagem = new JButton();

    private JPanel PanelFundo = new JPanel(new BorderLayout());
    private JPanel PanelCabecalho = new JPanel(new BorderLayout());

    private JRadioButton btnPequeno = new JRadioButton();
    private JRadioButton btnMedio = new JRadioButton();
    private JRadioButton btnGrande = new JRadioButton();

    private JRadioButton btnDisponivel = new JRadioButton();
    private JRadioButton btnEmTratamento = new JRadioButton();
    private JRadioButton btnAdotado = new JRadioButton();
    private JRadioButton btnObito = new JRadioButton();

    private ButtonGroup tipoPorte = new ButtonGroup();
    private ButtonGroup tipoStatus = new ButtonGroup();

    private FrConfig config = new FrConfig();
    private ComponentLabel lbl = new ComponentLabel();
    private ComponentButton btn = new ComponentButton();
    final Focus focus = new Focus();

    private IfrAnimal tela;

    int tipo;

    int tamCabecalho;

    private DfrAnimal(JFrame jFrame, boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    DfrAnimal(int tipo, Animal animal, IfrAnimal tela) {
//        super(parent, modal);
        this.setLocationRelativeTo(null);
        ajusteTela();
        ajusteComponents();

        this.tela = tela;
        this.tipo = tipo;

        if (animal == null) this.animal = new Animal();
        else this.animal = animal;

        tipoPorte.add(btnPequeno);
        tipoPorte.add(btnMedio);
        tipoPorte.add(btnGrande);

        tipoStatus.add(btnDisponivel);
        tipoStatus.add(btnEmTratamento);
        tipoStatus.add(btnAdotado);
        tipoStatus.add(btnObito);

        if (tipo == 1) {// Construtor Tela Novo
            jlCabecalho.setText("Novo Animal");
           focus();
            btnSalvarEditar.addActionListener(e -> salvar());
            btnCancelar.addActionListener(e -> cancelar());
            jDataResgate.setText(Utilidades.getDataAtual());
        }
        else if (tipo == 2) {//Construtor Tela Ver
            jlCabecalho.setText("Editar Animal");
            carregaDados();
            this.setLabels();
            verAnimal();
            btnCancelar.addActionListener(e -> dispose());
        } else if (tipo == 3) {// Construtor Tela Editar
            focus();
            jlCabecalho.setText("Ver Animal");
            btnSalvarEditar.addActionListener(e -> editar());
            this.setLabels();
            carregaDados();
            btnCancelar.addActionListener(e -> cancelar());
        }
    }



    public void ajusteTela() {
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(788, 700));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                if (tipo == 2) {
                    DfrAnimal.this.dispose();
                }
                else{
                    Utilidades.showCloseAlert(DfrAnimal.this);
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
        PanelCabecalho.setSize(f.width*2, f.height/7);
        jlCabecalho.setSize(f.width * 2, f.height / 7);
        config.ajusteCabecalho(jlCabecalho);

        //Adiciona Arquivo Cabecalho
        JLabel ImgLogo = new JLabel();
        lbl.labelAnimal(ImgLogo);
        PanelCabecalho.add(ImgLogo, BorderLayout.CENTER);

    }

    /*Ajusta os componentes para ficarem sem borda*/
    public void ajusteComponents() {

        int i = tamCabecalho+100;

        //Ajusta o local dos campos
        Dimension f = PanelFundo.getSize();

        PanelFundo.add(jNome, BorderLayout.NORTH);
        jNome.setLocation(20, i + 50);
        jNome.setSize((f.width / 2) + 100, 20);

        PanelFundo.add(jlNome, BorderLayout.NORTH);
        jlNome.setLocation(20, i + 25);
        jlNome.setSize(250, 20);

        PanelFundo.add(jlPorte, BorderLayout.NORTH);
        jlPorte.setLocation(540, i + 25);
        jlPorte.setSize((f.width / 2) - 10, 20);
        jlPorte.setText("Porte");

        PanelFundo.add(btnPequeno, BorderLayout.NORTH);
        btnPequeno.setLocation(540, i + 50);
        btnPequeno.setSize(100, 20);

        PanelFundo.add(btnMedio, BorderLayout.NORTH);
        btnMedio.setLocation(540, i + 75);
        btnMedio.setSize(100, 20);

        PanelFundo.add(btnGrande, BorderLayout.NORTH);
        btnGrande.setLocation(540, i + 100);
        btnGrande.setSize(100, 20);

        PanelFundo.add(jlDadosdoResgate, BorderLayout.NORTH);
        jlDadosdoResgate.setLocation(20, i + 100);
        jlDadosdoResgate.setSize(250, 20);
        jlDadosdoResgate.setText("Dados do Resgate");

        PanelFundo.add(jDataResgate, BorderLayout.NORTH);
        jDataResgate.setLocation(20, i + 150);
        jDataResgate.setSize(180, 20);

        PanelFundo.add(jlDataResgate, BorderLayout.NORTH);
        jlDataResgate.setLocation(20, i + 125);
        jlDataResgate.setSize(180, 20);

        PanelFundo.add(btnImagem, BorderLayout.NORTH);
        btnImagem.setLocation(240, i + 150);
        btnImagem.setSize(100, 20);

        PanelFundo.add(jCEP, BorderLayout.NORTH);
        jCEP.setLocation(20, i + 200);
        jCEP.setSize((f.width / 2) - 200, 20);

        PanelFundo.add(jlCEP, BorderLayout.NORTH);
        jlCEP.setLocation(20, i + 175);
        jlCEP.setSize((f.width / 2) - 200, 20);

        PanelFundo.add(btnCEP, BorderLayout.NORTH);
        btnCEP.setLocation((f.width / 2) - 180, i + 200);
        btnCEP.setSize(10, 20);

        PanelFundo.add(jCidade, BorderLayout.NORTH);
        jCidade.setLocation((f.width / 2) - 150, i + 200);
        jCidade.setSize((f.width / 2) + 80, 20);
        jCidade.setEditable(false);

        PanelFundo.add(jlCidade, BorderLayout.NORTH);
        jlCidade.setLocation((f.width / 2) - 150, i + 175);
        jlCidade.setSize(250, 20);

        PanelFundo.add(btnCidade, BorderLayout.NORTH);
        btnCidade.setLocation((f.width) - 70, i + 200);
        btnCidade.setSize(20, 20);

        PanelFundo.add(jLogradouro, BorderLayout.NORTH);
        jLogradouro.setLocation(20, i + 250);
        jLogradouro.setSize((f.width / 2) + 200, 20);

        PanelFundo.add(jlLogradouro, BorderLayout.NORTH);
        jlLogradouro.setLocation(20, i + 225);
        jlLogradouro.setSize(250, 20);

        PanelFundo.add(jNumero, BorderLayout.NORTH);
        jNumero.setLocation((f.width / 2) + 240, i + 250);
        jNumero.setSize((f.width / 2) - 280, 20);

        PanelFundo.add(jlNumero, BorderLayout.NORTH);
        jlNumero.setLocation((f.width / 2) + 240, i + 225);
        jlNumero.setSize(100, 20);

        PanelFundo.add(jComplemento, BorderLayout.NORTH);
        jComplemento.setLocation(20, i + 300);
        jComplemento.setSize((f.width / 2) - 40, 20);

        PanelFundo.add(jlComplemento, BorderLayout.NORTH);
        jlComplemento.setLocation(20, i + 275);
        jlComplemento.setSize(320, 20);

        PanelFundo.add(jBairro, BorderLayout.NORTH);
        jBairro.setLocation((f.width / 2), i + 300);
        jBairro.setSize((f.width / 2) - 40, 20);

        PanelFundo.add(jlBairro, BorderLayout.NORTH);
        jlBairro.setLocation((f.width / 2), i + 275);
        jlBairro.setSize((f.width / 2) - 40, 20);

        PanelFundo.add(jlStatus, BorderLayout.NORTH);
        jlStatus.setLocation(20, i + 350);
        jlStatus.setSize((f.width / 2) - 10, 20);
        jlStatus.setText("Status");

        PanelFundo.add(btnDisponivel, BorderLayout.NORTH);
        btnDisponivel.setLocation(20, i + 375);
        btnDisponivel.setSize(100, 20);

        PanelFundo.add(btnEmTratamento, BorderLayout.NORTH);
        btnEmTratamento.setLocation(140, i + 375);
        btnEmTratamento.setSize(135, 20);

        PanelFundo.add(btnAdotado, BorderLayout.NORTH);
        btnAdotado.setLocation(300, i + 375);
        btnAdotado.setSize(85, 20);

        PanelFundo.add(btnObito, BorderLayout.NORTH);
        btnObito.setLocation(410, i + 375);
        btnObito.setSize(60, 20);

        PanelFundo.add(btnAdocao, BorderLayout.NORTH);
        btnAdocao.setLocation(20, i + 400);
        btnAdocao.setSize(90, 20);

        PanelFundo.add(btnTratamento, BorderLayout.NORTH);
        btnTratamento.setLocation(155, i + 400);
        btnTratamento.setSize(120, 20);

        JScrollPane spDescricao = new JScrollPane(jObservacao); //Adiciona Scroll a TextArea
        PanelFundo.add(spDescricao);
        spDescricao.setLocation(20, i+450);
        spDescricao.setSize((f.width)-40, 70);

        PanelFundo.add(jlObservacao, BorderLayout.NORTH);
        jlObservacao.setLocation(20, i+425);
        jlObservacao.setSize(100, 20);
        jlObservacao.setText("Descrição");

        PanelFundo.add(btnCancelar, BorderLayout.NORTH);
        btnCancelar.setLocation(600, f.height - 70);

        PanelFundo.add(btnSalvarEditar, BorderLayout.NORTH);
        btnSalvarEditar.setLocation(500, f.height - 70);

        ComponentText txt = new ComponentText();
        // Formata os campos
        txt.formatarNome(jlNome, jNome);
        txt.formatarLogradouro(jlLogradouro, jLogradouro);
        txt.formatarNumero(jlNumero, jNumero);
        txt.formatarComplemento(jlComplemento, jComplemento);
        txt.formatarBairro(jlBairro, jBairro);
        txt.formatarCidade(jlCidade, jCidade);
        txt.formatarTitulo(jlDadosdoResgate);
        txt.formatarDataResgate(jlDataResgate, jDataResgate);
        txt.formatarCEP(jlCEP, jCEP);
        txt.formatarTitulo(jlStatus);

        //Formata os botões
        btn.botaoSalvar(btnSalvarEditar);
        btn.botaoCancelar(btnCancelar);
        btn.botaoDadosTratamento(btnTratamento);
        btn.botaoDadosAdocao(btnAdocao);
        btn.botaoPortePequeno(btnPequeno);
        btn.botaoPorteMedio(btnMedio);
        btn.botaoPorteGrande(btnGrande);
        btn.botaoBusca(btnCEP);
        btn.botaoBusca(btnCidade);
        btn.botaoDisponivel(btnDisponivel);
        btn.botaoTratamento(btnEmTratamento);
        btn.botaoAdotado(btnAdotado);
        btn.botaoObito(btnObito);
        btn.botaoVerImagem(btnImagem);

        btnCancelar.addActionListener(e -> dispose());
        btnCEP.addActionListener(e -> consultaCep());
        btnCidade.addActionListener(e -> selecionaCidade());
        btnImagem.addActionListener(e -> visualizaImagem());
        btnTratamento.addActionListener(e -> selecionaTratamento());
    }



    public void setLabels(){
        jlNome.setText("Nome");
        jlDataResgate.setText("Data do Resgate");
        jlLogradouro.setText("Logradouro");
        jlNumero.setText("Número");
        jlComplemento.setText("Complemento");
        jlBairro.setText("Bairro");
        jlCidade.setText("Cidade");
    }

    private void carregaDados() {

        Endereco endereco = animal.getEndereco();
        if (endereco != null && endereco.getLogradouro() != null) {
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

        jNome.setText(animal.getNome());
        jDataResgate.setText(Formatacao.ajustaDataDMA(animal.getDataResgate()));

        if(animal.getAnimalPorte().getId() == 1){
            btnPequeno.setSelected(true);
        }else if(animal.getAnimalPorte().getId() == 2){
            btnMedio.setSelected(true);
        }else{
            btnGrande.setSelected(true);
        }

        if(animal.getAnimalStatus().getId() == 1){
            btnDisponivel.setSelected(true);
        }else if(animal.getAnimalStatus().getId() == 2){
            btnEmTratamento.setSelected(true);
        }else if(animal.getAnimalStatus().getId() == 3){
            btnAdocao.setSelected(true);
        }else if(animal.getAnimalStatus().getId() == 4){
            btnObito.setSelected(true);
        }
    }

    public void cancelar(){
            Utilidades.showCloseAlert(DfrAnimal.this);
        }

    public void verAnimal() {
        jNome.setEditable(false);
        jLogradouro.setEditable(false);
        jNumero.setEditable(false);
        jComplemento.setEditable(false);
        jBairro.setEditable(false);
        jCidade.setVisible(false);

        this.setLabels();
    }

    public void editar() {

        if (!validaCampos())
            return;

        if (this.animal == null) {
            Utilidades.showErro(this, new Erro("DF.02", "Erro ao identificar animal que deve ser editado"));
            return;
        }

        Endereco endereco = animal.getEndereco();
        endereco.setLogradouro(jLogradouro.getText());
        endereco.setNumero(jNumero.getText());
        endereco.setComplemento(jComplemento.getText());
        endereco.setBairro(jBairro.getText());
        endereco.setCep(Utilidades.somenteNumeros(jCEP.getText()));
        endereco.setCidade(this.cidade);

        Animal animal = this.animal;
        animal.setNome(jNome.getText());
        animal.setEndereco(endereco);

        animal.setArquivos(arquivos);

        Gson gson = new Gson();

        String json = gson.toJson(animal);

        ServicoCliente sv = new ServicoCliente();
        if (sv.enviaDados("animal", ServicoCliente.PUT, json, ServicoCliente.EMPTY_PARAMS, Animal.class)) {
            this.animal = (Animal) sv.getObject();

            Utilidades.showSucesso(this,"Animal atualizado com sucesso!");

            tela.resetaDados();
            this.dispose();
        } else {
            Utilidades.showErro(this, (Erro) sv.getObject());
        }
    }

    public void salvar() {
        if (!validaCampos())
            return;

        Endereco endereco = animal.getEndereco();
        endereco.setCep(Utilidades.somenteNumeros(jCEP.getText()));
        endereco.setLogradouro(jLogradouro.getText());
        endereco.setNumero(jNumero.getText());
        endereco.setComplemento(jComplemento.getText());
        endereco.setBairro(jBairro.getText());
        endereco.setCidade(this.cidade);

        AnimalPorte animalPorte = new AnimalPorte();
        if (btnPequeno.isSelected()) {
            animalPorte.setId(1);
        } else if (btnMedio.isSelected()) {
            animalPorte.setId(2);
        } else if (btnGrande.isSelected()) {
            animalPorte.setId(3);
        }

        AnimalStatus animalStatus = new AnimalStatus();
        if (btnDisponivel.isSelected()) {
            animalStatus.setId(1);
        } else if (btnEmTratamento.isSelected()) {
            animalStatus.setId(2);
        } else if (btnAdotado.isSelected()) {
            animalStatus.setId(3);
        } else if (btnObito.isSelected()) {
            animalStatus.setId(4);
        }

        Animal animal = new Animal();
        animal.setNome(jNome.getText());
        animal.setDataResgate(Formatacao.ajustaDataAMD(jDataResgate.getText()));
        animal.setObservacao(jObservacao.getText());

        animal.setAnimalStatus(animalStatus);
        animal.setAnimalPorte(animalPorte);
        animal.setEndereco(endereco);

        animal.setArquivos(arquivos);

        Gson gson = new Gson();
        String json = gson.toJson(animal);

        ServicoCliente sv = new ServicoCliente();
        if (sv.enviaDados("animal", ServicoCliente.POST, json, ServicoCliente.EMPTY_PARAMS, Animal.class)) {
            Animal animalAtt = (Animal) sv.getObject();
            this.animal = animalAtt;
            System.out.println("Animal cadastrado " + animalAtt.getId());

            Utilidades.showSucesso(this,"Animal registrado com sucesso!");

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
        if (!jLogradouro.getText().isEmpty()) {
            if (jCidade.getText().isEmpty()) {
                Utilidades.showAlerta(this, "Digite o endereço correto");
                return false;
            }
        }
        return true;
    }

    public void focus() {

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
        jDataResgate.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                focus.focusDataGained(jlDataResgate, jDataResgate);
            }
            public void focusLost(FocusEvent evt) {
                if (Formatacao.removerFormatacao(jDataResgate.getText()).length() == 0) {
                  focus.focusDataLost(jlDataResgate, jDataResgate);
                }
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

        jCEP.addActionListener(e -> consultaCep());
    }

    public void selecionaCidade() {
        DfrBusca buscaCidade = new DfrBusca(this, 1);
        buscaCidade.setVisible(true);
    }
    public void visualizaImagem() {
        DfrBusca verImagem = new DfrBusca(this, animal.getArquivos());
        verImagem.setVisible(true);
    }
    public void selecionaTratamento() {
        DfrBusca verTratamento = new DfrBusca(this, 2);
        verTratamento.setVisible(true);
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
        animal.getEndereco().setCidade(cidade);
        jCidade.setText(cidade.getDescricao() + " - " + cidade.getUf());
        this.cidade = cidade;
        jlCidade.setText("Cidade");
    }

    public void setImagens(ArrayList<Arquivo> arquivos) {
        this.arquivos = arquivos;
        System.out.println("Imagens: " + arquivos.size());
    }
}