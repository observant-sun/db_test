package kriuchkov.maksim.db_test;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws Exception {
        PrintStream outStream = null;
        try (Connection conn = DBConnectionManager.getConnection()) {
            // if no arguments are passed, writing to stdout
            // if a file name is passed, writing to that file
            outStream = (args.length == 0) ? System.out : new PrintStream(args[0]);

            // getting info from TABLE_LIST table
            Map<String, Set<String>> primaryKeysOfTables = new HashMap<>();
            ResultSet rsTableList = conn.createStatement()
                    .executeQuery("SELECT TABLE_NAME, PK FROM TABLE_LIST");
            while (rsTableList.next()) {
                String tableName = rsTableList.getString("TABLE_NAME");
                if (isBlankOrNull(tableName)) {
                    throw new RuntimeException("blank or null TABLE_NAME in table TABLE_LIST");
                }
                if (primaryKeysOfTables.containsKey(tableName)) {
                    throw new RuntimeException(String.format("TABLE_NAME %s appears more than once in TABLE_LIST", tableName));
                }

                String pk = rsTableList.getString("PK");
                if (isBlankOrNull(pk)) {
                    throw new RuntimeException("blank or null PK in table TABLE_LIST for TABLE_NAME " + tableName);
                }

                Set<String> primaryKeys = Arrays.stream(pk.split(","))
                        .map(String::trim)
                        .map(String::toLowerCase)
                        .collect(Collectors.toSet());
                primaryKeysOfTables.put(tableName, primaryKeys);
            }

            // getting info from TABLE_COLS and printing primary keys
            ResultSet rsTableCols = conn.createStatement()
                    .executeQuery("SELECT TABLE_NAME, COLUMN_NAME, COLUMN_TYPE FROM TABLE_COLS");
            while (rsTableCols.next()) {
                String tableName = rsTableCols.getString("TABLE_NAME");
                if (!primaryKeysOfTables.containsKey(tableName)) {
                    continue;
                }
                String columnName = rsTableCols.getString("COLUMN_NAME");
                if (isBlankOrNull(columnName)) {
                    throw new RuntimeException("blank or null COLUMN_NAME in table TABLE_LIST for TABLE_NAME " + tableName);
                }
                columnName = columnName.toLowerCase();
                if (primaryKeysOfTables.get(tableName).contains(columnName)) {
                    String columnType = rsTableCols.getString("COLUMN_TYPE");
                    outStream.printf("%s, %s, %s%n", tableName, columnName, columnType);
                }
            }
        } finally {
            // we shouldn't be closing System.out
            if ((outStream != null) && (outStream != System.out)) {
                outStream.close();
            }
        }
    }

    private static boolean isBlankOrNull(String str) {
        return (str == null) || str.isBlank();
    }

}
