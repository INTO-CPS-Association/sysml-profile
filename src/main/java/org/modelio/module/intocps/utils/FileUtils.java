package org.modelio.module.intocps.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.modelio.api.modelio.diagram.IDiagramHandle;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.module.intocps.impl.INTOCPSModule;
import org.modelio.module.modelermodule.api.IModelerModulePeerModule;
import org.modelio.module.modelermodule.api.IModelerModuleStereotypes;
import org.modelio.vcore.smkernel.mapi.MObject;


public class FileUtils {

    /**
     * Unzip it
     * @param zipFile input zip file
     * @param output zip file output folder
     */
    public static void unZipIt(File zipFile, File folder){

        byte[] buffer = new byte[1024];

        try{

            //create output directory is not exists
            if(!folder.exists()){
                folder.mkdir();
            }

            //get the zip file content
            try(ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))){
                //get the zipped file list entry
                ZipEntry ze = zis.getNextEntry();

                while(ze != null){

                    String fileName = ze.getName();
                    File newFile = new File(folder.getAbsolutePath() + File.separator + fileName);

                    //create all non exists folders
                    //else you will hit FileNotFoundException for compressed folder
                    new File(newFile.getParent()).mkdirs();

                    if (fileName.endsWith("/")){
                        newFile.mkdir();

                    }else{

                        try(FileOutputStream fos = new FileOutputStream(newFile)){

                            int len;
                            while ((len = zis.read(buffer)) > 0) {
                                fos.write(buffer, 0, len);
                            }

                            fos.close();
                        }
                    }
                    ze = zis.getNextEntry();
                }

                zis.closeEntry();
                zis.close();
            }


        }catch(IOException e){
            INTOCPSModule.logService.error(e);
        }
    }

    public static void delete(File f) throws IOException {
        if (f.isDirectory()) {
            for (File c : f.listFiles())
                delete(c);
        }
        if (!f.delete())
            throw new FileNotFoundException("Failed to delete file: " + f);
    }


    public static void saveRelatedDiag(ModelElement element, String filePath, String moduleName, String diagramType) {

        for (Dependency depends : element.getDependsOnDependency()){
            if (depends.isStereotyped(IModelerModulePeerModule.MODULE_NAME, IModelerModuleStereotypes.RELATED_DIAGRAM)){
                ModelElement target = depends.getDependsOn();

                if (target.isStereotyped(moduleName, diagramType)){
                    try(IDiagramHandle diagramHandle = INTOCPSModule.getInstance().getModuleContext().getModelioServices().getDiagramService().getDiagramHandle((AbstractDiagram) target)){

                        File image = new File(filePath);
                        if (!image.exists()){
                            image.getParentFile().mkdirs();
                            try {
                                image.createNewFile();
                            } catch (IOException e) {
                                INTOCPSModule.logService.error(e);
                            }
                        }
                        diagramHandle.saveInFile("PNG", filePath , 2);
                    }
                }
            }
        }
    }

    public static String findFilePath(String rootFilePath, final MObject object){

        File rootFile = new File(rootFilePath);
        String filename = object.getUuid().toString() + ".exml";

        String absolutePath = FileUtils.findFilePath(rootFile, filename);

        MObject current = object;

        while (absolutePath.equals("")){
            current = current.getCompositionOwner();
            filename = current.getUuid().toString() + ".exml";
            absolutePath = findFilePath(rootFile, filename);
        }

        return absolutePath;
    }

    public static boolean fileExist(String rootFilePath, MObject object){

        File rootFile = new File(rootFilePath);
        String filename = object.getUuid().toString() + ".exml";

        String absolutePath = FileUtils.findFilePath(rootFile, filename);

        return (!absolutePath.equals(""));
    }

    private static String findFilePath(File rootFile, String filename){

        String path = "";

        if(rootFile.isDirectory()){
            List<File> files = Arrays.asList(rootFile.listFiles());
            Iterator<File> fileIterator = files.iterator();
            while(fileIterator.hasNext() &&  path.equals("")){
                path = findFilePath(fileIterator.next(), filename);
            }
        } else {

            if (rootFile.getName().equals(filename)) {
                path = rootFile.getAbsolutePath();
            }

        }

        return path;
    }

    public static File findFile(File file, String filename){

        File path = null;

        if(file.isDirectory()){
            List<File> files = Arrays.asList(file.listFiles());
            Iterator<File> fileIterator = files.iterator();
            while(fileIterator.hasNext() &&  (path == null)){
                path = findFile(fileIterator.next(), filename);
            }
        } else {

            if (file.getName().equals(filename)) {
                path = file;
            }

        }

        return path;
    }


    public static String findDir(File parent, String dirname){

        String path = "";

        if(parent.isDirectory()){
            if (parent.getName().equals(dirname)) {
                path = parent.getAbsolutePath();
            }

            List<File> files = Arrays.asList(parent.listFiles());
            Iterator<File> fileIterator = files.iterator();
            while(fileIterator.hasNext() &&  path.equals("")){
                path = findDir(fileIterator.next(), dirname);
            }
        }

        return path;

    }


}
