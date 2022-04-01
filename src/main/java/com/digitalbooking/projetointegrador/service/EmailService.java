package com.digitalbooking.projetointegrador.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Async
    public void enviarEmail(SimpleMailMessage messagem) {
        try {
            javaMailSender.send(messagem);
            System.out.println("Email enviado com sucesso");
        } catch (MailException e) {
            e.printStackTrace();
        }
    }

    public SimpleMailMessage gerarEmailDeValidacao(String token, String destinatario) {

        SimpleMailMessage mensagem = new SimpleMailMessage();

        mensagem.setFrom("pigrupo03t2@gmail.com");
        mensagem.setTo(destinatario);
        mensagem.setSubject("Email de validação de registro - Digital Booking");
        mensagem.setText("Acesse o link e valide o seu registro: " + token);
        return mensagem;
    }

}
