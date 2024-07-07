package br.com.educreports.controllers;

import br.com.educreports.dao.ChildDAO;
import br.com.educreports.screens.ReportsEmissionView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportsEmissionController {
    private ChildDAO childDAO;
    private ReportsEmissionView view;

    /**
     * Class constructor
     * @param view
     */
    public ReportsEmissionController(ReportsEmissionView view){
        childDAO = new ChildDAO();
        this.view = view;
    }

    /**
     * Method responsible for catching child photo and converting in icon
     * @param RA
     * @return
     * @throws SQLException
     */
    public ImageIcon catch_child_photo(String RA) throws SQLException {
        ResultSet child_photo = childDAO.search_child_photo(RA);
        if(child_photo.next()) {
            Blob blob = child_photo.getBlob("child_photo");
            byte[] img = blob.getBytes(1, (int) blob.length());
            try {
                BufferedImage image = ImageIO.read(new ByteArrayInputStream(img));
                ImageIcon icon = new ImageIcon(image);
                return icon;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
        return null;
    }
}
