/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package componentes;

import utilidades.FrConfig;

import java.awt.*;
import javax.swing.*;

/**
 * @author cs
 */
public class ComponentButton {

    private Toolkit tk = Toolkit.getDefaultToolkit();
    private Dimension d = tk.getScreenSize();

    int tamletra;
    int tamletraRadio;

    private void sistemaOperacional(){
        String sis = System.getProperty("os.name");
        if (sis.contains("Windows")){
            tamletra = 16;
            tamletraRadio = 14;
        }
        else {
            tamletra = 14;
            tamletraRadio = 12;
        }
    }

    public void formatarButton(JButton botao) {
        try {
            sistemaOperacional();

            botao.setContentAreaFilled(false);
            botao.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.BLUE));
            botao.setFont(new Font("Arial", Font.BOLD, tamletra));
            botao.setHorizontalAlignment(SwingConstants.LEFT);
            botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void formatarRadioButton(JRadioButton botao) {
        try {
            sistemaOperacional();

            botao.setContentAreaFilled(false);
            botao.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.BLUE));
            botao.setFont(new Font("Arial", Font.BOLD, tamletraRadio));
            botao.setHorizontalAlignment(SwingConstants.LEFT);
            botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void formatarMenu(JMenuBar bar) {
        try {
            sistemaOperacional();

            //bar.setContentAreaFilled(false);
            bar.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(131, 131, 131)));
            bar.setBackground(new Color(196, 196, 196));
            bar.setFont(new Font("Arial", Font.BOLD, tamletra));
            bar.setPreferredSize(new Dimension(255, 30));
            bar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void ajusteIcones(String img, JButton botao) {
        ImageIcon iconLogo = new ImageIcon(getClass().getClassLoader().getResource(img));
        Image imgLogo = iconLogo.getImage();
        Image newimg = imgLogo.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
        ImageIcon imagemLogo = new ImageIcon(newimg);
        botao.setIcon(imagemLogo);

    }

    public void ajusteIconesLogin(String img, JButton botao) {
        ImageIcon iconLogo = new ImageIcon(getClass().getClassLoader().getResource(img));
        Image imgLogo = iconLogo.getImage();
        Image newimg = imgLogo.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
        ImageIcon imagemLogo = new ImageIcon(newimg);
        botao.setIcon(imagemLogo);
    }

    public void botaoLogin(JButton botao) {
        ajusteIconesLogin("imagens/login.png", botao);
        botao.setText("Login");
        formatarButton(botao);
    }

    // "Ver"
    public void botaoVisualizar(JButton botao) {
        ajusteIcones("imagens/eye.png", botao);
        botao.setText("Ver");
        formatarButton(botao);
        botao.setLocation(25, (d.height / 6));
        botao.setSize((int) (d.width / 7 / 3), 22);
    }

    // "Novo"
    public void botaoNovo(JButton botao) {
        ajusteIcones("imagens/add.png", botao);
        botao.setText("Novo");
        formatarButton(botao);
        botao.setLocation(25, (d.height / 6) + 40);
        botao.setSize((int) (d.width / 7 / 2.8), 22);

    }

    // "Editar"
    public void botaoEditar(JButton botao) {
        ajusteIcones("imagens/write.png", botao);
        botao.setText("Editar");
        formatarButton(botao);
        botao.setLocation(25, (d.height / 6) + 80);
        botao.setSize((int) (d.width / 7 / 2.3), 22);
    }

    // "Excluir"
    public void botaoExcluir(JButton botao) {
        ajusteIcones("imagens/trash.png", botao);
        botao.setText("Excluir");
        formatarButton(botao);
        botao.setLocation(25, (d.height / 6) + 120);
        botao.setSize((int) (d.width / 7 / 2.1), 22);
    }
    public void botaoRelatorio(JButton botao) {
        ajusteIcones("imagens/report.png", botao);
        botao.setText("Relatório");
        formatarButton(botao);
        botao.setLocation(25, (d.height / 6) + 160);
        botao.setSize((int) (d.width / 7 / 2), 22);
    }
    public void botaoGerarRelatorio(JButton botao) {
        ajusteIcones("imagens/report.png", botao);
        botao.setText("Gerar Relatório");
        formatarButton(botao);
    }

