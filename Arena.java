public class Arena {

	private static String[] gladiatorenNamen = {"Atilla","Arturo","James","Phil","Sergio","Luigi","Andrea","Angel","Thiago","Konstantin","Spartakus","Leonardo","Ricardo","Radja","Joshua","Anthony","Christian","Jordan","Harry","Killian"};
	private GladStat _glad1;
	private GladStat _glad2;
	private IReporter _reporter;

	public Arena(IReporter rep){
		_reporter=rep;
		
		Wuerfel w20 = new Wuerfel((int)(Math.random()*20)); // Objekt Würfel aus der anderen Klasse erzeugen
		Wuerfel w21 = new Wuerfel((int)(Math.random()*20));
		String glad1=gladiatorenNamen[w20.wuerfle()];
		String glad2=gladiatorenNamen[w21.wuerfle()];
		
		_glad1 = new GladStat(glad1);		// Objekt erzeugen, um Klasse Gladiator aufzurufen
		_glad2 = new GladStat(glad2);
	}

	public void neuerGlad(GladStat glad1, GladStat glad2){
		_glad1=glad1;
		_glad2=glad2;
	}
	
	boolean starteKampfRunde() {
		
		if (_glad1.attacke() == true) {
			_reporter.giveNewMessage(" X ");
			
			if (_glad2.parade() == false){
				_reporter.giveNewMessage("\t - ");
				int a = _glad2.calcTrefferPunkte();
				_reporter.giveNewMessage("\t\t" + a+ " TP");
				if (_glad2.nehmeSchaden(a)== false){
					return false;
				}
			}
			else{
				_reporter.giveNewMessage("\t O ");
			}
		}
		
		else{
			_reporter.giveNewMessage(" - ");
			if (_glad2.parade() == false){
				_reporter.giveNewMessage("\t - ");
			}
			else{
				_reporter.giveNewMessage("\t O ");	
			}
		}
		
		if (_glad2.attacke() == true) {
			if (_glad1.parade() == false){
				_reporter.giveNewMessage(" - ");
				int b = _glad1.calcTrefferPunkte();		
				_reporter.giveNewMessage("\t\t" + b + " TP");
				if (_glad1.nehmeSchaden(b)== false){
					return false;
				}
			}
			else{
				_reporter.giveNewMessage(" O ");
			}	
			_reporter.giveNewMessage("\t X ");		
		}
		else {
			if (_glad1.parade() == false){
				_reporter.giveNewMessage(" - ");
			}
			else{
				_reporter.giveNewMessage(" O ");
			}
			_reporter.giveNewMessage("\t - ");
		}
		return true;
	}
	
	void starteKampf() {

		while (starteKampfRunde()==true ){}
		_reporter.giveNewMessage("Der Kampf ist beendet");
	} 
	public static void main (String [] args) {
		
		Arena Kampf1 = new Arena (new IReporter(){
			@Override
			public void giveNewMessage(String str) {
				System.out.println(str);
			}
		});
		
		Kampf1.starteKampf();
	}
}