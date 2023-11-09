import java.time.LocalDate;

public abstract class Candidato {
    private String nomeUrna;
    private int qtdVotos;
    private int numCandidato;
    private int numFederacao;
    private LocalDate dataNascimento;
    private int genero;
    private int cdEleito;
    private boolean elegivel;
    private Partido partido;

    public Candidato(String nomeUrna,
            int numCandidato,
            int numFederacao,
            LocalDate dataNascimento,
            int genero,
            int cdEleito,
            int sitCand,
            Partido partido) {

        this.nomeUrna = nomeUrna;
        this.numCandidato = numCandidato;
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

    public int getNumCandidato() {
        return this.numCandidato;
    }

    public String getNomeUrna() {
        return this.nomeUrna;
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

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public int getGenero() {
        return this.genero;
    }

    public abstract void registraVotos(int votos);

    @Override
    public String toString() {
        String asterisco = "";
        if(this.numFederacao!=-1)  asterisco = "*";

        String flexaoDeVoto = null;
        if(this.qtdVotos > 1) {
            flexaoDeVoto = " votos)";
        } else {
            flexaoDeVoto = " voto)";
        }

        String msg = asterisco + this.nomeUrna + 
                    " (" + this.partido.getSigla() + ", " +
                    String.format("%,d",this.qtdVotos).replace(',', '.') + flexaoDeVoto;
        return msg;
    }
}

class ComparadorDeCandidato implements java.util.Comparator<Candidato> {
    @Override
    public int compare(Candidato arg0, Candidato arg1) {
        return arg1.getQtdVotos() - arg0.getQtdVotos();
    }
}