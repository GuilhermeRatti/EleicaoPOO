import java.time.LocalDate;

public class CandidatoNominal extends Candidato {
    public CandidatoNominal(String nomeUrna,
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
        this.adicionaVotos(votos);
        this.getPartido().registraVotosNominais(votos);
    }
}
