package com.stkizema.test8telemarketing.db.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity(
        indexes = {
                @Index(value = "idCategory", unique = true)
        })
public class Category {

        @Id
        private Long idCategory;

        @NotNull
        @Property(nameInDb = "name")
        private String name;

        @NotNull
        @Property(nameInDb = "id")
        private Integer id;

        @Generated(hash = 793011451)
        public Category(Long idCategory, @NotNull String name, @NotNull Integer id) {
            this.idCategory = idCategory;
            this.name = name;
            this.id = id;
        }

        @Generated(hash = 1150634039)
        public Category() {
        }

        public Long getIdCategory() {
            return this.idCategory;
        }

        public void setIdCategory(Long idCategory) {
            this.idCategory = idCategory;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getId() {
            return this.id;
        }

        public void setId(Integer id) {
            this.id = id;
        }
}
