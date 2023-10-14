public class GladStat implements Comparable<GladStat>{

	private static String[] gladiatorenNamen= {"Atilla","Arturo","James","Phil","Sergio","Luigi","Andrea","Angel","Thiago","Konstantin","Spartakus","Leonardo","Ricardo","Radja","Joshua","Anthony","Christian","Jordan","Harry","Killian"};
    public String name;
    public int gemachteKämpfe;
    public int anzahlSiege;
    public int niederlagen;
    public double siegqoute;         // siegquote= siege/niederlagen
	public boolean status=true;            // false=tot oder true=lebendig 
	private int _at;			// Attackewert
	private int _pa;			// Paradewert
	private int _rs;			// Rüstschutz
	public int _le;			// Lebensenergie
	private static IReporter _reporter;
	public boolean gewonnen=false;
	private Waffe _waffe;
	private static Wuerfel w6 =new Wuerfel((int)(Math.random()*6+1));

		public GladStat(String name) {
			this.name = name;
			Wuerfel w6 = new Wuerfel((int) (Math.random() * 6 + 1));
			this._at = _at;
			_at = 5 + w6.wuerfle();
			this._pa = _pa;
			_pa = 5 + w6.wuerfle();
			this._rs = _rs;
			_rs = 0;
			this._le = _le;
			_le = 30 + w6.wuerfle();
			this._waffe=_waffe;
			_waffe=Waffe.createWaffe();
		}
		public int calcTrefferPunkte() {			
			return _waffe.calcTrefferPunkte();		
		}

		boolean attacke() {

			int attacke = (int) (Math.random() * 20) + 1;
			if (attacke <=  5+ w6.wuerfle() ) {
				return true;
			} else {
				return false;
			}
		}

		boolean parade() {

			int parade = (int) (Math.random() * 20) + 1;

			if (parade <= 5+ w6.wuerfle()) {
				return true;
			} else {
				return false;
			}
		}

		boolean nehmeSchaden(int tp) {
			// tp:Schadenspunkte
			// le: Leben
			// Rüstung
			// tp -= _rs;
			tp -= _rs;
			_le -= tp; 
			
			if (_le < 5 && _le > 0) {
				System.out.println(name + " ist kampfunfaehig ");
				boolean verloren=true;
				return false;
			} else if (_le <= 0) {

				System.out.println(name + " ist tot");
				boolean verloren=true;
				return false;
			} else {
				boolean verloren=false;
				return true;
			}
		}
		@Override
		public int compareTo(GladStat gladiatoren) {

			if (gladiatoren.anzahlSiege > this.anzahlSiege) {
				return 1;
			} else if (gladiatoren.anzahlSiege < this.anzahlSiege) {
				return -1;
			}
			if (gladiatoren.niederlagen > this.niederlagen) {
				return -1;
			} else if (gladiatoren.niederlagen < this.niederlagen) {
				return 1;
			}
			if (gladiatoren.anzahlSiege == this.anzahlSiege) {
				if (gladiatoren.siegqoute > this.siegqoute) {
					return 1;
				} else if (gladiatoren.siegqoute < this.siegqoute) {
					return -1;
				}
			}
			if (gladiatoren.siegqoute == this.siegqoute) {
				if (gladiatoren.status == true) {
					if (this.status == false) {
						return 1;
					}
				} else if (this.status == true) {
					if (gladiatoren.status == false) {
						return -1;
					}
				}
			}
			if (gladiatoren.status == this.status) {
				int vergleich = this.name.compareTo(gladiatoren.name);
				return vergleich;
			}
			return 4;
		}

		@Override
		public String toString() {

			String s =" " + name;
			return s;
		}
		public String getName(){
			return getName();
		}
		public int getGemachteKämpfe(){
			return gemachteKämpfe;
		}
		public int getAnzahlSiege(){
			return anzahlSiege;
		}
		public int getNiederlagen(){
			return niederlagen;
		}
		public double getSieqquote(){
			return siegqoute;
		}
		public boolean getStatus(){
			return status;
		}

}