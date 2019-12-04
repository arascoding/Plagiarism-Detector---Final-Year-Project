
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Aras Abdo
 *
 */

// Class to read through directories for files to be compared.
public class ReaderWriter {

	private static final StringBuilder stringBuilderSimilarity = new StringBuilder();
	private static final StringBuilder stringBuilderCountTiles = new StringBuilder();

	/**
	 * Method locates directories within the path specified (main directory) and for
	 * each directory within the main directory, call getFileFromDirectory method to
	 * get the files.
	 */
	public static void getDirectoriesFromPathAndFillInformation(ArrayList<String> directoryNames,
			ArrayList<String> fileStrings, String path, ArrayList<String> allowedExtensions,
			ArrayList<String> filesBlackListed) {
		// directory to which path locates to
		File file_MainDirectoryPath = new File(path);

		// List to store directory's based on their absolutePath;
		ArrayList<String> sortDirectorys = new ArrayList<>();

		/**
		 * for each directory in the directory path, store the directory's absolutePath
		 * in sortDirectory
		 */
		for (File dir : file_MainDirectoryPath.listFiles()) {
			sortDirectorys.add(dir.getAbsolutePath());
		}
		// sort the list based on absolutePath
		Collections.sort(sortDirectorys);

		for (String f : sortDirectorys) {
			// Directories within the main directory path location
			File file_SubDirectories = new File(f);

			/**
			 * If it is a directory then call method that will read file that are in that
			 * directory. Files must have more than 1000 characters else it will not be
			 * read.
			 */
			if (file_SubDirectories.isDirectory()) {
				String temp = getFileFromDirectory(file_SubDirectories.getAbsolutePath(), allowedExtensions,
						filesBlackListed);
				if (temp.length() < 1000) {
					continue;
				}

				// Insert name of the directory into the list.
				directoryNames.add(file_SubDirectories.getName());

				// Insert read files into list.
				fileStrings.add(temp);
			}
		}
	}

	/**
	 * Method to append a string to either of the StringBuilders, where the
	 * parameter str is the string that is to be appended.
	 */
	public static void append(String str) {
		stringBuilderSimilarity.append(str);
		stringBuilderCountTiles.append(str);
	}

	/**
	 * Within the specified path, read all files that have the required extensions,
	 * while ignoring all those that have been blacklisted
	 */
	private static String getFileFromDirectory(String absolutePath, ArrayList<String> allowedExtensions,
			ArrayList<String> filesBlackListed) {
		// String for the file in the directory
		StringBuilder stringBuilderFileString = new StringBuilder();

		// Directory to where files are in
		File baseDirectory = new File(absolutePath);
		if (baseDirectory.isDirectory()) {
			// array of files that are in the directory
			File[] sub = baseDirectory.listFiles();
			if (sub != null) {
				for (File type : sub) {
					if (type.isFile()) {
						String tempFileName = type.getName();
						// Split the file name by ".", thus [name][extension]
						String[] nameSplit = tempFileName.split("\\.");
						if (nameSplit.length > 0) {
							for (String extension : allowedExtensions) {
								// Check if allowed extensions match the string at the end of the array (the
								// extension).
								if (extension.equalsIgnoreCase(nameSplit[nameSplit.length - 1])) {
									boolean ignore = false;
									/**
									 * for those files that have the allowed extensions, check if they are
									 * blacklisted. If it is blacklisted, set boolean to true, break out and
									 * continue with next file.
									 */
									for (String blackListed : filesBlackListed) {
										if (blackListed.equals(tempFileName)) {
											System.out.println("\"" + tempFileName + "\"" + " == " + blackListed
													+ " : This File is BlackListed");
											ignore = true;
											break;
										} else {
											System.out.println("\"" + tempFileName + "\"" + " != " + blackListed
													+ " : This File is not BlackListed");
										}
									}

									// If true, break.
									if (ignore) {
										break;
									}

									try {
										/**
										 * Adding information to the stringBuilder, the name of the file and the
										 * contents of the file are added.
										 */
										String tempFileString = readFile(type, Charset.defaultCharset());
										stringBuilderFileString
												.append("\n//" + tempFileName + "\n" + tempFileString + "\n");
									} catch (IOException exception) {
										System.out.println("getFileFromDirectory() METHOD : " + exception);
									}
								}
							}
						}
					} else if (type.isDirectory()) {
						/**
						 * If the type is not a file, but a directory. Then start again from the new
						 * path.
						 **/
						stringBuilderFileString.append(
								getFileFromDirectory(type.getAbsolutePath(), allowedExtensions, filesBlackListed));
					}
				}
			} else {
				System.out.println("No programs have been found");
			}

		}
		// System.out.println(stringBuilderFileString.toString());
		return stringBuilderFileString.toString();
	}

