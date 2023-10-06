import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class App {
    public static void main(String[] args) throws Exception {
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
        // Colocar colunas que nos vamos ler do csv
        Eleicao eleicao = new Eleicao();
        
        // LE E REGISTRA OS CANDIDATOS E PARTIDOS
        ReadFile("files/consulta_cand_2022_ES.csv", colsArqCand, eleicao);
        
        List<String> colsArqVot = new ArrayList<String>();
        colsArqVot.add("\"CD_CARGO\"");
        colsArqVot.add("\"NR_VOTAVEL\"");
        colsArqVot.add("\"QT_VOTOS\"");

        // LE E REGISTRA OS VOTOS DE CADA CANDIDATO/PARTIDO POR SEÇÃO ELEITORAL (SIM ISSO EH A SEÇÃO ELEITORAL)
        ReadFile("files/votacao_secao_2022_ES.csv", colsArqVot, eleicao);
    }

    // Separei a leitura do arquivo em uma funcao separada pq a gente usa ela em 2 arquivos diferentes e fica mais organizado
    public static void ReadFile(String Path, List<String> colsToRead, Eleicao eleicao) {
        CsvReader Reader = new CsvReader(colsToRead, ";");

        try (FileInputStream file = new FileInputStream(Path)) {
            Scanner scanner = new Scanner(file);
            String colunas = scanner.nextLine();

            Reader.setHeaders(colunas); // FUNCAO QUE ASSOCIA O INDICE DE CADA COLUNA COM O NOME DELAS

            System.out.println("Lendo arquivo " + Path.split(";")[-1] + "...");

            while(scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                Map<String,String> lineData = Reader.getLineData(linha);
                // ######################################################
                // ##### LEMBRETE DAVID: FAZER CONVERSAO DAS COLUNAS ####
                // ## PROCESSADAS ACIMA... ELAS ESTAO NO MAP, A CHAVE ###
                // ### EH O NOME DA COL E O VAL EH A STRING P CONVERTER #
                // ######################################################

                // Uma coisa q pensei eh que o nome da coluna sempre comeca com o tipo de dado da variavel...
                // tipo, NR eh numero, CD eh codigo, NM eh nome, DT eh data, etc...
                // (Lembrar que a gente colocou o nome da coluna entre "" por causa da formatacao merda)
                // usar a funcao de string .startsWith() pra ver se a coluna começa com "NR, "CD, "NM, "DT, etc... ex.: .startsWith("\"NR");
               
                // Da pra usar um ArrayList<Object> pra armazenar variaveis de diferentes tipos...
                ArrayList<Object> lineDataConverted = new ArrayList<Object>();

                // Eleicao.buildCandidato / Partido(LineDataConverted) TODO: GUILHERME
            }
            scanner.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
