import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

enum tipoDeCargo {
    ESTADUAL, FEDERAL;
}

public class Eleicao {
    private Map<Integer, Partido> partidos = new HashMap<Integer, Partido>();
    private Map<Integer, Candidato> totalCandidatos = new HashMap<Integer, Candidato>();
    private List<Candidato> candidatosOrdenados;
    private List<Partido> partidosOrdenados;
    private LocalDate dataDaEleicao;
    private tipoDeCargo tipo;
    private int numeroDeVagas;
    private int totalVotosNominais;
    private int totalVotosLegenda;

    public Eleicao(tipoDeCargo tipo, LocalDate data) {
        this.candidatosOrdenados = null;
        this.partidosOrdenados = null;
        this.dataDaEleicao = data;
        this.tipo = tipo;
    }

    public void registraLinha(Map<String, Object> linhaConvertida) {
        if (verificaDadosInvalidos(linhaConvertida))
            return;

        if (linhaConvertida.size() > 3) {

            if (!(this.tipo == tipoDeCargo.ESTADUAL && (int) linhaConvertida.get("CD_CARGO") == 7 ||
                    this.tipo == tipoDeCargo.FEDERAL && (int) linhaConvertida.get("CD_CARGO") == 6))
                return;

            Partido pt = partidos.get((Integer) linhaConvertida.get("NR_PARTIDO"));
            if (pt == null) {
                Partido partido = new Partido((String) linhaConvertida.get("SG_PARTIDO"),
                        (int) linhaConvertida.get("NR_PARTIDO"));
                partidos.put((Integer) linhaConvertida.get("NR_PARTIDO"), partido);
                pt = partido;
            }

            if (!((int) linhaConvertida.get("CD_SITUACAO_CANDIDATO_TOT") == 2 ||
                    (int) linhaConvertida.get("CD_SITUACAO_CANDIDATO_TOT") == 16) &&
                    !((String) linhaConvertida.get("NM_TIPO_DESTINACAO_VOTOS")).equals("Válido (legenda)"))
                return;

            Candidato candidato = this.criaCandidato(linhaConvertida, pt);
            totalCandidatos.put((int) linhaConvertida.get("NR_CANDIDATO"), candidato);
            pt.addCandidato(candidato);

            if ((int) linhaConvertida.get("CD_SIT_TOT_TURNO") == 3 ||
                    (int) linhaConvertida.get("CD_SIT_TOT_TURNO") == 2) {
                this.numeroDeVagas++;
            }

        } else if (this.tipo == tipoDeCargo.ESTADUAL && (int) linhaConvertida.get("CD_CARGO") == 7 ||
                this.tipo == tipoDeCargo.FEDERAL && (int) linhaConvertida.get("CD_CARGO") == 6) {

            Candidato c = this.totalCandidatos.get((int) linhaConvertida.get("NR_VOTAVEL"));
            if (c != null) {
                c.registraVotos((int) linhaConvertida.get("QT_VOTOS"));
                if (c instanceof CandidatoLegenda)
                    this.totalVotosLegenda += (int) linhaConvertida.get("QT_VOTOS");
                else
                    this.totalVotosNominais += (int) linhaConvertida.get("QT_VOTOS");
            } else {
                Partido p = this.partidos.get((int) linhaConvertida.get("NR_VOTAVEL"));
                if (p != null) {
                    p.registraVotosLegenda((int) linhaConvertida.get("QT_VOTOS"));
                    this.totalVotosLegenda += (int) linhaConvertida.get("QT_VOTOS");
                }
            }
        }
    }

