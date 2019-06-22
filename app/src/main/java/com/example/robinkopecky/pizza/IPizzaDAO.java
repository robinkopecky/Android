package com.example.robinkopecky.pizza;

import java.util.List;

public interface IPizzaDAO {

    void insertPizza(Pizza pizza);
    void updatePizza(Pizza pizza);
    void deletePizza(Pizza pizza);

    List<Pizza> getAllPizza();

    Pizza getPizzaByID(long id);

    void markAsFavorite(long id, boolean favorite);

    List<Pizza> getFavoritePizza();

}
