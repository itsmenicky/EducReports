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

package br.com.educreports.services;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @version 3.0
 * @author itsmenicky
 */
public class dateParser {

    /**
     * Function responsible for parsing string to java.util.Date
     * @param dateString
     * @return
     */
    public static Date to_date(String dateString){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try{
            Date date = dateFormat.parse(dateString);
            return date;
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "ERRO: Falha ao capturar data");
        }
        return null;
    }
}