    public Candidato criaCandidato(Map<String, Object> linhaConvertida, Partido pt) {
        Candidato candidato = null;

        if (((String) linhaConvertida.get("NM_TIPO_DESTINACAO_VOTOS")).equals("Válido (legenda)")) {
            candidato = new CandidatoLegenda((String) linhaConvertida.get("NM_URNA_CANDIDATO"),
                    (int) linhaConvertida.get("NR_CANDIDATO"),
                    (int) linhaConvertida.get("NR_FEDERACAO"),
                    (LocalDate) linhaConvertida.get("DT_NASCIMENTO"),
                    (int) linhaConvertida.get("CD_GENERO"),
                    (int) linhaConvertida.get("CD_SIT_TOT_TURNO"),
                    (int) linhaConvertida.get("CD_SITUACAO_CANDIDATO_TOT"),
                    pt);
        } else {
            candidato = new CandidatoNominal((String) linhaConvertida.get("NM_URNA_CANDIDATO"),
                    (int) linhaConvertida.get("NR_CANDIDATO"),
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
        System.out.println("Número de Vagas: " + this.numeroDeVagas + "\n");
    }

    public void printaRelatorio1() {
        if (this.candidatosOrdenados == null)
            this.ordenaCandidatos();

        int i = 0;
        System.out.println("Deputados estaduais eleitos: ");
        for (Candidato c : this.candidatosOrdenados) {
            if (i == this.numeroDeVagas)
                break;

            if (c.verificaEleito()) {
                System.out.println((i + 1) + " - " + c);
                i++;
            }
        }
        System.out.println("");
    }

    public void printaRelatorio2() {
        if (this.candidatosOrdenados == null)
            this.ordenaCandidatos();

        System.out.println("Candidatos mais votados (em ordem decrescente de votação e respeitando número de vagas):");
        for (int i = 0; i < this.numeroDeVagas; i++) {
            System.out.println((i + 1) + " - " + this.candidatosOrdenados.get(i));
        }
        System.out.println("");
    }

    public void printaRelatorio3() {
        if (this.candidatosOrdenados == null)
            this.ordenaCandidatos();

        System.out.println(
                "Teriam sido eleitos se a votação fosse majoritária, e não foram eleitos:\n(com sua posição no ranking de mais votados)");
        for (int i = 0; i < this.numeroDeVagas; i++) {
            if (!this.candidatosOrdenados.get(i).verificaEleito())
                System.out.println((i + 1) + " - " + this.candidatosOrdenados.get(i));
        }
        System.out.println("");
    }

    public void printaRelatorio4() {
        if (this.candidatosOrdenados == null)
            this.ordenaCandidatos();

        System.out.println(
                "Eleitos, que se beneficiaram do sistema proporcional:\n(com sua posição no ranking de mais votados)");
        int j = 0, i = 0;
        for (Candidato c : this.candidatosOrdenados) {
            if (i == numeroDeVagas)
                break;
            if (c.verificaEleito()) {
                i++;
                if (j >= this.numeroDeVagas)
                    System.out.println((j + 1) + " - " + c);
            }
            j++;
        }
        System.out.println("");
    }

    public void printaRelatorio5() {
        if (this.partidosOrdenados == null)
            this.ordenaPartidos();

        System.out.println("Votaçao dos partidos e número de candidatos eleitos:");
        int i = 0;
        for (Partido p : this.partidosOrdenados) {
            System.out.println((i + 1) + " - " + p);
            i++;
        }

        System.out.println("");
    }

    public void printaRelatorio6() {
        if (this.partidosOrdenados == null) {
            this.ordenaPartidos();
        }
        this.ordenaPartidosPorCandidatoMaisVotado();
        int i = 1;
        System.out.println("Primeiro e último colocados de cada partido:");
        for (Partido p : this.partidosOrdenados) {
            if (p.getQtdDeCandidatos() == 0)
                continue;
            formatacaoRelatorio6(p, i);
            i++;
        }
        System.out.println("");
    }

    public void formatacaoRelatorio6(Partido p, int i) {
        String msg = i + " - " + p.getSigla() + " - " + p.getNumPartido() + ", "
                + p.getCandidatoMaisVotado().getNomeUrna() + " (" + p.getCandidatoMaisVotado().getNumCandidato() + ", "
                + String.format("%,d", p.getCandidatoMaisVotado().getQtdVotos()).replace(',', '.') + " votos) / "
                + p.getCandidatoMenosVotado().getNomeUrna()
                + " (" + p.getCandidatoMenosVotado().getNumCandidato() + ", "
                + String.format("%,d", p.getCandidatoMenosVotado().getQtdVotos()).replace(',', '.') + " votos)";
        System.out.println(msg);
    }

    public void printaRelatorio7() {
        if (this.candidatosOrdenados == null)
            ordenaCandidatos();

        int sub30 = 0,
                _30a40 = 0,
                _40a50 = 0,
                _50a60 = 0,
                acima60 = 0,
                i = 0;

        for (Candidato c : this.candidatosOrdenados) {
            if (i == this.numeroDeVagas)
                break;
            if (c.verificaEleito()) {
                Period period = c.getDataNascimento().until(this.dataDaEleicao);
                int diff = period.getYears();
                if (diff < 30)
                    sub30++;
                else if (diff >= 30 && diff < 40)
                    _30a40++;
                else if (diff >= 40 && diff < 50)
                    _40a50++;
                else if (diff >= 50 && diff < 60)
                    _50a60++;
                else
                    acima60++;

                i++;
            }
        }

        System.out.println("Eleitos, por faixa etária (na data da eleição):");
        System.out.println("      Idade < 30: " + sub30 + " ("
                + String.format("%.02f", (float) sub30 / (float) this.numeroDeVagas * 100).replace(".", ",") + "%)");
        System.out.println("30 <= Idade < 40: " + _30a40 + " ("
                + String.format("%.02f", (float) _30a40 / (float) this.numeroDeVagas * 100).replace(".", ",") + "%)");
        System.out.println("40 <= Idade < 50: " + _40a50 + " ("
                + String.format("%.02f", (float) _40a50 / (float) this.numeroDeVagas * 100).replace(".", ",") + "%)");
        System.out.println("50 <= Idade < 60: " + _50a60 + " ("
                + String.format("%.02f", (float) _50a60 / (float) this.numeroDeVagas * 100).replace(".", ",") + "%)");
        System.out.println("60 <= Idade     : " + acima60 + " ("
                + String.format("%.02f", (float) acima60 / (float) this.numeroDeVagas * 100).replace(".", ",") + "%)");
        System.out.println("");
    }

    public void printaRelatorio8() {
        if (this.candidatosOrdenados == null)
            ordenaCandidatos();

        int fem = 0, masc = 0;

        for (Candidato c : this.candidatosOrdenados) {
            if (c.verificaEleito()) {
                if (c.getGenero() == 2)
                    masc++;
                else
                    fem++;
            }
        }

        System.out.println("Eleitos, por gênero:");
        System.out.println("Feminino:  " + fem + " ("
                + String.format("%.02f",
                        (float) fem / (float) this.numeroDeVagas * 100)
                        .replace(".", ",")
                + "%)");
        System.out.println("Masculino: " + masc + " ("
                + String.format("%.02f",
                        (float) masc / (float) this.numeroDeVagas * 100)
                        .replace(".", ",")
                + "%)");
        System.out.println("");
    }

    public void printaRelatorio9() {
        System.out.println("Total de votos válidos:    "
                + String.format("%,d", this.totalVotosLegenda + this.totalVotosNominais).replace(',', '.'));
        System.out.println("Total de votos nominais:   "
                + String.format("%,d", this.totalVotosNominais).replace(',', '.') + " ("
                + String.format("%.02f",
                        (float) this.totalVotosNominais / (float) (totalVotosLegenda + this.totalVotosNominais) * 100)
                        .replace(".", ",")
                + "%)");
        System.out.println("Total de votos de legenda: "
                + String.format("%,d", this.totalVotosLegenda).replace(',', '.') + " ("
                + String.format("%.02f",
                        (float) this.totalVotosLegenda / (float) (totalVotosLegenda + this.totalVotosNominais) * 100)
                        .replace(".", ",")
                + "%)");
        System.out.println("");
    }

    public void ordenaCandidatos() {
        List<Candidato> candidatosOrdenados = new ArrayList<Candidato>(this.totalCandidatos.values());
        candidatosOrdenados.sort(new ComparadorDeCandidato());
        this.candidatosOrdenados = candidatosOrdenados;
    }

    public void ordenaPartidos() {
        List<Partido> partidosOrdenados = new ArrayList<Partido>(this.partidos.values());
        partidosOrdenados.sort(new ComparadorDePartidos());
        this.partidosOrdenados = partidosOrdenados;
    }

    public void ordenaPartidosPorCandidatoMaisVotado() {
        List<Partido> partidosOrdenados = new ArrayList<Partido>(this.partidos.values());
        partidosOrdenados.sort(new ComparadorDePartidosPorCandidatoMaisVotado());
        this.partidosOrdenados = partidosOrdenados;
    }

    private boolean verificaDadosInvalidos(Map<String, Object> MapDeDados) {
        for (Object dado : MapDeDados.values()) {
            if (dado == null)
                return true;
        }
        return false;
    }
}
