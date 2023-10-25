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

    public Partido getPartido() {
        return this.partido;
    }

    public void adicionaVotos(int votos) {
        this.qtdVotos += votos;
    }

    public abstract void registraVotos(int votos);

    @Override
    public String toString() {
        String msg = this.nomeUrna + " - " + partido.toString() + " - VOTOS: " + this.qtdVotos;

        return msg;
    }
}
