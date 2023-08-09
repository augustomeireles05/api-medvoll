package br.com.med.voll.api.utils;

import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@NoArgsConstructor
public class Constants {

    public static final String ERROR_SAVE_MEDICO = "OCORREU UM ERRO AO TENTAR SALVAR MÉDICO!";
    public static final String ERROR_MESSAGE_DUPLICATE_EMAIL = "O EMAIL INFORMADO JÁ ESTÁ EM USO!";
    public static final String ERROR_MESSAGE_DUPLICATE_CRM = "O CRM INFORMADO JÁ ESTÁ EM USO!";
    public static final String ERROR_MESSAGE_NOT_FOUND_MEDICO = "NÃO FOI ENCONTRADO MÉDICO PARA O ID %d";
    public static final String ERROR_SAVE_PACIENTE = "OCORREU UM ERRO AO TENTAR SALVAR PACIENTE!";
    public static final String ERROR_MESSAGE_DUPLICATE_CPF = "O CPF INFORMADO JÁ ESTÁ EM USO!";
    public static final String ERROR_MESSAGE_NOT_FOUND_PACIENTE = "NÃO FOI ENCONTRADO PACIENTE PARA O ID %d";
    public static final String WITH_ISSUER = "API Voll.med";
    public static final String ERROR_GET_TOKEN_INVALID_OR_EXPIRED = "TOKEN JWT INVÁLIDO OU EXPIRADO!";
    public static final String ERROR_GENERATE_TOKEN = "ERRO AO GERAR TOKEN JWT!";

    public static final String AUTHORIZATION_HEADER = "Authorization";

    public static final DateTimeFormatter DATE_PATTERN = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter HOUR_PATTERN = DateTimeFormatter.ofPattern("HH:mm");

}
