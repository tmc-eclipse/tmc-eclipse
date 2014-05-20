package tmc.ui;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;

public class LoginDialog extends Dialog {

	protected Object result;
	protected Shell shlTmcLogin;
	private Text text;
	private Text text_1;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public LoginDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlTmcLogin.open();
		shlTmcLogin.layout();
		Display display = getParent().getDisplay();
		while (!shlTmcLogin.isDisposed()) {
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
		shlTmcLogin = new Shell(getParent(), SWT.CLOSE | SWT.MIN | SWT.MAX | SWT.TITLE | SWT.APPLICATION_MODAL);
		shlTmcLogin.setSize(326, 233);
		shlTmcLogin.setText("TMC Login");
		
		Label lblNewLabel = new Label(shlTmcLogin, SWT.NONE);
		lblNewLabel.setForeground(SWTResourceManager.getColor(76, 76, 76));
		lblNewLabel.setFont(SWTResourceManager.getFont("Ubuntu", 11, SWT.NORMAL));
		lblNewLabel.setBounds(10, 10, 109, 17);
		lblNewLabel.setText("Log in to TMC");
		
		Label lblUsername = new Label(shlTmcLogin, SWT.NONE);
		lblUsername.setBounds(10, 57, 70, 17);
		lblUsername.setText("Username");
		
		Label lblPassword = new Label(shlTmcLogin, SWT.NONE);
		lblPassword.setBounds(10, 90, 70, 17);
		lblPassword.setText("Password");
		
		text = new Text(shlTmcLogin, SWT.BORDER);
		text.setBounds(86, 47, 217, 27);
		
		text_1 = new Text(shlTmcLogin, SWT.BORDER | SWT.PASSWORD);
		text_1.setBounds(86, 80, 217, 27);
		
		Button btnSavePassword = new Button(shlTmcLogin, SWT.CHECK);
		btnSavePassword.setBounds(167, 113, 136, 24);
		btnSavePassword.setText("Save password");
		
		Button btnLogIn = new Button(shlTmcLogin, SWT.NONE);
		btnLogIn.setBounds(144, 160, 63, 29);
		btnLogIn.setText("Log in");
		
		Button btnCancel = new Button(shlTmcLogin, SWT.NONE);
		btnCancel.setBounds(212, 160, 91, 29);
		btnCancel.setText("Cancel");

	}
}
