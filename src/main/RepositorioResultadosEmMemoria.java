package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Repositório de resultados que guarda tudo em memória.
 **/
public class RepositorioResultadosEmMemoria implements RepositorioResultados {
  private final List<ResultadoBatalha> resultados = new ArrayList<>();

  @Override
  public void salvar(ResultadoBatalha resultado) {
    resultados.add(resultado);
  }

  @Override
  public List<ResultadoBatalha> listar() {
    return Collections.unmodifiableList(new ArrayList<>(resultados));
  }
}
