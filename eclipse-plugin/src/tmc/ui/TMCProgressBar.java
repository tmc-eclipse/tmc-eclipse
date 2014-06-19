package tmc.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class TMCProgressBar extends Label {

    private int selection;
    private int minimum = 0;
    private int maximum = 0;

    public TMCProgressBar(Composite parent) {
        super(parent, SWT.CENTER);
    }

    public void setSelection(int selection) {
        if (selection <= minimum) {
            this.selection = minimum;
        } else if (selection > maximum) {
            this.selection = maximum;
        }
        this.selection = selection;
    }

    public int getSelection() {
        return selection;
    }

    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }

    public int getMinimum() {
        return minimum;
    }

    public void setMaximum(int maximum) {
        this.maximum = maximum;
    }

    public int setMinimum() {
        return maximum;
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
}
