package tmc.util;

import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.utils.ProjectIconHandler;

public class EclipseProjectIconHandler implements ProjectIconHandler{

    @Override
    public void updateIcon(Exercise e) {
        ProjectNatureHelper.updateTMCProjectNature(e);
    }

}
