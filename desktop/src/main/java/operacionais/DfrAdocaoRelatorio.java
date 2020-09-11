package operacionais;

import cadastros.IfrAnimal;
import componentes.ComponentButton;
import componentes.ComponentLabel;
import componentes.ComponentText;
import componentes.Focus;
import entidades.Adocao;
import entidades.Animal;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import utilidades.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author cami0
 */
public final class DfrAdocaoRelatorio extends JDialog {

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

    private JRadioButton btnAguardandoAprovacao = new JRadioButton();
    private JRadioButton btnAusenciaInformacoes = new JRadioButton();
    private JRadioButton btnAprovado = new JRadioButton();
    private JRadioButton btnRejeitado = new JRadioButton();
    //private JRadioButton btnPequeno = new JRadioButton();
    //private JRadioButton btnMedio = new JRadioButton();
    //private JRadioButton btnGrande = new JRadioButton();

    private ButtonGroup tipoStatus = new ButtonGroup();
    private ButtonGroup tipoPorte = new ButtonGroup();

    private FrConfig config = new FrConfig();
    private ComponentLabel lbl = new ComponentLabel();
    private ComponentButton btn = new ComponentButton();
    final Focus focus = new Focus();

    private IfrAdocao tela;

    int tamCabecalho;

    private DfrAdocaoRelatorio(JFrame jFrame, boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    DfrAdocaoRelatorio(Component parent, boolean modal, Adocao adocao, IfrAdocao tela) {
//        super(parent, modal);
        this.setLocationRelativeTo(null);
        ajusteTela();
        ajusteComponents();
        setLabels();
        focus();
        jlCabecalho.setText("Relatório Adoções");

        this.tela = tela;
        tipoStatus.add(btnAguardandoAprovacao);
        tipoStatus.add(btnAusenciaInformacoes);
        tipoStatus.add(btnAprovado);
        tipoStatus.add(btnRejeitado);

        //tipoPorte.add(btnPequeno);
        //tipoPorte.add(btnMedio);
        //tipoPorte.add(btnGrande);
    }

    public void ajusteTela() {
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(788, 700));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                DfrAdocaoRelatorio.this.dispose();
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
        lbl.labelAdocao(ImgLogo);
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
        jlData.setText("Data Adoção");

        PanelFundo.add(jDataInicio, BorderLayout.NORTH);
        jDataInicio.setLocation(20, i + 100);
        jDataInicio.setSize(100, 20);

        PanelFundo.add(jlDataInicio, BorderLayout.NORTH);
        jlDataInicio.setLocation(20, i + 75);
        jlDataInicio.setSize(100, 20);

        PanelFundo.add(jDataFim, BorderLayout.NORTH);
        jDataFim.setLocation(180, i + 100);
        jDataFim.setSize(100, 20);

        PanelFundo.add(jlDataFim, BorderLayout.NORTH);
        jlDataFim.setLocation(180, i + 75);
        jlDataFim.setSize(80, 20);

        PanelFundo.add(jlPorte, BorderLayout.NORTH);
        jlPorte.setLocation(20, i + 150);
        jlPorte.setSize(100, 20);

        //PanelFundo.add(btnPequeno, BorderLayout.NORTH);
        //btnPequeno.setLocation(20, i + 175);
        //btnPequeno.setSize(100, 20);

        //PanelFundo.add(btnMedio, BorderLayout.NORTH);
        //btnMedio.setLocation(120, i + 175);
        //btnMedio.setSize(100, 20);

        //PanelFundo.add(btnGrande, BorderLayout.NORTH);
        //btnGrande.setLocation(200, i + 175);
        //btnGrande.setSize(100, 20);

        PanelFundo.add(jlStatus, BorderLayout.NORTH);
        jlStatus.setLocation(20, i + 225);
        jlStatus.setSize((f.width / 2) - 10, 20);
        jlStatus.setText("Status");

        PanelFundo.add(btnAguardandoAprovacao, BorderLayout.NORTH);
        btnAguardandoAprovacao.setLocation(20, i + 250);
        btnAguardandoAprovacao.setSize(200, 20);

        PanelFundo.add(btnAusenciaInformacoes, BorderLayout.NORTH);
        btnAusenciaInformacoes.setLocation(240, i + 250);
        btnAusenciaInformacoes.setSize(210, 20);

        PanelFundo.add(btnAprovado, BorderLayout.NORTH);
        btnAprovado.setLocation(480, i + 250);
        btnAprovado.setSize(95, 20);

        PanelFundo.add(btnRejeitado, BorderLayout.NORTH);
        btnRejeitado.setLocation(600, i + 250);
        btnRejeitado.setSize(90, 20);


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
        //btn.botaoPortePequeno(btnPequeno);
        //btn.botaoPorteMedio(btnMedio);
        //btn.botaoPorteGrande(btnGrande);
        btn.botaoGerarRelatorio(btnGerarRelatorio);
        btn.botaoAguardandoAprovacao(btnAguardandoAprovacao);
        btn.botaoAusenciaInformacoes(btnAusenciaInformacoes);
        btn.botaoAprovado(btnAprovado);
        btn.botaoRejeitado(btnRejeitado);

        btnCancelar.addActionListener(e -> dispose());
        btnGerarRelatorio.addActionListener(e -> GerarRelatorioTratamento());

    }

