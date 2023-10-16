import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvReader {
    private List<String> columns;
    private Map<String, Integer> headerIndices = new HashMap<String, Integer>();
    private Map<String, String> informations = new HashMap<String, String>();
    private String separador;

    public CsvReader(List<String> columns, String separador) {
        this.columns = columns;
        this.separador = separador;
    }

    public Map<String,String> getLineData(String linha) {
        String[] linhaArray = linha.split(this.separador);


        for(String col : columns){
            try {
                this.informations.put(col, linhaArray[this.headerIndices.get(col)]);
            } catch (Exception e) {
                System.out.println("\n!!Coluna não encontrada: " + col +"!!\n");
                System.exit(1);
            }
        }

        return new HashMap<String,String>(this.informations);
    }

    public void setHeaders(String colunas) {
        System.out.println("\nSetando headers...");

        String[] colunasArray = colunas.split(this.separador);
        
        for(int i = 0; i < colunasArray.length; i++) {
            this.headerIndices.put(removeDoubleQuotes(colunasArray[i]), i);
        }

        System.out.println("Headers setados!\n");
        System.out.println("Lista de headers processados: " + this.headerIndices.keySet()+"\n");
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
