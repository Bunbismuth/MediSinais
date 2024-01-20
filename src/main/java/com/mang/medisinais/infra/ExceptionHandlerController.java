package com.mang.medisinais.infra;

import com.mang.medisinais.dto.ExcecaoDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

  @ExceptionHandler(DataIntegrityViolationException.class)
  public String tratarEntradaDuplicada(DataIntegrityViolationException exception) {
    ExcecaoDTO exceptionDTO = new ExcecaoDTO("Usuário já cadastrado",
        HttpStatus.BAD_REQUEST.value());
    return exceptionDTO.mensagem();
  }

  @ExceptionHandler(MediSinaisExcecao.class)
  public String tratarExcecao(MediSinaisExcecao excecao) {
    ExcecaoDTO excecaoDTO = new ExcecaoDTO(excecao.getMessage(),
        HttpStatus.BAD_REQUEST.value());

    return "erro";
  }

  private void enviarErro(MediSinaisExcecao excecao, Model modelMap) {
    modelMap.addAttribute("excecao", excecao);
  }
}