package tmc.ui;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Slider;

public class ExerciseSelectorDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Table table;
	private boolean buttonSelectsAll;
	private Button btnSelectAll;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public ExerciseSelectorDialog(Shell parent, int style) {
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

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setSize(515, 302);
		shell.setText(getText());
		
		table = new Table(shell, SWT.BORDER | SWT.CHECK | SWT.MULTI
				| SWT.V_SCROLL);
		table.setTouchEnabled(true);
		table.setLinesVisible(true);
		table.setBounds(10, 33, 482, 162);
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateSelectAllButtonState();
			}
		});
		
		Label lblSelectExercisesTo = new Label(shell, SWT.NONE);
		lblSelectExercisesTo.setBounds(10, 10, 469, 17);
		lblSelectExercisesTo.setText("Exercises to download");

		btnSelectAll = new Button(shell, SWT.NONE);
		btnSelectAll.setBounds(418, 201, 91, 29);
		updateSelectAllButtonState();
		btnSelectAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectUnselectAction();
			}
		});

		Button btnClose = new Button(shell, SWT.NONE);
		btnClose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		btnClose.setBounds(447, 236, 62, 29);
		btnClose.setText("Close");

		Button btnDownload = new Button(shell, SWT.NONE);
		btnDownload.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("TODO: Lataa valitut tehtävät.");
			}
		});
		btnDownload.setBounds(361, 236, 80, 29);
		btnDownload.setText("Download");

		//DUMMY DATA
		addTableItem("Tehtävä");
		addTableItem("Tehtävä");
		addTableItem("Tehtävä");
		addTableItem("Tehtävä");
		addTableItem("Tehtävä");
		addTableItem("Tehtävä");
		addTableItem("Tehtävä");
		addTableItem("Tehtävä");
		addTableItem("Tehtävä");
		addTableItem("Tehtävä");
		addTableItem("Tehtävä");
		addTableItem("Tehtävä");
		addTableItem("Tehtävä");
		addTableItem("Tehtävä");
		addTableItem("Tehtävä");
		addTableItem("Tehtävä");
		addTableItem("Tehtävä");
		addTableItem("Tehtävä");
		addTableItem("Tehtävä");
		addTableItem("Tehtävä");
		addTableItem("Tehtävä");
		addTableItem("Tehtävä");
		addTableItem("Tehtävä");
		addTableItem("Tehtävä");
		addTableItem("Tehtävä");
		addTableItem("Tehtävä");

	}

	private void addTableItem(String itemToAdd) {
		TableItem tableItem = new TableItem(table, SWT.NONE);
		tableItem.setText(itemToAdd);
	}

	private boolean isAnySelected() {
		for (int i = 0; i < table.getItemCount(); i++) {
			if (table.getItem(i).getChecked()) {
				return true;
			}
		}
		return false;
	}

	private void selectUnselectAction() {

		for (int i = 0; i < table.getItemCount(); i++) {
			if (buttonSelectsAll) {
				table.getItem(i).setChecked(true);
			} else {
				table.getItem(i).setChecked(false);
			}
		}
		updateSelectAllButtonState();

	}

	private void updateSelectAllButtonState() {
		if (isAnySelected()) {
			buttonSelectsAll = false;
			btnSelectAll.setText("Unselect all");
		} else {
			buttonSelectsAll = true;
			btnSelectAll.setText("Select all");
		}
	}

}