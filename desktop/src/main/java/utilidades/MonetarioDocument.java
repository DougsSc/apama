package utilidades;

import javax.swing.text.*;

/**
 *
 * @author Christian F. Kroth
 */
public class MonetarioDocument extends PlainDocument {

    public static final int NUMERO_DIGITOS_MAXIMO = 16;

    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {

        String texto = getText(0, getLength());
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (!Character.isDigit(c)) {
                return;
            }
        }

        if (texto.length() < this.NUMERO_DIGITOS_MAXIMO) {
            super.remove(0, getLength());
            texto = texto.replace(".", "").replace(",", "").replace("R$ ", "");
            StringBuffer s = new StringBuffer(texto + str);

            //System.out.println(texto);
            //System.out.println(str);
            
            while(s.length() > 0 && s.charAt(0) == '0'){
                s.deleteCharAt(0);
            }
            /*if (s.length() > 0 && s.charAt(0) == '0') {
                s.deleteCharAt(0);
            }*/

            if (s.length() < 3) {
                if (s.length() < 1) {
                    s.insert(0,"000");
                }else if (s.length() < 2) {
                    s.insert(0,"00");
                }else{
                    s.insert(0,"0");
                }
            }

            s.insert(s.length() - 2, ",");

            if (s.length() > 6) {
                s.insert(s.length() - 6,".");
            }

            if (s.length() > 10) {
                s.insert(s.length() - 10,".");
            }

            super.insertString(0, "" + s.toString(), a);
        }
    }

    public void remove(int offset, int length) throws BadLocationException {
        super.remove(offset, length);
        String texto = getText(0, getLength());
        texto = texto.replace(",", "");
        texto = texto.replace(".", "");
        texto = texto.replace("R$ ", "");
        super.remove(0, getLength());
        insertString(0, texto, null);
    }
}