/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package componentes;

import utilidades.FrConfig;

import java.awt.*;
import javax.swing.*;

public class ComponentLabel {

    Toolkit tk = Toolkit.getDefaultToolkit();
    Dimension d = tk.getScreenSize();
    FrConfig config = new FrConfig();

   public void labelUser(JLabel label) {
       config.ajusteIconesLogin("imagens/user.png", label);
    }
    public void labelPassword(JLabel label) {
        config.ajusteIconesLogin("imagens/lock.png", label);
    }
    public void labelPessoa(JLabel label) {
        config.ajusteLogoCabecalho("imagens/pessoa.png", label);
    }
    public void labelCidade(JLabel label) {
        config.ajusteLogoCabecalho("imagens/cidade.png", label);
    }
    public void labelVoluntario(JLabel label) {
        config.ajusteLogoCabecalho("imagens/voluntario.png", label);
    }
    public void labelTutorDoador(JLabel label) {
        config.ajusteLogoCabecalho("imagens/tutor_doador.png", label);
    }
    public void labelAnimal(JLabel label) {
        config.ajusteLogoCabecalho("imagens/animal.png", label);
    }
    public void labelDoacao(JLabel label) {
        config.ajusteLogoCabecalho("imagens/doacao.png", label);
    }
    public void labelAdocao(JLabel label) {
        config.ajusteLogoCabecalho("imagens/adocao.png", label);
    }
    public void labelLogo(JLabel label) { config.ajusteLogo("imagens/logo.png", label);}
    public void labelImagem(JLabel label) {
        config.ajusteLogoCabecalho("imagens/arquivo.png", label);
    }
    public void labelTratamento(JLabel label) {
        config.ajusteLogoCabecalho("imagens/tratamento.png", label);
    }
    }

