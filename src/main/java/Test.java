import jmtp.*;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Test {


    public static void main(String[] args) {


        PortableDeviceManager manager = new PortableDeviceManager();
        PortableDevice device = manager.getDevices()[0];

        device.open();




        System.out.println("---------------");
        System.out.println("Envoi de fichier vers la tablette");

        // Iterate over deviceObjects
        for (
                PortableDeviceObject object: device.getRootObjects()) {
            // If the object is a storage object
            if (object instanceof PortableDeviceStorageObject) {
                //racine

                PortableDeviceStorageObject storage = (PortableDeviceStorageObject) object;

                System.out.println("ca marche dossier," + storage.getDescription());


                for (PortableDeviceObject o2: storage.getChildObjects()) {

                    if (o2.getOriginalFileName().equals("Android")) {
                        System.out.println("/dans Android");

                        PortableDeviceFolderObject storage1 = (PortableDeviceFolderObject) o2;
                        for (PortableDeviceObject o3: storage1.getChildObjects()) {
                            if (o3.getOriginalFileName().equals("data")) {
                                System.out.println("/data");

                                PortableDeviceFolderObject storage2 = (PortableDeviceFolderObject) o3;
                                for (PortableDeviceObject o4: storage2.getChildObjects()) {
                                    if (o4.getOriginalFileName().equals("com.example.planteinvasives")) {
                                        System.out.println("/com.example.planteinvasives");

                                        PortableDeviceFolderObject storage3 = (PortableDeviceFolderObject) o4;
                                        for (PortableDeviceObject o5: storage3.getChildObjects()) {
                                            if (o5.getOriginalFileName().equals("files")) {
                                                System.out.println("/files");

                                                PortableDeviceFolderObject storage4 = (PortableDeviceFolderObject) o5;
                                                for (PortableDeviceObject o6: storage4.getChildObjects()) {
                                                    if (o6.getOriginalFileName().equals("Pictures")) {
                                                        System.out.println("/Pictures");

                                                        //copie des images
                                                        PortableDeviceFolderObject storage5 = (PortableDeviceFolderObject) o6;
                                                        System.out.println("///////////");





                                                    }

                                                }

                                            }


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