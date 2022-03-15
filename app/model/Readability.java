package model;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import Helpers.Utils;
import play.libs.ws.WSResponse;

public class Readability {
	
	public static JsonNode processReadability(WSResponse ws) {
		JsonNode jsonNode = ws.asJson();
    	JsonNode projectJsonNode = ws.asJson().get("result").get("projects");
    	
    	float FREI = 0;
    	float FKGL = 0;
    	
    	for(JsonNode item : projectJsonNode) {
    		String description = item.get("preview_description").asText();
    		int numberOfSentence = findNumberOfSentence(description);
            int numberOfWord = description.split(" ").length;
            int numberOfSyllable = countSyllables(description);
            
            FREI += calculateFleschReadabilityIndex(numberOfSentence, numberOfWord, numberOfSyllable);
            FKGL += calculateFKGL(numberOfSentence, numberOfWord, numberOfSyllable);
            
    	};
    	
    	float avgFREI = FREI/projectJsonNode.size();
    	float avgFKGL = FKGL/projectJsonNode.size();
    	
//    	Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.fields();
//        
//    	while(fields.hasNext()) {
//    	    Map.Entry<String, JsonNode> field = fields.next();
//    	    String   fieldName  = field.getKey();
//    	    JsonNode fieldValue = field.getValue();
//
//    	    System.out.println(fieldName + " = " + fieldValue.asText());
//    	}
//    	
    	ObjectMapper objectMapper = new ObjectMapper();
    	ObjectNode parentNode = ((ObjectNode) jsonNode).putObject("score");
    	
    	parentNode.put("avgFREI", avgFREI);
    	parentNode.put("avgFKGL", avgFKGL);
    	
    	
        
    	//((ObjectNode) jsonNode).remove("request_id");
    	
    	return jsonNode;
	}
	
	public static String processReadabilityForSingleProject(String description) {
		int numberOfSentence = Readability.findNumberOfSentence(description);
        int numberOfWord = description.split(" ").length;
        int numberOfSyllable = countSyllables(description);
        
        return "\n Index: " + calculateFleschReadabilityIndex(numberOfSentence, numberOfWord, numberOfSyllable) + "\nFKGL: " + calculateFKGL(numberOfSentence, numberOfWord, numberOfSyllable); 
	}
	
	private static int countSyllables(String s) {
        int counter = 0;
        s = s.toLowerCase(); // converting all string to lowercase
        if(s.contains("the ")){
            counter++;
        }
        String[] split = s.split("e!$|e[?]$|e,|e |e[),]|e$");

        ArrayList<String> al = new ArrayList<String>();
        Pattern tokSplitter = Pattern.compile("[aeiouy]+");

        for (int i = 0; i < split.length; i++) {
            String s1 = split[i];
            Matcher m = tokSplitter.matcher(s1);

            while (m.find()) {
                al.add(m.group());
            }
        }

        counter += al.size();
        return counter;
    }

    private static int findNumberOfSentence(String input)
    {
        int len=0;     
        if(input.trim().length()==0)
        {
            len=0;
        }
        else
        {
            int count=1;
            for(int inc=0;inc<input.length();inc++)
            {
                char ch=input.charAt(inc);
                if(ch=='.' || ch=='!' || ch=='?' || ch==':' || ch==';')
                    count++;
                len=count;
            }
            if(count > 1) {
            	len -= 1;
            }
        }
        return len;
    }
    
    private static float calculateFleschReadabilityIndex(int numberOfSentence, int numberOfWord, int numberOfSyllable) {
        return (float)Math.round(206.835 - 1.015 * (numberOfWord/numberOfSentence) - 84.6 * (numberOfSyllable/numberOfWord));
    }
    
    private static float calculateFKGL(int numberOfSentence, int numberOfWord, int numberOfSyllable) {
        return  (float)(0.39 * (numberOfWord/numberOfSentence) + 11.8 * (numberOfSyllable/numberOfWord) - 15.59);
    }
}
