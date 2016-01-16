package com.moiseenko.gitapp.json;

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

    public class Repos implements Serializable{
        String id;
        String name;
        String full_name;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        String description;
        String created_at;

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
