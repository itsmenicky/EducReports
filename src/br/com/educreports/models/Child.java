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
}
