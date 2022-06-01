package controller;
import dao.DaoPessoa;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;
import model.Pessoa;
import recuperadorDeSenha.RecuperadorDeSenha;
import view.Login;

public class ControllerPessoa {
    public static void inicio(){
        Login telaLogin = new Login();
        telaLogin.setVisible(true);
    }
    
    public static Pessoa login(String login, String senha) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException{
        Pessoa pessoa = DaoPessoa.getPessoa(login, senha);
        
        if(pessoa != null){
            return pessoa;
        }
        
        return null;
    }
    
    public static boolean cadastro(Pessoa pessoa) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException{
        if(pessoa.getSenha().isEmpty() || pessoa.getLogin().isEmpty()){
            return false;
        }
        
        else if(DaoPessoa.insert(pessoa)){
            return true;
        }
        
        return false;
    }
    
    public static boolean atualiza(Pessoa pessoa, String nome, String cpf, String telefone, String email, String senha, int acesso) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException{
        int id = pessoa.getId();
        if(DaoPessoa.update(id, "nome", nome)
                && DaoPessoa.update(id, "cpf", cpf)
                && DaoPessoa.update(id, "telefone", telefone)
                && DaoPessoa.update(id, "email", email)
                && DaoPessoa.updateNivelDeAcesso(id, "nivelDeAcesso", acesso)){
            if(!senha.isEmpty()){
                if(DaoPessoa.update(id, "senha", senha)){
                    return true;
                }
                else{
                    return false;
                }
            }
            return true;
        }
        else{
            return false;
        }
    }
    
    public static boolean recuperarSenha(int id) throws NoSuchAlgorithmException, UnsupportedEncodingException, SQLException{
         RecuperadorDeSenha.recuperaSenha(id);
      
         return true;
    }
    
    public static Pessoa recuperarPessoa(String login, String email) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException{
        Pessoa pessoa = DaoPessoa.getPessoaRecuperacao(login, email);
        
        return pessoa;
    }
    
    public static List<Pessoa> getPessoas() throws SQLException{
        List<Pessoa> listaPessoas = DaoPessoa.getPessoas();
        return listaPessoas;
    }
    
    public static Pessoa getPessoa(String cpf) throws SQLException{
        return DaoPessoa.getUsuario(cpf);
    }
    
    public static void deletaPessoa(String cpf) throws SQLException{
        DaoPessoa.delete(cpf);
    }
}

