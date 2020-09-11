package operacionais;

import cadastros.IfrAnimal;
import componentes.ComponentButton;
import componentes.ComponentLabel;
import componentes.ComponentText;
import componentes.Focus;
import entidades.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import principais.DfrBusca;
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
public final class DfrDoacaoRelatorio extends JDialog {

    private Doacao doacao;

    private JFormattedTextField jDataInicio = new JFormattedTextField();
    private JFormattedTextField jDataFim = new JFormattedTextField();

    private JLabel jlCabecalho = new JLabel();
    private JLabel jlDataInicio = new JLabel();
    private JLabel jlData = new JLabel();
    private JLabel jlDataFim = new JLabel();
    private JLabel jlTipoDoacao = new JLabel();
    private JLabel jlDoador = new JLabel();

    private JTextField jTipoDoacao = new JTextField();
    private JTextField jDoador = new JTextField();

    private JButton btnCancelar = new JButton();
    private JButton btnGerarRelatorio = new JButton();
    private JButton btnDoador = new JButton();
    private JButton btnTipoDoacao = new JButton();

    private JPanel PanelFundo = new JPanel(new BorderLayout());
    private JPanel PanelCabecalho = new JPanel(new BorderLayout());

    private FrConfig config = new FrConfig();
    private ComponentLabel lbl = new ComponentLabel();
    private ComponentButton btn = new ComponentButton();
    final Focus focus = new Focus();

    private IfrDoacao tela;

    int tamCabecalho;

    private DfrDoacaoRelatorio(JFrame jFrame, boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    DfrDoacaoRelatorio(Frame parent, boolean modal, Doacao doacao, IfrDoacao tela) {
        super(parent, modal);
        this.setLocationRelativeTo(null);
        ajusteTela();
        ajusteComponents();
        focus();
        jlCabecalho.setText("Relatório Doações");
        carregaDados();

        this.tela = tela;
    }

    public void ajusteTela() {
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(788, 700));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
               DfrDoacaoRelatorio.this.dispose();
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
        lbl.labelDoacao(ImgLogo);
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
        jlData.setText("Data Doação");

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

        PanelFundo.add(jDoador, BorderLayout.NORTH);
        jDoador.setLocation(20, i + 150);
        jDoador.setSize(500, 20);
        jDoador.setEditable(false);

        PanelFundo.add(jlDoador, BorderLayout.NORTH);
        jlDoador.setLocation(20, i + 125);
        jlDoador.setSize(100, 20);

        PanelFundo.add(btnDoador, BorderLayout.NORTH);
        btnDoador.setLocation(520, i+150);
        btnDoador.setSize(20, 20);

        PanelFundo.add(jTipoDoacao, BorderLayout.NORTH);
        jTipoDoacao.setLocation(20, i + 200);
        jTipoDoacao.setSize(500, 20);
        jTipoDoacao.setEditable(false);

        PanelFundo.add(jlTipoDoacao, BorderLayout.NORTH);
        jlTipoDoacao.setLocation(20, i + 175);

        PanelFundo.add(btnTipoDoacao, BorderLayout.NORTH);
        btnTipoDoacao.setLocation(520, i+200);
        btnTipoDoacao.setSize(20, 20);

        PanelFundo.add(btnGerarRelatorio, BorderLayout.NORTH);
        btnGerarRelatorio.setLocation(400, f.height - 80);
        btnGerarRelatorio.setSize(150, 20);

        PanelFundo.add(btnCancelar, BorderLayout.NORTH);
        btnCancelar.setLocation(600, f.height - 80);

        ComponentText txt = new ComponentText();

        // Formata os campos
        txt.formatarDataInicio(jlDataInicio, jDataInicio);
        txt.formatarDataFim(jlDataFim, jDataFim);
        txt.formatarDoador(jlDoador, jDoador);
        txt.formatarTitulo(jlData);
        txt.formatarTipoDoacao(jlTipoDoacao, jTipoDoacao);

        //Formata os botões
        btn.botaoCancelar(btnCancelar);
        btn.botaoGerarRelatorio(btnGerarRelatorio);
        btn.botaoBusca(btnDoador);
        btn.botaoBusca(btnTipoDoacao);

        btnCancelar.addActionListener(e -> dispose());
        btnGerarRelatorio.addActionListener(e -> GerarRelatorioTratamento());
        btnDoador.addActionListener(e -> selecionaDoador());
        btnTipoDoacao.addActionListener(e -> selecionaTipoDoacao());

    }

