package edu.jsu.mcis;

import java.io.*;
import java.util.*;
import com.opencsv.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Converter {
    
    /*
    
        Consider the following CSV data:
        
        "ID","Total","Assignment 1","Assignment 2","Exam 1"
        "111278","611","146","128","337"
        "111352","867","227","228","412"
        "111373","461","96","90","275"
        "111305","835","220","217","398"
        "111399","898","226","229","443"
        "111160","454","77","125","252"
        "111276","579","130","111","338"
        "111241","973","236","237","500"
        
        The corresponding JSON data would be similar to the following (tabs and
        other whitespace have been added for clarity).  Note the curly braces,
        square brackets, and double-quotes!  These indicate which values should
        be encoded as strings, and which values should be encoded as integers!
        
        {
            "colHeaders":["ID","Total","Assignment 1","Assignment 2","Exam 1"],
            "rowHeaders":["111278","111352","111373","111305","111399","111160",
            "111276","111241"],
            "data":[[611,146,128,337],
                    [867,227,228,412],
                    [461,96,90,275],
                    [835,220,217,398],
                    [898,226,229,443],
                    [454,77,125,252],
                    [579,130,111,338],
                    [973,236,237,500]
            ]
        }
    
        Your task for this program is to complete the two conversion methods in
        this class, "csvToJson()" and "jsonToCsv()", so that the CSV data shown
        above can be converted to JSON format, and vice-versa.  Both methods
        should return the converted data as strings, but the strings do not need
        to include the newlines and whitespace shown in the examples; again,
        this whitespace has been added only for clarity.
    
        NOTE: YOU SHOULD NOT WRITE ANY CODE WHICH MANUALLY COMPOSES THE OUTPUT
        STRINGS!!!  Leave ALL string conversion to the two data conversion
        libraries we have discussed, OpenCSV and json-simple.  See the "Data
        Exchange" lecture notes for more details, including example code.
    
    */
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String results = "";
        
        try {
            
            CSVReader reader = new CSVReader(new StringReader(csvString));
            List<String[]> full = reader.readAll();
            Iterator<String[]> iterator = full.iterator();
            
            // INSERT YOUR CODE HERE
            ArrayList<String> colHeaders = new ArrayList<>();
            ArrayList<String> rowHeaders = new ArrayList<>();
            ArrayList<ArrayList<Integer>> values = new ArrayList<>();
            
            JSONObject jsonFormat = new JSONObject();
            
            String[] headers = full.get(0);
            
            for (String e: headers){

                colHeaders.add(e);

            }
            
            for(int i = 1; i < full.size(); i++){
                
                String[] row = full.get(i);
                ArrayList<Integer> rowValues = new ArrayList<>();
                
                rowHeaders.add(row[0]);
                
                for(int j = 1; j < row.length; ++j){
               
                    rowValues.add(Integer.parseInt(row[j]));

                }
                
                values.add(new ArrayList(rowValues));
            }
            
            jsonFormat.put("colHeaders", colHeaders);
            jsonFormat.put("data", values);
            jsonFormat.put("rowHeaders", rowHeaders);
            
            results = jsonFormat.toJSONString();
            
        }        
        catch(Exception e) { return e.toString(); }
        
        return results.trim();
        
    }
    
    public static String jsonToCsv(String jsonString) {
        
        String results = "";
        
        try {

            StringWriter writer = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(writer, ',', '"', '\n');
            
            // INSERT YOUR CODE HERE
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonFormat = (JSONObject)jsonParser.parse(jsonString);
            
            JSONArray storedColHeaders = (JSONArray)jsonFormat.get("colHeaders");
            JSONArray storedRowHeaders = (JSONArray)jsonFormat.get("rowHeaders");
            JSONArray storedValues = (JSONArray)jsonFormat.get("data");
            
            String[] colHeads = new String[storedColHeaders.size()];
            String[] rowHeads = new String[storedRowHeaders.size()];
            String[] values = new String[storedValues.size()];
            
            for(int i = 0; i < storedColHeaders.size(); i++){
                
                colHeads[i] = storedColHeaders.get(i).toString();

            }
            
            csvWriter.writeNext(colHeads);
            
            for(int i = 0; i < storedRowHeaders.size(); i++){

                rowHeads[i] = storedRowHeaders.get(i).toString();
                values[i] = storedValues.get(i).toString();

            }
            
            for(int i = 0; i < values.length; i++){

                JSONArray dataValues = (JSONArray)jsonParser.parse(values[i]);
                String[] row = new String[dataValues.size() + 1];

                row[0] = rowHeads[i];

                for(int j = 0; j < dataValues.size(); j++){

                    row[j + 1] = dataValues.get(j).toString();

                }

                csvWriter.writeNext(row);

            }
            
            results = writer.toString();
            
        }
        
        catch(Exception e) { return e.toString(); }
        
        return results.trim();
        
    }

}