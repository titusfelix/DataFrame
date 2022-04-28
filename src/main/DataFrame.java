package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class DataFrame {
    private List<String> index = new ArrayList<>();
    private Map<String, Object[]> data = new LinkedHashMap<>();
    private boolean[] numeric;
    private String indexName;
    private int nlin, ncol;

    public DataFrame() {
    }

    public DataFrame(String numeFisier, String delim,
                     boolean varN, // are header
                     int indexCol //coloana cu numele instantelor
    ) throws Exception {
        try (BufferedReader in = new BufferedReader(new FileReader(numeFisier))) {
            String[] columns = null;
            Map<String, List<String>> values = new LinkedHashMap<>();
            if (varN) {
                String[] t = in.readLine().split(delim);
                if (indexCol != -1) {
                    indexName = t[indexCol];
                    ncol = t.length - 1;
                    columns = new String[ncol];
                    numeric = new boolean[ncol];
                    for (int i = 0, j = 0; i < t.length; i++) {
                        if (i != indexCol) {
                            columns[j] = t[i].trim();
                            numeric[j] = true;
                            j++;
                        }
                    }
                } else {
                    indexName = "Index";
                    columns = new String[t.length];
                    numeric = new boolean[t.length];
                    ncol = t.length;
                    for (int i = 0; i < t.length; i++) {
                        columns[i] = t[i].trim();
                        numeric[i] = true;
                    }
                }
                for (String c : columns) {
                    values.put(c, new ArrayList());
                }
            }
            List<String> linii = new ArrayList<>();
            in.lines().forEach(linie -> linii.add(linie));
            nlin = linii.size();
            int k = 0;
            for (String linie : linii) {
                String[] t = linie.split(delim);
                if (indexCol != -1) {
                    if (ncol!=0) {
                        if (t.length - 1 != ncol) {
                            throw new Exception("Numar invalid de elemente pe o linie!");
                        }
                    } else {
                        ncol=t.length-1;
                        columns = new String[ncol];
                        numeric = new boolean[ncol];
                        for (int i = 0; i < ncol; i++) {
                            columns[i]="c"+i;
                            numeric[i]=true;
                            values.put(columns[i], new ArrayList());
                        }
                    }
                    index.add(t[indexCol].trim());
                    for (int i = 0, j = 0; i < t.length; i++) {
                        if (i != indexCol) {
                            String c = columns[j];
                            String val = t[i].trim();
                            if (!val.isEmpty()) {
                                try {
                                    Double.parseDouble(val);
                                } catch (Exception ex) {
                                    numeric[j] = false;
                                }
                            }
                            values.get(c).add(val);
                            j++;
                        }
                    }
                } else {
                    if (ncol!=0) {
                        if (t.length != ncol) {
                            throw new Exception("Numar invalid de elemente pe o linie!");
                        }
                    } else {
                        ncol=t.length;
                        columns = new String[ncol];
                        numeric = new boolean[ncol];
                        for (int i = 0; i < ncol; i++) {
                            columns[i]="c"+i;
                            numeric[i]=true;
                            values.put(columns[i], new ArrayList());
                        }
                    }
                    index.add(String.valueOf(k));
                    k++;
                    for (int i = 0; i < t.length; i++) {
                        String val = t[i].trim();
                        values.get(columns[i]).add(val);
                        if (!val.isEmpty()) {
                            try {
                                Double.parseDouble(val);
                            } catch (Exception ex) {
                                numeric[i] = false;
                            }
                        }
                    }
                }
            }
//            Copiere
            for (int i = 0; i < columns.length; i++) {
                List<String> v = values.get(columns[i]);
                if (numeric[i]) {
                    Double[] v_ = new Double[v.size()];
                    for (int j = 0; j < v.size(); j++) {
                        String val = v.get(j);
                        if (val.isEmpty()){
                            v_[j] = Double.NaN;
                        } else {
                            v_[j] = Double.parseDouble(v.get(j));
                        }
                    }
                    data.put(columns[i], v_);
                } else {
                    String[] v_ = v.toArray(new String[0]);
                    data.put(columns[i], v_);
                }
            }
        }
    }

    public String[] getIndex() {
        return index.toArray(new String[0]);
    }

    public double[][] getData() {
        Set<Integer> coloaneNumerice = new LinkedHashSet<>();
        for (int i = 0; i < numeric.length; i++) {
            if (numeric[i]) {
                coloaneNumerice.add(i);
            }
        }
        if (coloaneNumerice.size() == 0) {
            return null;
        }
        int m = coloaneNumerice.size();
        double[][] v = new double[nlin][m];
        String[] coloane = getColumns();
        int j = 0;
        for (int k : coloaneNumerice) {
            Object[] c = data.get(coloane[k]);
            for (int i = 0; i < c.length; i++) {
                v[i][j] = (double) c[i];
            }
            j++;
        }
        return v;
    }

    public double[][] getData(String[] c) {
        boolean isNumeric = true;
        List<String> cList = data.keySet().stream().collect(Collectors.toList());
        for (int i = 0; i < c.length; i++) {
            int k = cList.indexOf(c[i]);
            if (!numeric[k]) {
                isNumeric = false;
                break;
            }
        }
        if (isNumeric) {
            double[][] v = new double[nlin][c.length];
            int j = 0;
            for (String coloana : c) {
                Object[] val = data.get(coloana);
                for (int i = 0; i < val.length; i++) {
                    int k = cList.indexOf(coloana);
                    v[i][j] = (double) val[i];
                }
                j++;
            }
            return v;
        } else {
            return null;
        }
    }

    public String[] getNumericColumns(){
        Set<String> coloaneNumerice = new LinkedHashSet<>();
        String[] coloane = getColumns();
        for (int i = 0; i < numeric.length; i++) {
            if (numeric[i]){
                coloaneNumerice.add(coloane[i]);
            }
        }
        return coloaneNumerice.toArray(new String[0]);
    }
    public String[] getColumns() {
        String[] columns = data.keySet().toArray(new String[0]);
        return columns;
    }

    public boolean[] getNumeric() {
        return numeric;
    }

}
