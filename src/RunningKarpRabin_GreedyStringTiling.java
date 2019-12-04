
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Aras Abdo
 *
 */

public class RunningKarpRabin_GreedyStringTiling {
	/**
	 * Class for GST algorithm hash table, once called new HashMap is created, add
	 * and get methods
	 */
	public static class HashTable {
		// Each key is assigned an arrayList of integers
		HashMap<Long, ArrayList<Integer>> hashMap;

		// Constructor: Once called new hashmap is created
		public HashTable() {
			hashMap = new HashMap<>();
		}

		public void add(long key, int value) {
			ArrayList<Integer> list;

			if (hashMap.containsKey(key)) {
				/**
				 * if, the key is contained in the HashMap, get the arrayList that is already at
				 * the key, and add the new value to the arrayList. Finally put the new adjusted
				 * arrayList back into the key.
				 */
				list = hashMap.get(key);
				list.add(value);
				hashMap.put(key, list);

			} else {
				/**
				 * else, the hashMap does not contain the key, then that means there is no
				 * arrayList, thus new arrayList is created, the value is then added to the new
				 * arrayList and finally that list is assigned to the key.
				 */
				list = new ArrayList<>();
				list.add(value);
				hashMap.put(key, list);
			}
		}

		/**
		 * If the hasMmap does not contain the key then it returns null, otherwise the
		 * arrayList designated to the key is return, which is an arrayList of
		 * integers..
		 */
		public ArrayList<Integer> get(long key) {
			if (hashMap.containsKey(key)) {
				return hashMap.get(key);
			} else {
				return null;
			}
		}
	}

	/**
	 * The initial search length should be set to an initial small constant value,
	 * in this case 20 will suffice. Because very long maximal-matches are rare,
	 * thus having a large initial value will be inappropriate as it will generate a
	 * number of empty sweeps by scanpattern method, until a match is found.
	 */
	private static int initialSearchSize = 20;

	/**
	 * The Implementation of the RKR-GST Algorithm is based on the two papers
	 * written by M. J. Wise, shown below:
	 * 
	 * M. J. Wise, “YAP 3: Improved detection of similarities in computer program
	 * and other texts.,” in Technical Symposium on Computer Science Education:
	 * Proceedings of the twenty-seventh SIGCSE technical symposium on Computer
	 * science education, Philadelphia, Pennsylvania, 1996.
	 *
	 * M. J. Wise, “String similarity via greedy string tiling and running
	 * Karp-Rabin matching,” Department of Computer Science, University of Sydney,
	 * Australia, 1993.
	 */
	public static ArrayList<TileValuesMatch> run(String[] patternList, String[] textList, int minimalMatchingLength) {
		if (initialSearchSize < 5) {
			initialSearchSize = 20;
		}

		/**
		 * The length can be equal to 1, but in general will be an integer greater than
		 * 1.
		 */
		if (minimalMatchingLength < 1) {
			minimalMatchingLength = 3;
		}

		ArrayList<TileValuesMatch> tiles = new ArrayList<>();
		ArrayList<Queue<TileValuesMatch>> TVMATCHList = new ArrayList<>();

		/**
		 * Top-Level-Algorithm, Greedy String-Tiling, sets the search length equal to
		 * the initial search length, which is set to 20 by default.
		 */
		int searchLength = initialSearchSize;
		boolean stop = false;

		// while stop does not equal to false do;
		while (!stop) {
			// size of the largest maximal-matches from this scan.
			int largestMaximalMatch = scanpattern(TVMATCHList, searchLength, patternList, textList);

			/**
			 * for very long string do not mark the tiles, but iterate with a larger search
			 * length. From this top-level algorithm, it should be noted that only during
			 * the first iteration will the long strings be found. After the first
			 * iteration, no matches that are 2 times greater than the current
			 * search-length(s) will be found.
			 */
			if (largestMaximalMatch > 2 * searchLength) {
				searchLength = largestMaximalMatch;
			} else {
				/**
				 * Second Phase - MarkStrings. the maximal-matches, starting with the longest
				 * one, are taken in turn and tested to see whether they have been occluded by
				 * their sibling tile. i.e. part of the maximal-match is already marked. If it
				 * is not marked, then a new tile is created by marking the two substrings.
				 * MarkStrings: Create tiles from matches taken from list of queues. For each
				 * queue, starting with the top queue, while there is a non-empty queue do
				 */
				for (Queue<TileValuesMatch> TVMQueue : TVMATCHList) {
					while (!TVMQueue.isEmpty()) {
						/**
						 * Retrieves and removes the head of this queue and sets it to TVMATCH (thus the
						 * first head of the queue is set to match variable), or returns null if this
						 * queue is empty. match = (p-position, t-position, length)
						 */
						TileValuesMatch match = TVMQueue.poll();
						if (!isOccluded(tiles, match)) {
							for (int i = 0; i < match.length; i++) {
								patternList[match.patternPosition + i] = markToken(
										patternList[match.patternPosition + i]);
								textList[match.textPosition + i] = markToken(textList[match.textPosition + i]);
							}
							tiles.add(match);
						}
					}
				}
				TVMATCHList.clear();
				// End of markStrings (Second Phase)
				// markStrings(patternList, textList, TVMATCHList, searchLength, tiles);

				if (searchLength > (2 * minimalMatchingLength)) {
					searchLength = searchLength / 2;
				} else if (searchLength > minimalMatchingLength) {
					searchLength = minimalMatchingLength;
				} else {
					stop = true;
				}
			}
		}
		return tiles;
	}

