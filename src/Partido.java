import java.util.ArrayList;
import java.util.List;

public class Partido {
    private List<Candidato> candidatos = new ArrayList<Candidato>();
    private String sigla;
    private int numPartido;
    private int quantVotos;
    private int quantCand;

    public Partido(String sigla, int numPartido) {
        this.sigla = sigla;
        this.numPartido = numPartido;
        this.quantCand = 0;
        this.quantVotos = 0;
    }

    public void addCandidato(Candidato cand){
        this.candidatos.add(cand);
        this.quantCand++;
    }
}
