package com.digitalbooking.projetointegrador.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public boolean enviarEmail(SimpleMailMessage messagem) {
        try {
            javaMailSender.send(messagem);
            System.out.println("Email enviado com sucesso");
        } catch (MailException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public SimpleMailMessage gerarEmailDeValidacao(String token, String destinatario) {

        SimpleMailMessage mensagem = new SimpleMailMessage();

        mensagem.setFrom("pigrupo03t2@gmail.com");
        mensagem.setTo(destinatario);
        mensagem.setSubject("Email de validação de registro - Digital Booking");
        mensagem.setText("Clique aqui para validar seu registro: " + token);
        return mensagem;
    }

}
