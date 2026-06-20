package com.liquid.liquidpedia.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "admins")
@PrimaryKeyJoinColumn(name = "id_admin")
public class Admin extends User {

    @Column(name = "name_admin")
    private String nameAdmin;

    public Admin() {}

    public String getNameAdmin() { return nameAdmin; }
    public void setNameAdmin(String nameAdmin) { this.nameAdmin = nameAdmin; }
}
