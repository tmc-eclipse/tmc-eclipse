package fi.helsinki.cs.plugin.tmc.io.zipper.unzippingdecider;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import fi.helsinki.cs.plugin.tmc.domain.Project;

public abstract class AbstractUnzippingDecider implements UnzippingDecider {
	protected Project project;
	private List<String> doNotUnzip;

	public AbstractUnzippingDecider(Project project) {
		this.project = project;
		this.doNotUnzip = new ArrayList<String>();

		File file = new File(project.getRootPath() + "/.tmcproject.yml");

		if (!file.exists()) {
			return;
		}

		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNext()) {
				doNotUnzip.add(project.getRootPath() + "/" + scanner.next());
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean shouldUnzip(String filePath) {
		for (String s : doNotUnzip) {
			System.out.println(s);
			if (filePath.startsWith(s)
					&& (filePath.equals(s) || filePath.charAt(s.length()) == '/')) {
				return false;
			}
		}
		return true;
	}
}
