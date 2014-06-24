package tmc.eclipse.util;

import fi.helsinki.cs.tmc.core.domain.Exercise;
import fi.helsinki.cs.tmc.core.utils.ProjectIconHandler;

public class EclipseProjectIconHandler implements ProjectIconHandler{

    @Override
    public void updateIcon(Exercise e) {
        ProjectNatureHelper.updateTMCProjectNature(e);
    }

}
