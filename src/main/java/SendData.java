import be.derycke.pieter.com.COMException;
import jmtp.*;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SendData {
    public static void main(String[] args) {
        PortableDeviceManager manager = new PortableDeviceManager();
        PortableDevice device = manager.getDevices()[0];

        device.open();

        System.out.println("Appareil: " + device.getModel());

        System.out.println("---------------");
        System.out.println("Récupération de fichier depuis une tablette");

        // Iterate over deviceObjects
        for (PortableDeviceObject object : device.getRootObjects()) {
            // If the object is a storage object
            if (object instanceof PortableDeviceStorageObject) {
                PortableDeviceStorageObject storage = (PortableDeviceStorageObject) object;

                for (PortableDeviceObject o2 : storage.getChildObjects()) {
                    if(o2.getOriginalFileName().equals("copagazmobile")){

                        PortableDeviceFolderObject storage1 = (PortableDeviceFolderObject) o2;
                        for (PortableDeviceObject o3 : storage1.getChildObjects()) {
                            if(o3.getOriginalFileName().equals("backupdatabases")){
                                PortableDeviceFolderObject storage2 = (PortableDeviceFolderObject) o3;


                                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_hhmm");
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.YEAR, 1900);
                                Date data = calendar.getTime();
                                String idFile = null;
                                //fichier cible a copier
                                String fileName = null;
                                for (PortableDeviceObject o4 : storage2.getChildObjects()) {
                                    String dataArq = (o4.getOriginalFileName().substring(15, 28));

                                    try {
                                        if(new Date(format.parse(dataArq).getTime()).after(data)){
                                            data = new Date(format.parse(dataArq).getTime());
                                            idFile = o4.getID();
                                            fileName = o4.getOriginalFileName();
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                                //on recupere le backup de la base
                                PortableDeviceToHostImpl32 host =  new PortableDeviceToHostImpl32();
                                try {
                                    File f = new File( "c://sqlite//db");
                                    System.out.println("\n" +
                                            "Nettoyage du répertoire de gestion: " + f);
                                    //FileUtils.cleanDirectory(f);
                                    System.out.println("copie de fichier: " + fileName );
                                    host.copyFromPortableDeviceToHost(idFile, "c:/sqlite/db", device);
                                    f = new File( "c://sqlite//db//"+fileName);

                                    File novoArquivo = new File( "c://sqlite//db//copagaz_mobile_desenv.db");

                                    InputStream inStream = null;
                                    OutputStream outStream = null;
                                    inStream = new FileInputStream(f);
                                    outStream = new FileOutputStream(novoArquivo);

                                    byte[] buffer = new byte[1024];
                                    int length;
                                    while ((length = inStream.read(buffer)) > 0){
                                        outStream.write(buffer, 0, length);
                                    }
                                    inStream.close();
                                    outStream.close();
                                    f.delete();


                                } catch (COMException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();

                                System.out.println("Le dernier fichier provient de: " + data);
                            }
                        }
                    }
                }
            }
        }

        manager.getDevices()[0].close();
    }

}
}
