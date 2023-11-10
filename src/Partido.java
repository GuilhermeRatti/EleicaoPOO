import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Partido {
    private List<Candidato> candidatos = new ArrayList<Candidato>();
    private String sigla;
    private int numPartido;
    private int qtdVotosNominais;
    private int qtdVotosLegenda;
    private boolean estaOrdenado;
    

    public Partido(String sigla, int numPartido) {
        this.sigla = sigla;
        this.numPartido = numPartido;
        this.estaOrdenado = false;

    }

    public int getQtdTotalDeVotos() {
        return this.qtdVotosLegenda + this.qtdVotosNominais;
    }

    public int getNumPartido() {
        return this.numPartido;
    }

    public String getSigla() {
        return this.sigla;
    }

    public void addCandidato(Candidato cand) {
        if (cand instanceof CandidatoNominal)
            this.candidatos.add(cand);
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

    public void ordenaCandidatosDoPartido() {
        this.candidatos.sort(new ComparadorDeCandidato());
        this.estaOrdenado = true;
    }

    public Candidato getCandidatoMaisVotado() {
        if (!this.estaOrdenado) {
            this.ordenaCandidatosDoPartido();
        }

        // Pegando o candidato mais votado, sendo a data de nascimento o criterio de desempate (candidato mais velho)
        Candidato candMaisVotado = this.candidatos.get(0);
        LocalDate dataNascCandMaisVotado = candMaisVotado.getDataNascimento();
        for(int i=1; i<this.candidatos.size(); i++) {
            if(this.candidatos.get(i).getQtdVotos() == candMaisVotado.getQtdVotos()) {
                if(this.candidatos.get(i).getDataNascimento().isBefore(dataNascCandMaisVotado)) {
                    candMaisVotado = this.candidatos.get(i);
                    dataNascCandMaisVotado = candMaisVotado.getDataNascimento();
                }
            }
            else {
                break;
            }
        }

        return candMaisVotado;
    }

    public Candidato getCandidatoMenosVotado() {
        if (!this.estaOrdenado) {
            this.ordenaCandidatosDoPartido();
        }

        // Pegando o candidato menos votado, sendo a data de nascimento o criterio de desempate (candidato mais novo)
        Candidato candMenosVotado = this.candidatos.get(this.candidatos.size() - 1);
        LocalDate dataNascCandMenosVotado = candMenosVotado.getDataNascimento();
        for(int i=this.candidatos.size()-2; i>=0; i--) {
            if(this.candidatos.get(i).getQtdVotos() == candMenosVotado.getQtdVotos()) {
                if(this.candidatos.get(i).getDataNascimento().isAfter(dataNascCandMenosVotado)) {
                    candMenosVotado = this.candidatos.get(i);
                    dataNascCandMenosVotado = candMenosVotado.getDataNascimento();
                }
            }
            else {
                break;
            }
        }
        return candMenosVotado;
    }

    public int getQtdDeCandidatos() {
        return this.candidatos.size();
    }

    @Override
    public String toString() {
        String flexaoDeVotoTotal = null;
        if (this.getQtdTotalDeVotos() > 1) {
            flexaoDeVotoTotal = " votos ";
        } else { 
            flexaoDeVotoTotal = " voto ";
        }

        String flexaoDeVotoNominal = null;
        if (this.qtdVotosNominais > 1) {
            flexaoDeVotoNominal = " nominais e ";
        } else {
            flexaoDeVotoNominal = " nominal e ";
        }

        String msg = this.sigla + " - " + this.numPartido + ", " +
                String.format("%,d", this.qtdVotosLegenda + this.qtdVotosNominais).replace(',', '.') + flexaoDeVotoTotal +
                "(" + String.format("%,d", this.qtdVotosNominais).replace(',', '.') + flexaoDeVotoNominal
                + String.format("%,d", this.qtdVotosLegenda).replace(',', '.') + " de legenda), " +
                this.getQtdCandEleitos();

        if (this.getQtdCandEleitos() > 1) {
            msg += " candidatos eleitos";
        } else
            msg += " candidato eleito";

        return msg;
    }
}

class ComparadorDePartidos implements java.util.Comparator<Partido> {
    @Override
    public int compare(Partido arg0, Partido arg1) {
        if (arg1.getQtdTotalDeVotos() - arg0.getQtdTotalDeVotos() == 0) {
            return arg0.getNumPartido() - arg1.getNumPartido();
        }
        return arg1.getQtdTotalDeVotos() - arg0.getQtdTotalDeVotos();
    }
}

class ComparadorDeVotos implements java.util.Comparator<Partido> {
    @Override
    public int compare(Partido arg0, Partido arg1) {
        return arg1.getCandidatoMaisVotado().getQtdVotos() - arg0.getCandidatoMaisVotado().getQtdVotos();
    }

}

class ComparadorDePartidosPorCandidatoMaisVotado implements java.util.Comparator<Partido> {
    @Override
    public int compare(Partido arg0, Partido arg1) {
        if (arg0.getQtdDeCandidatos() == 0)
            return 1;
        else if (arg1.getQtdDeCandidatos() == 0)
            return -1;
        else
            return arg1.getCandidatoMaisVotado().getQtdVotos() - arg0.getCandidatoMaisVotado().getQtdVotos();
    }
}
