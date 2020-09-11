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
public final class DfrAnimalRelatorio extends JDialog {

    private JFormattedTextField jDataInicio = new JFormattedTextField();
    private JFormattedTextField jDataFim = new JFormattedTextField();

    private JLabel jlCabecalho = new JLabel();
    private JLabel jlDataInicio = new JLabel();
    private JLabel jlData = new JLabel();
    private JLabel jlDataFim = new JLabel();
    private JLabel jlPorte = new JLabel();
    private JLabel jlStatus = new JLabel();

    private JButton btnCancelar = new JButton();
    private JButton btnGerarRelatorio = new JButton();

    private JPanel PanelFundo = new JPanel(new BorderLayout());
    private JPanel PanelCabecalho = new JPanel(new BorderLayout());

    private JRadioButton btnPequeno = new JRadioButton();
    private JRadioButton btnMedio = new JRadioButton();
    private JRadioButton btnGrande = new JRadioButton();
    private JRadioButton btnDisponivel = new JRadioButton();
    private JRadioButton btnEmTratamento = new JRadioButton();
    private JRadioButton btnAdotado = new JRadioButton();
    private JRadioButton btnObito = new JRadioButton();
    private JRadioButton btnDataResgate = new JRadioButton();
    private JRadioButton btnDataAdocao = new JRadioButton();

    private ButtonGroup tipoPorte = new ButtonGroup();
    private ButtonGroup tipoStatus = new ButtonGroup();
    private ButtonGroup tipoData = new ButtonGroup();

    private FrConfig config = new FrConfig();
    private ComponentLabel lbl = new ComponentLabel();
    private ComponentButton btn = new ComponentButton();
    final Focus focus = new Focus();

    private IfrAnimal tela;

    int tamCabecalho;

