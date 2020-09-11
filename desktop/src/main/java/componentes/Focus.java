/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package componentes;

import utilidades.Formatacao;
import utilidades.LimparCampos;
import utilidades.Utilidades;

import javax.swing.*;
import java.awt.*;

public class Focus {

    public void formatarTxtGained(JTextArea campo) {

            campo.setForeground(Color.black);
    }
    public void formatarTxtLost(JTextArea campo) {

        campo.setForeground(new Color(0, 0, 0));
    }
    public void formatarTxtGained(JTextField campo) {

        campo.setForeground(Color.black);
    }
    public void formatarTxtLost(JTextField campo) {

        campo.setForeground(new Color(0, 0, 0));
    }
    public void focusLoginGained(JLabel label, JTextField campo) {
        if (campo.getText().equals("Usuário")) {
            campo.setText("");
            label.setText("Usuário");
            this.formatarTxtGained(campo);
        }
    }

    public void focusLoginLost(JLabel label, JTextField campo) {
        if (campo.getText().isEmpty()) {
            campo.setText("Usuário");
            label.setText("");
            this.formatarTxtLost(campo);
        }
    }
    public void focusValorGained(JLabel label, JTextField campo) {
        if (campo.getText().equals("Valor")) {
            campo.setText("");
            label.setText("Valor");
            this.formatarTxtGained(campo);
        }
    }

    public void focusValorLost(JLabel label, JTextField campo) {
        if (campo.getText().isEmpty()) {
            campo.setText("Valor");
            label.setText("");
            this.formatarTxtLost(campo);
        }
    }

    public void focusSenhaGained(JLabel label, JPasswordField campo) {
        if (campo.getText().equals("Senha")) {
            campo.setText("");
            campo.setEchoChar('•');
            label.setText("Senha");
            this.formatarTxtGained(campo);
        }
    }

    public void focusSenhaLost(JLabel label, JPasswordField campo) {
        if (campo.getText().isEmpty()) {
            campo.setEchoChar((char) 0);
            campo.setText("Senha");
            label.setText("");
            this.formatarTxtLost(campo);
        }
    }

    public void focusRepetirSenhaGained(JLabel label, JPasswordField campo) {
        if (campo.getText().equals("Confirme a Senha")) {
            campo.setText("");
            campo.setEchoChar('•');
            label.setText("Confirme a Senha");
            this.formatarTxtGained(campo);

        }
    }

    public void focusRepetirSenhaLost(JLabel label, JPasswordField campo) {
        if (campo.getText().isEmpty()) {
            campo.setEchoChar((char) 0);
            campo.setText("Confirme a Senha");
            label.setText("");
            this.formatarTxtLost(campo);

        }
    }

    public void focusNomeGained(JLabel label, JTextField campo) {
        if (campo.getText().equals("Nome")) {
            campo.setText("");
            label.setText("Nome");
            this.formatarTxtGained(campo);
        }
    }
    public void focusNomeLost(JLabel label, JTextField campo) {
        if (campo.getText().isEmpty()) {
            campo.setText("Nome");
            label.setText("");
            this.formatarTxtLost(campo);
        }
    }

    public void focusNomeFantasiaGained(JLabel label, JTextField campo) {
        if (campo.getText().equals("Nome Fantasia")) {
            campo.setText("");
            label.setText("Nome Fantasia");
            this.formatarTxtGained(campo);
        }
    }
    public void focusNomeFantasiaLost(JLabel label, JTextField campo) {
        if (campo.getText().isEmpty()) {
            campo.setText("Nome Fantasia");
            label.setText("");
            this.formatarTxtLost(campo);
        }
    }

    public void focusRazaoSocialLost(JLabel label, JTextField campo) {
        if (campo.getText().isEmpty()) {
            campo.setText("Razão Social");
            label.setText("");
            this.formatarTxtLost(campo);
        }
    }

    public void focusRazaoSocialGained(JLabel label, JTextField campo) {
        if (campo.getText().equals("Razão Social")) {
            campo.setText("");
            label.setText("Razão Social");
            this.formatarTxtGained(campo);
        }
    }



