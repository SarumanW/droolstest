package lavka.drools.repository;

import lavka.drools.model.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    public User findByLogin(String login);


}
