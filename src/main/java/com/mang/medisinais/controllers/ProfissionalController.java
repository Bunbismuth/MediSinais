package com.mang.medisinais.controllers;

import com.mang.medisinais.domain.Profissional;
import com.mang.medisinais.dto.FiltroDTO;
import com.mang.medisinais.dto.LoginDTO;
import com.mang.medisinais.dto.ProfissionalDTO;
import com.mang.medisinais.dto.ResultadoDTO;
import com.mang.medisinais.infra.MediSinaisExcecao;
import com.mang.medisinais.services.ProfissionalService;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class ProfissionalController {

  private final ProfissionalService profissionalService;

  @GetMapping("/")
  public String exibirIndex() {
    return "index";
  }

  @PostMapping("/cadastro")
  public String cadastrarProfissional(@RequestBody ProfissionalDTO dadosProfissional, ModelMap model) {
    ProfissionalDTO novoProfissional = profissionalService.criarProfissional(dadosProfissional);

    model.addAttribute("cadastrado", novoProfissional);

    return "paginaCadastrado";
  }

  @GetMapping("/login")
  public String exibirPaginaLogin() {
    return "login";
  }

  @PostMapping("/login")
  public String logarProfissional(@RequestBody LoginDTO login,HttpSession sessao) throws MediSinaisExcecao {
    Profissional profissionalLogado = profissionalService.autenticarProfissional(login);

    sessao.setAttribute("dadosProfissional", profissionalLogado);

    return "redirect:/home";
  }

  @GetMapping("/home")
  public String exibirHomeProfissional(Model model, HttpSession sessao) {
    Profissional profissional = (Profissional) sessao.getAttribute("dadosProfissional");

    model.addAttribute("profissional", profissional);

    return "paginaInicial";
  }

  @GetMapping("/pesquisa")
  public String exibirOesquisaProfissionais(FiltroDTO filtroDTO, ModelMap model) {
    List<Profissional> profissionais = profissionalService.pesquisaProfissionais(filtroDTO);

    model.addAttribute("profissionais", profissionais);

    return "listaProfissionais";
  }

  @GetMapping("/profissional/{slug}")
  public String exibirPaginaProfissional(@PathVariable String slug, ModelMap model) throws MediSinaisExcecao {
    ResultadoDTO profissional = profissionalService.encontrarPorSlug(slug);

    model.addAttribute("profissional", profissional);

    return "paginaProfissional";
  }

  @GetMapping("/logout")
  public void realizarLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
    HttpSession sessao = request.getSession(false);

    if(sessao != null) {
      sessao.invalidate();
      response.sendRedirect("/");
    }
  }

}
