/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilidades;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicInternalFrameUI;

/**
 *
 * @author cs
 */
public class FrConfig {
    
    Toolkit tk = Toolkit.getDefaultToolkit();
    Dimension d = tk.getScreenSize();
    
    public void ajusteTela(JFrame frame) {

        // Título da Tela
        frame.setTitle("Petsys - Sistema de Gestão de Adoções");
        //Tamanho e localização da Tela
        frame.setSize(d.width / 2, d.height - 100);
        frame.setLocationRelativeTo(null);
        Point p = frame.getLocation();  
    }
    public void ajusteDialog(JDialog dialog) {

        // Título da Tela
        dialog.setTitle("Petsys - Sistema de Gestão de Adoções");
        //Tamanho e localização da Tela
        dialog.setSize(d.width / 2, d.height - 100);
        dialog.setLocationRelativeTo(null);
        Point p = dialog.getLocation();
    }

    public void ajusteDialogBusca(JDialog dialog) {

        // Título da Tela
        dialog.setTitle("Busca");
        //Tamanho e localização da Tela
        dialog.setSize(d.width / 2, d.height - 100);
        dialog.setLocationRelativeTo(null);
        Point p = dialog.getLocation();
    }
    public void ajusteDialogJustificativa(JDialog dialog) {

        // Título da Tela
        dialog.setTitle("Justificativa");
        //Tamanho e localização da Tela
        dialog.setSize(d.width / 2, d.height - 200);
        dialog.setLocationRelativeTo(null);
        Point p = dialog.getLocation();
    }
    
    public void ajusteFundo(JPanel panel){
        // Cria o fundo cinza
       panel.setLayout(null);
        panel.setBackground(new Color(196, 196, 196));
    }
    public void ajusteCabecalho(JPanel panel){
        // Cria o fundo cinza
        panel.setLocation(0, 0);
        panel.setLayout(null);
        panel.setBackground(new Color(136, 136, 136));
    }
    public void ajusteMenuLateral(JPanel panel){
        panel.setOpaque(true);
        panel.setSize(d.width/7, d.height);
        panel.setLocation(d.width-(d.width/7), 0);
        panel.setLayout(null);
        Border lineBorder = BorderFactory.createLineBorder(new Color(136, 136, 136));
        panel.setBorder(lineBorder);
        panel.setBackground(new Color(196, 196, 196));
    }

    public void ajusteInterFrame(JInternalFrame JInternalFrame){
        JInternalFrame.setLocation((d.width - JInternalFrame.getSize().width), (d.height - JInternalFrame.getSize().height)-100);
        JInternalFrame.setSize(d.width, d.height-100 );
        JInternalFrame.setBorder(null);
        ((BasicInternalFrameUI) JInternalFrame.getUI()).setNorthPane(null);
    }
    public void ajusteCabecalho (JLabel label){
        try {
            label.setBackground(Color.BLACK);
            label.setFont(new Font("Consolas", Font.BOLD, 30));

            label.setLocation(d.width /10, 0);
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    public void ajusteLogoCabecalho(String img, JLabel label){
        ImageIcon iconLogo = new ImageIcon(getClass().getClassLoader().getResource(img));
        Image imgLogo = iconLogo.getImage();
        Image newimg = imgLogo.getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH);
        ImageIcon imagemLogo = new ImageIcon(newimg);
        label.setIcon(imagemLogo);
        label.setSize(70, 70);
        label.setLocation(10, 10);
    }


    public void ajusteIconesLogin(String img, JLabel label){
        ImageIcon iconLogo = new ImageIcon(getClass().getClassLoader().getResource(img));
        Image imgLogo = iconLogo.getImage();
        Image newimg = imgLogo.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
        ImageIcon imagemLogo = new ImageIcon(newimg);
        label.setIcon(imagemLogo);
        label.setSize(40, 40);
    }
    public void ajusteLogo(String img, JLabel label){
        ImageIcon iconLogo = new ImageIcon(getClass().getClassLoader().getResource(img));
        Image imgLogo = iconLogo.getImage();
        Image newimg = imgLogo.getScaledInstance((d.width/2), (d.height/2), java.awt.Image.SCALE_SMOOTH);
        ImageIcon imagemLogo = new ImageIcon(newimg);
        label.setIcon(imagemLogo);
        label.setSize((d.width/2), (d.height/2));
    }
}
