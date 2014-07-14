package ru.darvell.blkp;

/**
 * Created with IntelliJ IDEA.
 * User: darvell
 * Date: 11.07.14
 * Time: 17:10
 * Class execute commands
 */
public class Worker extends Thread{

	Heap heap;
	Thread t;

	Worker(Heap heap){
		//System.out.println("worker create");
		this.heap = heap;
		t = this;
		t.setDaemon(true);
		t.start();
	}

	@Override
	public void run() {
		try{
			while(true){

				if(heap.getCount()>0){
					String command = heap.getCommand();
					System.out.println("Worker: "+command);
				}
				t.sleep(500);
			}
		}catch (Exception e){
			System.out.println("Error: "+e.getMessage());
		}
	}
}