	/**
	 * Reading in one file from the directory, supported by
	 * https://stackoverflow.com/questions/326390/how-do-i-create-a-java-string-from-the-contents-of-a-file
	 */
	private static String readFile(File file, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
		return new String(encoded, encoding);
	}

	/**
	 * Write to file, with data from string. Supported by
	 * https://www.journaldev.com/878/java-write-to-file
	 */
	public static void writeFile(String fileName, String data) {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(fileName);
			fileWriter.write(data);
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException exception) {
			try {
				if (fileWriter != null) {
					fileWriter.close();
				}
			} catch (IOException exception1) {
				exception1.printStackTrace();
			}
		}
	}

	public static void setTokenWithPlagiarism(boolean[] str1Dir, ArrayList<TileValuesMatch> tiles) {
		for (int i = 0; i < tiles.size(); i++) {
			TileValuesMatch tvm = tiles.get(i);
			for (int j = tvm.patternPosition; j < tvm.patternPosition + tvm.length; j++) {
				str1Dir[j] = true;
			}
		}
	}

	/**
	 * creates a list of tokens without empty elements. The fileStrings are split by
	 * whitespace and non-word characters into a list, then the for loop is used to
	 * remove the empty lines, string from last line up to the first. So that there
	 * are no empty lines.
	 */
	public static List<String> createTokenList(String str1) {
		List<String> str1List = new ArrayList<>();
		str1List.addAll(Arrays.asList(str1.split("[\\s+|\\W+]")));

		for (int i = str1List.size() - 1; i >= 0; i--) {
			if (str1List.get(i).isEmpty()) {
				str1List.remove(i);
			}
		}
		return str1List;
	}

	/**
	 * Creates viewable indication of where the plagiarism code is in the files,
	 * sections within the file that are identified as plagiarized will be
	 * highlighted.
	 */
	public static StringBuilder createPlagiarismIndication(String str1, List<String> str1List, boolean[] str1Dir) {
		StringBuilder stringBuilderHTML = new StringBuilder();
		boolean lastState = false;
		int lastPosition = 0;

		// Make into HTML with appropriate tags, Preformatted text and and code tag.
		stringBuilderHTML.append("<html>\n");
		stringBuilderHTML.append("<pre> <code>\n");

		for (int i = 0; i < str1Dir.length; i++) {
			String str = str1List.get(i);
			if (!str.isEmpty()) {
				if (lastState != str1Dir[i]) {
					if (!lastState) {
						// Selected area is highlighted in yellow.
						stringBuilderHTML.append("<span style=\'background-color: #FFFF00\'>");
					} else {
						stringBuilderHTML.append("</span>");
					}
					lastState = str1Dir[i];
				}
			}
			int newPosition = str1.indexOf(str, lastPosition);
			String esc = str1.substring(lastPosition, newPosition);
			esc = esc.replaceAll("<", "&alt;");
			stringBuilderHTML.append(esc);
			lastPosition = newPosition;
		}
		String esc = str1.substring(lastPosition, str1.length());
		esc = esc.replaceAll("<", "&lt;");
		stringBuilderHTML.append("</code> </pre>\n");
		stringBuilderHTML.append("</html>\n");

		return stringBuilderHTML;
	}

	public static void extra(Job job) {
		stringBuilderSimilarity.append(String.format("%04f", job.similarity));
		stringBuilderCountTiles.append(job.tiles.size());
	}

	public static void writeCSV(int minimumTokenMatch) {
		writeFile("Plag-Similarity - minimumTokenMatch" + minimumTokenMatch + ".csv",
				stringBuilderSimilarity.toString());
		writeFile("Plag-Tiles - minimumTokenMatch" + minimumTokenMatch + ".csv", stringBuilderCountTiles.toString());
	}

}