    public void focusDataGained(JLabel label, JFormattedTextField campo) {
        String valor = campo.getText();
        if (valor.equals("Data Resgate")) {
            campo.setText("");
            label.setText("Data Resgate");
            this.formatarTxtGained(campo);
            Formatacao.formatarData((campo));
        }
        if (valor.isEmpty() || Utilidades.somenteNumeros(valor).length() == 8) {
            Formatacao.formatarData((campo));
            campo.setText(valor);
        }
    }

    public void focusDataLost(JLabel label, JFormattedTextField campo) {
        campo.setFormatterFactory(null);
        campo.setValue("Data Resgate");
        label.setText("");
        this.formatarTxtLost(campo);
    }

    public void focusDataInicioGained(JLabel label, JFormattedTextField campo) {
        String valor = campo.getText();
        if (valor.equals("Data Inicial")) {
            campo.setText("");
            label.setText("Data Inicial");
            this.formatarTxtGained(campo);
            Formatacao.formatarData((campo));
        }
        if (valor.isEmpty() || Utilidades.somenteNumeros(valor).length() == 8) {
            Formatacao.formatarData((campo));
            campo.setText(valor);
        }
    }

    public void focusDataInicioLost(JLabel label, JFormattedTextField campo) {
        campo.setFormatterFactory(null);
        campo.setValue("Data Inicial");
        label.setText("");
        this.formatarTxtLost(campo);
    }

    public void focusDataDoacaoGained(JLabel label, JFormattedTextField campo) {
        String valor = campo.getText();
        if (valor.equals("Data Doação")) {
            campo.setText("");
            label.setText("Data Doação");
            this.formatarTxtGained(campo);
            Formatacao.formatarData((campo));
        }
        if (valor.isEmpty() || Utilidades.somenteNumeros(valor).length() == 8) {
            Formatacao.formatarData((campo));
            campo.setText(valor);
        }
    }

    public void focusDataDoacaoLost(JLabel label, JFormattedTextField campo) {
        campo.setFormatterFactory(null);
        campo.setValue("Data Doação");
        label.setText("");
        this.formatarTxtLost(campo);
    }

    public void focusDataAdocaoGained(JLabel label, JFormattedTextField campo) {
        String valor = campo.getText();
        if (valor.equals("Data Adoção")) {
            campo.setText("");
            label.setText("Data Adoção");
            this.formatarTxtGained(campo);
            Formatacao.formatarData((campo));
        }
        if (valor.isEmpty() || Utilidades.somenteNumeros(valor).length() == 8) {
            Formatacao.formatarData((campo));
            campo.setText(valor);
        }
    }

    public void focusDataAdocaoLost(JLabel label, JFormattedTextField campo) {
        campo.setFormatterFactory(null);
        campo.setValue("Data Adoção");
        label.setText("");
        this.formatarTxtLost(campo);
    }

    public void focusDataFimGained(JLabel label, JFormattedTextField campo) {
        String valor = campo.getText();
        if (valor.equals("Data Final")) {
            campo.setText("");
            label.setText("Data Final");
            this.formatarTxtGained(campo);
            Formatacao.formatarData((campo));
        }
        if (valor.isEmpty() || Utilidades.somenteNumeros(valor).length() == 8) {
            Formatacao.formatarData((campo));
            campo.setText(valor);
        }
    }

    public void focusDataFimLost(JLabel label, JFormattedTextField campo) {
        campo.setFormatterFactory(null);
        campo.setValue("Data Final");
        label.setText("");
        this.formatarTxtLost(campo);
    }



