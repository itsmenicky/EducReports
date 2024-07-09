/*
 * Copyright (C) 2024 Nickolas Martins
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package br.com.educreports.controllers;

import br.com.educreports.dao.ChildDAO;
import br.com.educreports.models.Child;
import br.com.educreports.screens.StudentView;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author itsmenicky
 * @version 2.0
 */
public class StudentController {
    private StudentView view;
    private ChildDAO childDAO;

    //Instantiating object for byte stream
    private FileInputStream fis;

    //global variable to store the image size (bytes)
    private int size;

    public StudentController(StudentView view){
        this.view = view;
        this.childDAO = new ChildDAO();
    }

    /**
     * Function responsible for uploading photo on the system, and setting the
     * photo on the interface
     *
     * @return
     */
    public File photoUpload() {
        JFileChooser fileExplorer = new JFileChooser();
        fileExplorer.setDialogTitle("Selecionar arquivo");
        fileExplorer.setFileFilter(new FileNameExtensionFilter("Arquivo de imagens (*.PNG, *.JPG, *.JPEG)", "png", "jpg", "jpeg"));
        int result = fileExplorer.showOpenDialog(view);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                fis = new FileInputStream(fileExplorer.getSelectedFile());
                size = (int) fileExplorer.getSelectedFile().length();
                return fileExplorer.getSelectedFile();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
        return null;
    }

    /**
     * Function responsible for trasnform blob in icon
     * @param blob
     * @param photo_field
     * @return
     * @throws SQLException
     */
    public Icon blob_to_icon(Blob blob, JLabel photo_field) throws SQLException {
        byte[] img = blob.getBytes(1, (int) blob.length());
        BufferedImage imagem = null;
        try {
            imagem = ImageIO.read(new ByteArrayInputStream(img));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        ImageIcon icone = new ImageIcon(imagem);
        Icon photo = new ImageIcon(icone.getImage().getScaledInstance(photo_field.getWidth(), photo_field.getHeight(), Image.SCALE_SMOOTH));
        return photo;
    }

    /**
     * Function responsible for validating student and save in database
     * @param student
     */
    public void add_student(Child student){
        if(student.isValid()){
            childDAO.save(student);
        } else{
            JOptionPane.showMessageDialog(null, student.getValidationErrors());
        }
    }

    /**
     * Function responsible for validating student and update data
     * @param student
     * @param RA
     */
    public void edit_student(Child student, String RA, Icon photo) throws IOException {
        if(student.isValid()){
            childDAO.update(student, RA, iconToBytes(photo, "png"));
        }else{
            JOptionPane.showMessageDialog(null, "ERRO AO ATUALIZAR OS DADOS: " + student.getValidationErrors());
        }
    }

    /**
     * Function responsible for parsing icon into bufferedImage
     * @param icon
     * @return
     */
    public BufferedImage iconToBufferedImage(Icon icon) {
        if (icon instanceof ImageIcon) {
            return (BufferedImage)((ImageIcon)icon).getImage();
        }
        BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        icon.paintIcon(null, g2d, 0, 0);
        g2d.dispose();
        return bufferedImage;
    }

    /**
     * Function responsible for parsing bufferedImage to bytes
     * @param bufferedImage
     * @param format
     * @return
     */
    public byte[] bufferedImageToBytes(BufferedImage bufferedImage, String format) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, format, baos);
            baos.flush();
            byte[] imageBytes = baos.toByteArray();
            baos.close();
            return imageBytes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Function responsible for parsing icon into bytes
     * @param icon
     * @param format
     * @return
     */
    public byte[] iconToBytes(Icon icon, String format) throws IOException {
        ImageIcon image_icon = (ImageIcon) icon;
        Image image = image_icon.getImage();
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, format, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return imageBytes;
    }

    /**
     * Function responsible for parsing string into date sql
     * @param string_date
     * @return
     * @throws ParseException
     */
    public Date string_to_date(String string_date) throws ParseException {
        //Criando objeto SimpleDateFormat para o formato que a gente deseja
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        //Converte a string para um objeto java.util.date
        java.util.Date dateUtil = format.parse(string_date);
        //Converte o objeto do java.util.date para java.sql.date
        java.sql.Date dateSQL = new java.sql.Date(dateUtil.getTime());
        return dateSQL;
    }
}
