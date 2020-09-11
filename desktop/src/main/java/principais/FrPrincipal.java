/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package principais;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import cadastros.IfrAnimal;
import cadastros.IfrTutorDoador;
import cadastros.IfrVoluntario;
import componentes.ComponentButton;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import operacionais.IfrAdocao;
import operacionais.IfrDoacao;
import operacionais.IfrTratamento;
import utilidades.ConexaoDAO;

/**
 *
 * @author cami0
 */
public class FrPrincipal extends javax.swing.JFrame {




    Toolkit tk = Toolkit.getDefaultToolkit();
    Dimension d = tk.getScreenSize();

    private JMenuBar bar = new JMenuBar();

    private JMenu addMenuCadastro = new JMenu("Cadastro");
    private JMenuItem newVoluntario = new JMenuItem("Voluntário");
    private JMenuItem newTutorDoador = new JMenuItem("Tutor/Doador");
    private JMenuItem newAnimal = new JMenuItem("Animal");

    private JMenu addOperacional = new JMenu("Operacional");
    private JMenuItem newDoacao = new JMenuItem("Doação");
    private JMenuItem newAdocao = new JMenuItem("Adoção");
    private JMenuItem newTratamento = new JMenuItem("Tratamento");

    private JMenu addSair = new JMenu("Sair");

    private JDesktopPane jDesktop = new JDesktopPane();
    FrLogin frlogin = new FrLogin();

    public FrPrincipal() {

        /*
        try {
            JasperReport relatorio = JasperCompileManager.compileReport("C:\\Users\\Christian F. Kroth\\IdeaProjects\\apamadesktop\\src\\main\\resources\\relatorios\\report_petsys_adocoes.jrxml");
            
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
                    "INNER JOIN adocao_status s on ad.id_adocao_status = s.id";
            
            Map parametros = new HashMap();
            parametros.put("sql",sql);
            JasperPrint impressao = JasperFillManager.fillReport(relatorio, parametros, ConexaoDAO.getInstance().getConnection());
            JasperViewer.viewReport(impressao, false);
        } catch (JRException e) {
            e.printStackTrace();
        }
        */

        ajusteTela();
//        setResizable(false); //coloca o botão maximizar desativado
        voluntario();
        animal();
        tutorDoador();
        menuBar();
        adocao();
        doacao();
        tratamento();
        sair();
        frlogin.setVisible(false);
    }

    public void ajusteTela() {
        // Título da Tela
        this.setTitle("Petsys - Sistema de Gestão de Adoções");

        //Cria area de trabalho
        jDesktop.setOpaque(true);
        this.add(jDesktop, BorderLayout.CENTER);

        //Personaliza cor de fundo
        jDesktop.setBackground(new Color(196, 196, 196));

        //Ajusta a localização e cor do fundo
        jDesktop.setSize(d.width, d.height);
        jDesktop.setLocation(d.width, d.height);
        jDesktop.setBackground(new Color(196, 196, 196));

        //Adiciona o Logo da Apama Redimensionando
        ImageIcon iconLogo = new ImageIcon(getClass().getClassLoader().getResource("imagens/logo.png"));
        Image imgLogo = iconLogo.getImage();
        Image newimg = imgLogo.getScaledInstance(d.width / 2, d.height / 2, java.awt.Image.SCALE_SMOOTH);
        ImageIcon imagemLogo = new ImageIcon(newimg);
        JLabel logo = new JLabel(imagemLogo);
        jDesktop.add(logo, BorderLayout.PAGE_START);

        // Ajusta o local do logo
        Dimension f = jDesktop.getSize();
        logo.setSize(f.width, f.height);
        logo.setLocale(null);

    }


    public void menuBar() {

        // Cria o Menu Superior
        setJMenuBar(bar);

        //Cria os itens do Menu Cadastro

        addMenuCadastro.add(newVoluntario);
        addMenuCadastro.add(newTutorDoador);
        addMenuCadastro.add(newAnimal);
        bar.add(addMenuCadastro);

        //Cria os itens do Menu Operacional

        addOperacional.add(newDoacao);
        addOperacional.add(newAdocao);
        addOperacional.add(newTratamento);
        bar.add(addOperacional);

        //Cria o Menu Sair
        JMenu addSair = new JMenu("Sair");
        bar.add(addSair);

        //Personaliza Menu
        ComponentButton ajusteBar = new ComponentButton();
        ajusteBar.formatarMenu(bar);

    }

    //Cadastro
    public void voluntario() {
        //adiciona ação no Menu
        newVoluntario.addActionListener(e -> {
            IfrVoluntario ifrvoluntario = new IfrVoluntario();
            jDesktop.add(ifrvoluntario);
            ifrvoluntario.setVisible(true);
            ifrvoluntario.setPosicao();
        });
    }

    public void tutorDoador() {
        //adiciona ação no Menu
        newTutorDoador.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                IfrTutorDoador tutorDoador = new IfrTutorDoador();
                jDesktop.add(tutorDoador);
                tutorDoador.setVisible(true);
                tutorDoador.setPosicao();
            }

        });
    }

    public void animal() {
        //adiciona ação no Menu
        newAnimal.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                IfrAnimal animal = new IfrAnimal();
                jDesktop.add(animal);
                animal.setVisible(true);
                animal.setPosicao();
            }

        });

    }

    //Operacional

    public void adocao() {
        //adiciona ação no Menu
        newAdocao.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    IfrAdocao adocao = new IfrAdocao();
                    jDesktop.add(adocao);
                    adocao.setVisible(true);
                    adocao.setPosicao();
                }

            });

    }

    public void doacao() {
        //adiciona ação no Menu
        newDoacao.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                IfrDoacao doacao = new IfrDoacao();
                jDesktop.add(doacao);
                doacao.setVisible(true);
                doacao.setPosicao();
            }

        });

    }
    public void tratamento() {
        //adiciona ação no Menu
        newTratamento.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                IfrTratamento tratamento = new IfrTratamento();
                jDesktop.add(tratamento);
                tratamento.setVisible(true);
                tratamento.setPosicao();
            }

        });

    }

    public void sair() {

        //adiciona ação no Menu
        addSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i = JOptionPane.showConfirmDialog(
                        null,
                        "Deseja sair do Sistema"
                );
                if (i == JOptionPane.YES_OPTION) {
                    FrLogin login = new FrLogin();
                    FrPrincipal principal = new FrPrincipal();
                    login.setVisible(true);
                    principal.dispose();
                } else if (i == JOptionPane.CANCEL_OPTION) {
                }

            }

        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Petsys");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 2040, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 884, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

            }
        });
    }
}
