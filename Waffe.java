public class Waffe {
    enum Waffentyp {HAND,KURZSCHWERT,SCHWERT,SPEER,DREIZACK,PEITSCHE}

    private Waffentyp _typ;

    private Waffe(Waffentyp typ){
       this. _typ=typ;
    }
   
    int calcTrefferPunkte(){
         Wuerfel w6 = new Wuerfel((int)(Math.random()*6+1));
         
       switch(_typ){
           case HAND : return w6.wuerfle();                  // Kein BREAK, weil der Switch wegen return verlassen wird
           case KURZSCHWERT: return w6.wuerfle()+2;          // andere Methode: if (_typ==Waffentyp.KURZSCHWERT) {return ...};
           case SCHWERT : return w6.wuerfle()+4;             // Bei switch-case muss man keinen enum-Namen (Waffentyp) aufschreiben
           case SPEER: return w6.wuerfle()+3;
           case DREIZACK: return w6.wuerfle()+5;
           case PEITSCHE: return w6.wuerfle()+1;
       }
       return 10;
    }
    public static Waffe createWaffe() {
        Wuerfel w6 = new Wuerfel((int)(Math.random()*6+1));

        int a = w6.wuerfle();
        
        switch(a){
            case 1: return new Waffe(Waffentyp.HAND);
            case 2: return new Waffe(Waffentyp.KURZSCHWERT);
            case 3: return new Waffe(Waffentyp.SCHWERT);
            case 4: Waffe b = new Waffe(Waffentyp.SPEER);
                 return b;
            case 5: return new Waffe (Waffentyp.DREIZACK);
            case 6: return new Waffe (Waffentyp.PEITSCHE);
        }
        return new Waffe(Waffentyp.DREIZACK);
    }
}