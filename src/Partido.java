import java.util.ArrayList;
import java.util.List;

public class Partido {
    private List<Candidato> candidatos = new ArrayList<Candidato>();
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

    public void addCandidato(Candidato cand) {
        this.candidatos.add(cand);
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

        for (Candidato c : this.candidatos) {
            if (c.verificaEleito())
                qtd++;
        }

        return qtd;
    }

    @Override
    public String toString() {
        String msg = this.sigla + " - " + this.numPartido + ", " +
                String.format("%,d", this.qtdVotosLegenda + this.qtdVotosNominais).replace(',', '.') + " votos " +
                "(" + String.format("%,d", this.qtdVotosNominais).replace(',', '.') + " nominais e " + String.format("%,d", this.qtdVotosLegenda).replace(',', '.') + " de legenda), " +
                this.getQtdCandEleitos() + " candidatos eleitos";

        return msg;
    }
}

class ComparadorDePartidos implements java.util.Comparator<Partido> {
    @Override
    public int compare(Partido agr0, Partido arg1) {
        return agr0.getQtdTotalDeVotos() - arg1.getQtdTotalDeVotos();
    }
}