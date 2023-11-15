package presenters;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.*;

import com.itextpdf.text.DocumentException;
import models.GeneratePDF;
import models.MyProcess;
import models.OperatingSystem;
import views.AddProcessDialog;
import views.MainFrame;

public class Presenter implements ActionListener {

	private OperatingSystem operatingSystem;
	private MainFrame mainFrame;
	private AddProcessDialog addProcessDialog;
	private AddProcessDialog editProcessDialog;

	public Presenter() {
		operatingSystem = new OperatingSystem();
		initFrame();
	}

	private void initFrame(){
		mainFrame = new MainFrame(this);
		mainFrame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (Events.valueOf(e.getActionCommand())){
			case ADD:
				manageAddAction();
				break;
			case ACCEPT:
				manageAcceptAction();
				break;
			case CANCEL:
				manageCancelAction();
				break;
			case INIT:
				manageInitAction();
				break;
			case NEW_SIMULATION:
				manageNewSimulationAction();
				break;
			case EDIT:
				manageEditAction(e);
				break;
			case DELETE:
				manageDeleteAction(e);
				break;
			case ACCEPT_EDIT:
				manageAcceptEditAction(e);
				break;
			case CANCEL_EDIT:
				manageCancelEditAction();
				break;
			case EXPORT:
				manageExportAction();
				break;
			case EXIT:
				System.exit(0);
				break;
		}
	}

	private void manageAddAction() {
		addProcessDialog = new AddProcessDialog(this, false);
		addProcessDialog.setVisible(true);
	}

	private void manageAcceptAction() {
		try {
			operatingSystem.verifyProcessName(addProcessDialog.getProcessName());
			operatingSystem.addProcess(new MyProcess(addProcessDialog.getProcessName(), addProcessDialog.getProcessTime(),
													addProcessDialog.getIsBlocked()));
			addProcessDialog.dispose();
			mainFrame.updateProcesses(operatingSystem.getProcessQueue());
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(mainFrame, "Debe ingresar unicamente numeros", "ERROR!!!",
					JOptionPane.ERROR_MESSAGE);
		} catch (Exception ne){
			JOptionPane.showMessageDialog(mainFrame, ne.getMessage(), "ERROR!!!", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void manageCancelAction() {
		addProcessDialog.dispose();
	}

	private void manageInitAction() {
		if (!operatingSystem.getProcessQueue().isEmpty()){
			operatingSystem.startSimulation();
			mainFrame.initReportsPanel(operatingSystem.getReadyProccess(), operatingSystem.getProcessDespachados(),
					operatingSystem.getExecuting(), operatingSystem.getProcessToLocked(), operatingSystem.getProcessLocked(),
					operatingSystem.getProcessWakeUp(), operatingSystem.getProcessExpired(), operatingSystem.getProcessTerminated());
		}else{
			JOptionPane.showMessageDialog(mainFrame, "Debe haber almenos 1 proceso para poder iniciar la simulacion",
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void manageNewSimulationAction() {
		operatingSystem = new OperatingSystem();
		mainFrame.newSimulation();
	}

	private void manageDeleteAction(ActionEvent event) {
		String processName = ((JButton) event.getSource()).getName();
		operatingSystem.deleteProccess(processName);
		mainFrame.updateProcesses(operatingSystem.getProcessQueue());
	}

	private void manageEditAction(ActionEvent event) {
		String processName = ((JButton) event.getSource()).getName();
		editProcessDialog = new AddProcessDialog(this, true);
		MyProcess process = operatingSystem.search(processName);
		editProcessDialog.setInitialInfo(process.getName(), String.valueOf((int) process.getTime()), process.isLocked());
		editProcessDialog.setVisible(true);
	}

	private void manageAcceptEditAction(ActionEvent event) {
		try {
			String actualName = ((JButton) event.getSource()).getName();
			if(!actualName.equals(editProcessDialog.getProcessName())){
				operatingSystem.verifyProcessName(editProcessDialog.getProcessName());
			}
			operatingSystem.editProcess(actualName, editProcessDialog.getProcessName(),
										editProcessDialog.getProcessTime(), editProcessDialog.getIsBlocked());
			editProcessDialog.dispose();
			mainFrame.updateProcesses(operatingSystem.getProcessQueue());
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(mainFrame, "Debe ingresar unicamente numeros", "ERROR!!!",
					JOptionPane.ERROR_MESSAGE);
		} catch (Exception ne){
			JOptionPane.showMessageDialog(mainFrame, ne.getMessage(), "ERROR!!!", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void manageCancelEditAction() {
		editProcessDialog.dispose();
	}

	private void manageExportAction() {
		try {
			String fileName = mainFrame.getPdfPath();
			GeneratePDF.createPDF(fileName, operatingSystem.getReadyProccess(), operatingSystem.getProcessDespachados(),
					operatingSystem.getExecuting(), operatingSystem.getProcessToLocked(), operatingSystem.getProcessLocked(),
					operatingSystem.getProcessWakeUp(), operatingSystem.getProcessExpired(), operatingSystem.getProcessTerminated());
			JOptionPane.showMessageDialog(mainFrame, "Archivo PDF generado con exito", "PDF", JOptionPane.INFORMATION_MESSAGE);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
