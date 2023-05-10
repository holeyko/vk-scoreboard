package com.holeyko.vkscoreboard.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserStatistic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    @ToString.Exclude
    private User user;

    @OneToMany(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE},
            mappedBy = "userStatistic"
    )
    private List<UserCategoryStatistic> categoryStatistics = new ArrayList<>();

    public void addCategoryStatistic(UserCategoryStatistic userCategoryStatistic) {
        categoryStatistics.add(userCategoryStatistic);
    }
}
