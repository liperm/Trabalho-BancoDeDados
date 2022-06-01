package dao;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Pessoa;

public class DaoPessoa {
    public static boolean insert(Pessoa pessoa) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException{
        Connection con = ConectaBanco.createConnection();
        
        if(con != null){
            
            if(cpfExistente(pessoa.getCpf()) || emailExistente(pessoa.getEmail()) || loginExistente(pessoa.getLogin())){
                con.close();
                System.out.println("Login, Email ou CPF ja existe");
                return false;
            }

            String query = "INSERT INTO pessoas (nome, telefone, cpf, email, ativo, nivelDeAcesso, login, senha) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement st = con.prepareStatement(query);
            
            st.setString(1, pessoa.getNome());
            st.setString(2, pessoa.getTelefone());
            st.setString(3, pessoa.getCpf());
            st.setString(4, pessoa.getEmail());
            st.setInt(5, 1);
            st.setInt(6, pessoa.getNivelDeAcesso());
            st.setString(7, pessoa.getLogin());
            String senhaCriptografada = Criptografia.criptografar(pessoa.getSenha());
            st.setString(8, senhaCriptografada);
            st.execute();
            con.close();
            return true;
        }
        con.close();
        return false;
    }
    
    public static boolean senhaExistente(String senha) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException{
        Connection con = ConectaBanco.createConnection();

        if(con != null){
            String senhaCriptografada = Criptografia.criptografar(senha);
            String query = "SELECT * FROM pessoas WHERE senha = ? AND ativo = 1";
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1, senhaCriptografada);
            ResultSet rs = st.executeQuery();

            if(rs.next()){
                return true;
            }

            return false;
            
        }

