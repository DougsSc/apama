/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilidades;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;

/**
 *
 * @author cami0
 */
public class Validacao {

    public boolean validarDataDMA(int d, int m, int a) {
        boolean correto = true;
        int[] dias = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (a < 0 || m < 1 || m > 12) {
            correto = false;
        } else {
            // valida o dia
            if (a % 4 == 0 && (a % 100 != 0 || a % 400 == 0)) {
                dias[1] = 29;
            }
            if (d < 1 || d > dias[m - 1]) {
                correto = false;
            }
        }
        return (correto);
    }

    public boolean validarDataDM(int d, int m) {
        boolean correto = true;
        int[] dias = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (m > 12) {
            correto = false;
        } else {
            if (d < 1 || d > dias[m - 1]) {
                correto = false;
            }
        }

        return (correto);
    }

    public boolean validarDataFormatadaAniver(String dataComFormato) {

        boolean correto = false;
        if (dataComFormato.equals("  /  ")) {
            correto = true;
        } else {
            String[] data = dataComFormato.split("/");
            return (validarDataDM(Integer.parseInt(data[0]), Integer.parseInt(data[1])));
        }
        return correto;
    }

    public static boolean validarData(String strDate){
        if(strDate.trim().isEmpty() || Utilidades.somenteNumeros(strDate).length() != 8){
            return false;
        }

        SimpleDateFormat sdfrmt = new SimpleDateFormat("yyyy-MM-dd");
        sdfrmt.setLenient(false);

        try{
            Date javaDate = sdfrmt.parse(strDate);
            //System.out.println(strDate+" is valid date format");
        }catch (ParseException e){
            //System.out.println(strDate+" is Invalid Date format");
            return false;
        }
        return true;
    }

    public static boolean validarEmail(String email) {
        boolean correto = false;
        if (email.trim().isEmpty()) {
            correto = true;
        } else {
            if ((email.contains("@"))
                    && (email.contains("."))
                    && (!email.contains(" "))) {
                String usuario = new String(email.substring(0, email.lastIndexOf('@')));
                String dominio = new String(email.substring(email.lastIndexOf('@') + 1, email.length()));
                if ((usuario.length() >= 1)
                        && (!usuario.contains("@"))
                        && (dominio.contains("."))
                        && (!dominio.contains("@"))
                        && (dominio.indexOf(".") >= 1)
                        && (dominio.lastIndexOf(".") < dominio.length() - 1)) {

                    correto = true;
                } else {
                    correto = false;
                }
            } else {
                correto = false;
            }
        }
        return correto;
    }

    public boolean validarNome(String nome) {
        boolean correto = false;
        if (!nome.isEmpty()) {
            correto = true;
            for (int i = 0; i < nome.length(); i++) {
                if (Character.isDigit(nome.charAt(i))) {
                    correto = false;
                }

            }
        } else if (nome.length() < 4) {
            correto = false;
        } else {
            correto = false;
        }
        return correto;
    }

    public boolean validarSenha(String senha) {
        boolean correto = false;
        if (senha.length() > 5 && senha.length() < 9) {
            correto = true;
        } else {
            correto = false;
        }
        return correto;
    }

    public static boolean validarCPF(String cpf){
        // considera-se erro CPF's formados por uma sequencia de numeros iguais
        if (cpf.equals("00000000000") || cpf.equals("11111111111") ||
                cpf.equals("22222222222") || cpf.equals("33333333333") ||
                cpf.equals("44444444444") || cpf.equals("55555555555") ||
                cpf.equals("66666666666") || cpf.equals("77777777777") ||
                cpf.equals("88888888888") || cpf.equals("99999999999") || (cpf.length() != 11)
        ){
            return false;
        }

        char dig10, dig11;
        int sm, i, r, num, peso;

        try {
            sm = 0;
            peso = 10;
            for (i=0; i<9; i++) {
                num = (int)(cpf.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)){
                dig10 = '0';
            }else{
                dig10 = (char)(r + 48); // converte no respectivo caractere numerico
            }

            sm = 0;
            peso = 11;
            for(i=0; i<10; i++) {
                num = (int)(cpf.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)){
                dig11 = '0';
            }else {
                dig11 = (char)(r + 48);
            }

            if ((dig10 == cpf.charAt(9)) && (dig11 == cpf.charAt(10))){
                return true;
            }else{
                return(false);
            }
        } catch (InputMismatchException erro) {
            return false;
        }
    }

    public static boolean validarCNPJ(String CNPJ) {
        // considera-se erro CNPJ's formados por uma sequencia de numeros iguais
        if (CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111") ||
                CNPJ.equals("22222222222222") || CNPJ.equals("33333333333333") ||
                CNPJ.equals("44444444444444") || CNPJ.equals("55555555555555") ||
                CNPJ.equals("66666666666666") || CNPJ.equals("77777777777777") ||
                CNPJ.equals("88888888888888") || CNPJ.equals("99999999999999") ||
                (CNPJ.length() != 14))
            return(false);

        char dig13, dig14;
        int sm, i, r, num, peso;

        // "try" - protege o código para eventuais erros de conversao de tipo (int)
        try {
            // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 2;
            for (i=11; i>=0; i--) {
                // converte o i-ésimo caractere do CNPJ em um número:
                // por exemplo, transforma o caractere '0' no inteiro 0
                // (48 eh a posição de '0' na tabela ASCII)
                num = (int)(CNPJ.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10)
                    peso = 2;
            }

            r = sm % 11;
            if ((r == 0) || (r == 1))
                dig13 = '0';
            else dig13 = (char)((11-r) + 48);

            // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 2;
            for (i=12; i>=0; i--) {
                num = (int)(CNPJ.charAt(i)- 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10)
                    peso = 2;
            }

            r = sm % 11;
            if ((r == 0) || (r == 1))
                dig14 = '0';
            else dig14 = (char)((11-r) + 48);

            // Verifica se os dígitos calculados conferem com os dígitos informados.
            if ((dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13)))
                return(true);
            else return(false);
        } catch (InputMismatchException erro) {
            return(false);
        }
    }
}