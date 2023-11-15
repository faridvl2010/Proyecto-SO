package models;

import java.util.ArrayList;


public class OperatingSystem {

	private Queue<MyProcess> processQueueReady;
	private ArrayList<MyProcess> readyAndDespachado;
	private ArrayList<MyProcess> lockedAndWakeUp;
	private ArrayList<MyProcess> executing;
	private ArrayList<MyProcess> expired;
	private ArrayList<MyProcess> processTerminated;

	public OperatingSystem() {
		this.processQueueReady = new Queue<>();
		this.lockedAndWakeUp = new ArrayList<>();
		this.processTerminated = new ArrayList<>();
		executing = new ArrayList<>();
		expired = new ArrayList<>();
		readyAndDespachado = new ArrayList<>();
	}

	public boolean addProcess(MyProcess myProcess) {
		if (search(myProcess.getName()) == null) {
			readyAndDespachado.add(new MyProcess(myProcess.getName(), myProcess.getTime(), myProcess.isLocked()));
			processQueueReady.push(myProcess);
			return true;
		}
		return false;
	}

	/**
	 * Me avisa si no funciona, xd
	 * @param actualName
	 * @param name
	 * @param time
	 * @param lockedStatus
	 */
	public void editProcess(String actualName, String name, int time, boolean lockedStatus) {
		edit(search(actualName), name, time, lockedStatus);
		edit(searchInList(actualName, readyAndDespachado), name, time, lockedStatus);
	}
	
	private void edit(MyProcess myProcess, String name, int time, boolean lockedStatus) {
		myProcess.setName(name);
		myProcess.updateTime(time);
		myProcess.setLocked(lockedStatus);
	}
	
	/**
	 * Eliminar de la cola y de la lista de listos
	 * @param name
	 * @return
	 */
	public boolean deleteProccess(String name) {
		boolean isDelete = false;
		Node<MyProcess> temp = processQueueReady.peek();
		readyAndDespachado.remove(searchInList(name, readyAndDespachado));
		if (temp.getData().getName().equals(name)) {
			processQueueReady.pop();
			isDelete = true;
		}else {
			isDelete = deleteProcess(name, isDelete, temp);
		}	
		return isDelete;
	}

	private boolean deleteProcess(String name, boolean isDelete, Node<MyProcess> temp) {
		while (temp.getNext() != null) {
			if (temp.getNext().getData().getName().equals(name)) {
				temp.setNext(temp.getNext().getNext());
				isDelete = true;
			} else {
				temp = temp.getNext();
			}
		}
		return isDelete;
	}

	private MyProcess searchInList(String name, ArrayList<MyProcess> myProcesses) {
		for (MyProcess myProcess : myProcesses) {
			if (name.equals(myProcess.getName())) {
				return myProcess;
			}
		}
		return null;
	}
	
	
	public MyProcess search(String name) {
		Node<MyProcess> temp = processQueueReady.peek();
		while (temp != null) {
			if (temp.getData().getName().equals(name)) {
				return temp.getData();
			} else {
				temp = temp.getNext();
			}
		}
		return null;
	}

	public void startSimulation() {
		while (!processQueueReady.isEmpty()) {
			MyProcess process = processQueueReady.peek().getData();
			valideSystemTimer(process);
		}
	}

	private void valideSystemTimer(MyProcess process) {
		executing.add(new MyProcess(process.getName(), (process.getTime()-5< 0 ? 0:process.getTime()-5), process.isLocked()));
		if ((process.getTime() - 5) > 0) {
			proccessTimeDiscount(process);
		} else {
			MyProcess myProcess = processQueueReady.pop();
			myProcess.setTime((int)myProcess.getTime());
			processTerminated.add(myProcess);
		}
	}

	private void proccessTimeDiscount(MyProcess process) {
		process.setTime(5);
		valideLocked(process);
		readyAndDespachado.add(new MyProcess(process.getName(), process.getTime(), process.isLocked()));
		processQueueReady.push(processQueueReady.pop());
	}

	private void valideLocked(MyProcess process) {
		if (process.isLocked()) {
			lockedAndWakeUp.add(new MyProcess(process.getName(), process.getTime(), process.isLocked()));
		} else {
			expired.add(new MyProcess(process.getName(), process.getTime(), process.isLocked()));
		}
	}

	/**
	 * 
	 * @return Los procesos que se van agregando a la lista, estos toca ir actualizando
	 * cada que se agregan a la interfaz
	 */
	public Queue<MyProcess> getProcessQueue() {
		return processQueueReady;
	}

	public void verifyProcessName(String name) throws Exception {
		Node<MyProcess> temp = processQueueReady.peek();
		boolean aux = true;
		while(temp != null){
			if(temp.getData().getName().equals(name)){
				throw new Exception("Nombre de proceso no disponible");
			}
			temp = temp.getNext();
		}
	}


	public ArrayList<MyProcess> getProcessQueueLocked() {
		return lockedAndWakeUp;
	}

	/**
	 * 
	 * @return Procesos terminados
	 */
	public ArrayList<MyProcess> getProcessTerminated() {
		return processTerminated;
	}

	/**
	 * 
	 * @return Lista de los procesos listos
	 */
	public ArrayList<MyProcess> getReadyProccess() {
		return readyAndDespachado;
	}

	/**
	 * 
	 * @return Procesos despachados
	 */
	public ArrayList<MyProcess> getProcessDespachados() {
		return readyAndDespachado;
	}

	/**
	 * 
	 * @return  Processos en ejecucion
	 */
	public ArrayList<MyProcess> getExecuting() {
		return executing;
	}

	/**
	 * 
	 * @return Procesos expirados
	 */
	public ArrayList<MyProcess> getProcessExpired() {
		return expired;
	}

	/**
	 * 
	 * @return Los que pasan a bloqueado
	 */
	public ArrayList<MyProcess> getProcessToLocked() {
		return lockedAndWakeUp;
	}

	/**
	 * 
	 * @return Porcesos bloqueados
	 */
	public ArrayList<MyProcess> getProcessLocked() {
		return lockedAndWakeUp;
	}

	/**
	 * 
	 * @return Procesos despertados
	 */
	public ArrayList<MyProcess> getProcessWakeUp() {
		return lockedAndWakeUp;
	}

	public static Object[][] processInfo(ArrayList<MyProcess> processes){
		Object[][] processInfo = new Object[processes.size()][3];
		for (int i = 0; i < processes.size(); i++) {
			processInfo[i][0] = processes.get(i).getName();
			processInfo[i][1] = processes.get(i).getTime();
			processInfo[i][2] = processes.get(i).isLocked();
		}
		return processInfo;
	}
}