        return false;
    } 

    public static boolean cpfExistente(String cpf) throws SQLException{
        Connection con = ConectaBanco.createConnection();

        if(con != null){
            String query = "SELECT * FROM pessoas WHERE cpf = ? AND ativo = 1";
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1, cpf);
            ResultSet rs = st.executeQuery();

            if(rs.next()){
                return true;
            }

            return false;
            
        }

        return false;
    }
    
    public static boolean loginExistente(String login) throws SQLException{
        Connection con = ConectaBanco.createConnection();

        if(con != null){
            String query = "SELECT * FROM pessoas WHERE login = ? AND ativo = 1";
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1, login);
            ResultSet rs = st.executeQuery();

            if(rs.next()){
                return true;
            }

            return false;
            
        }

        return false;
    }
    
    public static boolean emailExistente(String email) throws SQLException{
        Connection con = ConectaBanco.createConnection();

        if(con != null){
            String query = "SELECT * FROM pessoas WHERE email = ? AND ativo = 1";
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1, email);
            ResultSet rs = st.executeQuery();

            if(rs.next()){
                return true;
            }

            return false;
            
        }

        return false;
    }
    
    public static Pessoa getPessoaRecuperacao(String login, String email) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException{
        Connection con = ConectaBanco.createConnection();
        if(con != null){
            String query = "SELECT * FROM pessoas WHERE login = ? AND email = ? AND ativo = 1";

            PreparedStatement st = con.prepareStatement(query);
            st.setString(1, login);
            st.setString(2, email);
            ResultSet rs = st.executeQuery();

            if(rs.next()){
                Pessoa u = new Pessoa();

                u.setId(rs.getInt("id"));
                u.setNome(rs.getString("nome"));
                u.setCpf(rs.getString("cpf"));
                u.setEmail(rs.getString("email"));
                u.setTelefone(rs.getString("telefone"));
                u.setLogin(rs.getString("login"));
                u.setSenha(rs.getString("senha"));
                u.setNivelDeAcesso(rs.getInt("nivelDeAcesso"));
                u.setAtivo(rs.getInt("ativo"));
                
                con.close();
                return u;
            }

            con.close();
            return null;
        }

        con.close();
        return null;
    }
    
    public static Pessoa getPessoa(String login, String senha) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException{
        Connection con = ConectaBanco.createConnection();
        if(con != null){
            String query = "SELECT * FROM pessoas WHERE login = ? AND senha = ? AND ativo = 1";

            PreparedStatement st = con.prepareStatement(query);
            st.setString(1, login);
            senha = Criptografia.criptografar(senha);
            st.setString(2, senha);
            ResultSet rs = st.executeQuery();

            if(rs.next()){
                Pessoa u = new Pessoa();

                u.setId(rs.getInt("id"));
                u.setNome(rs.getString("nome"));
                u.setCpf(rs.getString("cpf"));
                u.setEmail(rs.getString("email"));
                u.setTelefone(rs.getString("telefone"));
                u.setLogin(rs.getString("login"));
                u.setSenha(rs.getString("senha"));
                u.setNivelDeAcesso(rs.getInt("nivelDeAcesso"));
                u.setAtivo(rs.getInt("ativo"));
                
                con.close();
                return u;
            }

            con.close();
            return null;
        }

        con.close();
        return null;
    }
    
    public static boolean update(String cpf, String columnName, String newValue) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException{
        
        Connection con = ConectaBanco.createConnection();

        if(con != null){
            if(columnName == "senha"){
                    newValue = Criptografia.criptografar(newValue);
            }
            if(columnName == "senha" && senhaExistente(newValue) && !newValue.equals(getUsuario(cpf).getSenha())){
                System.out.println("Erro na senha");
                return false;
            }
            else if(columnName == "cpf" && (cpfExistente(newValue) && (!newValue.equals(getUsuario(cpf).getCpf())))){
                
                return false;
            }
            else{
                
                String query = "UPDATE pessoas SET "+columnName+" = ? WHERE cpf = ?";

                PreparedStatement st = con.prepareStatement(query);

                st.setString(1, newValue);
                st.setString(2, cpf);
                
                st.executeUpdate();
        
                con.close();

                return true;
            }
        }
        return false;
    }
    
    public static boolean update(int id, String columnName, String newValue) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException{
        
        Connection con = ConectaBanco.createConnection();

        if(con != null){
            if(columnName == "senha"){
                    newValue = Criptografia.criptografar(newValue);
            }
            if(columnName == "senha" && senhaExistente(newValue) && !newValue.equals(getUsuario(id).getSenha())){
                System.out.println("Erro na senha");
                return false;
            }
            else if(columnName == "cpf" && (cpfExistente(newValue) && (!newValue.equals(getUsuario(id).getCpf())))){
                
                return false;
            }
            else{
                
                String query = "UPDATE pessoas SET "+columnName+" = ? WHERE id = ?";

                PreparedStatement st = con.prepareStatement(query);

                st.setString(1, newValue);
                st.setInt(2, id);
                
                st.executeUpdate();
        
                con.close();

                return true;
            }
        }
        return false;
    }
    
    public static boolean updateNivelDeAcesso(int id, String columnName, int newValue) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException{
        
        Connection con = ConectaBanco.createConnection();

        if(con != null){
                
            String query = "UPDATE pessoas SET nivelDeAcesso = ? WHERE id = ?";

            PreparedStatement st = con.prepareStatement(query);

            st.setInt(1, newValue);
            st.setInt(2, id);
                
            st.executeUpdate();
        
            con.close();

            return true;
        }
        return false;
    }
    
    public static Pessoa getUsuario(String cpf) throws SQLException{
        Connection con = ConectaBanco.createConnection();

        if(con != null){
            String query = "SELECT * FROM pessoas WHERE ativo = 1 AND cpf = '" +cpf+"'";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            rs.next();
            Pessoa user = new Pessoa(rs.getInt("ativo"), rs.getInt("nivelDeAcesso"), rs.getString("nome"), rs.getString("email"), rs.getString("telefone"), rs.getString("cpf"), rs.getString("login"), rs.getString("Senha"));
            user.setId(rs.getInt("id"));

            con.close();
            return user;
        }

        con.close();
        return null;
        
    }
    
    public static Pessoa getUsuario(int id) throws SQLException{
        Connection con = ConectaBanco.createConnection();

        if(con != null){
            String query = "SELECT * FROM pessoas WHERE ativo = 1 AND id = " +id+"";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            rs.next();
            Pessoa user = new Pessoa(rs.getInt("ativo"), rs.getInt("nivelDeAcesso"), rs.getString("nome"), rs.getString("email"), rs.getString("telefone"), rs.getString("cpf"), rs.getString("login"), rs.getString("Senha"));
            user.setId(rs.getInt("id"));

            con.close();
            return user;
        }

        con.close();
        return null;
        
    }
    
    public static List<Pessoa> getPessoas() throws SQLException{
        ArrayList<Pessoa> lista = new ArrayList();

        Connection con = ConectaBanco.createConnection();
        String query = "SELECT * FROM pessoas WHERE ativo = 1";

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);

        while(rs.next()){
            Pessoa pessoa = new Pessoa();

            pessoa.setNome(rs.getString("nome"));
            pessoa.setCpf(rs.getString("cpf"));
            pessoa.setEmail(rs.getString("email"));
            pessoa.setTelefone(rs.getString("telefone"));
            pessoa.setId(rs.getInt("id"));
            pessoa.setAtivo(rs.getInt("ativo"));
            pessoa.setNivelDeAcesso(rs.getInt("nivelDeAcesso"));
            pessoa.setLogin(rs.getString("login"));
            pessoa.setSenha(rs.getString("senha"));

            lista.add(pessoa);
        }
        
        con.close();
        return lista;
    }
    
    public static boolean delete(String cpf) throws SQLException{
        String query = "UPDATE pessoas SET ativo = 0 WHERE cpf = '" + cpf + "'";
        
        Connection con = ConectaBanco.createConnection();

        if(con != null){
            Statement st = con.createStatement();
            
            st.execute(query);
            con.close();
            return true;
        }

        return false;     
    }
}
