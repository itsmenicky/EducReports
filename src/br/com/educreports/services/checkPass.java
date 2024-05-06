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

import java.util.Arrays;

/**
 * @version 2.0
 * @author Nick1
 */
public class checkPass {
    public static Boolean check_password_char(char[] fonte){
        String special_char = "!@#$%^&*()-_=+[]{}|;:',.<>?";
        String upper_case = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String low_case = "abcdefghijklmnopqrstuvwxyz";
        Boolean has_special_char = false;
        Boolean has_uppercase = false;
        Boolean has_lowcase = false;
        Boolean has_number = false;
        Boolean pass_ok = false;
        for(char character : fonte){
            if(special_char.contains(String.valueOf(character))){
                has_special_char = true;
            }else if (upper_case.contains(String.valueOf(character))){
                has_uppercase = true;
            }else if (low_case.contains(String.valueOf(character))){
                has_lowcase = true;
            }else if(Character.isDigit(character)){
                has_number = true;
            }
            
            if(has_special_char == true && has_uppercase == true && has_lowcase == true && has_number == true){
                pass_ok = true;
            }
        }
        return pass_ok;
    }
}
