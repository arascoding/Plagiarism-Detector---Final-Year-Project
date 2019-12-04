
/**
 * @author Aras Abdo
 *
 */

/** Tiles with matched values will be stored in this class */
public class TileValuesMatch {
	public final int patternPosition;
	public final int textPosition;
	public final int length;

	public TileValuesMatch(int pat, int txt, int len) {
		super();
		this.patternPosition = pat;
		this.textPosition = txt;
		this.length = len;
	}
}
