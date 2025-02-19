package net.esliceu.Rest_Api_Forum.Entities;
import jakarta.persistence.*;

@Entity
@SequenceGenerator(name = "global_gen", sequenceName = "global_seq", allocationSize = 1)
@org.hibernate.annotations.SQLInsert(sql = "CREATE SEQUENCE IF NOT EXISTS global_seq START WITH 1 INCREMENT BY 1")
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_gen")
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
