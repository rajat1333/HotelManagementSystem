package spartanbots.v01.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * @author Rajat Masurkar
 */
@Document(collection ="bill")
public class Bill {
    @Id
    @Column(name = "id")
    private int id;
}
