package org.lionsoul.pview;

import java.io.File;
import java.io.FilenameFilter;

public class IFIleFIlter implements FilenameFilter {

	@Override
	public boolean accept(File dir, String name) {
		name = name.toLowerCase();
		if ( name.endsWith(".jpg")
				|| name.endsWith(".gif") || name.endsWith(".png")
				|| name.endsWith(".bmp"))
			return true;
		return false;
	}

}
