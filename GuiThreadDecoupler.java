import javax.swing.SwingUtilities; 

/**
 * Toolclass to decouple the GUI-Thread with the execution
 */
public class GuiThreadDecoupler{
	
	private Runnable _runActionExecution;
	private Runnable _runActionGuiUpdate;
	private Thread _threadExecute;
	
	/**
	 * Constructor taking a runnable that is run decoupled from the GUI-Thread.
	 *
	 * @param runActionExecution IN: The action to perform in a different thread.
	 */
	public GuiThreadDecoupler(Runnable runActionExecution){
		 _runActionExecution= runActionExecution;
	}
	
    /**
	 * Starts the execution of the action passed by constructor
	 */
	public void startActionExecution(){
		_threadExecute= new Thread(new Runnable(){
			public void run(){
				//NOTE: Do try-catch because it is good style to do it!
				try{
					_runActionExecution.run();
				}
				catch (Exception ex){
					System.out.println("Exception in execution of thread:");
					ex.printStackTrace();
				}
			};
		});
		_threadExecute.start();
	}
	
	/**
	 * Starts an update of the GUI during the action execution.
	 *
	 * @param runActionGuiUpdate IN: The action to perform for GUI-Updating
	 * @param iWaitAfter IN: The milliseconds to wait after the GUI has been updated (can be used to ensure that GUI-Update has happened before the next action is done).
	 */
	public void startGuiUpdate(final Runnable runActionGuiUpdate,int iWaitAfter){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				//NOTE: Do try-catch because it is good style to do it!
				try{
					runActionGuiUpdate.run();
				}
				catch (Exception ex){
					System.out.println("Exception in updating the SWING-GUI:");
					ex.printStackTrace();
				}
			};
		});
		
		try {
			Thread.sleep(iWaitAfter);
		}catch(Exception ex){
			//Do nothing as it is only concerned for sleep.
		}
	}
	
	
}