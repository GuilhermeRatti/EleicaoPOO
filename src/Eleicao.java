import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Eleicao {
    private Map<String, Partido> partidos = new HashMap<String, Partido>();
    private Map<Integer, Candidato> totalCandidatos = new HashMap<Integer, Candidato>();
    private int numVagas;
    private String estado;

    public void registraLinha(Map<String, Object> linhaConvertida) {
        if (!((int) linhaConvertida.get("CD_SITUACAO_CANDIDATO_TOT") == 2 ||
                (int) linhaConvertida.get("CD_SITUACAO_CANDIDATO_TOT") == 16) &&
                !((String) linhaConvertida.get("NM_TIPO_DESTINACAO_VOTOS")).equals("Válido (legenda)")) {
            return;
        }

        if (linhaConvertida.size() > 3) {
            Partido pt = partidos.get((String) linhaConvertida.get("SG_PARTIDO"));
            if (pt == null) {
                Partido partido = new Partido((String) linhaConvertida.get("SG_PARTIDO"),
                        (int) linhaConvertida.get("NR_PARTIDO"));
                partidos.put((String) linhaConvertida.get("SG_PARTIDO"), partido);
                pt = partido;
            }

            Candidato candidato = new Candidato((String) linhaConvertida.get("NM_URNA_CANDIDATO"),
                    (int) linhaConvertida.get("NR_CANDIDATO"),
                    (int) linhaConvertida.get("CD_CARGO"),
                    (int) linhaConvertida.get("NR_FEDERACAO"),
                    (LocalDate) linhaConvertida.get("DT_NASCIMENTO"),
                    (int) linhaConvertida.get("CD_GENERO"),
                    (int) linhaConvertida.get("CD_SIT_TOT_TURNO"),
                    (int) linhaConvertida.get("CD_SITUACAO_CANDIDATO_TOT"),
                    pt);

            totalCandidatos.put((int) linhaConvertida.get("NR_CANDIDATO"), candidato);
            pt.addCandidato(candidato, (int) linhaConvertida.get("NR_CANDIDATO"));

        } else {
            // registra voto
        }
    }

    public void printCandidatos() {

        for (Candidato c : this.totalCandidatos.values()) {
            System.out.println(c);
        }
    }
}
