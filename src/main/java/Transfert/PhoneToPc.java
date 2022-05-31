package Transfert;

import be.derycke.pieter.com.COMException;
import jmtp.*;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PhoneToPc {

    private  String pathdel = System.getProperty("user.dir")+"/1.225_172303_8349967972327207504";
    MTPFileManager mtpFileManager = new MTPFileManager();

    public static void main(String[] args) {
        PhoneToPc phoneToPc = new PhoneToPc();
        //phoneToPc.TransfertPhoto();
        //phoneToPc.TransfertDb();
    }

    public void TransfertPhoto(String nomAppareil, String path){
        PortableDeviceManager manager = new PortableDeviceManager();
       // PortableDevice device = manager.getDevices()[0];
        PortableDevice device  = null;
        // TEST///////////////////////////////
        for (PortableDevice portableDevice :  manager.getDevices()){
            portableDevice.open();
            if(portableDevice.getModel().equals(nomAppareil)){
                device = portableDevice;
            }
            portableDevice.close();
        }
        // TEST///////////////////////////////

        //device.open();
        // Iterate  deviceObjects
        for (PortableDeviceObject object: device.getRootObjects()) {

            if (object instanceof PortableDeviceStorageObject) {

                //racine
                PortableDeviceStorageObject storage = (PortableDeviceStorageObject) object;
                System.out.println("////PHOTOS" + storage.getDescription());

                for (PortableDeviceObject o2: storage.getChildObjects()) {

                    if (o2.getOriginalFileName().equals("Android")) {


                        PortableDeviceFolderObject storage1 = (PortableDeviceFolderObject) o2;
                        for (PortableDeviceObject o3: storage1.getChildObjects()) {
                            if (o3.getOriginalFileName().equals("data")) {


                                PortableDeviceFolderObject storage2 = (PortableDeviceFolderObject) o3;
                                for (PortableDeviceObject o4: storage2.getChildObjects()) {
                                    if (o4.getOriginalFileName().equals("com.example.planteinvasives")) {


                                        PortableDeviceFolderObject storage3 = (PortableDeviceFolderObject) o4;
                                        for (PortableDeviceObject o5: storage3.getChildObjects()) {
                                            if (o5.getOriginalFileName().equals("files")) {


                                                PortableDeviceFolderObject storage4 = (PortableDeviceFolderObject) o5;
                                                //parcours tout les fichiers du path
                                                for (PortableDeviceObject o6: storage4.getChildObjects()) {
                                                    if(o6.getOriginalFileName().equals("Pictures")){
                                                        PortableDeviceFolderObject storage5 = (PortableDeviceFolderObject) o6;

                                                        for (PortableDeviceObject o7: storage5.getChildObjects()) {
                                                            System.out.println(o7.getOriginalFileName());
                                                            //copie des images
                                                            PortableDeviceToHostImpl32 host =  new PortableDeviceToHostImpl32();
                                                            try {
                                                                System.out.println("copie de l'image =>"+o7.getOriginalFileName());
                                                                host.copyFromPortableDeviceToHost(o7.getID(),path,device);
                                                            } catch (COMException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                   device.close();
                }
            }
        }
    }

    public void TransfertDb(String nomAppareil, String path){

        int verrou = 0;
        PortableDeviceManager manager = new PortableDeviceManager();
        //PortableDevice device = manager.getDevices()[0];
        PortableDevice device  = null;
        // TEST///////////////////////////////
        for (PortableDevice portableDevice :  manager.getDevices()){
            portableDevice.open();
            if(portableDevice.getModel().equals(nomAppareil)){
                device = portableDevice;
            }
            portableDevice.close();
        }
        // TEST///////////////////////////////
        System.out.println("-------BDD--------");
        System.out.println("Tentative de connexion");

        // Iterate  deviceObjects
        for (PortableDeviceObject object: device.getRootObjects()) {

            if (object instanceof PortableDeviceStorageObject) {

                //racine
                PortableDeviceStorageObject storage = (PortableDeviceStorageObject) object;
                System.out.println("///COPIE BDD" + storage.getDescription());

                for (PortableDeviceObject o2: storage.getChildObjects()) {

                    if (o2.getOriginalFileName().equals("Android")) {


                        PortableDeviceFolderObject storage1 = (PortableDeviceFolderObject) o2;
                        for (PortableDeviceObject o3: storage1.getChildObjects()) {
                            if (o3.getOriginalFileName().equals("data")) {


                                PortableDeviceFolderObject storage2 = (PortableDeviceFolderObject) o3;
                                for (PortableDeviceObject o4: storage2.getChildObjects()) {
                                    if (o4.getOriginalFileName().equals("com.example.planteinvasives")) {


                                        PortableDeviceFolderObject storage3 = (PortableDeviceFolderObject) o4;
                                        for (PortableDeviceObject o5: storage3.getChildObjects()) {

                                            if (o5.getOriginalFileName().equals("files")) {

                                                PortableDeviceFolderObject storage4 = (PortableDeviceFolderObject) o5;
                                                //parcours tout les fichiers du path
                                                for (PortableDeviceObject o6: storage4.getChildObjects()) {

                                                    if(o6.getOriginalFileName().equals("Pictures")){
                                                        PortableDeviceFolderObject storage5 = (PortableDeviceFolderObject) o6;

                                                        for (PortableDeviceObject o7: storage5.getChildObjects()) {

                                                            if(o7.getOriginalFileName().equals("DBsaves")){
                                                           PortableDeviceFolderObject storage6 = (PortableDeviceFolderObject) o7;
                                                                for (PortableDeviceObject o8: storage6.getChildObjects()) {

                                                                    //copie de la base sql
                                                                    PortableDeviceToHostImpl32 host =  new PortableDeviceToHostImpl32();
                                                                    try {
                                                                        System.out.println("copie de "+o8.getOriginalFileName());
                                                                        host.copyFromPortableDeviceToHost(o8.getID(),path,device);
                                                                    } catch (COMException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                    try {
                                                                        //TODO COPIE DU FICHIER DELETE
                                                    if(verrou == 0){
                                                        // si on trouve pas le fichier delete, on le créer
                                                        if (!findDelFile(o5)){
                                                            System.out.println("######ajout du fichier delete dans" + storage4.getName());
                                                            try{
                                                                File file =  new File(pathdel);
                                                                storage4.addAudioObject(
                                                                        file, "string1","string2", new BigInteger("12345"));
                                                                if (file.exists()){
                                                                    System.out.println("verrouiller");
                                                                    verrou = 1;
                                                                }
                                                            }
                                                            catch(Exception e)
                                                            { System.out.println(e);}
                                                        }


                                                    }
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }

                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                   device.close();
                }
            }
        }
    }



    public static void copyDirectory(String sourceDirectoryLocation, String destinationDirectoryLocation)
            throws IOException {
        Files.walk(Paths.get(sourceDirectoryLocation))
                .forEach(source -> {
                    Path destination = Paths.get(destinationDirectoryLocation, source.toString()
                            .substring(sourceDirectoryLocation.length()));
                    try {
                        Files.copy(source, destination);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }


    public boolean findDelFile(PortableDeviceObject o){
        System.out.println("METHODE FIND DEL#####");
        Boolean i  = false;
        String filename = "1.225_172303_8349967972327207504";
        PortableDeviceFolderObject storage = (PortableDeviceFolderObject) o;
        for (PortableDeviceObject portableDeviceObject: storage.getChildObjects()) {
            System.out.println(portableDeviceObject.getOriginalFileName());
            if (portableDeviceObject.getOriginalFileName().equals(filename)){
                System.out.println("####le fichier existe->");
                System.out.println(portableDeviceObject.getOriginalFileName() + "correspond a:"+ filename);
                i = true;
            }
        }
        return i;
    }
}