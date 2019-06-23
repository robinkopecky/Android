package com.example.robinkopecky.pizza;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PizzaDetailActivity extends AppCompatActivity {

    private long id;
    private TextView pizzaName;
    private TextView pizzaDescription;
    private TextView pizzaPrice;
    private CheckBox pizzaLike;
    private ImageView pizzaImage;
    private List<Pizza> pizzaList = new ArrayList<>();

    private Pizza pizza;

    private PizzaDAOImplementation pizzaDAOImplementation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pizza_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        FloatingActionButton bag = (FloatingActionButton) findViewById(R.id.bag);
        bag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(BagActivity.getIntent(PizzaDetailActivity.this, null));
            }
        });

        id = getIntent().getLongExtra(IntentConstants.ID,-1);
        pizzaName = findViewById(R.id.pizza_name_detail);
        pizzaDescription = findViewById(R.id.pizza_description_detail);
        pizzaPrice = findViewById(R.id.pizza_price_detail);
        pizzaLike = findViewById(R.id.fav_detail);


        pizzaDAOImplementation = new PizzaDAOImplementation(this);
        pizzaList = pizzaDAOImplementation.getAllPizza();

        pizza = pizzaDAOImplementation.getPizzaByID(id);
        //pizzaImage.setImageDrawable(R.drawable.pizza);


        pizzaLike.setChecked(pizza.isFavorite());



        pizzaName.setText(pizza.getName());
        if (pizza.getDescription() != null && !pizza.getDescription().equals("")){
            pizzaDescription.setText(pizza.getDescription());
        } else {
            pizzaDescription.setText(getResources().getString(R.string.noDescription));
        }
        if (pizza.getPrice() != null && !pizza.getPrice().equals("")){
            pizzaPrice.setText(pizza.getPrice()+ " Kč");
        } else {
            pizzaPrice.setText(getResources().getString(R.string.noPrice));
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        pizzaLike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pizza.setFavorite(isChecked);
                pizzaDAOImplementation.updatePizza(pizza);

            }
        });
    }

    public static Intent getIntent(MainActivity context, Long id) {
        Intent intent = new Intent(context,
                PizzaDetailActivity.class);
        if (id != null){
            intent.putExtra(IntentConstants.ID, id);
        }
        return intent;
    }
    public static Intent getIntent(FavoriteActivity context, Long id) {
        Intent intent = new Intent(context,
                PizzaDetailActivity.class);
        if (id != null){
            intent.putExtra(IntentConstants.ID, id);
        }
        return intent;
    }

}
