package br.com.med.voll.api.utils;

import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@NoArgsConstructor
public class Constants {

    public static final String ERROR_SAVE_MEDICO = "OCORREU UM ERRO AO TENTAR SALVAR MÉDICO!";
    public static final String ERROR_SAVE_PACIENTE = "OCORREU UM ERRO AO TENTAR SALVAR PACIENTE!";

    public static final DateTimeFormatter DATE_PATTERN = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter HOUR_PATTERN = DateTimeFormatter.ofPattern("HH:mm");

}
