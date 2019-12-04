
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Aras Abdo
 *
 */

/*
 * This class can be adjusted to handle any programming language, currently this
 * is designed for java source code. Change it for you language by adapting the
 * global variables to suit your language. Or add several languages and test to
 * see what language the source code is and set normaliser to the String with
 * that parameter.
 * 
 * This class does the following. @1-It firstly removes all of the
 * comments. @2-It then removes all of the string constants. @3-It then
 * transforms all of the letters in the string to lower-case letters. @4-It maps
 * all of the synonyms to more common forms. @5-Reorders all of the functions
 * detected according to their calling order, the first method call is replaced
 * by the method body, and all other calls of the function are replaced by the
 * word FUN. @6-Finally all of the tokens that are not apart of the target
 * programming language will be deleted.
 */
public class Normaliser {
	// Array to detect all of the comments. /* */ and // etc
	private static String commentPatterns = "(/\\*([^*]|[\\r\\n]|(\\*+([^*/]|[\\r\\n])))*\\*+/)|(//.*)";

	/*
	 * Array to detect all of the string constants. Finds double quotes and single
	 * quotes, with the stuff inside.
	 */
	private static String stringConstantPatterns[] = { "\"(.|\n)*?\"", "\'(.|\n)*?\'" };

	// Language Keywords
	private static String keywords[] = { "public", "void", "private", "boolean", "true", "false", "null", "abstract",
			"assert", "int", "double", "else", "break", "byte", "case", "catch", "char", "class", "const", "continue",
			"default", "do", "enum", "extends", "for", "finally", "float", "goto", "if", "implements", "import",
			"instanceof", "interface", "long", "native", "new", "package", "protected", "return", "short", "static",
			"strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "try", "volatile",
			"while" };

	// My own keywords for describing the structure
	private static String ownKeyWords[] = { "BEGIN_METHOD", "END_METHOD", "TEST", "ASSIGN", "BEGIN_CLASS", "END_CLASS",
			"FUN", "APPLY" };

	/*
	 * Pattern to detect classes and methods. It is important to note that the
	 * pMethod pattern does not detect every single type of method definition,
	 * definitions that do not have one of the strings within the [] at the start of
	 * their declaration, will not be detected. e.g.(String method(){) will not be
	 * detected. While pClass will be able to detected all occurrences of a class
	 * definition
	 */
	private static String pClass = "(((public|private|abstract|static|final|protected)\\s+class\\s+(\\w+)\\s+((extends\\s+\\w+)|(implements\\s+\\w+( ,\\w+)*))?\\s*{)|(class\\s+(\\w+)\\s+((extends\\s+\\w+)|(implements\\s+\\w+( ,\\w+)*))?\\s*{)|(class\\s+(\\w+)\\s+((extends\\s+\\w+\\s+implements\\s+\\w+( ,\\w+)*))?\\s*{)|((public|private|abstract|static|final|protected)\\s+class\\s+(\\w+)\\s+((extends\\s+\\w+\\s+implements\\s+\\w+( ,\\w+)*))?\\s*{))";
	private static String pMethod = "((public|private|protected|static|final|native|synchronized|abstract|transient)+\\s)+[\\$_\\w\\<\\>\\w\\s\\[\\]]*\\s+[\\$_\\w]+\\([^\\)]*\\)?\\s*{";

	// Patterns to apply
	private static HashMap<String, String> patterns;

	// Synonyms, Assignments
	private static HashMap<String, String> synonyms;

	/*  */
	public static String normaliser(String content, boolean returnLinesForEachToken) {
		synonyms = new HashMap<>();
		synonyms.put("+=", "=");
		synonyms.put("-=", "=");
		synonyms.put("*=", "=");
		synonyms.put("/=", "=");

		patterns = new HashMap<>();
		// number=
		patterns.put("[^=\\n]+=", "ASSIGN ");
		// if(asdasdsa){
		patterns.put("if .*{", "if TEST");
		// method call, e.g. scanpattern(matchList, s, PList, TList)
		patterns.put("(\\w+\\.)*\\w+\\((\\s|,|\\[|\\]|\\w|\\w+\\(.*\\)|\\n)*\\)", "APPLY");

		String normalisedString = "";

		/*
		 * Array of numbers, where each index goes up to the total length of the string
		 * lines. thus lineNumberList = total number of lines, each entry is the number
		 * the line is in the string. line 4 is entry 4 in the array.
		 */
		// Split string by line
		String lines[] = content.split("\\r?\\n");

		ArrayList<Integer> lineNumberList = new ArrayList<Integer>();
		// Set lineNumberList to number the line is at in each index.
		for (int i = 0; i < lines.length; i++) {
			lineNumberList.add(i);
		}

		// Preprocessing the string
		content = preprocess(content, lineNumberList);

		// 1 - Remove all of the comments
		normalisedString = removeComments(content, lineNumberList);

		// 2 - Remove all of the string constants
		normalisedString = removeStringConstants(normalisedString, lineNumberList);

		// 3 - Transform everything to lower case
		normalisedString = normalisedString.toLowerCase();

		// 4 - Map the synonyms
		normalisedString = mapSynonyms(normalisedString);

		return normalisedString.toString();
	}

