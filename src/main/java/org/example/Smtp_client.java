package org.example;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.util.Properties;

@SuppressWarnings("all")
public class Smtp_client {

    //fields
    private String dst_addr;
    private InternetAddress host;
    private String src_addr;
    private String subject;
    private final int DEFAULT_PORT=25;

    protected Smtp_client(){

    };
    public Smtp_client(String dst_addr,InternetAddress dst_ip,String src_addr,String subject){
        this.dst_addr=dst_addr;
        this.host=dst_ip;
        this.src_addr = src_addr;
        this.subject = subject;
    }

    public static void main(String[] args) throws IOException, MessagingException {
        Smtp_client cli = new Smtp_client();
        for(int i=0;i<args.length;i++){
            switch(args[i]){
                case "-obj":
                    cli.setObj(args[i+1]);
                    break;
                case "-h":
                    cli.setHost(args[i+1]);
                    break;
                case "-src":
                    cli.setSrc_addr(args[i+1]);
                    break;
                case "-dst":
                    cli.setDst_addr(args[i+1]);
                    break;
            }
        }
        //client setup and debug pring
        String msg_path = "msg.html";
        String html_content = readHTMLFromFile(msg_path);

        //setting persistance proprietis
        Properties properties = System.getProperties();
        properties.setProperty("fake-SMTP","localhost");
        Message msg = cli.prepareMsg(properties,html_content);
        // Now send the actual message
        Transport.send(msg);
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

    private Message prepareMsg(Properties prop,String html_content){
        Message msg = new MimeMessage(Session.getInstance(prop));
        try {//set dst address of msg
            msg.setFrom(new InternetAddress(this.get_scrAddr()));
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        try {//Add this recipient address to the existing ones of the given type,header field
            msg.addRecipient(Message.RecipientType.TO,new InternetAddress(this.get_dstAdrr()));
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        try {//add subject field
            msg.setSubject(this.getSubject());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        try {//content of email plus setting content header field
            msg.setContent(html_content,"text/html");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return msg;
    }

    //gettes & setter
    private String getSubject(){
        return this.subject;
    }
    private InternetAddress getAddress() {
        return this.host;
    }
    private String get_dstAdrr(){
        return this.dst_addr;
    }
    private String get_scrAddr(){
        return this.src_addr;
    }
    private void setObj(String obj){
        this.subject = obj;
    }
    private void setHost(String dst_addr){
        try {
            this.host = new InternetAddress(dst_addr);
        } catch (AddressException e) {
            throw new RuntimeException(e);
        }
    }
    private void setSrc_addr(String src_addr){
        this.src_addr=src_addr;
    }
    private void setDst_addr(String dst_addr){
        this.dst_addr=dst_addr;
    }
}

