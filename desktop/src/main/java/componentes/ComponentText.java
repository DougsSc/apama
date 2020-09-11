/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package componentes;

import utilidades.Validacao;
import java.awt.*;
import java.text.ParseException;
import javax.swing.*;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author cs
 */
public class ComponentText {

    private Toolkit tk = Toolkit.getDefaultToolkit();
    private Dimension d = tk.getScreenSize();

    public static JFormattedTextField getFormatado(String formato) {
        JFormattedTextField campoFormatado = null;
        MaskFormatter format = new MaskFormatter();

        format.setPlaceholderCharacter(' ');
        format.setValueContainsLiteralCharacters(false);

        try {
            format.setMask(formato);
            campoFormatado = new JFormattedTextField(format);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return campoFormatado;
    }

    public void formatarTxt(JTextField campo) {
        try {
            campo.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
            campo.setOpaque(false);
            campo.setForeground(new Color(0, 0, 0));
           campo.setFont(new Font("Arial", Font.BOLD, 14));
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void formatarTxt(JTextArea campo) {
        try {
            campo.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.BLACK));
            campo.setOpaque(false);
            campo.setForeground(new Color(0, 0, 0));
            campo.setFont(new Font("Arial", Font.BOLD, 14));
        } catch (Exception e) {
            System.err.println(e);
        }
    }



    public void formatarlabel(JLabel label) {
        try {
            label.setBackground(Color.BLACK);
            label.setFont(new Font("Arial", Font.BOLD, 14));
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void formatarCombobox(JComboBox combo) {

    }

    public void formatarSenha(JLabel label, JPasswordField campo) {
        try {
            campo.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
            campo.setOpaque(false);
            campo.setForeground(new Color(0, 0, 0));
            campo.setFont(new Font("Arial", Font.BOLD, 14));
            this.formatarlabel(label);
            label.setText("");
            campo.setText("Senha");
            campo.setEchoChar((char) 0);

        } catch (Exception e) {
            System.err.println(e);
        }

    }

    public void formatarConfirmarSenha(JLabel label, JPasswordField campo) {
        try {
            this.formatarTxt(campo);
            this.formatarlabel(label);
            label.setText("");
            campo.setText("Confirme a Senha");
            campo.setEchoChar((char) 0);

        } catch (Exception e) {
            System.err.println(e);
        }

    }

    public void formatarLogin(JLabel label, JTextField campo) {

        this.formatarTxt(campo);
        this.formatarlabel(label);
        label.setText("");
        campo.setText("Usuário");
    }

    public void formatarBusca(JLabel label, JTextField campo) {

        this.formatarTxt(campo);
        this.formatarlabel(label);
        label.setText("");
        campo.setText("Busca");
        campo.setLocation(20, d.height / 6);
        label.setLocation(20, (d.height / 6) - 25);
    }


    public void formatarNome(JLabel label, JTextField campo) {
        this.formatarlabel(label);
        this.formatarTxt(campo);
        label.setText("");
        campo.setText("Nome");

    }
    public void formatarRazaoSocial(JLabel label, JTextField campo) {
        this.formatarlabel(label);
        this.formatarTxt(campo);
        label.setText("");
        campo.setText("Razão Social");

    }

    public void formatarCPF(JLabel label, JFormattedTextField campo) {
        this.formatarlabel(label);
        this.formatarTxt(campo);
        label.setText("");
        campo.setText("CPF");

    }
    public void formatarCNPJ(JLabel label, JFormattedTextField campo) {
        this.formatarlabel(label);
        this.formatarTxt(campo);
        label.setText("");
        campo.setText("CNPJ");

    }

    public void formatarLogradouro(JLabel label, JTextField campo) {
        this.formatarlabel(label);
        this.formatarTxt(campo);
        label.setText("");
        campo.setText("Logradouro");
    }

    public void formatarNumero(JLabel label, JTextField campo) {
        this.formatarlabel(label);
        this.formatarTxt(campo);
        label.setText("");
        campo.setText("Número");
    }

    public void formatarComplemento(JLabel label, JTextField campo) {
        this.formatarlabel(label);
        this.formatarTxt(campo);
        label.setText("");
        campo.setText("Complemento");

    }

    public void formatarBairro(JLabel label, JTextField campo) {
        this.formatarlabel(label);
        this.formatarTxt(campo);
        label.setText("");
        campo.setText("Bairro");

    }

    public void formatarCEP(JLabel label, JTextField campo) {
        this.formatarlabel(label);
        this.formatarTxt(campo);
        label.setText("");
        campo.setText("CEP");

    }
    public void formatarCidade(JLabel label, JTextField campo) {
        this.formatarlabel(label);
        this.formatarTxt(campo);
        label.setText("");
        campo.setText("Cidade");
    }

    public void formatarTipoTelefone(JComboBox combo) {
        this.formatarCombobox(combo);
        combo.addItem("Fixo");
        combo.addItem("Celular");
    }

    public void formatarTelefone(JLabel label, JTextField campo) {
        this.formatarlabel(label);
        this.formatarTxt(campo);
        label.setText("");
        campo.setText("Contato");

    }

    public void formatarEmail(JLabel label, JTextField campo) {
        this.formatarlabel(label);
        this.formatarTxt(campo);
        label.setText("");
        campo.setText("E-mail");
        Validacao valida = new Validacao();
    }

    public void formatarTutor(JLabel label, JTextField campo) {
        this.formatarlabel(label);
        this.formatarTxt(campo);
        label.setText("");
        campo.setText("Tutor");
    }

    public void formatarAnimal(JLabel label, JTextField campo) {
        this.formatarlabel(label);
        this.formatarTxt(campo);
        label.setText("");
        campo.setText("Animal");
    }
    public void formatarTipoTratamento(JLabel label, JTextField campo) {
        this.formatarlabel(label);
        this.formatarTxt(campo);
        label.setText("Tratamento");
    }

    public void formatarTitulo(JLabel label) {
        label.setBackground(Color.BLACK);
        label.setFont(new Font("Consolas", Font.BOLD, 16));
    }

    public void formatarDataResgate(JLabel label, JTextField campo) {
        this.formatarlabel(label);
        this.formatarTxt(campo);
        label.setText("");
        campo.setText("Data Resgate");
    }

    public void formatarDataAdocao(JLabel label, JTextField campo) {
        this.formatarlabel(label);
        this.formatarTxt(campo);
        label.setText("");
        campo.setText("Data Adoção");
    }
    public void formatarDataTratamento(JLabel label, JTextField campo) {
        this.formatarlabel(label);
        this.formatarTxt(campo);
        label.setText("");
        campo.setText("Data Tratamento");
    }
    public void formatarDataTratamentoProximo(JLabel label, JTextField campo) {
        this.formatarlabel(label);
        this.formatarTxt(campo);
        label.setText("");
        campo.setText("Data Próximo Tratamento");
    }

    public void formatarDataInicio(JLabel label, JTextField campo) {
        this.formatarlabel(label);
        this.formatarTxt(campo);
        label.setText("");
        campo.setText("Data Inicial");
    }
    public void formatarDataFim(JLabel label, JTextField campo) {
        this.formatarlabel(label);
        this.formatarTxt(campo);
        label.setText("");
        campo.setText("Data Final");
    }

    public void formatarDataDoacao(JLabel label, JTextField campo) {
        this.formatarlabel(label);
        this.formatarTxt(campo);
        label.setText("");
        campo.setText("Data Doação");
    }

    public void formatarNomeFantasia(JLabel label, JTextField campo) {
        this.formatarlabel(label);
        this.formatarTxt(campo);
        label.setText("");
        campo.setText("Nome Fantasia");
    }

    public void formatarObservacao(JTextArea campo) {
        this.formatarTxt(campo);
        campo.setLineWrap(true);
    }

    public void formatarDoador(JLabel label, JTextField campo) {
        this.formatarlabel(label);
        this.formatarTxt(campo);
        label.setText("");
        campo.setText("Doador");
    }

    public void formatarTipoDoacao(JLabel label, JTextField campo) {
        this.formatarlabel(label);
        this.formatarTxt(campo);
        label.setText("");
        campo.setText("Tipo Doação");
    }

    public void formatarValor(JLabel label, JTextField campo) {
        this.formatarlabel(label);
        this.formatarTxt(campo);
        label.setText("");
        campo.setText("Valor");
    }

    public void formatarTipoUser(JLabel label) {
        this.formatarlabel(label);
        label.setText("Tipo Usuário");
    }

    public void formatarDescricao(JLabel label, JTextField campo) {
        this.formatarlabel(label);
        this.formatarTxt(campo);
        label.setText("");
        campo.setText("Descrição");
    }

    public void formatarUnidadedeMedida(JLabel label, JTextField campo) {
        this.formatarlabel(label);
        this.formatarTxt(campo);
        label.setText("");
        campo.setText("Unidade de Medida");
    }

    public void formatarMascara(JLabel label, JTextField campo) {
        this.formatarlabel(label);
        this.formatarTxt(campo);
        label.setText("");
        campo.setText("Máscara");
    }
    public void formatarImagem(JLabel label) {
        this.formatarlabel(label);
    }

    public void formatarCampo(JTextField campo) {
        this.formatarTxt(campo);
    }
}

