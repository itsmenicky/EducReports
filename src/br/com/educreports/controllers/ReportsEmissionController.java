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
import br.com.educreports.screens.ReportsEmissionView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author itsmenicky
 * @version 3.0
 */
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