    public void focusDataTratamentoGained(JLabel label, JFormattedTextField campo) {
        String valor = campo.getText();
        if (valor.equals("Data Tratamento")) {
            campo.setText("");
            label.setText("Data Tratamento");
            this.formatarTxtGained(campo);
            Formatacao.formatarData((campo));
        }
        if (valor.isEmpty() || Utilidades.somenteNumeros(valor).length() == 8) {
            Formatacao.formatarData((campo));
            campo.setText(valor);
        }
    }
    public void focusDataTratamentoLost(JLabel label, JFormattedTextField campo) {
        campo.setFormatterFactory(null);
        campo.setValue("Data Tratamento");
        label.setText("");
        this.formatarTxtLost(campo);
    }
    public void focusDataProximoGained(JLabel label, JFormattedTextField campo) {
        String valor = campo.getText();
        if (valor.equals("Data Próximo Tratamento")) {
            campo.setText("");
            label.setText("Data Próximo Tratamento");
            this.formatarTxtGained(campo);
            Formatacao.formatarData((campo));
        }
        if (valor.isEmpty() || Utilidades.somenteNumeros(valor).length() == 8) {
            Formatacao.formatarData((campo));
            campo.setText(valor);
        }
    }
    public void focusDataProximoLost(JLabel label, JFormattedTextField campo) {
        campo.setFormatterFactory(null);
        campo.setValue("Data Próximo Tratamento");
        label.setText("");
        this.formatarTxtLost(campo);
    }

    public void focusCPFGained(JLabel label, JFormattedTextField campo) {
        String valor = campo.getText();

        if (valor.equals("CPF")) {
            campo.setText("");
            label.setText("CPF");
            this.formatarTxtGained(campo);
            Formatacao.formatarCpf(campo);
        }
        if (valor.isEmpty() || Utilidades.somenteNumeros(valor).length() == 11) {
            Formatacao.formatarCpf(campo);
            campo.setText(valor);
        }
    }

    public void focusCPFLost(JLabel label, JFormattedTextField campo) {
        campo.setFormatterFactory(null);
        campo.setValue("CPF");
        label.setText("");
        this.formatarTxtLost(campo);
    }

    public void focusTelefoneGained(JLabel label, JFormattedTextField campo, String string) {

        String valor = campo.getText();
        if (valor.equals("Contato")) {
            campo.setText("");
            label.setText("Contato");
            this.formatarTxtGained(campo);
            if (string.equals("Celular") ) {
                Formatacao.formatarCelular(campo);
                campo.setText(valor);
            } else {
                Formatacao.formatarTelefone(campo);
                campo.setText(valor);
            }
        }
        if (valor.isEmpty() || Utilidades.somenteNumeros(valor).length() > 9) {
            if (string.equals("Celular") ) {
                Formatacao.formatarCelular(campo);
                campo.setText(valor);
    //            Formatacao.formatarTelefone(campo);
            } else {
                Formatacao.formatarTelefone(campo);
                campo.setText(valor);
            }
        }
    }

    public void focusTelefoneLost(JLabel label, JFormattedTextField campo) {
        campo.setFormatterFactory(null);
        campo.setValue("Contato");
        label.setText("");
        this.formatarTxtLost(campo);
//        System.out.println("Entrou no focus");
    }


    public void focusCNPJGained(JLabel label, JFormattedTextField campo) {
        String valor = campo.getText();
        if (valor.equals("CNPJ")) {
            campo.setText("");
            label.setText("CNPJ");
            this.formatarTxtGained(campo);
            Formatacao.formatarCnpj((campo));
        }
        if (valor.isEmpty() || Utilidades.somenteNumeros(valor).length() == 14) {
            Formatacao.formatarCnpj((campo));
            campo.setText(valor);
        }
    }
    public void focusCNPJLost(JLabel label, JFormattedTextField campo) {
        campo.setFormatterFactory(null);
        campo.setValue("CNPJ");
        label.setText("");
        this.formatarTxtLost(campo);
    }
    public void focusLogradouroGained(JLabel label, JTextField campo) {
        if (campo.getText().equals("Logradouro")) {
            campo.setText("");
            label.setText("Logradouro");
            this.formatarTxtGained(campo);
        }
    }

    public void focusLogradouroLost(JLabel label, JTextField campo) {
        if (campo.getText().isEmpty()) {
            campo.setText("Logradouro");
            label.setText("");
            this.formatarTxtLost(campo);
        }
    }

    public void focusNumeroGained(JLabel label, JTextField campo) {
        if (campo.getText().equals("Número")) {
            campo.setText("");
            label.setText("Número");
            this.formatarTxtGained(campo);
        }
    }