    public void setLabels(){
        //jlPorte.setText("Porte");
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
        String dataInicial = jDataInicio.getText();
        String dataFinal = jDataFim.getText();
        /*int porte = 0;*/
        int status = 0;

        dataInicial = dataInicial.equals("Data Inicial") || dataInicial.equals("__/__/____") ? "" : dataInicial;
        dataFinal = dataFinal.equals("Data Final") || dataFinal.equals("__/__/____") ? "" : dataFinal;
        /*if(btnPequeno.isSelected()){
            porte = 1;
        }else if(btnMedio.isSelected()){
            porte = 2;
        }else if(btnGrande.isSelected()){
            porte = 3;
        }*/

        if(btnAguardandoAprovacao.isSelected()){
            status = 1;
        }else if(btnAusenciaInformacoes.isSelected()){
            status = 2;
        }else if(btnAprovado.isSelected()){
            status = 3;
        }else if(btnRejeitado.isSelected()){
            status = 4;
        }
        
        if(dataInicial.isEmpty() && dataFinal.isEmpty() /*&& porte == 0*/ && status == 0){
            Utilidades.showAlerta(this,"Informe ao menos um dos campos");
            return;
        }else if(!dataInicial.isEmpty() && !Validacao.validarData(Formatacao.ajustaDataAMD(dataInicial))){
            Utilidades.showAlerta(this,"Data inicial inválida");
            return;
        }else if(!dataFinal.isEmpty() && !Validacao.validarData(Formatacao.ajustaDataAMD(dataFinal))){
            Utilidades.showAlerta(this,"Data final inválida");
            return;
        }
        
        String sql = "" +
                "SELECT " +
                "ad.id AS adocao_id, " +
                "a.nome AS animal_nome, " +
                "pf.nome AS nome_tutor, " +
                "s.descricao AS adocao_status, " +
                "to_char(ad.data_registro, 'DD/MM/YYYY') AS adocao_data_registro, " +
                "(select COUNT(*) from adocao ad " +
                "INNER JOIN animal a on ad.id_animal = a.id " +
                "INNER JOIN pessoa_fisica pf on ad.id_pessoa_tutor = pf.id_pessoa " +
                "INNER JOIN adocao_status s on ad.id_adocao_status = s.id )  as total " +
                "FROM adocao ad " +
                "INNER JOIN animal a on ad.id_animal = a.id " +
                "INNER JOIN pessoa_fisica pf on ad.id_pessoa_tutor = pf.id_pessoa " +
                "INNER JOIN adocao_status s on ad.id_adocao_status = s.id " +
                "WHERE 1 = 1 ";

        if(!dataInicial.isEmpty()){
            sql += " AND ad.data_adocao >= '"+Formatacao.ajustaDataAMD(dataInicial)+"' ";
        }
        if(!dataFinal.isEmpty()){
            sql += " AND ad.data_adocao <= '"+Formatacao.ajustaDataAMD(dataFinal)+"' ";
        }
        if(status > 0){
            sql += " AND ad.id_adocao_status = "+status;
        }

        JasperReport relatorio = null;
        try {
            //relatorio = JasperCompileManager.compileReport("C:\\Users\\Christian F. Kroth\\IdeaProjects\\apamadesktop\\src\\main\\resources\\relatorios\\report_petsys_adocoes.jrxml");
            relatorio = JasperCompileManager.compileReport(System.getProperty("user.dir")+"/src/main/resources/relatorios/report_petsys_adocoes.jrxml");
            Map parametros = new HashMap();
            parametros.put("sql",sql);
            JasperPrint impressao = JasperFillManager.fillReport(relatorio, parametros, ConexaoDAO.getInstance().getConnection());
            JasperViewer.viewReport(impressao,false);
        } catch (JRException e) {
            e.printStackTrace();
            Utilidades.showAlerta(this,"Erro ao abrir relatório.");
        }
    }
}