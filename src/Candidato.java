import java.time.LocalDate;

public class Candidato {
    private String nomeUrna;
    private int numCandidato;
    private int cargo;
    private int numFederacao;
    private LocalDate dataNascimento;
    private int genero;
    private int cdEleito;
    private boolean elegivel;
    private Partido partido;

    public Candidato(String nomeUrna, int numCandidato, int cargo, int numFederacao, LocalDate dataNascimento, int genero, int cdEleito, int sitCand, Partido partido) {
        this.nomeUrna = nomeUrna;
        this.numCandidato = numCandidato;
        this.cargo = cargo;
        this.numFederacao = numFederacao;
        this.dataNascimento = dataNascimento;
        this.genero = genero;
        this.cdEleito = cdEleito;
        // Candidatos nao elegiveis ainda tem seus votos contabilizados para a legenda
        if(sitCand == 2 || sitCand == 16) {
            this.elegivel = true;
        }
        else {
            this.elegivel = false;
        }

        this.partido = partido;
    }

    @Override
    public String toString() {
        String msg = this.nomeUrna + " - " + partido.toString();
        
        return msg;
    }
}
