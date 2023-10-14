import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class KampfStatTabelle {
  
   public  GladStat[] gladiatoren = { new GladStat("Atilla"), new GladStat("Arturo"), new GladStat("James"),
           new GladStat("Phil"), new GladStat("Sergio"), new GladStat("Luigi"), new GladStat("Andrea"),
           new GladStat("Angel"), new GladStat("Thiago"), new GladStat("Konstantin"), new GladStat("Spartakus"),
           new GladStat("Leonardo"), new GladStat("Ricardo"), new GladStat("Radja"), new GladStat("Joshua"),
           new GladStat("Anthony"), new GladStat("Christian"), new GladStat("Jordan"), new GladStat("Harry"),
           new GladStat("Killian") };

   private Arena arena;
   public GladStat _glad1;
   public GladStat _glad2;
   private int capacity = 20;
   private static int size = 20;
   private final String CSTR_TRENN_ZEICHEN= ";";

   public KampfStatTabelle() {

       arena = new Arena(null);
   }

   public void sortArray() {
       for (int i = 1; i < gladiatoren.length; i++) {
           for (int j = gladiatoren.length - 1; j >= i; j--) {
               if (gladiatoren[j] != null) {
                   if ((gladiatoren[j]).compareTo(gladiatoren[j - 1]) < 0) {
                       GladStat tausch = new GladStat("temp");
                       tausch = gladiatoren[j];
                       gladiatoren[j] = gladiatoren[j - 1];
                       gladiatoren[j - 1] = tausch;
                   }
               }
           }
       }
   }

   public void add(GladStat a) {
       if (capacity >= size) {
           GladStat[] copy = new GladStat[capacity * 2];

           for (int i = 0; i < gladiatoren.length; i++) {
               copy[i] = gladiatoren[i];
           }
           gladiatoren = copy;
           capacity= capacity*2;
           
       }
       gladiatoren[size] = a;
       size++;
       
       // Da das Array jetzt eine doppelte Länge hat und sehr viele Plätze mit null belegt sind, löschen wir diese freie Plätze
       GladStat[] remove = new GladStat[size];  
       for (int z = 0; z < remove.length; z++) {
           remove[z] = gladiatoren[z];
       }
       gladiatoren = remove;
   }

   public int suche(GladStat a) {
       for (int i = 0; i < gladiatoren.length; i++) {
           if (gladiatoren[i] == a) {
               return i;
           }
       }
       return -1;
   }

   public void remove(GladStat a) {
       GladStat[] remove = new GladStat[gladiatoren.length - 1];
       int index = suche(a);

       if (suche(a) == -1) {
           throw new RuntimeException(" Gladiator kann nicht gefunden werden ");
       }

       for (int i = 0; i < remove.length; i++) {
           if (i < index) {
               remove[i] = gladiatoren[i];
           } else {
               remove[i] = gladiatoren[i + 1];
           }
       }
       gladiatoren = remove;
       size--;
   }

   public void reset() {
       for (int i = 0; i < gladiatoren.length; i++) {
           gladiatoren[i].gemachteKämpfe = 0;
           gladiatoren[i].anzahlSiege = 0;
           gladiatoren[i].niederlagen = 0;
           gladiatoren[i].siegqoute = 0;
           gladiatoren[i].status = true;
       }
   }

   public void berechne(GladStat a, GladStat b) {
    
       if (a._le < 5 && a._le > 0) {
           a.niederlagen++;
           b.anzahlSiege++;
           b.gewonnen=true;

       } else if (b._le < 5 && b._le > 0) {
           b.niederlagen++;
           a.anzahlSiege++;
           a.gewonnen=true;
       }

       if (a._le <= 0) {
           a.niederlagen++;
           a.status = false;
           b.anzahlSiege++;
           b.gewonnen=true;
       } else if (b._le <= 0) {
           b.niederlagen++;
           b.status = false;
           a.anzahlSiege++;
           a.gewonnen=true;
       }

       a.gemachteKämpfe++;
       b.gemachteKämpfe++;

       if(a.niederlagen != 0){
           a.siegqoute =(double) a.anzahlSiege / (double) a.gemachteKämpfe * 100;
       }
       else{
           a.siegqoute = 100;
       }
       if(b.niederlagen != 0){
           b.siegqoute =(double) b.anzahlSiege / (double) b.gemachteKämpfe * 100;
       }
       else{
           b.siegqoute = 100;
       }
   }

   public void ladeDatei(File file) throws IOException {
            
       while (gladiatoren.length >= 1) {
                remove(gladiatoren[0]);
       }
        
       if(file.exists()==false) {
            throw new IOException("Datei '"+file+"' existiert nicht!");
       }
    
       BufferedReader br= null;
       try {
            FileReader fr=new FileReader(file);
            br=new BufferedReader(fr);

            String strZeile=br.readLine();
            while(strZeile!=null){
                verarbeiteZeile(strZeile);
                strZeile=br.readLine();
            }
       }
       catch (Exception ex) {
            ex.printStackTrace();
       }
       finally{
            try{  if(br!=null){br.close();} } 
            catch(IOException ioex){} 
       }
   }
   public void verarbeiteZeile(String strZeile){

        String [] strs= strZeile.split(CSTR_TRENN_ZEICHEN);	  
        if(strs.length!=6){
            throw new RuntimeException("Folgende Zeile kann nicht geparst werden, weil nicht sechs Angaben entstehen:" + strZeile);
        }
        
        int gemachteKämpfe=0, anzahlSiege=0, niederlagen=0;
        double siegqoute=0;
        boolean status=true;

        try{
            gemachteKämpfe=Integer.parseInt(strs[1].trim());
            anzahlSiege=Integer.parseInt(strs[2].trim());
            niederlagen=Integer.parseInt(strs[3].trim());
            siegqoute=Double.parseDouble(strs[4].trim());
            status=Boolean.parseBoolean(strs[5].trim());
        }
        catch(Exception ex){
            throw new RuntimeException("Folgende Zeile kann nicht geparst werden " + strZeile);
        }
        
        GladStat inListeEintragen= new GladStat(strs[0].trim());    
        inListeEintragen.anzahlSiege = anzahlSiege;
        inListeEintragen.gemachteKämpfe=gemachteKämpfe;
        inListeEintragen.niederlagen=niederlagen;
        inListeEintragen.siegqoute=siegqoute;
        inListeEintragen.status=status;
        this.add(inListeEintragen);
    }

   public void schreibeDatei(File file){
    if(file.exists()==true){
        System.out.println("Ueberschreibe existierende Datei "+file.toString());
    }
    
    BufferedWriter bw=null;
    try { 
         FileWriter fw = new FileWriter(file); 
         bw = new BufferedWriter(fw); 

         GladStat listeDesArrays [] = gladiatoren;

         for(int i=0; i<gladiatoren.length; i++){
            GladStat le =listeDesArrays[i];
            bw.write(le.name+CSTR_TRENN_ZEICHEN+le.getGemachteKämpfe() +CSTR_TRENN_ZEICHEN+le.getAnzahlSiege() +CSTR_TRENN_ZEICHEN+le.getNiederlagen() +CSTR_TRENN_ZEICHEN+le.getSieqquote() +CSTR_TRENN_ZEICHEN+le.getStatus());
            bw.newLine(); // Neue Zeile einfügen
         }
         bw.flush(); // Schreibt alles sicher weg
     } 
    catch (Exception ex) { 
        ex.printStackTrace(); 
    } 
    finally{
        try{
            if(bw!=null){bw.close();} 
        } 
        catch(IOException ioex){} //Tue nichts, wenn das eine Exception wirft!
    }
   }
}