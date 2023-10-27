import java.time.LocalDate;

public class CandidatoLegenda extends Candidato {
    public CandidatoLegenda(String nomeUrna,
            int numCandidato,
            int numFederacao,
            LocalDate dataNascimento,
            int genero, int cdEleito,
            int sitCand,
            Partido partido) {
        super(nomeUrna, numCandidato, numFederacao, dataNascimento, genero, cdEleito, sitCand, partido);
    }

    @Override
    public void registraVotos(int votos) {
        this.getPartido().registraVotosLegenda(votos);
    }
}
