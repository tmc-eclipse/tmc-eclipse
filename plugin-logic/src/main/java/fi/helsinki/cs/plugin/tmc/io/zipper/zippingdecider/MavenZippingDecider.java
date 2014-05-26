package fi.helsinki.cs.plugin.tmc.io.zipper.zippingdecider;

import java.util.regex.Pattern;

public class MavenZippingDecider extends AbstractZippingDecider {

    private static final Pattern rejectPattern = Pattern.compile("^[^/]+/(target|lib/testrunner)/.*");

   /* public MavenZippingDecider(TmcProjectInfo projectInfo) {
        super(projectInfo);
    }*/

    @Override
    public boolean shouldZip(String zipPath) {
        if (!super.shouldZip(zipPath)) {
            return false;
        }

        return !rejectPattern.matcher(zipPath).matches();
    }
}
