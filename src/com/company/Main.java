package com.company;

public class Main {

    public static void main(String[] args) {
        EmailMessage wiadomosc = EmailMessage.builder()
                .addFrom("sbobek@agh.edu.pl")
                .addTo("student@agh.edu.pl")
                .addSubject("Mail testowy")
                .addContent("Brak tresci")
                .build();
        wiadomosc.send();
    }
}
