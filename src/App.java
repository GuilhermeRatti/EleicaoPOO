
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

enum tipoDeVotos {
    ESTADUAL, FEDERAL;
}

public class App {
    public static void main(String[] args) throws Exception {
        // if (args.length != 4) {
        // throw new Exception("Numero de argumentos invalido");
        // }

        // tipoDeVotos tipo;
        // if (args[0].equals("--estadual")) {
        // tipo = tipoDeVotos.ESTADUAL;
        // } else if (args[0].equals("--federal")) {
        // tipo = tipoDeVotos.FEDERAL;
        // } else {
        // throw new Exception("Tipo de votos invalido");
        // }

        // String pathCand = args[1];
        // String pathVotos = args[2];

        // LocalDate data =
        // LocalDate.parse(args[3],DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        Eleicao eleicao = new Eleicao(tipoDeVotos.ESTADUAL);

        List<String> colsArqCand = new ArrayList<String>();
        colsArqCand.add("CD_CARGO");
        colsArqCand.add("CD_SITUACAO_CANDIDATO_TOT");
        colsArqCand.add("NR_CANDIDATO");
        colsArqCand.add("NM_URNA_CANDIDATO");
        colsArqCand.add("NR_PARTIDO");
        colsArqCand.add("SG_PARTIDO");
        colsArqCand.add("NR_FEDERACAO");
        colsArqCand.add("DT_NASCIMENTO");
        colsArqCand.add("CD_SIT_TOT_TURNO");
        colsArqCand.add("CD_GENERO");
        colsArqCand.add("NM_TIPO_DESTINACAO_VOTOS");

        List<String> colsArqVot = new ArrayList<String>();
        colsArqVot.add("CD_CARGO");
        colsArqVot.add("NR_VOTAVEL");
        colsArqVot.add("QT_VOTOS");

        ReadFile("files/consulta_cand_2022_ES.csv", colsArqCand, eleicao);
        ReadFile("files/votacao_secao_2022_ES.csv", colsArqVot, eleicao);

        eleicao.printaNumeroDeVagas();
        eleicao.ordenaCandidatos();
        eleicao.printaRelatorio3();
        eleicao.printaRelatorio4();
    }

    // Separei a leitura do arquivo em uma funcao separada pq a gente usa ela em 2
    // arquivos diferentes e fica mais organizado
    public static void ReadFile(String Path, List<String> colsToRead, Eleicao eleicao) {
        CsvReader Reader = new CsvReader(colsToRead, ";");

        try (FileInputStream file = new FileInputStream(Path)) {
            InputStreamReader input = new InputStreamReader(file, "ISO-8859-1");
            BufferedReader buffer = new BufferedReader(input);

            Reader.setHeaders(buffer.readLine()); // FUNCAO QUE ASSOCIA O INDICE DE CADA COLUNA COM O NOME DELAS

            System.out.println("Lendo arquivo " + Path.split(";")[Path.split(";").length - 1] + "...");
            Locale brLocale = Locale.forLanguageTag("pt-BR");
            NumberFormat nf = NumberFormat.getInstance(brLocale);

            String linha = buffer.readLine();
            while (linha != null) {
                Map<String, Object> lineData = Reader.getLineData(linha, nf);
                eleicao.registraLinha(lineData);
                linha = buffer.readLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
