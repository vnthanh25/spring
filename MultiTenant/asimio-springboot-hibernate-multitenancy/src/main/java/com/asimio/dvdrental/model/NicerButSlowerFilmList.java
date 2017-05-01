package com.asimio.dvdrental.model;
// Generated Jul 21, 2016 11:52:14 PM by Hibernate Tools 3.2.2.GA


import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * NicerButSlowerFilmList generated by hbm2java
 */
@Entity
@Table(name="nicer_but_slower_film_list"
    ,schema="public"
)
public class NicerButSlowerFilmList  implements java.io.Serializable {


     private NicerButSlowerFilmListId id;

    public NicerButSlowerFilmList() {
    }

    public NicerButSlowerFilmList(NicerButSlowerFilmListId id) {
       this.id = id;
    }
   
     @EmbeddedId
    
    @AttributeOverrides( {
        @AttributeOverride(name="fid", column=@Column(name="fid") ), 
        @AttributeOverride(name="title", column=@Column(name="title") ), 
        @AttributeOverride(name="description", column=@Column(name="description") ), 
        @AttributeOverride(name="category", column=@Column(name="category", length=25) ), 
        @AttributeOverride(name="price", column=@Column(name="price", precision=4) ), 
        @AttributeOverride(name="length", column=@Column(name="length") ), 
        @AttributeOverride(name="rating", column=@Column(name="rating") ), 
        @AttributeOverride(name="actors", column=@Column(name="actors") ) } )
    public NicerButSlowerFilmListId getId() {
        return this.id;
    }
    
    public void setId(NicerButSlowerFilmListId id) {
        this.id = id;
    }




}


