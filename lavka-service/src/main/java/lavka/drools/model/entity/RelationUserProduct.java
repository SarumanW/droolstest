package lavka.drools.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@SequenceGenerator(name = "user_prod_seq", allocationSize = 1)
public class RelationUserProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_prod_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    private boolean shown;

    private boolean liked;

    public RelationUserProduct(User user, Product product) {
        this.user = user;
        this.product = product;
        this.shown = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isShown() {
        return shown;
    }

    public void setShown(boolean shown) {
        this.shown = shown;
    }

    public RelationUserProduct(Product product, User user, boolean shown) {
        this.product = product;
        this.user = user;
        this.shown = shown;
    }

    public RelationUserProduct() {
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}