    private DfrAnimalRelatorio(JFrame jFrame, boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    DfrAnimalRelatorio(Frame parent, boolean modal, Animal animal, IfrAnimal tela) {
        super(parent, modal);
        this.setLocationRelativeTo(null);
        ajusteTela();
        ajusteComponents();
        setLabels();
        focus();
        jlCabecalho.setText("Relatório Animais");

        this.tela = tela;
        tipoPorte.add(btnPequeno);
        tipoPorte.add(btnMedio);
        tipoPorte.add(btnGrande);

        tipoStatus.add(btnDisponivel);
        tipoStatus.add(btnEmTratamento);
        tipoStatus.add(btnAdotado);
        tipoStatus.add(btnObito);

        tipoData.add(btnDataAdocao);
        tipoData.add(btnDataResgate);
        btnDataResgate.setSelected(true);
    }



    public void ajusteTela() {
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(788, 700));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                DfrAnimalRelatorio.this.dispose();

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

        PanelFundo.add(jlData, BorderLayout.NORTH);
        jlData.setLocation(20, i + 50);
        jlData.setSize(200, 20);

        PanelFundo.add(btnDataResgate, BorderLayout.NORTH);
        btnDataResgate.setLocation(20, i + 75);
        btnDataResgate.setSize(150, 20);

        PanelFundo.add(btnDataAdocao, BorderLayout.NORTH);
        btnDataAdocao.setLocation(170, i + 75);
        btnDataAdocao.setSize(150, 20);

        PanelFundo.add(jDataInicio, BorderLayout.NORTH);
        jDataInicio.setLocation(20, i + 125);
        jDataInicio.setSize(100, 20);

        PanelFundo.add(jlDataInicio, BorderLayout.NORTH);
        jlDataInicio.setLocation(20, i + 100);
        jlDataInicio.setSize(100, 20);

        PanelFundo.add(jDataFim, BorderLayout.NORTH);
        jDataFim.setLocation(180, i + 125);
        jDataFim.setSize(100, 20);

        PanelFundo.add(jlDataFim, BorderLayout.NORTH);
        jlDataFim.setLocation(180, i + 100);
        jlDataFim.setSize(80, 20);

        PanelFundo.add(jlPorte, BorderLayout.NORTH);
        jlPorte.setLocation(20, i + 175);
        jlPorte.setSize(100, 20);

        PanelFundo.add(btnPequeno, BorderLayout.NORTH);
        btnPequeno.setLocation(20, i + 200);
        btnPequeno.setSize(100, 20);

        PanelFundo.add(btnMedio, BorderLayout.NORTH);
        btnMedio.setLocation(120, i + 200);
        btnMedio.setSize(100, 20);

        PanelFundo.add(btnGrande, BorderLayout.NORTH);
        btnGrande.setLocation(200, i + 200);
        btnGrande.setSize(100, 20);

        PanelFundo.add(jlStatus, BorderLayout.NORTH);
        jlStatus.setLocation(20, i + 250);
        jlStatus.setSize(100, 20);

        PanelFundo.add(btnDisponivel, BorderLayout.NORTH);
        btnDisponivel.setLocation(20, i + 275);
        btnDisponivel.setSize(100, 20);

        PanelFundo.add(btnEmTratamento, BorderLayout.NORTH);
        btnEmTratamento.setLocation(140, i + 275);
        btnEmTratamento.setSize(135, 20);

        PanelFundo.add(btnAdotado, BorderLayout.NORTH);
        btnAdotado.setLocation(300, i + 275);
        btnAdotado.setSize(85, 20);

        PanelFundo.add(btnObito, BorderLayout.NORTH);
        btnObito.setLocation(410, i + 275);
        btnObito.setSize(60, 20);

        PanelFundo.add(btnGerarRelatorio, BorderLayout.NORTH);
        btnGerarRelatorio.setLocation(400, f.height - 80);
        btnGerarRelatorio.setSize(150, 20);

        PanelFundo.add(btnCancelar, BorderLayout.NORTH);
        btnCancelar.setLocation(600, f.height - 80);

        ComponentText txt = new ComponentText();

        // Formata os campos
        txt.formatarDataInicio(jlDataInicio, jDataInicio);
        txt.formatarDataFim(jlDataFim, jDataFim);
        txt.formatarTitulo(jlStatus);
        txt.formatarTitulo(jlData);
        txt.formatarTitulo(jlPorte);

        //Formata os botões
        btn.botaoCancelar(btnCancelar);
        btn.botaoPortePequeno(btnPequeno);
        btn.botaoPorteMedio(btnMedio);
        btn.botaoPorteGrande(btnGrande);
        btn.botaoDisponivel(btnDisponivel);
        btn.botaoTratamento(btnEmTratamento);
        btn.botaoAdotado(btnAdotado);
        btn.botaoObito(btnObito);
        btn.botaoGerarRelatorio(btnGerarRelatorio);
        btn.botaoDataAdocao(btnDataAdocao);
        btn.botaoDataResgate(btnDataResgate);

        btnCancelar.addActionListener(e -> dispose());
        btnGerarRelatorio.addActionListener(e -> GerarRelatorioTratamento());

    }

    public void setLabels(){
        jlData.setText("Data");
        jlPorte.setText("Porte");
        jlStatus.setText("Status");
    }

    public void focus() {

        jDataInicio.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent evt) {
                focus.focusDataInicioGained(jlDataInicio, jDataInicio);
            }

            @Override
            public void focusLost(FocusEvent evt) {
                if (Formatacao.removerFormatacao(jDataInicio.getText()).length() == 0) {
                    focus.focusDataInicioLost(jlDataInicio, jDataInicio);
                }
            }
        });

        jDataFim.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                focus.focusDataFimGained(jlDataFim, jDataFim);
            }
            public void focusLost(FocusEvent evt) {
                if (Formatacao.removerFormatacao(jDataFim.getText()).length() == 0) {
                    focus.focusDataFimLost(jlDataFim, jDataFim);
                }
            }

        });
    }


    public void selecionaCidade() {
       /* DfrBusca buscaCidade = new DfrBusca(this, 1);
        buscaCidade.setVisible(true);
        */
    }

    public void GerarRelatorioTratamento() {

    }
}