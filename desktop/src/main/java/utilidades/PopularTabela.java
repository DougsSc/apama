package utilidades;

import entidades.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PopularTabela {

    private static void montaTabela(JTable table, Object[] cabecalho, Object[][] dadosTabela) {
        table.setModel(new DefaultTableModel(dadosTabela, cabecalho) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Não editavel
            }
        });

        // Permite seleção de apenas uma linha da tabela
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(0);

        // Centraliza e negrita o titulo
        ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));

        //- Zebrar as linhas e Centraliza o texto
        StripedRowTableCellRenderer srtcr = new StripedRowTableCellRenderer();
        srtcr.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, srtcr);

        table.setRowHeight(50);
    }

    public void cidades(JTable table, ArrayList<Cidade> cidades) {
        // cabecalho da tabela
        Object[] cabecalho = {"Nome - UF"};

        // cria matriz de acordo com nº de registros da tabela
        Object[][] dadosTabela = new Object[cidades.size()][cabecalho.length];

        int lin = 0;
        try {
            for (Cidade cidade : cidades) {

                StringBuilder sb = new StringBuilder();
                if (cidade.getDescricao() != null) {
                    sb.append(cidade.getDescricao());
                    if (cidade.getUf() != null) {
                        sb.append(" - ").append(cidade.getUf());
                    }
                }

                dadosTabela[lin][0] = sb.toString();

                lin++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        montaTabela(table, cabecalho, dadosTabela);

        // redimensiona as colunas de uma tabela
        TableColumn column;
        for (int i = 0; i < table.getColumnCount(); i++) {
            column = table.getColumnModel().getColumn(i);
            switch (i) {
                case 0:
                    column.setPreferredWidth(17);
                    break;
                case 1:
                    column.setPreferredWidth(140);
                    break;
            }
        }
    }

    public void tiposDoacao(JTable table, ArrayList<TipoDoacao> tiposDoacoes) {
        // cabecalho da tabela
        Object[] cabecalho = {"Descrição", "Unidade de medida"};

        // cria matriz de acordo com nº de registros da tabela
        Object[][] dadosTabela = new Object[tiposDoacoes.size()][cabecalho.length];

        int lin = 0;
        try {
            for (TipoDoacao tipoDoacao : tiposDoacoes) {

                if (tipoDoacao.getDescricao() != null)
                    dadosTabela[lin][0] = tipoDoacao.getDescricao();
                if (tipoDoacao.getUnidadeMedida() != null)
                    dadosTabela[lin][1] = tipoDoacao.getUnidadeMedida();

                lin++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        montaTabela(table, cabecalho, dadosTabela);
    }

    /** OPERACIONAIS **/
    public static void voluntarios(JTable table, ArrayList<Usuario> usuarios) {
        // cabecalho da tabela
        Object[] cabecalho = {"Nome", "CPF", "Login", "Cidade"};
        Object[][] dadosTabela = new Object[0][0];

        if (usuarios != null)
            dadosTabela = new Object[usuarios.size()][cabecalho.length];

        /*dadosTabela[0][0] = cabecalho[0];
        dadosTabela[0][1] = cabecalho[1];
        dadosTabela[0][2] = cabecalho[2];
        dadosTabela[0][3] = cabecalho[3]; */

        int lin = 0;
        try {
            if (usuarios != null) {
                for (Usuario usuario : usuarios) {
                    if (usuario.getNome() != null)
                        dadosTabela[lin][0] = usuario.getNome();
                    if (usuario.getCpf() != null)
                        dadosTabela[lin][1] = Formatacao.formataCPF(usuario.getCpf());
                    if (usuario.getCpf() != null)
                        dadosTabela[lin][2] = usuario.getLogin();
                    if (usuario.getEndereco() != null)
                        dadosTabela[lin][3] = usuario.getEndereco().getCidade().getDescricao() + " - " + usuario.getEndereco().getCidade().getUf();

                    lin++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        montaTabela(table, cabecalho, dadosTabela);

//        // redimensiona as colunas de uma tabela
//        TableColumn column;
//        for (int i = 0; i < table.getColumnCount(); i++) {
//            column = table.getColumnModel().getColumn(i);
//            switch (i) {
//                case 0:
//                    column.setPreferredWidth(17);
//                    break;
//                case 1:
//                    column.setPreferredWidth(140);
//                    break;
////                case 2:
////                    column.setPreferredWidth(14);
////                    break;
//            }
//        }
    }

    public static void tutoresDoadores(JTable table, ArrayList<Object> doadores) {
        System.out.println("Size Doadores: " + doadores.size());
        // cabecalho da tabela
        Object[] cabecalho = {"Nome", "CPF / CNPJ", "Bairro - Cidade"};

        // cria matriz de acordo com nº de registros da tabela
        Object[][] dadosTabela = new Object[doadores.size()][cabecalho.length];

        int lin = 0;
        try {
            for (Object object : doadores) {
                if (object instanceof Pessoa) {
                    Pessoa pessoa = (Pessoa) object;
                    if (object instanceof PessoaFisica) {
                        PessoaFisica pf = (PessoaFisica) object;
                        if (pf.getNome() != null)
                            dadosTabela[lin][0] = pf.getNome();

                        if (pf.getCpf() != null)
                            dadosTabela[lin][1] = Formatacao.formataCPF(pf.getCpf());

                    } else if (object instanceof PessoaJuridica) {

                        PessoaJuridica pj = (PessoaJuridica) object;
                        if (pj.getNomeFantasia() != null)
                            dadosTabela[lin][0] = pj.getNomeFantasia();
                        if (pj.getCnpj() != null)
                            dadosTabela[lin][1] = Formatacao.formataCNPJ(pj.getCnpj());
                    }

                    if (pessoa.getEndereco() != null) {
                        String endereco = "";
                        if (pessoa.getEndereco().getBairro() != null)
                            endereco += pessoa.getEndereco().getBairro();

                        Cidade cidade = pessoa.getEndereco().getCidade();
                        if (cidade != null && cidade.getDescricao() != null)
                            endereco += " - " + cidade.getDescricao();

                        dadosTabela[lin][2] = endereco;
                    }


                }


                lin++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        montaTabela(table, cabecalho, dadosTabela);
    }

    public static void animais(JTable table, ArrayList<Animal> animais) {
        // cabecalho da tabela
        Object[] cabecalho = {"Arquivo", "Nome", "Porte", "Data de Resgate"};
        Object[][] dadosTabela = new Object[0][0];

        if (animais != null)
            dadosTabela = new Object[animais.size()][cabecalho.length];

        int lin = 0;
        try {
            if (animais != null) {
                for (Animal animal : animais) {
                    ImageIcon iconLogo = new ImageIcon(PopularTabela.class.getClassLoader().getResource("imagens/dog.jpeg"));
                    Image newimg = iconLogo.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    ImageIcon icon = new ImageIcon(newimg);

                    List<Arquivo> imagens = animal.getArquivos();
                    if (imagens != null && !imagens.isEmpty()) {
                        Arquivo arquivo = imagens.get(0);

                        if (arquivo != null) {
                            //File file = Utilidades.getTempFile(arquivo.getArquivo(), arquivo.getNome());
                            File file = Utilidades.getDecodedFile(arquivo.getArquivo(), arquivo.getNome());

                            BufferedImage image = ImageIO.read(file);
                            BufferedImage resized = Utilidades.resize(image, 100, 100);

                            //newimg = new ImageIcon(file.getAbsolutePath()).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                            //newimg = new ImageIcon(file.getAbsolutePath()).getImage();
                            newimg = new ImageIcon(resized).getImage();
                        }
                    }

                    BufferedImage rounded = Utilidades.arredondaBordas(newimg, 300);
                    icon = new ImageIcon(rounded);

                    //Image imageRounded = rounded.getScaledInstance(100,100,Image.SCALE_SMOOTH);
                    //icon = new ImageIcon(imageRounded);

                    //icon = new ImageIcon(newimg); //-OLD

                    dadosTabela[lin][0] = icon;

                    if (animal.getNome() != null)
                        dadosTabela[lin][1] = animal.getNome();

                    if (animal.getAnimalPorte() != null && animal.getAnimalPorte().getDescricao() != null)
                        dadosTabela[lin][2] = animal.getAnimalPorte().getDescricao();

                    if (animal.getDataResgate() != null)
                        dadosTabela[lin][3] = Formatacao.ajustaDataDMA(animal.getDataResgate());

                    lin++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        PopularTabela.montaTabela(table, cabecalho, dadosTabela);
        table.setModel(new DefaultTableModel(dadosTabela, cabecalho) {
            @Override
            // quando retorno for FALSE, a tabela nao é editavel
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            // alteracao no metodo que determina a coluna em que o objeto ImageIcon devera aparecer
            @Override
            public Class getColumnClass(int column) {
                if (column == 0)
                    return ImageIcon.class;
                return Object.class;
            }
        });

        table.setRowHeight(105);
    }

    public static void doacoes(JTable table, ArrayList<Doacao> doacoes) {
        // cabecalho da tabela
        Object[] cabecalho = {"Nome", "Doação", "Data do Ocorrido"};
        Object[][] dadosTabela = new Object[0][0];

        if (doacoes != null)
            dadosTabela = new Object[doacoes.size()][cabecalho.length];

        int lin = 0;
        try {
            if (doacoes != null) {
                for (Doacao doacao : doacoes) {
                    if (doacao.getPessoaFisica() != null) {
                        PessoaFisica pf = doacao.getPessoaFisica();
                        dadosTabela[lin][0] = pf.getNome();
                    } else if (doacao.getPessoaJuridica() != null) {
                        PessoaJuridica pj = doacao.getPessoaJuridica();
                        dadosTabela[lin][0] = pj.getNomeFantasia();
                    }
//                    if (Utilidades.isPessoaFisica(json)) {
//                        PessoaFisica pf = doacao.getPessoaFisica();
//                        dadosTabela[lin][0] = pf.getNome();
//                    } else {
//                        PessoaJuridica pj = doacao.getPessoaJuridica();
//                        dadosTabela[lin][0] = pj.getNomeFantasia();
//                    }

                    if (doacao.getTipoDoacao() != null)
                        dadosTabela[lin][1] = doacao.getTipoDoacao().getDescricao();

                    if (doacao.getObservacao() != null)
                        dadosTabela[lin][2] = doacao.getData();

                    lin++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        montaTabela(table, cabecalho, dadosTabela);
    }

    public static void adocoes(JTable table, ArrayList<Adocao> adocoes) {
        Object[] cabecalho = {"Data","Tutor","Animal","Status"};
        Object[][] dadosTabela = new Object[0][0];

        if (adocoes != null)
            dadosTabela = new Object[adocoes.size()][cabecalho.length];

        int lin = 0;
        try {
            if (adocoes != null) {
                for (Adocao adocao : adocoes) {
                    String data = adocao.getDataAdocao() != null ? Formatacao.ajustaDataDMA(adocao.getDataAdocao()) : "-";
                    String tutor = adocao.getTutor() != null && adocao.getTutor().getNome() != null ? adocao.getTutor().getNome() : "";
                    String animal = adocao.getAnimal() != null && adocao.getAnimal().getNome() != null ? adocao.getAnimal().getNome() : "";
                    String status = adocao.getStatus() != null && adocao.getStatus().getDescricao() != null ? adocao.getStatus().getDescricao() : "";

                    dadosTabela[lin][0] = data;
                    dadosTabela[lin][1] = tutor;
                    dadosTabela[lin][2] = animal;
                    dadosTabela[lin][3] = status;

                    lin++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        montaTabela(table, cabecalho, dadosTabela);
    }

    public static void tratamentos(JTable tabela, ArrayList<AnimalTratamento> tratamentos) {
        // cabecalho da tabela
        Object[] cabecalho = {"Data","Animal","Tratamento", "Próximo tratamento"};

        // cria matriz de acordo com nº de registros da tabela
        Object[][] dadosTabela = new Object[tratamentos.size()][cabecalho.length];

        int lin = 0;
        try {
            for (AnimalTratamento tratamento : tratamentos) {
                if(tratamento.getDataTratamento() != null){
                    dadosTabela[lin][0] = Formatacao.ajustaDataDMA(tratamento.getDataTratamento());
                }

                if(tratamento.getAnimal() != null && tratamento.getAnimal().getNome() != null){
                    dadosTabela[lin][1] = tratamento.getAnimal().getNome();
                }

                if(tratamento.getTipoTratamento() != null && tratamento.getTipoTratamento().getDescricao() != null){
                    dadosTabela[lin][2] = tratamento.getTipoTratamento().getDescricao();
                }

                if(tratamento.getProximaDataTratamento() != null){
                    dadosTabela[lin][3] = Formatacao.ajustaDataDMA(tratamento.getProximaDataTratamento());
                }

                lin++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        montaTabela(tabela, cabecalho, dadosTabela);
    }

    public static void arquivos(JTable tabela, ArrayList<Arquivo> arquivos) {
        Object[] cabecalho = {"Imagem", "Nome"};
        Object[][] dadosTabela = new Object[arquivos.size()][cabecalho.length];

        int lin = 0;
        try {
            for (Arquivo arquivo : arquivos) {
                if (arquivo.getArquivo() != null && !arquivo.getArquivo().isEmpty()) {
                    File file = Utilidades.getDecodedFile(arquivo.getArquivo(), arquivo.getNome());

                    BufferedImage image = ImageIO.read(file);
                    BufferedImage resized = Utilidades.resize(image, 100, 100);

                    Image newimg = new ImageIcon(resized).getImage();
                    BufferedImage rounded = Utilidades.arredondaBordas(newimg, 300);
                    ImageIcon icon = new ImageIcon(rounded);

                    dadosTabela[lin][0] = icon;
                }

                if (arquivo.getNome() != null)
                    dadosTabela[lin][1] = arquivo.getNome();

                lin++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        montaTabela(tabela, cabecalho, dadosTabela);
        tabela.setModel(new DefaultTableModel(dadosTabela, cabecalho) {
            @Override
            // quando retorno for FALSE, a tabela nao é editavel
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            // alteracao no metodo que determina a coluna em que o objeto ImageIcon devera aparecer
            @Override
            public Class getColumnClass(int column) {
                if (column == 0)
                    return ImageIcon.class;
                return Object.class;
            }
        });

        tabela.setRowHeight(105);
    }

    /*
    public ArrayList<Usuario> buscaVoluntarios(JTable table,String nome) {
        // cabecalho da tabela
        Object[] cabecalho = {"Nome","CPF","Login","Cidade"};

        HashMap<String, String> params = new HashMap<>();
        params.put("nome",nome);

        // cria matriz de acordo com nº de registros da tabela
        ServicoCliente sv = new ServicoCliente();
        String json = sv.buscaDados("usuario", ServicoCliente.GET, "", params);

        System.out.println(json);

        ArrayList<Usuario> dados = new Gson().fromJson(json, new TypeToken<ArrayList<Usuario>>() {}.getType());
        Object[][] dadosTabela = new Object[dados.size()][cabecalho.length];

        int lin = 0;
        try {
            for (Usuario usuario: dados) {
//              dadosTabela[lin][0] = usuario.getId();
                dadosTabela[lin][0] = usuario.getNome();
                dadosTabela[lin][1] = Formatacao.formatarCPF(usuario.getCpf());
                dadosTabela[lin][2] = usuario.getLogin();
                dadosTabela[lin][3] = usuario.getEndereco().getCidade().getDescricao() + " - " + usuario.getEndereco().getCidade().getUf();

                lin++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.montaTabela(table,cabecalho,dadosTabela);

        // redimensiona as colunas de uma tabela
        TableColumn column;
        for (int i = 0; i < table.getColumnCount(); i++) {
            column = table.getColumnModel().getColumn(i);
            switch (i) {
                case 0:
                    column.setPreferredWidth(17);
                    break;
                case 1:
                    column.setPreferredWidth(140);
                    break;
//                case 2:
//                    column.setPreferredWidth(14);
//                    break;
            }
        }

        return dados;
    }
    */
}
