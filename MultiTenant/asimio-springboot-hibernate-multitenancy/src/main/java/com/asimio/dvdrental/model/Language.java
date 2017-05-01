package com.asimio.dvdrental.model;
// Generated Jul 21, 2016 11:52:14 PM by Hibernate Tools 3.2.2.GA


import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Language generated by hbm2java
 */
@Entity
@Table(name="language"
    ,schema="public"
)
public class Language  implements java.io.Serializable {


     private int languageId;
     private String name;
     private Date lastUpdate;
     private Set<Film> films = new HashSet<Film>(0);

    public Language() {
    }

	
    public Language(int languageId, String name, Date lastUpdate) {
        this.languageId = languageId;
        this.name = name;
        this.lastUpdate = lastUpdate;
    }
    public Language(int languageId, String name, Date lastUpdate, Set<Film> films) {
       this.languageId = languageId;
       this.name = name;
       this.lastUpdate = lastUpdate;
       this.films = films;
    }
   
     @Id 
    
    @Column(name="language_id", unique=true, nullable=false)
    public int getLanguageId() {
        return this.languageId;
    }
    
    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }
    
    @Column(name="name", nullable=false, length=20)
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="last_update", nullable=false, length=29)
    public Date getLastUpdate() {
        return this.lastUpdate;
    }
    
    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="language")
    public Set<Film> getFilms() {
        return this.films;
    }
    
    public void setFilms(Set<Film> films) {
        this.films = films;
    }




}


