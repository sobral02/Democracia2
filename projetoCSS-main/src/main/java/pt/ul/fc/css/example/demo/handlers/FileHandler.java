package pt.ul.fc.css.example.demo.handlers;

import java.io.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
public class FileHandler {
  public static File fileToBinaryFile(String pdfFileName, String newName) {
    boolean isTestFile = pdfFileName.contains("TestFiles");
    File pdfFile;
    pdfFile = new File(pdfFileName);
    String directory = isTestFile ? "BinTestFiles" : "BinFiles";
    File newFile = new File(directory + "/" + newName + ".bin");
    try {
      long fileSize = pdfFile.length();
      byte[] pdfBytes = new byte[(int) fileSize];
      // Leitura do pdf para o buffer pdfBuffer
      InputStream fin = new FileInputStream(pdfFile);
      fin.read(pdfBytes);
      // Escrita para o novo ficheiro
      FileOutputStream fileOut = new FileOutputStream(newFile);
      fileOut.write(pdfBytes, 0, (int) fileSize);

      fileOut.flush();
      fileOut.close();
      fin.close();
      System.out.println("PDF file has been converted to binary file.");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return newFile;
  }

  public static void BinaryFileToPdf(File bin) {
    try {

      long fileSize = bin.length();
      byte[] binBytes = new byte[(int) fileSize];

      FileInputStream fin = new FileInputStream(bin);
      fin.read(binBytes);

      File newFile = new File("myPdf.pdf");
      FileOutputStream fileOut = new FileOutputStream(newFile);
      fileOut.write(binBytes);
      fileOut.flush();
      fileOut.close();
      fin.close();
      System.out.println("Bin file has been converted to pdf");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void deleteFile(File file) {
    file.delete();
  }
}
