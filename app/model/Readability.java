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

/**
 * Readability class to handale readability calculations
 * @author Kazi Asif Tanim
 */
public class Readability {
	
	/**
     * Process WSResponse and calculate FRI & FKGL and return a JsonNode ading the score value
     * @param WSResponse from API response
     * @return JsonNode by appending FRI & FKGL score
     */
	public static JsonNode processReadability(WSResponse ws) {
		JsonNode jsonNode = ws.asJson();
    	JsonNode projectJsonNode = ws.asJson().get("result").get("projects");
    	
    	AllProjects projects = Utils.convertNodeToAllProjects(jsonNode);
        List<String> combinedStream = Stream.of(projects.getDescriptions())
                .flatMap(Collection::stream).collect(toList());
    	
    	float FREI = 0;
    	float FKGL = 0;
    	
    	for(String description : combinedStream) {
    		int numberOfSentence = findNumberOfSentence(description);
            int numberOfWord = description.split(" ").length;
            int numberOfSyllable = countSyllables(description);
            
            FREI += calculateFleschReadabilityIndex(numberOfSentence, numberOfWord, numberOfSyllable);
            FKGL += calculateFKGL(numberOfSentence, numberOfWord, numberOfSyllable);
    	}
    	
    	float avgFREI = FREI/combinedStream.size();
    	float avgFKGL = FKGL/combinedStream.size();

    	ObjectMapper objectMapper = new ObjectMapper();
    	ObjectNode parentNode = ((ObjectNode) jsonNode).putObject("score");
    	
    	parentNode.put("avgFREI", avgFREI);
    	parentNode.put("avgFKGL", avgFKGL);
    	
    	return jsonNode;
	}
	
	/**
     * Process a given String and calculate FRI & FKGL
     * @param String of preview_description
     * @return a String by appending FRI & FKGL score and Education level to read
     */
	public static String processReadabilityForSingleProject(String description) {
		int numberOfSentence = Readability.findNumberOfSentence(description);
        int numberOfWord = description.split(" ").length;
        int numberOfSyllable = countSyllables(description);
        
        float FRI = calculateFleschReadabilityIndex(numberOfSentence, numberOfWord, numberOfSyllable);
        float FKGL = calculateFKGL(numberOfSentence, numberOfWord, numberOfSyllable);
        String FRIEducationLevel = "";
        
        if(FRI > 100) {
        	FRIEducationLevel = "Early";
        }else if(FRI > 91) {
        	FRIEducationLevel = "5th grade";
        }else if(FRI > 81) {
        	FRIEducationLevel = "6th grade";
        }else if(FRI > 71) {
        	FRIEducationLevel = "7th grade";
        }else if(FRI > 66) {
        	FRIEducationLevel = "8th grade";
        }else if(FRI > 61) {
        	FRIEducationLevel = "9th grade";
        }else if(FRI > 51) {
        	FRIEducationLevel = "High School";
        }else if(FRI > 31) {
        	FRIEducationLevel = "Some College";
        }else if(FRI > 0) {
        	FRIEducationLevel = "College Graduate";
        }else if(FRI <= 0) {
        	FRIEducationLevel = "Law School Graduate";
        }
        
        return "\nIndex: " + FRI + "\nEducation Level: " + FRIEducationLevel + "\nFKGL: " + FKGL; 
	}
	
	/**
     * Method to count syllabales from a given string
     * @param String of preview_description
     * @return int count number of syllables from a given string
     */
	private static int countSyllables(String s) {
        int counter = 0;
        s = s.toLowerCase();
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

	/**
     * Method to count sentence from a string
     * @param String of preview_description
     * @return int count number of sentence from a given string
     */
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
    
    /**
     * Method to calculate Flesch Readability Index
     * @param int number of sentence, int number of word, int number of sullable
     * @return Flaot value of Flesch Redability Index value
     */
    private static float calculateFleschReadabilityIndex(int numberOfSentence, int numberOfWord, int numberOfSyllable) {
        return (float)Math.round(206.835 - 1.015 * (numberOfWord/numberOfSentence) - 84.6 * (numberOfSyllable/numberOfWord));
    }
    
    /**
     * Method to calculate Flesch-Kincaid Grade Level
     * @param int number of sentence, int number of word, int number of sullable
     * @return Flaot value of Flesch-Kincaid Grade Level value
     */
    private static float calculateFKGL(int numberOfSentence, int numberOfWord, int numberOfSyllable) {
        return  (float)(0.39 * (numberOfWord/numberOfSentence) + 11.8 * (numberOfSyllable/numberOfWord) - 15.59);
    }
}
