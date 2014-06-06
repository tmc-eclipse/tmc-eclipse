package tmc.ui;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;

public class PastebinResultDialog extends Dialog {

	protected Object result;
	protected Shell shlPastebinNotification;
	private Text text;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public PastebinResultDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlPastebinNotification.open();
		shlPastebinNotification.layout();
		Display display = getParent().getDisplay();
		while (!shlPastebinNotification.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlPastebinNotification = new Shell(getParent(), getStyle());
		shlPastebinNotification.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		shlPastebinNotification.setSize(450, 210);
		shlPastebinNotification.setText("Pastebin notification\r\n");

		Label lblCodeSubmittedTo = new Label(shlPastebinNotification, SWT.NONE);
		lblCodeSubmittedTo.setFont(SWTResourceManager.getFont(
				"Microsoft Sans Serif", 12, SWT.NORMAL));
		lblCodeSubmittedTo.setBounds(10, 10, 254, 19);
		lblCodeSubmittedTo.setText("Code submitted to TMC pastebin.");

		Button btnNewButton = new Button(shlPastebinNotification, SWT.NONE);
		btnNewButton.setFont(SWTResourceManager.getFont("Microsoft Sans Serif",
				12, SWT.NORMAL));
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnNewButton.setBounds(10, 35, 90, 33);
		btnNewButton.setText("View paste");

		text = new Text(shlPastebinNotification, SWT.BORDER);
		text.setBounds(10, 74, 424, 25);

		Button btnNewButton_1 = new Button(shlPastebinNotification, SWT.NONE);
		btnNewButton_1.setFont(SWTResourceManager.getFont(
				"Microsoft Sans Serif", 12, SWT.NORMAL));
		btnNewButton_1.setBounds(384, 105, 50, 33);
		btnNewButton_1.setText("Ok");

		Button btnNewButton_2 = new Button(shlPastebinNotification, SWT.NONE);
		btnNewButton_2.setFont(SWTResourceManager.getFont(
				"Microsoft Sans Serif", 12, SWT.NORMAL));
		btnNewButton_2.setBounds(300, 144, 134, 31);
		btnNewButton_2.setText("Copy to clipboard");

	}
}
