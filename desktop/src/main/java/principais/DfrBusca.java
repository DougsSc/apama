package principais;

import cadastros.DfrAnimal;
import cadastros.DfrAnimalRelatorio;
import cadastros.DfrTutorDoador;
import cadastros.DfrVoluntario;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import componentes.*;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import entidades.*;
import operacionais.DfrAdocao;
import operacionais.DfrDoacao;
import operacionais.DfrDoacaoRelatorio;
import operacionais.DfrTipoDoacao;
import utilidades.*;

public final class DfrBusca extends javax.swing.JDialog {

    // Variaveis de controle
    DfrVoluntario dfrVoluntario;
    ArrayList<Cidade> tempCidades;

    DfrDoacao dfrDoacao;
    ArrayList<TipoDoacao> tempTiposDoacao;
    ArrayList<Object> tempTutoresDoadores;

    ArrayList<Arquivo> arquivos;
    ArrayList<Animal> tempAnimais;

    DfrTutorDoador dfrTutorDoador;

    DfrAnimal dfrAnimal;

    DfrAdocao dfrAdocao;

    DfrDoacaoRelatorio dfrDoacaoRelatorio;

    DfrAnimalRelatorio dfrAnimalRelatorio;

    private JTable jTabelaBusca = new JTable();

    private JPanel PanelFundo = new JPanel(new BorderLayout());
    private JPanel PanelCabecalho = new JPanel(new BorderLayout());

    private JTextField jBusca = new JTextField();

    private JLabel jlBusca = new JLabel();
    private JLabel jCabecalho = new JLabel();
    private JLabel jLogoCabecalho = new JLabel();

    private FrConfig config = new FrConfig();

    private JButton btnOk = new JButton();
    private JButton btnCancelar = new JButton();
    private JButton btnBusca = new JButton();
    private JButton btnNova = new JButton();
    private JButton btnExcluir = new JButton();
    private JButton btnVisualizar = new JButton();


    private ComponentButton btn = new ComponentButton();
    private ComponentText txt = new ComponentText();
    private ComponentLabel label = new ComponentLabel();
    ImageIcon img = new ImageIcon();

    // Voluntário
    public DfrBusca(DfrVoluntario parent) {
        super(parent, true);
        this.dfrVoluntario = parent;
        iniciaTela();
        iniciaCidades();
    }

    // TutorDoador
    public DfrBusca(DfrTutorDoador parent) {
        super(parent, true);
        this.dfrTutorDoador = parent;
        iniciaTela();
        iniciaCidades();
    }

    // Doação
    public DfrBusca(DfrDoacao parent, int tipo) {
        super(parent, true);
        this.dfrDoacao = parent;
        iniciaTela();

        if (tipo == 1) {
            iniciaTutorDoador();
        } else if (tipo == 2) {
            iniciaTiposDoacao();
        }
    }
    // Doação
    public DfrBusca(DfrDoacaoRelatorio parent, int tipo) {
        super(parent, true);
        this.dfrDoacaoRelatorio = parent;
        iniciaTela();

        if (tipo == 1) {
            iniciaTutorDoador();
        } else if (tipo == 2) {
            iniciaTiposDoacao();
        }
    }

    // Animal
    public DfrBusca(DfrAnimal parent, List<Arquivo> arquivos) {
        super(parent, true);
        this.dfrAnimal = parent;
        iniciaTela();

        if (arquivos != null) {
            this.arquivos = new ArrayList<>();
            this.arquivos.addAll(arquivos);
        }
        iniciaImagem();
    }

    public DfrBusca(DfrAnimal parent, int tipo) {
        super(parent, true);
        this.dfrAnimal = parent;
        iniciaTela();
        if (tipo == 1) {
            iniciaCidades();
        } else if (tipo == 2) {
            iniciaTratamento();
        }
    }

