package recuperadorDeSenha;

import dao.DaoPessoa;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import javax.mail.*;
import javax.mail.internet.*;

public class RecuperadorDeSenha {
    private static String[] charSet ={"0","1","2","3","4","5","6","7","8","9","a","b",
        "c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t",
        "u","v","w","x","y","z","A","B","C","D","E","F","G","H","I","J","K","L",
        "M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    
    public static void recuperaSenha(int id) throws NoSuchAlgorithmException, UnsupportedEncodingException, SQLException{
        Random gerador = new Random();
        int tamanho = gerador.nextInt(26) + 6;
        String novaSenha = "";
        
        do{
            for(int i = 0; i < tamanho; i++){
                novaSenha = novaSenha + charSet[gerador.nextInt(charSet.length)];
            }
        }while(DaoPessoa.senhaExistente(novaSenha));
        
        if(DaoPessoa.update(id, "senha", novaSenha)){
            RecuperadorDeSenha.enviaEmail(novaSenha);
        }
        else{
            System.out.println("Erro ao recuperar senha");
        }
    }
    
    private static void enviaEmail(String novaSenha){
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
        "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        
        Session session = Session.getDefaultInstance(props,
      new javax.mail.Authenticator() {
           protected PasswordAuthentication getPasswordAuthentication()
           {
                 return new PasswordAuthentication("email@gmail.com",
                 "SENHA");//Colocar o email e senha do email remetente 
           }
      });
        
    session.setDebug(true);

    try {

      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress("email@gmail.com"));
      //Remetente
      
      //Destinatário
      Address[] toUser = InternetAddress.parse("feliperibeiromachado1401@gmail.com");

      message.setRecipients(Message.RecipientType.TO, toUser);
      message.setSubject("Recuperação de Senha");//Assunto
      message.setText("Olá. Sua nova senha para acesso é " + novaSenha + ".");
      /**Método para enviar a mensagem criada*/
      Transport.send(message);

      System.out.println("Feito!!!");

     } catch (MessagingException e) {
        throw new RuntimeException(e);
    }

    }
}
