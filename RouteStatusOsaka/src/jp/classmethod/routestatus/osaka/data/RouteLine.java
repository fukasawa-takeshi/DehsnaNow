package jp.classmethod.routestatus.osaka.data;

import java.io.Serializable;
import java.util.ArrayList;

public class RouteLine implements Serializable {

	private static final long serialVersionUID = -5293835603413116884L;

	public String company;

	public ArrayList<LineInfo> lineInfo;

}
