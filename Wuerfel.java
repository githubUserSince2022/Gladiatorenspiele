public class Wuerfel {
	public int _seiten;

	int wuerfle(){
		
		if(_seiten == (int)(Math.random() * 6+1)) {
			return _seiten;
		}
		else if(_seiten == (int)(Math.random() * 20+1)) {
			return _seiten;
		}
		return _seiten;
	}

	public Wuerfel(int seiten) {
		this._seiten= seiten;
	}
}