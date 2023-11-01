import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvReader {
    private List<String> columns;
    private Map<String, Integer> headerIndices = new HashMap<String, Integer>();
    private String separador;

    public CsvReader(List<String> columns, String separador) {
        this.columns = columns;
        this.separador = separador;
    }

    public Map<String,Object> getLineData(String linha, NumberFormat nf) {
        String[] linhaArray = linha.split(this.separador);
        Map<String,Object> result = new HashMap<String,Object>();

        for(String col : columns){
            try {
                if (linhaArray[this.headerIndices.get(col)] == null) {
                    System.out.println("ERa isso meixmo");
                    System.exit(1);
                }
                else if (col.startsWith("NR") || col.startsWith("QT") || col.startsWith("CD")) {
                    int valorConvertido = nf.parse(removeDoubleQuotes(linhaArray[this.headerIndices.get(col)])).intValue();
                    result.put(col, valorConvertido);
                } else if (col.startsWith("DT")) {
                    LocalDate dataConvertida = LocalDate.parse(removeDoubleQuotes(linhaArray[this.headerIndices.get(col)]),
                    DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    result.put(col, dataConvertida);
                } else {
                    result.put(col, removeDoubleQuotes(linhaArray[this.headerIndices.get(col)]));
                }     
            } catch (Exception e) {
                // System.out.println("\n!!Coluna não encontrada: " + col +"!!\n");
                // System.exit(1);
                result.put(col, null);
            }
        }

        return result;
    }

    public void setHeaders(String colunas) {
        //System.out.println("\nSetando headers...");

        String[] colunasArray = colunas.split(this.separador);

        for (int i = 0; i < colunasArray.length; i++) {
            this.headerIndices.put(removeDoubleQuotes(colunasArray[i]), i);
        }

        //System.out.println("Headers setados!\n");
        //System.out.println("Lista de headers processados: " + this.headerIndices.keySet() + "\n");
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
