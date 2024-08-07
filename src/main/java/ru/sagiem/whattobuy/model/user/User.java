package ru.sagiem.whattobuy.model.user;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.sagiem.whattobuy.model.product.Product;
import ru.sagiem.whattobuy.model.shopping.PointShopping;
import ru.sagiem.whattobuy.model.shopping.Shopping;
import ru.sagiem.whattobuy.model.shopping.ShoppingProject;
import ru.sagiem.whattobuy.model.token.Token;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private LocalDateTime createDateTime;


    @ManyToMany(mappedBy = "users")
    private List<FamilyGroup> familyGroups;

    public void removeFamilyGroup(FamilyGroup familyGroup) {
        familyGroups.remove(familyGroup);
        familyGroup.getUsers().remove(this);
    }

    @OneToMany(mappedBy = "userCreator")
    private List<PointShopping> pointShoppingsCreater;

    @OneToMany(mappedBy = "lastModifiedUser")
    private List<PointShopping> pointShoppingsModified;

    @OneToMany(mappedBy = "userCreator")
    private List<Shopping> userCreatorShoppings;

    @OneToMany(mappedBy = "userExecutor")
    private List<Shopping> userExecutorShoppings;

    @OneToMany(mappedBy = "userCreator")
    private List<ShoppingProject> userCreatorShoppingProjects;

    @OneToMany(mappedBy = "userCreator")
    private List<FamilyGroup> userCreatorFamilyGroups;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    @OneToMany(mappedBy = "userCreator")
    private List<Shopping> usersShopingCreator;

    @OneToMany(mappedBy = "userExecutor")
    private List<Shopping> usersShopingExecutor;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
