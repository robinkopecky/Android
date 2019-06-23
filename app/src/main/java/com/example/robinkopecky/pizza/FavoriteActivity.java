package com.example.robinkopecky.pizza;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class FavoriteActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PizzaDAOImplementation pizzaDAOImplementation;

    private List<Pizza> pizzaList = new ArrayList<>();
    private Pizza addPizza;

    private PizzaAdapter pizzaAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_favorite));

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
                startActivity(BagActivity.getIntent(FavoriteActivity.this, null));
            }
        });



        recyclerView = findViewById(R.id.recycler_view);
        pizzaDAOImplementation = new PizzaDAOImplementation(this);
    }


    public class PizzaAdapter extends RecyclerView.Adapter<PizzaAdapter.PizzaViewHolder>{

        @NonNull
        @Override
        public PizzaAdapter.PizzaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(FavoriteActivity.this).inflate(R.layout.row_pizza_list, viewGroup,false);
            return new PizzaAdapter.PizzaViewHolder(itemView);
        }



        @Override
        public void onBindViewHolder(@NonNull final PizzaAdapter.PizzaViewHolder pizzaViewHolder, final int i) {

            final Pizza pizza = pizzaList.get(i);

            pizzaViewHolder.pizzaName.setText(pizza.getName());
            pizzaViewHolder.pizzaDescription.setText(pizza.getDescription());
            pizzaViewHolder.pizzaPrice.setText(pizza.getPrice()+ " Kč");

            pizzaViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int truePosition = pizzaViewHolder.getAdapterPosition();
                    startActivity(PizzaDetailActivity.getIntent(FavoriteActivity.this, pizzaList.get(truePosition).getId()));

                }
            });
            pizzaViewHolder.buyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    addPizza = new Pizza();
                    addPizza.setName(pizza.getName());
                    addPizza.setDescription(pizza.getDescription());
                    addPizza.setPrice(pizza.getPrice());


                    pizzaDAOImplementation.insertBag(addPizza);


                    Toast toast = Toast.makeText(FavoriteActivity.this,getResources().getString(R.string.adding),Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
                    toast.show();
                }
            });


            pizzaViewHolder.pizzaLike.setChecked(pizza.isFavorite());

            pizzaViewHolder.pizzaLike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    pizza.setFavorite(isChecked);
                    pizzaDAOImplementation.updatePizza(pizza);
                    /*
                    pizzaList.get(pizzaViewHolder.getAdapterPosition()).setFavorite(isChecked);

                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            notifyItemChanged(pizzaViewHolder.getAdapterPosition());
                        }
                    });*/

                }
            });






        }

        @Override
        public int getItemCount() {
            return pizzaList.size();
        }

        public class PizzaViewHolder extends RecyclerView.ViewHolder{

            public TextView pizzaName;
            public TextView pizzaDescription;
            public TextView pizzaPrice;
            public CheckBox pizzaLike;
            public Button buyButton;

            public PizzaViewHolder(@NonNull View itemView) {
                super(itemView);
                buyButton = itemView.findViewById(R.id.buy);
                pizzaLike = itemView.findViewById(R.id.fav);
                pizzaName = itemView.findViewById(R.id.pizza_name);
                pizzaDescription = itemView.findViewById(R.id.pizza_description);
                pizzaPrice = itemView.findViewById(R.id.pizza_price);

            }
        }
    }

    private void setList(){
        pizzaList.clear();
        pizzaList.addAll(pizzaDAOImplementation.getFavoritePizza());

        if (pizzaAdapter == null){
            pizzaAdapter = new PizzaAdapter();
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(pizzaAdapter);

        } else {
            pizzaAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setList();
    }







    public static Intent getIntent(MainActivity context, Long id) {
        Intent intent = new Intent(context,
                FavoriteActivity.class);
        if (id != null){
            intent.putExtra(IntentConstants.ID, id);
        }
        return intent;
    }







}