    private void carregaDados() {


        if (!jDoador.getText().isEmpty()) jlDoador.setText("Doador");
        if (!jTipoDoacao.getText().isEmpty()) jlTipoDoacao.setText("Tipo Doação");

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

    public void selecionaDoador() {
        DfrBusca buscaDoador = new DfrBusca(this, 1);
        buscaDoador.setVisible(true);
    }
    public void selecionaTipoDoacao() {
        DfrBusca buscaDoador = new DfrBusca(this, 2);
        buscaDoador.setVisible(true);
    }
    public void setTipoDoacao(TipoDoacao tipoDoacao) {
        doacao.setTipoDoacao(tipoDoacao);
        jTipoDoacao.setText(tipoDoacao.getDescricao() + " - " + tipoDoacao.getUnidadeMedida());
    }

    public void setDoador(Object object) {

        if (object instanceof PessoaFisica) {
            PessoaFisica pf = (PessoaFisica) object;

            jDoador.setText(pf.getNome() + " - " + pf.getCpf());
        } else if (object instanceof PessoaJuridica) {
            PessoaJuridica pj = (PessoaJuridica) object;
            jDoador.setText(pj.getNomeFantasia() + " - " + pj.getCnpj());
        }
    }


    public void GerarRelatorioTratamento() {
        String dataInicial = jDataInicio.getText();
        String dataFinal = jDataFim.getText();
        dataInicial = dataInicial.equals("Data Inicial") || dataInicial.equals("__/__/____") ? "" : dataInicial;
        dataFinal = dataFinal.equals("Data Final") || dataFinal.equals("__/__/____") ? "" : dataFinal;

        if(!dataInicial.isEmpty() && !Validacao.validarData(Formatacao.ajustaDataAMD(dataInicial))){
            Utilidades.showAlerta(this,"Data inicial inválida");
            return;
        }else if(!dataFinal.isEmpty() && !Validacao.validarData(Formatacao.ajustaDataAMD(dataFinal))){
            Utilidades.showAlerta(this,"Data final inválida");
            return;
        }
        
        String sql = "" +
                "SELECT " +
                "d.id AS doacao_id, " +
                "coalesce((pf.nome ),(pj.razao_social))AS pessoa_nome, " +
                "td.descricao AS doacao_tipo, " +
                "rtrim (to_char(d.valor, '99G999D99')) AS doacao_valor, " +
                "to_char(d.data, 'DD/MM/YYYY') AS doacao_data, " +
                "d.status, " +
                "td.mascara, " +
                "(select COUNT(*) from doacao d " +
                "INNER JOIN pessoa p ON d.id_pessoa = p.id " +
                "LEFT JOIN pessoa_fisica pf on p.id = pf.id_pessoa " +
                "LEFT JOIN pessoa_juridica pj on p.id = pj.id_pessoa " +
                "INNER JOIN tipo_doacao td on d.id_tipo_doacao = td.id ) as total " +
                "FROM doacao d " +
                "INNER JOIN pessoa p ON d.id_pessoa = p.id " +
                "LEFT JOIN pessoa_fisica pf on p.id = pf.id_pessoa " +
                "LEFT JOIN pessoa_juridica pj on p.id = pj.id_pessoa " +
                "INNER JOIN tipo_doacao td on d.id_tipo_doacao = td.id";
        if(!dataInicial.isEmpty()){
            sql += " AND d.data >= '"+Formatacao.ajustaDataAMD(dataInicial)+"' ";
        }
        if(!dataFinal.isEmpty()){
            sql += " AND d.data <= '"+Formatacao.ajustaDataAMD(dataFinal)+"' ";
        }

        JasperReport relatorio = null;
        try {
            //relatorio = JasperCompileManager.compileReport("C:\\Users\\Christian F. Kroth\\IdeaProjects\\apamadesktop\\src\\main\\resources\\relatorios\\report_petsys_adocoes.jrxml");
            relatorio = JasperCompileManager.compileReport(System.getProperty("user.dir")+"/src/main/resources/relatorios/report_petsys_doacoes.jrxml");
            Map parametros = new HashMap();
            parametros.put("sql",sql);
            JasperPrint impressao = JasperFillManager.fillReport(relatorio, parametros, ConexaoDAO.getInstance().getConnection());
            JasperViewer.viewReport(impressao, false);
            dispose();
        } catch (JRException e) {
            e.printStackTrace();
            Utilidades.showAlerta(this,"Erro ao abrir relatório.");
        }
    }

}