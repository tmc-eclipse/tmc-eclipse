package tmc.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.ui.UserVisibleException;

public class LoginDialog extends Dialog {

    protected Object result;
    protected Shell shlTmcLogin;
    private Text userNameText;
    private Text passWordText;
    private Label errorMessage;

    /**
     * Create the dialog.
     * 
     * @param parent
     * @param style
     */
    public LoginDialog(Shell parent, int style) {
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

        errorMessage = new Label(shlTmcLogin, SWT.NONE);
        errorMessage.setBounds(10, 142, 293, 17);
        errorMessage.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
        errorMessage.setText("");

        userNameText = new Text(shlTmcLogin, SWT.BORDER);
        userNameText.setBounds(86, 47, 217, 27);
        userNameText.setText("");

        passWordText = new Text(shlTmcLogin, SWT.BORDER | SWT.PASSWORD);
        passWordText.setBounds(86, 80, 217, 27);
        passWordText.setText("");

        final Button btnSavePassword = new Button(shlTmcLogin, SWT.CHECK);
        btnSavePassword.setBounds(167, 113, 136, 24);
        btnSavePassword.setText("Save password");

        Button btnLogIn = new Button(shlTmcLogin, SWT.NONE);
        btnLogIn.setBounds(113, 176, 83, 29);
        btnLogIn.setText("Log in");
        btnLogIn.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                Core.getSettings().setUsername(userNameText.getText());
                Core.getSettings().setPassword(passWordText.getText());
                Core.getSettings().setSavePassword(btnSavePassword.getSelection());
                boolean success = true;
                try {
                    Core.getUpdater().updateCourses();
                } catch (UserVisibleException uve) {
                    errorMessage.setText("Authentication failed");
                    success = false;
                }
                if (success) {
                    close();
                }
            }
        });

        Button btnCancel = new Button(shlTmcLogin, SWT.NONE);
        btnCancel.setBounds(202, 176, 101, 29);
        btnCancel.setText("Cancel");
        btnCancel.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                close();
            }
        });

        btnSavePassword.setSelection(Core.getSettings().isSavePassword());
        userNameText.setText(Core.getSettings().getUsername());
        if (btnSavePassword.getSelection()) {
            passWordText.setText(Core.getSettings().getPassword());
        }
    }

    private void close() {
        shlTmcLogin.close();
    }
}