	/**
	 * First Phase - ScanPattern algorithm: that uses Running Karp-Rabin matching,
	 * it is used to find all of the maximal matches above a certain size(search
	 * length) or greater, these are collected. Scans both text and pattern arrays
	 * for matches, if a match is found which is 2* the size of the search length(s)
	 * then this size is returned so that the method scanPattern can be restarted
	 * with it. A list of queues is used to store all of the matches that have been
	 * found.
	 */
	private static int scanpattern(ArrayList<Queue<TileValuesMatch>> tVMATCHList, int s, String[] Pattern,
			String[] Text) {
		HashTable hashTable = new HashTable();

		// used as Key later
		int t = 0;

		// used as Value (Object) later
		int karpRabinHashValue = 0;

		int longestMaximalMatch = 0;

		// Sets to true if their is no tile after the current one.
		boolean noNextTile = false;

		// While the line(t) in String(Text) is less than the length of the String(Text)
		while (t < Text.length) {
			/**
			 * check if the tile is marked, if it is then increment it to the next line, so
			 * that the next unmarked tile can be the subject, it is determined as marked if
			 * the first index in the line matched '*', if t = 1, then we are on 2 now.
			 */
			if (!unMarked(Text[t])) {
				t = t + 1;
				continue;
			}

			/**
			 * we now have the unmarked token, we need to get the distance to the next tile,
			 * if it is equal to null then that means there is no next tile. Else, if it's
			 * not null then that means there is a next tile.
			 */
			int distanceToNextTile = 0;
			if (distanceToNextTile(Text, t) instanceof Integer) {
				distanceToNextTile = (int) distanceToNextTile(Text, t);
			} else {
				distanceToNextTile = Text.length - t;
				noNextTile = true;
			}

			/**
			 * If distanceToNextTile is less than searchLength do. If there is no next tile
			 * then set t to the text length. else, if there is no next unmarked token after
			 * tile then set t to the text length. Otherwise the method has returned an
			 * instance of an integer, this value is set to t. Else the distance is not less
			 * than the search length, thus...............................................
			 */
			if (distanceToNextTile < s) {
				if (noNextTile) {
					t = Text.length;
				} else {
					if (moveToFirstUnMarkedTokenAfterNextTile(Text, t) instanceof Integer) {
						t = (int) moveToFirstUnMarkedTokenAfterNextTile(Text, t);
					} else {
						t = Text.length;
					}
				}
			} else {
				/**
				 * Creates a KRHashValue for the substring (T[t] to T[t] + s - 1), this value is
				 * set to karpRabinHashValue, and then the value is added to the hashTable.
				 * finally t is incremented by 1 to go to next line in string.
				 */
				StringBuilder stringBuilder = new StringBuilder();
				for (int i = t; i <= t + s - 1; i++) {
					stringBuilder.append(Text[i]);
				}
				String subString = stringBuilder.toString();
				karpRabinHashValue = KRHashValue(subString);
				hashTable.add(karpRabinHashValue, t); // save(hashvalue, position)
				t = t + 1;
			}
		}

		Queue<TileValuesMatch> queue = new LinkedList<>();
		noNextTile = false;
		int p = 0;

		// While the line(p) in String(Pattern) is less than the length of the
		// String(Pattern)
		while (p < Pattern.length) {
			// Check if the tile is unmarked
			if (!unMarked(Pattern[p])) {
				p = p + 1;
				continue;
			}

			int distanceToNextTile;
			if (distanceToNextTile(Pattern, p) instanceof Integer) {
				distanceToNextTile = (int) distanceToNextTile(Pattern, p);
			} else {
				distanceToNextTile = Pattern.length - p;
				noNextTile = true;
			}

			if (distanceToNextTile < s) {
				if (noNextTile) {
					p = Pattern.length;
				} else {
					if (moveToFirstUnMarkedTokenAfterNextTile(Pattern, p) instanceof Integer) {
						p = (int) moveToFirstUnMarkedTokenAfterNextTile(Pattern, p);
					} else {
						p = Pattern.length;
					}
				}
			} else {
				StringBuilder stringBuilder = new StringBuilder();
				for (int i = p; i <= p + s - 1; i++) {
					stringBuilder.append(Pattern[i]);
				}
				String subString = stringBuilder.toString();
				karpRabinHashValue = KRHashValue(subString);
				ArrayList<Integer> values = hashTable.get(karpRabinHashValue);

				if (values != null) {
					for (Integer currentValue : values) {
						StringBuilder stringBuilder2 = new StringBuilder();
						for (int i = currentValue; i <= currentValue + s - 1; i++) {
							stringBuilder2.append(Text[i]);
						}

						if (stringBuilder2.toString().equals(subString)) {
							t = currentValue;
							int k = s;

							while (p + k < Pattern.length && t + k < Text.length && Pattern[p + k].equals(Text[t + k])
									&& unMarked(Pattern[p + k]) && unMarked(Text[t + k])) {
								k = k + 1;
							}

							if (k > 2 * s) {
								return k;
							} else {
								if (longestMaximalMatch < s) {
									longestMaximalMatch = s;
								}

								TileValuesMatch tileValuesMatch = new TileValuesMatch(p, t, k);
								queue.add(tileValuesMatch);
							}
						}
					}
				}
				p += 1;
			}
		}

		if (!queue.isEmpty()) {
			tVMATCHList.add(queue);
		}
		return longestMaximalMatch;
	}

