package tmc.eclipse.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import tmc.eclipse.tasks.TaskStarter;
import fi.helsinki.cs.tmc.core.Core;
import fi.helsinki.cs.tmc.core.domain.Course;
import fi.helsinki.cs.tmc.core.domain.Exercise;
import fi.helsinki.cs.tmc.core.ui.UserVisibleException;

public class ExerciseSelectorDialog extends Dialog {

    protected Object result;
    protected Shell shell;
    private Table table;
    private boolean buttonSelectsAll;
    private Button btnSelectAll;

    private Button btnDownload;
    private boolean showCompleted;

    /**
     * Create the dialog.
     * 
     * @param parent
     * @param style
     */
    public ExerciseSelectorDialog(Shell parent, int style) {
        super(parent, style);
        setText("Download exercises");

        this.showCompleted = false;
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

        table = new Table(shell, SWT.BORDER | SWT.CHECK | SWT.MULTI | SWT.V_SCROLL);
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
        lblSelectExercisesTo.setBounds(10, 10, 489, 17);
        lblSelectExercisesTo.setText("Exercises to download");

        btnSelectAll = new Button(shell, SWT.NONE);
        btnSelectAll.setBounds(398, 201, 111, 29);
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
        btnClose.setBounds(427, 236, 82, 29);
        btnClose.setText("Close");

        btnDownload = new Button(shell, SWT.NONE);
        btnDownload.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                downloadExercises();
                shell.close();
            }
        });
        btnDownload.setBounds(331, 236, 90, 29);
        btnDownload.setText("Download");

        try {
            Course currentCourse = Core.getCourseDAO().getCurrentCourse(Core.getSettings());

            for (Exercise e : getExercisesForCourse(currentCourse)) {
                addTableItem(e.getName());
            }

            updateSelectAllButtonState();
        } catch (UserVisibleException uve) {
        }

    }

    private void addTableItem(String itemToAdd) {
        TableItem tableItem = new TableItem(table, SWT.NONE);
        tableItem.setText(itemToAdd);
        tableItem.setChecked(true);
    }

    private boolean isAnySelected() {
        for (int i = 0; i < table.getItemCount(); i++) {
            if (table.getItem(i).getChecked()) {
                return true;
            }
        }
        return false;
    }

    private void downloadExercises() {
        final ArrayList<Exercise> list = new ArrayList<Exercise>();
        for (int i = 0; i < table.getItemCount(); i++) {
            if (table.getItem(i).getChecked()) {
                String exerciseName = table.getItem(i).getText();
                Course currentCourse = Core.getCourseDAO().getCurrentCourse(Core.getSettings());

                if (currentCourse != null) {
                    for (Exercise e : currentCourse.getExercises()) {
                        if (e.getName().equals(exerciseName)) {
                            list.add(e);
                        }
                    }
                }
            }
        }
        TaskStarter.startExerciseDownloadTask(list, new EclipseIdeUIInvoker(shell));
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
            btnDownload.setEnabled(true);
        } else {
            buttonSelectsAll = true;
            btnSelectAll.setText("Select all");
            btnDownload.setEnabled(false);
        }
    }

    private List<Exercise> getExercisesForCourse(Course course) {
        if (showCompleted) {
            return course.getCompletedDownloadableExercises();
        } else {
            return course.getDownloadableExercises();
        }
    }

    public void setShowCompleted(boolean showCompleted) {
        this.showCompleted = showCompleted;
    }

}
