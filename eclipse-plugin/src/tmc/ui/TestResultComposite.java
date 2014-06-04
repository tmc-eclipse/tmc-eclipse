package tmc.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class TestResultComposite extends Composite {
    private Text colorBar;
    private Text text;

    /**
     * Create the composite.
     * 
     * @param parent
     * @param style
     */
    public TestResultComposite(Composite parent, int style) {
        super(parent, style);
        setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

        text = new Text(this, SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
        text.setBounds(5, 0, parent.getSize().x, 100);
        text.setText("aaaaaaaaaaaa\n" + "asdasd");
        GC gc = new GC(Display.getDefault());
        Point i = gc.stringExtent(text.getText());
        text.setSize(i);
        text.addPaintListener(new PaintListener() {
            @Override
            public void paintControl(PaintEvent e) {
                Point textSize = e.gc.stringExtent(text.getText());

                if (text.getText().contains("FAIL: ")) {
                    text.setBounds(5, 0, textSize.x + 5, textSize.y * 2);
                } else {
                    text.setBounds(5, 0, textSize.x + 5, textSize.y);
                }
                colorBar.setBounds(0, 0, 5, text.getSize().y);
            }
        });

        colorBar = new Text(this, SWT.READ_ONLY);
        colorBar.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));

        Button btnNewButton = new Button(this, SWT.NONE);
        btnNewButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            }
        });

        btnNewButton.setBounds(80, 106, 169, 29);
        btnNewButton.setText("Show detailed message");

    }

    public void setTestResultMessage(String message) {
        text.setText(message);
    }

    public Text getColorBar() {
        return colorBar;
    }

    public Text getText() {
        return text;
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
}
