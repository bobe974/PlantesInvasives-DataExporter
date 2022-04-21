package Transfert;

import be.derycke.pieter.com.COMException;
import jmtp.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PhoneToPc {

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
                                                System.out.println("/files");

                                                PortableDeviceFolderObject storage4 = (PortableDeviceFolderObject) o5;
                                                //parcours tout les fichiers du path
                                                for (PortableDeviceObject o6: storage4.getChildObjects()) {
                                                    if(o6.getOriginalFileName().equals("Pictures")){
                                                        PortableDeviceFolderObject storage5 = (PortableDeviceFolderObject) o6;

                                                        for (PortableDeviceObject o7: storage5.getChildObjects()) {
                                                            System.out.println(o7.getOriginalFileName());

                                                            //copie de toutes les images sur le bureau
                                                            //TODO modif la route
                                                            PortableDeviceToHostImpl32 host =  new PortableDeviceToHostImpl32();
                                                            try {
                                                                System.out.println("copie de "+o7.getOriginalFileName());
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

                                                                    //copie de toutes les images sur le bureau
                                                                    PortableDeviceToHostImpl32 host =  new PortableDeviceToHostImpl32();
                                                                    try {
                                                                        System.out.println("copie de "+o8.getOriginalFileName());
                                                                        host.copyFromPortableDeviceToHost(o8.getID(),path,device);
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
}

//  PortableDeviceFolderObject host =  (PortableDeviceFolderObject) o3;
//                                try {
//                                    File f = new File( "c://sqlite//db//copagaz_mobile_desenv.db");
//                                    System.out.println("Copiando arquivo .db para o Tablet");
//                                    host.addAudioObject(f, "0", "1", new BigInteger("12345"));
//                                    System.out.println("Arquivo copiado");
//
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }