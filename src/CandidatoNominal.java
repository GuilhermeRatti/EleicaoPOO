import java.time.LocalDate;

public class CandidatoNominal extends Candidato {
    public CandidatoNominal(String nomeUrna,
            int numCandidato,
            int cargo,
            int numFederacao,
            LocalDate dataNascimento,
            int genero, int cdEleito,
            int sitCand,
            Partido partido) {
        super(nomeUrna, numCandidato, cargo, numFederacao, dataNascimento, genero, cdEleito, sitCand, partido);
    }

    @Override
    public void registraVotos(int votos) {
        this.adicionaVotos(votos);
        this.getPartido().registraVotosNominais(votos);
    }
}
