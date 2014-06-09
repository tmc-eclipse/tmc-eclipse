package fi.helsinki.cs.plugin.tmc.io.zipper.unzippingdecider;

import fi.helsinki.cs.plugin.tmc.domain.Project;

public class DefaultUnzippingDecider extends AbstractUnzippingDecider {

    public DefaultUnzippingDecider(Project project) {
    	super(project);
    }

    @Override
    public boolean shouldUnzip(String filePath) {
		String s = project.getRootPath() + "/src";
		if (filePath.startsWith(s)
				&& (filePath.equals(s) || filePath.charAt(s.length())  == '/')) {
			return false;
		}
        return super.shouldUnzip(filePath);
    }

}
