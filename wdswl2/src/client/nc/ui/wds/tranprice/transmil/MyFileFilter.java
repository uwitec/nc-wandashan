package nc.ui.wds.tranprice.transmil;

import java.io.File;
import java.util.Hashtable;
import java.util.Enumeration;
import javax.swing.*;
import javax.swing.filechooser.*;

@SuppressWarnings( { "unchecked", "serial", "deprecation", "unused",
		"static-access" })
public class MyFileFilter extends javax.swing.filechooser.FileFilter {
	private static String TYPE_UNKNOWN = "Type Unknown";

	private static String HIDDEN_FILE = "Hidden File";

	private Hashtable filters = null;

	private String description = null;

	private String fullDescription = null;

	private boolean useExtensionsInDescription = true;

	/**
	 * MyFileFilter 构造子注解。
	 */
	public MyFileFilter() {
		super();
		this.filters = new Hashtable();
	}

	/**
	 * Whether the given file is accepted by this filter.
	 */
	public boolean accept(java.io.File f) {
		if (f != null) {
			if (f.isDirectory()) {
				return true;
			}
			String extension = getExtension(f);
			if (extension != null && filters.get(getExtension(f)) != null) {
				return true;
			}
			;
		}
		return false;
	}

	public void addExtension(String extension) {
		if (filters == null) {
			filters = new Hashtable(5);
		}
		filters.put(extension.toLowerCase(), this);
		fullDescription = null;
	}

	/**
	 * The description of this filter. For example: "JPG and GIF Images"
	 * 
	 * @see FileView#getName
	 */
	public String getDescription() {
		if (fullDescription == null) {
			if (description == null || isExtensionListInDescription()) {
				fullDescription = description == null ? "(" : description
						+ " (";
				// build the description from the extension list
				Enumeration extensions = filters.keys();
				if (extensions != null) {
					fullDescription += "." + (String) extensions.nextElement();
					while (extensions.hasMoreElements()) {
						fullDescription += ", ."
								+ (String) extensions.nextElement();
					}
				}
				fullDescription += ")";
			} else {
				fullDescription = description;
			}
		}
		return fullDescription;
	}

	public String getExtension(File f) {
		if (f != null) {
			String filename = f.getName();
			int i = filename.lastIndexOf('.');
			if (i > 0 && i < filename.length() - 1) {
				return filename.substring(i + 1).toLowerCase();
			}
			;
		}
		return null;
	}

	public boolean isExtensionListInDescription() {
		return useExtensionsInDescription;
	}

	/**
	 * The description of this filter. For example: "JPG and GIF Images"
	 * 
	 * @see FileView#getName
	 */
	public void setDescription(String description) {
		this.description = description;
		fullDescription = null;
	}

	public void setExtensionListInDescription(boolean b) {
		useExtensionsInDescription = b;
		fullDescription = null;
	}
}