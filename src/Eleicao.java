import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Eleicao {
    private Map<Integer, Partido> partidos = new HashMap<Integer, Partido>();
    private Map<Integer, Candidato> totalCandidatos = new HashMap<Integer, Candidato>();
    private List<Candidato> candidatosOrdenados;
    private List<Partido> partidosOrdenados;
    private String estado;
    private tipoDeVotos tipo;
    private int numeroDeVagas;

    public Eleicao(tipoDeVotos tipo) {
        this.candidatosOrdenados = null;
        this.partidosOrdenados = null;
        this.tipo = tipo;
    }

    public void registraLinha(Map<String, Object> linhaConvertida) {
        if (linhaConvertida.size() > 3) {
            if (!((int) linhaConvertida.get("CD_SITUACAO_CANDIDATO_TOT") == 2 ||
                    (int) linhaConvertida.get("CD_SITUACAO_CANDIDATO_TOT") == 16) &&
                    !((String) linhaConvertida.get("NM_TIPO_DESTINACAO_VOTOS")).equals("Válido (legenda)"))
                return;

            if (!(this.tipo == tipoDeVotos.ESTADUAL && (int) linhaConvertida.get("CD_CARGO") == 7 ||
                    this.tipo == tipoDeVotos.FEDERAL && (int) linhaConvertida.get("CD_CARGO") == 6))
                return;

            Partido pt = partidos.get((Integer) linhaConvertida.get("NR_PARTIDO"));
            if (pt == null) {
                Partido partido = new Partido((String) linhaConvertida.get("SG_PARTIDO"),
                        (int) linhaConvertida.get("NR_PARTIDO"));
                partidos.put((Integer) linhaConvertida.get("NR_PARTIDO"), partido);
                pt = partido;
            }

            Candidato candidato = this.criaCandidato(linhaConvertida, pt);
            totalCandidatos.put((int) linhaConvertida.get("NR_CANDIDATO"), candidato);
            pt.addCandidato(candidato, (int) linhaConvertida.get("NR_CANDIDATO"));

            if ((int) linhaConvertida.get("CD_SIT_TOT_TURNO") == 3 ||
                    (int) linhaConvertida.get("CD_SIT_TOT_TURNO") == 2) {
                this.numeroDeVagas++;
            }

        } else if (this.tipo == tipoDeVotos.ESTADUAL && (int) linhaConvertida.get("CD_CARGO") == 7 ||
                this.tipo == tipoDeVotos.FEDERAL && (int) linhaConvertida.get("CD_CARGO") == 6) {

            Candidato c = this.totalCandidatos.get((int) linhaConvertida.get("NR_VOTAVEL"));
            if (c != null) {
                c.registraVotos((int) linhaConvertida.get("QT_VOTOS"));
            } else {
                Partido p = this.partidos.get((int) linhaConvertida.get("NR_VOTAVEL"));
                if (p != null)
                    p.registraVotosLegenda((int) linhaConvertida.get("QT_VOTOS"));
            }
        }
    }

    public Candidato criaCandidato(Map<String, Object> linhaConvertida, Partido pt) {
        Candidato candidato = null;

        if (((String) linhaConvertida.get("NM_TIPO_DESTINACAO_VOTOS")).equals("Válido (legenda)")) {
            candidato = new CandidatoLegenda((String) linhaConvertida.get("NM_URNA_CANDIDATO"),
                    (int) linhaConvertida.get("NR_CANDIDATO"),
                    (int) linhaConvertida.get("CD_CARGO"),
                    (int) linhaConvertida.get("NR_FEDERACAO"),
                    (LocalDate) linhaConvertida.get("DT_NASCIMENTO"),
                    (int) linhaConvertida.get("CD_GENERO"),
                    (int) linhaConvertida.get("CD_SIT_TOT_TURNO"),
                    (int) linhaConvertida.get("CD_SITUACAO_CANDIDATO_TOT"),
                    pt);
        } else {
            candidato = new CandidatoNominal((String) linhaConvertida.get("NM_URNA_CANDIDATO"),
                    (int) linhaConvertida.get("NR_CANDIDATO"),
                    (int) linhaConvertida.get("CD_CARGO"),
                    (int) linhaConvertida.get("NR_FEDERACAO"),
                    (LocalDate) linhaConvertida.get("DT_NASCIMENTO"),
                    (int) linhaConvertida.get("CD_GENERO"),
                    (int) linhaConvertida.get("CD_SIT_TOT_TURNO"),
                    (int) linhaConvertida.get("CD_SITUACAO_CANDIDATO_TOT"),
                    pt);
        }
        return candidato;
    }

    public void printaNumeroDeVagas() {
        System.out.println("Numero de Vagas: " + this.numeroDeVagas);
    }

    public void ordenaCandidatos(){
        List<Candidato> candidatosOrdenados = new ArrayList<Candidato>(this.totalCandidatos.values());
        candidatosOrdenados.sort();
    }

    public void printCandidatos() {

        for (Candidato c : this.totalCandidatos.values()) {
            System.out.println(c);
        }
    }

    public void printPartidos() {
        for (Partido p : this.partidos.values()) {
            System.out.println(p);
        }
    }
}

class ComparadorDeCandidato implements java.util.Comparator<Candidato> {
    @Override
    public int compare(Candidato arg0, Candidato arg1) {
        return arg0.getQtdVotos() - arg1.getQtdVotos();
    }
}

class ComparadorDePartidos implements java.util.Comparator<Partido> {
    @Override
    public int compare(Partido agr0, Partido arg1) {
        return agr0.getQtdTotalDeVotos() - arg1.getQtdTotalDeVotos();
    }
}
