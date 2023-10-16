import java.util.Date;

public class Candidato {
    private String nomeUrna;
    private int numCandidato;
    private int cargo;
    private int numFederacao;
    private Date dataNascimento;
    private int genero;
    private Partido partido;

    public Candidato(String nomeUrna, int numCandidato, int cargo, int numFederacao, Date dataNascimento, int genero, Partido partido) {
        this.nomeUrna = nomeUrna;
        this.numCandidato = numCandidato;
        this.cargo = cargo;
        this.numFederacao = numFederacao;
        this.dataNascimento = dataNascimento;
        this.genero = genero;
        this.partido = partido;
    }
}
