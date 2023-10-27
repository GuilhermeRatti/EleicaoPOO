import java.util.HashMap;
import java.util.Map;

public class Partido {
    private Map<Integer, Candidato> candidatos = new HashMap<Integer, Candidato>();
    private String sigla;
    private int numPartido;
    private int quantCand;
    private int qtdVotosNominais;
    private int qtdVotosLegenda;

    public Partido(String sigla, int numPartido) {
        this.sigla = sigla;
        this.numPartido = numPartido;
    }

    public int getQtdTotalDeVotos() {
        return this.qtdVotosLegenda + this.qtdVotosNominais;
    }

    public String getSigla() {
        return this.sigla;
    }

    public void addCandidato(Candidato cand, int numCandidato) {
        this.candidatos.put(numCandidato, cand);
        this.quantCand++;
    }

    public void registraVotosLegenda(int votos) {
        this.qtdVotosLegenda += votos;
    }

    public void registraVotosNominais(int votos) {
        this.qtdVotosNominais += votos;
    }

    public int getQtdCandEleitos() {
        int qtd = 0;

        for (Candidato c : this.candidatos.values()) {
            if (c.verificaEleito())
                qtd++;
        }

        return qtd;
    }

    @Override
    public String toString() {
        String msg = this.sigla + " - " + this.numPartido + ", " +
                (this.qtdVotosLegenda + this.qtdVotosNominais) + " votos " +
                "(" + this.qtdVotosNominais + " nominais e " + this.qtdVotosLegenda + " de legenda), " +
                this.getQtdCandEleitos() + " candidatos eleitos";

        return msg;
    }
}
