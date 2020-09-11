package entidades;

import utilidades.Utilidades;

public class Erro {

    private String codigoErro;
    private String mensagem;

    public Erro(String codigoErro, String mensagem) {
        this.codigoErro = codigoErro;
        this.mensagem = mensagem;
    }

    public Erro(String codigoErro){
        this.codigoErro = codigoErro;
        this.mensagem = Utilidades.getErro(codigoErro);
    }

    public String getCodigoErro() {
        return codigoErro;
    }

    public void setCodigoErro(String codigoErro) {
        this.codigoErro = codigoErro;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
