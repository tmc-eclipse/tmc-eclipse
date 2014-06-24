package tmc.eclipse.handlers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.ISources;

public class SubmitCommandState extends AbstractSourceProvider {
    public final static String MY_STATE = "fi.helsinki.cs.plugins.eclipse.commands.sourceprovider.active";
    public final static String ENABLED = "ENABLED";
    public final static String DISABLED = "DISABLED";
    private boolean enabled = true;

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

    @Override
    public Map<String, String> getCurrentState() {
        Map<String, String> map = new HashMap<String, String>(1);
        String value = enabled ? ENABLED : DISABLED;
        map.put(MY_STATE, value);
        return map;
    }

    @Override
    public String[] getProvidedSourceNames() {
        return new String[] {MY_STATE};
    }

    public void setState(boolean b) {
        String value = b ? ENABLED : DISABLED;
        fireSourceChanged(ISources.WORKBENCH, MY_STATE, value);
    }

}
