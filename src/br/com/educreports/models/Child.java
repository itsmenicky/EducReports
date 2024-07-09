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

package br.com.educreports.models;

import lombok.Data;

import java.util.Date;

@Data
public class Child {
    private String name;
    private Date birth_date;
    private String grade;
    private byte[] photo;
    private String contact;
    private String responsible;
    private String address;
    private String teacher_name;
    private Long teacher_id;
    private String status;

    /**
     * Class constructor
     * @param name
     * @param birth_date
     * @param grade
     * @param photo
     * @param contact
     * @param responsible
     * @param address
     * @param teacher_name
     * @param teacher_id
     * @param status
     */
    public Child(String name, Date birth_date, String grade, byte[] photo, String contact, String responsible, String address, String teacher_name, Long teacher_id, String status){
        this.name = name;
        this.birth_date = birth_date;
        this.grade = grade;
        this.photo = photo;
        this.contact = contact;
        this.responsible = responsible;
        this.address = address;
        this.teacher_name = teacher_name;
        this.teacher_id = teacher_id;
        this.status = status;
    }

    /**
     * Method responsible for validate a student
     * @return
     */
    public boolean isValid(){
        return isNameValid() && isBirthDateValid() && isGradeValid()  && isContactValid() && isResponsibleValid() && isAddressValid();
    }

    /**
     * Method responsible for validate a student name
     * @return
     */
    public boolean isNameValid(){
        return name != null && !name.trim().isEmpty();
    }

    /**
     * Method responsible for validate a student birth date
     * @return
     */
    public boolean isBirthDateValid(){
        return birth_date != null;
    }

    /**
     * Method responsible for validate a student class
     * @return
     */
    public boolean isGradeValid(){
        return grade != null && !grade.trim().isEmpty();
    }

    /**
     * Method responsible for validate student contact
     * @return
     */
    public boolean isContactValid(){
        return contact != null && !contact.trim().isEmpty();
    }

    /**
     * Method responsible for validate student responsible
     * @return
     */
    public boolean isResponsibleValid(){
        return responsible != null && !responsible.trim().isEmpty();
    }

    /**
     * Method responsible for validate student address
     * @return
     */
    public boolean isAddressValid(){
        return address != null && !address.trim().isEmpty();
    }

    /**
     * Method responsible for getting validation errors
     * @return
     */
    public String getValidationErrors(){
        StringBuilder errors = new StringBuilder();
        if(!isNameValid()){
            errors.append("Campo nome inválido!");
        }
        if(!isBirthDateValid()){
            errors.append("Campo de data de nascimento inválido!");
        }
        if(!isGradeValid()){
            errors.append("Campo turma inválido!");
        }
        if(!isContactValid()){
            errors.append("Campo de contato inválido!");
        }
        if(!isResponsibleValid()){
            errors.append("Campo de responsável inválido!");
        }
        if(!isAddressValid()){
            errors.append("Campo de endereço inválido!");
        }
        return errors.toString();
    }
}