    public void focusNumeroLost(JLabel label, JTextField campo) {
        if (campo.getText().isEmpty()) {
            campo.setText("Número");
            label.setText("");
            this.formatarTxtLost(campo);
        }
    }

    public void focusComplementoGained(JLabel label, JTextField campo) {
        if (campo.getText().equals("Complemento")) {
            campo.setText("");
            label.setText("Complemento");
            this.formatarTxtGained(campo);
        }
    }

    public void focusComplementoLost(JLabel label, JTextField campo) {
        if (campo.getText().isEmpty()) {
            campo.setText("Complemento");
            label.setText("");
            this.formatarTxtLost(campo);
        }
    }

    public void focusBairroGained(JLabel label, JTextField campo) {
        if (campo.getText().equals("Bairro")) {
            campo.setText("");
            label.setText("Bairro");
            this.formatarTxtGained(campo);
        }
    }

    public void focusBairroLost(JLabel label, JTextField campo) {
        if (campo.getText().isEmpty()) {
            campo.setText("Bairro");
            label.setText("");
            this.formatarTxtLost(campo);
        }
    }

    public void focusCEPGained(JLabel label, JFormattedTextField campo) {
        String valor = campo.getText();
        if (valor.equals("CEP")) {
            campo.setText("");
            label.setText("CEP");
            this.formatarTxtGained(campo);
            Formatacao.formatarCEP((campo));
        }
        if (valor.isEmpty() || Utilidades.somenteNumeros(valor).length() == 8) {
            Formatacao.formatarCEP((campo));
            campo.setText(valor);
        }
    }

    public void focusCEPLost(JLabel label, JFormattedTextField campo) {
        campo.setFormatterFactory(null);
        campo.setValue("CEP");
        label.setText("");
        this.formatarTxtLost(campo);
    }



    public void focusEmailGained(JLabel label, JTextField campo) {
        if (campo.getText().equals("E-mail")) {
            campo.setText("");
            label.setText("E-mail");
            this.formatarTxtGained(campo);
        }
    }

    public void focusEmailLost(JLabel label, JTextField campo) {
        if (campo.getText().isEmpty()) {
            campo.setText("E-mail");
            label.setText("");
            this.formatarTxtLost(campo);
        }
    }
    public void focusBuscaGained(JLabel label, JTextField campo) {
        if (campo.getText().equals("Busca")) {
            campo.setText("");
            label.setText("Busca");
            this.formatarTxtGained(campo);
        }
    }

    public void focusBuscaLost(JLabel label, JTextField campo) {
        if (campo.getText().isEmpty()) {
            campo.setText("Busca");
            label.setText("");
            this.formatarTxtLost(campo);
        }
    }
    public void focusDescricaoGained(JLabel label, JTextField campo) {
        if (campo.getText().equals("Descrição")) {
            campo.setText("");
            label.setText("Descrição");
            this.formatarTxtGained(campo);
        }
    }

    public void focusDescricaoLost(JLabel label, JTextField campo) {
        if (campo.getText().isEmpty()) {
            campo.setText("Descrição");
            label.setText("");
            this.formatarTxtLost(campo);
        }
    }
    public void focusMascaraGained(JLabel label, JTextField campo) {
        if (campo.getText().equals("Máscara")) {
            campo.setText("");
            label.setText("Máscara");
            this.formatarTxtGained(campo);
        }
    }

    public void focusMascaraLost(JLabel label, JTextField campo) {
        if (campo.getText().isEmpty()) {
            campo.setText("Máscara");
            label.setText("");
            this.formatarTxtLost(campo);
        }
    }
    public void focusUnidadeGained(JLabel label, JTextField campo) {
        if (campo.getText().equals("Unidade de Medida")) {
            campo.setText("");
            label.setText("Unidade de Medida");
            this.formatarTxtGained(campo);
        }
    }

    public void focusUnidadeLost(JLabel label, JTextField campo) {
        if (campo.getText().isEmpty()) {
            campo.setText("Unidade de Medida");
            label.setText("");
            this.formatarTxtLost(campo);
        }
    }


}
