package org.zamzam.customerservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
@Document("consumers")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Consumer {
    @Id
    private String id;
    @Field(name = "name")
    private String name;
    @Field(name = "consumerCategory")
    private ConsumerCategory consumerCategory;
    @Field(name = "passport")
    @Indexed(unique = true)
    private String passport;
    @Field(name = "enterDate")
    private Date enterDate;
    @Field(name = "exitDate")
    private Date exitDate;
    @Field(name = "quota")
    private Integer quota;

    public Consumer(String name, String passport) {
        this.name = name;
        this.passport = passport;
    }
}
