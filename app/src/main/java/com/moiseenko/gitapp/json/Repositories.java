package com.moiseenko.gitapp.json;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Виктор on 08.01.2016.
 */
public class Repositories implements Serializable {

    public Repositories(List<Repos> reposes) {
        this.reposes = reposes;
    }

    public List<Repos> reposes;

    public List<Repos> getReposes() {
        return reposes;
    }

    public void setReposes(List<Repos> reposes) {
        this.reposes = reposes;
    }

    @DatabaseTable(tableName = "Repos")
    public static class Repos implements Serializable{

        public Repos() {
        }

        @DatabaseField(canBeNull = false, dataType = DataType.STRING, id = true)
        String id;

        @DatabaseField(canBeNull = false, dataType = DataType.STRING)
        String name;

        @DatabaseField(canBeNull = false, dataType = DataType.STRING)
        String full_name;

        Owner owner;

        @DatabaseField(canBeNull = false, dataType = DataType.STRING)
        String size;

        @DatabaseField(dataType = DataType.STRING)
        String language;

        @DatabaseField(canBeNull = true, dataType = DataType.STRING)
        String description;

        @DatabaseField(canBeNull = false, dataType = DataType.STRING)
        String created_at;
        @DatabaseField(canBeNull = false, dataType = DataType.STRING)
        String updated_at;

        public Owner getOwner() {
            return owner;
        }

        public void setOwner(Owner owner) {
            this.owner = owner;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }


        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFull_name() {
            return full_name;
        }

        public void setFull_name(String full_name) {
            this.full_name = full_name;
        }
    }

}
