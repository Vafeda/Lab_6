package Collection;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.TreeMap;


public class Collection extends TreeMap {

    private java.time.LocalDateTime changeDate;

    public Collection() {
        super();
    }
    public boolean CreateFromFile(String file_name) throws IOException{
        File file = new File(file_name);
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        while (true) {
            String fline = br.readLine();
            if (fline == null) break;
            String fields[] = fline.split(";");
            if (fields.length == 7) {
                Organization org = new Organization(Long.parseLong(fields[0]), fields[1]);
                org.SetType(fields[5]);
                org.GetOfficialAddress().SetZipCode(fields[6]);
                org.GetCoordinates().SetX(fields[2]);
                org.GetCoordinates().SetY(fields[3]);
                org.SetAnnualTurnover(fields[4]);
                this.put(org.GetID(), org);
            }
        }
        fis.close();
        return false;
    }
    public boolean WriteToFile (String file_name) throws FileNotFoundException {

        File file_out = new File(file_name);
        PrintWriter pw = new PrintWriter(file_out);
        Organization org;
        if ( this.size() > 0 ) {
            for (Iterator itr = this.keySet().iterator(); itr.hasNext(); ) {
                org = (Organization) this.get(itr.next());
                if (org != null) {
                    pw.write(org.GetID() + ";");
                    pw.write(org.GetName() + ";");
                    pw.write(org.GetCoordinates().GetX() + ";");
                    pw.write(org.GetCoordinates().GetY() + ";");
//                    pw.write(org.GetCreationDate() + ";");
                    pw.write(org.GetAnnualTurnover() + ";");
                    pw.write(org.GetType() + ";");
                    pw.write(org.GetOfficialAddress().GetZipCode() + ";");
                    pw.write("\n");
                }
            }
        }
        pw.flush();
        pw.close();

        return true;
    }

    public void SetChangeDate(){
        this.changeDate = LocalDateTime.now();
    }
}
