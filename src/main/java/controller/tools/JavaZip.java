package controller.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class JavaZip {

    private List<String> filesListInDir;
    private static final int BUFFER_SIZE = 4096;

    /**
     * This method zips the directory.
     *
     * @param dir
     * @param zipDirName
     */
    public boolean zipDirectory(String dir, String zipDirName) {
        try {
            File fileDir = new File(dir);
            filesListInDir = new ArrayList<String>();
            populateFilesList(fileDir);
            
            FileOutputStream fos = new FileOutputStream(zipDirName);
            ZipOutputStream zos = new ZipOutputStream(fos);
            
            for (String filePath : filesListInDir) {
                System.out.println("Zipping " + filePath);
                //for ZipEntry we need to keep only relative file path, so we used substring on absolute path
                ZipEntry ze = new ZipEntry(fileDir.getName() + "/" + filePath.substring(fileDir.getAbsolutePath().length() + 1, filePath.length()));
                
                zos.putNextEntry(ze);
                //read the file and write to ZipOutputStream
                FileInputStream fis = new FileInputStream(filePath);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                zos.closeEntry();
                fis.close();
            }
            zos.close();
            fos.close();
            
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            
            return false;
        }
    }

    public static final void zipDirectory2(File directory, File zip) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zip));
        zip(directory, directory, zos);
        zos.close();
    }
 
    private static final void zip(File directory, File base,
            ZipOutputStream zos) throws IOException {
        File[] files = directory.listFiles();
 
        byte[] buffer = new byte[8192];
        int read = 0;
 
        for (int i = 0, n = files.length; i < n; i++) {
            if (files[i].isDirectory()) {
                zip(files[i], base, zos);
            } else {
                FileInputStream in = new FileInputStream(files[i]);
                ZipEntry entry = new ZipEntry(files[i].getPath().substring(
                        base.getPath().length() + 1));
                zos.putNextEntry(entry);
                while (-1 != (read = in.read(buffer))) {
                    zos.write(buffer, 0, read);
                }
                in.close();
            }
        }
    }
    
    
    /**
     * This method compresses the single file to zip format.
     *
     * @param file
     * @param zipFileName
     */
    public boolean zipSingleFile(File file, String zipFileName) {
        try {
            //create ZipOutputStream to write to the zip file
            FileOutputStream fos = new FileOutputStream(zipFileName);
            ZipOutputStream zos = new ZipOutputStream(fos);
            //add a new Zip Entry to the ZipOutputStream
            ZipEntry ze = new ZipEntry(file.getName());
            zos.putNextEntry(ze);
            //read the file and write to ZipOutputStream
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }

            //Close the zip entry to write to zip file
            zos.closeEntry();
            //Close resources
            zos.close();
            fis.close();
            fos.close();
            System.out.println(file.getCanonicalPath() + " is zipped to " + zipFileName);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * Extract zipfile to outdir with complete directory structure.
     *
     * @param fileName Input .zip file
     * @param filePath Output directory
     */
//    public boolean unZipFile(String sourceFile, String targetFile) {
//        File zipfile = new File(sourceFile);
//        File outdir = new File(targetFile);
//        try {
//            ZipInputStream zin = new ZipInputStream(new FileInputStream(zipfile));
//            ZipEntry entry;
//            String name, dir;
//            while ((entry = zin.getNextEntry()) != null) {
//                name = entry.getName();
//                if (entry.isDirectory()) {
//                    mkdirs(outdir, name);
//                    continue;
//                }
//
//                dir = dirpart(name);
//                if (dir != null) {
//                    mkdirs(outdir, dir);
//                }
//
//                extractFile(zin, outdir, name);
//            }
//            zin.close();
//            
//            return true;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
	public boolean unZipFile(String fileName, String filePath) {
		try {
			int BUFFER = 4096;
			ZipFile zipFile = new ZipFile(fileName);
			Enumeration emu = zipFile.entries();

			while(emu.hasMoreElements()){
				ZipEntry entry = (ZipEntry)emu.nextElement();
				if (entry.isDirectory())
				{
					new File(filePath + File.separator + entry.getName()).mkdirs();
					continue;
				}
				BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
				File file = new File(filePath + File.separator + entry.getName());
				File parent = file.getParentFile();
				if(parent != null && (!parent.exists())){
					parent.mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(file);
				BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER);           

				int count;
				byte data[] = new byte[BUFFER];
				while ((count = bis.read(data, 0, BUFFER)) != -1)
				{
					bos.write(data, 0, count);
				}
				bos.flush();
				bos.close();
				bis.close();
			}
			zipFile.close();
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
	
    /* ====================== Helper Methods ====================== */
    /**
     * This method populates all the files in a directory to a List
     *
     * @param dir
     * @throws IOException
     */
    private void populateFilesList(File dir) throws IOException {
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                filesListInDir.add(file.getAbsolutePath());
            } else {
                populateFilesList(file);
            }
        }
    }

    private void extractFile(ZipInputStream in, File outdir, String name) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(outdir, name)));
        int count = -1;
        while ((count = in.read(buffer)) != -1) {
            out.write(buffer, 0, count);
        }
        out.close();
    }

    private void mkdirs(File outdir, String path) {
        File d = new File(outdir, path);
        if (!d.exists()) {
            d.mkdirs();
        }
    }

    private String dirpart(String name) {
        int s = name.lastIndexOf(File.separatorChar);
        return s == -1 ? null : name.substring(0, s);
    }

}
