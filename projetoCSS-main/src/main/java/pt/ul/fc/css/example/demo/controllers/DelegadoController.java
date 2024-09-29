package pt.ul.fc.css.example.demo.controllers;

import java.util.List;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import pt.ul.fc.css.example.demo.entities.Delegado;
import pt.ul.fc.css.example.demo.repositories.DelegadoRepository;

@RestController
@RequestMapping("/api")
public class DelegadoController {
  private final DelegadoRepository delegadoRepository;

  public DelegadoController(DelegadoRepository delegadoRepository) {
    this.delegadoRepository = delegadoRepository;
  }

  @GetMapping("/delegado/{id}")
  public Delegado getDelegado(@PathVariable Integer id) {
    return this.delegadoRepository.getDelegadoById(id);
  }

  @GetMapping("/delegados")
  public List<Delegado> getDelegados() {
    return this.delegadoRepository.allDelegados();
  }

  @PutMapping("/delegado")
  public Integer putDelegado(@NonNull @RequestBody Delegado delegado) {
    Delegado saved = delegadoRepository.save(delegado);
    return saved.getId();
  }

  @PostMapping("/delegado")
  public Integer postDelegado(@RequestBody String nome) {
    Delegado novoDelegado = new Delegado(nome);
    Delegado saved = delegadoRepository.save(novoDelegado);
    return saved.getId();
  }

  @GetMapping("/delegadoExiste/{id}")
  public Boolean existeDelegadoById(@PathVariable Integer id) {
    return this.delegadoRepository.existsById(id);
  }
}