	/**  */
	private static boolean isOccluded(ArrayList<TileValuesMatch> tiles, TileValuesMatch match) {
		if (tiles.isEmpty() || tiles == null) {
			return false;
		}
		for (TileValuesMatch matches : tiles) {
			if ((matches.patternPosition + matches.length == match.patternPosition + match.length)
					&& (matches.textPosition + matches.length == match.textPosition + match.length)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns true if the string(str) at a given index; is greater than 0 and does
	 * not contain '*' at index 0, which states that is has not been marked.
	 * Otherwise if it is marked then return false. !unMarked can be used to state
	 * that it has been marked.
	 */
	private static boolean unMarked(String str) {
		if (str.length() > 0 && str.charAt(0) != '*') {
			return true;
		} else {
			return false;
		}
	}

	/**  */
	private static Object distanceToNextTile(String[] strList, int position) {
		int distanceToNextTile = 0;
		// check if the index is the last index of the stringList (last line in string),
		// if so return null.
		if (position == strList.length) {
			return null;
		}
		while (1 + position + distanceToNextTile < strList.length
				&& unMarked(strList[position + distanceToNextTile + 1])) {
			distanceToNextTile += 1;
		}
		if (1 + position + distanceToNextTile == strList.length) {
			return null;
		}

		return distanceToNextTile + 1;
	}

	/**  */
	private static Object moveToFirstUnMarkedTokenAfterNextTile(String[] strList, int position) {
		Object distanceToNextTile = distanceToNextTile(strList, position);
		if (distanceToNextTile instanceof Integer) {
			position = position + (int) distanceToNextTile;
		} else {
			return null;
		}
		while (1 + position < strList.length && (!unMarked(strList[position + 1]))) {
			position = position + 1;
		}
		if (1 + position > strList.length - 1) {
			return null;
		}
		return position + 1;
	}

	/**
	 * Marks a string at a particular index with '*', as to state that it has been
	 * marked.
	 */
	private static String markToken(String str) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("*");
		stringBuilder.append(str);
		return stringBuilder.toString();
	}

	/**  */
	private static int KRHashValue(String subString) {
		int karpRabinHashValue = 0;
		for (int i = 0; i < subString.length(); i++) {
			karpRabinHashValue = ((karpRabinHashValue << 1) + (int) subString.charAt(i));
		}
		return karpRabinHashValue;
	}

}
