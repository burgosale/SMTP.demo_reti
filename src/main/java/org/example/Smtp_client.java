package org.example;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.sql.DataSource;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

@SuppressWarnings("all")
public class Smtp_client {

    public static void main(String[] args) throws IOException, MessagingException {
        //client setup and debug pring
        Smtp_client cli = new Smtp_client(args[0],InetAddress.getByName(args[1]),args[2],args[3]);
        System.out.println("cli up : dst: "+args[0] + " src : provaemailSRC@gmail.com\n");
        String msg_path = "/home/burgos/IdeaProjects/SMTP.demo_reti/src/main/resources/msg.html";
        String html_content = readHTMLFromFile(msg_path);

        try{
            Process p = Runtime.getRuntime().exec(msg_path);
        }catch(IOException e){
            System.out.println();
        }
        //setting persistance proprietis
        Properties properties = System.getProperties();
        properties.setProperty("fake-SMTP",args[0]);
        //setting smtp session
        Session sess = Session.getDefaultInstance(properties);
        // Create a default MimeMessage object.
        MimeMessage message = new MimeMessage(sess);
        // Set From: header field of the header.
        try {
            message.setFrom(new InternetAddress(args[2]));
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        // Set To: header field of the header.
        try {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(args[0]));
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        // Set Subject: header field
        message.setSubject(args[3]);
        message.setContent(html_content,"text/html");
        System.out.println(message.toString());
        // Now set the actual message
        Transport.send(message);
        /*
        2nd task starting
         */
        // Create a multipart message for attachment
        BodyPart bodyPart = new MimeBodyPart();
        bodyPart.setText(html_content);
        Multipart multipart = new MimeMultipart();
        // Set text message part
        multipart.addBodyPart(bodyPart);
        // Second part is attachment
        bodyPart = new MimeBodyPart();
        String filename = "abc.txt";
        /*
        DataSource source = new FileDataSource(filename);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(filename);
        multipart.addBodyPart(messageBodyPart);
         */
    }
    //fields
    private String dst_addr;
    private InetAddress dst_ip;
    private String src_addr;
    private String subject;
    private final int DEFAULT_PORT=25;

    public Smtp_client(String dst_addr,InetAddress dst_ip,String src_addr,String subject){
        this.dst_addr=dst_addr;
        this.dst_ip=dst_ip;
        this.src_addr = src_addr;
        this.subject = subject;
    }
    //gettes
    private String getSubject(){
        return subject;
    }

    private static String readHTMLFromFile(String path)throws IOException{
        StringBuilder contentBuilder= new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        while((line = br.readLine())!=null){
            contentBuilder.append(line).append("\n");
        }
        return contentBuilder.toString();
    }
    /*
    private Message prepareMsg(Properties prop){
        Message msg = new MimeMessage();
    }

     **/
}
