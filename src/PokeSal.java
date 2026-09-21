import java.util.ArrayList;
import java.util.List;

/**
 *Classe PokeSal.
 **/
public class PokeSal {

  private String nome;
  private float hpMaximo;
  private float hpAtual;
  private float atk;
  private float def;
  private float spd;
  private TipoElemental tipoElemental;
  private List<StatusEffect> status;
  private List<Golpe> golpes;
  private boolean exaustoTurnoAtual; // apenas para o ataque tsunami

  /**
   * Método construtor.
   *
   *@param nome é o nome do pokesal.
   *@param hpMaximo define o hp.
   *@param atk define o ataque.
   *@param def define a defesa.
   *@param spd define a velocidade.
   *@param tipoElemental define o tipo elemental do pokesal.
   **/
  public PokeSal(String nome, float hpMaximo, float atk,
      float def, float spd, TipoElemental tipoElemental) {
    this.nome = nome;
    this.hpMaximo = hpMaximo;
    this.hpAtual = hpMaximo;
    this.atk = atk;
    this.def = def;
    this.spd = spd;
    this.tipoElemental = tipoElemental;
    this.status = new ArrayList<StatusEffect>();
    this.golpes = new ArrayList<Golpe>();
    this.exaustoTurnoAtual = false;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public boolean isExaustoTurnoAtual() {
    return exaustoTurnoAtual;
  }

  public void setExaustoTurnoAtual(boolean exaustoTurnoAtual) {
    this.exaustoTurnoAtual = exaustoTurnoAtual;
  }

  public List<Golpe> getGolpes() {
    return golpes;
  }

  /**
   * Método para adicionar o golpe.
   *
   * @param golpe adiciona golpe.
   */
  public void adicionarGolpe(Golpe golpe) {
    golpes.add(golpe);
  }

  public List<StatusEffect> getStatus() {
    return status;
  }

  public void setStatus(List<StatusEffect> status) {
    this.status = status;
  }

  public TipoElemental getTipo() {
    return tipoElemental;
  }

  public void setTipo(TipoElemental tipoElemental) {
    this.tipoElemental = tipoElemental;
  }

  public float getDef() {
    return def;
  }

  public void setDef(float def) {
    this.def = def;
  }

  public float getSpd() {
    return spd;
  }

  public void setSpd(float spd) {
    this.spd = spd;
  }

  public float getAtk() {
    return atk;
  }

  public void setAtk(float atk) {
    this.atk = atk;
  }

  public float getHpAtual() {
    return hpAtual;
  }

  public void setHpAtual(float hpAtual) {
    this.hpAtual = hpAtual;
  }

  public float getHpMaximo() {
    return hpMaximo;
  }

  public void setHpMaximo(float hpMaximo) {
    this.hpMaximo = hpMaximo;
  }

  /**
   * Ataca outro Pokesal usando um golpe específico.
   **/
  public float atacar(PokeSal alvo, Golpe golpe, Terreno terreno) {
    if (!isAptoAtacar()) {
      System.out.println(nome + " está exausto e não pode atacar nesta rodada!");
      setExaustoTurnoAtual(false);
      return 0;
    }
    return golpe.executar(this, alvo, terreno);
  }

  /**
   * Recebe dano e não deixa o HP ficar negativo.
   **/
  public void receberDano(float dano) {
    hpAtual = hpAtual - dano;
    if (hpAtual < 0) {
      hpAtual = 0;
    }
  }

  /**
   * Cura e não deixa passar do HP máximo.
   **/
  public void curar(float valor) {
    hpAtual = hpAtual + valor;
    if (hpAtual > hpMaximo) {
      hpAtual = hpMaximo;
    }
  }

  /**
   * Adiciona um status (queimado, envenenado, paralisado).
   **/
  public void aplicarStatus(StatusEffect novoStatus) {
    if (!status.contains(novoStatus)) {
      status.add(novoStatus);
      System.out.println(nome + " agora está com o status: " + novoStatus);
    }
  }

  /**
   * Limpa o status.
   **/
  public void limparStatus() {
    status.clear();
  }

  /**
   * Verifica se o pokesal está vivo.
   **/
  public boolean estaVivo() {
    return hpAtual > 0;
  }

  /**
   * Verifica se o pokesal esta apto a atacar.
   **/
  public boolean isAptoAtacar() {
    return !exaustoTurnoAtual && estaVivo();
  }
}