    // "Sair"
    public void botaoSair(JButton botao) {
        ajusteIcones("imagens/cross.png", botao);
        botao.setText("Sair");
        formatarButton(botao);
        botao.setLocation(25, (d.height / 6) + 260);
        botao.setSize((int) (d.width / 7 / 3), 22);
    }

    // lupa de busca, que geralmente está presente nos formulários
    public void botaoBusca(JButton botao) {
        ajusteIcones("imagens/glass.png", botao);
        formatarButton(botao);
        botao.setSize((d.width / 63), 20);
    }

    public void botaoBuscaIFR(JButton botao) {
        ajusteIcones("imagens/glass.png", botao);
        formatarButton(botao);
        botao.setSize((d.width / 63), 20);
        botao.setLocation((d.width - (d.width / 7)) - 70, d.height / 6);
    }

    // "Salvar"
    public void botaoSalvar(JButton botao) {
        ajusteIcones("imagens/check.png", botao);
        botao.setText("Salvar");
        formatarButton(botao);
        botao.setSize((d.width / 17), 20);
    }

    // "Editar", que geralmente está presente em sub-telas, como no formulário para cadastro de uma nova doação,
    // ao buscar um tipo de doação
    public void botaoEditarDialog(JButton botao) {
        ajusteIcones("imagens/write.png", botao);
        botao.setText("Editar");
        formatarButton(botao);
        botao.setSize((d.width / 15), 20);
    }

    // "Excluir", que geralmente está presente em sub-telas, como no formulário para cadastro de uma nova doação,
    // ao buscar um tipo de doação
    public void botaoExcluirDialog(JButton botao) {
        ajusteIcones("imagens/remove.png", botao);
        botao.setText("Excluir");
        formatarButton(botao);
        botao.setSize((d.width / 15), 20);
    }

    public void botaoVerDialog(JButton botao) {
        ajusteIcones("imagens/check.png", botao);
        botao.setText("Ver");
        formatarButton(botao);
        botao.setSize((d.width / 15), 20);
    }

    public void botaoOk(JButton botao) {
        ajusteIcones("imagens/check.png", botao);
        botao.setText("OK");
        formatarButton(botao);
        botao.setSize((d.width / 15), 20);
    }

    public void botaoNova(JButton botao) {
        ajusteIcones("imagens/add.png", botao);
        botao.setText("Novo");
        formatarButton(botao);
        botao.setSize((d.width / 15), 20);
    }

    public void botaoCancelar(JButton botao) {
        ajusteIcones("imagens/cross.png", botao);
        botao.setText("Cancelar");
        formatarButton(botao);
        botao.setSize((int) (d.width / 14), 20);
    }

    public void botaoAdotar(JButton botao) {
        ajusteIcones("imagens/cross.png", botao);
        botao.setText("Adicionar Adoção");
        formatarButton(botao);
        botao.setSize((d.width / 15), 20);
    }

    public void botaoEditarAdocao(JButton botao) {
        ajusteIcones("imagens/write.png", botao);
        botao.setText("Editar Adoção");
        formatarButton(botao);
        botao.setSize((d.width / 15), 20);
    }

    public void botaoAdicionarAdocao(JButton botao) {
        ajusteIcones("imagens/add.png", botao);
        botao.setText("Editar Adoção");
        formatarButton(botao);
        botao.setSize((d.width / 15), 20);
    }

    public void botaoDadosTutor(JButton botao) {
        ajusteIcones("imagens/eye.png", botao);
        botao.setText("Dados Tutor");
        formatarButton(botao);
    }
    public void botaoDadosDoador(JButton botao) {
        ajusteIcones("imagens/eye.png", botao);
        botao.setText("Dados Doador");
        formatarButton(botao);
    }

