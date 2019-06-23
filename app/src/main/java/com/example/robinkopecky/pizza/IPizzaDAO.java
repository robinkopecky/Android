package com.example.robinkopecky.pizza;

import java.util.List;

public interface IPizzaDAO {

    void insertPizza(Pizza pizza);
    void updatePizza(Pizza pizza);
    void deletePizza(Pizza pizza);
    void insertBag(Pizza pizza);
    void deleteBag(Pizza pizza);
    void emptyBag();

    List<Pizza> getPizzaBag();

    List<Pizza> getAllPizza();

    Pizza getPizzaByID(long id);

    void markAsFavorite(long id, boolean favorite);

    List<Pizza> getFavoritePizza();

}
