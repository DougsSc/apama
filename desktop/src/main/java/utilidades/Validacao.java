/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilidades;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author cami0
 */
public class Validacao {

    public static boolean validarDataDMA(int d, int m, int a) {
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

    public static boolean validarDataDM(int d, int m) {
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

    public static boolean validarDataFormatadaAniver(String dataComFormato) {

        boolean correto = false;
        if (dataComFormato.equals("  /  ")) {
            correto = true;
        } else {
            String[] data = dataComFormato.split("/");
            return (validarDataDM(Integer.parseInt(data[0]), Integer.parseInt(data[1])));
        }
        return correto;
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

    public static boolean validarNome(String nome) {
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

    public static boolean validarSenha(String senha) {
        boolean correto = false;
        if (senha.length() > 5 && senha.length() < 9) {
            correto = true;
        } else {
            correto = false;
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
}