    // Adoção
    public DfrBusca(DfrAdocao parent, List<Arquivo> arquivos) {
        super(parent, true);
        this.dfrAdocao = parent;
        iniciaTela();
        if (arquivos != null) {
            this.arquivos = new ArrayList<>(arquivos);
//            this.arquivos.addAll(arquivos);
        }
        iniciaImagem();
    }

    public DfrBusca(DfrAdocao parent, int tipo) {
        super(parent, true);
        this.dfrAdocao = parent;
        iniciaTela();
        if (tipo == 1) {
            iniciaTutorDoador();
        } else if (tipo == 2) {
            iniciaAnimal();
        }
    }


    private void iniciaTela() {
        ajusteTela();
        ajusteComponents();
        focus();

        this.setLocationRelativeTo(null);
    }

    private DfrBusca(JFrame jFrame, boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void ajusteTela() {
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
        jLogoCabecalho.setIcon(img);
        PanelCabecalho.add(jLogoCabecalho, BorderLayout.NORTH);
        jLogoCabecalho.setSize(200, f.height / 7);

    }

    public void ajusteComponents() {
        getRootPane().setDefaultButton(btnBusca);

        Dimension f = PanelFundo.getSize();
        PanelFundo.add(jBusca, BorderLayout.NORTH);
        jBusca.setLocation(20, 150);
        jBusca.setSize((f.width) - 60, 20);

        PanelFundo.add(jlBusca, BorderLayout.NORTH);
        jlBusca.setLocation(10, 125);
        jlBusca.setSize(100, 20);

        PanelFundo.add(btnCancelar, BorderLayout.NORTH);
        btnCancelar.setLocation(f.width - 150, 550);

        PanelFundo.add(btnOk, BorderLayout.NORTH);
        btnOk.setLocation(f.width - 250, 550);

        PanelFundo.add(btnVisualizar, BorderLayout.NORTH);
        btnVisualizar.setLocation(f.width - 250, 550);
        btnVisualizar.setVisible(false);

        PanelFundo.add(btnNova, BorderLayout.NORTH);
        btnNova.setLocation(f.width - 350, 550);
        btnNova.setVisible(false);

        PanelFundo.add(btnExcluir, BorderLayout.NORTH);
        btnExcluir.setLocation(f.width - 450, 550);
        btnExcluir.setVisible(false);

        btn.botaoOk(btnOk);
        btn.botaoCancelar(btnCancelar);
        btn.botaoBusca(btnBusca);
        btn.botaoNova(btnNova);
        btn.botaoExcluirDialog(btnExcluir);
        btn.botaoVerDialog(btnVisualizar);

        txt.formatarBusca(jlBusca, jBusca);

        PanelFundo.add(jTabelaBusca, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(jTabelaBusca);
        PanelFundo.add(scrollPane, BorderLayout.NORTH);
        jTabelaBusca.setSize((int) (f.width - 60), (f.height) / 2);
        jTabelaBusca.setLocation(20, (f.height / 4));
        jTabelaBusca.setBackground(new Color(136, 136, 136));
        jTabelaBusca.setForeground(Color.black);
        scrollPane.setSize((int) (f.width - 60), (f.height) / 2);
        scrollPane.setLocation(20, (f.height / 4));
        scrollPane.setBackground(new Color(136, 136, 136));
        scrollPane.setForeground(Color.black);

        btnCancelar.addActionListener(e -> dispose());
    }

    public void focus() {
        final Focus focus = new Focus();
        jBusca.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent evt) {
                focus.focusBuscaGained(jlBusca, jBusca);
            }

            @Override
            public void focusLost(FocusEvent evt) {
                focus.focusBuscaLost(jlBusca, jBusca);
            }
        });
    }

    private void iniciaCidades() {
        jCabecalho.setText("Busca Cidade");
        label.labelCidade(jLogoCabecalho);

        jBusca.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
            }

            public void removeUpdate(DocumentEvent e) {
                filter();
            }

            public void insertUpdate(DocumentEvent e) {
                filter();
            }

            public void filter() {
                String busca = jBusca.getText();
                if (!busca.equalsIgnoreCase("busca") && !busca.isEmpty()) {

                    tempCidades = new ArrayList<>();
                    for (Cidade cidade : Sessao.shared().getCidades()) {
                        if (cidade.getDescricao().toLowerCase().contains(busca.toLowerCase())) {
                            tempCidades.add(cidade);
                        }
                    }
                } else {
                    tempCidades = Sessao.shared().getCidades();
                }

                new PopularTabela().cidades(jTabelaBusca, tempCidades);
                jTabelaBusca.invalidate();
            }
        });

        jTabelaBusca.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2)
                    retornaCidade();
            }
        });

        btnOk.addActionListener(e -> retornaCidade());
    }

    private void retornaCidade() {
        if (jTabelaBusca.getSelectedRow() < 0) {
            Utilidades.showErro(this, new Erro("C.01", "Selecione a cidade!"));
        } else {
            //parent.setCidade(cidade);
            if (dfrVoluntario != null) {
                dfrVoluntario.setCidade(tempCidades.get(jTabelaBusca.getSelectedRow()));
            } else if (dfrTutorDoador != null) {
                dfrTutorDoador.setCidade(tempCidades.get(jTabelaBusca.getSelectedRow()));
            } else if (dfrAnimal != null) {
                dfrAnimal.setCidade(tempCidades.get(jTabelaBusca.getSelectedRow()));
            }

            this.dispose();
        }
    }

    public void iniciaTiposDoacao() {
        jCabecalho.setText("Tipo Doação");
        label.labelDoacao(jLogoCabecalho);
        btnNova.setVisible(true);
        btnExcluir.setVisible(true);

        jBusca.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
            }

            public void removeUpdate(DocumentEvent e) {
                filter();
            }

            public void insertUpdate(DocumentEvent e) {
                filter();
            }

            public void filter() {
                String busca = jBusca.getText();
                if (!busca.equalsIgnoreCase("busca") && !busca.isEmpty()) {

                    tempTiposDoacao = new ArrayList<>();
                    for (TipoDoacao tipoDoacao : Sessao.shared().getTiposDoacao(false)) {
                        if (Utilidades.semAcentos(tipoDoacao.getDescricao()).toLowerCase().contains(Utilidades.semAcentos(busca).toLowerCase())) {
                            tempTiposDoacao.add(tipoDoacao);
                        }
                    }
                } else {
                    tempTiposDoacao = Sessao.shared().getTiposDoacao(false);
                }

                new PopularTabela().tiposDoacao(jTabelaBusca, tempTiposDoacao);
                jTabelaBusca.invalidate();
            }
        });

        jTabelaBusca.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2)
                    retornaTipoDoacao();
            }
        });

        btnOk.addActionListener(e -> retornaTipoDoacao());
        btnNova.addActionListener(e -> {
            DfrTipoDoacao dfr = new DfrTipoDoacao(DfrBusca.this);
            dfr.setVisible(true);
        });
        tempTiposDoacao = new ArrayList<>();
        tempTiposDoacao.addAll(Sessao.shared().getTiposDoacao(true));

        new PopularTabela().tiposDoacao(jTabelaBusca, tempTiposDoacao);
        jTabelaBusca.invalidate();
    }

    private void retornaTipoDoacao() {
        if (jTabelaBusca.getSelectedRow() < 0) {
            Utilidades.showErro(this, new Erro("TD.01", "Selecione o tipo de doação!"));
        } else {
            if (dfrDoacao != null) {
                dfrDoacao.setTipoDoacao(tempTiposDoacao.get(jTabelaBusca.getSelectedRow()));
            }
            else if (dfrDoacaoRelatorio != null){
                dfrDoacaoRelatorio.setTipoDoacao(tempTiposDoacao.get(jTabelaBusca.getSelectedRow()));
            }
            this.dispose();
        }
    }

    private void iniciaImagem() {
        jCabecalho.setText("Arquivos");
        if (arquivos == null)
            arquivos = new ArrayList<>();
        label.labelImagem(jLogoCabecalho);
        jBusca.setVisible(false);
        btnNova.setVisible(true);
        btnExcluir.setVisible(true);
        btnExcluir.setVisible(true);
        btnVisualizar.setVisible(false);
        btnOk.setVisible(true);

        btnOk.addActionListener(e -> retornaImagens());

        btnNova.addActionListener(e -> {
            try {
                FileDialog fd = new FileDialog(new JFrame());
                fd.setMultipleMode(true);
                fd.setVisible(true);

                File[] files = fd.getFiles();
                if (files.length > 0) {
                    for (File file : files) {
                        String encoded = Utilidades.encodeFileToBase64Binary(file);

                        Arquivo arquivo = new Arquivo(file.getName(), encoded);
                        arquivos.add(arquivo);

                        PopularTabela.arquivos(jTabelaBusca, arquivos);
                        jTabelaBusca.invalidate();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        btnExcluir.addActionListener(e -> {
            if (jTabelaBusca.getSelectedRowCount() == 0) {
                Utilidades.showAlerta(this, "Por favor, selecione uma Imagem!");
            } else {
                try {
//                    System.out.println("Imagem removida");
                    arquivos.remove(jTabelaBusca.getSelectedRow());

                    PopularTabela.arquivos(jTabelaBusca, arquivos);
                    jTabelaBusca.invalidate();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        });

        PopularTabela.arquivos(jTabelaBusca, arquivos);
        jTabelaBusca.invalidate();
    }

    private void retornaImagens() {

        if (dfrAnimal != null) {
            dfrAnimal.setImagens(arquivos);
        } else if (dfrAdocao != null) {
            dfrAdocao.setArquivos(arquivos);
        }

        this.dispose();
    }

    private void iniciaTutorDoador() {
        jCabecalho.setText("Doador");
        label.labelTutorDoador(jLogoCabecalho);

        jBusca.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {}

            public void removeUpdate(DocumentEvent e) {
                filter();
            }

            public void insertUpdate(DocumentEvent e) {
                filter();
            }

            public void filter() {
                String busca = jBusca.getText();
                if (!busca.equalsIgnoreCase("busca") && !busca.isEmpty()) {

                    tempTutoresDoadores = new ArrayList<>();
                    for (Object object : Sessao.shared().getTutorDoador(DfrBusca.this, false)) {
                        tempTutoresDoadores.add(object);
                        if (object instanceof PessoaFisica) {
                            if (((PessoaFisica) object).getNome().toLowerCase().contains(busca.toLowerCase())) {
                                tempTutoresDoadores.add(object);
                            }
                        } else if (object instanceof PessoaJuridica) {
                            if (((PessoaJuridica) object).getNomeFantasia().toLowerCase().contains(busca.toLowerCase())) {
                                tempTutoresDoadores.add(object);
                            }
                        }
                    }
                } else {
                    tempTutoresDoadores = Sessao.shared().getTutorDoador(DfrBusca.this, false);
                }

                System.out.println(tempTutoresDoadores.size());

                PopularTabela.tutoresDoadores(jTabelaBusca, tempTutoresDoadores);
                jTabelaBusca.invalidate();
            }
        });

        jTabelaBusca.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2)
                    retornaTutorDoador();
            }
        });

        btnOk.addActionListener(e -> retornaTutorDoador());


        PopularTabela.tutoresDoadores(jTabelaBusca, Sessao.shared().getTutorDoador(DfrBusca.this, false));
        jTabelaBusca.invalidate();
    }

    private void retornaTutorDoador() {
        if (jTabelaBusca.getSelectedRow() < 0) {
            Utilidades.showErro(this, new Erro("DO.01", "Selecione o doador!"));
        } else {
            if (dfrDoacao != null)
                dfrDoacao.setDoador(tempTutoresDoadores.get(jTabelaBusca.getSelectedRow()));
            else if (dfrAdocao != null) {
                PessoaFisica pf = (PessoaFisica) tempTutoresDoadores.get(jTabelaBusca.getSelectedRow());
                dfrAdocao.setTutor(pf);
            }
            else if (dfrDoacaoRelatorio != null){
                dfrDoacaoRelatorio.setDoador(tempTutoresDoadores.get(jTabelaBusca.getSelectedRow()));
            }
            this.dispose();
        }
    }


    private void iniciaAnimal() {
        jCabecalho.setText("Animais");
        label.labelAnimal(jLogoCabecalho);

        jBusca.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {}

            public void removeUpdate(DocumentEvent e) {
                filter();
            }

            public void insertUpdate(DocumentEvent e) {
                filter();
            }

            public void filter() {
                String busca = jBusca.getText();
                if (!busca.equalsIgnoreCase("busca") && !busca.isEmpty()) {
                    ServicoCliente sv = new ServicoCliente();

                    HashMap<String, String> map = new HashMap<>();
                    map.put("nome", busca);
                    String json = sv.buscaDados("animal", ServicoCliente.GET, "", map);

                    try {
                        tempAnimais.addAll(new Gson().fromJson(json, new TypeToken<ArrayList<Animal>>() {
                        }.getType()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

//                System.out.println(tempAnimais.size());

                PopularTabela.animais(jTabelaBusca, tempAnimais);
                jTabelaBusca.invalidate();
            }
        });

        jTabelaBusca.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2)
                    retornaAnimal();
            }
        });

        btnOk.addActionListener(e -> retornaAnimal());

        ServicoCliente sv = new ServicoCliente();
        HashMap<String, String> map = new HashMap<>();
        map.put("adotaveis", "1");
        String json = sv.buscaDados("animal", ServicoCliente.GET, "", map);
        tempAnimais = new Gson().fromJson(json, new TypeToken<ArrayList<Animal>>() {}.getType());

        PopularTabela.animais(jTabelaBusca, tempAnimais);
        jTabelaBusca.invalidate();
    }

    private void retornaAnimal() {
        if (jTabelaBusca.getSelectedRow() < 0) {
            Utilidades.showErro(this, new Erro("AN.01", "Selecione o animal!"));
        } else {
            dfrAdocao.setAnimal(tempAnimais.get(jTabelaBusca.getSelectedRow()));
            this.dispose();
        }
    }

    private void iniciaTratamento() {
        jCabecalho.setText("Tratamento");
        label.labelTratamento(jLogoCabecalho);
        btnNova.setVisible(true);
        btnExcluir.setVisible(true);


      /*  jBusca.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {}

            public void removeUpdate(DocumentEvent e) {
                filter();
            }

            public void insertUpdate(DocumentEvent e) {
                filter();
            }

            public void filter() {
                String busca = jBusca.getText();
                if (!busca.equalsIgnoreCase("busca") && !busca.isEmpty()) {
                    ServicoCliente sv = new ServicoCliente();

                    HashMap<String, String> map = new HashMap<>();
                    map.put("nome", busca);
                    String json = sv.buscaDados("animais", ServicoCliente.GET, "", map);

                    try {
                        tempAnimais.addAll(new Gson().fromJson(json, new TypeToken<ArrayList<TipoDoacao>>() {}.getType()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                System.out.println(tempAnimais.size());

                PopularTabela.animais(jTabelaBusca, tempAnimais);
                jTabelaBusca.invalidate();
            }
        });

        jTabelaBusca.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2)
                    retornaAnimal();
            }
        });

          */

        btnOk.addActionListener(e -> retornaTratamento());
    }

    private void retornaTratamento() {
        if (jTabelaBusca.getSelectedRow() < 0) {
            Utilidades.showErro(this, new Erro("AN.01", "Selecione o Tratamento!"));
        } else {
           /* dfrAdocao.setDoador(tempAnimais.get(jTabelaBusca.getSelectedRow()));
            this.dispose();
            */
        }
    }

}
