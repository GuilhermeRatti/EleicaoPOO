import java.io.FileInputStream;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        Eleicao eleicao = new Eleicao(); 

        List<String> colsArqCand = new ArrayList<String>();
        colsArqCand.add("\"CD_CARGO\"");
        colsArqCand.add("\"CD_SITUACAO_CANDIDATO_TOT\"");
        colsArqCand.add("\"NR_CANDIDATO\"");
        colsArqCand.add("\"NM_URNA_CANDIDATO\"");
        colsArqCand.add("\"NR_PARTIDO\"");
        colsArqCand.add("\"SG_PARTIDO\"");
        colsArqCand.add("\"NR_FEDERACAO\"");
        colsArqCand.add("\"DT_NASCIMENTO\"");
        colsArqCand.add("\"CD_SIT_TOT_TURNO\"");
        colsArqCand.add("\"CD_GENERO\"");
        colsArqCand.add("\"NM_TIPO_DESTINACAO_VOTOS\"");

        List<String> colsArqVot = new ArrayList<String>();
        colsArqVot.add("\"CD_CARGO\"");
        colsArqVot.add("\"NR_VOTAVEL\"");
        colsArqVot.add("\"QT_VOTOS\"");

        ReadFile("files/consulta_cand_2022_ES.csv", colsArqCand, eleicao);
       //ReadFile("files/votacao_secao_2022_ES.csv", colsArqVot, eleicao);
    }

    // Separei a leitura do arquivo em uma funcao separada pq a gente usa ela em 2
    // arquivos diferentes e fica mais organizado
    public static void ReadFile(String Path, List<String> colsToRead, Eleicao eleicao) {
        CsvReader Reader = new CsvReader(colsToRead, ";");

        try (FileInputStream file = new FileInputStream(Path)) {
            Scanner scanner = new Scanner(file);
            String colunas = scanner.nextLine();

            Reader.setHeaders(colunas); // FUNCAO QUE ASSOCIA O INDICE DE CADA COLUNA COM O NOME DELAS

            System.out.println("Lendo arquivo " + Path.split(";")[Path.split(";").length - 1] + "...");
            Locale brLocale = Locale.forLanguageTag("pt-BR");
            NumberFormat nf = NumberFormat.getInstance(brLocale);
            HashMap<String, Object> lineDataConverted = new HashMap<String, Object>();

            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                Map<String, String> lineData = Reader.getLineData(linha);

                for (String s : lineData.keySet()) {
                    s = removeDoubleQuotes(s);
                    if (s.startsWith("\"NR") || s.startsWith("\"QT")) {
                        int valorConvertido = nf.parse(lineData.get(s)).intValue();
                        lineDataConverted.put(s, valorConvertido);
                    } else if (s.startsWith("\"DT")) {
                        LocalDate dataConvertida = LocalDate.parse(lineData.get(s),
                                DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        lineDataConverted.put(s, dataConvertida);
                    } else {
                        lineDataConverted.put(s, lineData.get(s));
                    }
 
                }

                    
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String removeDoubleQuotes(String input) {

        StringBuilder sb = new StringBuilder();

        char[] tab = input.toCharArray();
        for (char current : tab) {
            if (current != '"')
                sb.append(current);
        }

        return sb.toString();
    }
}
