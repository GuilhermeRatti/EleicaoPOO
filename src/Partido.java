import java.sql.Struct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Partido {
    private Map<Integer,Candidato> candidatos = new HashMap<Integer,Candidato>();
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

    public void addCandidato(Candidato cand, int numCandidato){
        this.candidatos.put(numCandidato, cand);
        this.quantCand++;
    }

    @Override
    public String toString() {
        
        String msg = this.sigla;
        
        return msg;
    }
}
