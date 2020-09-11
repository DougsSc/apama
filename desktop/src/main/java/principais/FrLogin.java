package principais;

import com.google.gson.Gson;
import componentes.ComponentButton;
import componentes.ComponentLabel;
import componentes.ComponentText;
import componentes.Focus;
import entidades.Erro;
import entidades.Usuario;
import utilidades.FrConfig;
import utilidades.ServicoCliente;
import utilidades.Sessao;
import utilidades.Utilidades;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.logging.Level;
import javax.swing.*;

public final class FrLogin extends javax.swing.JFrame {

    //boolean aut = false; caso queira utilizar dessa forma, apague a mesma variável que está abaixo, no método
    Toolkit tk = Toolkit.getDefaultToolkit();
    Dimension d = tk.getScreenSize();


    //compoentes utilizados na tela
    JTextField jLogin = new JTextField();
    JLabel jlLogin = new JLabel();

    JLabel jlSenha = new JLabel();
    JPasswordField jSenha = new JPasswordField();

    JButton btnEntrar = new JButton();

    ComponentText txt = new ComponentText();
    private ComponentButton btn = new ComponentButton();
    private ComponentLabel lbl = new ComponentLabel();

    public FrLogin() {
        initComponents();
        focus();
        ajusteComponents();
        ajusteTela();
        acaoBotao();
        setResizable(false);
    }


    public void ajusteTela() {

        FrConfig configTela = new FrConfig();

        configTela.ajusteTela(this);

        Dimension g = this.getSize();

               //cria o fundo cinza

        Container c = this.getContentPane();

        c.setLayout(new FlowLayout()); //altera o layout para FlowLayout explicitamente


        JPanel PanelFundo = new JPanel();
        configTela.ajusteFundo(PanelFundo);
        getContentPane().add(PanelFundo);
        PanelFundo.setPreferredSize(new Dimension(g.width, g.height));

        //adiciona o logo da Apama Redimensionando
        JLabel ImgLogo = new JLabel();
        lbl.labelLogo(ImgLogo);
        PanelFundo.add(ImgLogo);

        Dimension f = this.getSize();
        ImgLogo.setLocation(0, 0);

        Dimension l = ImgLogo.getSize();
        int i = l.height;
        //ajusta o local dos campos Login/Senha

        PanelFundo.add(jlLogin);
        jlLogin.setLocation((f.width / 2) - 125, i -25);
        jlLogin.setSize(250, 20);

        PanelFundo.add(jLogin);
        jLogin.setLocation((f.width / 2) - 125, i);
        jLogin.setSize(250, 20);

        PanelFundo.add(jlSenha);
        jlSenha.setLocation((f.width / 2) - 125, i + 25);
        jlSenha.setSize(250, 20);

        PanelFundo.add(jSenha);
        jSenha.setLocation((f.width / 2) - 125, i + 50);
        jSenha.setSize(250, 20);

        //ajusta o botÃƒÂ£o Entrar

        PanelFundo.add(btnEntrar);
        btnEntrar.setLocation((f.width / 2) + 56, i+75);
        btnEntrar.setSize(80,30);

        btn.botaoLogin(btnEntrar);
        //adiciona imagem user
        JLabel ImgLogin = new JLabel();
        lbl.labelUser(ImgLogin);
        PanelFundo.add(ImgLogin);
        ImgLogin.setLocation((f.width / 2) - 160, i -17);

       //adiciona imagem senha
        JLabel ImgSenha = new JLabel();
        lbl.labelPassword(ImgSenha);
        PanelFundo.add(ImgSenha);
        ImgSenha.setLocation((f.width / 2) - 160, i + 32);

    }


    //ajusta os componentes para ficarem sem borda
    public void ajusteComponents() {

        //formatar campo Login
        txt.formatarLogin(jlLogin, jLogin);

        //formatar campo Senha
        txt.formatarSenha(jlSenha, jSenha);

        //formatar os Botões
        ComponentButton btn;
        btn = new ComponentButton();
        btn.formatarButton(btnEntrar);
    }


    //altera os campos Usuário e Senha ao clicar no campo
    public void focus() {

        final Focus focus = new Focus();
        getRootPane().setDefaultButton(btnEntrar);
        btnEntrar.requestFocus();
        jLogin.addKeyListener(new java.awt.event.KeyListener() {
            public void keyTyped(java.awt.event.KeyEvent e) {
            }

            //ao digitar some o texto
            public void keyPressed(java.awt.event.KeyEvent e) {
                focus.focusLoginGained(jlLogin, jLogin);
            }

            //caso estiver vazio volta aparecer usuário
            public void keyReleased(java.awt.event.KeyEvent e) {
                focus.focusLoginLost(jlLogin, jLogin);
            }
        });

        jSenha.addKeyListener(new java.awt.event.KeyListener() {
            public void keyTyped(java.awt.event.KeyEvent e) {
                focus.focusSenhaGained(jlSenha, jSenha);
            }

            //ao digitar some o texto
            public void keyPressed(java.awt.event.KeyEvent e) {
            }

            //caso estiver vazio volta aparecer usuário
            public void keyReleased(java.awt.event.KeyEvent e) {
                focus.focusSenhaLost(jlSenha, jSenha);
            }
        });
    }




    private void acaoBotao() {

        btnEntrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                boolean aut = false;

                HashMap<String, String> sendMap = new HashMap<>();
                sendMap.put("senha", jSenha.getText());
                sendMap.put("login", jLogin.getText());

                ServicoCliente sv = new ServicoCliente();
                String json = sv.buscaDados("login", ServicoCliente.GET, "", sendMap, true);

                System.out.println(json);
                Gson gson = new Gson();
                Usuario usuario = gson.fromJson(json, Usuario.class);
                Sessao.shared().setUsuarioLogado(usuario);

//                Map<String, Object> map = gson.fromJson(json, new TypeToken<Map<String, Object>>() {}.getType());
//                if (map.containsKey("token"))
//                    Sessao.getInstancia().token = String.valueOf(map.get("token"));

                if (Sessao.shared().getUsuarioLogado() != null && !Sessao.shared().getUsuarioLogado().getToken().isEmpty())
                    aut = true;

                if (aut) {
                    FrPrincipal frprincipal = new FrPrincipal();
                    frprincipal.setVisible(true);
                    frprincipal.setExtendedState(MAXIMIZED_BOTH);

                    // fecha a tela de login
                    setVisible(false);
                    dispose();
                } else {
                    jSenha.setText("");
                    Erro erro = new Erro("","Usuário ou Senha Incorretos");

                    Utilidades.showErro(FrLogin.this, erro);
    /*                ImageIcon imagem = new ImageIcon(System.getProperty("user.dir")+"/src/main/resources/imagens/erro.png");

                    int i = JOptionPane.showConfirmDialog(
                            FrLogin.this,
                            "Usuário ou Senha Incorreta \n"
                                    + "Tentar novamente?"," Erro",JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, imagem
                    );
                    if (i == JOptionPane.YES_OPTION) {
                        txt.formatarSenha(jlSenha, jSenha);
                    } else if (i == JOptionPane.NO_OPTION) {
                        System.exit(0);
                    } else if (i == JOptionPane.CANCEL_OPTION) {
                        txt.formatarSenha(jlSenha, jSenha);
                    }*/
                }
            }
        });



    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(700, 622));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 723, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 752, Short.MAX_VALUE)
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
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(FrLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrLogin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
