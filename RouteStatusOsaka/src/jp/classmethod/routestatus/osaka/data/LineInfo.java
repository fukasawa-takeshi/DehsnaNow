package jp.classmethod.routestatus.osaka.data;

import java.io.Serializable;

public class LineInfo implements Serializable {

	private static final long serialVersionUID = 7784963865390006248L;

	public String id;
	public String lineName;
	public String searchWord;
	public String companyName;
	public boolean isFavorite;
	public String url;
}
