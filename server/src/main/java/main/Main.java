package main;

import com.google.gson.Gson;
import dao.*;
import entidades.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilidades.Utilidades;
import utilidades.Validacao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class.getName());

    public static void main(String[] argv) {
        if (Utilidades.testaConexao()){
            System.out.println("Iniciou");
            new Thread(new Server()).start();
        }else{
            System.out.println("Erro ao conectar com o BD");
            logger.error("Erro de conexão com o BD");
        }

        /*
        HashMap<String,String> map = new HashMap<>();
        map.put("nome","a");

        UsuarioDAO dao = new UsuarioDAO();
        List<Usuario> usuarios = dao.buscaCustom(map);

        if(usuarios == null){
            System.out.println("Nenhum usuario encontrado");
        }else{
            for (Usuario u : usuarios){
                System.out.println(u.getNome());
            }
        }*/



        /*
        AnimalTratamentoDAO dao = new AnimalTratamentoDAO();
        AnimalTratamento a = dao.busca(4);

        System.out.println(a.getDataTratamento());
        System.out.println(a.getProximaDataTratamento());
        */
        /*
        AnimalTratamentoDAO dao = new AnimalTratamentoDAO();
        AnimalTratamento at = new AnimalTratamento();
        Animal a = new Animal();
        TipoTratamento t = new TipoTratamento();

        a.setId(1);
        t.setId(1);

        at.setAnimal(a);
        at.setTipoTratamento(t);
        at.setObservacao("Cadastro de teste");
        at.setDataTratamento("2019-11-23");
        at.setStatus(1);

        dao.cadastra(at);
        */

        //listaVoluntarios();

        //cadastraDoacao();

        //testeCadastroUsuario();
        //testeAtualizaUsuario();
        //testePopupaTabela();

        //Usuario usuario = new UsuarioDAO().busca(9);
        //System.out.println(usuario.getEndereco().getLogradouro());
        //System.out.println(usuario.getEndereco().getCidade().getDescricao());

        /*
        String cpf = "123.456.789-09";
        System.out.println(cpf);

        cpf = Utilidades.somenteNumeros(cpf);
        System.out.println(cpf);

        cpf = Utilidades.formataCpf(cpf);
        System.out.println(cpf);
        */


        /*String user = "admin";
        String senha = "admin";
        if(testeLogin(user,senha)){
            System.out.println("Login válido");
        }else{
            System.out.println("Login INVÁLIDO");
        }*/
    }

    public static void listaVoluntarios(){
        UsuarioDAO dao = new UsuarioDAO();

        List<Usuario> usuarios = dao.lista(null,0,5);
        for(Usuario usuario : usuarios){
            System.out.println(usuario.getNome());
        }

        System.out.println("- - - - - - - - - - - - - - - - - -");

        usuarios = dao.lista(null,5,5);
        for(Usuario usuario : usuarios){
            System.out.println(usuario.getNome());
        }

        System.out.println("- - - - - - - - - - - - - - - - - -");

        usuarios = dao.lista(null,10,5);
        for(Usuario usuario : usuarios){
            System.out.println(usuario.getNome());
        }
    }

    public static void cadastraDoacao(){
        PessoaFisica pessoa = new PessoaFisica();
        pessoa.setId(4);

        TipoDoacao tipoDoacao = new TipoDoacao();
        tipoDoacao.setId(1);

        Doacao doacao = new Doacao();

        doacao.setPessoaFisica(pessoa);
        doacao.setTipoDoacao(tipoDoacao);
        doacao.setStatus(1);
        doacao.setData("2019-11-11 01:16:23");
        doacao.setValor(100.00);

        System.out.println(new DoacaoDAO().cadastra(doacao));
    }

    public static void testeListagemAnimal(){
        List<Animal> animal = new AnimalDAO().lista(null,null,null);

        for (Animal dado : animal){
                System.out.print(dado.getNome() + " ");
                System.out.print(dado.getId() + " ");
                System.out.print(dado.getAnimalStatus().getDescricao() + " ");
                System.out.println("");
            }
    }

    public static void testePopupaTabela(){
        List<String[]> dados = new ArrayList<>(); //new UsuarioDAO().populaTabela();
        if(dados.size() > 0){
            for (String[] dado : dados){
                System.out.println(dado[0]);
                System.out.println(dado[1]);
                System.out.println(dado[2]);
                System.out.println(dado[3]);
                System.out.println(dado[4]);
            }
        }else{
            System.out.println("Sem dados");
        }
    }

    public static boolean testeLogin(String usuario, String senha){
//        if(new UsuarioDAO().login(usuario,senha)){
//            return true;
//        }
        return false;
    }

    public static boolean testeAtualizaUsuario(){
        int userID = 16;

        Usuario usuario = new UsuarioDAO().busca(userID);
        if(usuario == null){
            System.out.println("Erro ao buscar dados do usuário");
            return false;
        }

        usuario.setIdTipoUsuario(2);
        usuario.setLogin("edit");
        usuario.setSenha(Utilidades.criptografaSenha("edit"));
        usuario.setStatusUsuario(1);
        usuario.setNome("Christian Edit");
        usuario.setCpf("12345678909");
        usuario.setIdTipoPessoa(2);
        usuario.setStatus(1);
        usuario.setLiberarAdocao(1);

        if(!new UsuarioDAO().atualiza(usuario)){
            System.out.println("Erro ao atualizar os dados do usuário");
            return false;
        }

        Endereco endereco = new PessoaDAO().getEndereco(usuario.getId());
        endereco.setLogradouro("edit");
        endereco.setNumero("edit");
        endereco.setComplemento("edit");
        endereco.setBairro("edit");
        endereco.setCep("95900000");
        if(!new EnderecoDAO().atualiza(endereco)){
            System.out.println("Erro ao atualizar os dados do endereço");
            return false;
        }

        List<Contato> contatos = new PessoaDAO().getContatos(usuario.getId());
        if(contatos != null){
            for(Contato contato : contatos){
                contato.setStatus(0);

                if(!new ContatoDAO().atualiza(contato)){
                    System.out.println("Erro ao inativar contatos do usuário");
                }
            }
        }

        TipoContato t1 = new TipoContato();
        Contato c1 = new Contato();
        t1.setId(1);

        c1.setIdPessoa(usuario.getId());
        c1.setTipoContato(t1);
        c1.setContato("(51)3714-1234");
        c1.setObservacao("Edit");
        c1.setStatus(1);

        TipoContato t2 = new TipoContato();
        Contato c2 = new Contato();
        t2.setId(2);

        c2.setIdPessoa(usuario.getId());
        c2.setTipoContato(t2);
        c2.setContato("(51)99748-6229");
        c2.setObservacao("Edit");
        c2.setStatus(1);

        TipoContato t3 = new TipoContato();
        Contato c3 = new Contato();
        t3.setId(3);

        c3.setIdPessoa(usuario.getId());
        c3.setTipoContato(t3);
        c3.setContato("christiankroth@gmail.com");
        c3.setObservacao("Edit");
        c3.setStatus(1);

        if(!new ContatoDAO().cadastra(c1)){
            System.out.println("Erro ao cadastrar contato 1");
            return false;
        }
        if(!new ContatoDAO().cadastra(c2)){
            System.out.println("Erro ao cadastrar contato 2");
            return false;
        }
        if(!new ContatoDAO().cadastra(c3)){
            System.out.println("Erro ao cadastrar contato 3");
            return false;
        }

        return true;

    }

    public static boolean testeCadastroUsuario(){
        Usuario usuario = new Usuario();
        usuario.setIdTipoPessoa(1);
        usuario.setLiberarAdocao(1);
        usuario.setStatus(1);
        usuario.setNome("Christian Fernando Kroth");
        usuario.setCpf("12345678909");
        usuario.setIdTipoUsuario(1);
        usuario.setLogin("admin");
        usuario.setSenha(Utilidades.criptografaSenha("admin"));
        usuario.setStatusUsuario(1);

        if(!new UsuarioDAO().cadastra(usuario)){
            System.out.println("Erro ao cadastrar usuário");
            return false;
        }

        TipoContato t1 = new TipoContato();
        Contato c1 = new Contato();
        t1.setId(1);
        c1.setIdPessoa(usuario.getId());
        c1.setTipoContato(t1);
        c1.setContato("(51)3714-1234");
        c1.setStatus(1);

        TipoContato t2 = new TipoContato();
        Contato c2 = new Contato();
        t2.setId(2);
        c2.setIdPessoa(usuario.getId());
        c2.setTipoContato(t2);
        c2.setContato("(51)99748-6229");
        c2.setStatus(1);

        TipoContato t3 = new TipoContato();
        Contato c3 = new Contato();
        t3.setId(3);
        c3.setIdPessoa(usuario.getId());
        c3.setTipoContato(t3);
        c3.setContato("christiankroth@gmail.com");
        c3.setStatus(1);

        if(!new ContatoDAO().cadastra(c1)){
            System.out.println("Erro ao cadastrar contato 1");
            return false;
        }
        if(!new ContatoDAO().cadastra(c2)){
            System.out.println("Erro ao cadastrar contato 2");
            return false;
        }
        if(!new ContatoDAO().cadastra(c3)){
            System.out.println("Erro ao cadastrar contato 3");
            return false;
        }

        Cidade cidade = new Cidade();
        cidade.setId(1);
        //cidade.setDescricao("LAJEADO");
        //cidade.setUf("RS");
        //cidade.setEstado("RIO GRANDE DO SUL");

        Endereco endereco = new Endereco();
        endereco.setCidade(cidade);
        endereco.setLogradouro("Rua Aloysio Lenz");
        endereco.setNumero("581");
        endereco.setComplemento("");
        endereco.setBairro("Floresta");
        endereco.setCep("95902540");

        if(!new EnderecoDAO().cadastra(endereco)){
            System.out.println("Erro ao cadastrar endereço");
            return false;
        }

        if(!new EnderecoDAO().cadastraPessoaEndereco(usuario.getId(),endereco.getId())){
            System.out.println("Erro ao vincular endereço com o usuário");
            return false;
        }

        Gson gson = new Gson();
        String json = gson.toJson(usuario);

        System.out.println(json);

        System.out.println("Usuário criado com sucesso!");
        return true;
    }
}
