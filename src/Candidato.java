import java.time.LocalDate;

public abstract class Candidato {
    private String nomeUrna;
    private int qtdVotos;
    private int numCandidato;
    private int cargo;
    private int numFederacao;
    private LocalDate dataNascimento;
    private int genero;
    private int cdEleito;
    private boolean elegivel;
    private Partido partido;

    public Candidato(String nomeUrna,
            int numCandidato,
            int cargo,
            int numFederacao,
            LocalDate dataNascimento,
            int genero,
            int cdEleito,
            int sitCand,
            Partido partido) {

        this.nomeUrna = nomeUrna;
        this.numCandidato = numCandidato;
        this.cargo = cargo;
        this.numFederacao = numFederacao;
        this.dataNascimento = dataNascimento;
        this.genero = genero;
        this.cdEleito = cdEleito;
        // Candidatos nao elegiveis ainda tem seus votos contabilizados para a legenda
        if (sitCand == 2 || sitCand == 16) {
            this.elegivel = true;
        } else {
            this.elegivel = false;
        }

        this.partido = partido;
    }

    public boolean verificaElegibilidade() {
        return this.elegivel;
    }

    public int getQtdVotos() {
        return this.qtdVotos;
    }

    public Partido getPartido() {
        return this.partido;
    }

    public void adicionaVotos(int votos) {
        this.qtdVotos += votos;
    }

    public boolean verificaEleito() {
        if (this.cdEleito == 2 || this.cdEleito == 3) {
            return true;
        } else {
            return false;
        }
    }

    public abstract void registraVotos(int votos);

    @Override
    public String toString() {
        String msg = this.nomeUrna + 
                    "(" + this.partido.getSigla() + ", " +
                    String.format("%,d",this.qtdVotos).replace(',', '.') + " votos)";
        return msg;
    }
}

class ComparadorDeCandidato implements java.util.Comparator<Candidato> {
    @Override
    public int compare(Candidato arg0, Candidato arg1) {
        return arg0.getQtdVotos() - arg1.getQtdVotos();
    }
}