import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Eleicao {
    private Map<String, Partido> partidos = new HashMap<String, Partido>();
    private List<Candidato> totalCandidatos = new ArrayList<Candidato>();
    private int numVagas;
    private String estado;

    public void registraLinha(Map<String, Object> linhaConvertida) {
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
                    (Date) linhaConvertida.get("DT_NASCIMENTO"),
                    (int) linhaConvertida.get("CD_GENERO"),
                    pt);
            
            totalCandidatos.add(candidato);
            pt.addCandidato(candidato);

           /*colsArqCand.add("\"CD_SITUACAO_CANDIDATO_TOT\"");
            colsArqCand.add("\"CD_SIT_TOT_TURNO\"");
            colsArqCand.add("\"NM_TIPO_DESTINACAO_VOTOS\"");*/
        } else {
            // registra voto
        }
    }
}
