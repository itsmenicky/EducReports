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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @version 3.0
 * @author itsmenicky
 */
public class passwordCrypt {

   /**
    * Function responsible for encrypt password
    * @param password
    * @return
    * @throws NoSuchAlgorithmException 
    */ 
    public static String passCrypt(String password) throws NoSuchAlgorithmException {
        MessageDigest algorithm = MessageDigest.getInstance("MD5");
        byte[] messageDigest = algorithm.digest(password.getBytes(StandardCharsets.UTF_8));
        String encrypted_password = bytesToHex(messageDigest);
        return encrypted_password;
    }

    /**
     * Function responsible for converting bytes to Hex, returning password as a String
     * @param bytes
     * @return 
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

}
