package tmc.ui;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class SettingsDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text userNameText;
	private Text passWordText;
	private Text serverAddress;
	private Text text;

	public SettingsDialog(Shell parent, int style) {
		super(parent, style);
		setText("Settings");
	}

	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/* Generated by WindowBuilder-tool */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(550, 450);
		shell.setText(getText());
		
		Label lblErrorText = new Label(shell, SWT.NONE);
		lblErrorText.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		lblErrorText.setBounds(10, 10, 430, 17);
		lblErrorText.setText("ErrorLabel");
		
		Label lblUserName = new Label(shell, SWT.NONE);
		lblUserName.setBounds(10, 44, 77, 17);
		lblUserName.setText("Username");
		
		userNameText = new Text(shell, SWT.BORDER);
		userNameText.setBounds(154, 44, 259, 27);
		
		Label lblPassword = new Label(shell, SWT.NONE);
		lblPassword.setBounds(10, 83, 70, 17);
		lblPassword.setText("Password");
		
		passWordText = new Text(shell, SWT.BORDER);
		passWordText.setBounds(154, 77, 259, 27);
		
		Label lblServerAddress = new Label(shell, SWT.NONE);
		lblServerAddress.setText("Server Address");
		lblServerAddress.setBounds(10, 117, 123, 17);
		
		serverAddress = new Text(shell, SWT.BORDER);
		serverAddress.setBounds(154, 110, 386, 27);
		
		Button btnSavePassword = new Button(shell, SWT.CHECK);
		btnSavePassword.setBounds(419, 77, 131, 24);
		btnSavePassword.setText("Save Password");
		
		Label lblCurrentCourse = new Label(shell, SWT.NONE);
		lblCurrentCourse.setText("Current course");
		lblCurrentCourse.setBounds(10, 151, 123, 17);
		
		Combo combo = new Combo(shell, SWT.READ_ONLY);
		combo.setBounds(154, 143, 259, 29);
		
		Button btnRefreshCourses = new Button(shell, SWT.NONE);
		btnRefreshCourses.setBounds(419, 143, 121, 29);
		btnRefreshCourses.setText("Refresh");
		
		Label label = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(10, 188, 530, 2);
		
		Label lblFolder = new Label(shell, SWT.NONE);
		lblFolder.setText("Folder for projects");
		lblFolder.setBounds(10, 206, 138, 17);
		
		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.setBounds(449, 380, 91, 29);
		btnCancel.setText("Cancel");
		
		Button btnOk = new Button(shell, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnOk.setText("OK");
		btnOk.setBounds(350, 380, 91, 29);
		
		text = new Text(shell, SWT.BORDER | SWT.READ_ONLY);
		text.setBounds(154, 206, 259, 27);
		
		Button btnBrowse = new Button(shell, SWT.NONE);
		btnBrowse.setText("Browse...");
		btnBrowse.setBounds(419, 204, 121, 29);

	}
}