	/*
	 * This method will map the synonyms to more common forms, loops through the
	 * map, and finds all occurrences of the key in the string and replaces it with
	 * the value set at that key.
	 */
	private static String mapSynonyms(String normalisedString) {
		for (Map.Entry<String, String> entry : synonyms.entrySet()) {
			normalisedString = normalisedString.replaceAll(Pattern.quote(entry.getKey()), entry.getValue());
		}
		return normalisedString;
	}

	/* Removes all of the string constants from the content string */
	private static String removeStringConstants(String normalisedString, ArrayList<Integer> lineNumberList) {
		normalisedString = removePatternMatchesFromString(normalisedString, stringConstantPatterns, lineNumberList);
		return normalisedString;
	}

	/* Removes all of the comments from the content string */
	private static String removeComments(String content, ArrayList<Integer> lineNumberList) {
		String commentPatternsTemp[] = { commentPatterns };
		content = removePatternMatchesFromString(content, commentPatternsTemp, lineNumberList);
		return content;
	}

	/*
	 * This method will remove the given pattern from the string content and it will
	 * also adjust the lineNumberList accordingly
	 */
	private static String removePatternMatchesFromString(String content, String[] patterns,
			ArrayList<Integer> lineNumberList) {
		while (true) {
			int unmatched = 0;
			for (int i = 0; i < patterns.length; i++) {
				String pattern = patterns[i];
				Pattern patternTemp = Pattern.compile(pattern);
				Matcher matcher = patternTemp.matcher(content);

				// While their is a match, for each match do.
				if (matcher.find()) { // or while?
					// Stores start and end of each match.
					int start = matcher.start();
					int end = matcher.end();
					String found = matcher.group();
					// Counts the number of lines from the start of the match to the end
					int countNumberOfLines = found.split("\\n").length - 1;

					if (countNumberOfLines > 0) {
						/*
						 * Counts the number of lines from the start of the string all the way to the
						 * start of the pattern match found. The number of lines is set to the int.
						 */
						int start1 = content.substring(0, start).split("\\r?\\n").length;
						int end1 = start1 + countNumberOfLines;
						// Removes the lines from the lineNumberList.
						lineNumberList.subList(start1, end1).clear();
					}
					// Remove the matched pattern from the content.
					content = content.substring(0, start) + content.substring(end, content.length());
				} else {
					unmatched++;
				}
			}
			if (unmatched == patterns.length) {
				break;
			}
		}
		return content;
	}

	/*
	 * Searches through the content string for class and function definitions which
	 * are split over multiple lines. The definitions parts are combined into a
	 * single line, and the string is returned
	 */
	private static String preprocess(String content, ArrayList<Integer> lineNumberList) {
		// Split String content by lines, and set each line as entry in array.
		String lines[] = content.split("\\r?\\n");
		int lineNumber = 0;

		while (lineNumber < lines.length) {
			// Pattern pc = Pattern.compile("new\\s+\\w+\\(");
			// Matcher mc = pc.matcher(lines[lineNumber]);

			Pattern pm = Pattern.compile(
					"((public|private|protected|static|final|native|synchronized|abstract|transient)+\\s)+[\\$_\\w\\<\\>\\w\\s\\[\\]]*\\s+[\\$_\\w]+\\(");
			Matcher mm = pm.matcher(lines[lineNumber]);

			if (mm.find()) {
				int count = 0;
				String s = lines[lineNumber];
				int openP = countParenthesis(lines[lineNumber]);
				while (openP != 0) {
					count += 1;
					openP += countParenthesis(lines[lineNumber + count]);
					s += " " + lines[lineNumber + count];
					// remove the newly empty lines Strings by setting them to ""
					lines[lineNumber + count] = "";
				}
				lines[lineNumber] = s; 
				// also have to remove the empty lines ints from the numberList
				lineNumberList.subList(lineNumber + 1, lineNumber + count + 1).clear();
				lineNumber += count + 1;
				continue;
			}
			lineNumber += 1;
		}
		String output = String.join("\n", lines);
		return output;
	}

	/*
	 * Line in the string is checked for the number of times the parenthesises
	 * occur, loops through each character in the line and checks if it matches '('
	 * and increments the counter by 1, every close decrements it by one.
	 */
	private static int countParenthesis(String line) {
		int openP = 0;
		for (int i = 0; i < line.length(); i++) {
			if (line.charAt(i) == '(') {
				openP += 1;
			} else if (line.charAt(i) == ')') {
				openP -= 1;
			}
		}
		String temp1 = pClass;
		String temp2 = pMethod;
		String[] temp3 = ownKeyWords;
		String[] temp4 = keywords;
		return openP;
	}

}
