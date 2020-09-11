package utilidades;

public class ConfiguracaoEmail {

    private int tpAmb;
    private String login;
    private String senha;
    private String porta;
    private String smtp;
    private boolean requerAutenticacao;
    private boolean ssl;

    public int getTpAmb() {
        return tpAmb;
    }

    public void setTpAmb(int tpAmb) {
        this.tpAmb = tpAmb;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getPorta() {
        return porta;
    }

    public void setPorta(String porta) {
        this.porta = porta;
    }

    public String getSmtp() {
        return smtp;
    }

    public void setSmtp(String smtp) {
        this.smtp = smtp;
    }

    public boolean isRequerAutenticacao() {
        return requerAutenticacao;
    }

    public void setRequerAutenticacao(boolean requerAutenticacao) {
        this.requerAutenticacao = requerAutenticacao;
    }

    public boolean isSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

}
