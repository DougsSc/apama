package operacionais;

import componentes.ComponentButton;
import componentes.ComponentLabel;
import componentes.ComponentText;
import utilidades.FrConfig;
import utilidades.Utilidades;

import javax.swing.*;
import java.awt.*;

public final class DfrJustificativa extends javax.swing.JDialog {

    private JPanel PanelFundo = new JPanel(new BorderLayout());
    private JPanel PanelCabecalho = new JPanel(new BorderLayout());

    private JTextArea jJustificativa = new JTextArea();

    private JLabel jCabecalho = new JLabel();
    private JLabel jLogoCabecalho = new JLabel();
    private JLabel jlJustificativa = new JLabel();
    private JLabel jlCabecalho = new JLabel();

    private FrConfig config = new FrConfig();

    private JButton btnSalvar = new JButton();
    private JButton btnCancelar = new JButton();

    private ComponentButton btn = new ComponentButton();
    private ComponentText txt = new ComponentText();
    private ComponentLabel label = new ComponentLabel();
    ImageIcon img = new ImageIcon();
    int tamCabecalho;


    public DfrJustificativa(IfrAdocao parent, int tipo) {
        ajusteTela();
        ajusteComponents();
        this.setLocationRelativeTo(null);
        jlCabecalho.setText("Justificativa");
        if (tipo == 1){
            btnSalvar.addActionListener(e -> salvar(1));
        }
        if (tipo == 2){
            btnSalvar.addActionListener(e -> salvar(2));
        }

    }

    public void ajusteTela() {

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(788, 700));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                    Utilidades.showAlerta(DfrJustificativa.this, "Digite uma Justificativa. (Este campo é obrigatório)");
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

        config. ajusteDialogJustificativa(this);

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

        /*Adiciona Arquivo Cabecalho
        JLabel ImgLogo = new JLabel();
        label.labelAdocao(ImgLogo);
        PanelCabecalho.add(ImgLogo, BorderLayout.CENTER);
         */

    }
    public void ajusteComponents() {
        int i = tamCabecalho+100;

        Dimension f = PanelFundo.getSize();
        PanelFundo.add(jlJustificativa, BorderLayout.NORTH);
        jlJustificativa.setLocation(20, i + 25);
        jlJustificativa.setSize(240, 20);
        jlJustificativa.setText("Justificativa");

        JScrollPane spDescricao = new JScrollPane(jJustificativa); //Adiciona Scroll a TextArea
        PanelFundo.add(spDescricao);
        spDescricao.setLocation(20, i + 50);
        spDescricao.setSize((f.width)-40, f.width-400);

        PanelFundo.add(btnCancelar, BorderLayout.NORTH);
        btnCancelar.setLocation(600, f.height - 80);

        PanelFundo.add(btnSalvar, BorderLayout.NORTH);
        btnSalvar.setLocation(500, f.height - 80);

        ComponentText txt = new ComponentText();
        // Formata os campos
        txt.formatarObservacao(jJustificativa);
        txt.formatarTitulo(jlJustificativa);
        jlJustificativa.setText("Justificativa");

        //Formata os botões
        btn.botaoSalvar(btnSalvar);
        btn.botaoCancelar(btnCancelar);

        btnCancelar.addActionListener(e -> dispose());

    }
    public void salvar(int tipo){

    }

}
