package tmc.util;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;

import fi.helsinki.cs.plugin.tmc.io.FileUtil;

public class IProjectHelper {
	
	public static boolean projectWithThisFilePathExists(String path){
		for(IProject p: ResourcesPlugin.getWorkspace().getRoot().getProjects()){
		    
			if(p.getRawLocation() != null && path.contains(FileUtil.getUnixPath(p.getRawLocation().toString()))){
			    System.out.println("COMPARING: ");
			    System.out.println(FileUtil.getUnixPath(p.getRawLocation().toString()));
			    System.out.println(path);
				return true;
			}
		}
		return false;
	}
	
	public static IProject getProjectWithThisFilePath(String path){
for(IProject p: ResourcesPlugin.getWorkspace().getRoot().getProjects()){
            
            if(p.getRawLocation() != null && path.contains(FileUtil.getUnixPath(p.getRawLocation().toString()))){
                System.out.println("COMPARING: ");
                System.out.println(FileUtil.getUnixPath(p.getRawLocation().toString()));
                System.out.println(path);
                return p;
            }
        }
        return null;
	}
	
}
