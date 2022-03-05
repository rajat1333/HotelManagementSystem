package spartanbots.v01.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document(collection ="hotels")
public class Hotel {

    @Id
    private String id;
    private String name;
    private String city;

    public Hotel(String id, String name, String city) {
        this.id = id;
        this.name = name;
        this.city = city;
    }

}