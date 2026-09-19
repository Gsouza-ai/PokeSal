import java.util.ArrayList;
import java.util.List;

/**
 *Classe PokeSal.
 **/
public class PokeSal {

  private String nome;
  private double hpMaximo;
  private double hpAtual;
  private double atk;
  private double def;
  private double spd;
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
  public PokeSal(String nome, double hpMaximo, double atk,
      double def, double spd, TipoElemental tipoElemental) {
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

  public double getDef() {
    return def;
  }

  public void setDef(double def) {
    this.def = def;
  }

  public double getSpd() {
    return spd;
  }

  public void setSpd(double spd) {
    this.spd = spd;
  }

  public double getAtk() {
    return atk;
  }

  public void setAtk(double atk) {
    this.atk = atk;
  }

  public double getHpAtual() {
    return hpAtual;
  }

  public void setHpAtual(double hpAtual) {
    this.hpAtual = hpAtual;
  }

  public double getHpMaximo() {
    return hpMaximo;
  }

  public void setHpMaximo(double hpMaximo) {
    this.hpMaximo = hpMaximo;
  }

  /**
   * Ataca outro Pokesal usando um golpe específico.
   **/
  public double atacar(PokeSal alvo, Golpe golpe, Terreno terreno) {
    if (!isAptoAtacar()) {
      System.out.println(nome + " está exausto e não pode atacar nesta rodada!");
      return 0;
    }
    return golpe.executar(this, alvo, terreno);
  }

  /**
   * Recebe dano e não deixa o HP ficar negativo.
   **/
  public void receberDano(double dano) {
    hpAtual = hpAtual - dano;
    if (hpAtual < 0) {
      hpAtual = 0;
    }
  }

  /**
   * Cura e não deixa passar do HP máximo.
   **/
  public void curar(double valor) {
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