    public void botaoVerImagem(JButton botao) {
        ajusteIcones("imagens/eye.png", botao);
        botao.setText("Arquivos");
        formatarButton(botao);
    }

    public void botaoDadosAnimal(JButton botao) {
        ajusteIcones("imagens/eye.png", botao);
        botao.setText("Dados Animal");
        formatarButton(botao);
    }

    public void botaoDadosAdocao(JButton botao) {
        ajusteIcones("imagens/eye.png", botao);
        botao.setText("Adoção");
        formatarButton(botao);
    }

    public void botaoDadosTratamento(JButton botao) {
        ajusteIcones("imagens/eye.png", botao);
        botao.setText("Tratamento");
        formatarButton(botao);
    }

    public void botaoPessoaFisica(JRadioButton botao) {
        botao.setText("Pessoa Física");
        formatarRadioButton(botao);
    }

    public void botaoPessoaJuridica(JRadioButton botao) {
        botao.setText("Pessoa Jurídica");
        formatarRadioButton(botao);
    }

    public void botaoContatoFixo(JRadioButton botao) {
        botao.setText("Fixo");
        formatarRadioButton(botao);
    }

    public void botaoContatoCelular(JRadioButton botao) {
        botao.setText("Celular");
        formatarRadioButton(botao);
    }

    public void botaoTutor(JRadioButton botao) {
        botao.setText("Tutor");
        formatarRadioButton(botao);
    }

    public void botaoAdmnistrador(JRadioButton botao) {
        botao.setText("Administrador");
        formatarRadioButton(botao);
    }

    public void botaoOperador(JRadioButton botao) {
        botao.setText("Operador");
        formatarRadioButton(botao);
    }

    public void botaoPortePequeno(JRadioButton botao) {
        botao.setText("Pequeno");
        formatarRadioButton(botao);
    }

    public void botaoPorteMedio(JRadioButton botao) {
        botao.setText("Médio");
        formatarRadioButton(botao);
    }

    public void botaoPorteGrande(JRadioButton botao) {
        botao.setText("Grande");
        formatarRadioButton(botao);
    }

    public void botaoLiberaDoacao(JRadioButton botao) {
        botao.setText("Libera Doação");
        formatarRadioButton(botao);
    }

    public void botaoDisponivel(JRadioButton botao) {
        botao.setText("Disponível");
        formatarRadioButton(botao);
    }

    public void botaoTratamento(JRadioButton botao) {
        botao.setText("Em Tratamento");
        formatarRadioButton(botao);
    }

    public void botaoAdotado(JRadioButton botao) {
        botao.setText("Adotado");
        formatarRadioButton(botao);
    }
    public void botaoObito(JRadioButton botao) {
        botao.setText("Óbito");
        formatarRadioButton(botao);
    }

    public void botaoAguardandoAprovacao(JRadioButton botao) {
        botao.setText("Aguardando Aprovação");
        formatarRadioButton(botao);
    }
    public void botaoAusenciaInformacoes(JRadioButton botao) {
        botao.setText("Ausência de Informações");
        formatarRadioButton(botao);
    }
    public void botaoAprovado(JRadioButton botao) {
        botao.setText("Aprovado");
        formatarRadioButton(botao);
    }
    public void botaoRejeitado(JRadioButton botao) {
        botao.setText("Rejeitado");
        formatarRadioButton(botao);
    }

    public void botaoPasta(JButton botao) {
        ajusteIcones("imagens/folder.png", botao);
        formatarButton(botao);
    }

    public void botaoExigeValor(JRadioButton botao) {
        botao.setText("Exigir Valor");
        formatarRadioButton(botao);
    }
    public void botaoDataResgate(JRadioButton botao) {
        botao.setText("Data Resgate");
        formatarRadioButton(botao);
    }
    public void botaoDataAdocao(JRadioButton botao) {
        botao.setText("Data Adoção");
        formatarRadioButton(botao);
    }

}
