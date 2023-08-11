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

    public static final String NOT_EXIST_ID_PACIENTE = "O ID DO PACIENTE INFORMADO NÃO EXISTE.";
    public static final String NOT_EXIST_ID_MEDICO = "O ID DO MÉDICO INFORMADO NÃO EXISTE.";
    public static final String REQUIRED_SPECIALITY = "ESPECIALIDADE É OBRIGATÓRIA QUANDO MÉDICO NÃO FOR ESCOLHIDO.";
    public static final String NON_OPERATIONAL_HOURS_ERROR = "CONSULTA FORA DO HORÁRIO DE FUNCIONAMENTO DA CLÍNICA.";
    public static final String MINIMUM_ADVANCE_BOOKING_ERROR = "A CONSULTA DEVE SER AGENDADA COM ANTECEDÊNCIA MÍNIMA DE 30 MINUTOS.";
    public static final String CANNOT_SCHEDULE_WITH_DELETED_DOCTOR = "CONSULTA NÃO PODE SER AGENDADA COM MÉDICO EXCLUÍDO.";
    public static final String DOCTOR_ALREADY_BOOKED_AT_THIS_TIME = "MÉDICO JÁ POSSUI OUTRA CONSULTA AGENDADA NESSE MESMO HORÁRIO.";
    public static final String CANNOT_SCHEDULE_WITH_DELETED_PATIENT = "CONSULTA NÃO PODE SER AGENDADA COM PACIENTE EXCLUÍDO.";
    public static final String PATIENT_ALREADY_HAS_APPOINTMENT_ON_THIS_DAY = "PACIENTE JÁ POSSUI UMA CONSULTA AGENDADA NESSE DIA.";
    public static final String NO_AVAILABLE_DOCTOR_FOR_CHOSEN_DATE = "NÃO EXISTE MÉDICO DISPONÍVEL NESSA DATA.";
    public static final String ERROR_MESSAGE_NOT_FOUND_CONSULTA = "NÃO FOI ENCONTRADO CONSULTA PARA O CPF %s.";


    public static final DateTimeFormatter DATE_PATTERN = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter HOUR_PATTERN = DateTimeFormatter.ofPattern("HH:mm");

}
